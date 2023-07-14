<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<title>部门选择</title>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<script type="text/javascript">
    let valid = null;
    let form;
    $(document).ready(function () {
        laydate({
            elem: '#startTime',
            event: 'focus',
            istime: true,
            format: 'YYYY-MM-DD hh:mm:ss',
            min: laydate.now(),
            choose: function (datas) { //选择日期完毕的回调
                const v = $("#schedule").validate();
                v.element("#startTime");
            }
        });
        form = $("#schedule");
        valid = $("#schedule").validate({
            focusCleanup: true,
            rules: {
                clazz: {
                    required: true
                },
                name: {
                    required: true
                },
                startTime: {
                    required: true
                },
                cron: {
                    required: true
                },
                times: {
                    required: true,
                    digits: true
                },
                intervals: {
                    required: true,
                    digits: true
                }
            },
            onfocusout: function (element) {
                $(element).valid();
            }
        });
        setType();
    });

    function setType() {
        const type = $("#type").val();
        if (type === 1) {
            /* $("#cron").val(""); */
            $("#cron").attr("disabled", "disabled");
            $("#intervals").removeAttr("disabled");
            $("#times").removeAttr("disabled");
            $("#cron").rules("remove");
            $("#times").rules("add", {
                required: true,
                digits: true
            });
            $("#intervals").rules("add", {
                required: true,
                digits: true
            });
        } else {
            $("#cron").removeAttr("disabled");
            $("#intervals").attr("disabled", "disabled");
            $("#times").attr("disabled", "disabled");
            $("#times").rules("remove");
            $("#intervals").rules("remove");
            $("#cron").rules("add", {
                required: true
            });
        }
    }


    function getTaskName() {
        if ($("#clazz").val() != "") {
            $.ajax({
                url: path + "schedule/check",
                type: "post",
                data: {clazz: $("#clazz").val()},
                dataType: "json",
                success: function (data) {
                    if (Tip.hasError(data)) {
                        return;
                    }
                    $("#name").val(data.name);
                }
            });
        }
    }

</script>
<div>
    <form id="schedule" method="post" action="<%=basePath %>schedule/${action eq 'add'?'add':'edit'}"
          autocomplete="off">
        <input type="hidden" name="id" value="${schedule.id}"/> <input type="hidden" name="createTime"
                                                                       value="${schedule.createTime }">
        <table width="100%">
            <tr>
                <td class="title"><span style="color:red;">*</span>任务类名</td>
                <td><input id="clazz" name="clazz" value="${schedule.clazz}"><input type="button"
                                                                                    onclick="getTaskName()"
                                                                                    value="检查"></td>
                <td class="title"><span style="color:red;">*</span>任务名称</td>
                <td><input id="name" name="name" value="${schedule.name}" readonly="readonly"></td>
            </tr>
            <tr>
                <td class="title">任务类型</td>
                <td><select name="type" id="type" onchange="setType()">
                    <option value="1" ${schedule.type eq 1?'selected':'' }>简单任务</option>
                    <option value="2" ${schedule.type eq 2?'selected':'' }>CRON表达式</option>
                </select></td>
                <td class="title"><span style="color:red;">*</span>cron表达式</td>
                <td><input id="cron" name="cron" value="${schedule.cron}" disabled="disabled"></td>
            </tr>
            <tr>
                <td class="title"><span style="color:red;">*</span>启动时间</td>
                <td><input id="startTime" name="startTime" value="${schedule.startTime}" class="laydate-icon"
                           readonly="readonly"></td>
                <td class="title"><span style="color:red;">*</span>执行间隔(秒)</td>
                <td><input id="intervals" name="intervals" value="${schedule.intervals}" maxlength="9"></td>
            </tr>
            <tr>
                <td class="title"><span style="color:red;">*</span>执行次数</td>
                <td><input id="times" name="times" value="${schedule.times}" maxlength="9"></td>
            </tr>
            <tr style="height:80px;">
                <td class="title"><span style="color:red;">*</span>运行描述</td>
                <td colspan="3"><textarea id="runDesc" name="runDesc" style="width:100%;height:100%"
                                          style="resize: none;">${schedule.runDesc}</textarea></td>
            </tr>
        </table>
    </form>
</div>
