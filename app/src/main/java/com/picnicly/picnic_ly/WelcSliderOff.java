package com.picnicly.picnic_ly;

/**
 * Created by Giovy on 10/03/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class WelcSliderOff extends AppCompatActivity {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private int[] layout;
    private PrefManager prefManager;
    private Button btnSkip,btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welc_slider);


        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }

        viewPager = (ViewPager)findViewById(R.id.view_pager);

        layout = new int[]{
                R.layout.welc1,
                R.layout.welc2,
                R.layout.welc3,
                R.layout.welc4,
                R.layout.welc5 };

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);

        btnSkip.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                launchHomeScreen();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < layout.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
            }
        });
    }

    private int getItem (int i){ return viewPager.getCurrentItem() + i; }

    public void launchHomeScreen(){ // TODO: Method to change with Log In Activity
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(WelcSliderOff.this, HomeActivityOff.class));
        finish();
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {


            if (position == layout.length - 1) {
                // last page. make button text to GOT IT
                btnNext.setText(getString(R.string.gotit));
                btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                btnNext.setText(getString(R.string.next));
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {        }

        @Override
        public void onPageScrollStateChanged(int arg0) {        }

    };

    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;


        @Override
        public Object instantiateItem(ViewGroup container, int position){
            layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View v = layoutInflater.inflate(layout[position],container,false);
            container.addView(v);

            return v;
        }

        @Override
        public int getCount(){ return layout.length; }

        @Override
        public boolean isViewFromObject(View v, Object obj){ return v == obj; }

        @Override
        public void destroyItem(ViewGroup container, int position, Object obj){
            View v = (View) obj;
            container.removeView(v);
        }
    }
}

