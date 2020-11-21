package com.example.sd2020;;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;

import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class Sign_up extends AppCompatActivity {

    TextView text_comment_id,text_comment_pw,text_comment_nm,text_comment_em,text_comment_pn;
    boolean isExist_id=false,isExist_pw=false,isExist_nm=false,isExist_em=false,isExist_pn=false;
    EditText editid, editpw1,editpw2,editnm,editem,editpn;
    String id;
    FirebaseDatabase db;
    String pattern = "^[a-zA-Z0-9]{3,12}$";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        editid=(EditText)findViewById(R.id.et_id);
        editpw1=(EditText)findViewById(R.id.et_pw1);
        editpw2=(EditText)findViewById(R.id.et_pw2);
        editnm=(EditText)findViewById(R.id.et_nm);
        editem=(EditText)findViewById(R.id.et_em);
        editpn=(EditText)findViewById(R.id.et_pn);
        text_comment_id=(TextView)findViewById(R.id.tv_id);
        text_comment_pw=(TextView)findViewById(R.id.tv_pw);
        text_comment_nm=(TextView)findViewById(R.id.tv_nm);
        text_comment_em=(TextView)findViewById(R.id.tv_em);
        text_comment_pn=(TextView)findViewById(R.id.tv_pn);
        db=FirebaseDatabase.getInstance();
        final DatabaseReference mrb=db.getReference();
        editid.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                text_comment_id.setVisibility(View.INVISIBLE);
            }
            @Override
            public void afterTextChanged(Editable arg0) {
                id=arg0.toString();
                if(id.equals(""))
                {
                    text_comment_id.setVisibility(View.INVISIBLE);
                    isExist_id=false;
                }
                else {
                    mrb.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(id)) {
                                text_comment_id.setText("이미 사용중인 아이디 입니다.");
                                text_comment_id.setTextColor(Color.rgb(255, 0, 0));
                                text_comment_id.setVisibility(View.VISIBLE);
                                isExist_id=false;
                            }
                            else
                            {
                                if(id.length()<3||id.length()>12)
                                {   text_comment_id.setText("아이디는 3자 이상 12자 이하로 입력해주세요");
                                    text_comment_id.setTextColor(Color.rgb(255, 0, 0));
                                    text_comment_id.setVisibility(View.VISIBLE);
                                    isExist_id=false;
                                    return;
                                }
                                if (Pattern.matches(pattern, id) == false) {
                                    text_comment_id.setText("아이디는 영어와 숫자로만 구성되어야 합니다.");
                                    text_comment_id.setTextColor(Color.rgb(255, 0, 0));
                                    text_comment_id.setVisibility(View.VISIBLE);
                                    isExist_id=false;
                                }
                                else {
                                    text_comment_id.setText("멋진 아이디네요!");
                                    text_comment_id.setTextColor(Color.rgb(45, 180, 0));
                                    text_comment_id.setVisibility(View.VISIBLE);
                                    isExist_id=true;
                                }
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_comment_id.setVisibility(View.INVISIBLE);
            }
        });
        editpw2.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                text_comment_pw.setVisibility(View.INVISIBLE);
            }
            @Override
            public void afterTextChanged(Editable arg0) {
                String pw=arg0.toString();
                if(editpw1.getText().toString().equals("")||pw.equals(""))
                {
                    isExist_pw=false;
                    text_comment_pw.setVisibility(View.INVISIBLE);
                }
                else if(editpw1.getText().toString().equals(pw))
                {
                    isExist_pw=true;
                    text_comment_pw.setVisibility(View.INVISIBLE);

                }
                else
                {//비밀번호가 일치하지 않습니다.
                    isExist_pw=false;
                    text_comment_pw.setText("비밀번호가 일치하지 않습니다");
                    text_comment_pw.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_comment_pw.setVisibility(View.INVISIBLE);
                // 입력하기 전에 호출된다.
            }
        });
        editnm.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                text_comment_nm.setVisibility(View.INVISIBLE);
            }
            @Override
            public void afterTextChanged(Editable arg0) {
                String nm=arg0.toString();
                if(nm.equals(""))
                {
                    isExist_nm=false;
                    text_comment_nm.setVisibility(View.VISIBLE);
                }
                else
                {
                    isExist_nm=true;
                    text_comment_nm.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_comment_nm.setVisibility(View.INVISIBLE);
                // 입력하기 전에 호출된다.
            }
        });
        editpn.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                text_comment_pn.setVisibility(View.INVISIBLE);
            }
            @Override
            public void afterTextChanged(Editable arg0) {
                String pn=arg0.toString();
                if(pn.equals(""))
                {
                    isExist_pn=false;
                    text_comment_pn.setVisibility(View.VISIBLE);
                }
                else
                {
                    isExist_pn=true;
                    text_comment_pn.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_comment_pn.setVisibility(View.INVISIBLE);
                // 입력하기 전에 호출된다.
            }
        });
        editem.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                text_comment_em.setVisibility(View.INVISIBLE);
            }
            @Override
            public void afterTextChanged(Editable arg0) {
                String em=arg0.toString();
                if(em.equals(""))
                {
                    isExist_em=false;
                    text_comment_em.setVisibility(View.VISIBLE);
                }
                else
                {
                    isExist_em=true;
                    text_comment_em.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text_comment_em.setVisibility(View.INVISIBLE);
                // 입력하기 전에 호출된다.
            }
        });
        Button bt_sign_up=(Button)findViewById(R.id.sign_up);
        bt_sign_up.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                //Toast.makeText(Sign_up.this,""+isExist_id+" "+isExist_pw+" "+isExist_nm+" "+isExist_em+" "+isExist_pn,Toast.LENGTH_LONG).show();
                if(isExist_em&&isExist_id&&isExist_nm&&isExist_pn&&isExist_pw) {
                    //SHA256.getSHA256(cur_pw);
                    User myUser=new User(SHA256.getSHA256(editpw1.getText().toString()),editnm.getText().toString(),editpn.getText().toString(),editem.getText().toString(),editid.getText().toString());
                    mrb.child("user").child(editid.getText().toString()).setValue(myUser);
                    Intent it = new Intent(Sign_up.this, LoginActivity.class);
                    startActivity(it);
                    finish();
                }
                else//NJM5C68V
                {
                    if(!isExist_pw)
                    {
                        String pw1=editpw1.getText().toString();
                        String pw2=editpw2.getText().toString();
                        if(pw1.equals("")||pw2.equals("")) {
                            text_comment_pw.setText("필수 정보입니다..");
                            text_comment_pw.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            text_comment_pw.setText("비밀번호가 일치하지 않습니다");
                            text_comment_pw.setVisibility(View.VISIBLE);

                        }
                    }
                    if(!isExist_pn)
                    {
                        text_comment_pn.setVisibility(View.VISIBLE);
                    }
                    if(!isExist_nm)
                    {
                        text_comment_nm.setVisibility(View.VISIBLE);

                    }
                    if(!isExist_id)
                    {
                        id=editid.getText().toString();
                        if (Pattern.matches(pattern, id) == false) {
                            text_comment_id.setText("아이디는 형식이 올바르지 않습니다.");
                            text_comment_id.setTextColor(Color.rgb(255, 0, 0));
                            text_comment_id.setVisibility(View.VISIBLE);
                        }
                        else {
                            text_comment_id.setText("필수 정보입니다..");
                            text_comment_id.setTextColor(Color.rgb(255, 0, 0));
                            text_comment_id.setVisibility(View.VISIBLE);
                        }

                    }
                    if(!isExist_em)
                    {
                        text_comment_em.setVisibility(View.VISIBLE);

                    }
                }
            }
        });
    }
}
