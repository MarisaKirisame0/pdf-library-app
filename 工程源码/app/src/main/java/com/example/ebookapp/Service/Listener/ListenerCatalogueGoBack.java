package com.example.ebookapp.Service.Listener;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

/* 目录页面的返回按键的点击监听器 */
public class ListenerCatalogueGoBack implements View.OnClickListener {
    private Activity activity;

    public ListenerCatalogueGoBack(Activity activity){
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        // 设置页码为负数传回到之前的页面，结束开启目录页面的任务
        intent.putExtra("pageNum", -1);
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }
}
