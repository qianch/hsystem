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
		EasyUI.grid.search("dg", "warehousedetailSearchForm");
	}

	$(function(){

		$('#dg').datagrid({
			url:"${path}stock/productStock/warehouseDetail",
			onBeforeLoad: dgOnBeforeLoad,
		});
	});

	function warehouseCount(value, row){
		return Math.round(row.SUMWEIGHT);
	}
	
	function inDays(value,row){
		if(isEmpty(row.INTIME))return "";
		var intimes=row.INTIME+"";
		var begintime_ms =new Date(intimes.replace(/-/g, "/"));
		var nowtimes=new Date();
		var millions=nowtimes.getTime()-begintime_ms.getTime();
		var days=Math.floor(millions/(24*3600*1000))
		if(days<=30) return "1个月以内";
		if(days>30&&days<=90) return "1-3个月";
		if(days>90&&days<=180) return "3-6个月";
		if(days>180&&days<=360) return "6-12个月";
		if(days>=360) return "12个月以上";
		return days;
	}
	
	function exportDetail(){
		location.href= encodeURI(path +  "stock/productStock/export?"+JQ.getFormAsString("warehousedetailSearchForm"));
	}
	
	 $(function() {
		/* var nowdays = new Date();
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
		
	    $("#start").datetimebox("setValue",
				lastDay + " 08:00:00"); */
		/* $("#end").datetimebox("setValue",
				new Calendar().format("yyyy-MM-dd") + " 20:00:00"); */
		/* filter(); */
	});

</script>