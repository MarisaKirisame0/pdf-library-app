package com.example.ebookapp.Service.Listener;

import android.content.Context;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.example.ebookapp.R;

public class ListenerToolbarOnClick implements View.OnClickListener  {
    private Context context;

    public ListenerToolbarOnClick(Context context) {
        this.context = context;
    }
    @Override
    public void onClick(View v) {
        View view = View.inflate(this.context, R.layout.alert_developinfo, null);
        // 初始化弹窗(用于确定书架名)
        final AlertDialog.Builder DeveloperInfoAlert = new AlertDialog.Builder(this.context);
        // 弹窗配置与创建
        DeveloperInfoAlert
                .setTitle((String) context.getResources().getString(R.string.developInfo_alert_title))
                .setView(view)
                .setIcon(R.mipmap.my_icon_layer)
                .create();
        // 弹窗显示
        final AlertDialog show = DeveloperInfoAlert.show();
    }
}
