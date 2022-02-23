package com.example.tutionplus.ProjectClass;

public class GetEmoji {

    public String getEmoji(int code){

        String emoji = new String(Character.toChars(code));
        return emoji;
    }
}
