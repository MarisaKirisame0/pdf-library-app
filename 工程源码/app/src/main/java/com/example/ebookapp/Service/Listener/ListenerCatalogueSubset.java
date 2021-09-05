package com.example.ebookapp.Service.Listener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.RequiresApi;

import com.example.ebookapp.R;
import com.example.ebookapp.Service.Adapter.ListViewCatalogueSubsetAdapter;
import com.example.ebookapp.widget.TreeNodeData;

import java.util.ArrayList;
import java.util.List;

/* 按键点击监听：显示目录项的子目录 */
public class ListenerCatalogueSubset implements View.OnClickListener {
    private Activity activity;
    private Context context;
    private TreeNodeData treeNodeData;
    private ListView listView;
    private ImageButton imageButton;
    private boolean flag = true;

    public ListenerCatalogueSubset(Activity activity, Context context, TreeNodeData treeNodeData, ListView listView, ImageButton imageButton) {
        this.activity = activity;
        this.context = context;
        this.treeNodeData = treeNodeData;
        this.listView = listView;
        this.imageButton = imageButton;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        // 获取目录项的子目录
        final List<TreeNodeData> treeNodeDataSubset = this.treeNodeData.getSubset();
        // 空列表
        List<TreeNodeData> NullList = new ArrayList<>();
        if (treeNodeDataSubset != null){
            if(flag){
                // 点击显示，修改图标为上拉图标
                imageButton.setImageDrawable(this.context.getDrawable(R.drawable.ali_up));

                // 在listView中加入元素
                listView.setAdapter(new ListViewCatalogueSubsetAdapter(this.context, R.layout.listview_book,treeNodeDataSubset));

                // 设置子目录目录项的点击监听函数(子目录项页码传回PdfViewActivity页面)
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TreeNodeData treeNodeData_select = treeNodeDataSubset.get(position);
                        Intent intent = new Intent();
                        intent.putExtra("pageNum",treeNodeData_select.getPageNum());
                        activity.setResult(Activity.RESULT_OK, intent);
                        activity.finish();
                    }
                });
            }
            else {
                // 点击关闭子目录，修改图标为下拉
                imageButton.setImageDrawable(this.context.getDrawable(R.drawable.ali_down));
                // 输入空元素，关闭子目录
                listView.setAdapter(new ListViewCatalogueSubsetAdapter(this.context, R.layout.listview_book, NullList));
            }
            flag = !flag;


        }
    }
}