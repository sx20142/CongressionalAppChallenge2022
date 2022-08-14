package com.example.shareme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirstSignUpActivity extends AppCompatActivity {

    //declare variables
    EditText email_input, password_input;
    Button next_btn;
    TextView haveAccount_txt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_sign_up);

        //Actionbar and its title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create Account (1/2)");
        //enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //initialize variables
        email_input = findViewById(R.id.email_input);
        password_input = findViewById(R.id.pass_input);
        next_btn = findViewById(R.id.next_btn);
        haveAccount_txt = findViewById(R.id.haveAccount_txt);

        //move to second sign up activity
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_input.getText().toString().trim();
                String password = password_input.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    email_input.setError("Invalid Email");
                    email_input.setFocusable(true);
                }
                else if (password.length() < 6) {
                    password_input.setError("Password length must be at least 6 characters");
                    password_input.setFocusable(true);
                }
                Intent intent = new Intent(FirstSignUpActivity.this, SecondSignUpActivity.class);
                intent.putExtra("userEmail", email_input.getText().toString().trim());
                intent.putExtra("userPass", password_input.getText().toString().trim());
                startActivity(intent);
            }
        });

        //handle login textview click listener
        haveAccount_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstSignUpActivity.this, LoginActivity.class));
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); //go to previous activity
        return super.onSupportNavigateUp();
    }
}