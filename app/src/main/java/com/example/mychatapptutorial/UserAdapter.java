package com.example.mychatapptutorial;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.Viewholder> {

    private Context context;
    private ArrayList<UserModel> arraylist;
    private Map<String, UserModel> users = new HashMap<>();
    private Map<String, Object> userdata = new HashMap<>();
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;



    // Constructor
    public UserAdapter(Context context, ArrayList<UserModel> arraylist) {
        this.context = context;
        this.arraylist = arraylist;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        UserModel userModel = arraylist.get(position);
        holder.courseNameTV.setText(userModel.getName());
        holder.courseRatingTV.setText(""+userModel.getDistance());
        Picasso.get().load(userModel.getimage()).into(holder.courseIV);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, SpecificChatActivity.class);
                intent.putExtra("name",userModel.getName());
                intent.putExtra("receiveruid",userModel.getId());
                intent.putExtra("imageuri",userModel.getimage());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        // this method is used for showing number
        // of card items in recycler view.
        return arraylist.size();
    }

    // View holder class for initializing of
    // your views such as TextView and Imageview.
    public class Viewholder extends RecyclerView.ViewHolder {
        private ImageView courseIV;
        private TextView courseNameTV, courseRatingTV;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            courseIV = itemView.findViewById(R.id.user_image_card);
            courseNameTV = itemView.findViewById(R.id.username_card);
            courseRatingTV = itemView.findViewById(R.id.dis_card);
        }
    }
}
