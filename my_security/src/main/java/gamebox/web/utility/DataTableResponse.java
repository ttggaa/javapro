package gamebox.web.utility;

import java.util.ArrayList;
import java.util.List;

public class DataTableResponse<T> {
    private int draw;
    private ArrayList<T> data;
    private long recordsTotal;
    private long recordsFiltered;

    public DataTableResponse() {
        this.draw = 0;
        this.data = new ArrayList<>();
        this.recordsTotal = 0;
        this.recordsFiltered = 0;
    }

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
