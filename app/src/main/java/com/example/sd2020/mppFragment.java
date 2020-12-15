package com.example.sd2020;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class mppFragment extends Fragment {
    Button bt_logout,bt_change,bt_admin;
    private TextView tv_mp_name,tv_mp_id, tv_mp_rel, tv_mp_tel;
    FirebaseDatabase db;
    DatabaseReference mdr;
    View view;
    SharedPreferences sf;
    Context context;
    String cur_name,cur_id,cur_family, cur_level, cur_phone;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        db = FirebaseDatabase.getInstance();
        mdr = db.getReference();
        sf = context.getSharedPreferences("saved",0);
        cur_id = sf.getString("id", "0");
        cur_family = sf.getString("family", "0");

        view=inflater.inflate(R.layout.activity_fragment_mypage_parent, container, false);
        bt_logout=view.findViewById(R.id.bt_parent_logout);
        bt_change=view.findViewById(R.id.bt_change_information);
        bt_admin=view.findViewById(R.id.bt_admin_family);
        tv_mp_name = view.findViewById(R.id.tv_mp_name);
        tv_mp_id = view.findViewById(R.id.tv_mp_id);
        tv_mp_rel = view.findViewById(R.id.tv_mp_rel);
        tv_mp_tel = view.findViewById(R.id.tv_mp_tel);
        mdr.child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(cur_id)) {
                    cur_name = dataSnapshot.child(cur_id).child("name").getValue().toString();
                    cur_id = dataSnapshot.child(cur_id).child("id").getValue().toString();
                    cur_level = dataSnapshot.child(cur_id).child("level").getValue().toString();
                    cur_phone = dataSnapshot.child(cur_id).child("phone").getValue().toString();
                    tv_mp_name.setText("이   름     : " + cur_name);
                    tv_mp_id.setText("아이디    : " + cur_id);
                    tv_mp_rel.setText("권한 : 보호자");
                    tv_mp_tel.setText("전화번호 : " + cur_phone);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        bt_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it =new Intent(getActivity(),admin_family.class);
                startActivity(it);
                getActivity().finish();
            }
        });
        bt_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it =new Intent(getActivity(),Check_pw.class);
                startActivity(it);
                getActivity().finish();

            }
        });//bt_change_information
        bt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it =new Intent(getActivity(),LoginActivity.class);
                SharedPreferences.Editor editor = sf.edit();
                editor.clear();
                editor.commit();
                startActivity(it);
                getActivity().finish();
            }
        });
        return view;
    }
}