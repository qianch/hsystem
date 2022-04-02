/**
 * JS日历封装，操作时间
 * 解决JS Date的各种奇葩问题
 * @author 高飞  252878950@qq.com 
 * 主页:http://www.xdemo.org/calendar-js/
 */
(function(target) {


	/**
	 * Calendar构造方法
	 * 无参:默认new Date()
	 * 一个参数:数字类型，等于new Date(数字);
	 * 2-6个参数:必须都是数字类型，分别对应 年、月、日、时、分、秒
	 */
	var Calendar = function() {
		switch (arguments.length) {
			case 0:
				this._date_ = new Date()
				return this;
			case 1:
				if (typeof arguments[0] == 'string') {
					this._date_ = new Date(Date.parse(arguments[0]));
				}
				if (typeof arguments[0] == 'number') {
					this._date_ = new Date(arguments[0]);
				}
				if(arguments[0] instanceof Date){
					this._date_=arguments[0];
				}
				return this;
			default:
				this._date_ = new Date(arguments[0] || 1970, (arguments[1] - 1) || 0, arguments[2] || 1, arguments[3] || 0, arguments[4] || 0, arguments[5] || 0);
				return this;
		}
	}

	Calendar.field = {
		/**
		 * 年
		 */
		YEAR : 1,
		/**
		 * 月 
		 */
		MONTH : 2,
		/**
		 * 日 
		 */
		DAY_OF_MONTH : 3,
		/**
		 * 时 
		 */
		HOUR : 4,
		/**
		 * 分 
		 */
		MINUTE : 5,
		/**
		 * 秒 
		 */
		SECOND : 6,
		
		/**
		 * 天数 
		 */
		DAY:7,
		/**
		 * 周数
		 */
		WEEK:8
	};
	
	
	/**
	 * 判断是否是闰年
	 * @param {Object} year
	 */
	Calendar.isLeapYear=function(year){
		return (year % 400 == 0) || (year % 4 == 0 && year % 100 != 0);
	}
	
	/**
	 * 判断Date1是否在Date2之前 
	 * @param {Object} date1
	 * @param {Object} date2
	 */
	Calendar.isBefore=function(date1,date2){
		return date1.getTime()>date2.getTime();
	}
	
	/**
	 * 获取指定年份，月份的最后一天 
	 * @param {Object} year
	 * @param {Object} month
	 */
	Calendar.lastDayOfMonth=function(year,month){
		return [31, null, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31][month-1] || (((year % 400 == 0) || (year % 4 == 0 && year % 100 != 0)) ? 29 : 28);
	}
	
	/**
	 * 获取两个日期时间差 
	 * @param {Object} date1
	 * @param {Object} date2
	 * @param {Object} field Calendar.field
	 */
	Calendar.diff=function(date1,date2,field){
		if(date1.getTime()>date2.getTime()){
			throw new Error("第一个日期参数必须在第二个日期之前");
			return;
		}
		switch (field) {
				case 1://年
					var year=date2.getFullYear()-date1.getFullYear();
					if(date1.getMonth()>date2.getMonth()){
						year-=1;
					}else{
						if(date1.getDate()>date2.getDate()){
							year-=1;
						}else{
							if(date1.getHours()>date2.getHours()){
								year-=1;
							}else{
								if(date1.getMinutes()>date2.getMinutes()){
									year-=1;
								}else{
									if(date1.getSeconds()>date2.getSeconds()){
										year-=1;
									}
								}
							}
						}
					}
					return year;
				case 2://月
					var year=this.diff(date1,date2,Calendar.field.YEAR);
					var month=date2.getMonth()-date1.getMonth();
					if(date1.getDate()>date2.getDate()){
							month-=1;
						}else{
							if(date1.getHours()>date2.getHours()){
								month-=1;
							}else{
								if(date1.getMinutes()>date2.getMinutes()){
									month-=1;
								}else{
									if(date1.getSeconds()>date2.getSeconds()){
										month-=1;
									}
								}
							}
						}
					return year*12+month;
				case 4://时
					return Math.floor((date2.getTime()-date1.getTime())/(60*60*1000));
				case 5://分
					return Math.floor((date2.getTime()-date1.getTime())/(60*1000));
				case 6:
					return Math.floor((date2.getTime()-date1.getTime())/(1000));
				case 7://天
					return Math.floor((date2.getTime()-date1.getTime())/(24*60*60*1000));
				case 8://周数
					return Math.floor(Math.floor((date2.getTime()-date1.getTime())/(24*60*60*1000))/7);
				default:
					throw new Error("unknown Calendar field");
			}
	}

	Calendar.prototype = {

		/**
		 * 当前时间
		 */
		now: function() {
			this._date_=new Date();
			return this;
		},
		/**
		 * 时间操作，在时间的某个字段上加上一定的数字（正负数）
		 * @param {Object} field
		 * @param {Object} number
		 */
		add: function(field, number) {
			switch (field) {
				case 1:
					this._date_ = new Date(this._date_.getFullYear() + number, this._date_.getMonth(), this._date_.getDate(), this._date_.getHours(), this._date_.getMinutes(), this._date_.getSeconds());
					return this;
				case 2:
					this._date_ = new Date(this._date_.getFullYear(), this._date_.getMonth() + number, this._date_.getDate(), this._date_.getHours(), this._date_.getMinutes(), this._date_.getSeconds());
					return this;
				case 3:
					this._date_ = new Date(this._date_.getFullYear(), this._date_.getMonth(), this._date_.getDate() + number, this._date_.getHours(), this._date_.getMinutes(), this._date_.getSeconds());
					return this;
				case 4:
					this._date_ = new Date(this._date_.getTime() + (3600000 * number));
					return this;
				case 5:
					this._date_ = new Date(this._date_.getTime() + (60000 * number));
					return this;
				case 6:
					this._date_ = new Date(this._date_.getTime() + (1000 * number));
					return this;
				default:
					throw new Error("unknown Calendar field");
			}
		},
		/**
		 * 设置时间 第一个参数 年|月|日|时|分|秒 之一
		 * @param {Object} field 
		 * 		Calendar.field.YEAR
		 * 		Calendar.field.MONTH
		 *  	Calendar.field.DAY_OF_MONTH
		 *  	Calendar.field.HOUR
		 *  	Calendar.field.MINUTE
		 *  	Calendar.field.SECOND
		 * @param {Object} number
		 */
		set: function(field, number) {
			switch (field) {
				case 1:
					this._date_ = new Date(number, this._date_.getMonth(), this._date_.getDate(), this._date_.getHours(), this._date_.getMinutes(), this._date_.getSeconds());
					return this;
				case 2:
					this._date_ = new Date(this._date_.getFullYear(), number-1, this._date_.getDate(), this._date_.getHours(), this._date_.getMinutes(), this._date_.getSeconds());
					return this;
				case 3:
					this._date_ = new Date(this._date_.getFullYear(), this._date_.getMonth(), number, this._date_.getHours(), this._date_.getMinutes(), this._date_.getSeconds());
					return this;
				case 4:
					this._date_ = new Date(this._date_.getFullYear(), this._date_.getMonth(), this._date_.getDate(), number, this._date_.getMinutes(), this._date_.getSeconds());
					return this;
				case 5:
					this._date_ = new Date(this._date_.getFullYear(), this._date_.getMonth(), this._date_.getDate(), this._date_.getHours(), number, this._date_.getSeconds());
					return this;
				case 6:
					this._date_ = new Date(this._date_.getFullYear(), this._date_.getMonth(), this._date_.getDate(), this._date_.getHours(), this._date_.getMinutes(), number);
					return this;
				default:
					throw new Error("unknown Calendar field");
			}
		},
		/**
		 * GET
		 * @param {Object} field
		 */
		get: function(field) {
			switch (field) {
				case 1:
					return this._date_.getFullYear();
				case 2:
					return this._date_.getMonth()+1;
				case 3:
					return this._date_.getDate();
				case 4:
					return this._date_.getHours();
				case 5:
					return this._date_.getMinutes();
				case 6:
					return this._date_.getSeconds();
				default:
					throw new Error("unknown Calendar field");
			}
		},
		/**
		 * 是否是闰年
		 */
		isLeapYear: function() {
			var year = this._date_.getFullYear();
			return (year % 400 == 0) || (year % 4 == 0 && year % 100 != 0);
		},
		/**
		 * 获取一年的第多少周
		 */
		weekOfYear: function() {
			var now = this._date_,
				year = now.getFullYear(),
				month = now.getMonth(),
				days = now.getDate();

			//那一天是那一年中的第多少天
			for (var i = 0; i < month; i++) {
				days += this.getDaysOfMonth(year, i);
			}

			//那一年第一天是星期几
			var yearFirstDay = new Date(year, 0, 1).getDay() || 7;

			var week = null;
			if (yearFirstDay == 1) {
				week = Math.ceil(days / yearFirstDay);
			} else {
				days -= (7 - yearFirstDay + 1);
				week = Math.ceil(days / 7) + 1;
			}

			return week;
		},
		/**
		 * 格式化日期
		 * @param {Object} format y{年} M{月} d{日} H{24时} h{12时} m{分} s{秒} S{毫秒数} q{季度}
		 */
		format: function(format) {
			var o = {
				"M+": this._date_.getMonth() + 1, //月份 
				"d+": this._date_.getDate(), //日 
				"H+": this._date_.getHours(), //24小时 
				"h+": this._date_.getHours() % 12 == 0 ? 12 : this._date_.getHours() % 12, //12小时
				"m+": this._date_.getMinutes(), //分 
				"s+": this._date_.getSeconds(), //秒 
				"q+": Math.floor((this.getMonth() + 3) / 3), //季度 
				"S": this._date_.getMilliseconds() //毫秒 
			};
			if (/(y+)/.test(format)) format = format.replace(RegExp.$1, (this._date_.getFullYear() + "").substr(4 - RegExp.$1.length));
			for (var k in o)
				if (new RegExp("(" + k + ")").test(format)) format = format.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
			return format;
		},
		/**
		 * 设置时间
		 * @param {Object} date JS Date类型或者数字类型
		 */
		setTime: function(date) {
			if (date instanceof Date) {
				this._date_ = date;;
			}
			if (typeof date == 'number') {
				this._date_ = new Date(date);
			}
			return this;
		},
		/**
		 * 获取时间，返回Date类型
		 */
		getTime: function() {
			return this._date_;
		},
		/**
		 * 获取时间，返回Long类型
		 */
		getTimeLong: function() {
			return this._date_.getTime();
		},
		/**
		 * 获取对应月份的天数
		 * @param {Object} year
		 * @param {Object} month
		 */
		getDaysOfMonth: function() {
			var year = this._date_.getFullYear();
			var month = this._date_.getMonth();
			return [31, null, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31][month] || (this.isLeapYear(year) ? 29 : 28);
		},
		/**
		 * 获取年份
		 */
		getYear: function() {
			return this._date_.getFullYear();
		},
		/**
		 * 获取月份
		 */
		getMonth: function() {
			return this._date_.getMonth() + 1;
		},
		/**
		 * 获取日期
		 */
		getDayOfMonth: function() {
			return this._date_.getDate();
		},
		/**
		 * 获取小时数
		 */
		getHours: function() {
			return this._date_.getHours();
		},
		/**
		 * 获取分钟数 
		 */
		getMinutes: function() {
			return this._date_.getMinutes();
		},
		/**
		 * 获取秒数
		 */
		getSeconds: function() {
			return this._date_.getSeconds();
		},
		/**
		 * 获取毫秒数
		 */
		getMilliseconds: function() {
			return this._date_.getTime();
		},
		/**
		 * 转字符串
		 */
		toString: function() {
			return this.format("yyyy-MM-dd HH:mm:ss");
		}
	};
	target.Calendar = Calendar;
})(window);