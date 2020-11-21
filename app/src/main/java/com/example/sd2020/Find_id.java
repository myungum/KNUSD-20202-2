package com.example.sd2020;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Find_id extends AppCompatActivity {

    EditText et_name,et_email;
    TextView tv_id;
    String name,email,id;
    Button bt_find_id,bt_ok;
    FirebaseDatabase db;
    DatabaseReference mdr;
    boolean sw_fin;
    public void onBackPressed() {
        super.onBackPressed();
        Intent it = new Intent(Find_id.this, LoginActivity.class);
        startActivity(it);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);
        et_name=(EditText)findViewById(R.id.find_id_nm);
        et_email=(EditText)findViewById(R.id.find_id_em);
        tv_id=(TextView)findViewById(R.id.find_id_id);
        bt_find_id=(Button)findViewById(R.id.find_id_bt);
        bt_ok=(Button)findViewById(R.id.bt_ok);
        db=FirebaseDatabase.getInstance();
        mdr=db.getReference("user");
        sw_fin=false;
        bt_find_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name=et_name.getText().toString();
                email=et_email.getText().toString();
                if(name.equals(""))
                {
                    Toast.makeText(Find_id.this,"이름을 입력해 주세요", Toast.LENGTH_LONG).show();
                }
                else if(email.equals(""))
                {
                    Toast.makeText(Find_id.this,"이메일를 입력해 주세요", Toast.LENGTH_LONG).show();
                }
                else
                {
                    mdr.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(sw_fin)return;
                            for(DataSnapshot snapshot:dataSnapshot.getChildren())
                            {
                                String cur_name=snapshot.child("name").getValue().toString();
                                String cur_email=snapshot.child("email").getValue().toString();
                                if(name.equals(cur_name)&&email.equals(cur_email))
                                {
                                    tv_id.setText(snapshot.getKey());
                                    Toast.makeText(Find_id.this,"아이디를 찾았습니다.",Toast.LENGTH_LONG).show();
                                    sw_fin=true;
                                }
                            }
                            if(!sw_fin)Toast.makeText(Find_id.this,"아이디를 찾지 못하였습니다. 다시 확인 해주세요.",Toast.LENGTH_LONG).show();
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }

            }
        });
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sw_fin)finish();
                else
                {

                    AlertDialog.Builder popup = new AlertDialog.Builder(Find_id.this);//팝업창을 하나 생성합니다.
                    popup.setMessage("아이디를 찾지 못하였습니다. 그만 찾으시겠습니까?");//팝업창에 "계란 타이머를 종료하시겠습니까?"와 같은 문장을 출력합니다

                    popup.setTitle("종료");//판업창의 타이틀을 "계란타이머 종료"로 설정합니다.
                    popup.setNegativeButton("아이디를 계속 찾습니다.", new DialogInterface.OnClickListener() {//"어플을 종료하지 않습니다"에 해당하는 버튼을 생성합니다
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
