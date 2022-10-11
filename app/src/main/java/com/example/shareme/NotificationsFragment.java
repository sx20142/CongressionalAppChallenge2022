package com.example.shareme;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shareme.adapters.LendNotificationsAdapter;
import com.example.shareme.adapters.LendPostAdapter;
import com.example.shareme.templates.LendNotificationsTemplate;
import com.example.shareme.templates.LendPostTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    //declare variables
    FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;
    List<LendNotificationsTemplate> notificationsList;
    LendNotificationsAdapter notificationsAdapter;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        //initialize variables
        firebaseAuth = FirebaseAuth.getInstance();
        //recycler view and its properties
        recyclerView = view.findViewById(R.id.lendNotifications_recView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //show newest post first, so load from last
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);

        notificationsList = new ArrayList<>();

        loadNotifications();

        checkUserStatus();
        return view;
    }

    private void loadNotifications() {
        //path of all posts
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Lend_Notifications");
        //get all data from this ref
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notificationsList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    LendNotificationsTemplate notificationsTemplate = ds.getValue(LendNotificationsTemplate.class);

                    notificationsList.add(notificationsTemplate);
                    //adapter
                    notificationsAdapter = new LendNotificationsAdapter(getActivity(), notificationsList);
                    //set adapter to recyclerview
                    recyclerView.setAdapter(notificationsAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //in case of error
                Toast.makeText(getActivity(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUserStatus() {
        //get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            //user is signed in stay here
            //set email of logged in user
            //profile_txt.setText(user.getEmail());
        }
        else {
            //user not signed in, go to main activity
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);//to show menu option in fragment
        super.onCreate(savedInstanceState);
    }
}