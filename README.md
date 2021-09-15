# 不动的大图书馆

Author：雾雨霜星

Web：[雾雨霜星 | DA☆ZE (shuangxing.top)](https://www.shuangxing.top/#/)

Time：2021-09-04

## 软件简介

"不动的大图书馆"是一款简单、高效、开源的PDF电子书书架软件。

特点：

* 简单：简洁UI界面；无需用户登录；
* 干净：不带有任何广告，不会插播任何等待打开页面
* 安全：不提供联网接口，不会发送任何用户信息
* 高效：功能清晰，记录每次最后阅读
* 独立：不与任何第三方软件进行交互
* 兼容：适用于任何Android版本的系统
* 轻量：自身体积小，后期所占空间由添加的PDF文件决定
* 开源：源码完全开放，可根据需求自定义

开源地址：[不动的大图书馆：一款简单、高效、开源的PDF电子书架软件](https://gitee.com/marisa-kirisame/a-large-immovable-library)

下载APK：https://gitee.com/marisa-kirisame/a-large-immovable-library/blob/master/app-release.apk

下载APK(github)：https://github.com/MarisaKirisame0/pdf-library-app/blob/master/app-release.apk



## 使用说明

主要有四个页面：主页面(书架与电子书)、搜索页面、阅读页面、目录页面

<img src="http://marisa-kirisame.gitee.io/a-large-immovable-library/页面展示/主页面.jpg" style="zoom:25%;" /><img src="http://marisa-kirisame.gitee.io/a-large-immovable-library/页面展示/搜索页面.jpg" style="zoom:25%;" /><img src="http://marisa-kirisame.gitee.io/a-large-immovable-library/页面展示/阅读页面.jpg" style="zoom:25%;" /><img src="http://marisa-kirisame.gitee.io/a-large-immovable-library/页面展示/目录页面.jpg" style="zoom:25%;" />

### 页面说明

主页面：

* 工具栏左上角的加号按键：创建新书架
* "最近阅读"书架：记录了上次阅读的PDF文件及其页码
* 书架块靠左侧加号按键：对此书架添加PDF文件，进入搜索页面
* 书架块靠右侧菜单按键：下拉展示此书架内的PDF文件
* 长按书架块：对书架进行重命名或者删除操作
* 长按电子书块：对相应书架文件夹下的PDF文件进行重命名或者删除操作
* 点击电子书块：进入阅读页面，打开此PDF文件

搜索页面：

* 返回书架按键：关闭搜索页面，返回主页面
* 从手机中选择文件按键：从手机中选择PDF文件添加到相应的书架
* 从文件夹搜索文件按键：从手机中选择文件夹，搜索此文件夹内的PDF文件，列出搜索结果，点击搜索结果决定是否选择添加

阅读页面：

* 上一页按键：自动翻页到上一页
* 目录按键：进入目录页面
* 下一页按键：自动翻页到下一页

目录页面：

* 左上角的返回按键：关闭目录页面，返回阅读页面
* 目录项左侧的箭头按键：点击下拉展开该目录项的子目录
* 点击目录项块：关闭目录页面并返回阅读页面，翻页到相应的页码

### 内部存储模式

* APP安装后第一次启动，会在自己的目录路径下建立以下两个文件
  * library文件夹：其内为书架文件夹(每个书架对应一个文件夹)，书架文件夹内为该书架所有的PDF文件
  * recentReadRecord.txt：用于记录最近阅读的文本文件(第一行为"最近阅读"书架的书架名；第二行为上次阅读的PDF文件位置；第三行为上次阅读到的页码)
* 关于添加PDF文件：复制操作，而不是移动操作。即原位置的PDF文件不会改变，只是复制多了一份到相应的书架文件夹下。
* 内部存储空间占用：软件自身大小约为20M左右，后期内部存储空间占用为所添加的PDF文件。考虑到某些PDF文件比较大(甚至可能大于25M)，可考虑删除其原本所在位置下的PDF文件。

### 软件卸载

卸载软件时，其中的library文件夹和recentReadRecord.txt也会被一并删除。故已经复制到了书架文件夹中的PDF文件的复制文件也会被删除。



## 已知BUG

1. 视图延迟：

  * 电子书执行重命名或者删除操作后，书架中PDF文件的顺序未变化，仅视图上变化了。要重新点击展开按键，来实现更改。
  * "最近阅读"软件后重开才生效，即不关闭软件，显示上不会改变。但记录文件在阅读页面退出时已更新。

2. 视图缺失：

   对普通书架进行重命名或删除操作后，"最近阅读"书架会在界面消失。重新打开软件即可。

3. 记录延迟：

   "最近阅读"中对应的PDF文件被删除后，记录未发生改变时，打开会直接显示空白。

4. 记录缺失：

   "最近阅读"书架重命名后其内保存的记录会消失。



## 源码浅析

### src/main/java/com.example.ebookapp/

* MainActivity.java：主页面类
  1. 主页面挂载activity_main.xml，文件夹管理器(FileManager)、视图控制管理器(ViewManager)初始化
  2. 视图管理器控制显示书架、"最近阅读"书架
  3. 主页面的ToolBar初始化
* CatalogueActivity.java：目录页面类，主要是控制显示目录列表，设置子目录显示的监听
* PdfViewActivity.java：阅读页面类
  1. 获取上一个页面启动Intent传输过来的PDF文件位置、页码
  2. pdfView的设置：事件监听、渲染风格、文件确定
  3. 完全加载后获取目录树数据
  4. 设置目录页面的回调监听
* SearchBookActivity.java：搜索页面类
  1. 初始化UriFileService
  2. 页面控件初始化，视图控制管理器监听控制页面变化
  3. 配置使用Intent获取URI进行文件访问的回调监听
* Service/Manager/：
  * FileManager：文件管理类
    1. APP初始化检查Library文件夹、检查记录文本文件
    2. 读取最近阅读记录文本文件
    3. 读取书架文件夹
    4. 删除文件夹或者文件
  * ViewManager：视图控制管理类
    1. 书架显示
    2. 显示"最近阅读"书架
    3. 目录显示
    4. 显示搜索结果列表
  * PermissionManager：权限控制类。主要进行动态获取权限、检查权限状况。
* Service/Service/：提供服务接口的类
  * UriFileService：提供使用Intent获取URI访问文件的服务
    1. 启动Intent获取打开文件、文件树的URI的页面
    2. 文件复制：提供文件URI与目录路径执行复制操作
    3. 获取文件树URI对应文件夹下所有的PDF文件的URI
* Service/Listener/：
  * ListenerAddBook：添加PDF文件的按键的点击事件监听器
  * ListenerCatalogueGoBack：目录页面返回按键的点击事件监听器
  * ListenerCatalogueItemOnClick：目录页面目录项的点击事件监听器
  * ListenerCatalogueSubset：目录页面展示子目录按键的点击事件监听器
  * ListenerCreateShell：创建新书架按键的点击事件监听器
  * ListenerDisplayBook：下拉显示电子书按键的点击事件监听器
  * ListenerDisplayBookRecentRead：下拉"最近阅读"书架显示电子书按键的点击事件监听器
  * ListenerOpenCatalogue：阅读页面打开目录按键的点击事件监听器
  * ListenerRemoveRenameBook：电子书块的长按执行重命名或删除的事件监听器
  * ListenerRemoveRenameRecentRead："最近阅读"书架长按执行重命名或删除的事件监听器
  * ListenerRenameRemoveShell：普通书架块的长按执行重命名或删除的事件监听器
  * ListenerSearchItemOnClick：搜索结果列表各项的点击事件监听器
  * ListenerToolbarOnClick：主页面的工具栏(ToolBar)点击事件监听器
* Service/Adapter/：各个ListView的子元素数据接口Adapter
  * ListViewAdapter：用于显示：书架电子书、搜索项电子书的ListView的子元素数据接口
  * ListViewCatalogueSubsetAdapter：主要用于显示目录的子目录的ListView每一项的数据接口
* widget/：JavaBean风格抽象类
  * Book：电子书抽象化
  * Shell：书架抽象化
  * MyListView：自定义ListView控件，解决ScrollView中只显示一项的问题
  * TreeNodeData：树形控件数据类（会用于页面间传输，所以需实现Serializable 或 Parcelable）

### src/main/res/values/strings.xml

各处显示文本的资源文件，可在此处集中修改显示文本。

### 外部依赖

使用的外部依赖是：android-pdfview

开源地址：[GitHub - JoanZapata/android-pdfview: A fast PDF reader component for Android development](https://github.com/JoanZapata/android-pdfview)

主要用于完成PDF文件的解码、渲染、载入工作。

### UI资源

项目中所用的各种SVG图标的来源：[iconfont-阿里巴巴矢量图标库](https://www.iconfont.cn/)

项目的APP图标来源：腾讯云某广告的中间截图所得的图案

### 其他

源码中已经做了详细注释。

具体技术实现可以从我的网站上的文章进行了解。



## 起源

大概就是没事找事然后就做了这么一件事。

之前获得了大量的PDF电子书文件，每次打开WPS来看首先就是广告，然后不小心点到哪里，又是弹出广告......服了=_=。

为了方便此后对PDF电子书和文献的阅读，顺路玩玩Android，于是不动的大图书馆就出现了。

欢迎任何BUG的反馈、新的需求的提出，可在评论区留言或者在我的网站留言。

欢迎对新主题的开发。



## 参考

Android官方文档：[从共享存储空间访问文档和其他文件  | Android 开发者  | Android Developers (google.cn)](https://developer.android.google.cn/training/data-storage/shared/documents-files#grant-access-directory)

关于基本布局：[Android——六大基本布局总结_编程小马-CSDN博客_android 布局](https://blog.csdn.net/qq_40205116/article/details/88418781)

关于Android10/11的分区存储策略：[Android Q & Android 11存储适配(一) 基础知识点梳理 - 掘金 (juejin.cn)](https://juejin.cn/post/6854573214447140871#heading-7)

关于权限的管理：[Android权限（Permissions）处理 - 简书 (jianshu.com)](https://www.jianshu.com/p/3541647480a9)

关于PDF文件目录树的解析：[Android原生PDF功能实现 - 齐行超 - 博客园 (cnblogs.com)](https://www.cnblogs.com/qixingchao/p/11658226.html)

关于URI的使用：[Android中的Uri详解_yy的博客-CSDN博客_android uri](https://blog.csdn.net/sinat_37205087/article/details/102815247)

关于URI的使用：[Android 调用系统文件管理器在指定路径目录打开文件 - SegmentFault 思否](https://segmentfault.com/q/1010000021126126/a-1020000021139219)

关于URI与文件管理器：[Android文件管理器选择文件，获得文件路径URI转File - 简书 (jianshu.com)](https://www.jianshu.com/p/df60c12d27b6)

关于Android的系统资源图标：[Android系统资源图标android.R.drawable - 竹山一叶 - 博客园 (cnblogs.com)](https://www.cnblogs.com/jeffen/p/6845489.html)



## 鸣谢

自然是要感谢我自己。

推广一下：[雾雨霜星 | DA☆ZE (shuangxing.top)](https://www.shuangxing.top/#/)
