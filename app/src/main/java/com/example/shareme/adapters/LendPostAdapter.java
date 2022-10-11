package com.example.shareme.adapters;

import android.content.Context;
import android.net.Uri;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shareme.LendPostActivity;
import com.example.shareme.R;
import com.example.shareme.templates.LendPostTemplate;
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
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class LendPostAdapter extends RecyclerView.Adapter<LendPostAdapter.MyHolder> {

    Context context;
    List<LendPostTemplate> postList;
    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;
    View user_view;
    View confirm_lend;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String phone;

    public LendPostAdapter(Context context, List<LendPostTemplate> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.lend_row_posts, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        //get data
        String uid = postList.get(position).getUid();
        String uEmail = postList.get(position).getuEmail();
        String uName = postList.get(position).getuName();
        //String uDp = postList.get(position).getuDp();
        String pId = postList.get(position).getpId();
        String pCategory = postList.get(position).getpCategory();
        String pDuration = postList.get(position).getpDuration();
        String pTitle = postList.get(position).getpTitle();
        String pDescription = postList.get(position).getpDescr();
        String pImage = postList.get(position).getpImage();
        String pTime = postList.get(position).getpTime();

        //convert timestamp to dd/mm/yyyy hh:mm am/pm
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(pTime));
        String pTimestamp = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();

        //set data
        holder.username_txt.setText(uName);
        holder.postTime_txt.setText(pTimestamp);
        holder.postTitle_txt.setText(pTitle);
        holder.postDescription_txt.setText("Description: " + pDescription);
        holder.postCategory_txt.setText("Category: " + pCategory);
        holder.postDuration_txt.setText("Duration: " + pDuration);
        holder.postImage_image.setVisibility(View.GONE);

        //set user dp
        /*try {
            Picasso.get().load(uDp).placeholder(R.drawable.ic_baseline_emoji_emotions_24).into(holder.userPfp_image);
        }
        catch (Exception e) {}

        //set post image
        //if there is no image 9.e. pImage.equals("noImage") then hide ImageView
        if (pImage.equals("noImage")) {
            //hide imageview
            holder.postImage_image.setVisibility(View.GONE);
        }
        else {
            try {
                Picasso.get().load(pImage).into(holder.postImage_image);
            }
            catch (Exception e) {}
        }*/

        //handle button clicks
        holder.lend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //will implement later
                dialogBuilder = new AlertDialog.Builder(context);
                confirm_lend = LayoutInflater.from(context).inflate(R.layout.confirm_lend, null);
                dialogBuilder.setView(confirm_lend);
                dialog = dialogBuilder.create();
                dialog.show();

                Button yesLend_button = confirm_lend.findViewById(R.id.yesLend_button);
                Button noLend_button = confirm_lend.findViewById(R.id.noLend_button);

                yesLend_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        firebaseDatabase = FirebaseDatabase.getInstance();
                        databaseReference = firebaseDatabase.getReference("Users");
                        String username = holder.username_txt.getText().toString();
                        Query query = databaseReference.orderByChild("name").equalTo(username);
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                                //check until required data get
                                for (DataSnapshot ds: datasnapshot.getChildren())
                                    phone = ""+ds.child("phone").getValue();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {}
                        });
                        addLendNotification(uName, phone, pTitle);
                        firebaseDatabase.getReference("Lend_Posts").child(pId).removeValue();
                    }
                });

                noLend_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        //show user profile
        holder.userPfp_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder = new AlertDialog.Builder(context);
                user_view = LayoutInflater.from(context).inflate(R.layout.user_profile, null);
                dialogBuilder.setView(user_view);
                dialog = dialogBuilder.create();

                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference("Users");
                String username = holder.username_txt.getText().toString();
                Query query = databaseReference.orderByChild("name").equalTo(username);
                query.addValueEventListener(new ValueEventListener() {
                    TextView user_name = user_view.findViewById(R.id.nameTv);
                    TextView user_email = user_view.findViewById(R.id.emailTv);
                    TextView user_phone = user_view.findViewById(R.id.phoneTv);
                    TextView user_school = user_view.findViewById(R.id.schoolTv);
                    TextView user_resHall = user_view.findViewById(R.id.resHallTv);
                    ImageView user_avatar = user_view.findViewById(R.id.avatarIv);

                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                        //check until required data get
                        for (DataSnapshot ds: datasnapshot.getChildren()) {
                            //get data
                            String name = ""+ds.child("name").getValue();
                            String email = ""+ds.child("email").getValue();
                            String phone = ""+ds.child("phone").getValue();
                            String image = ""+ds.child("image").getValue();
                            String school = ""+ds.child("school").getValue();
                            String resHall = ""+ds.child("resHall").getValue();

                            //set data
                            user_name.setText(name);
                            user_email.setText(email);
                            user_phone.setText(phone);
                            user_school.setText(school);
                            user_resHall.setText(resHall);

                            try {
                                Picasso.get().load(image).into(user_avatar);
                            }
                            catch(Exception e){
                                Picasso.get().load(R.drawable.ic_person).into(user_avatar);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
                dialog.show();
            }
        });

    }

    private void addLendNotification(String uName, String uPhone, String pTitle) {
        String timeStamp = String.valueOf(System.currentTimeMillis());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference data_ref = database.getReference("Lend_Notifications");
        data_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<Object, String> lendNotification_hashMap = new HashMap<>();
                //put post info
                lendNotification_hashMap.put("otherName", uName);
                lendNotification_hashMap.put("otherPhone", phone);
                lendNotification_hashMap.put("itemName", pTitle);
                data_ref.child(timeStamp).setValue(lendNotification_hashMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    //view holder class
    class MyHolder extends RecyclerView.ViewHolder {

        //declare views from row_posts.xml
        ImageView userPfp_image, postImage_image;
        TextView username_txt, postTime_txt, postTitle_txt, postDescription_txt, postCategory_txt, postDuration_txt;
        Button lend_btn;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            //initialize views
            userPfp_image = itemView.findViewById(R.id.userPfp_image);
            postImage_image = itemView.findViewById(R.id.postImage_image);
            username_txt = itemView.findViewById(R.id.username_txt);
            postTime_txt = itemView.findViewById(R.id.postTime_txt);
            postTitle_txt = itemView.findViewById(R.id.postTitle_txt);
            postCategory_txt = itemView.findViewById(R.id.postCategory_txt);
            postDuration_txt = itemView.findViewById(R.id.postDuration_txt);
            postDescription_txt = itemView.findViewById(R.id.postDescription_txt);
            lend_btn = itemView.findViewById(R.id.lend_btn);

        }
    }
}
