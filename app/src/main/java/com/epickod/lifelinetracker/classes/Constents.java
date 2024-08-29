package com.epickod.lifelinetracker.classes;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

public class Constents {
    @NonNull
    @Contract(pure = true)
    public static String api_url() {
//        return "https://fyp.epickod.com/api/";
        return "http://192.168.100.93:8000/api/";

    }
}
