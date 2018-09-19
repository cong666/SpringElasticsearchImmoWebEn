package com.chen.app.base;

/**
 * DataTables（前端框架）相应结构
 *
 * Created by ccong
 */
public class DataTablesResponse {

    //验证结果
    private int draw;
    //总数
    private long recordsTotal;
    //分页
    private long recordsFiltered;

    private Object data;

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public long getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(long recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public long getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(long recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

