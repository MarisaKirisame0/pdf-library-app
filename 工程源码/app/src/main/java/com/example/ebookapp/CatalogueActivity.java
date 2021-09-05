package com.example.ebookapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;

import android.os.Build;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.ebookapp.Service.Listener.ListenerCatalogueGoBack;
import com.example.ebookapp.Service.Manager.ViewManager;
import com.example.ebookapp.widget.TreeNodeData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

/**
 * @author qsj
 * 日期：2021.08.26
 * 目录页面类
 */
public class CatalogueActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogue);

        /* 隐藏最原始的上边的标题栏 */
        Objects.requireNonNull(getSupportActionBar()).hide();

        // 获取页面传参得到的目录列表（此处会显示警告，忽略即可）
        List<TreeNodeData> catalogue = (List<TreeNodeData>) getIntent().getSerializableExtra("catalogue");

        // 找到用于存放目录项显示的LinerLayout布局
        LinearLayout linear = findViewById(R.id.CatalogueLinerLayout);
        // 初始化该页面的ViewManager
        ViewManager viewManager = new ViewManager(this);
        // 设置目录展示
        viewManager.DisplayCatalogue(this, linear, catalogue);

        /* 初始化目录页面的返回按键 */
        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        /* 设置返回键点击监听器 */
        floatingActionButton.setOnClickListener(new ListenerCatalogueGoBack(this));
    }
}