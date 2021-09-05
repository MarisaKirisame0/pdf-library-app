package com.example.ebookapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ebookapp.Service.Listener.ListenerOpenCatalogue;
import com.example.ebookapp.Service.Manager.FileManager;
import com.example.ebookapp.widget.TreeNodeData;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.shockwave.pdfium.PdfDocument;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PdfViewActivity extends AppCompatActivity implements
        OnPageChangeListener,
        OnLoadCompleteListener,
        OnPageErrorListener {

    //控件
    private PDFView pdfView;
    private TextView page;
    private TextView pageTotal;
    // 电子书所在路径
    private String BookLocation;
    // 当前页码
    private int nowPage;
    //PDF目录集合
    List<TreeNodeData> catalogue;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfview);

        /* 隐藏最原始的上边的标题栏 */
        Objects.requireNonNull(getSupportActionBar()).hide();

        /* 取得启动该Activity的Intent对象，并获取书架文件夹路径 */
        Intent intent =getIntent();
        this.BookLocation = intent.getStringExtra("BookLocation");
        /* 获取应该打开的页面的页码 */
        int defaultPageNumber = intent.getIntExtra("PageNumber", 0);

        // 获取页面控件
        this.pdfView = findViewById(R.id.pdfView);
        this.page = findViewById(R.id.page);
        this.pageTotal = findViewById(R.id.pageTotal);

        // 实例化电子书文件
        assert BookLocation != null;
        File file = new File(BookLocation);
        // pdfView配置开启
        this.pdfView.fromFile(file)
                .enableSwipe(true)//支持滑动翻页
                .swipeHorizontal(false)//设置false:垂直翻页;设置true:水平翻页;
                .enableDoubletap(false)//连续点击同一点两次执行放大
                .defaultPage(defaultPageNumber)//默认打开页的页码
                .onLoad(this)
                .onPageChange(this)
                .enableAnnotationRendering(true) // render annotations (such as comments, colors or forms)
                .enableAnnotationRendering(false)// 渲染风格设置
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true) // 改善低分辨率屏幕上的渲染
                // 页面间的间距。定义间距颜色，设置背景视图
                .spacing(0)
                .load();
    }
    /**
     * 当成功加载PDF：
     * 1、可获取PDF的目录信息
     *
     * @param nbPages the number of pages in this PDF file
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void loadComplete(int nbPages) {
        //获得文档书签信息
        List<PdfDocument.Bookmark> bookmarks = pdfView.getTableOfContents();
        if (catalogue != null) {
            catalogue.clear();
        } else {
            catalogue = new ArrayList<>();
        }
        //将bookmark转为目录数据集合
        bookmarkToCatelogues(catalogue, bookmarks, 1);

        //加载完成后设置各个按键的点击监听
        Button goToLastPage = findViewById(R.id.GoToLastPage);
        Button goToNextPage = findViewById(R.id.GoToNextPage);
        goToLastPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdfView.jumpTo(nowPage - 1);
            }
        });
        goToNextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdfView.jumpTo(nowPage + 1);
            }
        });
        //设置目录按键监听函数
        Button openCatalogue = findViewById(R.id.OpenCatalogue);
        openCatalogue.setOnClickListener(new ListenerOpenCatalogue(this, this, catalogue));
        // 设置页码显示
        page.setText(nbPages + "");
        pageTotal.setText(0 +  "/");
    }

    private void bookmarkToCatelogues(List<TreeNodeData> catelogues, List<PdfDocument.Bookmark> bookmarks, int level) {
        for (PdfDocument.Bookmark bookmark : bookmarks) {
            TreeNodeData nodeData = new TreeNodeData();
            nodeData.setName(bookmark.getTitle());
            nodeData.setPageNum((int) bookmark.getPageIdx());
            nodeData.setTreeLevel(level);
            nodeData.setExpanded(false);
            catelogues.add(nodeData);
            if (bookmark.getChildren() != null && bookmark.getChildren().size() > 0) {
                List<TreeNodeData> treeNodeDatas = new ArrayList<>();
                nodeData.setSubset(treeNodeDatas);
                bookmarkToCatelogues(treeNodeDatas, bookmark.getChildren(), level + 1);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onPageChanged(int page, int pageCount) {
        pageTotal.setText(page + "/");
        nowPage = page;
    }

    @Override
    public void onPageError(int page, Throwable t) {
    }

    /**
     * 目录页面带回页码，跳转到指定PDF页面
     *
     * @param requestCode 用于在每个startActivityForResult开启的页面跳转传参线程中进行区分
     * @param resultCode 结构状态码，跳转后的页面在finish前进行设置，告知原页面任务已经完成
     * @param data 从跳转的页面完成任务后获得传回的数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            int pageNum = data.getIntExtra("pageNum",0);
            if (pageNum > 0) {
                this.pdfView.jumpTo(pageNum);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //内存保存记录目前的阅读
        if (pdfView != null) {
            FileManager fileManager = new FileManager(this);
            String recordFilePath = fileManager.getRecentReadRecordPath();
            File file = new File(recordFilePath);
            try {
                List<String> record = fileManager.readRecentReadRecord();
                FileWriter fileWriter = new FileWriter(file, false);
                fileWriter.write(record.get(0));
                fileWriter.write("\r\n");
                fileWriter.write(this.BookLocation);
                fileWriter.write("\r\n");
                fileWriter.write(String.valueOf(this.nowPage));
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}