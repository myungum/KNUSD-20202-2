package com.example.sd2020;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Find_pw extends AppCompatActivity {
    char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
    EditText et_name,et_email,et_id,et_key;
    String name,email,id,key,cur_key;
    TextView tv_pw;
    Button bt_send_key,bt_ok,bt_ck;
    FirebaseDatabase db;
    DatabaseReference mdr;
    boolean sw_fin,sw_send;
    public void onBackPressed() {
        super.onBackPressed();
        Intent it = new Intent(Find_pw.this, LoginActivity.class);
        startActivity(it);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw);
        et_name=(EditText)findViewById(R.id.find_pw_nm);
        et_email=(EditText)findViewById(R.id.find_pw_em);
        et_id=(EditText) findViewById(R.id.find_pw_id);
        et_key=(EditText) findViewById(R.id.find_pw_key);
        bt_send_key=(Button)findViewById(R.id.find_pw_bt);
        tv_pw=(TextView)findViewById(R.id.find_pw_pw);
        bt_ok=(Button)findViewById(R.id.find_pw_ok);
        bt_ck=(Button)findViewById(R.id.find_pw_ck);
        db=FirebaseDatabase.getInstance();
        mdr=db.getReference("user");
        sw_fin=false;
        sw_send=false;
        key="";
        bt_send_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                id = et_id.getText().toString();
                name = et_name.getText().toString();
                email = et_email.getText().toString();
                if (id.equals("")) {
                    Toast.makeText(Find_pw.this, "아이디을 입력해 주세요", Toast.LENGTH_LONG).show();
                } else if (name.equals("")) {
                    Toast.makeText(Find_pw.this, "이름을 입력해 주세요", Toast.LENGTH_LONG).show();
                } else if (email.equals("")) {
                    Toast.makeText(Find_pw.this, "이메일를 입력해 주세요", Toast.LENGTH_LONG).show();
                } else {
                    mdr.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (sw_fin) return;
                            if(dataSnapshot.hasChild(id)){
                                String cur_name = dataSnapshot.child(id).child("name").getValue().toString();
                                String cur_email = dataSnapshot.child(id).child("email").getValue().toString();
                                if (cur_name.equals(name) && cur_email.equals(email)) {
                                    Random mr = new Random();
                                    key = "";
                                    for (int i = 0; i < 6; i++) {
                                        key += "" + mr.nextInt(10);
                                    }
                                    GmailSender sender = new GmailSender("sm970124@gmail.com", "vclvhodvlkvnqbez");
                                    try {

                                        sender.sendMail("[치매노인지키미]비밀번호 인증번호입니다", key,
                                                email, email);
                                        sw_send = true;
                                    } catch (Exception e) {
                                        Toast.makeText(Find_pw.this, "예상치 못한 오류로 조금 뒤에 인증 부탁드립니다..", Toast.LENGTH_LONG).show();
                                    }
                                    Toast.makeText(Find_pw.this, "입력하신 이메일로 인증번호를 보냈습니다.", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(Find_pw.this, "개인정보가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(Find_pw.this, "개인정보가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }

            }
        });
        bt_ck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!sw_send)
                {
                    Toast.makeText(Find_pw.this, "인증번호를 먼저 발급받으세요.", Toast.LENGTH_LONG).show();
                }
                else
                {
                    cur_key=et_key.getText().toString();
                    if(cur_key.equals(key)||true )
                    {//SHA256.getSHA256()
                        String temp_password="";
                        for(int i=0;i<8;i++)
                        {
                            temp_password+=charSet[(int)(Math.random()*charSet.length)];
                        }tv_pw.setText(temp_password);
                        mdr.child(id).child("password").setValue(SHA256.getSHA256(temp_password));
                        Toast.makeText(Find_pw.this,"임시 비밀번호를 발급하였습니다.",Toast.LENGTH_LONG).show();
                        sw_fin=true;
                    }
                    else
                    {
                        Toast.makeText(Find_pw.this,"인증번호가 올바르지 않습니다. 다시 인증번호를 발급해주세요.",Toast.LENGTH_LONG).show();
                        sw_send=false;
                    }
                }

            }
        });
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sw_fin)finish();
                else
                {

                    AlertDialog.Builder popup = new AlertDialog.Builder(Find_pw.this);//팝업창을 하나 생성합니다.
                    popup.setMessage("비밀번호를 찾지 못하였습니다. 그만 찾으시겠습니까?");//팝업창에 "계란 타이머를 종료하시겠습니까?"와 같은 문장을 출력합니다

                    popup.setTitle("종료");//판업창의 타이틀을 "계란타이머 종료"로 설정합니다.
                    popup.setNegativeButton("비밀번호를 계속 찾습니다.", new DialogInterface.OnClickListener() {//"어플을 종료하지 않습니다"에 해당하는 버튼을 생성합니다
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return; //클릭시 return으로 해당 팝업을 종료합니다.
                        }
                    });
                    popup.setPositiveButton("그만 찾겠습니다.", new DialogInterface.OnClickListener() {//"어플을 종료합니다"에 해당하는 버튼을 생성합니다.
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();//클릭시 해당 어플리케이션을 종료합니다.
                        }
                    });
                    popup.show();
                }
            }
        });
    }
}
