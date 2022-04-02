function isEmpty(obj) {
	if(typeof obj=="number"){
		return false;
	}
	if (obj == undefined || obj == "" || obj == null)
		return true;
	return false;
}

function countFormatter(value, row, index) {
	if (isEmpty(value)) {
		return "-" ;
	}
	var unit = "";
	
	if (row.PRODUCTISTC == 1 || row.productIsTc ==1) {
		return "<font class='unit'>"+new Number(value).toFixed(0)+"套</font>";
	} else {
		return "<font class='unit'>"+new Number(value).toFixed(0)+"卷</font>";
	}
}

function assistCountFormatter(value, row, index) {
	if (isEmpty(value)) {
		return "-" ;
	}
	return new Number(value).toFixed(2)+row.ASSISTUNIT;
}

function rollFormatter(value, row, index) {
	if (isEmpty(value)) {
		return "0";
	}
	return value;
}

function trayFormatter(value, row, index) {
	if (isEmpty(value)) {
		return "0";
	}
	return value;
}

function processFormatter(value, row, index) {
	var unit = "";

/*	if (row.productIsTc == 1) {
		unit = "套";
	} else {
		unit = "kg";
	}*/

	if (isEmpty(value)) {
		return "0" ;
	}
	return new Number(value).toFixed(2);
}

/*function unCompleteCount(value, row, index) {
	//////1:是套材  2:非套材
	var unit = "kg";
	if (row.PRODUCTISTC == 1) {
		unit = "套";
	}
	return (row.PRODUCTCOUNT - (row.PRODUCECOUNT == null ? 0 : row.PRODUCECOUNT)) ;
}*/

function processStyler(value, index, row){
	return "/*background:rgb(170, 171, 171);color:white;*/";
}

function unCompleteStyler(value,index,row){
	return "/*background:rgb(170, 171, 171);color:white;*/";
}

function planIsSettledStyler(index,row){
	if(!isEmpty(row.ISSETTLED)&&row.ISSETTLED==1){
		return "background:#ccc7c7"; 
	}
	return "";
}

function closeStyler(index,row){
	if(isEmpty(row.CLOSED)||row.CLOSED==0){
		return "";
	}
	return "text-decoration:line-through;background: #989696;";
}

function bvFormatter(value,row,index){
	if(isEmpty(row.PACKBOMID))return "无包装";
	if(row.BCBOMCODE=="无包装")return "无包装";
	if(!isEmpty(row.BV)&&!isEmpty(row.BA)&&row.BA!=2){
		return "[变更中]"+row.BV;
	}
	return row.BV;
}

function bvStyler(value,row,index){
	
	if(isEmpty(row))return null;
	if(!isEmpty(row.BV)&&!isEmpty(row.BA)&&row.BA!=2){
		return "background:rgba(255, 214, 0, 0.88);";
	}
	return "";
}


function vFormatter(value,row,index){
	if(row==undefined)return null;
	//套材
	if(row.PRODUCTISTC==1){
		if(!isEmpty(row.TV)&&!isEmpty(row.TA)&&row.TA!=2){
				return "[变更中]"+row.TV;
		}
		return row.TV;
	}else{
		if(!isEmpty(row.FV)&&!isEmpty(row.FA)&&row.FA!=2){
				return "[变更中]"+row.FV;
		}
		return row.FV;
	}
}

function vStyler(value,row,index){
	if(row==undefined)return null;
	if(row.PRODUCTISTC==1||row.productIsTc==1){
		if(!isEmpty(row.TV)&&!isEmpty(row.TA)&&row.TA!=2){
				return "background:rgba(255, 214, 0, 0.88);";
		}
	}else{
		if(!isEmpty(row.FV)&&!isEmpty(row.FA)&&row.FA!=2){
				return "background:rgba(255, 214, 0, 0.88);";
		}
	}
	return "";
}


function getBV(row){
	if(isEmpty(row))return null;
	if(!isEmpty(row.BV)&&!isEmpty(row.BA)&&row.BA!=2){
		return "[变更中]"+row.BV;
	}
	return row.BV;
}

function getV(row){
	if(row==undefined)return null;
	if(row.PRODUCTISTC==1||row.productIsTc==1){
		if(!isEmpty(row.TV)&&!isEmpty(row.TA)&&row.TA!=2){
				return "[变更中]"+row.TV;
		}
		return row.TV;
	}else{
		if(!isEmpty(row.FV)&&!isEmpty(row.FA)&&row.FA!=2&&row.FA!=2){
				return "[变更中]"+row.FV;
		}
		return row.FV;
	}
}

function unCompleteCount(value, row, index) {
	//////1:是套材  2:非套材
	var unit = "";
	if (row.PRODUCTISTC == 1) {
		unit = "";
	}
	return new Number(((isEmpty(row.PRODUCTCOUNT)?0:row.PRODUCTCOUNT) - (isEmpty(row.PRODUCECOUNT)? 0 : row.PRODUCECOUNT))).toFixed(2) ;
}

/*function unCompleteStyler(value,row,index){
	return "background:rgb(221, 0, 35)!important;color:white;";
}
*/

function formatterIsTc(value, row,index) {
	if (value == '1') {
		return "套材";
	} else if(value=="2") {
		return "非套材";
	}else if(value=="-1"){
		return "胚布";
	}else{
		return "未知";
	}
}

function productStyler(value, row,index){
	if (row.PRODUCTISTC == 1) {
		return "background:rgba(0, 255, 80, 0.31);";
	} else if(row.PRODUCTISTC==2) {
		return "";
	}else if(row.PRODUCTISTC==-1){
		return "background:rgba(0, 183, 255, 0.58);";
	}else{
		return "";
	}
}

function dateFormat(value, row,index){
	if(isEmpty(value))return "";
	return value.substring(0,10);
}
function processNumberFormatter(value, row, index) {
	if (isEmpty(value)) {
		value="0" ;
	}
	return parseFloat(value).toFixed(1);

}

function orderStyler(){
	return "color:red;font-weight:bolder;";
}

function printOrderStyler(){
	return "color:red;";
}

function danxiangbu(index,row){
	if(row.partType=="成品胚布")
		return "color:red;font-weight:bolder;";
	return "";
}

function planAssistCountFormatter(value,row,index){
	if(isEmpty(value))return "-";
	var unit = "";
	if(row.planAssistUnit){
		unit=row.planAssistUnit;
	}else{
		unit=row.PLANASSISTUNIT;
	}
	
	return " <font class='unit unit2'>"+value+" "+unit+"</font>";
}


function planCountFormatter(value,row,index){
	if(isEmpty(value))return "-";
	var unit = "";
	if (row.PRODUCTISTC == 1 || row.productIsTc ==1) {
		return "<font class='unit'>"+new Number(value).toFixed(0)+" 套</font>";
	} else {
		return "<font class='unit'>"+new Number(value).toFixed(0)+" 卷</font>";
	}
}

function planCountFormatter2(value,row,index){
	if(row.PARTID){
		return "<font class='unit'>"+row.REQUIREMENTCOUNT+" 卷</font>";
	}
	if(isEmpty(value))return "-";
	return "<font class='unit'>"+new Number(value).toFixed(0)+" 卷</font>";
}

function rcFormatter(value,row,index){
	if(row.PRODUCTISTC==1||row.productIsTc==1)return "-";
	if(isEmpty(value))return "-";
	return " <font class='unit unit3'>"+value+" 卷"+"</font>";
}

function tcFormatter(value,row,index){
	if(isEmpty(value))return "-";
	return "<font class='unit unit3'>"+value+" 托"+"</font>";
}

function totalWeightFormatter(value,row,index){
	var t=value||"-";
	var a=Math.round(row.ALLOCATECOUNT*100)/100||"-";
	return " <font class='unit unit2'>"+a+"<font style='color:#ffffff;font-weight:bold;'> / </font>"+t+" KG</font>";
}

function planTotalWeightFormatter(value,row,index){
	var t=value||"-";
	var a=Math.round(row.PLANTOTALWEIGHT*100)/100||"-";
	return " <font class='unit unit2'>"+a+"<font style='color:#ffffff;font-weight:bold;'> / </font>"+t+" KG</font>";
}

function planTotalWeightFormatter3(value,row,index){
	var t=value||"-";
	var a=Math.round(row.planTotalWeight*100)/100||"-";
	return " <font class='unit unit2'>"+a+"<font style='color:#ffffff;font-weight:bold;'> / </font>"+t+" KG</font>";
}

function planTotalWeightFormatter2(value,row,index){
	var a=row.PLANTOTALWEIGHT||"-";
	return " <font class='unit unit2'>"+a+" KG</font>";
}

function totalWeightFormatter2(value,row,index){
	if(value)
		return " <font class='unit unit2'>"+value+" KG</font>";
	return "-";
}



function totalMetresFormatter(value,row,index){
	return " <font class='unit unit2'>"+(value||0)+" 米</font>";
}

function processFormatter2(value, row, index) {
	
	var rc=row.RC||"-";
	var ts=row.TS||"-";
	var count=row.PRODUCTCOUNT||row.productCount||"-";
	
	if (row.PRODUCTISTC == 1 || row.productIsTc ==1) {
		return " <font class='unit unit3'>"+ts+"<font style='color:#ffffff;font-weight:bold;'> / </font>"+count+" 套</font>";
	} else {
		return " <font class='unit unit3'>"+rc+"<font style='color:#ffffff;font-weight:bold;'> / </font>"+count+" 卷</font>";
	}
}

function processFormatter3(value, row, index) {
	log(row)
	var rc=row.RC||"-";
	var ts=row.TS||"-";
	var count=row.requirementCount||row.REQUIREMENTCOUNT;
	if (row.PRODUCTISTC == 1 || row.productIsTc ==1) {
		return " <font class='unit unit3'>"+ts+"<font style='color:#ffffff;font-weight:bold;'> / </font>"+count+" 套</font>";
	}
		return " <font class='unit unit3'>"+rc+"<font style='color:#ffffff;font-weight:bold;'> / </font>"+count+" 卷</font>";
}

function processFormatter4(value, row, index) {
	
	var rc=row.RC||"-";
	var ts=row.TS||"-";
	var count=row.requirementCount||row.REQUIREMENTCOUNT||"-";
	
	if (row.PRODUCTISTC == 1 || row.productIsTc ==1) {
		return " <font class='unit unit3'>"+ts+"<font style='color:#ffffff;font-weight:bold;'> / </font>"+count+" 套</font>";
	} else {
		return " <font class='unit unit3'>"+rc+"<font style='color:#ffffff;font-weight:bold;'> / </font>"+count+" 卷</font>";
	}
}

function totalTrayCount(value,row,index){
	if(!isEmpty(row.PARTID))return " <font class='unit unit3' style='background:gray;'>无</font>";;
	var tc=row.TC||"-";
	var count=row.totalTrayCount||row.TOTALTRAYCOUNT||"-";
	return " <font class='unit unit3'>"+tc+"<font style='color:#ffffff;font-weight:bold;'> / </font>"+count+" 托</font>";
}

function totalTrayCountFormatter(value,row,index){
	var count=row.totalTrayCount||row.TOTALTRAYCOUNT||"-";
	return " <font class='unit unit3'>"+count+" 托</font>";
}
