package com.example.lonelytwitter;


import java.util.Date;

public class Sad extends Mood {
    public Sad() {
        super();
    }

    public Sad(Date date) {
        super(date);
    }

    @Override
    public String checkMood() {
        return "I am Sad (aww)";
    }
}

