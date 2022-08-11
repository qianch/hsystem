package com.bluebirdme.mes.siemens.bom.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 图纸BOM
 *
 * @author Goofy
 * @Date 2017年7月18日 上午11:23:21
 */
@MappedSuperclass
public class BaseDrawings extends BaseEntity {
    @Desc("图纸BOM ID")
    private Long dwId;

    @Desc("套材BOM ID")
    @Column(nullable = false)
    private Long tcBomId;

    @Desc("小部件ID")
    @Column(nullable = false)
    private String fragmentId;

    @Desc("小部件名称")
    @Column(nullable = true)
    private String fragmentName;

    @Desc("小部件重量")
    @Column(nullable = true)
    private Double fragmentWeight;

    @Desc("长度(M)")
    @Column(nullable = true)
    private String fragmentLength;

    @Desc("宽度(M)")
    @Column(nullable = true)
    private String fragmentWidth;

    @Desc("胚布规格")
    @Column(nullable = false)
    private String farbicModel;

    @Desc("数量")
    @Column(nullable = false)
    private Integer fragmentCountPerDrawings;

    @Desc("图号")
    @Column(nullable = false)
    private String fragmentDrawingNo;

    @Desc("图纸版本")
    @Column(nullable = false)
    private String fragmentDrawingVer;

    @Desc("部件ID")
    @Column(nullable = false)
    private Long partId;

    @Desc("部件名称")
    @Column(nullable = false)
    private String partName;

    @Desc("图内套数")
    @Column(nullable = true)
    private Integer suitCountPerDrawings;

    @Desc("出图顺序")
    @Column(nullable = false)
    private Integer printSort;

    @Desc("备注")
    @Column(nullable = true, columnDefinition = "text")
    private String fragmentMemo;

    @Desc("是否已删除")
    @Column
    private Integer isDeleted;

    @Transient
    private String _selected;

    @Desc("小部件编码")
    @Column(nullable = true)
    private String fragmentCode;


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

    public String getFarbicModel() {
        return farbicModel;
    }

    public void setFarbicModel(String farbicModel) {
        this.farbicModel = farbicModel;
    }

    public Integer getFragmentCountPerDrawings() {
        return fragmentCountPerDrawings;
    }

    public void setFragmentCountPerDrawings(Integer fragmentCountPerDrawings) {
        this.fragmentCountPerDrawings = fragmentCountPerDrawings;
    }

    public String getFragmentDrawingNo() {
        return fragmentDrawingNo;
    }

    public void setFragmentDrawingNo(String fragmentDrawingNo) {
        this.fragmentDrawingNo = fragmentDrawingNo;
    }

    public String getFragmentDrawingVer() {
        return fragmentDrawingVer;
    }

    public void setFragmentDrawingVer(String fragmentDrawingVer) {
        this.fragmentDrawingVer = fragmentDrawingVer;
    }

    public Long getPartId() {
        return partId;
    }

    public void setPartId(Long partId) {
        this.partId = partId;
    }

    public Integer getSuitCountPerDrawings() {
        return suitCountPerDrawings;
    }

    public void setSuitCountPerDrawings(Integer suitCountPerDrawings) {
        this.suitCountPerDrawings = suitCountPerDrawings;
    }

    public Integer getPrintSort() {
        return printSort;
    }

    public void setPrintSort(Integer printSort) {
        this.printSort = printSort;
    }

    public String getFragmentMemo() {
        return fragmentMemo;
    }

    public void setFragmentMemo(String fragmentMemo) {
        this.fragmentMemo = fragmentMemo;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getFragmentName() {
        return fragmentName;
    }

    public void setFragmentName(String fragmentName) {
        this.fragmentName = fragmentName;
    }

    public String get_selected() {
        return _selected;
    }

    public void set_selected(String _selected) {
        this._selected = _selected;
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

    public String getFragmentCode() {
        return fragmentCode;
    }

    public void setFragmentCode(String fragmentCode) {
        this.fragmentCode = fragmentCode;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public Long getDwId() {
        return dwId;
    }

    public void setDwId(Long dwId) {
        this.dwId = dwId;
    }

    /**
     * 将图纸BOM转换成一个其他的子类
     */
    public <T extends BaseDrawings> T convert(Class<T> clazz) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        T t = clazz.newInstance();
        Method[] ms = BaseDrawings.class.getDeclaredMethods();
        for (Method m : ms) {
            if (m.getName().startsWith("set")) {
                m.invoke(t, BaseDrawings.class.getMethod(m.getName().replaceFirst("s", "g"), null).invoke(this, null));
            }
        }
        return t;
    }
}
