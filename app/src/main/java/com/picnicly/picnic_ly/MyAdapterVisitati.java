package com.picnicly.picnic_ly;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;


public class MyAdapterVisitati extends RecyclerView.Adapter<MyAdapterVisitati.DerpHolder> {
    private List<ListItem> listData;
    private LayoutInflater inflater;

    public MyAdapterVisitati(List<ListItem> listData, Context c){
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
        ListItem item = listData.get(position);

        holder.title.setText(item.getTitle());
        holder.icon.setImageResource(item.getImageResId());
        holder.title2.setText(item.getTitle2());
        holder.icon2.setImageResource(item.getImageResId2());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class DerpHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private ImageButton icon;
        private TextView title2;
        private ImageButton icon2;

        public DerpHolder(View itemView){
            super(itemView);

            title = (TextView)itemView.findViewById(R.id.Ibl_item_text);
            icon = (ImageButton)itemView.findViewById(R.id.im_item_icon);
            title2 = (TextView)itemView.findViewById(R.id.Ibl_item_text2);
            icon2 = (ImageButton)itemView.findViewById(R.id.im_item_icon2);

        }
    }
}
