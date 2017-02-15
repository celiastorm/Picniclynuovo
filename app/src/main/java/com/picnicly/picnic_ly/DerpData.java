package com.picnicly.picnic_ly;

import java.util.ArrayList;
import java.util.List;

import static com.picnicly.picnic_ly.R.drawable.ic_delete_black_36dp;
import static com.picnicly.picnic_ly.R.drawable.ic_place_black_36dp;

// Put here database things
public class DerpData {
    private static final String[] titles  = {"Parco dello Storga", "Parco Forte Boerio",
            "Parco Foundation"} ;
    private static final String[] titles2 = {"4", "5", "-"};
    private static int icon = ic_place_black_36dp;
    private static int icon2 = ic_delete_black_36dp;

    public static List<ListItem> getListData() {
        List<ListItem> data = new ArrayList<>();

        //Repeat process 4 times, so that we have enough data to demonstrate a scrollable
        //RecyclerView
        for (int x = 0; x < 4; x++) {
            //create ListItem with dummy data, then add them to our List
            for (int i = 0; i < titles.length; i++) {
                ListItem item = new ListItem();
                item.setTitle(titles[i]);
                item.setTitle2(titles2[i]);
                item.setImageResId(icon);
                item.setImageResId2(icon2);
                data.add(item);
            }
        }
        return data;
    }
}
