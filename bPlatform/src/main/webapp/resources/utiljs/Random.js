/**
 * 随机数操作
 * Random.js
 * @author 高飞  252878950@qq.com 
 * 主页:http://www.xdemo.org/random-js/
 */
var Random = Random = Random || {};

/**
 * 随机数-整数
 * @param {Object} min 最小值
 * @param {Object} max 最大值
 */
Random.int=function(min,max){
	//因为floor是向下取整，所以max+1才能取到Max的最大值
	return Math.floor(min+Math.random()*((max+1)-min));
}

/**
 * 随机数-浮点数
 * @param {Object} min 最小值
 * @param {Object} max 最大值
 * @param {Object} precision 精度
 */
Random.float=function(min,max,precision){
	return new Number(min+Math.random()*(max-min)).toFixed(precision);
}

/**
 * 生成随机字符串
 * @param {Object} length 长度
 * @param {Object} containNumber 是否包含数字
 * @param {Object} upperCase 转大写？
 */
Random.string=function(length,containNumber,upperCase){
	var chars=['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'];
	var target=[];
	var min=0,max=61;
	
	if(!containNumber)min=10;
	for(var i=0;i<length;i++){
		target[i]=chars[Math.floor(min+Math.random()*((max+1)-min))];
	}
	
	return upperCase?target.join("").toUpperCase():target.join("");
}