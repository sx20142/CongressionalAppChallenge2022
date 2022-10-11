package com.example.shareme.adapters;

import android.content.Context;
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

import com.example.shareme.R;
import com.example.shareme.templates.LendNotificationsTemplate;
import com.example.shareme.templates.LendPostTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class LendNotificationsAdapter extends RecyclerView.Adapter<LendNotificationsAdapter.MyHolder> {

    Context context;
    List<LendNotificationsTemplate> notificationsList;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public LendNotificationsAdapter(Context context, List<LendNotificationsTemplate> notificationsList) {
        this.context = context;
        this.notificationsList = notificationsList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.lend_notification_posts, parent, false);
        return new MyHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        //get data
        String otherName = notificationsList.get(position).getOtherName();
        String itemName = notificationsList.get(position).getItemName();
        String otherPhone = notificationsList.get(position).getOtherPhone();

        holder.name_item_txt.setText("You lent " + otherName + " a " +  itemName);
        holder.otherPhone_txt.setText("Please message " + otherPhone + " to sort out the details!");
    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    //view holder class
    class MyHolder extends RecyclerView.ViewHolder {

        //declare views from row_posts.xml
        ImageView userPfp_image;
        TextView name_item_txt, otherPhone_txt;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            //initialize views
            userPfp_image = itemView.findViewById(R.id.userPfp_image);
            name_item_txt = itemView.findViewById(R.id.name_item_txt);
            otherPhone_txt = itemView.findViewById(R.id.otherPhone_txt);
        }
    }
}
