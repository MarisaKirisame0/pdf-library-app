package com.example.ebookapp.Service.Listener;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.ebookapp.SearchBookActivity;

/* 按键点击监听：页面跳转至添加电子书到书架 */
public class ListenerAddBook implements View.OnClickListener {
    private Context context;
    private String shell_path;

    public ListenerAddBook(Context context, String shell_path){
        this.context = context;
        this.shell_path = shell_path;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setClass(this.context, SearchBookActivity.class);
        intent.putExtra("ShellName", this.shell_path);
        this.context.startActivity(intent);
    }
}
