
//为所有的datagrid添加表头右键菜单
$.fn.datagrid.defaults.onHeaderContextMenu = createGridHeaderMenu;

//所有的datagrid在重新加载数据前（分页、调整页大小）取消所有选中
$.fn.datagrid.defaults.onBeforeLoad = function(){$(this).datagrid("unselectAll")};

var webroot = window.location.pathname.substring(window.location.pathname.indexOf("\/")+1).substring(0, window.location.pathname.substring(window.location.pathname.indexOf("\/")+1).indexOf("\/"));
var MES_BASE = window.location.protocol+"\/\/" + window.location.host + "\/" + webroot;
/**
 * 对所有的ajax请求做登陆验证
 * 根据后台拦截器设定的状态跳转到登录页面
 */
$(document).ajaxComplete(function(event,xhr,options){
	var param=xhr.getResponseHeader("param");
	if(param == "toLogin"){
		window.top.location.href = MES_BASE+"/login.jsp";
	}
});

/**
 * 创建datagrid表头的右键菜单，实现列的可隐藏
 * @param {} 右键点击事件
 * @param {} field 被点击的表头的字段
 * @param {} tableId datagrid表的id
 * @param {} isShowfrozen 冻结列是否看隐藏
 */
function createGridHeaderMenu(e, field) {
	e.preventDefault();//阻止浏览器捕获右键事件
	var grid = $(this);
	if (!$("#tMenu").length) {
		var tmenu = $('<div id="tMenu" style="width:100px;"></div>').appendTo('body');  
		var fields = grid.datagrid('getColumnFields',false);
		for ( var i = 0; i < fields.length; i++) {
			var field_option = grid.datagrid("getColumnOption", fields[i]);
			var title = field_option.title;
			$('<div iconCls="icon-ok" field="'+fields[i]+'" />').html(title).appendTo(tmenu); 
		}
		tmenu.menu({
			onClick : function(item) {
				var l_field = $(item.target).attr("field");
				if (item.iconCls == "icon-ok") {
					grid.datagrid("hideColumn", l_field);
					tmenu.menu('setIcon', {  
                        target: item.target,  
                        iconCls: 'icon-empty'  
                    });  
				} else {
					grid.datagrid("showColumn", l_field);
					tmenu.menu("setIcon", {
						target : item.target,
						iconCls : "icon-ok"
					});
				}
			}
		});
	}
	$("#tMenu").menu("show", {
		 left:e.pageX,  
         top:e.pageY  
	});
};
/**
 * 显示屏幕右下角的消息框
 * @param {} title 标题
 * @param {} msg 消息内容
 */
function showTips(title,msg){
	$.messager.show({
    title:title,  
	msg:msg,  
	timeout:5000,  
	showType:'slide'  
   });		
}
/**
 * 获取给定时间的前一天日期或后一天日期
 * @param {} date 给定的时间，Date类型 ,为空，则为this，再则默认为当前日期
 * @param {} yesterday_or_tomorrow 参数DATE.YESTERDAY(-1) 或是DATE.TOMORROW(1)
 * @return {}
 */
function getTheDay(yesterday_or_tomorrow,p_date){
	var date = new Date();
	if(p_date){
		date = p_date;
	}else if(typeof this.getDate == "function"){
		date = this;
	}
	//一天的毫秒数 1000*60*60*24
	var one_day_milliseconds = 86400000;
	var the_day_milliseconds = date.getTime();
	if(yesterday_or_tomorrow==-1){
		the_day_milliseconds=the_day_milliseconds-one_day_milliseconds;    
	}
	if(yesterday_or_tomorrow==1){
		the_day_milliseconds=the_day_milliseconds+one_day_milliseconds;    
	}
	var yesterday = new Date();     
	yesterday.setTime(the_day_milliseconds);     
	   
	
	return yesterday;
}
/**
 * 格式化日期 yyy-mm-dd
 * @param {} p_date 指定要格式化的日期，为空则取this，再则则取当前日期
 * @param {} withTime 是否返回时间 boolean类型 为空，则默认为false
 * @return {}
 */
function dateFormatter(p_isWithTime,p_date){
	var date = new Date();
	var isWithTime = false;
	if(p_date){
		date = p_date;
	}else if(typeof this.getDate == "function"){
		date = this;
	}
	if(p_isWithTime==true ||p_isWithTime==false){
		isWithTime = p_isWithTime;
	}
	var myyear = date.getFullYear();  
    var mymonth = date.getMonth()+1;  
    var myweekday = date.getDate();   
    var myHour=date.getHours();
    var myMinute=date.getMinutes();
    var mySecond=date.getSeconds();
    
    if(mymonth < 10){  
        mymonth = "0" + mymonth;  
    }   
    if(myweekday < 10){  
        myweekday = "0" + myweekday;  
    }
    var strdate;
 	if(isWithTime){
    	strdate = myyear+"-"+mymonth + "-" + myweekday + " "+myHour+":"+myMinute+":"+mySecond;
 	}else{
    	strdate = myyear+"-"+mymonth + "-" + myweekday;   
    }
    return strdate;
}

/**
 * 取得给出日期的前后number_of_days天的日期对象{id:date,text:date}数组
 * @param {} number_of_days 前后多少时间
 * @param {} p_date 给出的基准日期，为空则取this，this为空,则取当前日期
 * @return {}
 */
function getDateTreeData(number_of_days,p_date){
	var date;
	var num;
	if(p_date){
		date = p_date;
	}else if(typeof this.getDate == "function"){
		date = this;
	}else{
		date = new Date();
	}
	if(number_of_days){
		num = number_of_days;
	}else{
		num = 10;
	}
	
	var dateArray = new Array();
	// 计算的当前日期
	var c_date = date;
	var value = date.dateFormatter();
	var o_day = {
		id:value,
		text:value
	}
	dateArray.push(o_day);
	//前十天日期
	for(var i=0;i<num;i++){
		c_date = c_date.getTheDay(date.YESTERDAY);
		value = c_date.dateFormatter();
		o_day ={
			id:value,
			text:value
		}
		dateArray.push(o_day);
	}
	//后十天日期
	var c_date = date;
	for(var i=0;i<num;i++){
		c_date = c_date.getTheDay(date.TOMORROW);
		value = c_date.dateFormatter();
		o_day ={
			id:value,
			text:value
		}
		dateArray.push(o_day);
	}
	//排序
	var i=0;
	var j=num;
	while(i<j){
		var temp = dateArray[i];
		dateArray[i] = dateArray[j];
		dateArray[j] = temp;
		i++;
		j--;
	}
	return dateArray;
}
/**
 * 去除字符串首尾空格
 * @param {} str 字符串
 * @return {}
 */
function trim(p_str){
	var start=0,end=0,str="";
	if(p_str||p_str == ""){
		str = p_str;
	}else{
		str = this;
	}
	for(var i=0;i<str.length;i++){
		if(str.charAt(i)!=" "){
			start = i;
			break;
		}
	}
	for(var i=str.length-1;i>=0;i--){
		if(str.charAt(i)!=" "){
			end = i+1;
			break;
		}
	}
	return str.substring(start,end);
}
/**
 * 更改datagrid的width
 */
function changeDatagridWidth(id){
		var grid = $('#'+id);
		var height = $(document.body).height();
  		var width = $(document.body).width()-$(".layout-split-west").width();
  		var options = grid.datagrid('options');
  		options.width = width -20;
  		grid.datagrid('resize',{width:width-12});
}
/**********************************placeholder 兼容 start**********************************/
/**
 * 判断该节点是否是隐藏的
 * @param {} obj dom对象
 * @return {Boolean}
 */
function isHidden(obj){
	if(obj.offsetWidth*obj.offsetHeight==0){
		return true;
	}else{
		return false;
	}
}
function isPlaceHolder(){  //判断浏览器是否支持placeholder
    var input = document.createElement("input");
    return "placeholder" in input;
}
if(!isPlaceHolder()){
    function placeHolder(obj){
        if(!obj){return;}
        this.input = obj;
        this.label = document.createElement("label");
        this.label.innerHTML = obj.getAttribute("placeholder");
        this.label.className = "placeholder";
        if(obj.value != ""){
            this.label.style.display = "none";
        }
        this.init();
    }
    placeHolder.prototype = {
        getxy : function(obj){
            if(document.documentElement.getBoundingClientRect){
                var st=document.documentElement.scrollTop||document.body.scrollTop,
                sl=document.documentElement.scrollLeft||document.body.scrollLeft,
                ct=document.documentElement.clientTop||document.body.clientTop,
                cl=document.documentElement.clientLeft||document.body.clientLeft
                return {left:obj.getBoundingClientRect().left+sl-cl,top:obj.getBoundingClientRect().top+st-ct};
                //return {left:$(obj).offset.left,top:$(obj).offset.top};
            }
            else{
                var l=t=0;
                while(obj){
                    l+=obj.offsetLeft;
                    t+=obj.offsetTop;
                    obj=obj.offsetParent;
                }
                return {top:t,left:l}
            }
        },
        getwh : function(obj){
            return {w:obj.offsetWidth,h:obj.offsetHeight}
        },
        setStyles : function(obj,styles){
            for(var p in styles){
                obj.style[p] = styles[p]+'px';
            }
        },
        init : function(){
            var label = this.label,
            input = this.input,
            xy = this.getxy(input),
            wh = this.getwh(input);
            this.setStyles(label,{'width':wh.w,'height':wh.h,'lineHeight':wh.h,'left':xy.left,'top':xy.top});
            //var parent = $(input).parent().get(0);
            //parent.appendChild(this.label);
            document.body.appendChild(this.label);
            label.onclick = function(){
                this.style.display = "none";
                input.focus();
            }
            input.onfocus = function(){
                label.style.display = "none";
            };
            input.onblur = function(){
                if(this.value == ""){
                    label.style.display = "";
                }
            };
        }
    }
    function init(){
        var inps = document.getElementsByTagName("input");
        for(var i=0,len=inps.length;i<len;i++){
            if(inps[i].getAttribute("placeholder")){
            	if(!isHidden(inps[i])){
                	new placeHolder(inps[i]);
            	}
            }
        }
    }
    window.onload = init;
}
/**********************************placeholder 兼容 end **********************************/
/****************************************************validate  start***********************************************/
/**
 * 远程ajax验证 获取true or false结果 ，true表示验证通过
 * @param {} url ajax 请求的url
 * @param {} obj 对象 ，obj对象的每个属性都是需要传递的参数
 * @return {} boolean类型 true or false
 */
function testAjaxDate(url,obj){	
	var flag;
	var str = new String("");
	for(property in obj){
		str=str+property+"="+obj[property]+"&";
	}
	$.ajax({					
		url:url+"?"+str.substring(0,str.length-1),
		type:"post",
		async: false,
		dataType:"json",
		success: function(item){	
			if(item){
				flag=false;
			}else{
				flag=true;
			}
		} 
	});
	return flag;
}
/**
 * 检查新增和跟新编码的编码编码是否唯一(与本地相比较) true 表示验证通过
 * @param {} inserted 新增对象数据数组
 * @param {} updated 更新对象数据数组
 * @param {} uniqueField 唯一字段名
 * @param {} p_msg alert提示信息 可选 默认msg='本页新增或更新的数据有重复'
 * @param {} id datagrid的id 可选 如有则打开未通过行的验证
 * @return {Boolean}
 */
function testLocalUnique(inserted,updated,uniqueField,p_msg,id){
	var msg = '本页新增或更新的数据有重复';
	if(p_msg){
		msg = p_msg;
	}
	var rows= new Array(0);
	var invalidRows = new Array();
	rows = inserted.concat(updated);
	for(var i=0;i<rows.length-1;i++){
		var flag = true;
		for(var j=i+1;j<rows.length;j++){
			if(rows[i][uniqueField]==rows[j][uniqueField]){
				flag =false;
			}
		}
		if(!flag){
			invalidRows.push(rows[i]);
		}
	}
	if(invalidRows.length>0){
		if(id){
			for(var i=0;i<invalidRows.length;i++){
				var index = $('#'+id).datagrid('getRowIndex',invalidRows[i]);
				$('#'+id).datagrid('beginEdit',index);
			}
		}
		$.messager.alert('系统提示',msg);
		return false;
	}else{
		return true;
	}
}
/**
 * 检查新增和跟新的批量数据是否唯一(与数据库相比较) true为通过验证 
 * @param {} url 远程请求的url
 * @param {} inserted 新增对象数据数组
 * @param {} updated 更新对象数据数组
 * @param {} id datagrid的id 可选 有则打开未通过验证行的编辑
 * @param {} p_array 需传递的参数名数组
 * @return {}
 */
function testRemoteUnique(url,inserted,updated,p_array,id){
	var flag = true;
	for(var i=0;i<inserted.length;i++){
		var obj = new Object();
		for(var j=0;j<p_array.length;j++){
			var pNames = p_array[j].split(".")
			var pName;
			if(pNames.length>1){
				pName = pNames[pNames.length-1];
			}else{
				pName = pNames[0];
			}
			obj[p_array[j]] = inserted[i][pName];
		}
		if(!testAjaxDate(url,obj)){
			if(id){
				var index = $('#'+id).datagrid('getRowIndex',inserted[i]);
				$('#'+id).datagrid('beginEdit',index);
			}
			flag = false;
		}
	}
	for(var i=0;i<updated.length;i++){
		var obj = new Object();
		for(var j=0;j<p_array.length;j++){
			var pNames = p_array[j].split(".")
			var pName;
			if(pNames.length>1){
				pName = pNames[pNames.length-1];
			}else{
				pName = pNames[0];
			}
			obj[p_array[j]] = updated[i][pName];
		}
		if(!testAjaxDate(url,obj)){
			if(id){
				var index = $('#'+id).datagrid('getRowIndex',updated[i]);
				$('#'+id).datagrid('beginEdit',index);
			}
			flag = false;
		}
	}
	if(!flag){
		$.messager.alert('系统提示','请检查唯一性字段的有效性!');
	}
	return flag;
}

//验证当前页中的唯一性：不弹窗
function pageUniqueValidate(inserted,updated,uniqueField,p_msg,id){
	var msg = '本页新增或更新的数据有重复';
	if(p_msg){
		msg = p_msg;
	}
	var rows= new Array(0);
	var invalidRows = new Array();
	rows = inserted.concat(updated);
	for(var i=0;i<rows.length-1;i++){
		var flag = true;
		for(var j=i+1;j<rows.length;j++){
			if(rows[i][uniqueField]==rows[j][uniqueField]){
				flag =false;
			}
		}
		if(!flag){
			invalidRows.push(rows[i]);
		}
	}
	if(invalidRows.length>0){
		if(id){
			for(var i=0;i<invalidRows.length;i++){
				var index = $('#'+id).datagrid('getRowIndex',invalidRows[i]);
				$('#'+id).datagrid('beginEdit',index);
			}
		}
		return false;
	}else{
		return true;
	}
}

//唯一性验证，不弹出提示信息框
function remoteUniqueValidate(url,inserted,updated,p_array,id){
	var flag = true;
	for(var i=0;i<inserted.length;i++){
		var obj = new Object();
		for(var j=0;j<p_array.length;j++){
			var pNames = p_array[j].split(".")
			var pName;
			if(pNames.length>1){
				pName = pNames[pNames.length-1];
			}else{
				pName = pNames[0];
			}
			obj[p_array[j]] = inserted[i][pName];
		}
		if(!testAjaxDate(url,obj)){
			if(id){
				var index = $('#'+id).datagrid('getRowIndex',inserted[i]);
				$('#'+id).datagrid('beginEdit',index);
			}
			flag = false;
		}
	}
	for(var i=0;i<updated.length;i++){
		var obj = new Object();
		for(var j=0;j<p_array.length;j++){
			var pNames = p_array[j].split(".")
			var pName;
			if(pNames.length>1){
				pName = pNames[pNames.length-1];
			}else{
				pName = pNames[0];
			}
			obj[p_array[j]] = updated[i][pName];
		}
		if(!testAjaxDate(url,obj)){
			if(id){
				var index = $('#'+id).datagrid('getRowIndex',updated[i]);
				$('#'+id).datagrid('beginEdit',index);
			}
			flag = false;
		}
	}
	return flag;
}

/*************************************************validate end **************************************************/

/************************************************datagrid 扩展 start**************************************************/
/**
 * 扩展验证的类型
 */
$.extend($.fn.validatebox.defaults.rules, { 
	tel:{ 
 		validator: function(value, param){ 
 				var reg = /^(([0\+]\d{2,3}-)?(0\d{2,3})-)?(\d{7,8})(-(\d{3,}))?$/; 
 				return reg.test(value); 
			}, 
 		message: '请输入有效的电话号码'
 	},
 	phone:{ 
 		validator: function(value, param){ 
 				var reg = /^(((13[0-9]{1})|159|(15[0-9]{1}))+\d{8})$/; 
 				return reg.test(value); 
			}, 
 		message: '请输入有效的手机号码'
 	},
 	IDCard:{
 		validator: function(value, param){
 			var reg = /(^\d{15}$)|(^\d{17}([0-9]|X)$)/; 
 			return reg.test(value);
 		},
 		message:'请输入有效的身份证号码'
 	},
 	validDateType:{
 		validator: function(value, param){
 			var reg = new RegExp("^(?:(?:([0-9]{4}(-|\/)(?:(?:0?[1,3-9]|1[0-2])(-|\/)(?:29|30)|((?:0?[13578]|1[02])(-|\/)31)))|([0-9]{4}(-|\/)(?:0?[1-9]|1[0-2])(-|\/)(?:0?[1-9]|1\\d|2[0-8]))|(((?:(\\d\\d(?:0[48]|[2468][048]|[13579][26]))|(?:0[48]00|[2468][048]00|[13579][26]00))(-|\/)0?2(-|\/)29))))$");
 			return reg.test(value);
 		},
 		message:'请输入正确的日期格式'
 	},
 	beforeCurrDate:{
 		validator: function(value, param){
 			var flag = true;
 			var currDate = new Date(); 
 			var currDateStr = currDate.format('yyyy-MM-dd');
 			if ((new Date(currDateStr)) < (new Date(value)))
 			{
 				flag = false;
 			}
 			
 			return flag;
 		},
 		message:'必须小于等于当前日期'
 	},
 	afterCurrDate:{
 		validator: function(value, param){
 			var flag = true;
 			var currDate = new Date(); 
 			var currDateStr = currDate.format('yyyy-MM-dd');
 			if ((new Date(currDateStr)) > (new Date(value)))
 			{
 				flag = false;
 			}
 			
 			return flag;
 		},
 		message:'必须大于等于当前日期'
 	},
 	compareDateAfter:
 	{
 		validator:function(value, param){
 			var reg = new RegExp("^(?:(?:([0-9]{4}(-|\/)(?:(?:0?[1,3-9]|1[0-2])(-|\/)(?:29|30)|((?:0?[13578]|1[02])(-|\/)31)))|([0-9]{4}(-|\/)(?:0?[1-9]|1[0-2])(-|\/)(?:0?[1-9]|1\\d|2[0-8]))|(((?:(\\d\\d(?:0[48]|[2468][048]|[13579][26]))|(?:0[48]00|[2468][048]00|[13579][26]00))(-|\/)0?2(-|\/)29))))$");
 			var comDate = $(param[0]).datebox('getValue');
 			var flag = true;
 			if (reg.test(value) &&  reg.test(comDate))
 			{
 				if ((new Date(value)) < (new Date(comDate)))
 				{
 					flag = false;
 				}
 			}
 			return flag;
 			
 		},
 		message: '{1}必须大于等于{2}'
 	},
 	safepass: {  
        validator: function (value, param) {  
            return !(/^(([A-Z]*|[a-z]*|\d*|[-_\~!@#\$%\^&\*\.\(\)\[\]\{\}<>\?\\\/\'\"]*)|.{0,5})$|\s/.test(value));   
        },  
        message: '密码由字母和数字组成，至少6位'  
    },
    equalTo: {  
        validator: function (value, param) {  
            return value == $(param[0]).val();  
        },  
        message: '两次输入的字符不一至'  
    },
    number: {  
        validator: function (value, param) {  
            return /^\d*$/.test(value);  
        },  
        message: '请输入数字'
    },
    chinese : {// 验证中文 
        validator : function(value) { 
            return /^[\Α-\￥]+$/i.test(value); 
        }, 
        message : '请输入中文' 
    },
    notChinese : {// 验证非中文 
        validator : function(value) { 
            return (/^[^\Α-\￥]*$/i.test(value)); 
        }, 
        message : '请输入非中文字符' 
    }
});
/**
 * datagrid 扩展方法
 */
$.extend($.fn.datagrid.methods, {
		/**
		 * 结束所有行的编辑 如果验证，返回true为通过验证
		 * @param {} jq
		 * @param {} items 数组 ，第一个参数为boolean类，是否验证，第二个参数为验证失败时，alert的提示信息
		 * @return {Boolean}
		 */
		endEditAll:function(jq,items){
			var grid = jq;
			var isValidate = false; 
			var msg;
			if(items){
				if(typeof items =='boolean'){
					isValidate = items;
				}else{
					isValidate = items[0]
					if(items.length>=2){
						msg= items[1];
					}
				}
				
			}
			var rows = grid.datagrid('getRows');
        	for ( var i = 0; i < rows.length; i++) {
	        	grid.datagrid('endEdit', i);
	        	if(isValidate){
	        		grid.datagrid('beginEdit',i)
		        	if(grid.datagrid('validateRow',i)){
		        		grid.datagrid('endEdit',i)
		        		continue;
		        	}else{
		        		 if(msg){
		        		 	$.messager.alert('系统提示',msg);
		        		 }
		        		 return false;
		        	}
	        	}
        	}
        	return true;		
		},
		/**
		 * 保存最后一次以来的所有更改，数据以json格式传送，分为inserted（插入的数据数组），deleted（删除的数据数组），updated（跟新的数据数组）
		 * @param {} jq
		 * @param {objec} items :url,isShowMsg，msg，inserted，deleted,updated
		 * url：即保存数据提交的地址
		 * isShowMsg:ture|false，可选，默认为true，即成功提交显示提示信息 
		 * msg：isShowMsg 为true是有效，即成功的提示信息
		 * inserted，deleted,updated:即提交的添加、更新、删除的参数名称，默认为inserted、updated、deleted
		 */
		saveChanges:function(jq,items){
			var grid = jq;
			var url;
			var flag;
			var isShowMsg = true;
			if(items.isShowMsg){
				isShowMsg = items.isShowMsg;
			}
			var msg = "保存更改数据成功!";
			url = items.url;
			if(typeof url =="undefined"){
				$.messager.alert('系统提示',"url不可为空！");
				return ;
			}
			var insertedName = items.inserted;
			var deletedName = items.deleted;
			var updateName = items.updated;
			
			if(!grid.datagrid('endEditAll',true)){
				return;
			}
			if(grid.datagrid('getChanges').length){
				 var inserted = grid.datagrid('getChanges', "inserted");
				 var deleted = grid.datagrid('getChanges', "deleted");
				 var updated = grid.datagrid('getChanges', "updated");
				 var effectRow = new Object();
			}
			if(inserted.length){
				if(insertedName){
					effectRow[insertedName] = JSON.stringify(inserted);
				}else{
					effectRow["inserted"] = JSON.stringify(inserted);
				}
			}
			if(deleted.length){
				if(deletedName){
					effectRow[deletedName] = JSON.stringify(deleted);
				}else{
					effectRow["deleted"] = JSON.stringify(deleted);
				}
			}
			if(updated.length){
				if(updateName){
					effectRow[updateName] = JSON.stringify(updated);
				}else{
					effectRow["updated"] = JSON.stringify(updated);
				}
			}
//			 $.post(url, 
//			 	effectRow,
//			    function(data){
//			    	if(isShowMsg){
//				    	$.messager.show({
//							title:"提示",  
//							msg:"保存更改数据成功",  
//							timeout:5000,  
//							showType:'slide'  
//						});	
//			    	}
//					grid.datagrid('acceptChanges');	
//					flag = true;
//				}, 
//				"JSON"
//			).error(function() {
//				$.messager.alert("提示", "保存更改数据失败！");
//				flag = false;
//			});
			
			$.ajax({
				type: "post",
  				url: url,
  				dataType: "JSON",
				data:effectRow,
				async:false,
				success: function(data){
			    	if(isShowMsg){
				    	$.messager.show({
							title:"提示",  
							msg:"保存更改数据成功",  
							timeout:5000,  
							showType:'slide'  
						});	
			    	}
			    	grid.datagrid('acceptChanges');	
					flag = true;
				}
			
			}).error(function() {
				$.messager.alert("提示", "保存更改数据失败！");
				flag = false;
			});
			return flag;
		}
});
/************************************************datagrid 扩展 start**************************************************/

/***/
Date.prototype.format = function(format)
{
    var o =
    {
        "M+" : this.getMonth()+1, //month
        "d+" : this.getDate(),    //day
        "h+" : this.getHours(),   //hour
        "m+" : this.getMinutes(), //minute
        "s+" : this.getSeconds(), //second
        "q+" : Math.floor((this.getMonth()+3)/3),  //quarter
        "S" : this.getMilliseconds() //millisecond
    }
    if(/(y+)/.test(format))
    format=format.replace(RegExp.$1,(this.getFullYear()+"").substr(4 - RegExp.$1.length));
    for(var k in o)
    if(new RegExp("("+ k +")").test(format))
    format = format.replace(RegExp.$1,RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length));
    return format;
}
Date.prototype.YESTERDAY = -1;
Date.prototype.TOMORROW = 1;
Date.prototype.dateFormatter = dateFormatter;
Date.prototype.getTheDay = getTheDay;
Date.prototype.getDateTreeData = getDateTreeData;
String.prototype.trim = trim;
/**
 * 操作成功提示信息
 */
function alertSuccess(){
	$.messager.alert('提示','操作成功','info');
}
//对Date的扩展，将 Date 转化为指定格式的String
//月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
//年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
//例子：
//(new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
//(new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
Date.prototype.Format = function(fmt) { //author: meizz
	var o = {
		"M+" : this.getMonth() + 1, //月份
		"d+" : this.getDate(), //日
		"h+" : this.getHours(), //小时
		"m+" : this.getMinutes(), //分
		"s+" : this.getSeconds(), //秒
		"q+" : Math.floor((this.getMonth() + 3) / 3), //季度
		"S" : this.getMilliseconds() //毫秒
	};
	if (/(y+)/.test(fmt))
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k])
					: (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
}

function SQLDateFormat(sqlDateString){
	sqlDateString=trim(sqlDateString.substring(0,10));
	sqlDateString=sqlDateString.replace(new RegExp("/","g"),"-");
	return sqlDateString;
}

/**
 * 编辑当前行，其他行取消编辑
 * @param {} index 行索引
 * @param {} id 表格ID
 */
function editCurrentRow(index,id){
	var rows=$("#"+id).datagrid("getRows");
	
	if(rows==null){
		return;
	}
	
	if(rows.length==1){
		$("#"+id).datagrid("beginEdit",0)
		return;
	}
	
	for(var i=0;i<rows.length;i++){
		
		if(i==index){
			$("#"+id).datagrid("beginEdit",i);
			continue;
		}
		$("#"+id).datagrid("endEdit",i);
	}
}

/**
 * 结束表格所有行编辑状态
 * @param {} id 表格ID
 */
function editAllEdit(id){
	var rows=$("#"+id).datagrid("getRows");
	for(var i=0;i<rows.length;i++){
		$("#"+id).datagrid("endEdit",i);
	}
}


function openWin(url,winName,w,h,fullScreen)
{
	var newwin;
    var top=Math.abs((screen.height-h)/2);
    var left=Math.abs((screen.width-w)/2);
    if(!fullScreen)
    	fullScreen="no";
	
	newwin=window.open(url,winName,"toolbar=0;location=0,fullscreen="+fullScreen+",status=0,resizable=1,scrollbars=1,menubar=0,width="+w+"px,height="+h+"px,top="+top+",left="+left);
    
    newwin.focus();
    return newwin;
}



















