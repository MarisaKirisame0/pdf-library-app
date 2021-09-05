package com.example.ebookapp.Service.Listener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.ebookapp.R;
import com.example.ebookapp.Service.Adapter.ListViewAdapter;
import com.example.ebookapp.Service.Manager.FileManager;
import com.example.ebookapp.widget.Book;

import java.io.File;
import java.util.List;

/* 长按监听器：用于对“电子书”执行“删除/重命名”操作 */
public class ListenerRemoveRenameBook implements AdapterView.OnItemLongClickListener {
    private Context context;
    private FileManager fileManager;
    private ListView listView;
    private List<Book> books;
    private String shell;

    public ListenerRemoveRenameBook(Context context, FileManager fileManager, ListView listView, String shell){
        this.context = context;
        this.fileManager = fileManager;
        this.listView = listView;
        this.books = fileManager.books(shell);
        this.shell = shell;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final Book book = books.get(position);

        // 获取弹窗的UI视图
        View alertRenameRemoveView = View.inflate(this.context, R.layout.alert_renameremove, null);

        // 初始化弹窗(用于确定电子书名)
        final AlertDialog.Builder shellManager = new AlertDialog.Builder(this.context);

        // 获取控件：弹窗的两个按键：重命名/删除
        Button ReNameButton = alertRenameRemoveView.findViewById(R.id.ReNameButton);
        Button DeleteButton = alertRenameRemoveView.findViewById(R.id.DeleteButton);
        // 获取控件：弹窗的文本输入框(用于输入新的电子书名)
        final EditText editText = alertRenameRemoveView.findViewById(R.id.editText);
        editText.setHint((String) context.getResources().getString(R.string.renameRemove_alert_textHint));

        // 弹窗配置与创建
        shellManager
                .setTitle((String) context.getResources().getString(R.string.renameRemove_alert_title))
                .setView(alertRenameRemoveView)
                .setIcon(R.mipmap.my_icon_layer)
                .create();

        // 弹窗显示
        final AlertDialog alertRenameRemoveShow = shellManager.show();

        // 设置弹窗重命名按键的点击监听器
        ReNameButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View onClickView) {
                // 获取文本框输入的内容
                StringBuilder newBookName = new StringBuilder(editText.getText().toString());
                // 指定文件名及路径
                File file = new File(shell + "/" + book.getTitle());
                // 判断文件是否存在（若文件名对应文件以已经存在,则其命名加上'(N)'）
                while(fileManager.check_file(shell + "/" + newBookName + ".pdf")){
                    newBookName.append("(N)");
                }
                // 进行文件重命名
                if (file.renameTo(new File(shell + "/" + newBookName + ".pdf"))) {
                    Toast.makeText(context, "Rename successfully!", Toast.LENGTH_SHORT).show();
                }
                else{
                    // 如果命名失败则显示错误信息(重命名文件或者文件夹已经存在的状况下会发生此状况)
                    Toast.makeText(context, (String) context.getResources().getString(R.string.RenameError), Toast.LENGTH_LONG).show();
                }
                // 关闭弹窗
                alertRenameRemoveShow.dismiss();
                // 再次动态设置显示书架列表(先清空然后再重新加载显示书架列表)
                List<Book> bookList = fileManager.books(shell);
                listView.setAdapter(new ListViewAdapter(context, R.layout.listview_book, bookList));
            }
        });

        // 设置弹窗删除按键的点击监听器
        DeleteButton.setOnClickListener(new View.OnClickListener(){
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                // 先关闭原来的弹窗
                alertRenameRemoveShow.dismiss();
                // 初始化新的弹窗(用于确定是否删除)
                final AlertDialog.Builder removeAlert = new AlertDialog.Builder(context);
                // 使用是否选择该电子书的视图作为是否删除的视图
                View removeAlertView = View.inflate(context, R.layout.alert_selectbook, null);
                // 获取控件：弹窗的文本显示框
                final TextView textView = removeAlertView.findViewById(R.id.textView);
                textView.setHeight(100);
                textView.setText("是否进行删除：" + book.getTitle());
                // 弹窗配置与创建
                removeAlert
                        .setTitle((String) context.getResources().getString(R.string.delete_alert_title))
                        .setView(removeAlertView)
                        .setIcon(R.mipmap.my_icon_layer)
                        .create();
                // 弹窗显示
                final AlertDialog removeAlertShow = removeAlert.show();
                // 获取控件：弹窗的确认按键与取消按键
                Button buttonYes = removeAlertView.findViewById(R.id.button_yes);
                Button buttonNo = removeAlertView.findViewById(R.id.button_no);
                // 设置确认键点击监听器：删除相应文件
                buttonYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        File removeFile = new File(shell + "/" + book.getTitle());
                        if(removeFile.delete()){
                            // 再次动态设置显示书架列表(先清空然后再重新加载显示书架列表)
                            List<Book> bookList = fileManager.books(shell);
                            listView.setAdapter(new ListViewAdapter(context, R.layout.listview_book, bookList));
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

