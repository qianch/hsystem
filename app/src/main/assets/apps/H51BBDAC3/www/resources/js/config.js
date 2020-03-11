//var SERVER_IP="10.10.1.50/mes";//测试服务器
var SERVER_IP="10.10.1.9/mes";//生产环境
//var SERVER_IP="192.168.101.18:8080/mes";//个人测试
//var SERVER_IP="192.168.1.111/mes";//个人测试
var Config={
	/**
	 * 服务器地址  
	 */
	serverUrl:"http://"+SERVER_IP+"/mobile/",
	/**
	 * 项目配置
	 */
	project:{
		/**
		 * APP名称
		 */
		name:"恒石MES PDA客户端",
		/**
		 * APP当前版本号
		 */

		version:"4.7.5"
	},
	/**
	 * 动画定义
	 */
	animation:{
		/**
		 * 动画切换速度
		 */
		speed:200,
		/**
		 * 动画效果
		 */
		type:"zoom-out"//slide-in-right,slide-in-left,slide-in-top,slide-in-bottom,fade-in,zoom-out,pop-in
	},
	/**
	 * 超时时间,单位秒
	 */
	EXPIRE_TIME:30*60//30分钟
	
}
