package com.example.ebookapp.Service.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.ebookapp.R;
import com.example.ebookapp.widget.Book;

import java.util.List;

/* ListView的Adapter(主要用于显示：书架电子书、搜索项电子书) */
public class ListViewAdapter extends ArrayAdapter<Book> {

    private int resourceId;
    private List<Book> result;

    public ListViewAdapter(Context context, int textViewResourceId, List<Book> objects) {
        super(context,textViewResourceId,objects);
        this.resourceId=textViewResourceId;
        this.result = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent){
        /* 使用position确定被点击的电子书对象 */
        Book book = getItem(position);
        /* 视图控制初始化 */
        View view;
        ViewHolder viewHolder;

        if (convertView==null){
            // 避免ListView每次滚动时都要重新加载布局，以提高运行效率
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            // 避免每次调用getView()时都要重新获取控件实例
            viewHolder= new ViewHolder();
            viewHolder.bookName=view.findViewById(R.id.bookName);
            // 将ViewHolder存储在View中（即将控件的实例存储在其中）
            view.setTag(viewHolder);
        } else{
            view=convertView;
            viewHolder=(ViewHolder) view.getTag();
        }
        // 获取控件实例，并调用set方法使其显示出来
        if(book != null){
            viewHolder.bookName.setText(book.getTitle());
        }
        else {
            viewHolder.bookName.setText("");
        }
        return view;
    }

    // 定义一个内部类，用于对控件的实例进行缓存
    static class ViewHolder{
        TextView bookName;
    }
}
