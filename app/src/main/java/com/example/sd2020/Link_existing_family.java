package com.example.sd2020;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Link_existing_family extends AppCompatActivity {
    Button bt_link_family_account,bt_ok;
    FirebaseDatabase db;
    DatabaseReference mdr;
    TextView tv_linked_family_id;
    String id,cur_id;
    SharedPreferences sf;
    boolean applied_sw;
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferences.Editor editor = sf.edit();
        editor.clear();
        editor.commit();
        Intent it = new Intent(Link_existing_family.this, LoginActivity.class);
        startActivity(it);
        finish();
    }
    @Override//tv_linked_family_id
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_existing_family_account);
        tv_linked_family_id=(TextView)findViewById(R.id.tv_linked_family_id);
        bt_link_family_account=(Button)findViewById(R.id.bt_link_family_account);
        bt_ok=(Button)findViewById(R.id.bt_link_existing_family_account_ok);
        db= FirebaseDatabase.getInstance();
        mdr=db.getReference();
        sf=getSharedPreferences("saved",0);
        cur_id=sf.getString("id","0");
        mdr.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(cur_id).hasChild("applied"))
                {
                    tv_linked_family_id.setText(dataSnapshot.child(cur_id).child("applied").getValue().toString());
                }
                else
                {
                    tv_linked_family_id.setText("연동 신청한 적이 없습니다.");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        bt_link_family_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it=new Intent(Link_existing_family.this,Link_exsisting_family_page.class);
                startActivity(it);
                finish();
            }
        });
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sf.edit();
                editor.clear();
                editor.commit();
                Intent it=new Intent(Link_existing_family.this,LoginActivity.class);
                startActivity(it);
                finish();
            }
        });
    }
}

