package com.picnicly.picnic_ly;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;
import java.util.Locale;


public class MyAdapterVisitati extends RecyclerView.Adapter<MyAdapterVisitati.DerpHolder> {
    private List<Vis> listData;
    private LayoutInflater inflater;

    public MyAdapterVisitati(List<Vis> listData, Context c){
        inflater = LayoutInflater.from(c);
        this.listData = listData;
    }

    @Override
    public MyAdapterVisitati.DerpHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = inflater.inflate(R.layout.content_visitati, parent, false);
        return new DerpHolder(view);
    }

    @Override
    public void onBindViewHolder (DerpHolder holder, int position){
        Vis item = listData.get(position);
        holder.title.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class DerpHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView title;
        private Button icon;
        private TextView title2;
        private Button icon2;
        View container;

        public DerpHolder(final View itemView){
            super(itemView);

            title = (TextView)itemView.findViewById(R.id.Ibl_item_text);
            icon = (Button)itemView.findViewById(R.id.im_item_icon);
            title2 = (TextView)itemView.findViewById(R.id.Ibl_item_text2);
            icon2 = (Button)itemView.findViewById(R.id.im_item_icon2);
            container = (View)itemView.findViewById(R.id.cont_item_root);
            container.setOnClickListener(this);

            icon2.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Vis item = listData.get(getAdapterPosition());
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference mRootRef = database.getReference();
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Log.i("UTENTE" ,item.getId());

                    listData.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    notifyItemRangeChanged(getAdapterPosition(),listData.size());
                    notifyDataSetChanged();
                    mRootRef.child("users").child(uid).child("vis").child(item.getId()).removeValue();

                }

            });
            icon.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Vis item = listData.get(getAdapterPosition());
                    Double lon = item.getLon();
                    Double lat = item.getLat();
                    String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f, %f", lat, lon);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    container.getContext().startActivity(intent);

                }

            });
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.cont_item_root){

            }else{

            }
        }

    }

}
