package com.example.ebookapp.Service.Manager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextPaint;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.ebookapp.R;
import com.example.ebookapp.Service.Adapter.ListViewAdapter;
import com.example.ebookapp.Service.Listener.ListenerAddBook;
import com.example.ebookapp.Service.Listener.ListenerCatalogueItemOnClick;
import com.example.ebookapp.Service.Listener.ListenerCatalogueSubset;
import com.example.ebookapp.Service.Listener.ListenerDisplayBook;
import com.example.ebookapp.Service.Listener.ListenerDisplayBookRecentRead;
import com.example.ebookapp.Service.Listener.ListenerRemoveRenameRecentRead;
import com.example.ebookapp.Service.Listener.ListenerRenameRemoveShell;
import com.example.ebookapp.widget.Book;
import com.example.ebookapp.widget.MyListView;
import com.example.ebookapp.widget.TreeNodeData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 视图控制管理类
 * 作者：qsj
 * 日期：2021.08.26
 */
public class ViewManager {
    private Context context;

    public ViewManager(Context context){
        this.context = context;
    }

    /* 动态添加控件，书架显示 */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("UseCompatLoadingForDrawables")
    public void DisplayShells(List<String> shells, LinearLayout linear, FileManager fileManager){
        /* 添加书架：自己定义创建的书架 */
        for (int i=0; i < shells.size(); i++){
            // 动态添加的控件的ID（i=0 对应的code已经给了最近阅读书架）
            int code = (i+1)*6;

            // 获取书架数据：书架名、书架文件夹位置
            String shellName = shells.get(i).substring(fileManager.getLibrary().length() + 1);
            String shellPath = shells.get(i);

            /* 添加书架 */
            this.DisplayAddShell(fileManager, linear, code, shellName, shellPath);
        }
    }

    /* 每个书架的单独动态添加 */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("UseCompatLoadingForDrawables")
    public void DisplayAddShell(FileManager fileManager, LinearLayout linear, int code, String shellName, String ShellPath){
        // 初始化每个书架的“约束布局”(Attention：All children of ConstraintLayout must have ids to use ConstraintSet)
        ConstraintLayout constraintLayout = new ConstraintLayout(linear.getContext());
        constraintLayout.setId(code+4);
        linear.addView(constraintLayout);

        // 初始化每个书架的ListView,使用重写的ListView Class(在ScrollView中不会只列出一项)
        MyListView myListView = new MyListView(linear.getContext());
        myListView.setId(code+6);
        myListView.setAdapter(new ArrayAdapter<>(linear.getContext(), android.R.layout.simple_list_item_1, new String[]{}));
        linear.addView(myListView);

        // 初始化每个书架的TextView,用于显示书架名
        TextView textView = new TextView(this.context);
        textView.setId(code+1);
        textView.setText(shellName);
        textView.setWidth(this.context.getResources().getDisplayMetrics().widthPixels);
        textView.setHeight(200);
        textView.setBackgroundColor(0); // 设置书架背景颜色
        textView.setBackground(this.context.getDrawable(R.drawable.border)); // 设置书架背景:边框白底
        textView.setGravity(Gravity.CENTER_VERTICAL); // 字体总体位置的对称结构:靠左居中
        textView.setPadding(150,0,0,0); // 字体位置的偏移调整
        textView.setTextSize(16); // 字体大小设置
        TextPaint tp = textView.getPaint(); // 设置字体加粗
        tp.setFakeBoldText(true);

        // 设置每一项的长按监听器(长按书架进行重命名/删除)
        textView.setOnLongClickListener(new ListenerRenameRemoveShell(this.context, linear, fileManager, shellName, fileManager.getLibrary()));

        // 在约束布局中添加TextView
        constraintLayout.addView(textView);

        // 初始化每个书架的“展示电子书按键”(ImageButton)
        ImageButton displayBookButton = new ImageButton(this.context);
        displayBookButton.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ali_display));
        displayBookButton.setBackgroundColor(0);
        displayBookButton.setOnClickListener(new ListenerDisplayBook(myListView, linear, fileManager, ShellPath));
        ConstraintLayout.LayoutParams layoutParams1 = new ConstraintLayout.LayoutParams(constraintLayout.getLayoutParams());
        layoutParams1.width = (int)((this.context.getResources().getDisplayMetrics().widthPixels)*0.1);
        layoutParams1.height = 100;
        displayBookButton.setLayoutParams(layoutParams1);
        displayBookButton.setId(code+2);

        // 在约束布局中添加“展示电子书按键”
        constraintLayout.addView(displayBookButton);

        // 初始化每个书架的“添加电子书按键”(ImageButton)
        ImageButton addBookButton = new ImageButton(this.context);
        addBookButton.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ali_add));
        addBookButton.setBackgroundColor(0);
        addBookButton.setOnClickListener(new ListenerAddBook(this.context, ShellPath));
        ConstraintLayout.LayoutParams layoutParams2 = new ConstraintLayout.LayoutParams(constraintLayout.getLayoutParams());
        layoutParams2.width = (int)((this.context.getResources().getDisplayMetrics().widthPixels)*0.1);
        layoutParams2.height = 100;
        addBookButton.setLayoutParams(layoutParams2);
        addBookButton.setId(code+3);

        // 在约束布局中添加“添加电子书按键”
        constraintLayout.addView(addBookButton);

        // 配置约束布局，设置各个动态添加的控件的约束关系
        ConstraintSet constraintSet = new ConstraintSet();
        ConstraintLayout mConstraintLayout;
        mConstraintLayout = constraintLayout;
        constraintSet.clone(mConstraintLayout);
        constraintSet.connect(code+2,ConstraintSet.TOP,code+1,ConstraintSet.TOP,16);
        constraintSet.connect(code+2,ConstraintSet.BOTTOM,code+1,ConstraintSet.BOTTOM,16);
        constraintSet.connect(code+2,ConstraintSet.END,code+1,ConstraintSet.END,26);
        constraintSet.connect(code+3,ConstraintSet.TOP,code+1,ConstraintSet.TOP,16);
        constraintSet.connect(code+3,ConstraintSet.BOTTOM,code+1,ConstraintSet.BOTTOM,16);
        constraintSet.connect(code+3,ConstraintSet.END,code+2,ConstraintSet.START,55);

        // 应用配置好的约束布局
        constraintSet.applyTo(mConstraintLayout);
    }

    /* 动态添加控件：显示最近阅读的书的书架 */
    @SuppressLint({"ResourceType", "UseCompatLoadingForDrawables"})
    public void DisplayRecentReadShell(LinearLayout linear, FileManager fileManager) throws IOException {
        List<String> recentRecordInfo = fileManager.readRecentReadRecord();
        // 初始化每个书架的“约束布局”(Attention：All children of ConstraintLayout must have ids to use ConstraintSet)
        ConstraintLayout constraintLayout = new ConstraintLayout(linear.getContext());
        constraintLayout.setId(0);
        linear.addView(constraintLayout);
        // 初始化每个书架的ListView,使用重写的ListView Class(在ScrollView中不会只列出一项)
        MyListView myListView = new MyListView(linear.getContext());
        myListView.setId(1);
        myListView.setAdapter(new ArrayAdapter<>(linear.getContext(), android.R.layout.simple_list_item_1, new String[]{}));
        linear.addView(myListView);
        // 初始化每个书架的TextView,用于显示书架名
        TextView textView = new TextView(this.context);
        textView.setId(2);
        // 设置"最近阅读"书架书架名字(从recentReadRecord.txt中进行读取)
        textView.setText(recentRecordInfo.get(0));
        textView.setWidth(this.context.getResources().getDisplayMetrics().widthPixels);
        textView.setHeight(200);
        textView.setBackgroundColor(0);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setPadding(150,0,0,0);
        textView.setTextSize(16); // 字体大小设置
        TextPaint tp = textView.getPaint(); // 设置字体加粗
        tp.setFakeBoldText(true);
        // 设置每一项的长按监听器(长按书架进行重命名)
        textView.setOnLongClickListener(new ListenerRemoveRenameRecentRead(this.context, fileManager));
        // 在约束布局中添加TextView
        constraintLayout.addView(textView);
        // 初始化每个书架的“展示电子书按键”(ImageButton)
        ImageButton displayBookButton = new ImageButton(this.context);
        displayBookButton.setImageDrawable(this.context.getResources().getDrawable(R.drawable.ali_display));
        displayBookButton.setBackgroundColor(0);
        // 对应的进行设置
        if(fileManager.readRecentReadRecord().size() > 1){
            displayBookButton.setOnClickListener(new ListenerDisplayBookRecentRead(myListView, linear, fileManager));
        }
        ConstraintLayout.LayoutParams layoutParams1 = new ConstraintLayout.LayoutParams(constraintLayout.getLayoutParams());
        layoutParams1.width = (int)((this.context.getResources().getDisplayMetrics().widthPixels)*0.1);
        layoutParams1.height = 100;
        displayBookButton.setLayoutParams(layoutParams1);
        displayBookButton.setId(3);
        // 在约束布局中添加“展示电子书按键”
        constraintLayout.addView(displayBookButton);
        // 配置约束布局，设置各个动态添加的控件的约束关系
        ConstraintSet constraintSet = new ConstraintSet();
        ConstraintLayout mConstraintLayout;
        mConstraintLayout = constraintLayout;
        constraintSet.clone(mConstraintLayout);
        constraintSet.connect(3,ConstraintSet.TOP,2,ConstraintSet.TOP,16);
        constraintSet.connect(3,ConstraintSet.BOTTOM,2,ConstraintSet.BOTTOM,16);
        constraintSet.connect(3,ConstraintSet.END,2,ConstraintSet.END,26);
        // 应用配置好的约束布局
        constraintSet.applyTo(mConstraintLayout);
    }

    public void DisplayBooks(){

    }

    /* 动态添加控件，目录显示 */
    @SuppressLint({"UseCompatLoadingForDrawables", "ResourceAsColor"})
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void DisplayCatalogue(Activity activity, LinearLayout linear, List<TreeNodeData> catalogue){
        for (int i=0; i<catalogue.size(); i++){

            // 获取目录列表中每一项
            TreeNodeData treeNodeData = catalogue.get(i);

            // 动态控件的ID
            int code = i*6;

            // 初始化每个目录项的约束布局(All children of ConstraintLayout must have ids to use ConstraintSet)
            ConstraintLayout constraintLayout = new ConstraintLayout(linear.getContext());
            constraintLayout.setId(code+1);
            linear.addView(constraintLayout);

            // 初始化每个目录项的ListView(用于显示子目录)
            MyListView myListView = new MyListView(linear.getContext());
            myListView.setId(code+2);
            myListView.setAdapter(new ArrayAdapter<String>(linear.getContext(),android.R.layout.simple_list_item_1,new String[] {}));
            linear.addView(myListView);

            // 初始化每个目录项的TextView(用于显示目录项具体目录内容)
            TextView textView = new TextView(this.context);
            textView.setText(treeNodeData.getName());
            // 设置目录项的TextView具备可点击的特性
            textView.setClickable(true);
            textView.setHeight(200);
            textView.setPadding(250,0,0,0);
            // 设置字体靠左居中
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setId(code+3);

            // 目录项的TextView设置点击监听函数(点击获取页码返回到PDF阅读页面进行页面跳转)
            textView.setOnClickListener(new ListenerCatalogueItemOnClick(activity, treeNodeData.getPageNum()));

            // 约束布局中添加该TextView控件
            constraintLayout.addView(textView);

            // 设置用于是否显示子目录的按键
            ImageButton displaySubsetImgButton = new ImageButton(this.context);
            displaySubsetImgButton.setBackgroundColor(0);
            displaySubsetImgButton.setScaleType(ImageButton.ScaleType.FIT_CENTER);
            displaySubsetImgButton.setImageDrawable(this.context.getDrawable(R.drawable.ali_down));

            // 子目录显示按键监听函数：
            displaySubsetImgButton.setOnClickListener(new ListenerCatalogueSubset(activity,this.context,treeNodeData,myListView,displaySubsetImgButton));

            // 在约束布局中添加按键控件
            displaySubsetImgButton.setId(code+4);
            constraintLayout.addView(displaySubsetImgButton);

            // 配置约束关系
            ConstraintSet constraintSet = new ConstraintSet();
            ConstraintLayout mConstraintLayout;
            mConstraintLayout = constraintLayout;
            constraintSet.clone(mConstraintLayout);
            constraintSet.connect(code+4,ConstraintSet.TOP,code+3,ConstraintSet.TOP);
            constraintSet.connect(code+4,ConstraintSet.BOTTOM,code+3,ConstraintSet.BOTTOM);
            constraintSet.connect(code+4,ConstraintSet.START,code+3,ConstraintSet.START,15);

            // 应用约束关系
            constraintSet.applyTo(mConstraintLayout);
        }
    }

    /* 动态添加控件：显示搜索结果列表
     *  searchResult:记录搜索结果的列表,每一项是相应电子书所在的位置绝对路径
     *  listView:用于显示各个电子书的ListView控件
     * */
    public void DisplaySearchResult(List<String> searchResult, ListView listView){
        List<Book> DisplayResultList = new ArrayList<>();
        for (int i=0; i<searchResult.size();i++) {
            Book book_class = new Book(searchResult.get(i).substring(searchResult.get(i).lastIndexOf("/") + 1), searchResult.get(i));
            DisplayResultList.add(book_class);
            ListViewAdapter listViewAdapter = new ListViewAdapter(context,R.layout.listview_book,DisplayResultList);
            listView.setAdapter(listViewAdapter);
        }
    }
}
