<!--
	作者:徐波
	日期:2016-10-24 15:08:19
	页面:原料库存表JS文件
-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script>
	

	var dialogWidth = 700, dialogHeight = 350;

	

	function rowStyler(index, row) {
		var str = new Date(row.MATERIALSHELFLIFES);
		str=str.setTime(str.getTime()-8000*60*60);
		var date1 = new Date();
		date1=date1.setDate(date1.getDate()-1);  
		if (str < date1) {
			return 'background-color:red';
		}
	}


	//查询
	function filter() {
		EasyUI.grid.search("dg", "materialStockReportSearchForm");
	}
	
	function export1(){
			location.href= encodeURI(path +  "stock/materialStockState/export?"+JQ.getFormAsString("materialStockReportSearchForm"));
	}
	
	$(function() {
		var nowdays = new Date();
	    var year = nowdays.getFullYear();
	    var month = nowdays.getMonth();
	    if(month==0)
	    {
	        month=12;
	        year=year-1;
	    }
	    if (month < 10) {
	        month = "0" + month;
	    }
	    var firstDay = year + "-" + month + "-" + "01";//上个月的第一天

	    var myDate = new Date(year, month, 0);
	    var lastDay = year + "-" + month + "-" + myDate.getDate();//上个月的最后一天
		
	   /*  $("#start").datetimebox("setValue",
				lastDay + " 08:00:00"); */
		/* $("#end").datetimebox("setValue",
				new Calendar().format("yyyy-MM-dd") + " 20:00:00"); */
		filter();
	});

</script>