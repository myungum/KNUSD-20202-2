package com.example.sd2020;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sd2020.Diary.Article;
import com.example.sd2020.Diary.ArticleAdapter;
import com.example.sd2020.Diary.WritediaryActivity;
import com.example.sd2020.Schedule.Work;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class Fragmentp2 extends Fragment{
    SharedPreferences sf;
    private Context mContext;
    private ArticleAdapter mAdapter;

    private Button button;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase Database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference = Database.getReference();
    private FirebaseAuth mAuth= FirebaseAuth.getInstance();

    String userEmail;
    private final int ACTIVITY_CODE = 1;

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == RESULT_OK) {
            if (requestCode == ACTIVITY_CODE) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(this).attach(this).commit();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_fragment_p2, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sf = getActivity().getSharedPreferences("saved",0);
        userEmail = sf.getString("id", "");

        button = (Button)getView().findViewById(R.id.button_write);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                go_writediary(view);
            }
        });

        /*일기 리스트*/
        mContext = getContext();
        final ArrayList<Article> articleList = new ArrayList<>();
        recyclerView = (RecyclerView)getView().findViewById(R.id.diary_Recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);

        mDatabaseReference.child("Diary").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Article article = postSnapshot.getValue(Article.class);
                    if(article.getuserEmail().equals(userEmail)) {
                        articleList.add(article);
                    }
                }
                mAdapter = new ArticleAdapter(articleList);
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("TAG: ", "Failed to read value", databaseError.toException());
            }
        });

        mAdapter = new ArticleAdapter(articleList);
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            public void onClick(View view, int position) {
                Article article = articleList.get(position);
                Intent intent;
                intent = new Intent(getActivity(), WritediaryActivity.class);
                intent.putExtra("it_id",article.getid());
                intent.putExtra("userEmail",userEmail);
                startActivityForResult(intent, ACTIVITY_CODE);
                return;
            }
            @Override
            public void onLongClick(View view, int position) {
                return;
            }
        }));
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private Fragmentp2.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final Fragmentp2.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }

    public void go_writediary(View v){
        Intent intent = new Intent(getActivity(), WritediaryActivity.class);
        intent.putExtra("it_id","null");
        intent.putExtra("userEmail",userEmail);
        startActivityForResult(intent, ACTIVITY_CODE);
    }
}