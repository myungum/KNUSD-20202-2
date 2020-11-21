package com.example.sd2020;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity{
    EditText ed_id,ed_pw;
    CheckBox cb_login;
    FirebaseDatabase db;
    DatabaseReference mdr;
    String cur_id,cur_pw,changed_pw;
    boolean isChecked_auto_login=false;
    SharedPreferences sf;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sf=getSharedPreferences("saved",0);
        db=FirebaseDatabase.getInstance();
        mdr=db.getReference();
        ed_id=(EditText)findViewById(R.id.id);
        ed_pw=(EditText)findViewById(R.id.password);
        mAuth=FirebaseAuth.getInstance();
        Button bt_login=(Button)findViewById(R.id.bt_login);
        Button bt_signup=(Button)findViewById(R.id.bt_sign_up);
        Button bt_findid=(Button)findViewById(R.id.bt_find_id);
        Button bt_findpw=(Button)findViewById(R.id.bt_find_password);
        cb_login=(CheckBox)findViewById(R.id.cb_login_state);
        cb_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    isChecked_auto_login=true;
                }
                else
                {
                    isChecked_auto_login=false;
                }
            }
        });
        bt_findid.setOnClickListener(new View.OnClickListener() {//아이디 찾기
            @Override
            public void onClick(View view) {
                Intent it=new Intent(LoginActivity.this, Find_id.class);
                startActivity(it);
            }
        });
        bt_findpw.setOnClickListener(new View.OnClickListener() {//비밀번호 찾기
            @Override
            public void onClick(View view) {
                Intent it=new Intent(LoginActivity.this, Find_pw.class);
                startActivity(it);
            }
        });
        bt_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                cur_id=ed_id.getText().toString().trim(); // 문자 뒤에 공백 제거
                cur_pw=ed_pw.getText().toString().trim();
                changed_pw= SHA256.getSHA256(cur_pw);

                if(cur_id.length()==0)
                {
                    Toast.makeText(LoginActivity.this,"아이디를 입력해 주세요",Toast.LENGTH_LONG).show();
                }
                else if(cur_pw.length()==0)
                {
                    Toast.makeText(LoginActivity.this,"비밀번호를 입력해 주세요",Toast.LENGTH_LONG).show();
                }
                else {
                    mdr.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(cur_id)) {
                                String pw=dataSnapshot.child(cur_id).child("password").getValue().toString();
                                String family=dataSnapshot.child(cur_id).child("family").getValue().toString();
                                String level=dataSnapshot.child(cur_id).child("level").getValue().toString();
                                String name=dataSnapshot.child(cur_id).child("name").getValue().toString();
                                if(pw.equals(changed_pw))
                                {
                                    SharedPreferences.Editor editor=sf.edit();
                                    editor.putString("save",""+isChecked_auto_login);
                                    editor.putString("id",cur_id);
                                    editor.putString("pw",cur_pw);
                                    editor.putString("family",family);
                                    editor.putString("name",name);
                                    editor.putString("level",level);
                                    editor.commit();
                                    if(family.equals("NULL"))
                                    {
                                        Intent it=new Intent(getApplicationContext(),Find_family.class);
                                        startActivity(it);
                                        finish();
                                    }
                                    else if(level.equals("admin"))
                                    {
                                        Intent it = new Intent(getApplicationContext(), Main.class);
                                        startActivity(it);
                                        finish();
                                    }
                                    else if(level.equals("child"))
                                    {
                                        Log.d("check1","aa");
                                        Intent it = new Intent(getApplicationContext(), Main2.class);
                                        startActivity(it);
                                        finish();
                                    }
                                    else {
                                        editor.putString("save","false");
                                        editor.commit();
                                        Toast.makeText(getApplicationContext(),"인터넷 연결이 불안합니다. 조금 있다 다시 시도해주세요.",Toast.LENGTH_LONG).show();
                                    }
                                }
                                else    Toast.makeText(LoginActivity.this,"존재 하지 않는 사용자입니다.",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this,"존재 하지 않는 사용자입니다.",Toast.LENGTH_LONG).show();
                            }

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                }
            }
        });
        bt_signup.setOnClickListener(new View.OnClickListener(){//회원가입
            @Override
            public void onClick(View v)
            {
                Intent it=new Intent(getApplicationContext(), Sign_up.class);
                startActivity(it);
                finish();
            }
        });
    }
}