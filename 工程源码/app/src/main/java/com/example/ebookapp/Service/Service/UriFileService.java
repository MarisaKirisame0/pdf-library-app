package com.example.ebookapp.Service.Service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
/*import android.os.FileUtils;*/
import android.provider.DocumentsContract;
import android.provider.OpenableColumns;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.documentfile.provider.DocumentFile;

import com.example.ebookapp.R;
import com.github.barteksc.pdfviewer.util.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qsj
 * 日期：2021.09.03
 * 提供使用Intent获取URI进行文件访问的服务
 */
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class UriFileService {
    private Activity activity;
    private Context context;
    private ContentResolver contentResolver;
    private int REQUEST_CODE_RequestTreeDocument;
    private int REQUEST_CODE_RequestFile;
    private List<Uri> result;

    public UriFileService(Activity activity, Context context, ContentResolver contentResolver){
        this.activity = activity;
        this.context = context;
        this.contentResolver = contentResolver;
        this.REQUEST_CODE_RequestTreeDocument = 1;
        this.REQUEST_CODE_RequestFile = 2;
        this.result = new ArrayList<>();
    }

    /* 开启获取文件夹Uri */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void startDocument(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, 0);
        this.activity.startActivityForResult(intent,REQUEST_CODE_RequestTreeDocument);
    }

    /* 开启获取文件URI */
    public void startFile(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, 0);
        this.activity.startActivityForResult(intent, REQUEST_CODE_RequestFile);
    }

    /* 根据输入的文件Uri执行文件复制操作 */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public boolean copyFile(Uri uri, String Path){
        String displayName = "";
        // 根据所得uri进行query操作，得到所选文件元数据
        @SuppressLint("Recycle") Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            // 读取文件名
            displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
        }
        if(displayName.endsWith("pdf")){ // 判断文件是否是pdf文件
            try {
                // 建立文件输入流
                InputStream inputStream = contentResolver.openInputStream(uri);
                // 确定目标文件的绝对路径
                File cache = new File(Path, displayName);
                // 建立文件输出流
                FileOutputStream outputStream = new FileOutputStream(cache);
                // 调用文件工具类执行复制操作
                assert inputStream != null;
                //FileUtils.copy(inputStream, outputStream);（android内部的FileUtil方法只对Android10以上适用）
                FileUtils.copy(inputStream, cache);// 使用pdfView包的FileUtil的copy方法，对Android9以及Android10以上都适用
                // 关闭文件流
                outputStream.close();
                inputStream.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        else {
            // 显示错误信息
            Toast.makeText(context, (String) context.getResources().getString(R.string.AddBookError), Toast.LENGTH_LONG).show();
            return false;
        }
    }

    /* 获取文件树uri对应文件夹下所有的pdf文件的uri */
    public List<Uri> listFile(Uri uri) throws IOException {
        // 记录此uri
        // recordDocumentUri(uri);
        // 根据输入的文件树uri,建立文件解析器
        DocumentFile documentFile = DocumentFile.fromTreeUri(context, uri);
        // 执行迭代搜索pdf文件，获取List
        assert documentFile != null;
        // 创建用于存储pdf文件的List容器
        List<Uri> FileList = new ArrayList<>();
        // 记录获取到的文件Uri列表(方便在搜索结果项目点击监听器中进行调用)
        this.result = FileList;
        // 返回装载该文件夹下的pdf文件的uri列表
        return this.list(documentFile, FileList);
    }

    /*  */
    public List<String> uriListGetName(List<Uri> list) {
        List<String> bookPathList = new ArrayList<>();
        for(int i=0;i<list.size();i++) {
            bookPathList.add(list.get(i).getPath());
        }
        return bookPathList;
    }

    /* 对文件解析器所指向的文件夹下文件进行迭代，搜寻此文件夹下所有pdf文件 */
    public List<Uri> list(DocumentFile dFile, List<Uri> FileList) {
        if(dFile.isDirectory())//判断file是否是目录
        {
            DocumentFile [] dFiles = dFile.listFiles();
            for (DocumentFile file : dFiles) {
                list(file, FileList);
            }
        }else {
            String fileName = dFile.getName();
            assert fileName != null;
            if(fileName.endsWith("pdf")){
                FileList.add(dFile.getUri());
            }
        }
        return FileList;
    }

    public int getREQUEST_CODE_RequestTreeDocument(){ return this.REQUEST_CODE_RequestTreeDocument; }

    public int getREQUEST_CODE_RequestFile(){ return this.REQUEST_CODE_RequestFile;}

    public List<Uri> getResult() { return this.result; }

}
