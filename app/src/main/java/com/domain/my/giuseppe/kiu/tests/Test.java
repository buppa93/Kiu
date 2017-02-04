package com.domain.my.giuseppe.kiu.tests;

import com.domain.my.giuseppe.kiu.model.User;

/**
 * Created by giuseppe on 07/11/16.
 */

public class Test
{
    private final static String TAG = "Test - Class";
    User user;

    public Test(User user) {this.user = user;}

    public User getUser() {return user;}

    public void setUser(User user) {this.user = user;}
}
