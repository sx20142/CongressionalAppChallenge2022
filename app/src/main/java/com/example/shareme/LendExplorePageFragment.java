package com.example.shareme;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shareme.adapters.LendPostAdapter;
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

public class LendExplorePageFragment extends Fragment {

    //declare variables
    FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;
    List<LendPostTemplate> postList;
    LendPostAdapter postAdapter;

    public LendExplorePageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lend_explore_page, container, false);

        //initialize variables
        firebaseAuth = FirebaseAuth.getInstance();
        //recycler view and its properties
        recyclerView = view.findViewById(R.id.lendPosts_recView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //show newest post first, so load from last
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);

        postList = new ArrayList<>();
        
        loadPosts();

        return view;
    }

    private void loadPosts() {
        //path of all posts
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Lend_Posts");
        //get all data from this ref
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    LendPostTemplate postTemplate = ds.getValue(LendPostTemplate.class);

                    postList.add(postTemplate);
                    //adapter
                    postAdapter = new LendPostAdapter(getActivity(), postList);
                    //set adapter to recyclerview
                    recyclerView.setAdapter(postAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //in case of error
                Toast.makeText(getActivity(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchPosts(final String searchQuery){

        //path of all posts
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Lend_Posts");
        //get all data from this ref
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    LendPostTemplate postTemplate = ds.getValue(LendPostTemplate.class);


                    if (postTemplate.getpTitle().toLowerCase().contains(searchQuery.toLowerCase()) ||
                            postTemplate.getpDescr().toLowerCase().contains(searchQuery.toLowerCase())){
                        postList.add(postTemplate);
                    }

                    //adapter
                    /*postAdapter = new LendPostAdapter(getActivity(), postList);
                    //set adapter to recyclerview
                    recyclerView.setAdapter(postAdapter);*/
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //in case of error
                Toast.makeText(getActivity(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*private void checkUserStatus() {
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

    //inflate options menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //searchview to search posts by post title/description
        MenuItem item = menu.findItem(R.id.search_action);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        //search listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //called when user press search button
                if (!TextUtils.isEmpty(s))
                    searchPosts(s);
                else
                    loadPosts();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //called as and when user press any letter
                if (!TextUtils.isEmpty(s))
                    searchPosts(s);
                else
                    loadPosts();
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    //handle menu item clicks
    @Override
    public boolean onOptionsItemSelected(@Nullable MenuItem item) {
        //get item id
        int id = item.getItemId();
        if (id == R.id.logout_action) {
            firebaseAuth.signOut();
            checkUserStatus();
        }
       // else if (R.id.action_settings) {

        //}
        return super.onOptionsItemSelected(item);
    }*/
}