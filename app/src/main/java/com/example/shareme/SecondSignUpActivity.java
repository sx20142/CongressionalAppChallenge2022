package com.example.shareme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SecondSignUpActivity extends AppCompatActivity {

    //declare variables
    Spinner school_spinner;
    String email, password;
    Button signUp_btn;
    ProgressDialog progressDialog; //progressbar to display while registering user
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_sign_up);

        //Actionbar and its title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create Account (2/2)");
        //enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();

        //initialize variables
        school_spinner = findViewById(R.id.school_spinner);
        email = intent.getStringExtra("userEmail");
        password = intent.getStringExtra("userPass");
        signUp_btn = findViewById(R.id.signUp_btn);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        progressDialog.setMessage("Registering User...");

        String[] texas_schools = new String[] {
                "Texas schools...",
                "Abilene Christian University",
                "Amarillo College",
                "Amberton University",
                "Angelina College",
                "Angelo State University",
                "Arlington Baptist University",
                "Austin College",
                "Baylor University",
                "Blinn College",
                "Brazosport College",
                "Central Texas College",
                "Cisco College",
                "Clarendon College",
                "Coastal Bend College",
                "Collin College",
                "Concordia University Texas",
                "Dallas Baptist University",
                "Del Mar College",
                "East Texas Baptist University",
                "Frank Phillips College",
                "Galveston College",
                "Grayson College",
                "Hardin-Simmons University",
                "Hill College",
                "Houston Baptist University",
                "Howard College",
                "Howard Payne University",
                "Huston-Tillotson University",
                "Jacksonville College",
                "Jarvis Christian College",
                "Kilgore College",
                "Lamar Institute of Technology",
                "Lamar State College at Orange",
                "Lamar State College at Port Arthur",
                "Lamar University",
                "Laredo College",
                "Lee College",
                "LeTourneau University",
                "Lone Star College - CyFair",
                "Lubbock Christian University",
                "McMurry University",
                "Midland College",
                "Midwestern State University",
                "Navarro College",
                "North Central Texas College",
                "Northeast Lakeview College",
                "Northwest Vista College",
                "Odessa College",
                "Our Lady of the Lake University",
                "Palo Alto College",
                "Panola College",
                "Parker University",
                "Paul Quinn College",
                "Prarie View A&M University",
                "Ranger College",
                "Rice University",
                "Sam Houston State University",
                "San Antonio College",
                "San Jacinto College North Campus",
                "Schreiner University",
                "South Plains College",
                "South Texas College",
                "Southern Methodist University",
                "SouthWest College For the Deaf",
                "Southwestern Adventist University",
                "Southwestern Assemblies of God University",
                "Southwestern Christian College",
                "Southwestern University",
                "St. Edward\'s University",
                "St. Mary\'s University",
                "St. Philip\'s College",
                "Stephen F. Austin State University",
                "Sul Ross State University",
                "Sul Ross State University Rio Grande College",
                "Tarleton State University",
                "Tarrant County College-Northwest Campus",
                "Temple College",
                "Texarkana College",
                "Texas A&M University",
                "Texas A&M University at Galveston",
                "Texas A&M University - Central Texas",
                "Texas A&M University - Commerce",
                "Texas A&M University - Corpus Christi",
                "Texas A&M University - Kingsville",
                "Texas A&M University - San Antonio",
                "Texas A&M University - Texarkana",
                "Texas Christian University",
                "Texas College",
                "Texas Lutheran University",
                "Texas Southern University",
                "Texas Southmost College",
                "Texas State University",
                "Texas Tech University",
                "Texas Wesleyan University",
                "Texas Woman\'s University",
                "The University of Texas - Rio Grande Valley - Edinburg",
                "The University of Texas at Arlington",
                "The University of Texas at Austin",
                "The University of Texas at Dallas",
                "The University of Texas at El Paso",
                "The University of Texas at San Antonio",
                "The University of Texas at Tyler",
                "The University of Texas Permian Basin",
                "Trinity University",
                "University of Dallas",
                "University of Houston",
                "University of Houston - Clear Lake",
                "University of Houston - Downtown",
                "University of Houston - Victoria",
                "University of Mary Hardin-Baylor",
                "University of North Texas",
                "University of North Texas at Dallas",
                "University of St. Thomas",
                "University of The Incarnate Word",
                "Vernon College",
                "Victoria College",
                "Wayland Baptist University",
                "Weatherford College",
                "West Texas A&M University",
                "Western Texas College",
                "Wiley College"
        };

        List<String> texasSchools_list = new ArrayList<>(Arrays.asList(texas_schools));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, texasSchools_list) {
            @Override
            public boolean isEnabled (int position) {
                if (position == 0)
                    return false;
                else
                    return true;
            }
            @Override
            public View getDropDownView (int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0)
                    tv.setTextColor(Color.GRAY);
                else
                    tv.setTextColor(Color.BLACK);
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        school_spinner.setAdapter(adapter);

        signUp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    registerUser(email, password);
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
                            Toast.makeText(SecondSignUpActivity.this, "Registered...\n"+user.getEmail(), Toast.LENGTH_SHORT).show();
                            //move to new screen
                            startActivity(new Intent(SecondSignUpActivity.this, DashboardActivity.class));
                            finish();
                        }
                        else {
                            //If sign in fails, display a message to the user
                            progressDialog.dismiss();
                            Toast.makeText(SecondSignUpActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //error, dismiss progress dialog and get and show the error message
                        progressDialog.dismiss();
                        Toast.makeText(SecondSignUpActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); //go to previous activity
        return super.onSupportNavigateUp();
    }

}