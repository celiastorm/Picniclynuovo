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

    private int[] image_resources = {R.drawable.picnic,R.drawable.picnic2,R.drawable.ic_visitati,R.drawable.side_nav_bar,R.drawable.ic_tonality_black_36dp};
    public String[] stringa = {"Paolo Vedorin: ","Mariagiovanna Czarnecki: ","Dora Pavan: ","Martina Lunardi: ","Claudia Farronato: "};
    public String[] stringa2 = {"nato a Treviso, studente presso il Liceo Classico 'A.Canova' di Treviso, ora è laureando in scienze informatiche presso l'università Ca' Foscari",
            "nata a Treviso, studentessa presso il Liceo Classico 'A.Canova' di Treviso, ora è laureanda in tecnologia e scienze dell'informazione presso l'universtà Ca' Foscari, Venezia",
            "cose",
            "cose",
            "cose"};
    private Context ctx;
    private LayoutInflater layoutInflater;

    public CustomSwipeAdapter(Context ctx){
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return image_resources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){
        layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = layoutInflater.inflate(R.layout.swipe_layout,container,false);
        ImageView imageView = (ImageView)item_view.findViewById(R.id.image_view);
        TextView textView = (TextView) item_view.findViewById(R.id.image_count);
        imageView.setImageResource(image_resources [position] );
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
