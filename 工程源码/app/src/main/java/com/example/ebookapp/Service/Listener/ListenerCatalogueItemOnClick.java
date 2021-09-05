package com.example.ebookapp.Service.Listener;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

/* 按键点击监听：获取被点击的目录项页码传回PdfViewActivity页面 */
public class ListenerCatalogueItemOnClick implements View.OnClickListener {

    private Activity activity;
    private int PageNum;

    public ListenerCatalogueItemOnClick(Activity activity,int PageNum){
        this.activity = activity;
        this.PageNum = PageNum;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.putExtra("pageNum",this.PageNum);
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }
}
