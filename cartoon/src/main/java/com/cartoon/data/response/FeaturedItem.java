package com.cartoon.data.response;

public class FeaturedItem {
    public static final int TYPE_APP = 1;
    public static final int TYPE_AD = 2;

    public long id; // c端 ID
    public int sourceType; //1:应用 2：广告
    public String sourceName;
    public String name; //app名称
    public String logo;
    public String poster;
    public String intro;
    public String shortIntro;
    public int playCount;
    public int commentCount;
    public long publishedAt;
    public float rating;
}
