package com.kariqu.tyt.http.pkg;

public class ResponsePaging {
    private int total;
    private int page;
    private int count;

    public ResponsePaging() {
        this.total = 0;
        this.page = 0;
        this.count = 0;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
