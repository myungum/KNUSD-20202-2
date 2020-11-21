package com.example.sd2020;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class mppFragment extends Fragment {
    Button bt_logout,bt_change,bt_admin;
    View view;
    SharedPreferences sf;
    Context context;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        sf = context.getSharedPreferences("saved",0);
        view=inflater.inflate(R.layout.activity_fragment_mypage_parent, container, false);
        bt_logout=view.findViewById(R.id.bt_parent_logout);
        bt_change=view.findViewById(R.id.bt_change_information);
        bt_admin=view.findViewById(R.id.bt_admin_family);
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