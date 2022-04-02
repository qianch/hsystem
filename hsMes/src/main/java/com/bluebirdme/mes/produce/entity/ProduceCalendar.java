package com.bluebirdme.mes.produce.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

/**
 * 排产日历
 * @author Goofy
 * @Date 2016年11月1日 下午12:55:15
 */
@Desc("排产日历")
@Entity
@Table(name="hs_produce_calendar")
public class ProduceCalendar extends BaseEntity {
	
	@Desc("销售订单号")
	@Column(nullable=false)
	@Index(name="salesOrderCode")
	private String salesOrderCode;
	
	@Desc("开始时间")
	@Column(nullable=false)
	private Date startTime;
	
	@Desc("结束时间")
	@Column(nullable=false)
	private Date endTime;

	public String getSalesOrderCode() {
		return salesOrderCode;
	}

	public void setSalesOrderCode(String salesOrderCode) {
		this.salesOrderCode = salesOrderCode;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
}
