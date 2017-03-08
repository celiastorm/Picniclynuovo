package com.picnicly.picnic_ly;

import java.util.List;

/**
 * Created by Giovy on 06/03/2017.
 */

public class User {
    public String uid;
    public String username;
    public String email;



    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, String uid) {
        this.username = username;
        this.email = email;
        this.uid = uid;
    }
}
