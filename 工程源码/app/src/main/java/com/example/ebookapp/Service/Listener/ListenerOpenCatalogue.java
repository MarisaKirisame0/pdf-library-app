package com.example.ebookapp.Service.Listener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.ebookapp.CatalogueActivity;
import com.example.ebookapp.widget.TreeNodeData;

import java.io.Serializable;
import java.util.List;

/* 按键点击监听：打开目录(传入该PDF文件的目录信息列表,跳转到CatalogueActivity页面) */
public class ListenerOpenCatalogue implements View.OnClickListener  {
    Activity activity;
    private Context context;
    private List<TreeNodeData> catalogue;

    public ListenerOpenCatalogue(Context context, Activity activity, List<TreeNodeData> catalogue){
        this.context = context;
        this.activity = activity;
        this.catalogue = catalogue;
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setClass(this.context, CatalogueActivity.class);
        intent.putExtra("catalogue", (Serializable) this.catalogue);
        this.activity.startActivityForResult(intent, 200);
    }
}
