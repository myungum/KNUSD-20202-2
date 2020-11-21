package com.example.sd2020;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Find_family extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_family);
        Button bt_make_new = (Button) findViewById(R.id.bt_make_new_family_accaount);
        Button bt_link_exist = (Button) findViewById(R.id.bt_link_existing_family_account);
        bt_make_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(Find_family.this,Apply_new_family.class);
                startActivity(it);
                finish();
            }
        });
        bt_link_exist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(Find_family.this, Link_existing_family.class);
                startActivity(it);
                finish();
            }
        });
    }
}
