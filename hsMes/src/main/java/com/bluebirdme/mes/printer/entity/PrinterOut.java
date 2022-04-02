package com.bluebirdme.mes.printer.entity;

import java.util.List;

import javax.persistence.Column;

import com.bluebirdme.mes.store.entity.IBarcode;

public class PrinterOut {
    private String printerName;
    private String btwFileUrl;
    private String txtFileUrl;
    private List<IBarcode> li;
    private List<String> contents;

    public List<IBarcode> getLi() {
        return li;
    }

    public void setLi(List<IBarcode> li) {
        this.li = li;
    }

    public String getTxtFileUrl() {
        return txtFileUrl;
    }

    public void setTxtFileUrl(String txtFileUrl) {
        this.txtFileUrl = txtFileUrl;
    }

    public String getPrinterName() {
        return printerName;
    }

    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    public String getBtwFileUrl() {
        return btwFileUrl;
    }

    public void setBtwFileUrl(String btwFileUrl) {
        this.btwFileUrl = btwFileUrl;
    }

    public List<String> getContents() {
        return contents;
    }

    public void setContents(List<String> contents) {
        this.contents = contents;
    }

}
