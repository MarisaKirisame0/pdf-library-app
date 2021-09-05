package com.example.ebookapp.Service.Listener;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.example.ebookapp.R;
import com.example.ebookapp.Service.Manager.FileManager;
import com.example.ebookapp.Service.Manager.ViewManager;

import java.util.List;

/* 按键点击监听：创建新书架 */
public class ListenerCreateShell implements View.OnClickListener {

    private Context context;
    private FileManager fileManager;
    private LinearLayout linear;
    private int code;

    public  ListenerCreateShell(Context context, List<String> shells, LinearLayout linear, FileManager fileManager, int code){
        this.context = context;
        this.fileManager = fileManager;
        this.linear = linear;
        this.code = code;
    }

    @Override
    public void onClick(View v) {
        View view = View.inflate(this.context, R.layout.alert_makeshell, null);

        // 初始化弹窗(用于确定书架名)
        final AlertDialog.Builder createShell = new AlertDialog.Builder(this.context);

        // 获取控件：弹窗的确认按键
        Button SureToCreateShell = view.findViewById(R.id.button);
        // 获取控件：弹窗的文本输入框
        final EditText editText = view.findViewById(R.id.editTextTextPersonName);

        // 弹窗配置与创建
        createShell
                .setTitle("Create Shell")
                .setView(view)
                .setIcon(R.mipmap.my_icon_layer)
                .create();

        // 弹窗显示
        final AlertDialog show = createShell.show();

        // 设置弹窗按键的点击监听器
        SureToCreateShell.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {

                // 获取文本框输入的内容
                String shellName = editText.getText().toString();

                // 确定建立新的书架文件夹的文件路径
                String shellPath = fileManager.getLibrary() + "/" + shellName;

                // 检查是否与已有书架文件夹名字相同(若名字相同则在名字后加"(N)")
                while (fileManager.check_file(shellPath)){
                    shellPath = shellPath + "(N)";
                }

                // 创建新的书架文件夹
                if(!fileManager.create_dir(shellPath)){

                    Toast.makeText(context, (String) context.getResources().getString(R.string.recentReadRecordFileInitError), Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(context, "Created successfully!", Toast.LENGTH_SHORT).show();
                }

                // 关闭弹窗
                show.dismiss();

                // 再次动态设置显示书架列表
                ViewManager viewManager = new ViewManager(context);
                viewManager.DisplayAddShell(fileManager, linear, code, shellName, shellPath);
            }
        });
    }
}