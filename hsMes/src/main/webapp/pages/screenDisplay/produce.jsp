<!--
作者:宋黎明
日期:2016-9-29 11:46:46
页面:设备机台显示JSP文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>设备机台显示</title>
    <%@ include file="../base/meta.jsp" %>
    <style type="text/css">
        table {
            border: 1;
            width: 100%
        }

        span {
            display: inline-block;
            zoom: 1;
            color: red;
        }

        table tr td {
            font-size: 30px;
            padding: 4px;
        }

        thead tr td span {
            margin-left: 33px;
            font-size: 30px
        }

        thead tr td {
            font-size: 25px
        }

        thead tr th {
            font-size: 25px
        }

        tr {
            height: 35px
        }

        .tip_bar {
            float: left
        }

        .graph {
            position: relative;
            padding: 2px;
            font-size: 18px;
            line-height: 35px;
        }

        .graph .orange {
            position: relative;
            display: block;
            background-color: #56ff00;
            height: 33px
        }

        .prior {
            background: red;
            color: white;
        }
    </style>
    <script type="text/javascript">
        $(function () {
            innerFirstProductInfo();
            setInterval(innerFirstProductInfo, 60000);
        });

        function innerFirstProductInfo() {
            $("#list").empty();
            $.ajax({
                type: "post",
                url: "machineViewFirstInfo",
                data: {"ip": $("#machineIp").text()},
                dataType: 'json',
                success: function (data) {
                    let str = "";
                    for (let i = 0; i < data.length; i++) {
                        initProduceNum(data[i].PRODUCTID, data[i].DEVICEID, data[i].ID);
                        data[i].PRODUCTMODEL = "JD-20161101-BZ3-" + Random.int(1, 80);
                        str += "<tr class='prior'>";
                        str += "<td style='font-size:18px;text-align:center'>" + data[i].PRODUCTMODEL + "</td>";//产品规格
                        str += "<td style='font-size:18px;text-align:center'>" + data[i].CONSUMERNAME + "</td>";//客户名称
                        str += "<td style='font-size:18px;text-align:center'>" + data[i].SALESORDERCODE + "</td>";//客户名称
                        /* str+="<td style='font-size:18px;text-align:center'>"+data[i].SALESORDERDELIVERYTIME+"</td>";//交货日期 */
                        str += "<td style='font-size:18px;text-align:center'>" + data[i].PRODUCTPROCESSCODE + "</td>";//工艺代号
                        str += "<td style='font-size:18px;text-align:center'>" + data[i].PRODUCTPACKAGINGCODE + "</td>";//包装代号
                        data[i].PRODUCTIONCOUNT = Random.int(1, data[i].COUNT);
                        if (data[i].COUNT != null) {
                            str += '<td><div class="graph"><span class="orange" style="width:' + Calc.mul(Calc.div(data[i].PRODUCTIONCOUNT, data[i].COUNT, 2), 100, 0) + '%;" id="main' + data[i].ID + '">' + Calc.mul(Calc.div(data[i].PRODUCTIONCOUNT, data[i].COUNT, 2), 100, 0) + '%</span></div></td>';
                            str += '<td style="font-size:18px;text-align:center">' + data[i].PRODUCTIONCOUNT + '/' + data[i].COUNT + '</td>';
                        } else {
                            str += '<td><div class="graph"><span class="orange" style="width:' + Calc.mul(Calc.div(data[i].PRODUCTIONWEIGHT, data[i].WEIGHT, 2), 100, 0) + '%;" id="main' + data[i].ID + '">' + Calc.mul(Calc.div(data[i].PRODUCTIONWEIGHT, data[i].WEIGHT, 2), 100, 0) + '%</span></div></td>';
                            str += '<td style="font-size:18px;text-align:center">' + data[i].PRODUCTIONWEIGHT + '/' + data[i].WEIGHT + '</td>';
                        }
                        str += "<td style='font-size:18px;text-align:center' id='y" + data[i].ID + "'></td>";//该机台生产数量
                        str += "</tr>";
                    }
                    $("#list").append(str);
                    innerProductInfo();
                }
            });
        }

        function innerProductInfo() {
            $.ajax({
                type: "post",
                url: "machineViewInfo",
                data: {"ip": $("#machineIp").text()},
                dataType: 'json',
                success: function (data) {
                    let str = "";
                    for (let i = 0; i < data.length; i++) {
                        data[i].PRODUCTMODEL = "JD-20161101-BZ3-" + Random.int(1, 80);
                        initProduceNum(data[i].PRODUCTID, data[i].DEVICEID, data[i].ID);
                        str += "<tr  >";
                        str += "<td style='font-size:18px;text-align:center'>" + data[i].PRODUCTMODEL + "</td>";//产品规格
                        str += "<td style='font-size:18px;text-align:center'>" + data[i].CONSUMERNAME + "</td>";//客户名称
                        str += "<td style='font-size:18px;text-align:center'>" + data[i].SALESORDERCODE + "</td>";//客户名称
                        /* str+="<td style='font-size:18px;text-align:center'>"+data[i].SALESORDERDELIVERYTIME+"</td>";//交货日期 */
                        str += "<td style='font-size:18px;text-align:center'>" + data[i].PRODUCTPROCESSCODE + "</td>";//工艺代号
                        str += "<td style='font-size:18px;text-align:center'>" + data[i].PRODUCTPACKAGINGCODE + "</td>";//包装代号
                        data[i].PRODUCTIONCOUNT = Random.int(1, data[i].COUNT);
                        if (data[i].COUNT != null) {
                            str += '<td><div class="graph"><span class="orange" style="width:' + Calc.mul(Calc.div(data[i].PRODUCTIONCOUNT, data[i].COUNT, 2), 100, 0) + '%;" id="main' + data[i].ID + '">' + Calc.mul(Calc.div(data[i].PRODUCTIONCOUNT, data[i].COUNT, 2), 100, 0) + '%</span></div></td>';
                            str += '<td style="font-size:18px;text-align:center">' + data[i].PRODUCTIONCOUNT + '/' + data[i].COUNT + '</td>';
                        } else {
                            str += '<td><div class="graph"><span class="orange" style="width:' + Calc.mul(Calc.div(data[i].PRODUCTIONWEIGHT, data[i].WEIGHT, 2), 100, 0) + '%;" id="main' + data[i].ID + '">' + Calc.mul(Calc.div(data[i].PRODUCTIONWEIGHT, data[i].WEIGHT, 2), 100, 0) + '%</span></div></td>';
                            str += '<td style="font-size:18px;text-align:center">' + data[i].PRODUCTIONWEIGHT + '/' + data[i].WEIGHT + '</td>';
                        }
                        str += "<td style='font-size:18px;text-align:center' id='g" + data[i].ID + "'></td>";//该机台生产数量
                        str += "</tr>";
                    }
                    $("#list").append(str);
                }
            });
        }

        function initProduceNum(productId, deviceId, dataId) {
            $.ajax({
                type: "post",
                url: "machineProduceNum",
                data: {"productId": productId, "deviceId": deviceId},
                dataType: 'json',
                success: function (data) {
                    $("#y" + dataId).text(Random.int(0, 10));
                    $("#g" + dataId).text(Random.int(0, 10));

                }
            });
        }

        function doChange(nv, ov) {
            location.href = location.href.substring(0, location.href.indexOf("?")) + "?ip=" + nv;
        }
    </script>
</head>
<body>
<div class="tip_bar">
    <span style="background: yellow;width:50px;text-align:center;color:black">优先</span>
    <span style="background: #ADFF2F;width:50px;text-align:center;color:black">正常</span>
</div>
<div id="toolbar" class="datagrid-toolbar">
    <!-- <a href="javascript:void(0)" plain="true" class="easyui-linkbutton" onclick="doDelete()" id="delete" iconcls="icon-remove" >删除</a> -->
    &nbsp; 监控机台屏幕&nbsp;
    <input type="text" panelHeight="auto" id="ip" editable="false" class="easyui-combobox"
           style="width:250px;height:25px" data-options="onChange:doChange" url="${path}screen/combobox?all=1"
           valueField="id" textField="text" value="请选择机台屏幕">
</div>
<table id="deviceShow">
    <thead>
    <tr>
        <td colspan="4">机台屏幕名称:<span>${device.machineScreenName}</span></td>
        <td colspan="4">机台屏幕ip:<span id="machineIp">${device.machineScreenIp}</span></td>
    </tr>
    <tr>
        <th>任务单号</th>
        <th>客户名称</th>
        <th>销售订单</th>
        <!-- <th>交货日期</th> -->
        <th>工艺代号</th>
        <th>包装代号</th>
        <th width="20%">订单进度</th>
        <th>实际/总产量</th>
        <th>本机生产数量</th>
    </tr>
    </thead>
    <tbody id="list">
    </tbody>
</table>
</body>