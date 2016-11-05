package com.wangjw.imagelibdemo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.wangjw.imagelib.PhotoSelectHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PhotoSelectHelper.OnPhotoSelectListener {

    private static final String TAG = "MainActivity";

    private static final int REQ_CODE_CAMERA = 1;

    private Button mBtnAddPhoto;
    private RecyclerView mRecyclerView;

    private PhotoAcapter mAdapter;
    private PhotoSelectHelper mPhotoHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        mBtnAddPhoto = (Button) findViewById(R.id.Button_Add_Photo);
        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mAdapter = new PhotoAcapter(this);
        mRecyclerView.setAdapter(mAdapter);

        mPhotoHandler = new PhotoSelectHelper(this, StorageUtils.getOwnCacheDirectory(this, AppConfig.IMAGE_CACHE_PATH));
        mPhotoHandler.setOnPhotoSelectListener(this);

        mBtnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setItems(R.array.add_img_choice, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            checkCamera();
                        } else if (which == 1) {
                            mPhotoHandler.selectPhotoFromGallery();
                        }
                    }
                });
                builder.create().show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(mPhotoHandler.onActivityResult(requestCode, resultCode, data)) {

        }
    }

    @Override
    public void onPhotoSelectFail(int errorType, String errorMsg) {

    }

    @Override
    public void onPhotoSelectSucc(Intent intent, File file) {
        if(file != null) {
            Log.d(TAG, "image path = " + file.getAbsolutePath());
            mAdapter.addData(file);
        }
    }

    private void checkCamera() {
        int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int writeStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        Log.d(TAG, "permission = " + cameraPermission + "," + writeStoragePermission);
        List<String> permissList = new ArrayList<String>();
        if(cameraPermission != PackageManager.PERMISSION_GRANTED) {
            permissList.add(Manifest.permission.CAMERA);
        }
        if(writeStoragePermission != PackageManager.PERMISSION_GRANTED) {
            permissList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(permissList.size() > 0) {
            String[] arr = new String[permissList.size()];
            for(int i=0; i<arr.length; i++) {
                arr[i] = permissList.get(i);
            }
            ActivityCompat.requestPermissions(this, arr, REQ_CODE_CAMERA);
        } else {
            mPhotoHandler.takePhoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQ_CODE_CAMERA && grantResults.length > 0) {
            boolean grantAll = true;
            for(int r : grantResults) {
                if(r != PackageManager.PERMISSION_GRANTED) {
                    grantAll = false;
                    break;
                }
            }
            if(grantAll) {
                mPhotoHandler.takePhoto();
            } else {
                toastNeedPermission();
            }
        }
    }

    private void toastNeedPermission() {
        Toast.makeText(this, "请到系统设置里开启相关权限, 否则不能继续使用", Toast.LENGTH_SHORT).show();
    }
}
