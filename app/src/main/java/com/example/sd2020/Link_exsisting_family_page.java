package com.example.sd2020;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import androidx.appcompat.app.AppCompatActivity;

public class Link_exsisting_family_page extends AppCompatActivity {
    Button bt_apply,bt_ok;
    FirebaseDatabase db;
    DatabaseReference mdr;
    EditText et_id;
    String id,cur_id,cur_name,cur_pn;
    TextView tv_state;
    SharedPreferences sf;
    boolean sw;
    Parent p;
    public void onBackPressed() {
        AlertDialog.Builder popup = new AlertDialog.Builder(Link_exsisting_family_page.this);//팝업창을 하나 생성합니다.
        popup.setMessage("가족 계정에 연동을 하지 않았습니다. 종료하시겠습니까?");//팝업창에 "계란 타이머를 종료하시겠습니까?"와 같은 문장을 출력합니다

        popup.setTitle("종료");//판업창의 타이틀을 "계란타이머 종료"로 설정합니다.
        popup.setNegativeButton("연동을 진행하겠습니다.", new DialogInterface.OnClickListener() {//"어플을 종료하지 않습니다"에 해당하는 버튼을 생성합니다
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return; //클릭시 return으로 해당 팝업을 종료합니다.
            }
        });
        popup.setPositiveButton("종료하겠습니다.", new DialogInterface.OnClickListener() {//"어플을 종료합니다"에 해당하는 버튼을 생성합니다.
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent it=new Intent(Link_exsisting_family_page.this, Link_existing_family.class);
                startActivity(it);
                finish();//클릭시 해당 어플리케이션을 종료합니다.
            }
        });
        popup.show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_existing_family_account_page);
        bt_ok=(Button)findViewById(R.id.bt_finish_apply);
        et_id=(EditText)findViewById(R.id.et_exsiting_family_id);
        tv_state=(TextView) findViewById(R.id.tv_state_link);
        sf=getSharedPreferences("saved",0);
        cur_id=sf.getString("id","0");
        cur_name=sf.getString("name","0");
        cur_pn=sf.getString("pn","0");
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
                                tv_state.setText("연동 가능한 아이디 입니다.");
                                tv_state.setTextColor(Color.rgb(45, 180, 0));
                                tv_state.setVisibility(View.VISIBLE);
                                sw=true;
                            }
                            else {
                                tv_state.setText("존재하지 않는 아이디 입니다.");
                                tv_state.setTextColor(Color.rgb(255, 0, 0));
                                tv_state.setVisibility(View.VISIBLE);
                                sw = false;
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
                if(sw){
                    p=new Parent(cur_name,cur_id,cur_pn);
                    mdr.child("family").child("member").child(id).child("The_wait").child(cur_id).setValue(p);
                    mdr.child("user").child(cur_id).child("applied").setValue(id);
                    bt_ok.setClickable(true);
                    Intent it=new Intent(Link_exsisting_family_page.this, Link_existing_family.class);
                    startActivity(it);
                    finish();
                }
                else {

                    AlertDialog.Builder popup = new AlertDialog.Builder(Link_exsisting_family_page.this);//팝업창을 하나 생성합니다.
                    popup.setMessage("가족 계정에 연동을 하지 않았습니다. 종료하시겠습니까?");//팝업창에 "계란 타이머를 종료하시겠습니까?"와 같은 문장을 출력합니다

                    popup.setTitle("종료");//판업창의 타이틀을 "계란타이머 종료"로 설정합니다.
                    popup.setNegativeButton("연동을 진행하겠습니다.", new DialogInterface.OnClickListener() {//"어플을 종료하지 않습니다"에 해당하는 버튼을 생성합니다
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return; //클릭시 return으로 해당 팝업을 종료합니다.
                        }
                    });
                    popup.setPositiveButton("종료하겠습니다.", new DialogInterface.OnClickListener() {//"어플을 종료합니다"에 해당하는 버튼을 생성합니다.
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent it=new Intent(Link_exsisting_family_page.this, Link_existing_family.class);
                            startActivity(it);
                            finish();//클릭시 해당 어플리케이션을 종료합니다.
                        }
                    });
                    popup.show();
                }
            }
        });
    }
}
