<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    //导出到ExcelUrl
    const exportUrl = encodeURI(path + "trayBoxRollRelation/export");
    window.onload = function () {
        $("#start").datetimebox("setValue",
            new Calendar().format("yyyy-MM-01 00:00:00"));
        $("#end").datetimebox("setValue",
            new Calendar().format("yyyy-MM-dd HH:mm:ss"));
        //filter();
    };

    function filter() {
        EasyUI.grid.search("dg", "trayBoxRollRelationSearchForm");
    }

    $(function () {
        $('#dg').datagrid({
            url: "${path}trayBoxRollRelation/list",
            onBeforeLoad: dgOnBeforeLoad,
        });
    });

    /**
     * 托合卷关系Excel导出
     */
    function export1() {
        location.href = encodeURI(exportUrl + "?" + JQ.getFormAsString("trayBoxRollRelationSearchForm"));
    }
</script>