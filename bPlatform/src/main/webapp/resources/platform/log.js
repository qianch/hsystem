$(document).ready(function () {
    laydate({
        elem: '#startlogDate',
        event: 'focus',
        format: 'YYYY-MM-DD hh:mm:ss',
        istime: true
    });

    laydate({
        elem: '#endlogDate',
        event: 'focus',
        format: 'YYYY-MM-DD hh:mm:ss',
        istime: true
    });

});

function excel() {
    location.href = path + "log/excel";
}

function clearAll() {
    Dialog.confirm(function () {
        $.ajax({
            url: path + "log/clear",
            type: "post",
            dataType: "json",
            success: function (data) {
                if (Tip.hasError(data)) {
                    return;
                }
                Tip.success('已清空所有日志信息');
                filter();
            }
        });
    }, '确定清空?');
}

function filter() {
    EasyUI.grid.search("dg", "logFilter");
}