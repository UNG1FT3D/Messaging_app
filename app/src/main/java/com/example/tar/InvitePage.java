package com.example.tar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class InvitePage extends AppCompatActivity {

    ExtendedFloatingActionButton inviteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_page);

        inviteBtn=(ExtendedFloatingActionButton) findViewById(R.id.extended_tab);
        inviteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareBody="Hey am using this amazing app, can you please download this application:-https://cdn.dribbble.com/users/17323/screenshots/996340/great.png&hl=el";
                String shareSub="TAR App";
                intent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
                intent.putExtra(Intent.EXTRA_TEXT,shareBody);
                startActivity(Intent.createChooser(intent,"Share Using"));
            }
        });
    }
}