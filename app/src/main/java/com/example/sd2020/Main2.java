package com.example.sd2020;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class Main2 extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private Fragmentc1 fragment1;
    private mpcFragment fragmentmpc;
    private Fragmentc2 fragment2;
    private Fragmentc3 fragment3;
    private FragmentTransaction transaction;
    SharedPreferences sf;
    FirebaseDatabase db;
    DatabaseReference mdr;
    String cur_id,cur_family;
    TextView frag1,frag2,frag3,frag4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_child);
        frag1=(TextView)findViewById(R.id.child_1);
        frag2=(TextView)findViewById(R.id.child_2);
        frag3=(TextView)findViewById(R.id.child_3);
        frag4=(TextView)findViewById(R.id.child_4);

        db= FirebaseDatabase.getInstance();
        mdr=db.getReference();
        sf=getSharedPreferences("saved",0);
        cur_id=sf.getString("id","0");
        cur_family=sf.getString("family","0");

        fragmentManager = getSupportFragmentManager();
        fragment1=new Fragmentc1(cur_family);
        fragmentmpc=new mpcFragment();
        fragment2=new Fragmentc2(cur_family);
        fragment3=new Fragmentc3();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout_child, fragment1).commitAllowingStateLoss();
    }

    public void clickHandler(View view)
    {
        transaction = fragmentManager.beginTransaction();

        switch(view.getId())
        {
            case R.id.child_1:
                transaction.replace(R.id.frameLayout_child, fragment1).commitAllowingStateLoss();

                break;
            case R.id.child_2:
                transaction.replace(R.id.frameLayout_child, fragment2).commitAllowingStateLoss();
                break;
            case R.id.child_3:
                transaction.replace(R.id.frameLayout_child, fragment3).commitAllowingStateLoss();
                break;
            case R.id.child_4:
                transaction.replace(R.id.frameLayout_child, fragmentmpc).commitAllowingStateLoss();
                break;
        }
    }


}
