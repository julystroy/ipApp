package com.cartoon.utils;

import java.util.Collection;

/**
 * Created by jinbangzhu on 7/20/16.
 */

public class CollectionUtils {
    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty() || collection.size() == 0;
    }
}
