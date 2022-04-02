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
		EasyUI.grid.search("dg", "summaryDetailSearchForm");
	}
	
	function exportDetail(){
		location.href= encodeURI(path +  "stock/productStock/exportSummary?"+JQ.getFormAsString("summaryDetailSearchForm"));
	}
	
	 $(function() {
         $('#dg').datagrid({
             url:"${path}stock/productStock/summaryDetail",
             onBeforeLoad: dgOnBeforeLoad,
         });
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
	    //var firstDay = year + "-" + month + "-" + "01";//上个月的第一天
	    var myDate = new Date(year, month, 0);
	    var lastDay = year + "-" + month + "-" + myDate.getDate();//上个月的最后一天
	    $("#start").datetimebox("setValue",
				lastDay + " 08:00:00"); 
		/* $("#end").datetimebox("setValue",
				new Calendar().format("yyyy-MM-dd") + " 20:00:00"); */
		/* filter(); */
	});

	/* //呈现列表数据
	 function onLoadSuccess(data){
	       $('#dg').datagrid('appendRow', {
	       	DAYS: '<span class="subtotal">合计</span>',
	       	OLDWEIGHT: '<span class="subtotal">' + compute("OLDWEIGHT").toFixed(1) + '</span>',
	       	NOWWEIGHT: '<span class="subtotal">' + compute("NOWWEIGHT").toFixed(1) + '</span>',
	       	DIFFERENCE: '<span class="subtotal">' + compute("DIFFERENCE").toFixed(1) + '</span>',
	       });
	 }
	 		
	 //指定列求和
	 function compute(colName) {
	     var rows = $('#dg').datagrid('getRows');
	     var total = 0;
	     for (var i = 0; i < rows.length; i++) {
	         total += parseFloat(rows[i][colName]);
	     }
	     return total;
	 }	  */
</script>