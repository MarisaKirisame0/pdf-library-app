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

import java.util.ArrayList;
import java.util.List;

/* 按键点击监听：显示书架的电子书 */
public class ListenerDisplayBook implements View.OnClickListener {

    private ListView listview;
    private Context context;
    private FileManager fileManager;
    private String shell;
    private List<Book> books;
    private boolean flag = true;

    public  ListenerDisplayBook(ListView lv, LinearLayout liner, FileManager fileManager, String shell){
        this.listview = lv;
        this.context = liner.getContext();
        this.fileManager = fileManager;
        this.shell = shell;
    }

    @Override
    public void onClick(View v) {
        List<Book> bookList = new ArrayList<>();
        if(flag){
            //每次点击都会获取一次相应书架下的电子书
            this.books =  this.fileManager.books(this.shell);
            bookList = this.books;
        }
        this.listview.setAdapter(new ListViewAdapter(this.context, R.layout.listview_book,bookList));
        flag = !flag;

        // 设置每个电子书的点击监听器(获取该电子书的文件位置,传参到PdfViewActivity页面)
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = books.get(position);
                Intent intent = new Intent();
                intent.setClass(context, PdfViewActivity.class);
                intent.putExtra("BookLocation", book.getLocation());
                intent.putExtra("PageNumber", 0);
                context.startActivity(intent);
            }
        });
        // 设置每个电子书长按监听器(执行重命名或者删除操作)
        listview.setOnItemLongClickListener(new ListenerRemoveRenameBook(context, fileManager, listview, shell));
    }
}
