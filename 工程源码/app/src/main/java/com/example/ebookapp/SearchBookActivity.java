package com.example.ebookapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ebookapp.Service.Listener.ListenerCatalogueGoBack;
import com.example.ebookapp.Service.Listener.ListenerSearchItemOnClick;
import com.example.ebookapp.Service.Manager.PermissionManager;
import com.example.ebookapp.Service.Manager.ViewManager;
import com.example.ebookapp.Service.Service.UriFileService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchBookActivity extends AppCompatActivity {
    private List<String> search_res = new ArrayList<>();
    private UriFileService uriFileService;
    private String shellPath;
    private TextView explainTextView;
    private ListView listView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchbook);

        /* 取得启动该Activity的Intent对象，并获取书架文件夹路径 */
        Intent intent =getIntent();
        String shell_path = intent.getStringExtra("ShellName");
        this.shellPath = shell_path;

        /* 使用Intent访问本地文件内容接口的服务初始化 */
        uriFileService = new UriFileService(this,this ,getContentResolver());

        /* 页面控件初始化 */
        ImageButton back = this.findViewById(R.id.searchBack);
        ImageButton all = this.findViewById(R.id.searchAll);
        ImageButton doc = this.findViewById(R.id.searchDocument);
        this.explainTextView = this.findViewById(R.id.explainText);
        this.listView = findViewById(R.id.listView);

        /* 设置搜索结果选项点击监听函数 */
        // 注意此处传入context必须使用this，内部对话框需要确认具体挂载到哪个activity上。
        listView.setOnItemClickListener(new ListenerSearchItemOnClick(this, uriFileService, shell_path));

        /* 权限管理器初始化 */
        PermissionManager permissionManager = new PermissionManager(this, this);

        /* 动态获取权限 */
        permissionManager.RequestPermission();

        /* 配置按键监听器 */
        back.setOnClickListener(new ListenerCatalogueGoBack(this)); // 使用原本在目录页面使用的返回按键的点击监听器：结束页面,返回上一个页面(书架页面)
        all.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { uriFileService.startFile(); }});
        doc.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { uriFileService.startDocument(); }});
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        // 获取文件uri的Intent回调(Intent开启的选择页面执行任务后)
        if (requestCode == uriFileService.getREQUEST_CODE_RequestFile() && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                // 获取intent返回的数据uri
                Uri uri = resultData.getData();
                if(uriFileService.copyFile(uri, shellPath)){ // 对uri指定的文件执行复制操作
                    // 显示信息告知添加成功
                    Toast.makeText(this, (String) this.getResources().getString(R.string.AddBookSuccessfully), Toast.LENGTH_SHORT).show();
                }
            }
        }
        // 获取文件树uri的Intent回调(Intent开启的选择页面执行任务后)
        if (requestCode == uriFileService.getREQUEST_CODE_RequestTreeDocument() && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                Uri uri = resultData.getData();
                try {
                    DocumentFile documentFile = DocumentFile.fromTreeUri(this, uri);
                    // 获取文件树uri对应文件夹中所有pdf文件的绝对地址
                    List<String> bookPathList = uriFileService.uriListGetName(uriFileService.listFile(uri));
                    // 视图更新,清空说明文本
                    //TextView explain = this.findViewById(R.id.explainText);
                    explainTextView.setText("");
                    // 更新搜索页面视图:展现搜索到的pdf文件
                    ViewManager viewManager = new ViewManager(this);
                    //ListView listView = findViewById(R.id.listView);
                    viewManager.DisplaySearchResult(bookPathList, listView);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}