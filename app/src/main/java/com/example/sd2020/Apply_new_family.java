package com.example.sd2020;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;

public class Apply_new_family extends AppCompatActivity {
    Button bt_ok;
    EditText et_id;
    TextView tv_state;
    FirebaseDatabase db;
    DatabaseReference mdr;
    SharedPreferences sp;
    boolean sw;
    String pattern = "^[a-zA-Z0-9]{3,12}$";
    String id;
    String cur_id,cur_name,cur_pn;
    Parent p;
    public void onBackPressed() {
        super.onBackPressed();
        Intent it = new Intent(Apply_new_family.this, Find_family.class);
        startActivity(it);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_new_family_account);
        bt_ok=(Button)findViewById(R.id.bt_start_iwana);
        et_id=(EditText)findViewById(R.id.et_family_id);
        tv_state=(TextView) findViewById(R.id.tv_state_id);
        sp=getSharedPreferences("saved",0);
        cur_id=sp.getString("id","0");
        cur_name=sp.getString("name","0");
        cur_pn=sp.getString("pn","0");
        db= FirebaseDatabase.getInstance();
        mdr=db.getReference();
        sw=false;
        et_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_state.setVisibility(View.INVISIBLE);
            }
            @Override
            public void afterTextChanged(Editable arg0) {
                id=arg0.toString();
                if(id.equals(""))
                {
                    tv_state.setVisibility(View.INVISIBLE);
                    sw=false;
                }
                else {
                    mdr.child("family").child("member").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(id)) {
                                tv_state.setText("이미 사용중인 아이디 입니다.");
                                tv_state.setTextColor(Color.rgb(255, 0, 0));
                                tv_state.setVisibility(View.VISIBLE);
                                sw=false;
                            }
                            else
                            {
                                if(id.length()<3||id.length()>12)
                                {
                                    tv_state.setText("아이디는 3자 이상 12자 이하로 입력해주세요");
                                    tv_state.setTextColor(Color.rgb(255, 0, 0));
                                    tv_state.setVisibility(View.VISIBLE);
                                    sw=false;
                                    return;
                                }
                                if (Pattern.matches(pattern, id) == false) {
                                    tv_state.setText("아이디는 영어와 숫자로만 구성되어야 합니다.");
                                    tv_state.setTextColor(Color.rgb(255, 0, 0));
                                    tv_state.setVisibility(View.VISIBLE);
                                    sw=false;
                                }
                                else {
                                    tv_state.setText("멋진 아이디네요!");
                                    tv_state.setTextColor(Color.rgb(45, 180, 0));
                                    tv_state.setVisibility(View.VISIBLE);
                                    sw=true;
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
                tv_state.setVisibility(View.INVISIBLE);
            }
        });
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sw)
                {
                    p=new Parent(cur_name,cur_id,cur_pn);
                    SharedPreferences sf=getSharedPreferences("saved",0);
                    SharedPreferences.Editor editor=sf.edit();
                    editor.putString("family",id);
                    editor.putString("level","admin");
                    editor.commit();

                    mdr.child("user").child(cur_id).child("family").setValue(id);
                    mdr.child("user").child(cur_id).child("level").setValue("admin");
                    mdr.child("family").child("member").child(id).child("Head_admin").setValue(cur_id);
                    mdr.child("family").child("member").child(id).child("admin").child(cur_id).setValue(p);
                    bt_ok.setClickable(false);
                    Intent it=new Intent(Apply_new_family.this,Main.class);
                    startActivity(it);
                    finish();
                }
                else
                {
                    id=et_id.getText().toString();
                    if (Pattern.matches(pattern, id) == false) {
                        tv_state.setText("아이디는 형식이 올바르지 않습니다.");
                        tv_state.setTextColor(Color.rgb(255, 0, 0));
                        tv_state.setVisibility(View.VISIBLE);
                    }
                    else {
                        tv_state.setText("필수 정보입니다..");
                        tv_state.setTextColor(Color.rgb(255, 0, 0));
                        tv_state.setVisibility(View.VISIBLE);
                    }

                }
            }
        });
    }

}