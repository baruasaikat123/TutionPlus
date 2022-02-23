package com.example.tutionplus.ProjectClass;

import com.android.volley.toolbox.StringRequest;

public class Teacher extends User{

   int fees;
   String location;
   String subject;
   String patterns;
   String status;
   String mode;
   String time;

   public void setFees(int fees){
       this.fees = fees;
   }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setSubject(String subject) { this.subject = subject; }

    public void setPatterns(String patterns) { this.patterns = patterns; }

    public void setMode(String mode) { this.mode = mode; }

    public void setTime(String time) { this.time = time; }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getFees(){
       return fees;
   }

    public String getLocation() {
        return location;
    }

    public String getSubject() { return subject; }

    public String getPatterns(){ return patterns; }

    public String getStatus() {
        return status;
    }

    public String getMode() { return mode; }

    public String getTime() { return time; }
}
