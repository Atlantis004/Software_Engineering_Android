package com.example.lonelytwitter;

import java.util.Date;

public class Happy extends Mood{
    public Happy() {
        super();
    }

    public Happy(Date date) {
        super(date);
    }

    @Override
    public String checkMood() {
        return "I am happy (yay)";
    }
}
