package com.cartoon.data;

import android.text.TextUtils;

import com.cartton.library.utils.DebugLog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Carousel {
//    private static final Logger LOG = LoggerFactory.getLogger(Carousel.class);
    private String url;
    private String title;
    private String poster;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public boolean isVertival() {
        if (!TextUtils.isEmpty(poster)) {
            int[] size = getPosterSize(poster);
            if (size != null && size[0] < size[1]) {
                return true;
            }
        } else {
            DebugLog.e("poster is null.");
        }
        return false;
    }

    public int[] getPosterSize(String poster) {
        Pattern p = Pattern.compile("_(\\d+)_(\\d+).jpg$");
        Matcher m = p.matcher(poster);
        while (m.find()) {
            return new int[]{Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2))};
        }
        return null;
    }

    @Override
    public String toString() {
        return "Carousel [url=" + url + ", title=" + title + ", poster=" + poster + "]";
    }

}
