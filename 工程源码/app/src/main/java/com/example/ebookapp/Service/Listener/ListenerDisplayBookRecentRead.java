package com.example.ebookapp.Service.Listener;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.ebookapp.PdfViewActivity;
import com.example.ebookapp.R;
import com.example.ebookapp.Service.Adapter.ListViewAdapter;
import com.example.ebookapp.Service.Manager.FileManager;
import com.example.ebookapp.widget.Book;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* 按键点击监听：显示书架的电子书 */
public class ListenerDisplayBookRecentRead implements View.OnClickListener {

    private ListView listview;
    private Context context;
    private Book book;
    private String bookPath;
    private String bookName;
    private int PageNum;
    private boolean flag = true;

    public  ListenerDisplayBookRecentRead(ListView lv, LinearLayout liner, FileManager fileManager) throws IOException {
        this.listview = lv;
        this.context = liner.getContext();
        this.bookPath = fileManager.readRecentReadRecord().get(1);
        this.bookName = this.bookPath.substring(fileManager.readRecentReadRecord().get(1).lastIndexOf("/") + 1);
        this.PageNum = Integer.parseInt(fileManager.readRecentReadRecord().get(2));
    }

    @Override
    public void onClick(View v) {
        List<Book> bookList = new ArrayList<>();
        if(flag){
            this.book = new Book(bookName, bookPath);
            bookList.add(book);
        }
        this.listview.setAdapter(new ListViewAdapter(this.context, R.layout.listview_book,bookList));
        flag = !flag;
        // 设置每个电子书的点击监听器(获取该电子书的文件位置,传参到PdfViewActivity页面)
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(context, PdfViewActivity.class);
                intent.putExtra("BookLocation", book.getLocation());
                intent.putExtra("PageNumber", PageNum);
                context.startActivity(intent);
            }
        });
    }
}
