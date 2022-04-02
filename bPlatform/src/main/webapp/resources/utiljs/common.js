/*****************************************************************************
					common.js 公共函数定义

 @author	kzh
 @version	1.0
 *****************************************************************************/
 /**
 *  @fileoverview  公共函数定义
 *  @author        kzh
 *  @version 1.0
 */
/**
 * 判断输入是否为字符串，并以boolean形式返回
 * @param {Object}    value 输入框的值
 * @return{Boolean}   如果输入框的值是字符串类型返回true,否则返回false
 * @version 1.0 
 */
function is_string(value) {
	return (value != null && value.constructor == String)
}
/**
 * 判断输入是否为数字，并以boolean形式返回
 * @param {Object}    value 输入框的值
 * @return{Boolean}   如果输入框的内容为数值类型返回true,否则返回false
 * @version 1.0
 */
function is_number(value) {
	return (value != null && value.constructor == Number)
}
/**
 * 判断输入是否为数组，并以boolean形式返回
 * @param {Object}    value 输入框的值
 * @return            如果该对象为数组返回true,否则返回false
 * @type Boolean
 * @version 1.0
*/
function is_array(value) {
	return (value != null && value.constructor == Array)
}
/**
 * 判断输入是否为对象，并以boolean形式返回
 */
function is_object(value) {
	return (value != null && value.constructor == Object)
}
/**
 * 判断输入值的类型是否已定义，并以boolean形式返回
 */
function is_defined(value) {
	return (typeof(window[value]) != "undefined");
}
/**
 * 将控件输入框中的日期数据进行格式化显示
 * @param {String}    fieldName 日期输入框的名称
 * @return            如果格式化成功返回true,否则返回false 
 * @type Boolean
 * @version 1.0
 */
function formatDateValue(fieldName) {
	var element = document.getElementById(fieldName);
	var value = element.value;
	if (value == "") { return true; }
	value = format_date(value);
	if (value == "") {
		return false;
	} else {
		element.value = value;
		return true;
	}
}
/**
 * 对日期进行格式化处理，如果输入的日期型不对，会给予相应提示
 * @param {String}    value 日期输入框的值
 * @return            经过处理的日期
 * @type String
 * @version 1.0
 */
function format_date(value) {
	if (value.length > 10) { value = value.substr(0, 10); }
	if (value.indexOf('-') < 0) { value = value.substr(0, 8); }
	var year,month,day;
	if (value.length == 8 && value.search(/[0-9]{8}/) == 0) {
		year = (value.substr(0, 4));
		month = (value.substr(4, 2));
		day = (value.substr(6, 2));
	} else if (value.length == 10 && value.search(/[0-9]{4}-[0-9]{2}-[0-9]{2}/) == 0) {
		year = (value.substr(0, 4));
		month = (value.substr(5, 2));
		day = (value.substr(8, 2));
	} else if (value== "") {
		return "";
	} else {
		alert("日期格式为 'yyyy-mm-dd' 或 'yyyymmdd'"); return "";
	}

	if (year < 1900) { alert("非法日期数据-年份太小"); return ""; }
	if (month < 1 || month > 12) { alert("非法日期数据-非法月份"); return ""; }
	if (day < 1 || day > days_of_month(year, month)) { alert("非法日期数据-非法天数"); return ""; }

	return year + "-" + month + "-" + day;
}
/*
 * below is for MyDate Object,at last should put these in a single JS file
 */
 var solarMonth = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
/**
 * 根据年份和月份获取这个月的天数
 * @param {Number}   y 查询的年份
 * @param {Number}   m 查询的月份  
 * @return           查询月份的天数
 * @type Number
 * @version 1.0
 */
function days_of_month(y,m) {
	if (m==2)
		return(((y%4 == 0) && (y%100 != 0) || (y%400 == 0))? 29: 28);
	else
		return(solarMonth[m - 1]);
}
/**
 * 检查函数是否存在
 * @param{String}	funcname 函数名称
 * @return          如果存在返回true,否则返回false
 * @type Boolean
 * @version 1.0
 */
function is_func_exist(funcname) {
	if (typeof(window[funcname]) == "function") {
		return true;
	} else {
		return false;
	}
}

/**
 * 获取函数的名称
 * @param {Object}	func 函数类型
 * @return      	函数的名称
 * @type String
 * @version 1.0
 */
function get_func_name(func) {
	if (typeof(func) != "function") return "";
	var funcstr = func.toString();
	var i1=funcstr.indexOf('function');
	var i2=funcstr.indexOf('(');
	return trim(funcstr.substring(i1+8,i2));
}

/**
 * 获取函数的内容
 * @param {String}	func 函数类型，类型为"function"
 * @return      	表示函数内容的字符串
 * @type String
 * @version 1.0
 */
function get_func_body(func) {
	if (typeof(func) != "function") return "";
	var funcstr = func.toString();
	var i1=funcstr.indexOf('{');
	var i2=funcstr.lastIndexOf('}');
	return trim(funcstr.substring(i1+1,i2));
}

/**
 * 获取指定元素的父亲窗口对象
 * @param {Object}	obj	指定的元素，如果为空，则使用当前事件源对象
 * @return      	obj对应的父亲窗口对象
 * @type Object
 * @version 1.0
 */
function get_parent_window(obj) {
	if (obj == null) { obj = event.srcElement; }
	if (obj == null) { return window; }

	for (;;) {
		obj = obj.parentElement;
		if (obj.tagName.toUpperCase() == "IFRAME" || obj.tagName.toUpperCase() == "HTML") {
			break;
		}
	}

	if (obj.tagName.toUpperCase() == "HTML") {
		return window;
	} else {
		return obj.contentWindow;
	}
}
/**
 * 设置Cookie值
 * @param {String}	name Cookie的名称
 * @param {String}	value Cookie的值
 * @param{Boolean}	forever	是否是临时的还是永久的，缺省是临时的
 * @version 1.0
 */
function set_cookie(name, value, forever) {
	var cookie = name + "=" + escape(value) + ";path=/;";
	if (forever != null && (forever == true || forever == 'true')) {
		cookie += "expires=Fri, 31 Dec 2050 23:59:59 GMT;";
	}
	document.cookie = cookie;
}

/**
 * 删除Cookie值
 * @param {String}	name Cookie的名称
 * @version 1.0
 */
function remove_cookie(name) {
	var cookie = name + "=;path=/;expires=Fri, 31 Dec 1970 23:59:59 GMT;";
	document.cookie = cookie;
}


/**
 * 获取Cookie值
 * @param {String}	name Cookie的名称
 * @return      	对应该名称的Cookie值，如果该Cookie不存在返回null
 * @type String
 * @version 1.0
 */
function get_cookie(name) {
	var cookies = document.cookie.split(';');
	for (var i = 0; i < cookies.length; i++) {
		var cookie = cookies[i];
		var item = cookie.split('=');
		if (trim(item[0]) == name) {
			if (item[1] != null) {
				return unescape(item[1]);
			} else {
				return "";
			}
		}
	}
	return null;
}

/**
 * 去除字符串两边的空格
 * @param {String}  str 进行处理的字符串
 * @return          处理过的字符串
 * @type String
 * @version 1.0 
 */
function trim(str){
    return str.replace(/(^[\s]*)|([\s]*$)/g, "");
}
/**
 * 去除字符串左边的空格
 * @param {String}  str 进行处理的字符串
 * @return          处理过的字符串
 * @type String
 * @version 1.0 
 */
function lTrim(str){
    return str.replace(/(^[\s]*)/g, "");
}
/**
 * 去除字符串右边的空格
 * @param {String}  str 进行处理的字符串
 * @return          处理过的字符串
 * @type String
 * @version 1.0 
 */
function rTrim(str){
    return str.replace(/([\s]*$)/g, "");
}
/**
 * 计算字符串的长度，一个汉字两个字符
 * @param {String}  str 进行检测的字符串
 * @return          字符串的长度
 * @type Number
 * @version 1.0
 */
function realLength(str){
  return str.replace(/[^\x00-\xff]/g,"**").length;
}
/**
 * 校验字符串是否为整型
 * @param {String}  str 进行校验的字符串
 * @return          如果字符串全部为数字或为空返回true,否则返回false
 * @type Boolean
 * @version 1.0
 * 参考提示信息：输入域必须为数字！
 */
function checkIsInteger(str){
    //如果为空，则通过校验
    if(str == "")
        return true;
    if(/^(\-?)(\d+)$/.test(str))
        return true;
    else
        return false;
}
/**
 * 校验整型是否为非负数
 * @param {String}  str 进行校验的字符串
 * @return          如果输入框的内容是非负数或为空值返回true,否则返回false
 * @type Boolean
 * @version 1.0
 */
function isNotNegativeInteger(str){
    //如果为空，则通过校验
    if(str == "")
        return true;
    if(checkIsInteger(str) == true)
    {
        if(parseInt(str,10) < 0)
            return false;
        else
            return true;
    }
    else
        return false;
}

/**
 * 校验字符串是否为浮点型
 * @param {String}  str 进行校验的字符串
 * @return          如果输入框的内容是浮点型小数或为空值返回true,否则返回false
 * @type Boolean
 * @version 1.0
 */
function checkIsDouble(str){
    //如果为空，则通过校验
    if(str == "")
        return true;
    //如果是整数，则校验整数的有效性
    if(str.indexOf(".") == -1){
        if(checkIsInteger(str) == true)
            return true;
        else
            return false;
    }else{
        if(/^(\-?)(\d+)(.{1})(\d+)$/g.test(str))
            return true;
        else
            return false;
    }
}

/**
 * 校验浮点型是否为非负数
 * @param {String}  str 进行校验的字符串
 * @return          如果输入框的内容是非负数或为空值返回true,否则返回false
 * @type Boolean
 * @version 1.0
 */
function isNotNegativeDouble(str){
    //如果为空，则通过校验
    if(str == "")
        return true;
    if(checkIsDouble(str) == true){
        if(parseFloat(str) < 0)
            return false;
        else
            return true;
    }
    else
        return false;
}
/**
 * 校验字符串是否为日期型
 * @param {String}  str 进行校验的字符串
 * @return          如果字符串为日期型或空值返回true,否则返回false
 * @type Boolean
 * @version 1.0
 */
function checkIsValidDate(str){
    //如果为空，则通过校验
    if(str == "")
        return true;
    var pattern = /^((\d{4})|(\d{2}))-(\d{1,2})-(\d{1,2})$/g;
    if(!pattern.test(str))
        return false;
    var arrDate = str.split("-");
    if(parseInt(arrDate[0],10) < 100)
        arrDate[0] = 2000 + parseInt(arrDate[0],10) + "";
    var date =  new Date(arrDate[0],(parseInt(arrDate[1],10) -1)+"",arrDate[2]);
    if(date.getYear() == arrDate[0]
      && date.getMonth() == (parseInt(arrDate[1],10) -1)+""
      && date.getDate() == arrDate[2])
        return true;
    else
        return false;
}
/**
 * 校验两个日期的先后
 * @param {String}  strStart 进行校验的日期
 * @param {String}  strEnd   进行校验的日期
 * @return          如果起始日期早于等于终止日期或其中有一个日期为空返回true,否则返回false
 * @type Boolean
 * @version 1.0
 */
function checkDateEarlier(strStart,strEnd){
    if(checkIsValidDate(strStart) == false || checkIsValidDate(strEnd) == false)
        return false;
    //如果有一个输入为空，则通过检验
    if (( strStart == "" ) || ( strEnd == "" ))
        return true;
    var arr1 = strStart.split("-");
    var arr2 = strEnd.split("-");
    var date1 = new Date(arr1[0],parseInt(arr1[1].replace(/^0/,""),10) - 1,arr1[2]);
    var date2 = new Date(arr2[0],parseInt(arr2[1].replace(/^0/,""),10) - 1,arr2[2]);
    if(arr1[1].length == 1)
        arr1[1] = "0" + arr1[1];
    if(arr1[2].length == 1)
        arr1[2] = "0" + arr1[2];
    if(arr2[1].length == 1)
        arr2[1] = "0" + arr2[1];
    if(arr2[2].length == 1)
        arr2[2]="0" + arr2[2];
    var d1 = arr1[0] + arr1[1] + arr1[2];
    var d2 = arr2[0] + arr2[1] + arr2[2];
    if(parseInt(d1,10) > parseInt(d2,10))
      return false;
    else
      return true;
}
/**
 * 校验email地址是否合法
 * @param {String}  str 进行校验的字符串 
 * @return          如果email地址合法或为空返回true,否则返回false
 * @type Boolean
 * @version 1.0
 */
function checkEmail(str)
{
    //如果为空，则通过校验
    if(str == "")
        return true;
    if (str.charAt(0) == "." || str.charAt(0) == "@" || str.indexOf('@', 0) == -1
        || str.indexOf('.', 0) == -1 || str.lastIndexOf("@") == str.length-1 || str.lastIndexOf(".") == str.length-1)
        return false;
    else
        return true;
}
/**
 * 校验字符串是否为中文
 * @param {String}  str 进行校验的字符串
 * @return          如果字符串为中文或为空返回true,否则返回false
 * @type Boolean
 * @version 1.0
 */
function checkIsChinese(str){
    //如果值为空，通过校验
    if (str == "")
        return true;
    var pattern = /^([\u4E00-\u9FA5]|[\uFE30-\uFFA0])*$/gi;
    if (pattern.test(str))
        return true;
    else
        return false;
}

/**
 * 校验字符串是否符合自定义正则表达式
 * @param {String}  str 进行校验的字符串
 * @param {String}  pat 自定义的正则表达式
 * @return          如果字符串符合自定义正则表达式或为空返回true,否则返回false
 * @type Boolean
 * @version 1.0
 */
function checkMask(str,pat){
    //如果值为空，通过校验
    if (str == "")
        return true;
    var pattern = new RegExp(pat,"gi")
    if (pattern.test(str))
        return true;
    else
        return false;
}
/**
 * 校验普通电话、传真号码：可以“+”开头，除数字外，可含有“-”
 * @param {String}  s 进行校验的字符串
 * @return          如果校验合格返回true,否则返回false
 * @type Boolean
 * @version 1.0
 */
function isTel(s){
	var patrn=/^[+]{0,1}(\d){1,3}[ ]?([-]?((\d)|[ ]){1,12})+$/;
	if (!patrn.exec(s)) return false
	return true
}

/**
 * 校验手机号码：必须以数字开头，除数字外，可含有“-”
 * @param {String}  s 进行校验的字符串
 * @return          如果校验合格返回true,否则返回false
 * @type Boolean
 * @version 1.0
 */
function isMobil(s){
	var patrn=/^[+]{0,1}(\d){1,3}[ ]?([-]?((\d)|[ ]){1,12})+$/;
	if (!patrn.exec(s)) return false
	return true
}

/**
 * 校验邮政编码
 * @param {String}  s 进行校验的字符串
 * @return          如果校验合格返回true,否则返回false
 * @type Boolean
 * @version 1.0
 */
function isPostalCode(s){
	var patrn=/^[a-zA-Z0-9 ]{3,12}$/;
	if (!patrn.exec(s)) return false
	return true
}

/**
 * 判断输入框的值是否符合条件要求
 * @param {Object}  ObjStr   输入框   
 * @param {Object}  ObjType  输入框要求输入的数据类型 : integer 整型，还可判断正整型和负整型,number 数值型，同样可判断正负,date 日期型，可支持以自定义分隔符的日期格式，缺省是以'-'为分隔符,string 判断一个字符串包括或不包括某些字符
 * @return          如果符合要求返回true,否则返回false
 * @type Boolean
 * @version 1.0
 */
 /*
 * 例子：
 *  example 1:要求检验输入框text1的输入数据是否是“整型”数据，若不是，则提示
 *　　　　if (!LogicalValue('text1','integer')) alert('Error: Your must input a integer number');
 *　example 2:要求检验输入框text1的输入数据是否是“正整型”数据，若不是，则提示　　
 *　　　if (!LogicalValue('text1','integer','+')) alert('Error: Your must input a positive integer number');
 *　example 3:要求检验输入框text1的输入数据是否是“负整型”数据，若不是，则提示　　
 *　　　if (!LogicalValue('text1','integer','-')) alert('Error: Your must input a negative integer number');
 *　exmaple 4:要求检验输入框text1的输入数据是否是数值，若不是，则提示　　
 *　　　if (!LogicalValue('text1','number')) alert('Error: Your must input a number');
 *　exmaple 5:要求检验输入框text1的输入数据是否是“正”数值，若不是，则提示　　
 *　　　if (!LogicalValue('text1','number','+')) alert('Error: Your must input a number');
 *　exmaple 6:要求检验输入框text1的输入数据是否是“负”数值，若不是，则提示　　
 *　　　if (!LogicalValue('text1','number','-')) alert('Error: Your must input a number');
 *　　example 7:要求检验输入框text1的输入数据是否是日期型，若不是，则提示
 *　　　if (!LogicalValue('text1','date')) alert('Error: Your must input a logical date value');
 *　　　若它的分隔符为A，“A”是一个变量，如常用的'-'和'/'，则用如下语法
 *　　　if (!LogicalValue('text1','date',A)) alert('Error: Your must input a logical date value');
 *　　　或当分隔符为'/'时
 *　　　if (!LogicalValue('text1','date','/')) alert('Error: Your must input a logical date value');
 *　　　当分隔符为'-'时，可不要参数'-'，可带上
 *　example 8:要求检验输入框text1的输入表示颜色的字符串是否合理，若不合理，则提示
 *　　　if (!LogicalValue('text1','string','0123456789ABCDEFabcdef')) alert('Error: Your must input a logical color value');
 *　example 9:要求检验输入框text1的输入表示密码的字符串是否不含“'"@#$%&^*”这些字符，若含有，则提示
 *　　　if (!LogicalValue('text1','string','\'"@#$%&^*',false)) alert('Error: Your password can not contain \'"@#$%&^*');
 *　　　其中false表示不包含有某些字符，true表示必须是哪些字符，缺省值为true
*/
function LogicalValue(ObjStr,ObjType){
　 var str='';
　 if ((ObjStr==null) || (ObjStr=='') || ObjType==null){
　　　alert('函数LogicalValue缺少参数');
　　　return false;
　 }
　 var obj = document.all(ObjStr);
　 if (obj.value=='') return false;
　 for(var i=2;i<arguments.length;i++){　
　　　if (str!='')
　　　　 str += ',';
　　　str += 'arguments['+i+']';
　 }
　 str=(str==''?'obj.value':'obj.value,'+str);
　 var temp=ObjType.toLowerCase();
　 if (temp=='integer'){
　　　 return eval('IsInteger('+str+')');
　 }else if (temp=='number'){
　　　return eval('IsNumber('+str+')');
　 }else if (temp=='string'){
　　　return eval('SpecialString('+str+')');
　 }else if (temp=='date'){
　　　return eval('IsDate('+str+')');
　 }else {
　　　alert('"'+temp+'"类型在现在版本中未提供');
　　　return false;
　 }
}

/**
 * 检验数字型字符串是否为整型字符串
 * @param {String}   string 进行校验的字符串
 * @param {Object}   sign   如果需要对正负做判断sign为'+'或'-'，否则sign可以不用
 * @return           如果为整型的则返回true,否则返回false
 * @type Boolean
 * @version 1.0
 */
 /*
 * 例子
 * var a = '123';
 *　 if (IsInteger(a))
 *　 {
 *　　　alert('a is a integer');
 *　 }
 *　 if (IsInteger(a,'+'))
 *　 {
 *　　　alert(a is a positive integer);
 *　 }
 *　 if (IsInteger(a,'-'))
 *　 {
 *　　　alert('a is a negative integer');
 *　 }
 */
function IsInteger(string ,sign){
	var integer;
　 if ((sign!=null) && (sign!='-') && (sign!='+')) {
　　　alert('IsInter(string,sign)的参数出错：\nsign为null或"-"或"+"');
　　　return false;
　 }
　 integer = parseInt(string);
　 if (isNaN(integer)){
　　　return false;
　 }else if (integer.toString().length==string.length){　
　　　if ((sign==null) || (sign=='-' && integer<0) || (sign=='+' && integer>0)){
　　　　 return true;
　　　}else
　　　　 return false;　
　 }
　 else
　　　return false;
}

/**
 * 校验字符串是否为日期格式的字符串
 * @param {String}  DateString 进行校验的字符串
 * @param {String}  Dilimeter  日期的分隔符，缺省值为'-'
 * @return          如果是日期格式的返回true,否则返回false
 * @type Boolean
 * @version 1.0
 */
 /*
 * 例子
 *  var date = '1999-1-2';
 *　 if (IsDate(date))
 *　 {
 *　　　alert('You see, the default separator is "-");
 *　 }
 *　 date = '1999/1/2";
 *　 if (IsDate(date,'/'))
 *　 {
 *　　　alert('The date\'s separator is "/");
 *　 }
 */
function IsDate(DateString , Dilimeter){
　 if (DateString==null) return false;
　 if (Dilimeter=='' || Dilimeter==null)Dilimeter = '-';
　 var tempy='';
　 var tempm='';
　 var tempd='';
　 var tempArray;
　 if (DateString.length<8 && DateString.length>10) return false;　　　 
　 tempArray = DateString.split(Dilimeter);
　 if (tempArray.length!=3)return false;
　 if (tempArray[0].length==4){
　　　tempy = tempArray[0];
　　　tempd = tempArray[2];
　 }else{
　　　tempy = tempArray[2];
　　　tempd = tempArray[1];
　 }
　 tempm = tempArray[1];
　 var tDateString = tempy + '/'+tempm + '/'+tempd+' 8:0:0';//加八小时是因为我们处于东八区
　 var tempDate = new Date(tDateString);
　 if (isNaN(tempDate))return false;
　if (((tempDate.getUTCFullYear()).toString()==tempy) && (tempDate.getMonth()==parseInt(tempm)-1) && (tempDate.getDate()==parseInt(tempd))){
　　　return true;
　}else{
　　　return false;
　 }
}

/**
 * 校验个数字型字符串是否为数值型字符串
 * @param {String}   string 进行校验的字符串
 * @param {Object}   sign   如果需要对正负做判断sign为'+'或'-'，否则sign可以不用
 * @return           如果为数值型的则返回true,否则返回false
 * @type Boolean
 * @version 1.0
 */
 /*
 * 例子
 *  var a = '123';
 *　 if (IsNumber(a))
 *　 {
 *　　　alert('a is a number');
 *   }
 *　 if (IsNumber(a,'+'))
 *　 {
 *　　　alert(a is a positive number);
 *　 }
 *　 if (IsNumber(a,'-'))
 *　 {
 *　　　alert('a is a negative number');
 *　 }
 */
function IsNumber(string,sign){
　 var number;
　 if (string==null) return false;
　 if ((sign!=null) && (sign!='-') && (sign!='+')){
　　　alert('IsNumber(string,sign)的参数出错：\nsign为null或"-"或"+"');
　　　return false;
　 }
　 number = new Number(string);
　 if(isNaN(number)){
　　　return false;
　 }else if ((sign==null) || (sign=='-' && number<0) || (sign=='+' && number>0)){
　　　return true;
　 }else
　　　return false;
}

/**
 * 校验一个字符串是否含有或不含有某些字符
 * @param {String}  string  进行校验的字符串
 * @param {String}  compare 比较的字符串(基准字符串) 
 * @param {Boolean} BelongOrNot： true或false，“true”表示string的每一个字符都包含在compare中， “false”表示string的每一个字符都不包含在compare中
 * @return          如果校验成功返回true,否则返回false
 * @type Boolean
 * @version 1.0
 */
 /*
 * sample1:
 *　 var str = '123G';
 *　 if (SpecialString(str,'1234567890'))
 *　 {
 *　　　alert('Yes, All the letter of the string in \'1234567890\'');
 *　 }
 *　 else
 *　 {
 *　　　alert('No, one or more letters of the string not in \'1234567890\'');
 *　 }
 *　 结果执行的是else部分
 * sample2:
 *　 var password = '1234';
 *　 if (!SpecialString(password,'\'"@#$%',false)) 
 *　 {
 *　　　alert('Yes, The password is correct.');
 * 　 }
 *　else
 *　 {
 *　　　alert('No, The password is contain one or more letters of \'"@#$%\'');
 *　 }　
 *　 结果执行的是else部分
 */
function SpecialString(string,compare,BelongOrNot){
　 if((string==null) || (compare==null) || ((BelongOrNot!=null) && (BelongOrNot!=true) && (BelongOrNot!=false))){
　　　alert('function SpecialString(string,compare,BelongOrNot)参数错误');
　　　return false;
　 }
　 if(BelongOrNot==null || BelongOrNot==true){
　　　for(var i=0;i<string.length;i++){
　　　　 if (compare.indexOf(string.charAt(i))==-1)return false;
　　　}
　　　return true;
　 }else{
　　　for(var i=0;i<string.length;i++){
　　　　 if(compare.indexOf(string.charAt(i))!=-1)return false;
　　　}
　　　return true;
　 }
}

/**
 * 字符掩码的定义,字符的pattern，如：’0’*　或　*’0’
 * 如果输入的域的类型为text，并且设置pattern，则以引号中的字符在用
 * 用户输入的数据前或之后填充，直到长度与format中定义的长度相等为止。
 * 用户输入的字符：
 * *：代表可以输入任何可见字符
 * d: 代表输入数字
 * s: 代表输入字符和数字，不包含空格
 * S：代表输入字符和数字，包含空格
 * @param {String}  values        进行处理的字符串  
 * @param {String}  pattern       掩码格式 
 * @param {Number}  input_length  掩码长度
 * @return          处理过的字符串
 * @type String
 * @version 1.0
 */
function format_string_pattern(values,pattern,input_length){
	 var ret="";
	 if(values==null||values==""||values==undefined)return ret;
   if(input_length==null||input_length==''||input_length==undefined){
      return values;
   }
    if(pattern == null||pattern == ""){
  	 return values;
  	}
    var add,add_end="";
  	var result;
    var first_pattern=pattern.charAt(0);//取出第一个字符
    if(pattern.length<3){
     	return values;
     }
    if((first_pattern=="d")&&(((pattern.charAt(1)=="'")||(pattern.charAt(1)=='"'))&&
 	  ((pattern.charAt(pattern.length-1)=="'")||(pattern.charAt(pattern.length-1)=='"')))){
 	        if(parseInt('1'+values)=='1'+values){
                var addstr = pattern.substring(2,pattern.length-1);
  	    	    result=values;
  	    	    if(addstr.length==0){
  	    	    	return values;
  	    	   	}else{
  	    	    while(result.length<input_length){
  		  	         result=result+addstr;
  		  	    }
  		  	    result=result.substring(0,input_length);
  		  	    return result;
  		  	    }
  		}
  		  	else{
  		  	  alert("请输入数字!");
  	          return values;
  		  	}
    }
  	else if(((first_pattern=="'")||(first_pattern=='"'))&&
  		   ((pattern.charAt(pattern.length-1))=="d")&&
  		   ((pattern.charAt(pattern.length-2)=="'")||(pattern.charAt(pattern.length-2)=='"'))){
           if(parseInt('1'+values)=='1'+values){
  	           addstr=pattern.substring(1,pattern.length-2);
  	           result=values;
  	           if(addstr.length==0){
  	           	  return values;
  	           	}else{
  		  	   while(result.length<input_length){
  		  			result=addstr+result;
  		  	   }
  		  	   result=result.substring((result.length-input_length),result.length);
  		  	   return result;
  		          }
  		}
  		   else{
  		  	   alert("请输入数字！");
  		   }
  	}

  	if((pattern.length>0)&&(first_pattern=="s")&&
  	  (((pattern.charAt(1)=="'")||(pattern.charAt(1)=='"'))&&
  	  ((pattern.charAt(pattern.length-1)=="'")||(pattern.charAt(pattern.length-1)=='"')))){
  	      if(values.constructor==String){
  		      var Spacebar=/ /g;//除去空格
  		      values=values.replace(Spacebar, "");
  	   	      addstr=pattern.substring(2,pattern.length-1);
  	   	      result=values;
  	   	      if(addstr.length==0){
  	   	      	return values;
  	   	      }else{
  	   		  while(result.length<input_length){
  		  		 result=result+addstr;
  		  	  }
  		  	  result=result.substring(0,input_length);
  		  	     return result;
  	          }
  	    }

  	}
  	else if(((first_pattern=="'")||(first_pattern=='"'))&&
  		   ((pattern.charAt(pattern.length-1))=="s")&&
  		   ((pattern.charAt(pattern.length-2)=="'")||(pattern.charAt(pattern.length-2)=='"'))){
  		     if(values.constructor ==String){
  			   var Spacebar=/ /g;//除去空格
  		       values=values.replace(Spacebar, "");
  	           addstr=pattern.substring(1,pattern.length-2);
  	           result=values;
  	           if(addstr.length==0){
  	           	return values;
  	           }else{
  		  	   while(result.length<input_length){
  		  			result=addstr+result;
  		  	   }
  		       result=result.substring(result.length-input_length,result.length);
  		  	        return result;
  		  	   }
  		  	}
  	}
  	if((pattern.length>0)&&(first_pattern=="*")&&
  	  (((pattern.charAt(1)=="'")||(pattern.charAt(1)=='"'))&&
  	  ((pattern.charAt(pattern.length-1)=="'")||(pattern.charAt(pattern.length-1)=='"')))){
  	        var addstr=pattern.substring(2,pattern.length-1);
  	        result=values;
  	        if(addstr.length==0){
  	        return values;
  	        }else{
  		  	while(result.length<input_length){
  		  	     result=result+addstr;
  		    }
  		         result=result.substring(0,input_length);
  		  	       return result;
  	        }
    }

    else if(((pattern.charAt(pattern.length-1))=="*")&&(first_pattern=="'")||(first_pattern=='"')
           &&((pattern.charAt(pattern.length-2)=="'")||(pattern.charAt(pattern.length-2)=='"'))){
        	 var addstr=pattern.substring(1,pattern.length-2);
  	         result=values;
  	         if(addstr.length==0){
  	         return values;
  	         }else{
  	         while(result.length<input_length){
  		  	       result=addstr+result;
  		     }
  		     result=result.substring(result.length-input_length,result.length);
  		     return result;
             }
   }

   if((pattern.length>0)&&(first_pattern=="S")&&
  	 (((pattern.charAt(1)=="'")||(pattern.charAt(1)=='"'))&&
  	 ((pattern.charAt(pattern.length-1)=="'")||(pattern.charAt(pattern.length-1)=='"')))){
  		    if(values.constructor ==String){
  	   	    addstr=pattern.substring(2,pattern.length-1);
  	   	    result=values;
  	   	    if(addstr.length==0){
  	   	    	return values;
  	   	    }else{
  	   	    while(result.length<input_length){
  		  	      result=result+addstr;
  		  	}
  		  	result=result.substring(0,input_length);
  		  	     return result;;
  		     }
  		    }
  	}

    else if(((pattern.charAt(pattern.length-1))=="S")&&(first_pattern=="'")||(first_pattern=='"')&&
  		   ((pattern.charAt(pattern.length-2)=="'")||(pattern.charAt(pattern.length-2)=='"'))){
  		    if(values.constructor ==String){
  	        addstr=pattern.substring(1,pattern.length-2);
  	             result=values;
  	        if(addstr.length==0){
  	        	return values;
  	        }else{
  		    while(result.length<input_length){
  		  	      result=addstr+result;
  		    }
  		    result=result.substring(result.length-input_length,result.length);
  		  	     return result;
  		  	 }
  		   }
   }
  		    else{
  		 	  //alert("你输入的pattern格式有误！");
  		 	  result=values;
  		 	  return result;
  		    }
}
/**
 * 将一个数字进行格式化成[-]nnn,nnn.nnnnnn的形式
 * @param {Number}  num   进行处理的值
 * @param {Number}  scale 小数位数
 * @return          处理后的值
 * @type String 
 * @version 1.0
 */
function format_number(num, scale) {
	if (scale == null) { scale = 0; }
	var negative = false;
	if (num < 0) { negative = true; num = -num; }

	var int_part = Math.floor(num);
	var float_part = num - int_part;

	/*
	 * 处理整数部分
	 */
	var int_str = int_part.toString();
	var len = int_str.length;
	if (len > 3) {
		var skip = len % 3;
		var tmp = "";
		if (skip > 0) { tmp = int_str.substr(0, skip); int_str = int_str.substr(skip); len -= skip; }
		while (len >= 3) {
			if (tmp.length > 0) { tmp += ','; }
			tmp += int_str.substr(0, 3);
			int_str = int_str.substr(3);
			len -= 3;
		}
		int_str = tmp;
	}

	if (negative) { int_str = '-' + int_str; }

	/*
	 * 处理小数部分
	 */
	var float_str = "";
	if (scale > 0) {
		float_str += '.';

		for (var i = 0; i < scale; i++) {
			float_part *= 10;
		}
		float_part = Math.round(float_part);
		var tmp = float_part.toString();
		while (tmp.length < scale) {
			tmp = '0' + tmp;
		}
		float_str += tmp;
	}
	return int_str + float_str;
}
/**
 * 格式化数字
 * @param {String}  value   进行处理的数字字符串
 * @param {Sting}   pattern 掩码格式
 * @return          格式化以后的字符串
 * @type String
 * @version 1.0
 */
function format_number_pattern(value,pattern){
   //判断掩码格式，如果格式无效则原样输出
   var value1=value.toString();
  
   if(pattern=="undefined" || pattern==null) return value;
   if(pattern.search(/#/)==-1&&pattern.search(/%/)==-1&&pattern.search(/,/)==-1&&pattern.search(/\./)<=-1 ){
   		return value;
   }
	var outputstr="";
	// 判断pattern中是否带有小数点
	if(pattern.search(/\.(0?|#?)/)!=-1){
		//判断pattern中是否带有"%"
		if(pattern.search(/\.(0*#*|#*0*)%/)!=-1){
			value=parseFloat(value)*100;
			var inp="";
			inp=value.toString();
			value=inp;
		}
		//判断整数部分是否有逗号
		if(pattern.search(/,#?\./)==-1){
			
			if(value1.search(/\./)==-1){
			
				outputstr=value;
		//alert("outputstr=="+outputstr);
			}else{
			outputstr=value.substr(0,inp.indexOf("."));
			}
		}
		if(pattern.search(/,#+\./)!=-1){
			//处理整数部分
			
			var str="";
			var str1="";
			var str2="";
			var str4="";
			var len=0;
			var len1=0;
			str=pattern.match(/,#+\./);
			len=str.lastIndex-str.index-2;

			if(value1.search(/\./)==-1){

				str2=value1;
			}else{

			str1=value1.match(/\d{1,}\./);
			str2=value1.substr(0,str1.lastIndex-1);
			}
			var newstr="";
			var j=0;
			for(var i=1;i<str2.length;i++){
  				if(i%len==0){
 				 newstr=","+str2.substr(str2.length-i,len)+newstr;
 				 j=i;
  				}
  			}
  			newstr=str2.substr(0,str2.length-j)+newstr;

			outputstr=newstr;
			if(value1.search(/\./)==-1 ){

				if(pattern.search(/\.0+/)==-1){
				}else{
				var otherstr="";
				otherstr=pattern.match(/\.0+/);
				outputstr+=pattern.substr(otherstr.index);
				}
				return outputstr;
			}
		}
		//小数部分有“0”的情况
		if(pattern.search(/\.0+/)!=-1){
			var zs="";
			zs=pattern.match(/\.0+/);
			zstr="";
			zstr=pattern.substr(zs.index+1,zs.lastIndex-zs.index-1);
			var str3="";
			str3=value.match(/\.\d{1,}/);
			if(str3!=null){
			str4=value.substr(str3.index+1);
			//处理小数部分取值的位数
			if(str4.length>=zstr.length){
				if(pattern.search(/\.0+#+/)==-1){
				var schar="";
				schar=str4.charAt(zstr.length);
				if(schar<5){
				str4=str4.substr(0,zstr.length);
				}else{
				  str4=parseInt(str4.substr(0,zstr.length))+1;
				}
			        }
			}else{
				var zero;
				zero="";
				for(var i=0;i<zstr.length-str4.length;i++){
					zero+='0';
				}
				str4+=zero;
			}
			outputstr+="."+str4;
		
			}else{
				outputstr="";
				var parm=0;
				parm=parseInt(value);

				str4=parm.toString()+zs;
				outputstr+=str4;
			}
			//小数带有％的情况
			if(pattern.search(/%/)!=-1){
				var pstr="";
				pstr=outputstr+"%";
				return pstr;
			}else{
				return outputstr;
			}
		}else if(pattern.search(/\.#+/)!=-1){
			//小数部分是“＃”的情况
			var sublen=0;
			var sublen1=0;
			if(value1.search(/\./)!=-1){
			sublen=value1.length-value1.indexOf(".")-1;
			sublen1=pattern.length-pattern.indexOf(".")-1;
			outputstr=outputstr+value1.match(/\.\d{1,}/);
			}
			if(pattern.search(/%/)!=-1){

				var pstr="";
				pstr=outputstr+"%";
				return pstr;
			}else{
				return outputstr;
			}
			return outputstr;
		}

	}else if(pattern.search(/,#+/)!=-1){
		//处理分割符
			var str5="";
			var str6="";
			str5=pattern.match(/,#+/);
			str6=pattern.substr(str5.index+1);

			var str7="";
			var str8="";
			str8=value;
			var j=0;
			if(value.search(/\./)!=-1){
  				str8=value.substr(0,value.indexOf("."));
  				if(value.substr(value.indexOf(".")+1,1)>=5){
  					var str9=0;
  					str9=parseInt(str8)+1;
  					str8=str9.toString();
  				}
  			}
  			for(var i=1;i<str8.length;i++){
  				if(i%str6.length==0){
 				 str7=","+str8.substr(str8.length-i,str6.length)+str7;
 				 j=i;
  				}
  			}


		var str12="";
		str12=str8.substr(0,str8.length-j)+str7;
		return str12;
	}else{
		//整数情况
		if(value.search(/\./)!=-1){

			var str10="";
			if(pattern.search(/%/)!=-1){
				//带有“％”的情况
				str10=parseFloat(value)*100+"%";
			}else{
			str10=value.substr(0,value.indexOf("."));
			if(value.substr(value.indexOf(".")+1,1)>=5){
				var str11=0;
				str11=parseInt(str10)+1;
				str10=str11.toString();
			}

			}
		return str10;
		}else{
		return value;
		}
	}
	return value;

}
/**
 * 将一个字符串反格式化成一个数字
 * @param {String}  str 进行处理的字符串
 * @return          反格式化得到的数字
 * @type Number
 * @version 1.0
 * 注意：如果该字符串不是合法的数字，可能返回NaN
 */
function unformat_number(str) {
	if (str == "" || str == null) {
		return 0;
	} else {
		if(str.indexOf("%")==-1)
			return new Number(str.replace(/,/g,""));
		//处理带％的情况
		str = str.replace(/%/,"")
		return new Number( str.replace(/,/g,"") )/100;
	}
}

/**
 * 返回指定对象所在的Form对象，如果Form对象不存在，返回Body对象
 * @param {Object}  obj 指定的对象
 * @return          指定对象所在的Form对象或Body对象
 * @type Object 
 * @version 1.0
 * 注意：如果不指定对象，就使用当前事件源
 */
function get_parent_form(obj) {
	if (obj == null) { if(event != null) obj = event.srcElement; }
	if (obj == null) {
		return document.body;
	} else if (obj.form == null) {
		return document.body;
	} else {
		return obj.form;
	}
}
/**
 * 根据名称获取控件
 * @param{String}	name 需要获取的控件名称
 * @return	         如果该控件不存在，返回null，如果存在同名多个控件，返回控件数组，否则返回该控件对象
 * @type Object
 * @version 1.0
 */
function get_control(name) {
	var obj = document.getElementsByName(name);
	if (obj.length == 0) {
		return null;
	} else if (obj.length == 1) {
		return obj[0];
	} else {
		return obj;
	}
}

/**
 * 比较两个日期，返回两者之间相差的天数
 * @param {String}  dt1 比较日期
 * @param {String}  dt2 比较日期
 * @return          两个日期之间相差的天数
 * @type Number 
 * @version 1.0
 */
function diff_date(dt1, dt2) {
	dt1 = dt1.replace(/-/g, '');
	dt2 = dt2.replace(/-/g, '');

	var year1 = dt1.substring(0,4);
	var month1 = dt1.substring(4,6);
	var day1 = dt1.substring(6,8);
	var Date1 = new MyDate(year1, month1, day1);

	var year2 = dt2.substring(0,4);
	var month2 = dt2.substring(4,6);
	var day2 = dt2.substring(6,8);
	var Date2 = new MyDate(year2, month2, day2);

	return Date2.getDays() - Date1.getDays();
}

/**
 * 在一个日期上增加若干日子，返回新的日期
 * @param {String}  dt   进行处理的日期
 * @param {Number}  days 增加的天数
 * @return          处理后的日期
 * @type String
 * @version 1.0
 */
function add_date(dt, days) {
	dt = dt.replace(/-/g, '');
	var year = dt.substring(0,4);
	var month = dt.substring(4,6);
	var day = dt.substring(6,8);
	var date1 = new MyDate(year, month, day);
	date1.addDays(days);
	return date1.toString();
}
/**
 * 根据年份获取这个年的天数
 * @param {Number}   y 查询的年份
 * @return           查询年份的天数
 * @type Number
 * @version 1.0
 */
function days_of_year(y) {
	if (((y%4 == 0) && (y%100 != 0) || (y%400 == 0))) {
		return 366;
	} else {
		return 365;
	}
}
function _return_false(){
	return false;
}
/**
 * IE提供的Date对象有问题，譬如以下两个日期在IE的Date对象中
 * 被认为是一样的:
 * 		d1 = new Date(1999, 08, 31);
 * 		d2 = new Date(1999, 09, 01);
 * 自定义的日期对象
 * @param {Number}  year  年份
 * @param {Number}  month 月份
 * @param {Number}  day   天
 * @version 1.0
 */
function MyDate(year, month, day) {
	var today = new Date();
	if (year == null) {
		this.year = today.getYear();
	} else {
		this.year = new Number(year);
	}

	if (month == null) {
		this.month = today.getMonth() + 1;
	} else {
		this.month = new Number(month);
	}

	if (day == null) {
		this.day = today.getDate();
	} else {
		this.day = new Number(day);
	}

	this.getYear = _date_get_year;
	this.getMonth = _date_get_month;
	this.getDay = _date_get_day;
	this.getDays = _date_get_days;
	this.addDays = _date_add_days;
	this.toString = _date_tostring;
}
/**
 * 根据年份和月份获取这个月的天数
 * @version 1.0
 */
function _date_get_month() {
	return this.month;
}
/**
 * 获取天数
 * @version 1.0
 */
function _date_get_day() {
	return this.day;
}
/**
 * 获取年份
 * @version 1.0
 */
function _date_get_year() {
	return this.year;
}
/**
 * 获取某年的天数
 * @version 1.0
 */
function _date_get_days() {
	var days = 0;
	for (i = 1900; i < this.year; i++) {
		days += days_of_year(i);
	}

	for (i = 1; i < this.month; i++) {
		days += days_of_month(this.year, i);
	}

	days += this.day;
	return days;
}
/**
 * 在一个日期上增加若干日子，返回新的日期
 * @param {Number}  daysAdd 增加的天数
 * @return          增加后的日期
 * @type String
 * @version 1.0
 */
function _date_add_days(daysAdd) {
	var days = 0;

	days = this.getDays();
	days += new Number(daysAdd);

	this.year = 1900;
	while (days > 0) {
		days -= days_of_year(this.year);
		this.year++;
	}
	this.year--;
	days += days_of_year(this.year);

	this.month = 1;
	while (days > 0) {
		days -= days_of_month(this.year, this.month);
		this.month++;
	}
	this.month--;
	days += days_of_month(this.year, this.month);

	this.day = days;
	return this;
}
/**
 * 把日期转化成字符型
 */
function _date_tostring() {
	var str;

	str = this.year.toString();

	str += '-';
	if (this.month < 10) {
		str += "0" + this.month;
	} else {
		str += this.month;
	}

	str += '-';
	if (this.day < 10) {
		str += "0" + this.day;
	} else {
		str += this.day;
	}

	return str;
}
/**
 * 目标定位，取指定的控件对象在当前窗口上的X坐标
 * @param {Object}  obj 指定的对象
 * @return          指定对象在当前窗口的X坐标
 * @type Number
 * @version 1.0
 */
function get_object_left(obj) {
	var left   = obj.offsetLeft;

	var parent = obj.offsetParent;
	if (parent != null) {
		while (parent.tagName != "BODY") {
			left  += parent.offsetLeft;
			parent = parent.offsetParent;
		}
	}
	return left;
}
/**
 * 取指定的控件对象在当前窗口上的Y坐标
 * @param {Object}  obj 指定的对象
 * @return          指定对象在当前窗口的Y坐标
 * @type Number
 * @version 1.0
 */
function get_object_top(obj) {
	var top    = obj.offsetTop;

	var parent = obj.offsetParent;
	if (parent != null) {
		while (parent.tagName != "BODY") {
			top   += parent.offsetTop;
			parent = parent.offsetParent;
		}
	}
	return top;
}
/**
 * 判断两个字符串或字符串数组是否相同
 * @param {Object}  a 字符串或数组
 * @param {Object}  b 字符串或数组
 * @return          如果相同返回true，否则返回false
 * @type Boolean
 * @version 1.0
 */
function _string_array_equals(a, b) {
	if (is_string(a) && is_string(b)) {
		return a == b;
	} else if (is_array(a) && is_array(b)) {
		if (a.length != b.length) { return false; }
		for (var i = 0; i < a.length; i++) {
			if (a[i] != b[i]) { return false; }
		}
		return true;
	} else {
		return false;
	}
}
/**
 * 身份证号严格验证
 * @param {String}  sId 身份证号
 * @return          如果身份证号合法返回身份证信息，否则返回错误信息
 * @type String
 * @version 1.0
 */ 
var aCity={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外"} 
function cidInfo(sId){ 
	var iSum=0 
	var info="" 
	if(!/^\d{17}(\d|x)$/i.test(sId))return false; 
	sId=sId.replace(/x$/i,"a"); 
	if(aCity[parseInt(sId.substr(0,2))]==null)return "Error:非法地区"; 
	sBirthday=sId.substr(6,4)+"-"+Number(sId.substr(10,2))+"-"+Number(sId.substr(12,2)); 
	var d=new Date(sBirthday.replace(/-/g,"/")) 
	if(sBirthday!=(d.getFullYear()+"-"+ (d.getMonth()+1) + "-" + d.getDate()))return "Error:非法生日"; 
	for(var i = 17;i>=0;i --) iSum += (Math.pow(2,i) % 11) * parseInt(sId.charAt(17 - i),11) 
	if(iSum%11!=1)return "Error:非法证号"; 
	return aCity[parseInt(sId.substr(0,2))]+","+sBirthday+","+(sId.substr(16,1)%2?"男":"女") 
} 
/**
 * 校验是否全由数字组成
 * @param {String}  s 校验的字符串
 * @return          如果全部是数字返回true,否则返回false
 * @type Boolean
 * @version 1.0
 */ 
function isDigit(s){
	var patrn=/^[0-9]{1,20}$/;
	if (!patrn.exec(s)) return false
	return true
}
/**
 * 数值格式化
 * @param {String}  number 进行处理的数值
 * @return          处理过的字符串
 * @type String
 * @version 1.0
 */
function number_format(number){
         if(/[^0-9\.\-]/.test(number)) return "invalid value";
         number=number.replace(/^(\d*)$/,"$1.");
         number=(number+"00").replace(/(\d*\.\d\d)\d*/,"$1");
         number=number.replace(".",",");
        var re=/(\d)(\d{3},)/;
        while(re.test(number))
                number=number.replace(re,"$1,$2");
        number=number.replace(/,(\d\d)$/,".$1");
       return number.replace(/^\./,"0.")
 }
/**
 * 小数格式化
 * @param {String}  srcStr 进行格式化的字符串
 * @return          格式化后的字符串
 * @type String
 * @version 1.0
 */        
function FormatNumber(srcStr,nAfterDot){
　　        var srcStr,nAfterDot;
　　        var resultStr,nTen;
　　        srcStr = ""+srcStr+"";
　　        strLen = srcStr.length;
　　        dotPos = srcStr.indexOf(".",0);
　　        if (dotPos == -1){
　　　　        resultStr = srcStr+".";
　　　　        for (i=0;i<nAfterDot;i++){
　　　　　　        resultStr = resultStr+"0";
　　　　        }
　　　　        return resultStr;
　　        }
　　        else{
　　　　        if ((strLen - dotPos - 1) >= nAfterDot){
　　　　　　        nAfter = dotPos + nAfterDot + 1;
　　　　　　        nTen =1;
　　　　　　        for(j=0;j<nAfterDot;j++){
　　　　　　　　        nTen = nTen*10;
　　　　　　        }
　　　　　　        resultStr = Math.round(parseFloat(srcStr)*nTen)/nTen;
　　　　　　        return resultStr;
　　　　        }
　　　　        else{
　　　　　　        resultStr = srcStr;
　　　　　　        for (i=0;i<(nAfterDot - strLen + dotPos + 1);i++){
　　　　　　　　        resultStr = resultStr+"0";
　　　　　　        }
　　　　　　        return resultStr;
　　　　        }
　　        }
  } 
/**
 * 数字格式化函数
 * @param {Number}   num 要进行格式化的数值型变量
 * @param {Number}   decimalNum（整型） ：要保留的小数位数
 * @param {Boolean}  bolLeadingZero   ：对-1<num<1之间的数是否要添加前导数字0；
 * @param {Boolean}  bolParens        ：在负号后是否使用小括号
 * @param {Boolean}  bolCommas        ：是否用“,”隔开数字位。
 * @return           经过格式化的字符串
 * @type String
 * @version 1.0
 */
 /*
 * 例子：alert(FormatNumber(1234567.8,2,false,false,true))
 */
function FormatNumber(num,decimalNum,bolLeadingZero,bolParens,bolCommas){
	if (isNaN(parseInt(num))) return "NaN";
	var tmpNum = num;
	var iSign = num < 0 ? -1 : 1; // 得到数字符号
	// 调整小数点位数到给定的数字。
	tmpNum *= Math.pow(10,decimalNum);
	tmpNum = Math.round(Math.abs(tmpNum))
	tmpNum /= Math.pow(10,decimalNum);
	tmpNum *= iSign; // 重新调整符号
	var tmpNumStr = new String(tmpNum);
	// 处理是否有前导0
	if (!bolLeadingZero && num < 1 && num > -1 && num != 0)
	if (num > 0)
	    tmpNumStr = tmpNumStr.substring(1,tmpNumStr.length);
	else
	    tmpNumStr = "-" + tmpNumStr.substring(2,tmpNumStr.length);
	//处理是否有逗号
	if (bolCommas && (num >= 1000 || num <= -1000)) {
		var iStart = tmpNumStr.indexOf(".");
		if (iStart < 0)iStart = tmpNumStr.length;
		iStart -= 3;
		while (iStart >= 1){
			tmpNumStr = tmpNumStr.substring(0,iStart) + "," + tmpNumStr.substring(iStart,tmpNumStr.length);
			iStart -= 3;
		}
	}
	// 处理是否有括号
	if (bolParens && num < 0)tmpNumStr = "(" + tmpNumStr.substring(1,tmpNumStr.length) + ")";
	return tmpNumStr; // 返回格式化后数字字符串
}
/**
 * 取得控件的绝对位置
 * @param {Object}  obj 指定的控件对象
 * @return          指定控件在当前窗口的坐标
 * @type Array
 * @version 1.0
 */ 
function getoffset(obj){  
	 var t=obj.offsetTop;  
	 var l=obj.offsetLeft;  
	 while(obj=obj.offsetParent) {  
	    t+=obj.offsetTop;  
	    l+=obj.offsetLeft;  
	 }  
	 var rec = new Array(1); 
	 rec[0]  = t; 
	 rec[1] = l; 
	 return rec;
} 
/**
 * 光标停在文字最后
 */
function lastFocus(){ 
	 var obj = event.srcElement; 
	 var r =obj.createTextRange(); 
	 r.moveStart('character',obj.value.length); 
	 r.collapse(true); 
	 r.select(); 
}
