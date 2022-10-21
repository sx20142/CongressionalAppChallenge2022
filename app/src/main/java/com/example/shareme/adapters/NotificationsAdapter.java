package com.example.shareme.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shareme.R;
import com.example.shareme.templates.NotificationsTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.MyHolder> {

    Context context;
    List<NotificationsTemplate> notificationsList;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public NotificationsAdapter(Context context, List<NotificationsTemplate> notificationsList) {
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
        String lendOrBorrow = notificationsList.get(position).getLendOrBorrow();

        if (lendOrBorrow == "borrow")
            holder.name_item_txt.setText("You lent " + otherName + " a " +  itemName);
        else
            holder.name_item_txt.setText("You borrowed a(n) " + itemName + " from " +  otherName);
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
