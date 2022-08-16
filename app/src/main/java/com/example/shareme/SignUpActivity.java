package com.example.shareme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    //declare variables
    EditText email_input, password_input;
    Button signup_btn;
    TextView haveAccount_txt;
    ProgressDialog progressDialog; //progressbar to display while registering user
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Actionbar and its title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create Account");
        //enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //initialize variables
        email_input = findViewById(R.id.email_input);
        password_input = findViewById(R.id.pass_input);
        signup_btn = findViewById(R.id.signup_btn);
        haveAccount_txt = findViewById(R.id.haveAccount_txt);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        progressDialog.setMessage("Registering User...");

        signup_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //validate email and password
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
                else {
                    registerUser(email, password);
                }
            }
        });
        //handle login textview click listener
        haveAccount_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
    }

    private void registerUser(String email, String password) {
        //email and password pattern is valid, show progress dialog and start registering user
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Sign in success, dismiss dialog and start register activity
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            //Get user email and uid from auth
                            String email = user.getEmail();
                            String uid = user.getUid();
                            //When user is registered store user info in firebase realtime databse using HashMap
                            HashMap<Object, String> hashMap = new HashMap<>();
                            //put info in hashmap
                            hashMap.put("email", email);
                            hashMap.put("uid", uid);
                            hashMap.put("name", ""); //will add later
                            hashMap.put("phone", "");
                            hashMap.put("image", "");

                            //firebase database instance
                            FirebaseDatabase database = FirebaseDatabase.getInstance();

                            DatabaseReference reference = database.getReference("Users");

                            reference.child(uid).setValue(hashMap);

                            Toast.makeText(SignUpActivity.this, "Registered...\n"+user.getEmail(), Toast.LENGTH_SHORT).show();
                            //move to new screen
                            startActivity(new Intent(SignUpActivity.this, DashboardActivity.class));
                            finish();
                        }
                        else {
                            //If sign in fails, display a message to the user
                            progressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //error, dismiss progress dialog and get and show the error message
                        progressDialog.dismiss();
                        Toast.makeText(SignUpActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); //go to previous activity
        return super.onSupportNavigateUp();
    }
}