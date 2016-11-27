package com.example.xdeveloper.simplewebstuff;

/**
 * Created by frankie on 27/11/2016.
 */

public class Comment {

    public final String comment;
    public final String mark;
    public final String created_at;

    public Comment (String comment, String mark, String time){
        this.comment = comment;
        this.mark = mark;
        this.created_at = time;

    }


    public String get_time(){
        return this.created_at;

    }

    public String get_comment(){
        return this.comment;

    }
    public String get_mark(){
        return this.mark;

    }

}
