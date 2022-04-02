package com.bluebirdme.mes.siemens.order.entity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.xdemo.superutil.j2se.ReflectUtils;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.siemens.bom.entity.BaseDrawings;

/**
 * 统计
 * 
 * @author Goofy
 * @Date 2017年7月31日 下午3:31:59
 */
@MappedSuperclass
public class FragmentSummary extends BaseDrawings {
	@Desc("派工套数")
	@Column(nullable=false)
	private Integer taskOrderCount;// 套,派工数量
	@Desc("应打数量")
	@Column(nullable=false)
	private Integer needToPrintCount;
	@Desc("已打数量")
	@Column(nullable=false)
	private Integer printedCount;
	@Desc("重打数量")
	@Column(nullable=false)
	private Integer rePrintCount;// 打出新条码，重打
	@Desc("补打数量")
	@Column(nullable=false)
	
	private Integer extraPrintCount;// 打出旧条码，补打
	
	public Integer getTaskOrderCount() {
		return taskOrderCount;
	}
	public void setTaskOrderCount(Integer taskOrderCount) {
		this.taskOrderCount = taskOrderCount;
	}
	public Integer getNeedToPrintCount() {
		return needToPrintCount;
	}
	public void setNeedToPrintCount(Integer needToPrintCount) {
		this.needToPrintCount = needToPrintCount;
	}
	public Integer getPrintedCount() {
		return printedCount;
	}
	public void setPrintedCount(Integer printedCount) {
		this.printedCount = printedCount;
	}
	public Integer getRePrintCount() {
		return rePrintCount;
	}
	public void setRePrintCount(Integer rePrintCount) {
		this.rePrintCount = rePrintCount;
	}
	public Integer getExtraPrintCount() {
		return extraPrintCount;
	}
	public void setExtraPrintCount(Integer extraPrintCount) {
		this.extraPrintCount = extraPrintCount;
	}
	
	/**
	 * 将任务图纸BOM转换成一个其他的子类
	 * @param clazz
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public <T extends FragmentSummary> T convert2(Class<T> clazz) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		T t=clazz.newInstance();
		List<Method> ms=ReflectUtils.getMethods(FragmentSummary.class, true);
		for(Method m:ms){
			if(m.getName().startsWith("set")){
				m.invoke(t, FragmentSummary.class.getMethod(m.getName().replaceFirst("s", "g"), null).invoke(this, null));
			}
		}
		return t;
	}
	

}
