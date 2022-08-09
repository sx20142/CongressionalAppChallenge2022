package com.example.shareme;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class BorrowPostFragment extends Fragment {
    public BorrowPostFragment() {
        // Required empty public constructor
    }
    FirebaseAuth firebaseAuth;
    EditText title, des;
    ProgressDialog pd;
    Spinner category, urgency;
    String edititle, editdes;
    Button upload;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        firebaseAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_borrow_post, container, false);

        title = view.findViewById(R.id.title);
        des = view.findViewById(R.id.description);
        upload = view.findViewById(R.id.upload);
        pd = new ProgressDialog(getContext());
        pd.setCanceledOnTouchOutside(false);
        // category dropdown menu
        category = view.findViewById(R.id.category);
        ArrayAdapter<String> categoryAdaptor = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.item_categories));
        categoryAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(categoryAdaptor);

        //urgency dropdown menu
        urgency = view.findViewById(R.id.urgency);
        ArrayAdapter<String> urgencyAdaptor = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.item_urgency));
        urgencyAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        urgency.setAdapter(urgencyAdaptor);

        Intent intent = getActivity().getIntent();

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titl = "" + title.getText().toString().trim();
                String description = "" + des.getText().toString().trim();

                // If empty set error
                if (TextUtils.isEmpty(titl)) {
                    title.setError("Title Cannot be empty");
                    Toast.makeText(getContext(), "Title cannot be left empty", Toast.LENGTH_LONG).show();
                    return;
                }

                // If empty set error
                if (TextUtils.isEmpty(description)) {
                    des.setError("Description Cant be empty");
                    Toast.makeText(getContext(), "Description can't be left empty", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });
        return view;
    }
}
