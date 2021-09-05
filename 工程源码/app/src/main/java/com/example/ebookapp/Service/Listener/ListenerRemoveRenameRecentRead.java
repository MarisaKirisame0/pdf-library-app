package com.example.ebookapp.Service.Listener;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.ebookapp.R;
import com.example.ebookapp.Service.Manager.FileManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/* 长按监听器：用于对“电子书”执行“删除/重命名”操作 */
public class ListenerRemoveRenameRecentRead implements View.OnLongClickListener {
    private Context context;
    private FileManager fileManager;

    public ListenerRemoveRenameRecentRead(Context context, FileManager fileManager){
        this.context = context;
        this.fileManager = fileManager;
    }

    @Override
    public boolean onLongClick(View v) {
        // 获取弹窗的UI视图
        View alertRenameRemoveView = View.inflate(this.context, R.layout.alert_renameremove, null);
        // 初始化弹窗(用于确定电子书名)
        final AlertDialog.Builder recentReadShellManager = new AlertDialog.Builder(this.context);
        // 获取控件：弹窗的两个按键：重命名/删除
        Button ReNameButton = alertRenameRemoveView.findViewById(R.id.ReNameButton);
        // 获取控件：弹窗的文本输入框(用于输入新的电子书名)
        final EditText editText = alertRenameRemoveView.findViewById(R.id.editText);
        editText.setHint((String) context.getResources().getString(R.string.renameRecentReadShellTextHint));
        // 弹窗配置与创建
        recentReadShellManager
                .setTitle((String) context.getResources().getString(R.string.renameRemove_alert_title))
                .setView(alertRenameRemoveView)
                .setIcon(R.mipmap.my_icon_layer)
                .create();
        // 弹窗显示
        final AlertDialog alertRenameRemoveShow = recentReadShellManager.show();
        // 设置弹窗重命名按键的点击监听器
        ReNameButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View onClickView) {
                // 获取文本框输入的内容
                String newShellName = editText.getText().toString();
                // 指定文件名及路径
                File file = new File(fileManager.getRecentReadRecordPath());
                if(newShellName.length() == 0){
                    Toast.makeText(context, (String) context.getResources().getString(R.string.renameShellTextHint), Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        FileWriter fileWriter = new FileWriter(file, false);
                        fileWriter.write(newShellName);
                        fileWriter.flush();
                        fileWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                // 关闭弹窗
                alertRenameRemoveShow.dismiss();
            }
        });
        return true;
    }
}