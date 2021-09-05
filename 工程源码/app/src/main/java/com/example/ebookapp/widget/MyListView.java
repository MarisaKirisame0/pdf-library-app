package com.example.ebookapp.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
/**
 * 自定义ListView控件（主要是解决ScrollView中只显示一项的问题）
 * 作者：qsj
 * 日期：2021.08.29
 */
public class MyListView extends ListView {
    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredHeight = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);//只写了这一句就搞定了
        super.onMeasure(widthMeasureSpec, measuredHeight);//这里需要将第二个参数改为我们测量好的measureHeight
    }
}
