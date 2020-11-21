package com.example.sd2020;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Check_pw extends AppCompatActivity {
    EditText et_pw;
    FirebaseDatabase db;
    DatabaseReference mdr;
    SharedPreferences sf;
    String cur_id,pw,level;
    public void onBackPressed() {
        super.onBackPressed();
        if(level.equals("admin")) {
            Intent it = new Intent(Check_pw.this, Main.class);
            startActivity(it);
            finish();
        }
        else if(level.equals("child")) {
            Intent it = new Intent(Check_pw.this, Main2.class);
            startActivity(it);
            finish();
        }
        else
        {
            Toast.makeText(this,"가족 정보가 수정되었습니다. 다시 로그인 해주세요",Toast.LENGTH_SHORT).show();
            Intent it = new Intent(Check_pw.this, LoginActivity.class);
            startActivity(it);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_pw);
        db= FirebaseDatabase.getInstance();
        mdr=db.getReference();
        sf=getSharedPreferences("saved",0);
        level=sf.getString("level","NULL");
        cur_id=sf.getString("id","0");
        Toast.makeText(this,cur_id,Toast.LENGTH_SHORT).show();
        Button bt_chk_pw= (Button) findViewById(R.id.bt_chk_pw);
        Button bt_change_info_go_back = (Button) findViewById(R.id.bt_change_info_go_back);
        et_pw=(EditText)findViewById(R.id.et_chk_pw);
        bt_chk_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw=SHA256.getSHA256(et_pw.getText().toString());
                mdr.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(pw.equals(dataSnapshot.child(cur_id).child("password").getValue().toString()))
                        {
                            Intent it = new Intent(Check_pw.this,Change_info.class);
                            startActivity(it);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(Check_pw.this,"비밀번호가 올바르지 않습니다",Toast.LENGTH_LONG).show();
                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });
        bt_change_info_go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(level.equals("admin")) {
                    Intent it = new Intent(Check_pw.this, Main.class);
                    startActivity(it);
                    finish();
                }
                else if(level.equals("child")) {
                    Intent it = new Intent(Check_pw.this, Main2.class);
                    startActivity(it);
                    finish();
                }
                else
                {
                    Toast.makeText(Check_pw.this,"가족 정보가 수정되었습니다. 다시 로그인 해주세요",Toast.LENGTH_SHORT).show();
                    Intent it = new Intent(Check_pw.this, LoginActivity.class);
                    startActivity(it);
                    finish();
                }
                finish();
            }
        });
    }
}
