package com.halo.myapplication;

import android.annotation.Nullable;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

/**
 * @author zhnaghaopneg
 */
public class SecondActivity extends AppCompatActivity {
    private static final String TAG = "SecondActivity";
    private HashMap<Test,Integer> hashMap = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_jump_album).setOnClickListener(v -> {
            hashMap.put(new Test(),1);
            hashMap.put(new Test(),2);
            for(int i = 0;i < hashMap.size();i++) {
                Log.e(TAG,"");
            }
        });
    }
}