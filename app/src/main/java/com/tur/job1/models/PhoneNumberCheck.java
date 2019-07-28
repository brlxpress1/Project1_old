package com.tur.job1.models;

import com.google.gson.annotations.SerializedName;

public class PhoneNumberCheck {

    @SerializedName("userExistr")
    private boolean userExist;

    @SerializedName("userSobseeker")
    private  boolean userSobseeker;


    public boolean isUserExist() {
        return userExist;
    }

    public void setUserExist(boolean userExist) {
        this.userExist = userExist;
    }

    public boolean isUserSobseeker() {
        return userSobseeker;
    }

    public void setUserSobseeker(boolean userSobseeker) {
        this.userSobseeker = userSobseeker;
    }
}
