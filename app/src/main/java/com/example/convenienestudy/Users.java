package com.example.convenienestudy;

import java.util.UUID;

public abstract class Users {

    private static int idCounter;
    private String name, email, userId;
    private String schoolId;

    public Users() {
    }

    public Users(String name, String email, String schoolID, String userId){
        this.name = name;
        this.email = email;
        this.schoolId = schoolID;
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUserId() {
        return userId;
    }

    public String getSchoolId() {
        return schoolId;
    }
}
