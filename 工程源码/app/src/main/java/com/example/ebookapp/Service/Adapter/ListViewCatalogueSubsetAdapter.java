package com.example.ebookapp.Service.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.ebookapp.R;
import com.example.ebookapp.widget.TreeNodeData;

import java.util.List;

/* ListView的Adapter(主要用于目录的子目录每一项) */
public class ListViewCatalogueSubsetAdapter extends ArrayAdapter<TreeNodeData>  {
    private int resourceId;

    public ListViewCatalogueSubsetAdapter(Context context, int textViewResourceId, List<TreeNodeData> objects){
        super(context,textViewResourceId,objects);
        this.resourceId=textViewResourceId;
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent){
        TreeNodeData treeNodeData = getItem(position);
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
            viewHolder= (ViewHolder) view.getTag();
        }
        // 获取控件实例，并调用set方法使其显示出来
        if(treeNodeData != null){
            viewHolder.bookName.setText(treeNodeData.getName());
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
