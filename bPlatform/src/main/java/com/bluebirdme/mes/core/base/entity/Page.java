package com.bluebirdme.mes.core.base.entity;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public class Page {
    public static String ROWS = "rows";
    private int page = 1;
    private int rows = 15;
    private long totalRows = 0L;
    private int all = 0;

    public Page() {
    }

    public int getAll() {
        return this.all;
    }

    public void setAll(int all) {
        this.all = all;
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return this.getAll() > 0 ? 0 : this.rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public long getTotalRows() {
        return this.totalRows;
    }

    public void setTotalRows(long totalRows) {
        this.totalRows = totalRows;
    }

    public static String getROWS() {
        return ROWS;
    }

    public static void setROWS(String rOWS) {
        ROWS = rOWS;
    }
}
