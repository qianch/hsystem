package com.bluebirdme.mes.siemens.bom.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import com.bluebirdme.mes.siemens.barcode.entity.FragmentBarcode;
import org.xdemo.superutil.j2se.ReflectUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.List;

/**
 * 裁片管理
 *
 * @author Goofy
 * @Date 2017年7月19日 下午4:01:46
 */
@Desc("裁片管理")
@Entity
@Table(name = "hs_siemens_fragment")
public class Fragment extends BaseEntity {
    @Desc("套材BOM ID")
    @Column(nullable = false)
    private Long tcBomId;

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

    @Desc("胚布规格")
    @Column(nullable = false)
    private String farbicModel;

    @Desc("备注")
    @Column(nullable = true, columnDefinition = "text")
    private String fragmentMemo;
    ;

    @Desc("数量")
    @Column(nullable = false)
    private Integer fragmentCountPerDrawings;

    @Desc("已删除")
    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer isDeleted;

    public Long getTcBomId() {
        return tcBomId;
    }

    public void setTcBomId(Long tcBomId) {
        this.tcBomId = tcBomId;
    }

    public String getFragmentCode() {
        return fragmentCode;
    }

    public void setFragmentCode(String fragmentCode) {
        this.fragmentCode = fragmentCode;
    }

    public String getFragmentName() {
        return fragmentName;
    }

    public void setFragmentName(String fragmentName) {
        this.fragmentName = fragmentName;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
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

    public static void main(String[] args) {
        List<Field> list = ReflectUtils.getFields(FragmentBarcode.class, true);
        for (Field field : list) {
            System.out.println("<#if key[\"" + field.getName().toUpperCase() + "\"]??> and " + field.getName().toUpperCase() + " like :" + field.getName().toUpperCase() + "</#if>");
        }
    }
}
