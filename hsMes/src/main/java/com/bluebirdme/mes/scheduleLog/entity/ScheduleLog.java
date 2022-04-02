/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2018版权所有
 */
package com.bluebirdme.mes.scheduleLog.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

/**
 * 调度日志
 * @author Goofy
 * @Date 2018年1月25日 上午9:10:52
 */
@Desc("调度日志")
@Entity
@Table(name="hs_schedule_log")
public class ScheduleLog extends BaseEntity {
	
	@Desc("任务名称")
	@Index(name="idx_taskName")
	private String taskName;
	
	@Desc("运行时间")
	@Index(name="idx_executeTime")
	private String executeTime;
	
	@Desc("运行用户")
	@Index(name="idx_executeUser")
	private String executeUser;
	@Column(nullable=false)
	
	@Desc("运行结果")
	@Index(name="idx_executeResult")
	private Integer executeResult;
	
	@Desc("运行速度")
	private Integer executeSpendTime;
	
	@Desc("备注")
	@Column(columnDefinition="text")
	private String executeMemo;
	
	@Desc("异常栈")
	@Column(columnDefinition="text")
	private String executeExceptionStack;
	
	@Desc("异常消息")
	private String executeExceptionMessage;
	
	@Desc("异常原因")
	private String executeExceptionCause;
	
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getExecuteTime() {
		return executeTime;
	}
	public void setExecuteTime(String executeTime) {
		this.executeTime = executeTime;
	}
	public String getExecuteUser() {
		return executeUser;
	}
	public void setExecuteUser(String executeUser) {
		this.executeUser = executeUser;
	}
	public Integer getExecuteResult() {
		return executeResult;
	}
	public void setExecuteResult(Integer executeResult) {
		this.executeResult = executeResult;
	}
	public Integer getExecuteSpendTime() {
		return executeSpendTime;
	}
	public void setExecuteSpendTime(Integer executeSpendTime) {
		this.executeSpendTime = executeSpendTime;
	}
	public String getExecuteMemo() {
		return executeMemo;
	}
	public void setExecuteMemo(String executeMemo) {
		this.executeMemo = executeMemo;
	}
	public String getExecuteExceptionStack() {
		return executeExceptionStack;
	}
	public void setExecuteExceptionStack(String executeExceptionStack) {
		this.executeExceptionStack = executeExceptionStack;
	}
	public String getExecuteExceptionMessage() {
		return executeExceptionMessage;
	}
	public void setExecuteExceptionMessage(String executeExceptionMessage) {
		this.executeExceptionMessage = executeExceptionMessage;
	}
	public String getExecuteExceptionCause() {
		return executeExceptionCause;
	}
	public void setExecuteExceptionCause(String executeExceptionCause) {
		this.executeExceptionCause = executeExceptionCause;
	}
	
	
}
