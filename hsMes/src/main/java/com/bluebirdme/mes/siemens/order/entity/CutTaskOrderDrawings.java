package com.bluebirdme.mes.siemens.order.entity;

import java.lang.reflect.Field;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.xdemo.superutil.j2se.ReflectUtils;

import com.bluebirdme.mes.core.annotation.Desc;

/**
 * 裁剪任务单图纸BOM
 * @author Goofy  
 * @Date 2017年7月31日 下午12:52:03
 */
@Entity
@Table(name="hs_siemens_cut_task_order_drawings")
public class CutTaskOrderDrawings extends FragmentSummary {
	
	@Desc("裁剪派工单ID")
	@Column
	private Long ctoId;
	
	@Desc("胚布卷数")
	@Column
	private String farbicRollCount;

	public Long getCtoId() {
		return ctoId;
	}

	public void setCtoId(Long ctoId) {
		this.ctoId = ctoId;
	}

	public String getFarbicRollCount() {
		return farbicRollCount;
	}

	public void setFarbicRollCount(String farbicRollCount) {
		this.farbicRollCount = farbicRollCount;
	}
	
	//{field:'orderid',title:'Order ID',width:200},
	
	public static void main(String[] args) {
		List<Field> list=ReflectUtils.getFields(CutTaskOrderDrawings.class, true);
		
		for(Field f:list){
			Desc d=f.getAnnotation(Desc.class);
			System.out.println("{field:'"+f.getName().toUpperCase()+"',title:'"+(d==null?f.getName():d.value())+"',width:15},");
		}
	}
	
}
