package com.halo.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.Nullable;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

/**
 * @author zhnaghaopneg
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private final int REQUEST_CODE_TAKE_PHOTO = 100;
    private final int REQUEST_CODE_RECORD_VIDEO = 101;
    private final int REQUEST_CODE_PICK_PHOTO = 102;
    private ImageView imageView;
    private TextView textView;
    private final String AUTH_FILE_PROVIDER = "com.cakes.democamera.fileprovider";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.iv);
        textView = findViewById(R.id.tv);
        findViewById(R.id.btn_jump_camera).setOnClickListener(v -> {
            startActivity(new Intent(this,SecondActivity.class));
        });

        findViewById(R.id.btn_jump_album).setOnClickListener(v -> {
            pickPicture();
        });
    }

    private void callSysCameraAppToTakePhoto() {
        // 拍照
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 表示跳转至相机的拍照界面
        if (intent.resolveActivity(getPackageManager()) != null) {
            //这句作用是如果没有相机则该应用不会闪退，要是不加这句则当系统没有相机应用的时候该应用会闪退
            // 表示录制完后保存的录制，如果不写，则会保存到默认的路径，在onActivityResult()的回调，通过intent.getData中返回保存的路径
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            //启动相机
            startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
        } else {
            Log.e(TAG, "callSysCameraAppToTakePhoto() -- error: open camera App failed");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult() --- 111111111, requestCode = " + requestCode
                + ", resultCode = " + resultCode);

        if (null == data) {
            /* 如果在调用startActivityForResult()之前设置照片的路径，那么这里返回的data为空，
             获取拍照的数据就要设置的文件中读取。*/
            Log.e(TAG, "onActivityResult() --- error: 1111111");
        }
        if (resultCode == RESULT_OK && null != data) {
            if (requestCode == REQUEST_CODE_TAKE_PHOTO) {
                Log.d(TAG, "onActivityResult() --- 2222222222");
                Uri uri = data.getData();
                // 视频的保存路径
                if (null != uri) {
                    textView.setText(uri.toString());
                } else {
                    Log.e(TAG, "onActivityResult() --- error: 22222");
                }
                /*缩略图信息是储存在返回的intent中的Bundle中的，
                 * 对应Bundle中的键为data，因此从Intent中取出
                 * Bundle再根据data取出来Bitmap即可*/

                Bundle extras = data.getExtras();
                Bitmap bitmap = (Bitmap) extras.get("data");
                Log.d(TAG, "onActivityResult() ---  bitmap.getWidth = " + bitmap.getWidth()
                        + " bitmap.getHeight = " + bitmap.getHeight());
                imageView.setImageBitmap(bitmap);

            } else if (requestCode == REQUEST_CODE_RECORD_VIDEO) {
                // 视频的保存路径
                Uri uri = data.getData();
                if (null != uri) {
                    textView.setText(uri.toString());
                    Log.i(TAG, "videoPath = " + uri.toString());
                } else {
                    Log.e(TAG, "onActivityResult() --- error: 444444444");
                }

                // 不设置保存路径的情况下，data.getExtras()在有些机型中返回的是null；而在有些机型中extras.get("data")返回的是null
//                Bundle extras = data.getExtras();
//                Object oc = extras.get("data");
//                if (null == oc) {
//                    Log.e(TAG, "onActivityResult() --- error: 555555");
//                } else {
//                    Log.i(TAG, "onActivityResult() --- 44444444444");
//                }
            } else if (requestCode == REQUEST_CODE_PICK_PHOTO) {
                Log.d(TAG, "onActivityResult() --- 555555555");
                Uri uri = data.getData();
                // 视频的保存路径
                if (null != uri) {
                    textView.setText(uri.toString());
                } else {
                    Log.e(TAG, "onActivityResult() --- error: 5555555555555");
                }
                /*缩略图信息是储存在返回的intent中的Bundle中的，
                 * 对应Bundle中的键为data，因此从Intent中取出
                 * Bundle再根据data取出来Bitmap即可*/

                Bitmap bitmapLocal = null;
                // 根据URI找到图片真正的路径
                String imagePath = getRealPathFromURI(uri);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                bitmapLocal = BitmapFactory.decodeFile(imagePath, options);
                imageView.setImageBitmap(bitmapLocal);
            }
        } else {
            Log.e(TAG, "onActivityResult() --- error: 333333333333");
        }
    }

    public void pickPicture() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_PICK_PHOTO);
    }


    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        //不能直接调用contentprovider的接口函数，需要使用contentresolver对象，通过URI间接调用contentprovider
        if (cursor == null) {
            // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }


}