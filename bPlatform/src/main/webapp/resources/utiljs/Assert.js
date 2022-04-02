var Assert = Assert = Assert || {};

/**
 * 判断对象是否为空
 * null		->	true
 * ""		->	true
 * " "		->	true
 * undefined->	true
 * @param {Object} obj
 */
Assert.isEmpty=function(obj){
	if(obj==null||obj==undefined)return true;
	if(!obj.replace(new RegExp(" ","gm"),"").replace(new RegExp("	","gm"),""))return true;
	return false;
}

/**
 * 判断是否是数字,数字类型或者数字字符串
 * @param {Object} str
 */
Assert.isNumber=function(val){
	if(+val===val)return true;
	if(typeof val=='string'){
		if(val.indexOf("-")==0)val=val.replace("-","");
		return val.match(/\d*/i)==val;
	}
	return false;
}

/**
 * 判断是否是数字,数字类型或者数字字符串
 * @param {Object} str
 */
Assert.isFloat=function(val){
	if(+val===val)return true;
	if(typeof val=='string'){
		if(val.indexOf("-")==0)val=val.replace("-","");
		return /^\d+(\.\d+)?$/.test(val);
	}
	return false;
}

/**
 * 判断对象是否是数组
 */
Assert.isArray=function(value){
	return value&&value.constructor == Array;
}

/**
 * 判断对象是否是某个类型
 * @param {Object} value
 * @param {Object} type
 */
Assert.isTypeOf=function(value,type){
	return (typeof value==type);
}

/**
 * 判断对象是否是某个类型的实例
 * @param {Object} value
 * @param {Object} type
 */
Assert.isInstanceOf=function(value,type){
	return value instanceof type;
}






