package com.ark.mailauthentication.model;

/**
 * Created by noufal on 26/2/18.
 */

public class UserDetail {

    public String fullName;
    public String phone;
    public String location;

    public UserDetail() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public UserDetail(String fullName, String phone, String location) {
        this.fullName = fullName;
        this.phone = phone;
        this.location = location;
    }
}
