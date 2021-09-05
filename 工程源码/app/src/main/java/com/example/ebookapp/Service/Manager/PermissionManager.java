package com.example.ebookapp.Service.Manager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

/**
 * @author qsj
 * time： 2021.08.25
 * 权限控制管理器服务：
 * 1.对 READ_EXTERNAL_STORAGE、WRITE_EXTERNAL_STORAGE权限的申请
 * 2.动态申请SD存储的读写权限
 **/
public class PermissionManager {

    private Activity activity;
    private int HasRead;
    private int HasWrite;
    private int REQUEST_CODE_ASK_PERMISSIONS;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public PermissionManager(Activity activity, Context context){
        this.activity = activity;
        this.HasRead = context.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        this.HasWrite = context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        this.REQUEST_CODE_ASK_PERMISSIONS = 123;
    }

    /* 动态申请权限 */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void RequestPermission(){
        if(this.HasRead != PackageManager.PERMISSION_GRANTED)
        {
            activity.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
        }
        if(this.HasWrite != PackageManager.PERMISSION_GRANTED)
        {
            activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
        }
    }
}

