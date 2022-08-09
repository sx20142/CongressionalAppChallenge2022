package com.example.shareme;

import android.os.Bundle;
import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class AddPostFragment extends Fragment{

    public AddPostFragment() {
    }
    Button LendItem_btn, BorrowItem_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_add_post, container, false);

        //initialize views
        LendItem_btn=(Button) view.findViewById(R.id.lend_btn);
        BorrowItem_btn=(Button)view.findViewById(R.id.borrow_btn);

        //takes user to lend item post page
        LendItem_btn.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LendPostFragment lendpostfrag = new LendPostFragment();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.addPost_fragment, lendpostfrag, lendpostfrag.getTag())
                        .commit();
            }
        });

        //takes user to borrow item post page
        BorrowItem_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BorrowPostFragment borrowpostfrag = new BorrowPostFragment();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.addPost_fragment, borrowpostfrag, borrowpostfrag.getTag())
                        .commit();
            }
        });
        return view;
    }
}
