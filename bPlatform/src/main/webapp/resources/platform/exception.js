$(document).ready(function () {
    laydate({
        elem: '#startoccurDate',
        event: 'focus',
        format: 'YYYY-MM-DD hh:mm:ss',
        istime: true
    });

    laydate({
        elem: '#endoccurDate',
        event: 'focus',
        format: 'YYYY-MM-DD hh:mm:ss',
        istime: true
    });

});

function excel() {
    location.href = path + "exception/excel";
}

function clearAll() {
    Dialog.confirm(function () {
        $.ajax({
            url: path + "exception/clear",
            type: "post",
            dataType: "json",
            success: function (data) {
                if (Tip.hasError(data)) {
                    return;
                }
                Tip.success('已清空所有异常信息');
                filter();
            }
        });
    }, '确定清空?');
}

function filter() {
    $("#dg").datagrid("reload", {
        "filter[clazz]": $("#clazz").val(),
        "filter[method]": $("#method").val(),
        "filter[startoccurDate]": $("#startoccurDate").val(),
        "filter[endoccurDate]": $("#endoccurDate").val()
    });
}