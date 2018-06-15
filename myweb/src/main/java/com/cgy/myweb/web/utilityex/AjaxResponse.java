package com.kariqu.zwsrv.web.utilityex;

import java.util.ArrayList;
import java.util.List;

public class AjaxResponse<T> {
    private int draw;
    private ArrayList<T> data = new ArrayList<>();
    private long recordsTotal;
    private long recordsFiltered;

    public List<T> getData() {
        return data;
    }

    public void add(T t)  {
        data.add(t);
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

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }
}
