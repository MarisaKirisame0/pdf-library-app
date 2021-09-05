package com.example.ebookapp.Service.Listener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.example.ebookapp.R;
import com.example.ebookapp.Service.Manager.FileManager;
import com.example.ebookapp.Service.Manager.ViewManager;

import java.io.File;

/* 长按监听器：用于对“书架”执行“删除/重命名”操作 */
public class ListenerRenameRemoveShell implements View.OnLongClickListener {

    private Context context;
    private String Name;
    private String rootPath;
    private LinearLayout linearLayout;
    private FileManager fileManager;

    public ListenerRenameRemoveShell(Context context, LinearLayout linearLayout, FileManager fileManager, String Name, String rootPath){
        this.context = context;
        this.Name = Name;
        this.rootPath = rootPath;
        this.linearLayout = linearLayout;
        this.fileManager = fileManager;
    }
    @Override
    public boolean onLongClick(View v) {
        // 获取弹窗的UI视图
        View view = View.inflate(this.context, R.layout.alert_renameremove, null);

        // 初始化弹窗(用于确定书架名)
        final AlertDialog.Builder shellManager = new AlertDialog.Builder(this.context);

        // 获取控件：弹窗的两个按键：重命名/删除
        Button ReNameButton = view.findViewById(R.id.ReNameButton);
        Button DeleteButton = view.findViewById(R.id.DeleteButton);
        // 获取控件：弹窗的文本输入框(用于输入新的书架名)
        final EditText editText = view.findViewById(R.id.editText);

        // 弹窗配置与创建
        shellManager
                .setTitle("Rename or Delete")
                .setView(view)
                .setIcon(R.mipmap.my_icon_layer)
                .create();

        // 弹窗显示
        final AlertDialog show = shellManager.show();

        // 设置弹窗重命名按键的点击监听器
        ReNameButton.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
               // 获取文本框输入的内容
               String newShellName = editText.getText().toString();
               // 判断输入的命名是否为空
               if (newShellName.equals("")){
                   Toast.makeText(context, (String) context.getResources().getString(R.string.renameShellTextHint), Toast.LENGTH_SHORT).show();
               }
               File file=new File(rootPath + "/" + Name); //指定文件名及路径
                // 进行文件重命名
                if (file.renameTo(new File(rootPath + "/" + newShellName))) {
                    Toast.makeText(context, "Rename successfully!", Toast.LENGTH_SHORT).show();
                }
                else{
                    // 如果命名失败则显示错误信息(重命名文件或者文件夹已经存在的状况下会发生此状况)
                    Toast.makeText(context, (String) context.getResources().getString(R.string.RenameError), Toast.LENGTH_LONG).show();
                }
                // 关闭弹窗
                show.dismiss();
                // 再次动态设置显示书架列表(先清空然后再重新加载显示书架列表)
                ViewManager viewManager = new ViewManager(context);
                linearLayout.removeAllViews();
                viewManager.DisplayShells(fileManager.shells(), linearLayout, fileManager);
            }
        });

        // 设置弹窗删除按键的点击监听器
        DeleteButton.setOnClickListener(new View.OnClickListener(){
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                // 先关闭原来的弹窗
                show.dismiss();
                // 初始化新的弹窗(用于确定是否删除)
                final AlertDialog.Builder removeAlert = new AlertDialog.Builder(context);
                // 使用是否选择该电子书的视图作为是否删除的视图
                View removeAlertView = View.inflate(context, R.layout.alert_selectbook, null);
                // 获取控件：弹窗的文本显示框
                final TextView textView = removeAlertView.findViewById(R.id.textView);
                textView.setHeight(100);
                textView.setText("是否进行删除：" + Name);
                // 弹窗配置与创建
                removeAlert
                        .setTitle("Remove Operation")
                        .setView(removeAlertView)
                        .setIcon(R.mipmap.my_icon_layer)
                        .create();
                // 弹窗显示
                final AlertDialog removeAlertShow = removeAlert.show();
                // 获取控件：弹窗的确认按键与取消按键
                Button buttonYes = removeAlertView.findViewById(R.id.button_yes);
                Button buttonNo = removeAlertView.findViewById(R.id.button_no);
                // 设置确认键点击监听器：使用递归进行文件以及文件夹的删除
                buttonYes.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View v) {
                        File removeFile = new File(rootPath + "/" + Name);
                        // 执行文件删除操作
                        if(fileManager.deleteFileAndFolder(removeFile)){
                            // 再次动态设置显示书架列表(先清空然后再重新加载显示书架列表)
                            ViewManager viewManager = new ViewManager(context);
                            linearLayout.removeAllViews();
                            viewManager.DisplayShells(fileManager.shells(), linearLayout, fileManager);
                            // 告知已经删除
                            Toast.makeText(context, "Delete Successfully!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(context, (String) context.getResources().getString(R.string.RemoveError), Toast.LENGTH_LONG).show();
                        }
                        // 关闭弹窗
                        removeAlertShow.dismiss();
                    }
                });
                // 设置取消键的点击监听器：关闭弹窗
                buttonNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeAlertShow.dismiss();
                    }
                });
            }
        });
        return true;
    }
}

