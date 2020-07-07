function initSelect() {
    var select_items = document.getElementsByTagName("select");
    for (var i = 0; i < select_items.length; i++) {
        var item = select_items[i];
        getOption(item);
    }
}

function getOption(item) {
    var dataoptions = item.getAttributeNode("data-options").value;
    if (dataoptions !== null && dataoptions !== '') {
        dataoptions = "{" + dataoptions + "}";
        dataoptions = dataoptions.replace(/'/g, '"');
        var jsondata = JSON.parse(dataoptions);
        var valueField = jsondata["valueField"];
        var textField = jsondata["textField"];
        var url = jsondata["url"];
        App.ajaxGet("http://" + SERVER_IP + url, function (data) {
            if (data.length > 0) {
                var optionstring = "";
                $.each(data, function (infoIndex, info) {
                    optionstring += "<option value=" + info[valueField] + ">" + info[textField] + "</option>";
                });
                item.innerHTML = optionstring;
            }
        });
    }
}

function accAdd(arg1, arg2) {
    var r1, r2, m;
    try {
        r1 = arg1.toString().split(".")[1].length
    } catch (e) {
        r1 = 0
    }
    try {
        r2 = arg2.toString().split(".")[1].length
    } catch (e) {
        r2 = 0
    }
    m = Math.pow(10, Math.max(r1, r2));
    return (arg1 * m + arg2 * m) / m
}
//给Number类型增加一个add方法，调用起来更加方便。
Number.prototype.add = function (arg) {
    return accAdd(arg, this);
}
