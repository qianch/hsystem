package com.bluebirdme.mes.core.excel;

import java.util.List;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public class ExcelContent {
    private List<String> titles;
    private List<String[]> data;

    public ExcelContent() {
    }

    public ExcelContent(List<String> titles, List<String[]> data) {
        this.titles = titles;
        this.data = data;
    }

    public List<String> getTitles() {
        return this.titles;
    }

    public ExcelContent setTitles(List<String> titles) {
        this.titles = titles;
        return this;
    }

    public List<String[]> getData() {
        return this.data;
    }

    public ExcelContent setData(List<String[]> data) {
        this.data = data;
        return this;
    }
}
