package com.example.ebookapp.Service.Listener;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.example.ebookapp.R;
import com.example.ebookapp.Service.Service.UriFileService;

public class ListenerSearchItemOnClick implements AdapterView.OnItemClickListener {
    private Context context;
    private UriFileService uriFileService;
    private String shellPath;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public ListenerSearchItemOnClick(Context context, UriFileService uriFileService, String shellPath) {
        this.context = context;
        this.uriFileService = uriFileService;
        this.shellPath = shellPath;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        View alertView = AdapterView.inflate(this.context, R.layout.alert_selectbook, null);

        final AlertDialog.Builder alert = new AlertDialog.Builder(this.context);

        // 文本显示区域
        TextView textView = alertView.findViewById(R.id.textView);
        textView.setText((String) context.getResources().getString(R.string.searchItemClick_alert_textView));
        // 确认键
        Button btn_yes = alertView.findViewById(R.id.button_yes);
        // 取消键
        Button btn_no = alertView.findViewById(R.id.button_no);
        alert
                .setTitle((String) context.getResources().getString(R.string.searchItemClick_alert_title))
                .setView(alertView)
                .setIcon(R.mipmap.my_icon_layer)
                .create();

        final AlertDialog show = alert.show();

        btn_yes.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                // 确定所点击项目的对应文件Uri
                Uri fileUri = uriFileService.getResult().get(position);
                // 调用UriFileService执行文件复制
                uriFileService.copyFile(fileUri, shellPath);
                //显示告知
                Toast.makeText(context, "添加成功！", Toast.LENGTH_LONG).show();
                show.dismiss();
            }
        });

        btn_no.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                show.dismiss();
            }
        });

    }
}
