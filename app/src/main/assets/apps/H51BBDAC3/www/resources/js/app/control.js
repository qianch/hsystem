function initSelect()
	{
		
		var  select_items=document.getElementsByTagName("select");
		for(var i=0;i<  select_items.length;i++ )
		{
			var selectitem=select_items[i];
			var dataoptions= selectitem.getAttributeNode("data-options").value;
			if(dataoptions!=null&&dataoptions!='')
			{
				dataoptions="{"+dataoptions+"}";
				dataoptions=dataoptions.replace(/'/g, '"');
				var jsondata=JSON.parse(dataoptions);
					var valueField=jsondata["valueField"];
					var textField=jsondata["textField"];
					var url=jsondata["url"];
					App.ajaxGet("http://"+SERVER_IP + url, function(data) {
						if (data.length > 0) {
							var optionstring="";  
							$.each(data,function(infoIndex,info){
								 optionstring += "<option value="+ info[valueField]+">"+ info[textField]+"</option>";
														
							     });
							selectitem.innerHTML=optionstring;
							}
						});
			 }
	   }
}