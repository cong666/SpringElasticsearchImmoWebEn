package com.chen.app.base;

import com.google.common.collect.Sets;
import org.springframework.data.domain.Sort;

import java.util.Set;

/**
 * Created by: ccong
 * Date: 18/9/2 下午10:38
 */
public class HouseSort {
    public static final String DEFAULT_SORT_KEY = "lastUpdateTime";
    public static final String DISTANCE_TO_SUBWAY_KEY = "distanceToSubway";
    private static final Set<String> SORT_KEYS = Sets.newHashSet(
            DEFAULT_SORT_KEY,
            "createTime",
            "price",
            "area",
            DISTANCE_TO_SUBWAY_KEY
    );

    public static Sort generateSort(String key,String directionKey) {
        key = getSortKey(key);
        Sort.Direction direction =  Sort.Direction.fromString(directionKey);
        if(direction ==null) {
            direction = Sort.Direction.DESC;
        }
        return new Sort(direction,key);
    }

    public static String getSortKey(String key) {
        if(!SORT_KEYS.contains(key)) {
            key = DEFAULT_SORT_KEY;
        }
        return key;
    }

}
