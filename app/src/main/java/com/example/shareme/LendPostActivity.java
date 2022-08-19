package com.example.shareme;

import androidx.annotation.NonNull;
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
import android.view.View;
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
    FirebaseDatabase firebaseDatabase;
    DatabaseReference userDBref;
    DataSnapshot dataSnapshot;
    ActionBar actionBar;
    EditText item_name, extra_info;
    ImageView image;
    Uri imageUri = null;
    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;
    String[] cameraPermission;
    String[] storagePermission;
    private static final int IMAGEPICK_GALLERY_REQUEST = 300;
    private static final int IMAGE_PICKCAMERA_REQUEST = 400;
    ProgressDialog pd;
    Spinner category, duration;
    String editTitle, editDes;
    String name, email, uid, dp;
    Button Add_post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lend_post);

        //initializing variables
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        actionBar = getSupportActionBar();

        item_name = findViewById(R.id.item_name);
        extra_info = findViewById(R.id.extra_info);
        Add_post = findViewById(R.id.upload);
        image = findViewById(R.id.image);
        pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);

        //enable back button in actionbar
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //get info on current user
        userDBref = firebaseDatabase.getReference("Users");
        Query query = userDBref.orderByChild("email").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren())
                {
                    name = ""+ds.child("name").getValue();
                    email = ""+ds.child("email").getValue();
                    dp = ""+ds.child("image").getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // category dropdown menu
        category = findViewById(R.id.category);
        ArrayAdapter<String> categoryAdaptor = new ArrayAdapter<>(LendPostActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.item_categories));
        categoryAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(categoryAdaptor);

        //duration dropdown menu
        duration = findViewById(R.id.duration);
        ArrayAdapter<String> durationAdaptor = new ArrayAdapter<>(LendPostActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.item_duration));
        durationAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        duration.setAdapter(durationAdaptor);

        // Initialising camera and storage permission
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        // After click on image we will be selecting an image
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePicDialog();
            }
        });

        // Now we will add the post
        Add_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = "" + item_name.getText().toString().trim();
                String description = "" + extra_info.getText().toString().trim();

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
                if(imageUri == null)
                    //post without image
                    uploadData(editTitle, editDes,"noImage");

                else{
                    //post with image
                    uploadData(editTitle,editDes,String.valueOf(imageUri));
                }
            }
        });
    }

    private void uploadData(String title, String description, String img)
    {
        pd.setMessage("Publishing Post");
        pd.show();

        //for post-image name, post-id, post-publish-time
        String timeStamp = String.valueOf(System.currentTimeMillis());

        String filePathAndName = "Posts/" + "post_" + timeStamp;

        if(!img.equals("noImage"))
        {
            //post with image
            StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathAndName);
            ref.putFile(Uri.parse(img))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //image is uploaded to firebase storage, now get it's url
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful()) ;

                            String downloadUri = uriTask.getResult().toString();

                            if (uriTask.isSuccessful()) {
                                //url is recieved upload post to firebase database
                                HashMap<Object, String> hashMap = new HashMap<Object, String>();
                                //put post info
                                hashMap.put("uid", uid);
                                hashMap.put("uMane", name);
                                hashMap.put("uEmail", email);
                                hashMap.put("uDp", dp);
                                hashMap.put("pId", timeStamp);
                                hashMap.put("pTitle", title);
                                hashMap.put("pDescr", description);
                                hashMap.put("pImage", downloadUri);
                                hashMap.put("pTime", timeStamp);

                                //path to store post data
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
                                //put data in ref
                                ref.child(timeStamp).setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                //added in database
                                                pd.dismiss();
                                                Toast.makeText(LendPostActivity.this, "Post published", Toast.LENGTH_SHORT).show();
                                                //reset views
                                                item_name.setText("");
                                                extra_info.setText("");
                                                image.setImageURI(null);
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
        else{
            HashMap<Object, String> hashMap = new HashMap<Object, String>();
            //put post info
            hashMap.put("uid", uid);
            hashMap.put("uMane", name);
            hashMap.put("uEmail", email);
            hashMap.put("uDp", dp);
            hashMap.put("pId", timeStamp);
            hashMap.put("pTitle", title);
            hashMap.put("pDescr", description);
            hashMap.put("pImage", "noImage");
            hashMap.put("pTime", timeStamp);

            //path to store post data
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
            //put data in ref
            ref.child(timeStamp).setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //added in database
                            pd.dismiss();
                            Toast.makeText(LendPostActivity.this, "Post published", Toast.LENGTH_SHORT).show();
                            //reset views
                            item_name.setText("");
                            extra_info.setText("");
                            image.setImageURI(null);
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

    private void showImagePicDialog() {
        String options[] = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(LendPostActivity.this);
        builder.setTitle("Pick Image From");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // check for the camera and storage permission if
                // not given the request for permission
                if (which == 0) {
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickFromCamera();
                    }
                } else if (which == 1) {
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        pickFromGallery();
                    }
                }
            }
        });
        builder.create().show();
    }

    // check for storage permission
    private Boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(LendPostActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    // if not given then request for permission after that check if request is given or not
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST: {
                if (grantResults.length > 0) {
                    boolean camera_accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageaccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    // if request access given the pick data
                    if (camera_accepted && writeStorageaccepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(LendPostActivity.this, "Please Enable Camera and Storage Permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }
            // function end
            break;
            case STORAGE_REQUEST: {
                if (grantResults.length > 0) {
                    boolean writeStorageaccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    // if request access given the pick data
                    if (writeStorageaccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(LendPostActivity.this, "Please Enable Storage Permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
        }
    }

    // request for permission to write data into storage
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragePermission() {
        requestPermissions(storagePermission, STORAGE_REQUEST);
    }

    // check camera permission to click picture using camera
    private Boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(LendPostActivity.this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(LendPostActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }
    // request for permission to click photo using camera in app
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission() {
        requestPermissions(cameraPermission, CAMERA_REQUEST);
    }

    // if access is given then pick image from camera and then put
    // the imageUri in intent extra and pass to startactivityforresult
    private void pickFromCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_pic");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");
        imageUri = LendPostActivity.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        Intent camerIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camerIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(camerIntent, IMAGE_PICKCAMERA_REQUEST);
    }

    // if access is given then pick image from gallery
    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGEPICK_GALLERY_REQUEST);
    }

    private void checkUserStatus()
    {
        //get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user!= null){
            //user is signed in stay here
            email = user.getEmail();
            uid = user.getUid();
        }
        else{
            //user not signed in, go to main activity
            startActivity(new Intent(LendPostActivity.this, MainActivity.class));
            LendPostActivity.this.finish();
        }
    }

}
