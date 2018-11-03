package com.cartoon.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * Created by cc on 17-6-28.
 */
public class RegularUtils {
    private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
    private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
    private static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
    private static final String regEx_space = "\\s*|\t|\r|\n";//定义空格回车换行符
    //去掉网页中图片的标签
    public static String getContent(String htmlStr) {
        //htmlStr= "擦擦擦<img src=\"/storage/emulated/0/XRichText/1498204881157-\"/>阿卡丽<img src=\"/storage/emulated/0/XRichText/1498204888936-\"/> ";
       /* String regxpForImgTag = "<img\\s[^>]+/>";
        Pattern pattern = Pattern.compile(regxpForImgTag);
        Matcher matcher = pattern.matcher(htmlStr);
        while (matcher.find()) {
            String temp = matcher.group();
            htmlStr = htmlStr.replace(temp, "");
        }*/
        //System.out.println(htmlStr+"#############");

        String newstr = "";
        newstr = htmlStr.replaceAll("<[.[^>]]*>","\n");
        newstr = newstr.replaceAll("&nbsp;", "");
        return newstr;
    }



    /**
     * 得到网页中图片的地址
     */
    public static List<String> getImgStr(String htmlStr) {
        //htmlStr= "擦擦擦<img src=\"/storage/emulated/0/XRichText/1498204881157-\"/>阿卡丽<img src=\"/storage/emulated/0/XRichText/1498204888936-\"/>阿卡丽<img src=\"/storage/emulated/0/XRichText/1498204888934-\"/> ";
        List<String> pics = new ArrayList<>();
        String img = "";
        Pattern p_image;
        Matcher m_image;
        //     String regEx_img = "<img.*src=(.*?)[^>]*?>"; //图片链接地址
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile
                (regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(htmlStr);
        while (m_image.find()) {
            // 得到<img />数据
            img = m_image.group();
            // 匹配<img>中的src数据
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while (m.find()) {
                pics.add(m.group(1));
                // System.out.println(pics+"#############");
            }
        }
        return pics;
    }
    /**
     * 修改网页中图片的地址
     */
    public static String getHtmlContent( String htmlStr) {
        //htmlStr= "擦擦擦<img src=\"/storage/emulated/0/XRichText/1498204881157-\"/>阿卡丽<img src=\"/storage/emulated/0/XRichText/1498204888936-\"/>阿卡丽<img src=\"/storage/emulated/0/XRichText/1498204888934-\"/> ";
        Pattern p_image;
        Matcher m_image;
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile
                (regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(htmlStr);
        while (m_image.find()) {
            htmlStr = htmlStr.replaceAll( "/storage/emulated/0/jiangye/","");
        }
        return htmlStr;
    }

    /**
     * @param targetStr 要处理的字符串
     * @description 切割字符串，将文本和img标签碎片化，如"ab<img>cd"转换为"ab"、"<img>"、"cd"
     */
    public static List<String> cutStringByImgTag(String targetStr) {
        List<String> splitTextList = new ArrayList<String>();
        Pattern pattern = Pattern.compile("<img.*?src=\\\"(.*?)\\\".*?>");
        Matcher matcher = pattern.matcher(targetStr);
        int lastIndex = 0;
        while (matcher.find()) {
            if (matcher.start() > lastIndex) {
                splitTextList.add(targetStr.substring(lastIndex, matcher.start()));
            }
            splitTextList.add(targetStr.substring(matcher.start(), matcher.end()));
            lastIndex = matcher.end();
        }
        if (lastIndex != targetStr.length()) {
            splitTextList.add(targetStr.substring(lastIndex, targetStr.length()));
        }
        return splitTextList;
    }

    /**
     * 获取img标签中的src值
     * @param content
     * @return
     */
    public static String getImgSrc(String content){
        String str_src = null;
        //目前img标签标示有3种表达式
        //<img alt="" src="1.jpg"/>   <img alt="" src="1.jpg"></img>     <img alt="" src="1.jpg">
        //开始匹配content中的<img />标签
        Pattern p_img = Pattern.compile("<(img|IMG)(.*?)(/>|></img>|>)");
        Matcher m_img = p_img.matcher(content);
        boolean result_img = m_img.find();
        if (result_img) {
            while (result_img) {
                //获取到匹配的<img />标签中的内容
                String str_img = m_img.group(2);

                //开始匹配<img />标签中的src
                Pattern p_src = Pattern.compile("(src|SRC)=(\"|\')(.*?)(\"|\')");
                Matcher m_src = p_src.matcher(str_img);
                if (m_src.find()) {
                    str_src = m_src.group(3);
                }
                //结束匹配<img />标签中的src

                //匹配content中是否存在下一个<img />标签，有则继续以上步骤匹配<img />标签中的src
                result_img = m_img.find();
            }
        }
        return str_src;
    }

    public static String rePlace(String htmlStr) {
        String s = htmlStr.replaceAll("\n", "<br/>");
        return s.replaceAll(" ","&nbsp;");
    }
}
