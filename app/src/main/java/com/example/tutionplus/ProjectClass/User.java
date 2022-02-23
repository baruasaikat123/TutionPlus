package com.example.tutionplus.ProjectClass;

public class User {

    String userName;
    String gender;
    String phoneNo;
    String classes;

    public void setUserName(String userName){
        this.userName = userName;
    }

    public void setGender(String gender){
        this.gender = gender;
    }

    public void setPhone(String phoneNo){
        this.phoneNo = phoneNo;
    }

    public void setStandard(String classes) {
        this.classes = classes;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getGender() {
        return gender;
    }

    public String getUserName() {
        return userName;
    }

    public String getClasses() {
        return classes;
    }

}


