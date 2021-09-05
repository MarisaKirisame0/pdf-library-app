package com.example.ebookapp.Service.Manager;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.ebookapp.R;
import com.example.ebookapp.widget.Book;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author qsj
 * time 2021.08.24
 * 文件管理服务：
 * 1.library文件夹管理
 * 2.获取书架列表()
 * 3.获取电子书列表()
 * 4.检查文件/文件夹是否存在、文件/文件夹创建
 **/
public class FileManager {

    private String library;
    private String recentReadRecordPath;
    private String defaultRecentReadShellName;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public FileManager(Context context){
        library = Objects.requireNonNull(context.getExternalFilesDir(null)).getAbsolutePath() + "/library";
        recentReadRecordPath = Objects.requireNonNull(context.getExternalFilesDir(null)).getAbsolutePath() + "/recentReadRecord.txt";
        defaultRecentReadShellName = (String) context.getResources().getString(R.string.defaultRecentReadShellName);
    }

    /* 查询文件夹或者文件是否存在 */
    public boolean check_file(String file_path){
        File file = new File(file_path);
        return file.exists();
    }

    /* 创建文件 */
    public boolean create_file(String file_path){
        File file = new File(file_path);
        return file.mkdir();
    }

    /* 创建文件夹 */
    public boolean create_dir(String dir_path){
        File file = new File(dir_path);
        return file.mkdirs();
    }

    /* APP初始化时的文件管理器初始化:需要确保library文件夹 */
    public boolean libraryInit(){
        if(this.check_file(this.library)) {
            return true;
        }
        else{
            return this.create_dir(this.library);
        }
    }

    /* APP初始化时的记录文件的初始化:需要确保记录最近阅读的文本文件存在 */
    public boolean recentReadRecordFileInit() throws IOException {
        File file = new File(this.recentReadRecordPath);
        if(file.exists()){
            return true;
        }else {
            if(file.createNewFile()){
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(defaultRecentReadShellName);
                fileWriter.flush();
                fileWriter.close();
                return  true;
            }else {
                return false;
            }
        }
    }

    /* 读取recentReadRecord.txt获取最近阅读 */
    public List<String> readRecentReadRecord() throws IOException {
        // 获取RecentRead记录文件
        File file = new File(this.getRecentReadRecordPath());
        // 使用文件流进行文件的读取
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        // 按行读取，每一行的内容都存入到List中
        String readText;
        List<String> recentReadInfo = new ArrayList<>();
        while((readText = bufferedReader.readLine()) != null){
            recentReadInfo.add(readText);
        }
        // 关闭文件流
        bufferedReader.close();
        inputStreamReader.close();
        fileInputStream.close();
        // 返回记录文件中每一行的内容的List
        return recentReadInfo;
    }

    /* 读取library中的文件夹 */
    public List<String> shells(){
        List<String> list = new ArrayList<>();
        File file = new File(this.library);
        if(file.exists()){
            File[] files = file.listFiles();
            if(files==null)return list;// 判断目录下是不是空的
            for (File f : files) {
                if(f.isDirectory()){// 判断是否文件夹
                    list.add(f.getPath());
                }
            }
        }
        return list;
    }

    /* 读取相应的书架文件夹中的PDF电子书文件 */
    public List<Book> books(String shell){
        List<Book> list = new ArrayList<>();
        File file = new File(shell);
        if(file.exists()){
            File[] files = file.listFiles();
            if(files==null)return list;// 判断目录下是不是空的
            for (File f : files) {
                if(f.isFile()){
                    String book_location = f.getPath();
                    Book book = new Book(book_location.substring(book_location.lastIndexOf("/") + 1) ,book_location);
                    list.add(book);
                }
            }
        }
        return list;
    }

    /* 删除文件或者文件夹(使用递归的方法删除文件夹：清空文件夹中文件后删除空文件夹) */
    public boolean deleteFileAndFolder(File dirFile){
        // 判断相应文件或者文件夹是否存在，不存在则退出
        if (!dirFile.exists()) {
            return false;
        }
        // 判断是文件夹还是文件
        if (dirFile.isFile()) {
            return dirFile.delete();
        } else {
            for (File file : dirFile.listFiles()) {
                deleteFileAndFolder(file);
            }
        }
        return dirFile.delete();
    }

    public String getLibrary() { return library; }

    public String getRecentReadRecordPath(){
        return this.recentReadRecordPath;
    }

    public String getDefaultRecentReadShellName() { return this.defaultRecentReadShellName; }
}
