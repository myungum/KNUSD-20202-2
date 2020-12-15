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


public class Main extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private Fragmentp1 fragment1;
    private mppFragment fragmentmpp;
    private Fragmentp2 fragment2;
    private Fragmentp3 fragment3;
    private FragmentTransaction transaction;
    SharedPreferences sf;
    FirebaseDatabase db;
    DatabaseReference mdr;
    String cur_id,cur_family;
    TextView frag1,frag2,frag3,frag4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frag1=(TextView)findViewById(R.id.parent_1);
        frag2=(TextView)findViewById(R.id.parent_2);
        frag3=(TextView)findViewById(R.id.parent_3);
        frag4=(TextView)findViewById(R.id.parent_4);

        db= FirebaseDatabase.getInstance();
        mdr=db.getReference();
        sf=getSharedPreferences("saved",0);
        cur_id=sf.getString("id","0");
        cur_family=sf.getString("family","0");

        fragmentManager = getSupportFragmentManager();
        fragment1=new Fragmentp1(cur_family);
        fragmentmpp=new mppFragment();
        fragment2=new Fragmentp2(cur_family);
        fragment3=new Fragmentp3();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragment1).commitAllowingStateLoss();
    }

    public void clickHandler(View view)
    {
        transaction = fragmentManager.beginTransaction();

        switch(view.getId())
        {
            case R.id.parent_1:
                transaction.replace(R.id.frameLayout, fragment1).commitAllowingStateLoss();

                break;
            case R.id.parent_2:
                transaction.replace(R.id.frameLayout, fragment2).commitAllowingStateLoss();
                break;
            case R.id.parent_3:
                transaction.replace(R.id.frameLayout, fragment3).commitAllowingStateLoss();
                break;
            case R.id.parent_4:
                transaction.replace(R.id.frameLayout, fragmentmpp).commitAllowingStateLoss();
                break;
        }
    }


}
