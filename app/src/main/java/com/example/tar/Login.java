package com.example.tar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    FirebaseAuth auth;
    SignInButton btnGoogle;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setStatusBarColor(ContextCompat.getColor(Login.this,R.color.white));
        btnGoogle=findViewById(R.id.btnGoogle);
        auth = FirebaseAuth.getInstance();
        mAuth=FirebaseAuth.getInstance();

        if(auth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),HomePage2.class));
            finish();
        }
        btnGoogle.setOnClickListener(view -> {
            Intent intent=new Intent(Login.this,GoogleSignInActivity.class);
            startActivity(intent);
        });
       /*RegisterNow.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View view) {
                Intent intent=new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });*/




    }
    /*private void loginUser(){
        String email = etLoginEmail.getEditText().getText().toString();
        String password = etLoginPassword.getEditText().getText().toString();

        if (TextUtils.isEmpty(email)){
            etLoginEmail.setError("Email Cannot be empty");
            etLoginEmail.requestFocus();
        }else if (TextUtils.isEmpty(password)){
            etLoginPassword.setError("Password cannot be empty");
            etLoginPassword.requestFocus();
        }else{
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){


                        Toast.makeText(Login.this,"User Logged in successfully",Toast.LENGTH_SHORT).show();

                        Intent intent=new Intent(Login.this,Homepage.class);
                        intent.putExtra("data",email);

                        startActivity(new Intent(Login.this,Homepage.class));
                        startActivity(intent);
                    }else{
                        Toast.makeText(Login.this,"Login error",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }*/
    }



