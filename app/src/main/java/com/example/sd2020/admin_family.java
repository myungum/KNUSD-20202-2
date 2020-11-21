package com.example.sd2020;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class admin_family extends AppCompatActivity {
    Button bt_ok;
    RecyclerView arecyclerView;
    CustomAdaptor_a aadapter;
    RecyclerView.LayoutManager alayoutManager;
    ArrayList<Parent> aarrayList;
    FirebaseDatabase db;
    DatabaseReference mdr;
    SharedPreferences sf;
    String saved_id="",saved_family="",temp_name,temp_id,temp_pn,child_name,child_id;
    TextView tv_id,tv_nm;
    int cnt=0;
    int p1,p2;
    public void onBackPressed() {
        super.onBackPressed();
        Intent it = new Intent(admin_family.this, Main.class);
        startActivity(it);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_family);
        bt_ok=(Button)findViewById(R.id.bt_admin_family_ok);
        tv_id=(TextView)findViewById(R.id.child_id);
        tv_nm=(TextView)findViewById(R.id.child_name);
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(admin_family.this, Main.class);
                startActivity(it);
                finish();
            }
        });
        sf=getSharedPreferences("saved",0);
        db= FirebaseDatabase.getInstance();
        mdr=db.getReference();
        saved_id=sf.getString("id","0");
        saved_family=sf.getString("family","0");
        Log.d("로그",saved_family);
        mdr.child("family").child("member").child(saved_family).child("child").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cnt=0;
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    cnt++;
                    child_id=snapshot.child("id").getValue().toString();
                    child_name=snapshot.child("name").getValue().toString();
                }
                if(cnt==0)
                {
                    tv_id.setText("없음");
                    tv_nm.setText("없음");
                }
                else if(cnt==1)
                {
                    tv_id.setText(child_id);
                    tv_nm.setText(child_name);
                }
                else
                {
                    tv_id.setText("오류발생");
                    tv_nm.setText("오류발생");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Button bt_delete=(Button)findViewById(R.id.bt_delete_child);
        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temp_id=tv_id.getText().toString();
                temp_name=tv_nm.getText().toString();
                if(temp_id.equals("없음")&&temp_name.equals("없음"))
                {
                    return;
                }
                else
                {
                    mdr.child("family").child("member").child(saved_family).child("child").child(temp_id).removeValue();
                    mdr.child("user").child(temp_id).child("family").setValue("NULL");
                    mdr.child("user").child(temp_id).child("level").setValue("NULL");
                }
            }
        });

        arecyclerView =findViewById(R.id.rv_applied);
        arecyclerView.setHasFixedSize(true);
        alayoutManager=new LinearLayoutManager(this);
        arecyclerView.setLayoutManager(alayoutManager);
        aarrayList=new ArrayList<>();

        mdr.child("family").child("member").child(saved_family).child("The_wait").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                aarrayList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    String name,id,pn;
                    name=snapshot.child("name").getValue().toString();
                    id=snapshot.child("id").getValue().toString();
                    pn=snapshot.child("pn").getValue().toString();
                    Parent parent=new Parent(name,id,pn);
                    aarrayList.add(parent);
                }
                aadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        aadapter=new CustomAdaptor_a(aarrayList,this);
        arecyclerView.setAdapter(aadapter);
        aadapter.setOnClickListener(new CustomAdaptor_a.MyRecyclerViewClickListener() {
            @Override
            public void onItemClicked(int position) {
            }
            @Override
            public void onGoDownClicked(int position) {
                if(cnt==0) {
                    temp_name = aarrayList.get(position).getName();
                    temp_id = aarrayList.get(position).getId();
                    temp_pn = aarrayList.get(position).getPn();
                    p2 = position;
                    mdr.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child("family").child("member").child(saved_family).child("The_wait").hasChild(temp_id)) {
                                String name, id, pn;
                                name = dataSnapshot.child("family").child("member").child(saved_family).child("The_wait").child(temp_id).child("name").getValue().toString();
                                id = dataSnapshot.child("family").child("member").child(saved_family).child("The_wait").child(temp_id).child("id").getValue().toString();
                                pn = dataSnapshot.child("family").child("member").child(saved_family).child("The_wait").child(temp_id).child("pn").getValue().toString();
                                dataSnapshot.child("family").child("member").child(saved_family).child("The_wait").child(temp_id).getRef().removeValue();
                                if (dataSnapshot.child("user").child(temp_id).child("family").getValue().toString().equals("NULL") &&
                                        dataSnapshot.child("user").child(temp_id).hasChild("applied")) {
                                    if (dataSnapshot.child("user").child(temp_id).child("applied").getValue().toString().equals(saved_family)) {
                                        Parent p = new Parent(temp_name, temp_id, temp_pn);
                                        mdr.child("family").child("member").child(saved_family).child("child").child(temp_id).setValue(p);
                                        mdr.child("user").child(temp_id).child("applied").removeValue();
                                        mdr.child("user").child(temp_id).child("level").setValue("child");
                                        mdr.child("user").child(temp_id).child("family").setValue(saved_family);
                                    } else {
                                        Toast.makeText(admin_family.this, "이미 보호자 계정이 연동된 아이디입니다.", Toast.LENGTH_LONG).show();
                                        mdr.child("user").child(temp_id).child("applied").removeValue();
                                    }
                                } else {
                                    Toast.makeText(admin_family.this, "이미 보호자 계정이 연동된 아이디입니다.", Toast.LENGTH_LONG).show();
                                    mdr.child("user").child(temp_id).child("applied").removeValue();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
                else
                {
                    Toast.makeText(admin_family.this, "이미 환자가 존재합니다..", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onDeleteClicked(int position) {
                temp_name = aarrayList.get(position).getName();
                temp_id = aarrayList.get(position).getId();
                temp_pn = aarrayList.get(position).getPn();
                Toast.makeText(admin_family.this,"삭제",Toast.LENGTH_LONG).show();
                mdr.child("user").child(temp_id).child("applied").removeValue();
                mdr.child("family").child("member").child(saved_family).child("The_wait").child(temp_id).removeValue();
            }
        });
    }
}