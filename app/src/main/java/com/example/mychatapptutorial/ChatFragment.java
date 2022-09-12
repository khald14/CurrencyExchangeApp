package com.example.mychatapptutorial;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ChatFragment extends Fragment {

    private FirebaseFirestore firebaseFirestore;
    LinearLayoutManager linearLayoutManager;
    private FirebaseAuth firebaseAuth;

    ImageView mimageviewofuser;

    FirestoreRecyclerAdapter<FirebaseModel,NoteViewHolder> chatAdapter;

    RecyclerView mrecyclerview;
    private ArrayList<String> users = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    ArrayList<Messages> messagesArrayList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View v=inflater.inflate(R.layout.chatfragment,container,false);

       firebaseAuth=FirebaseAuth.getInstance();
       firebaseFirestore= FirebaseFirestore.getInstance();
       mrecyclerview=v.findViewById(R.id.recyclerview);

       firebaseFirestore.collection("Users")
               .document(firebaseAuth.getUid())
               .get()
               .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                       DocumentSnapshot document = task.getResult();
                       users.addAll((ArrayList<String>) document.get("users_i_messaged"));
                   }
               }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                       Query query=firebaseFirestore.collection("Users").whereIn("uid", users);
                       FirestoreRecyclerOptions<FirebaseModel> allusername=new FirestoreRecyclerOptions.Builder<FirebaseModel>().setQuery(query, FirebaseModel.class).build();

                       chatAdapter=new FirestoreRecyclerAdapter<FirebaseModel, NoteViewHolder>(allusername) {
                           @Override
                           protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i, @NonNull FirebaseModel firebasemodel) {

                               noteViewHolder.particularusername.setText(firebasemodel.getName());
                               String uri=firebasemodel.getImage();

                               Picasso.get().load(uri).into(mimageviewofuser);
                               if(firebasemodel.getStatus().equals("Online"))
                               {
                                   noteViewHolder.statusofuser.setText(firebasemodel.getStatus());
                                   noteViewHolder.statusofuser.setTextColor(Color.GREEN);
                               }
                               else
                               {
                                   noteViewHolder.statusofuser.setText(firebasemodel.getStatus());
                               }

                               noteViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                       Intent intent=new Intent(getActivity(), SpecificChatActivity.class);
                                       intent.putExtra("name",firebasemodel.getName());
                                       intent.putExtra("receiveruid",firebasemodel.getUid());
                                       intent.putExtra("imageuri",firebasemodel.getImage());
                                       startActivity(intent);
                                   }
                               });
                           }

                           @NonNull
                           @Override
                           public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                               View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.chatviewlayout,parent,false);
                               return new NoteViewHolder(view);
                           }
                       };


                       mrecyclerview.setHasFixedSize(true);
                       linearLayoutManager=new LinearLayoutManager(getContext());
                       linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                       mrecyclerview.setLayoutManager(linearLayoutManager);
                       mrecyclerview.setAdapter(chatAdapter);
                       chatAdapter.startListening();
                   }
               });

        return v;




    }


    public class NoteViewHolder extends RecyclerView.ViewHolder
    {

        private TextView particularusername;
        private TextView statusofuser;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            particularusername=itemView.findViewById(R.id.name_of_user);
            statusofuser=itemView.findViewById(R.id.status_of_user);
            mimageviewofuser=itemView.findViewById(R.id.image_view_of_user);




        }
    }

    @Override
    public void onStart() {
        super.onStart();
//        chatAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
//        if(chatAdapter!=null)
//        {
//            chatAdapter.stopListening();
//        }
    }
}
