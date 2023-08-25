<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    const kuweis = [];
    $(document).ready(function () {
        $("td[kuwei]").each(function (index, dom) {
            kuweis.push($(dom).attr("kuwei"));
        });
        const type = $("body").attr("type");
        JQ.ajaxPost("<%=basePath%>stock/monitor/" + type + "/sum", {kuweis: kuweis.join(",")}, function (data) {
            let k;
            if (type === "yl") {
                for (k in data) {
                    $("td[kuwei=" + k + "]").html("<font color='red'>总重:" + data[k] + "KG</font>");
                }
            } else {
                for (k in data) {
                    const info = data[k].toString().split(";");
                    $("td[kuwei=" + k + "]").html("<font color='red'>总托数:" + (isEmpty(info[0]) ? 0 : info[0]) + "&nbsp;总卷数" + (isEmpty(info[1]) ? 0 : info[1]) + "</font>");
                }
            }
        });
    });
</script>
