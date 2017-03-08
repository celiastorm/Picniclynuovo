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
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MyAdapterPreferiti extends RecyclerView.Adapter<MyAdapterPreferiti.DerpHolder> {
    private List<Pref> listData;
    private LayoutInflater inflater;

    public MyAdapterPreferiti(List<Pref> listData, Context c){
        inflater = LayoutInflater.from(c);
        this.listData = listData;
    }


    @Override
    public MyAdapterPreferiti.DerpHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = inflater.inflate(R.layout.content_preferiti, parent, false);
        return new DerpHolder(view);
    }

    @Override
    public void onBindViewHolder (final DerpHolder holder, int position){

        //for(int i = 0; i<getItemCount(); i++){
            final Pref item = listData.get(position);
            final int pos = position;
            holder.title.setText(item.getName());
            //holder.icon.setImageResource(item.getImageResId());
            //holder.title2.setText(item.getTitle2());
            //holder.icon2.setImageResource(item.getImageResId2());
            holder.icon.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Double lon = item.getLon();
                    Double lat = item.getLat();
                    String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f, %f", lat, lon);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    holder.container.getContext().startActivity(intent);

            }

        });

        //}

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
                /*listData.remove(item);
                MyAdapterPreferiti adapter = new MyAdapterPreferiti(listData, holder.container.getContext());
                adapter.notifyItemRemoved(listData.size());*/
                    //removeAt(pos);
                    //delete(holder,pos);
                    final Pref item = listData.get(getAdapterPosition());
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference mRootRef = database.getReference();
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Log.i("UTENTE" ,item.getId());
                    mRootRef.child("users").child(uid).child("pref").child(item.getId()).removeValue();
                    listData.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    notifyItemRangeChanged(getAdapterPosition(),listData.size());
                    notifyDataSetChanged();
                    //MyAdapterPreferiti mp = new MyAdapterPreferiti(listData,container.getContext());
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

    public void removeAt(int position) {
        listData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listData.size());
    }

    private void delete(DerpHolder holder, int position) {
        listData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listData.size());
        holder.itemView.setVisibility(View.GONE);
    }
}
