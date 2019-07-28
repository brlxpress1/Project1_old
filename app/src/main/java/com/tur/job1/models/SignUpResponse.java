package com.tur.job1.models;

import java.io.Serializable;

public class SignUpResponse implements Serializable {

    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
