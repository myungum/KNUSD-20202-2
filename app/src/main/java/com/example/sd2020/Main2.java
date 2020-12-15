package com.example.sd2020;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
    private FragmentTransaction transaction;
    SharedPreferences sf;
    FirebaseDatabase db;
    DatabaseReference mdr;
    String cur_id,cur_family;
    TextView frag1,frag2,frag4;
    ImageView frag1im,frag2im,frag4im;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_child);
        frag1=(TextView)findViewById(R.id.child_1);
        frag2=(TextView)findViewById(R.id.child_2);
        frag4=(TextView)findViewById(R.id.child_4);
        frag1im=(ImageView)findViewById(R.id.child_calendar);
        frag2im=(ImageView)findViewById(R.id.child_landscape);
        frag4im=(ImageView)findViewById(R.id.child_gear);

        db= FirebaseDatabase.getInstance();
        mdr=db.getReference();
        sf=getSharedPreferences("saved",0);
        cur_id=sf.getString("id","0");
        cur_family=sf.getString("family","0");

        fragmentManager = getSupportFragmentManager();
        fragment1=new Fragmentc1(cur_family);
        fragmentmpc=new mpcFragment();
        fragment2=new Fragmentc2(cur_family);
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout_child, fragment1).commitAllowingStateLoss();
    }

    public void clickHandler(View view)
    {
        transaction = fragmentManager.beginTransaction();

        switch(view.getId())
        {
            case R.id.btn_fragment1_c:
                transaction.replace(R.id.frameLayout_child, fragment1).commitAllowingStateLoss();
                frag1im.setImageResource(R.drawable.pink_calendar);
                frag2im.setImageResource(R.drawable.gray_landscape);
                frag4im.setImageResource(R.drawable.gray_gear);
                frag1.setTextColor(Color.rgb(255,25,118));
                frag2.setTextColor(Color.rgb(191,191,191));
                frag4.setTextColor(Color.rgb(191,191,191));

                break;
            case R.id.btn_fragment2_c:
                transaction.replace(R.id.frameLayout_child, fragment2).commitAllowingStateLoss();
                frag1im.setImageResource(R.drawable.gray_calendar);
                frag2im.setImageResource(R.drawable.pink_landscape);
                frag4im.setImageResource(R.drawable.gray_gear);
                frag1.setTextColor(Color.rgb(191,191,191));
                frag2.setTextColor(Color.rgb(255,25,118));
                frag4.setTextColor(Color.rgb(191,191,191));
                break;
            case R.id.btn_fragment4_c:
                transaction.replace(R.id.frameLayout_child, fragmentmpc).commitAllowingStateLoss();
                frag1im.setImageResource(R.drawable.gray_calendar);
                frag2im.setImageResource(R.drawable.gray_landscape);
                frag4im.setImageResource(R.drawable.pink_gear);
                frag1.setTextColor(Color.rgb(191,191,191));
                frag2.setTextColor(Color.rgb(191,191,191));
                frag4.setTextColor(Color.rgb(255,25,118));
                break;
        }
    }


}
