$(function(){
	var pre_node = $('.DoubleDateBox');
	pre_node.each(function(){
		var start = $(this).find('.start');
		var end = $(this).find('.end');
		if(start.length == 1&&end.length==1){
			new DateBoxDiv($(this)).init();
		}
	});
});
function DateBox(pre_node,outer,sequence){
	/**
	 * date_bobox 文本框的jquery对象
	 */
	this.pre_node = pre_node;
	/**
	 * DateBox 所属的对象
	 * @type 
	 */
	this.outer = outer;
	this.sequence = sequence;
	/**
	 * 模板对象
	 */
	this.template = 0;
	/**
	 * 日期显示框对象
	 */
	this.text = 0;
	/**
	 * date图标对象
	 */
	this.dateIcon = 0;
	/**
	 * DateBox关联的日历对象
	 */
	this.calendar = 0;
	/**
	 * 取得DateBox模板
	 */
	this.getTemplate = function(){
		if(this.template){
			return this.template;
		}else{
			return getDateBoxTemplate();
		}
	};
	/**
	 * 载入模板，并处理pre_node节点
	 */
	this.load = function(){
		this.pre_node.after(this.template);
		this.pre_node.css("display","none");	
	};
	/**
	 * 初始化DateBox
	 */
	this.init = function(){
		this.template = this.getTemplate();
		this.dateIcon = this.template.find(".combo-arrow");
		this.text = this.template.find("input[type='text']");
		this.addEvent(this);
		this.load();
	};
}
DateBox.prototype.addEvent = function(dateBox){
	dateBox.dateIcon.hover(
          function () {
            $(this).addClass("combo-arrow-hover");
           },
           function () {
            $(this).removeClass("combo-arrow-hover");
           }
	);
	dateBox.dateIcon.click(function(){
       if(dateBox.calendar){
        	var other_left = dateBox.outer.left;
        	var this_left = dateBox.template.offset().left;
        	if(this_left != other_left){
        		dateBox.text.focus();
        		//dateBox.outer.show(dateBox);
        	}else{
	        	if(dateBox.outer.isShow){
	        		dateBox.outer.hide(dateBox);
	        	}else{
	        		//dateBox.outer.show(dateBox);
	        		dateBox.text.focus();
	        	}
        	}
        }else{
        	dateBox.outer.date_box_start.calendar = new Calendar(dateBox.outer.date_box_start);
        	dateBox.outer.date_box_end.calendar = new Calendar(dateBox.outer.date_box_end);
        	dateBox.outer.date_box_start.calendar.init();
        	dateBox.outer.date_box_end.calendar.init();
        	dateBox.outer.date_box_start.calendar.template.appendTo(dateBox.outer.start);
			dateBox.outer.date_box_end.calendar.template.appendTo(dateBox.outer.end);
        	dateBox.outer.addCalendar(dateBox.outer);
        	if(dateBox.outer.isShow){
        		dateBox.outer.hide(dateBox);
        	}else{
        		dateBox.outer.show(dateBox);
        	}
        	
        }
		/*if(dateBox.outer.isShow){
    		dateBox.outer.hide(dateBox);
	        	
    	}else{
    		dateBox.text.focus();
    	}*/
		
		/*var other_left = dateBox.outer.left;
    	var this_left = dateBox.template.offset().left;
    	if(this_left != other_left){
    		dateBox.text.focus();
    	}else{
        	//dateBox.outer.
    	}*/
    });
	    
};
/**
 * 
 * @param {} super_node
 */
function DateBoxDiv(super_node){
	/**
	 * 这是页面div节点对象
	 */
	this.super_node = super_node;
	/**
	 * div状态 true为显示，false为隐藏
	 * @type Boolean
	 */
	this.isShow = false;
	/**
	 * 确定按钮
	 */
	this.ok = 0;
	/**
	 * 关闭按钮
	 */
	this.close = 0;
	/**
	 * 开始时间
	 */
	this.start_time = new Date();
	/**
	 * 结束时间
	 */
	this.end_time = new Date();
	/**
	 * 上边距
	 * @type Number
	 */
	this.top = 0;
	/**
	 * 下边距
	 * @type Number
	 */
	this.left = 0;
	/**
	 * 模板的td 中的span对象
	 */
	this.start = 0;
	/**
	 * 模板td 中的span对象
	 */
	this.end = 0;
	/**
	 * DateBox(开始)的对象
	 */
	this.date_box_start = 0;
	/**
	 * DateBox(结束)的对象
	 */
	this.date_box_end = 0;
	/**
	 * 记录选择的开始日期
	 */
	this.date_box_start_time = new Date();
	/**
	 * 整个div的模板
	 */
	this.template = 0;
	
	this.getTemplate = function (){
		if(this.template){
			return this.template;
		}else{
			return getDateBoxDivTemplate();
		}
	};
	this.getTop = function(this_datebox){
		this.top = this_datebox.template.offset().top+this_datebox.template.height() + 5;
		return this.top;
	};
	this.getLeft = function(this_datebox){
		this.left = this_datebox.template.offset().left;
		return this.left;
	};
	this.load = function(){
		this.template.appendTo(this.super_node);
	};
	this.init = function(){
		this.template = this.getTemplate();
		this.start = this.template.find('.the-start');
		this.end = this.template.find('.the-end');
		this.ok = this.template.find('.datebox-ok');
		this.close = this.template.find('.datebox-close');
		this.date_box_start = new DateBox(this.super_node.find('.start'),this,0);
		this.date_box_end = new DateBox(this.super_node.find('.end'),this,1);
		this.date_box_start.init();
		this.date_box_end.init();
		this.addEvent(this);
		this.load();
	};
	this.setPosition = function(this_datebox){
		this.template.css({
			top:this.getTop(this_datebox),
			left:this.getLeft(this_datebox)
		});
	};
	this.show = function(this_datebox){
		if(this_datebox){
			this.setPosition(this_datebox);
		}
		this.isShow = true;
		this.template.show();
	};
	this.hide = function(this_datebox){
		if(this_datebox){
			this.setPosition(this_datebox);
		}
		this.isShow = false;
		this.template.hide();
	};
}
DateBoxDiv.prototype.addCalendar = function(dateBoxDiv){
	if(dateBoxDiv.date_box_start.calendar ==0){
		dateBoxDiv.date_box_start.calendar = new Calendar(dateBoxDiv.date_box_start);
		dateBoxDiv.date_box_end.calendar = new Calendar(dateBoxDiv.date_box_end);
		dateBoxDiv.date_box_start.calendar.init();
		dateBoxDiv.date_box_end.calendar.init();
		dateBoxDiv.date_box_start.calendar.template.appendTo(dateBoxDiv.start);
		dateBoxDiv.date_box_end.calendar.template.appendTo(dateBoxDiv.end);
	}
};
DateBoxDiv.prototype.addEvent = function(dateBoxDiv){
	this.close.hover(
	          function () {
	            $(this).addClass("datebox-button-hover");
	           },
	           function () {
	            $(this).removeClass("datebox-button-hover");
	           }
	);
	this.ok.hover(
	          function () {
	            $(this).addClass("datebox-button-hover");
	           },
	           function () {
	            $(this).removeClass("datebox-button-hover");
	           }
	);
	this.ok.click(function(){
		dateBoxDiv.hide();
	});
	this.close.click(function(){
		dateBoxDiv.hide();
	});
	this.date_box_start.text.focus(function(){
		if(dateBoxDiv.date_box_start.calendar ==0){
			dateBoxDiv.addCalendar(dateBoxDiv);
		}
		dateBoxDiv.show(dateBoxDiv.date_box_start);
	});
	this.date_box_end.text.focus(function(){
		if(dateBoxDiv.date_box_end.calendar ==0){
			dateBoxDiv.addCalendar(dateBoxDiv);
		}
		dateBoxDiv.show(dateBoxDiv.date_box_end);
	});
	
	$(document).click(function(){
		dateBoxDiv.hide();
	});
	this.super_node.click(function(){
		return false;
	});
};
/**
 * 日历对象，封装了日历的实现
 * @param pre_node 日历的前节点
 * @returns {calendar}
 */
function Calendar(pre_node){
	/**
	 * 上边距
	 */
	this.top = 0;
	/**
	 * 左边距
	 */
	this.left = 0;
	/**
	 * 日历状态：true为显示，false为隐藏，默认为false
	 */
	this.isShow = false;
	/**
	 * 日历的前节点,在这里是DateBox对象
	 */
	this.pre_node = pre_node;
	/**
	 * 当前日期 Date类型
	 */
	this.current_date = new Date();
	/**
	 * 今天按钮的对象
	 */
	this.today = 0;
	/**
	 * 关闭按钮的对象
	 */
	this.close = 0;
	/**
	 * 选中的日期，td的jquery对象
	 */
	this.selected_date = 0;
	/**
	 * 选中的月份，td的jquery对象
	 */
	this.selected_month = 0;
	/**
	 * 当前年份，文本框的jquery对象
	 */
	this.menu_year = 0;
	/**
	 * 日历的模板对象
	 */
	this.template = 0;
	
	/**
	 * 42个日期的jquery对象
	 */
	this.date_td = 0;
	/**
	 * 前一个月
	 */
	this.button_prevmonth = 0;
	/**
	 * 下一个月
	 */
	this.button_nextmonth = 0;
	/**
	 * 前一年
	 */
	this.button_prevyear = 0;
	/**
	 * 下一年
	 */
	this.button_nextyear = 0; 
	/**
	 * 日历头，显示年月的地方
	 */
	this.button_title = 0;
	/**
	 * 12个月份的jquery对象
	 */
	this.month_date = 0;
	/**
	 * 月份里的前一个月
	 */
	this.button_menu_prev = 0;
	/**
	 * 月份里的后一个月
	 */
	this.button_menu_next = 0;
	/**
	 * 月份的div
	 */
	this.calendar_menu = 0;
	/**
	 * 取得日历模板对象
	 */
	this.getTemplate = function(){
		if(this.template){
			return this.template;
		}else{	
			return getCalendarTemplate();
		}
	};
	/**
	 * 把处理好的模板加载入指定节点
	 */
	this.load = function(){
		this.pre_node.template.after(this.template);
	};
	/**
	 * 初始化日历，以上方法用在这里
	 */
	this.init = function(){
		this.template = this.getTemplate();
		this.date_td = this.template.find('table:first').find('[abbr]');
		this.button_prevmonth = this.template.find('.calendar-prevmonth');
		this.button_nextmonth = this.template.find('.calendar-nextmonth');
		this.button_prevyear = this.template.find('.calendar-prevyear');
		this.button_nextyear = this.template.find('.calendar-nextyear'); 
		this.button_title = this.template.find('.calendar-title');
		this.month_date = this.template.find('.calendar-menu-month-inner').find('[abbr]');
		this.button_menu_prev = this.template.find('.calendar-menu-prev');
		this.button_menu_next = this.template.find('.calendar-menu-next');
		this.calendar_menu = this.template.find('.calendar-menu');
		this.menu_year = this.template.find('.calendar-menu-year');
		this.today = this.template.find('.datebox-current');
		this.close = this.template.find('.datebox-close');
		this.addEvent(this);
		this.getDateData(this);
		var current_month = this.current_date.getMonth()+1;
		this.menu_year.val(this.current_date.getFullYear());
		this.selected_month = this.template.find('[abbr='+current_month+']');
		this.selected_month.addClass("calendar-selected");
		//this.load();
	};
	/**
	 * 隐藏日历
	 */
	this.hide = function(){
		this.template.css('display','none');
		this.isShow = false;
	};
	/**
	 * 显示日历
	 */
	this.show = function(){
		this.setPosition();
		this.template.css("display","block");
		this.isShow = true;
	};
	this.getLeft = function(){
		if(this.left == 0){
			this.left = this.pre_node.template.offset().left;
		}
		return this.left;
	};
	this.getTop = function(){
		if(this.top == 0){
			this.top = this.pre_node.template.offset().top+this.pre_node.template.height();
		}
		return this.top;
	};
	this.setPosition = function(){
		this.template.css({
			top:this.getTop(),
			left:this.getLeft()
		});
	};
}
/**
 * 给日历日期td加上事件
 */
Calendar.prototype.addTdEvent = function(calendar,td){
	 td.hover(
          function () {
            $(this).addClass("calendar-hover");
           },
           function () {
            $(this).removeClass("calendar-hover");
           }
    );
    
	td.click(function(){
        if(calendar.selected_date){
        	calendar.selected_date.removeClass("calendar-selected");
        }
        $(this).addClass("calendar-selected");
        calendar.selected_date = $(this);
        calendar.pre_node.text.val($(this).attr("abbr"));
        calendar.pre_node.pre_node.val($(this).attr("abbr"));
        if(calendar.pre_node.sequence ==0){
        	var str_time = $(this).attr("abbr").split('-');
        	calendar.pre_node.outer.start_time = new Date(str_time[0],str_time[1]-1,str_time[2]);
        	var other_calendar = calendar.pre_node.outer.date_box_end.calendar;
        	other_calendar.getDateData(other_calendar);
        }
    });
}
/**
 * 给日历加上事件响应
 */
Calendar.prototype.addEvent=function(calendar){
	calendar.today.hover(
	          function () {
	            $(this).addClass("datebox-button-hover");
	           },
	           function () {
	            $(this).removeClass("datebox-button-hover");
	           }
	);
	calendar.close.hover(
	          function () {
	            $(this).addClass("datebox-button-hover");
	           },
	           function () {
	            $(this).removeClass("datebox-button-hover");
	           }
	);
    calendar.close.hover(
          function () {
            $(this).addClass("calendar-menu-hover");
           },
           function () {
            $(this).removeClass("calendar-menu-hover");
           }
    );
    calendar.button_menu_next.hover(
          function () {
            $(this).addClass("calendar-menu-hover");
           },
           function () {
            $(this).removeClass("calendar-menu-hover");
           }
    );
    calendar.date_td.hover(
          function () {
            $(this).addClass("calendar-hover");
           },
           function () {
            $(this).removeClass("calendar-hover");
           }
    );
    calendar.month_date.hover(
          function () {
            $(this).addClass("calendar-menu-hover");
           },
           function () {
            $(this).removeClass("calendar-menu-hover");
           }
    );
    calendar.button_prevmonth.hover(
         function () {
            $(this).addClass("calendar-nav-hover");
           },
           function () {
            $(this).removeClass("calendar-nav-hover");
           }
    );
    calendar.button_nextmonth.hover(
         function () {
            $(this).addClass("calendar-nav-hover");
           },
           function () {
            $(this).removeClass("calendar-nav-hover");
           }
    );
    calendar.button_prevyear.hover(
         function () {
            $(this).addClass("calendar-nav-hover");
           },
           function () {
            $(this).removeClass("calendar-nav-hover");
           }
    );
    calendar.button_nextyear.hover(
         function () {
            $(this).addClass("calendar-nav-hover");
           },
           function () {
            $(this).removeClass("calendar-nav-hover");
           }
    );
    calendar.button_title.hover(
         function () {
            $(this).find('span').addClass("calendar-menu-hover");
           },
           function () {
            $(this).find('span').removeClass("calendar-menu-hover");
           }
    );
    calendar.today.click(function(){
    	calendar.current_date = new Date();
    	calendar.getDateData(calendar);
    	calendar.calendar_menu.css("display","none");
    });
    calendar.close.click(function(){
    	calendar.hide();
    });
    calendar.date_td.click(function(){
        if(calendar.selected_date){
        	calendar.selected_date.removeClass("calendar-selected");
        }
        $(this).addClass("calendar-selected");
        calendar.selected_date = $(this);
        calendar.pre_node.text.val($(this).attr("abbr"));
        calendar.pre_node.pre_node.val($(this).attr("abbr"));
        if(calendar.pre_node.sequence ==0){
        	var str_time = $(this).attr("abbr").split('-');
        	calendar.pre_node.outer.start_time = new Date(str_time[0],str_time[1]-1,str_time[2]);
        	var other_calendar = calendar.pre_node.outer.date_box_end.calendar;
        	other_calendar.getDateData(other_calendar);
        	other_calendar.pre_node.text.val("");
        }
    });
    calendar.month_date.click(function(){
        if(calendar.selected_month){
        	calendar.selected_month.removeClass("calendar-selected");
        }
        $(this).addClass("calendar-selected");
        calendar.selected_month = $(this);

        calendar.current_date.setMonth(calendar.selected_month.attr('abbr')-1);
        calendar.getDateData(calendar);
    });
    
    calendar.button_prevmonth.click(function(){
        calendar.current_date.setMonth(calendar.current_date.getMonth()-1);
        calendar.getDateData(calendar);
    });
    calendar.button_nextmonth.click(function(){
    	calendar.current_date.setMonth(calendar.current_date.getMonth()+1);
        calendar.getDateData(calendar);
    });
    calendar.button_prevyear.click(function(){
    	calendar.current_date.setFullYear(calendar.current_date.getFullYear()-1);
    	calendar.getDateData(calendar);
        calendar.menu_year.val(calendar.current_date.getFullYear());
    });
    calendar.button_nextyear.click(function(){
    	calendar.current_date.setFullYear(calendar.current_date.getFullYear()+1);
    	calendar.getDateData(calendar);
        calendar.menu_year.val(calendar.current_date.getFullYear());
    });
    calendar.button_menu_prev.click(function(){
    	calendar.current_date.setFullYear(calendar.current_date.getFullYear()-1);
    	calendar.getDateData(calendar);
        calendar.menu_year.val(calendar.current_date.getFullYear());
    });
    calendar.button_menu_next.click(function(){
    	calendar.current_date.setFullYear(calendar.current_date.getFullYear()+1);
    	calendar.getDateData(calendar);
        calendar.menu_year.val(calendar.current_date.getFullYear());
    });
    calendar.button_title.click(function(){
        var menu = calendar.calendar_menu;
        if(menu.css('display')=="none"){
            menu.css('display','block');
        }else{
            menu.css('display','none');
        }
    });
    calendar.menu_year.keypress(function(event){
    	var e = event || window.event || arguments.callee.caller.arguments[0];
    	var key_code = e.keyCode||e.which||charCode;
    	if(key_code==13){
        	var val = calendar.menu_year.val();
        	if(!isNaN(val)){
        		calendar.current_date.setFullYear(val);
        		calendar.getDateData(calendar);
        	}else{
        		calendar.menu_year.val(calendar.current_date.getFullYear());
        	}
   		}
    });
    
};  
/**
 * 根据给出的日期计算当前月日期并赋值
 * @param p_date
 */
Calendar.prototype.getDateData=function(calendar){
	var currrent_date = calendar.current_date;
	var current_year = currrent_date.getFullYear();
	var current_month = currrent_date.getMonth();
	var selected_date = calendar.selected_date;
	calendar.button_title.find('span').html(current_year+'年'+(current_month+1)+'月');
    var first_date = new Date(current_year,current_month,1);
    var fistt_date_week = first_date.getDay();
    var diff_date = 1;
    if(fistt_date_week==0){
        diff_date = -6;
    }else{
        diff_date =1-fistt_date_week;
    }
    var first_position_date = new Date(current_year,current_month,diff_date);
    var start_year = first_position_date.getFullYear();
    var start_month = first_position_date.getMonth();
    var start_date = first_position_date.getDate();
    var td = calendar.date_td;
    var local_date = new Date(start_year,start_month,(start_date));
    
    for(var i=0;i<td.length;i++){
        var tdo = $(td[i]);
        local_date.setMonth(start_month, start_date+i);
        if(calendar.pre_node.sequence == 1){
        	var start_tiem = calendar.pre_node.outer.start_time;
        	if(local_date<start_tiem){
	        	tdo.addClass("calendar-other-month");
	        	tdo.unbind();
        	}else{
        		calendar.addTdEvent(calendar,tdo);
        		var local_month = local_date.getMonth();
		        if(local_month != current_month){
		            tdo.addClass("calendar-other-month");
		        }else{
		            tdo.removeClass("calendar-other-month");
		        }            
        	}
        }else{
	        var local_month = local_date.getMonth();
	        if(local_month != current_month){
	            tdo.addClass("calendar-other-month");
	        }else{
	            tdo.removeClass("calendar-other-month");
	        }            
        }
        tdo.attr("abbr",local_date.format());
	    tdo.html(local_date.getDate());
    }
 
	if(selected_date){
            selected_date.removeClass("calendar-selected");
            selected_date = 0;
    }
   	var today_td = calendar.template.find('[class*=calendar-today]');
    if(today_td){
    	today_td.removeClass("calendar-today");
    }
    var today_str = new Date().format();
	today_td = calendar.template.find('[abbr='+today_str+']');
	if(today_td){
		today_td.addClass("calendar-today");
		if(calendar.pre_node.sequence == 0){
			calendar.selected_date = today_td;
			today_td.addClass("calendar-selected");
		}
	} 
	if(calendar.selected_month){
		calendar.selected_month.removeClass("calendar-selected");
	}
	calendar.selected_month = this.template.find('[abbr='+(current_month+1)+']');
	calendar.selected_month.addClass("calendar-selected");
};
/**
 * DateBox模板，返回模板对象
 */
function getDateBoxTemplate(){
	var template = '<span style="width: 130px; height: 20px;" class="combo datebox">'+
						'<input style="width: 108px; height: 20px; line-height: 20px;" autocomplete="off" class="combo-text validatebox-text" type="text">'+
						'<span>'+
							'<span style="height: 20px;" class="combo-arrow"></span>'+
						'</span>'+
						'<input value="" class="combo-value" type="hidden">'+
				   '</span>';
	return $(template);
}

/**
 * 日历模板
 * @returns
 */
function getCalendarTemplate(){
	var template =
			'<div class="datebox-calendar-inner">'+
				'<div style="width: 176px; height: 178px;" class="calendar calendar-noborder">'+
					'<div class="calendar-header">'+
						'<div class="calendar-prevmonth"></div>'+
						'<div class="calendar-nextmonth"></div>'+
						'<div class="calendar-prevyear"></div>'+
						'<div class="calendar-nextyear"></div>'+
						'<div class="calendar-title">'+
							'<span class=""></span>'+
						'</div>'+
					'</div>'+
					'<div style="height: 156px;" class="calendar-body">'+
						'<table border="0" cellpadding="0" cellspacing="0">'+
							'<thead>'+
								'<th>日</th> <th>一</th> <th>二</th> <th>三</th> <th>四</th> <th>五</th> <th>六</th>'+
							'</thead>'+
							'<tbody>'+
								'<tr>'+
									'<td abbr="" class="calendar-day  calendar-sunday"></td>'+
									'<td abbr="" class="calendar-day "></td>'+
									'<td abbr="" class="calendar-day "></td>'+
									'<td abbr="" class="calendar-day"></td>'+
									'<td abbr="" class="calendar-day"></td>'+
									'<td abbr="" class="calendar-day"></td>'+
									'<td abbr="" class="calendar-day calendar-saturday"></td>'+
								'</tr>'+
								'<tr>'+
									'<td abbr="" class="calendar-day calendar-sunday"></td>'+
									'<td abbr="" class="calendar-day"></td>'+
									'<td abbr="" class="calendar-day"></td>'+
									'<td abbr="" class="calendar-day"></td>'+
									'<td abbr="" class="calendar-day"></td>'+
									'<td abbr="" class="calendar-day"></td>'+
									'<td abbr="" class="calendar-day calendar-saturday"></td>'+
								'</tr>'+
								'<tr>'+
									'<td abbr="" class="calendar-day calendar-sunday"></td>'+
									'<td abbr="" class="calendar-day"></td>'+
									'<td abbr="" class="calendar-day"></td>'+
									'<td abbr="" class="calendar-day"></td>'+
									'<td abbr="" class="calendar-day"></td>'+
									'<td abbr="" class="calendar-day"></td>'+
									'<td abbr="" class="calendar-day calendar-saturday"></td>'+
								'</tr>'+
								'<tr>'+
									'<td abbr="" class="calendar-day calendar-today calendar-sunday"></td>'+
									'<td abbr="" class="calendar-day"></td>'+
									'<td abbr="" class="calendar-day"></td>'+
									'<td abbr="" class="calendar-day"></td>'+
									'<td abbr="" class="calendar-day"></td>'+
									'<td abbr="" class="calendar-day"></td>'+
									'<td abbr="" class="calendar-day calendar-saturday"></td>'+
								'</tr>'+
								'<tr>'+
									'<td abbr="" class="calendar-day calendar-sunday"></td>'+
									'<td abbr="" class="calendar-day"></td>'+
									'<td abbr="" class="calendar-day"></td>'+
									'<td abbr="" class="calendar-day"></td>'+
									'<td abbr="" class="calendar-day"></td>'+
									'<td abbr="" class="calendar-day"></td>'+
									'<td abbr="" class="calendar-day  calendar-saturday"></td>'+
								'</tr>'+
								'<tr>'+
									'<td abbr="" class="calendar-day  calendar-sunday"></td>'+
									'<td abbr="" class="calendar-day "></td>'+
									'<td abbr="" class="calendar-day "></td>'+
									'<td abbr="" class="calendar-day "></td>'+
									'<td abbr="" class="calendar-day "></td>'+
									'<td abbr="" class="calendar-day "></td>'+
									'<td abbr="" class="calendar-day calendar-other-month calendar-saturday"></td>'+
								'</tr>'+
							'</tbody>'+
						'</table>'+
						'<div style="display: none; width: 166px; height: 146px;" class="calendar-menu">'+
							'<div class="calendar-menu-year-inner">'+
								'<span class="calendar-menu-prev"></span><span><input class="calendar-menu-year" type="text" maxlength="4">'+
								'</span><span class="calendar-menu-next"></span>'+
							'</div>'+
							'<div style="height: 120px;" class="calendar-menu-month-inner">'+
								'<table>'+
									'<tbody>'+
										'<tr>'+
											'<td abbr="1" class="calendar-menu-month">1月</td>'+
											'<td abbr="2" class="calendar-menu-month">2月</td>'+
											'<td abbr="3" class="calendar-menu-month">3月</td>'+
											'<td abbr="4" class="calendar-menu-month">4月</td>'+
										'</tr>'+
										'<tr>'+
											'<td abbr="5" class="calendar-menu-month">5月</td>'+
											'<td abbr="6" class="calendar-menu-month">6月</td>'+
											'<td abbr="7" class="calendar-menu-month">7月</td>'+
											'<td abbr="8" class="calendar-menu-month">8月</td>'+
										'</tr>'+
										'<tr>'+
											'<td abbr="9" class="calendar-menu-month">9月</td>'+
											'<td abbr="10" class="calendar-menu-month">10月</td>'+
											'<td abbr="11" class="calendar-menu-month">11月</td>'+
											'<td abbr="12" class="calendar-menu-month">12月</td>'+
										'</tr>'+
									'</tbody>'+
								'</table>'+
							'</div>'+
						'</div>'+
					'</div>'+
				'</div>'+
			'</div>'+
			'<div class="datebox-button">'+
				'<a href="javascript:void(0)" class="datebox-current">Today</a>'+
			'</div>';
	return $(template);
}
function getMonthTemplate(){

	var template = 	'<div class="calendar-menu-year-inner"><span class="calendar-menu-prev"></span><span><input class="calendar-menu-year" type="text" maxlength="4"></span><span class="calendar-menu-next"></span></div><div style="height: 120px;" class="calendar-menu-month-inner"><table><tbody><tr><td abbr="1" class="calendar-menu-month">1月</td><td ="2" class="calendar-menu-month">2月</td><td abbr="3" class="calendar-menu-month">三月</td><td abbr="4" class="calendar-menu-month">4月</td></tr><tr><td abbr="5" class="calendar-menu-month calendar-selected">五月</td><td abbr="6" class="calendar-menu-month">Jun</td><td abbr="7" class="calendar-menu-month">7月</td><td abbr="8" class="calendar-menu-month">8月</td></tr><tr><td abbr="9" class="calendar-menu-month">9月</td><td abbr="10" class="calendar-menu-month">10月</td><td abbr="11" class="calendar-menu-month">11月</td><td abbr="12" class="calendar-menu-month">12月</td></tr></tbody></table></div>';
	return $(template);

}
/**
 * 取得DateBoxDiv模板
 * @return {}
 */
function getDateBoxDivTemplate(){
	var template =
	'<div  style="position: absolute; z-index: 9001; display: none">'+
		'<table style="border:1px solid #A4BED4">'+
			'<tbody>'+
				'<tr>'+
					'<td nowrap="nowrap"><span class="the-start"></span></td>'+
					'<td nowrap="nowrap"><span class="the-end"></span></td>'+
				'<tr>'+
				'<tr>'+
					'<td nowrap="nowrap" colspan="2" class="datebox-button"><a href="javascript:void(0)" class="datebox-close">close</a></td>'+
				'<tr>'+
			'</tbody>'+
		'</table>'+
	'</div>';
	return $(template);
}
	
/**
 * 日期格式化
 * @returns
 */
Date.prototype.format=function(){
	var y = this.getFullYear();  
    var m = this.getMonth()+1;  
    var d = this.getDate();      
    
    return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
}; 