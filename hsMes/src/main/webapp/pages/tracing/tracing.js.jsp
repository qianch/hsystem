<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script>

var url=path+"/tracing";

function tracing(){

	JQ.ajaxGet(url+"/"+$("#barcode").val(),function(data){
		console.log(data)
		var r=data.roll;
		// if(r.length==0){
		// 	Tip.warn("无效条码");
		// 	return;
		// }

		$("#productName").html(r[0].FACTORYPRODUCTNAME);
		$("#productModel").html(r[0].PRODUCTMODEL);

		$("#orderCode").html(r[0].SALESORDERCODE);
		$("#batchCode").html(r[0].BATCHCODE);
		$("#rollQualityGradeCode").html(r[0].ROLLQUALITYGRADECODE);
		$("#rollDeviceCode").html(r[0].ROLLDEVICECODE);
		$("#userName").html(r[0].USERNAME);
		$("#rollOutputTime").html(r[0].ROLLOUTPUTTIME);
		if(null !== r[0].ROLLWEIGHT){
            $("#rollWeight").html(r[0].ROLLWEIGHT+" KG");
        }

		$("#consumerName").html(r[0].CONSUMERNAME);
        $("#isAbandon").html(r[0].ISABANDON==1 ? '作废':'正常');

		var pack=data.pack;

		if(pack.length>0){
			$("#packTime").html(pack[0].PACKAGINGTIME);
	        $("#packUser").html(pack[0].USERNAME);
		}


		var p=data.process;
		if(p.length>0){
            $("#processCode").html(p[0].PROCESSBOMCODE);
            $("#processVersion").html(p[0].PROCESSBOMVERSION);
            $("#packageCode").html(p[0].BCBOMCODE);
            $("#packageVersion").html(p[0].BCBOMVERSION);
        }


		var tasks="";
		for(var i=0,length=data.pack_task.length;i<length;i++){
			tasks=tasks+"<li>"+data.pack_task[i].CODE+" / "+data.pack_task[i].VERSION+"</li>";
		}


		$("#pack_task").empty().append("<ul>"+tasks+"</ul>");

		var feedings="";
		for(var i=0,length=data.feeding.length;i<length;i++){
			feedings=feedings+"<tr><td>"+data.feeding[i].CODE+"</td><td>"+data.feeding[i].MODEL+"</td><td>"+data.feeding[i].DEVICECODE+"</td><td>"+data.feeding[i].FEEDINGDATE+"</td><td>"+data.feeding[i].USERNAME+"</td></tr>";
        }
		$("#feeding").empty().append(feedings);

		var delivery=data.delivery;

		if(delivery.length>0){
			$("#outTime").html(delivery[0].OUTTIME);
			$("#consumerSimpleName").html(delivery[0].CONSUMERNAME);
		}


		var rkinfo=data.rkinfo;

		if(rkinfo.length>0){
			$("#warehousecode").html(rkinfo[0].WAREHOUSECODE);
			$("#warehouseposcode").html(rkinfo[0].WAREHOUSEPOSCODE);
			$("#username").html(rkinfo[0].USERNAME);
			$("#intime").html(rkinfo[0].INTIME);
		}

        var pdinfo=data.pdinfo;

        if(pdinfo.length>0){
            $("#pendingWarehousecode").html(pdinfo[0].WAREHOUSECODE);
            $("#pendingWarehouseposcode").html(pdinfo[0].WAREHOUSEPOSCODE);
            $("#pendingUsername").html(pdinfo[0].USERNAME);
            $("#pendingIntime").html(pdinfo[0].INTIME);
        }





		var ykinfos="";
		for(var i=0,length=data.ykinfo.length;i<length;i++){
			ykinfos=ykinfos+"<tr><td>"+data.ykinfo[i].ORIGINWAREHOUSECODE+"</td><td>"+data.ykinfo[i].ORIGINWAREHOUSEPOSCODE+"</td><td>"
			+data.ykinfo[i].NEWWAREHOUSECODE+"</td><td>"+data.ykinfo[i].NEWWAREHOUSEPOSCODE+"</td><td>"+data.ykinfo[i].MOVETIME+
			"</td><td>"+data.ykinfo[i].USERNAME+"</td></tr>";
        }
        $("#ykinfos").empty().append(ykinfos);

        var vopenPackBarCodes="";
        var openPackBarCode=data.openpackbarcode;
        for(var i=0;i<openPackBarCode.length;i++){
            vopenPackBarCodes+="<tr><td>"+openPackBarCode[i].OPENPACKDATE+"</td><td>"+openPackBarCode[i].USERNAME+"</td><td>"+openPackBarCode[i].OPENPACKCONTENT+"</td></tr>";
        }
        $("#openPackBarCode").empty().append(vopenPackBarCodes);

        var vabandonBarCodes="";
        var abandonBarCode=data.abandonbarcode;
        for(var i=0;i<abandonBarCode.length;i++){
            vabandonBarCodes+="<tr><td>"+abandonBarCode[i].USERNAME+"</td><td>"+abandonBarCode[i].CREATEDATE+"</td></tr>";
        }
        $("#abandonBarCode").empty().append(vabandonBarCodes);
	})



}

</script>
