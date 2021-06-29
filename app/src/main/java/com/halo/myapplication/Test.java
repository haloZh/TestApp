package com.halo.myapplication;

import android.util.Log;

public class Test {

    {
        int c = 9;
    }

    public static void main(String[] args) {

    }

    static {
        a = 0;
    }

    public static final int a;
    public final int b;

    public Test() {
        b = 0;
        Log.e("test", "b: " + b);
    }
}
