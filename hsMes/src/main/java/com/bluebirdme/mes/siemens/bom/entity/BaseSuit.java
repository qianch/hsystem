package com.bluebirdme.mes.siemens.bom.entity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

/**
 * 组套BOM基类，还有裁剪任务单，派工单也会用到
 * @author Goofy
 * @Date 2017年7月31日 下午4:07:24
 */
@MappedSuperclass
public class BaseSuit extends BaseEntity {
	
	@Desc("套材BOM ID")
	@Column(nullable = false)
	private Long tcBomId;

	@Desc("小部件ID")
	@Column(nullable = false)
	private String fragmentId;

	@Desc("小部件编码")
	@Column(nullable = false)
	private String fragmentCode;

	@Desc("小部件名称")
	@Column(nullable = false)
	private String fragmentName;
	
	@Desc("小部件重量")
	@Column(nullable = false)
	private Double fragmentWeight;

	@Desc("长度(M)")
	@Column(nullable = true)
	private String fragmentLength;

	@Desc("宽度(M)")
	@Column(nullable = true)
	private String fragmentWidth;
	
	@Desc("部件ID")
	@Column(nullable = false)
	private Long partId;
	
	@Desc("部件名称")
	@Column
	private String partName;
	
	@Desc("胚布规格")
	@Column(nullable = true)
	private String farbicModel;
	
	@Desc("备注")
	@Column(nullable = true, columnDefinition = "text")
	private String fragmentMemo;;
	
	@Desc("数量")
	@Column(nullable = false)
	private Integer fragmentCountPerDrawings;
	
	@Desc("组套小部件数量")
	@Column
	private Integer suitCount;
	
	@Desc("排序")
	@Column
	private Integer suitSort;

	public Long getTcBomId() {
		return tcBomId;
	}

	public void setTcBomId(Long tcBomId) {
		this.tcBomId = tcBomId;
	}

	public String getFragmentId() {
		return fragmentId;
	}

	public void setFragmentId(String fragmentId) {
		this.fragmentId = fragmentId;
	}

	public String getFragmentName() {
		return fragmentName;
	}

	public void setFragmentName(String fragmentName) {
		this.fragmentName = fragmentName;
	}

	public String getFragmentCode() {
		return fragmentCode;
	}

	public void setFragmentCode(String fragmentCode) {
		this.fragmentCode = fragmentCode;
	}

	public Long getPartId() {
		return partId;
	}

	public void setPartId(Long partId) {
		this.partId = partId;
	}

	public Integer getSuitCount() {
		return suitCount;
	}

	public void setSuitCount(Integer suitCount) {
		this.suitCount = suitCount;
	}

	public Integer getSuitSort() {
		return suitSort;
	}

	public void setSuitSort(Integer suitSort) {
		this.suitSort = suitSort;
	}

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}
	
	
	
	public Double getFragmentWeight() {
		return fragmentWeight;
	}

	public void setFragmentWeight(Double fragmentWeight) {
		this.fragmentWeight = fragmentWeight;
	}

	public String getFragmentLength() {
		return fragmentLength;
	}

	public void setFragmentLength(String fragmentLength) {
		this.fragmentLength = fragmentLength;
	}

	public String getFragmentWidth() {
		return fragmentWidth;
	}

	public void setFragmentWidth(String fragmentWidth) {
		this.fragmentWidth = fragmentWidth;
	}

	public String getFarbicModel() {
		return farbicModel;
	}

	public void setFarbicModel(String farbicModel) {
		this.farbicModel = farbicModel;
	}

	public String getFragmentMemo() {
		return fragmentMemo;
	}

	public void setFragmentMemo(String fragmentMemo) {
		this.fragmentMemo = fragmentMemo;
	}

	public Integer getFragmentCountPerDrawings() {
		return fragmentCountPerDrawings;
	}

	public void setFragmentCountPerDrawings(Integer fragmentCountPerDrawings) {
		this.fragmentCountPerDrawings = fragmentCountPerDrawings;
	}

	/**
	 * 将图纸BOM转换成一个其他的子类
	 * @param clazz
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public <T extends BaseSuit> T convert(Class<T> clazz) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		T t=clazz.newInstance();
		Method ms[]=BaseSuit.class.getDeclaredMethods();
		for(Method m:ms){
			if(m.getName().startsWith("set")){
				m.invoke(t, BaseSuit.class.getMethod(m.getName().replaceFirst("s", "g"), null).invoke(this, null));
			}
		}
		return t;
	}

}
