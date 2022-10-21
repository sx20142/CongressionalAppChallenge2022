package com.example.shareme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class LendPostActivity extends AppCompatActivity {

    //declare variables
    FirebaseAuth firebaseAuth;
    DatabaseReference userDbRef;
    FirebaseDatabase firebaseDatabase;
    ActionBar actionBar;
    EditText item_name, extra_info;
    Uri imageUri = null;
    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;
    String[] cameraPermission;
    String[] storagePermission;
    private static final int IMAGE_PICK_GALLERY_REQUEST = 300;
    private static final int IMAGE_PICK_CAMERA_REQUEST = 400;
    ProgressDialog pd;
    Spinner category, duration;
    ArrayAdapter<String> categoryAdaptor, durationAdaptor;
    String name, email, uid, phone, dp, categorySelected, durationSelected;
    Button add_post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lend_post);

        //initializing variables
        firebaseAuth = FirebaseAuth.getInstance();
        //checkUserStatus();
        firebaseDatabase = FirebaseDatabase.getInstance();
        actionBar = getSupportActionBar();

        item_name = findViewById(R.id.item_name);
        extra_info = findViewById(R.id.extra_info);
        add_post = findViewById(R.id.upload);
        pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Add New Item");
        actionBar.setSubtitle(email);

        //enable back button in actionbar
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //get info on current user
        userDbRef = firebaseDatabase.getReference("Users");
        Query query = userDbRef.orderByChild("email").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren())
                {
                    email = ""+ds.child("email").getValue();
                    name = ""+ds.child("name").getValue();
                    //dp = ""+ds.child("image").getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        // category dropdown menu
        category = findViewById(R.id.category);
        ArrayAdapter<String> categoryAdaptor = new ArrayAdapter<>(LendPostActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.item_categories));
        categoryAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(categoryAdaptor);

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categorySelected = category.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        //duration dropdown menu
        duration = findViewById(R.id.duration);
        ArrayAdapter<String> durationAdaptor = new ArrayAdapter<>(LendPostActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.item_duration));
        durationAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        duration.setAdapter(durationAdaptor);


        duration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                durationSelected = duration.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Now we will add the post
        add_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = item_name.getText().toString().trim();
                String description = extra_info.getText().toString().trim();

                // If empty set error
                if (TextUtils.isEmpty(title)) {
                    item_name.setError("Title Cannot be empty");
                    Toast.makeText(LendPostActivity.this, "Title can not be left empty", Toast.LENGTH_LONG).show();
                    return;
                }

                // If empty set error
                if (TextUtils.isEmpty(description)) {
                    extra_info.setError("Description Cant be empty");
                    Toast.makeText(LendPostActivity.this, "Description can not be left empty", Toast.LENGTH_LONG).show();
                    return;
                }

                //if image is empty
                if (imageUri == null) {
                    //post without image
                    uploadData(title, description,"noImage");
                    startActivity(new Intent(LendPostActivity.this, AddPostFragment.class));
                }
                else {
                    //post with image
                    uploadData(title, description, String.valueOf(imageUri));
                    startActivity(new Intent(LendPostActivity.this, AddPostFragment.class));
                }
            }
        });
    }

    private void uploadData(String title, String description, String img) {
        pd.setMessage("Publishing Post");
        pd.show();

        //for post-image name, post-id, post-publish-time
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String filePathAndName = "Lend_Posts/" + "post_" + timeStamp;

        if(!img.equals("noImage")) {
            //post with image
            StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathAndName);
            ref.putFile(Uri.parse(img))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //image is uploaded to firebase storage, now get it's url
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful());
                            //String downloadUri = uriTask.getResult().toString();
                            String downloadUri = uriTask.getResult().toString();

                            if (uriTask.isSuccessful()) {
                                //url is recieved upload post to firebase database
                                HashMap<Object, String> lendPost_hashMap = new HashMap<>();
                                //put post info
                                lendPost_hashMap.put("uid", uid);
                                lendPost_hashMap.put("uName", name);
                                lendPost_hashMap.put("uEmail", email);
                                lendPost_hashMap.put("uPhone", phone);
                                lendPost_hashMap.put("uDp", dp);
                                lendPost_hashMap.put("pId", timeStamp);
                                lendPost_hashMap.put("pCategory", categorySelected);
                                lendPost_hashMap.put("pDuration", durationSelected);
                                lendPost_hashMap.put("pTitle", title);
                                lendPost_hashMap.put("pDescr", description);
                                lendPost_hashMap.put("pImage", downloadUri);
                                lendPost_hashMap.put("pTime", timeStamp);

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                //path to store post data
                                DatabaseReference data_ref = database.getReference("Lend_Posts");
                                //put data in ref
                                data_ref.child(email).setValue(lendPost_hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                //added in database
                                                pd.dismiss();
                                                Toast.makeText(LendPostActivity.this, "Post published", Toast.LENGTH_SHORT).show();
                                                //reset views
                                                item_name.setText("");
                                                extra_info.setText("");
                                                imageUri = null;
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //failed to add in database
                                                pd.dismiss();
                                                Toast.makeText(LendPostActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }

                                        });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //failed uploading image
                            pd.dismiss();
                            Toast.makeText(LendPostActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else {
            HashMap<Object, String> lendPost_hashMap = new HashMap<>();
            //put post info
            lendPost_hashMap.put("uid", uid);
            lendPost_hashMap.put("uName", name);
            lendPost_hashMap.put("uEmail", email);
            lendPost_hashMap.put("uPhone", phone);
            lendPost_hashMap.put("uDp", dp);
            lendPost_hashMap.put("pId", timeStamp);
            lendPost_hashMap.put("pCategory", categorySelected);
            lendPost_hashMap.put("pDuration", durationSelected);
            lendPost_hashMap.put("pTitle", title);
            lendPost_hashMap.put("pDescr", description);
            lendPost_hashMap.put("pImage", "null");
            lendPost_hashMap.put("pTime", timeStamp);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            //path to store post data
            DatabaseReference data_ref = database.getReference("Lend_Posts");
            //put data in ref
            data_ref.child(timeStamp).setValue(lendPost_hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //added in database
                            pd.dismiss();
                            Toast.makeText(LendPostActivity.this, "Post published", Toast.LENGTH_SHORT).show();
                            //reset views
                            item_name.setText("");
                            extra_info.setText("");
                            imageUri= null;
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //failed to add in database
                            pd.dismiss();
                            Toast.makeText(LendPostActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUserStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkUserStatus();
    }

    private void checkUserStatus() {
        //get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null){
            //user is signed in stay here
            name = user.getDisplayName();
            email = user.getEmail();
            uid = user.getUid();
        }
        else{
            //user not signed in, go to main activity
            startActivity(new Intent(LendPostActivity.this, MainActivity.class));
            LendPostActivity.this.finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    //inflate options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflating menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //handle menu item clicks
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //get item id
        int id = item.getItemId();
        if (id == R.id.logout_action) {
            firebaseAuth.signOut();
            checkUserStatus();
        }
        return super.onOptionsItemSelected(item);
    }
}
