package com.bluebirdme.mes.audit.entity;

/**
 * 审核状态定义
 *
 * @author Goofy
 * @Date 2016年10月24日 下午2:36:01
 */
public interface AuditConstant {

    public interface LEVEL {

        /**
         * 第一级节点
         */
        Integer ONE = 1;
        /**
         * 第二级节点
         */
        Integer TWO = 2;
    }

    public interface RS {
        /**
         * 不通过
         */
        Integer REJECT = -1;

        /**
         * 通过
         */
        Integer PASS = 2;

        /**
         * 审核中
         */
        Integer AUDITING = 1;

        /**
         * 待提交
         */
        Integer SUBMIT = 0;
    }

    public interface STATE {
        /**
         * 结束
         */
        Integer FINISHED = 1;

        /**
         * 未结束
         */
        Integer UNFINISHED = 0;

    }

    public interface CODE {
        /**
         * 销售
         */
        String XS = "XS";
        /**
         * 生产
         */
        String SC = "SC";
        /**
         * 翻包
         */
        String FB = "FB";
        /**
         * 冻结
         */
        String DJ = "DJ";
        /**
         * 解冻
         */
        String JD = "JD";
        /**
         * 内销出库
         */
        String CK = "CK";
        /**
         * 外销出库
         */
        String CK1 = "CK1";
        /**
         * 非套材常规
         */
        String FTC = "FTC";
        /**
         * 非套材变更试样
         */
        String FTC1 = "FTC1";
        /**
         * 非套材新品试样
         */
        String FTC2 = "FTC2";
        /**
         * 套材常规
         */
        String TC = "TC";
        /**
         * 套材变更试样
         */
        String TC1 = "TC1";
        /**
         * 套材新品试样
         */
        String TC2 = "TC2";
        /**
         * 包材常规
         */
        String BC = "BC";
        /**
         * 包材变更试样
         */
        String BC1 = "BC1";
        /**
         * 包材新品试样
         */
        String BC2 = "BC2";
        /**
         * 非套材包材常规
         */
        String FTCBC = "FTCBC";
        /**
         * 非套材包材变更试样
         */
        String FTCBC1 = "FTCBC1";
        /**
         * 非套材包材新品试样
         */
        String FTCBC2 = "FTCBC2";
        /**
         * 日计划
         */
        String RJH = "RJH";
        /**
         * 裁剪日计划
         */
        String CRJH = "CRJH";
        /**
         * 编制日计划（一车间）
         */
        String RJH1 = "RJH1";
        /**
         * 编制日计划（二车间）
         */
        String RJH2 = "RJH2";
        /**
         * 编制日计划（三车间）
         */
        String RJH3 = "RJH3";
        /**
         * 销售（国内）
         */
        String XS1 = "XS1";
        /**
         * 销售（国外）
         */
        String XS2 = "XS2";

        /**
         * 国内销售变更审核
         */
        String XS1BG = "XS1BG";

        /**
         * 国外销售变更审核
         */
        String XS2BG = "XS2BG";
        /**
         * 成品套材常规
         */
        String CPTC = "CPTC";
        /**
         * 成品套材试用
         */
        String CPTS = "CPTS";
        /**
         * 成品非套材常规
         */
        String CPFC = "CPFC";
        /**
         * 成品非套材试用
         */
        String CPFS = "CPFS";
        /**
         * 成品胚布常规
         */
        String CPPC = "CPPC";
        /**
         * 成品胚布试用
         */
        String CPPS = "CPPS";
    }
}
