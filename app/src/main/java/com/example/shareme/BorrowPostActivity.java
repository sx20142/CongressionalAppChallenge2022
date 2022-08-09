package com.example.shareme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class BorrowPostActivity extends AppCompatActivity {

    //declare variables
    FirebaseAuth firebaseAuth;
    EditText title, des;
    ProgressDialog pd;
    Spinner category, urgency;
    String editTitle, editDes;
    Button upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_post);

        //initialize variables
        firebaseAuth = FirebaseAuth.getInstance();
        title = findViewById(R.id.title);
        des = findViewById(R.id.description);
        upload = findViewById(R.id.upload);
        pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);

        // category dropdown menu
        category = findViewById(R.id.category);
        ArrayAdapter<String> categoryAdaptor = new ArrayAdapter<String>(BorrowPostActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.item_categories));
        categoryAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(categoryAdaptor);

        //urgency dropdown menu
        urgency = findViewById(R.id.urgency);
        ArrayAdapter<String> urgencyAdaptor = new ArrayAdapter<String>(BorrowPostActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.item_urgency));
        urgencyAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        urgency.setAdapter(urgencyAdaptor);
        //Intent intent = BorrowPostActivity.this.getIntent();

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titl = "" + title.getText().toString().trim();
                String description = "" + des.getText().toString().trim();

                // If empty set error
                if (TextUtils.isEmpty(titl)) {
                    title.setError("Title Cannot be empty");
                    Toast.makeText(BorrowPostActivity.this, "Title cannot be left empty", Toast.LENGTH_LONG).show();
                    return;
                }

                // If empty set error
                if (TextUtils.isEmpty(description)) {
                    des.setError("Description Cant be empty");
                    Toast.makeText(BorrowPostActivity.this, "Description can't be left empty", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });
    }
}