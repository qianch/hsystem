package com.bluebirdme.mes.siemens.barcode.entity;

import java.lang.reflect.Field;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.xdemo.superutil.j2se.MapUtils;
import org.xdemo.superutil.j2se.ReflectUtils;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

/**
 * 条码扫描错误记录
 * @author Goofy
 * @Date 2017年8月8日 下午2:23:47
 */
@Desc("条码扫描错误记录")
@Entity
@Table(name="hs_siemens_suit_error_log")
public class SuitErrorLog extends BaseEntity {
	
	@Desc("条码号")
	@Column(nullable=false)
	private String fragmentBarcode;

	@Desc("打印类型")
	@Column(nullable=false)
	private String fragmentPrintType;

	@Desc("任务单号")
	@Column(nullable=false)
	private String fragmentCtCode;

	@Desc("订单号")
	@Column(nullable=false)
	private String fragmentOrderCode;

	@Desc("批次号")
	@Column(nullable=false)
	private String fragmentBatchCode;

	@Desc("派工单号")
	@Column(nullable=false)
	private String fragmentCtoCode;

	@Desc("客户简称")
	@Column(nullable=false)
	private String fragmentConsumerSimpleName;

	@Desc("客户大类")
	@Column(nullable=false)
	private String fragmentConsumerCategory;

	@Desc("部件名称")
	@Column(nullable=false)
	private String partName;

	@Desc("小部件名称")
	@Column(nullable=false)
	private String fragmentName;

	@Desc("组套任务单号")
	@Column(nullable=false)
	private String suitCtCode;

	@Desc("组套订单号")
	@Column(nullable=false)
	private String suitOrderCode;

	@Desc("组套批次号")
	@Column(nullable=false)
	private String suitBatchCode;

	@Desc("组套派工单号")
	@Column(nullable=false)
	private String suitCtoCode;


	@Desc("错误信息")
	@Column(nullable=false)
	private String errorMsg;

	@Desc("扫描时间")
	@Column(nullable=false)
	private String scanTime;
	
	@Desc("操作人")
	@Column(nullable=false)
	private String scanUser;
	
	@Desc("部件条码")
	@Column(nullable=false)
	private String partBarcode;

	public String getFragmentBarcode() {
		return fragmentBarcode;
	}

	public void setFragmentBarcode(String fragmentBarcode) {
		this.fragmentBarcode = fragmentBarcode;
	}

	public String getFragmentPrintType() {
		return fragmentPrintType;
	}

	public void setFragmentPrintType(String fragmentPrintType) {
		this.fragmentPrintType = fragmentPrintType;
	}

	public String getFragmentCtCode() {
		return fragmentCtCode;
	}

	public void setFragmentCtCode(String fragmentCtCode) {
		this.fragmentCtCode = fragmentCtCode;
	}

	public String getFragmentOrderCode() {
		return fragmentOrderCode;
	}

	public void setFragmentOrderCode(String fragmentOrderCode) {
		this.fragmentOrderCode = fragmentOrderCode;
	}

	public String getFragmentBatchCode() {
		return fragmentBatchCode;
	}

	public void setFragmentBatchCode(String fragmentBatchCode) {
		this.fragmentBatchCode = fragmentBatchCode;
	}

	public String getFragmentCtoCode() {
		return fragmentCtoCode;
	}

	public void setFragmentCtoCode(String fragmentCtoCode) {
		this.fragmentCtoCode = fragmentCtoCode;
	}

	public String getFragmentConsumerSimpleName() {
		return fragmentConsumerSimpleName;
	}

	public void setFragmentConsumerSimpleName(String fragmentConsumerSimpleName) {
		this.fragmentConsumerSimpleName = fragmentConsumerSimpleName;
	}

	public String getFragmentConsumerCategory() {
		return fragmentConsumerCategory;
	}

	public void setFragmentConsumerCategory(String fragmentConsumerCategory) {
		this.fragmentConsumerCategory = fragmentConsumerCategory;
	}

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public String getFragmentName() {
		return fragmentName;
	}

	public void setFragmentName(String fragmentName) {
		this.fragmentName = fragmentName;
	}

	public String getSuitCtCode() {
		return suitCtCode;
	}

	public void setSuitCtCode(String suitCtCode) {
		this.suitCtCode = suitCtCode;
	}

	public String getSuitOrderCode() {
		return suitOrderCode;
	}

	public void setSuitOrderCode(String suitOrderCode) {
		this.suitOrderCode = suitOrderCode;
	}

	public String getSuitBatchCode() {
		return suitBatchCode;
	}

	public void setSuitBatchCode(String suitBatchCode) {
		this.suitBatchCode = suitBatchCode;
	}

	public String getSuitCtoCode() {
		return suitCtoCode;
	}

	public void setSuitCtoCode(String suitCtoCode) {
		this.suitCtoCode = suitCtoCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getScanTime() {
		return scanTime;
	}

	public void setScanTime(String scanTime) {
		this.scanTime = scanTime;
	}

	public String getScanUser() {
		return scanUser;
	}

	public void setScanUser(String scanUser) {
		this.scanUser = scanUser;
	}

	public String getPartBarcode() {
		return partBarcode;
	}

	public void setPartBarcode(String partBarcode) {
		this.partBarcode = partBarcode;
	}
	
	public static void main(String[] args) throws Exception {
		List<Field> list=ReflectUtils.getFields(SuitErrorLog.class, false);
		int i=0;
		for(Field f:list){
			System.out.println("rowData["+i+++"] = MapUtils.getAsStringIgnoreCase(row, \""+f.getName().toUpperCase()+"\");");
		}
		
		for(Field f:list){
			Desc d=f.getAnnotation(Desc.class);
			System.out.print("\""+d.value()+"\",");
		}
	}
	
}
