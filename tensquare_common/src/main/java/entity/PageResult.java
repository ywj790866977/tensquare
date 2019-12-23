package entity;

import java.util.List;

public class PageResult <T> {
    private long tatal;
    private List<T> rows;

    public PageResult() {
    }

    public PageResult(long tatal, List<T> rows) {
        this.tatal = tatal;
        this.rows = rows;
    }

    public long getTatal() {
        return tatal;
    }

    public void setTatal(long tatal) {
        this.tatal = tatal;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
