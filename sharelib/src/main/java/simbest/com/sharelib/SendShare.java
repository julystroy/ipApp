package simbest.com.sharelib;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by cc on 17-3-13.
 */
public class SendShare {
    private static boolean TEST = false;
    //记录打开app和分享

    public static void requestPost(String id,String token) {
        try {
           // String baseUrl = "http://116.228.59.173:8888/book/book/api/action/addAction?uid="+id+"&action_type=4&token="+token;
            String baseUrl = TEST ?
                    "http://116.228.59.173:8888/book/book/api/shareAction?uid="+id+"&action_type=4&token="+token :  "http://xjzm.mopian.tv/book/book/api/shareAction?uid="+id+"&action_type=4&token="+token;;
                   // "http://10.0.0.228:8080/book/book/api/shareAction?uid="+id+"&action_type=4&token="+token :  "http://xjzm.mopian.tv/book/book/api/share/shareAction?uid="+id+"&action_type=4&token="+token;;
            // String baseUrl = "http://xjzm.mopian.tv/book/book/api/action/addAction?uid="+id+"&action_type=4&token="+token;
           /* //合成参数
            StringBuilder tempParams = new StringBuilder();
            int pos = 0;
            for (String key : paramsMap.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                tempParams.append(String.format("%s=%s", key,  URLEncoder.encode(paramsMap.get(key),"utf-8")));
                pos++;
            }
            String params =tempParams.toString();
            // 请求的参数转换为byte数组
            byte[] postData = params.getBytes();*/
            // 新建一个URL对象
            URL url = new URL(baseUrl);
            // 打开一个HttpURLConnection连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            // 设置连接超时时间
            urlConn.setConnectTimeout(5 * 1000);

            // Post请求必须设置允许输出 默认false
            urlConn.setDoOutput(true);
            //设置请求允许输入 默认是true
            urlConn.setDoInput(true);
            // Post请求不能使用缓存
            urlConn.setUseCaches(false);
            // 设置为Post请求
            urlConn.setRequestMethod("POST");

            // 配置请求Content-Type
            urlConn.setRequestProperty("Content-Type", "application/json");
            // 开始连接
            urlConn.connect();
            // 发送请求参数
           /* DataOutputStream dos = new DataOutputStream(urlConn.getOutputStream());
            dos.write(postData);
            dos.flush();
            dos.close();*/
            if (urlConn.getResponseCode() == 200) {
                // 获取返回的数据
                System.out.println("#########成功");
            } else {
                System.out.println("#########失败");
            }
            // 关闭连接
            urlConn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
