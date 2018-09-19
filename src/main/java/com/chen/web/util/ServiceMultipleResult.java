package com.chen.web.util;

import java.util.List;

/**
 * Created by: ccong
 * Date: 18/8/26 下午8:29
 */
public class ServiceMultipleResult<T> {
    private long total;
    private List<T> result;

    public ServiceMultipleResult(long total, List<T> result) {
        this.total = total;
        this.result = result;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public int getResultSize() {
        if(result!=null) {
            return result.size();
        }
        return 0;
    }
}
