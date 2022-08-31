package com.bluebirdme.mes.tracing.controller;

import java.util.Date;

public class TracingInfo implements Comparable<TracingInfo> {
    public String messageType;
    public String Datetitle;
    public String messageinfo;
    public Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDatetitle() {
        return Datetitle;
    }

    public void setDatetitle(String datetitle) {
        Datetitle = datetitle;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageinfo() {
        return messageinfo;
    }

    public void setMessageinfo(String messageinfo) {
        this.messageinfo = messageinfo;
    }

    @Override
    public int compareTo(TracingInfo o) {
        int a = 0;
        if (o.date.before(this.date))
            a = -1;
        return a;
    }
}
