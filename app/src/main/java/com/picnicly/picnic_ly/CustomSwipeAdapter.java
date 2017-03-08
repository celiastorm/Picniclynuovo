package com.picnicly.picnic_ly;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class CustomSwipeAdapter extends PagerAdapter {

    public String[] stringa = {"Paolo Vedorin","Mariagiovanna Czarnecki","Dora Pavan","Martina Lunardi","Claudia Farronato"};
    public String[] stringa2 = {"Nato a Treviso, studente presso il Liceo Classico 'A.Canova' di Treviso indirizzo classico, ora è laureando in scienze informatiche presso l'università Ca' Foscari",
            "Nata a Treviso, diplomata presso il Liceo Classico 'A.Canova' di Treviso indirizzo classico, ora è laureanda in tecnologia e scienze dell'informazione presso l'università Ca' Foscari, Venezia",
            "Nata a Treviso, diplomata presso il Liceo Classico 'A.Canova' di Treviso indirizzo linguistico, ora è laureanda in tecnologia e scienze dell'informazione presso l'università Ca' Foscari, Venezia",
            "Nata ad Adria, diplomata presso 'I.I.S. Polotecnico' di Adria, ora è laureanda in tecnologia e scienze dell'informazione presso l'università Ca' Foscari, Venezia",
            "Nata a Bassano del Grappa, diplomata presso 'ITCG Luigi Einaudi' di Bassano del Grappa, ora è laureanda in tecnologia e scienze dell'informazione presso l'università Ca' Foscari, Venezia"};
    private Context ctx;
    private LayoutInflater layoutInflater;

    public CustomSwipeAdapter(Context ctx){
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){
        layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = layoutInflater.inflate(R.layout.swipe_layout,container,false);
        TextView textView = (TextView) item_view.findViewById(R.id.image_count);
        TextView textView2 = (TextView) item_view.findViewById(R.id.image_count2);
        textView.setText(stringa [position]);
        textView2.setText(stringa2 [position]);
        container.addView(item_view);
        return item_view;
    }
    /*to keep freer memory = faster app */
    @Override
    public void destroyItem (ViewGroup container, int position, Object object){
        container.removeView((LinearLayout)object);
    }
}