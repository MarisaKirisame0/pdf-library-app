package com.example.ebookapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ebookapp.Service.Listener.ListenerToolbarOnClick;
import com.example.ebookapp.Service.Manager.FileManager;
import com.example.ebookapp.Service.Listener.ListenerCreateShell;
import com.example.ebookapp.Service.Manager.ViewManager;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * @author qsj
 * 日期：2021.08.23
 * 主页面类
 */
public class MainActivity extends AppCompatActivity {

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context = getApplicationContext();

        /* 隐藏最原始的上边的标题栏 */
        Objects.requireNonNull(getSupportActionBar()).hide();

        /*  文件管理器初始化 */
        FileManager fileManager = new FileManager(context);
        if(!fileManager.libraryInit()){
            Toast.makeText(getApplicationContext(), (String) this.getResources().getString(R.string.LibraryInitError), Toast.LENGTH_LONG).show();
        }
        try {
            if(!fileManager.recentReadRecordFileInit()){
                Toast.makeText(getApplicationContext(), (String) this.getResources().getString(R.string.recentReadRecordFileInitError), Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* ViewManager初始化 */
        ViewManager viewManager = new ViewManager(this);

        /* 获取书架列表 */
        List<String> shells = fileManager.shells();
        LinearLayout linear = findViewById(R.id.linear_layout);

        /* 主页面的ToolBar初始化 */
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnClickListener(new ListenerToolbarOnClick(this));
        toolbar.setSubtitle(this.getResources().getStringArray(R.array.subtitle)[(int) (Math.random()*(5))]);
        /* 设置创建书架的监听器 */
        ImageView imageViewCreateShell = findViewById(R.id.createShell);
        imageViewCreateShell.setOnClickListener(new ListenerCreateShell(this, shells, linear, fileManager, shells.size()));

        /* 显示"最近阅读"书架 */
        try {
            viewManager.DisplayRecentReadShell(linear, fileManager);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* 主页面显示书架 */
        // 在android studio里面这里会划红显示错误，没有影响，忽略即可
        viewManager.DisplayShells(shells, linear, fileManager);
    }
}