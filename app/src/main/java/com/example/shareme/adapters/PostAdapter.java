package com.example.shareme.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shareme.R;
import com.example.shareme.templates.PostTemplate;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyHolder> {

    Context context;
    List<PostTemplate> postList;

    public PostAdapter(Context context, List<PostTemplate> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_posts, parent, false);
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
        holder.postDescription_txt.setText(pDescription);

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
        holder.more_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //will implement later
                Toast.makeText(context, "More", Toast.LENGTH_SHORT).show();
            }
        });
        holder.lendBorrow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //will implement later
                Toast.makeText(context, "Lend/Borrow", Toast.LENGTH_SHORT).show();
            }
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
        TextView username_txt, postTime_txt, postTitle_txt, postDescription_txt;
        ImageButton more_btn;
        Button lendBorrow_btn;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            //initialize views
            userPfp_image = itemView.findViewById(R.id.userPfp_image);
            postImage_image = itemView.findViewById(R.id.postImage_image);
            username_txt = itemView.findViewById(R.id.username_txt);
            postTime_txt = itemView.findViewById(R.id.postTime_txt);
            postTitle_txt = itemView.findViewById(R.id.postTitle_txt);
            postDescription_txt = itemView.findViewById(R.id.postDescription_txt);
            more_btn = itemView.findViewById(R.id.more_btn);
            lendBorrow_btn = itemView.findViewById(R.id.lendBorrow_btn);

        }
    }
}
