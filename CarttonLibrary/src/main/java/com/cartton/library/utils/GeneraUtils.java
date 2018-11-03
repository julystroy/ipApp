package com.cartton.library.utils;


import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 常用的工具类
 * <p>
 * Created by David on 16/5/12.
 */
public final class GeneraUtils {

    /**
     * 是否为空列表
     **/
    public static boolean isEmptyList(List list) {
        return (list == null || list.size() == 0);
    }

    /**
     * 关闭文件流
     **/
    public static void close(Closeable closeable) throws IOException {
        if (closeable != null) {
            synchronized (GeneraUtils.class) {
                closeable.close();
            }
        }
    }

    /**
     * 随机生成数字字符串
     *
     * @param length 数字字符串长度
     **/
    public static String getRandomString(int length) { //length表示生成字符串的长度
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(10);
            sb.append(number);
        }
        return sb.toString();
    }

    /**
     * 获取url 验签字符串
     *
     * @param params 请求参数
     * @return 验签字符串
     * <p>
     * opaque生成的通用步骤如下：
     * 第一步，设发送的数据为集合M，将集合M内非空参数值的参数按照参数名ASCII码从小到大排序（字典序），使用URL键值对的格式
     * （即key1=value1&key2=value2…）拼接成字符串stringA。
     * 第二步，在stringA最后拼接上key(key由后台分配),前面拼接上uri,得到stringSignTemp字符串，并对stringSignTemp进行MD5运算，
     * 再将得到的字符串所有字符转换为小写，得到sign值opaque。
     **/
    public static String getHttpOpaque(HashMap<String, String> params) {
        List<Map.Entry<String, String>> mappingList = null;

        if (params == null || params.isEmpty()) {
            return "";
        }

        StringBuilder builder = new StringBuilder();

        //排序
        mappingList = new ArrayList<Map.Entry<String, String>>(params.entrySet());
        Collections.sort(mappingList, new Comparator<Map.Entry<String, String>>() {
            @Override
            public int compare(Map.Entry<String, String> lhs, Map.Entry<String, String> rhs) {
                return lhs.getKey().compareTo(rhs.getKey());
            }
        });

        for (Map.Entry<String, String> mapping : mappingList) {
            builder.append(mapping.getKey());
            builder.append("=");
            builder.append(mapping.getValue());
            builder.append("&");
        }
        builder.replace(builder.length()-1, builder.length(), "");

        DebugLog.i(builder.toString());

        try {
            return MD5Util.md5Encode(builder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
