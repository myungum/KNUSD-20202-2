package com.example.sd2020.Diary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sd2020.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private ArrayList<Article> arrayList;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    /*// 커스텀 리스너 인터페이스
    public interface OnItemClickListener
    {
        void onItemClick(View v, int pos);
    }

    // 리스너 객체 참조를 저장하는 변수
    private OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.mListener = listener;
    }*/



    public ArticleAdapter(ArrayList<Article> List) {
        this.arrayList = List;
    }

    @Override
    public ArticleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /*Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View view = inflater.inflate(R.layout.recyclerview_item, parent, false);*/
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemview);
        return viewHolder;
    }


    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.

    @Override
    public void onBindViewHolder(com.example.sd2020.Diary.ArticleAdapter.ViewHolder viewHolder, int position) {
        Article article = arrayList.get(position);
        viewHolder.tv_title.setText(article.gettitle());
        viewHolder.tv_date.setText(article.getdate());
        viewHolder.tv_write.setText(article.getwrite());
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return arrayList.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_title;
        public TextView tv_date;
        public TextView tv_write;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        arrayList.set(pos, "item clicked. pos=" + pos) ;

                        notifyItemChanged(pos) ;
                    }
                }
            });*/

            // 뷰 객체에 대한 참조. (hold strong reference)
            this.tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            this.tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            this.tv_write = (TextView) itemView.findViewById(R.id.tv_write);
        }
    }


}
