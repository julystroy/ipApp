# ipApp
it is a fantasy world
这个是一个完整的app，里面有一套完整的答题系统，每日任务（签到、点赞、评论）升级系统，牵涉到宗门战，商城，道具使用，AR接入，广告云接入等
1.MVP模式； 
2.富文本（图文混排），webview展示活动，视频等； 
3.AR展现角色动画；
4.websocket集成即时通信，实现宗门群聊； 
5.接入h5小游戏； 
6.运用继承card view，recycleview等Android组件实现宗门内游戏，道具购买，任务升级等功能；
7.h5活动页，视频show
 运用到的三方  有这些   可以借鉴使用方法
 //compile 'com.squareup.okhttp:okhttp:2.4.0'
    compile 'com.squareup.okhttp3:okhttp:3.10.0'
    compile 'com.zhy:okhttputils:2.6.2'
    compile 'com.squareup.okhttp3:mockwebserver:3.5.0'
    compile 'com.zhy:autolayout:1.4.3'
    compile 'com.github.bumptech.glide:glide:3.7.0'
    compile 'com.github.junrar:junrar:0.7'
    //compile 'de.greenrobot:greendao:2.1.0'
    compile 'org.greenrobot:greendao:3.0.1'
    compile 'org.greenrobot:greendao-generator:3.0.0'
    compile 'com.squareup.picasso:picasso:2.5.2'
    compile 'com.jakewharton:butterknife:8.0.1'
    apt 'com.jakewharton:butterknife-compiler:8.0.1'
    compile 'com.readystatesoftware.systembartint:systembartint:1.0.3'
    //    compile 'fm.qingting.sdk:qingting:2.1.0'

    // compile 'fm.qingting.sdk:qingting:2.1.5'
    compile 'com.afollestad.material-dialogs:core:0.8.5.9'
    compile 'com.android.support:design:23.2.1'
    compile 'com.github.castorflex.verticalviewpager:library:19.0.1'
    compile 'com.jude:swipebackhelper:3.1.2'
    compile 'org.greenrobot:eventbus:3.0.0'
    compile 'org.sufficientlysecure:html-textview:1.8'
    compile 'com.umeng.analytics:analytics:latest.integration'
    compile 'com.baidu.mobstat:mtj-sdk:latest.integration'
    //bugtags，正式版需移除
    compile "com.google.android:flexbox:$flexboxVersion"
    // compile 'com.bugtags.library:bugtags-lib:latest.integration'
    compile project(':CarttonLibrary')
    compile project(':smoothendlesslibrary')
    compile project(':pickimagelib-release')
    compile project(':sharelib')
    compile 'com.github.chrisbanes.photoview:library:1.2.4'
    compile 'com.android.support:cardview-v7:23.1.0'
    //扩展65535，线上删除
    compile 'com.android.support:multidex:1.0.0'
    compile 'com.github.ksoichiro:android-observablescrollview:1.6.0'
    compile 'in.srain.cube:ultra-ptr:1.0.11'
    compile 'com.nineoldandroids:library:2.4.0'
