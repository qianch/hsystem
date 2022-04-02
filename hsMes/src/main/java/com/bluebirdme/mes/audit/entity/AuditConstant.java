package com.bluebirdme.mes.audit.entity;

/**
 * 审核状态定义
 * @author Goofy
 * @Date 2016年10月24日 下午2:36:01
 */
public interface AuditConstant {
	
	public interface LEVEL{
		
		/**
		 * 第一级节点
		 */
		public final static Integer ONE=1;
		/**
		 * 第二级节点
		 */
		public final static Integer TWO=2;
	}
	
	public interface RS{
		/**
		 * 不通过
		 */
		public final static Integer REJECT=-1;
		
		/**
		 * 通过
		 */
		public final static Integer PASS=2;
		
		/**
		 * 审核中
		 */
		public final static Integer AUDITING=1;
		
		/**
		 * 待提交
		 */
		public final static Integer SUBMIT=0;
	}
	
	public interface STATE{
		/**
		 * 结束
		 */
		public final static Integer FINISHED=1;
		
		/**
		 * 未结束
		 */
		public final static Integer UNFINISHED=0;
		
	}
	
	public interface CODE{
		/**
		 * 销售
		 */
		public final static String XS="XS";
		/**
		 * 生产
		 */
		public final static String SC="SC";
		/**
		 * 翻包
		 */
		public final static String FB="FB";
		/**
		 * 冻结
		 */
		public final static String DJ="DJ";
		/**
		 * 解冻
		 */
		public final static String JD="JD";
		/**
		 * 内销出库
		 */
		public final static String CK="CK";
		/**
		 * 外销出库
		 */
		public final static String CK1="CK1";
		/**
		 * 非套材常规
		 */
		public final static String FTC="FTC";
		/**
		 * 非套材变更试样
		 */
		public final static String FTC1="FTC1";
		/**
		 * 非套材新品试样
		 */
		public final static String FTC2="FTC2";
		/**
		 * 套材常规
		 */
		public final static String TC="TC";
		/**
		 * 套材变更试样
		 */
		public final static String TC1="TC1";
		/**
		 * 套材新品试样
		 */
		public final static String TC2="TC2";
		/**
		 * 包材常规
		 */
		public final static String BC="BC";
		/**
		 * 包材变更试样
		 */
		public final static String BC1="BC1";
		/**
		 * 包材新品试样
		 */
		public final static String BC2="BC2";
		/**
		 * 非套材包材常规
		 */
		public final static String FTCBC="FTCBC";
		/**
		 * 非套材包材变更试样
		 */
		public final static String FTCBC1="FTCBC1";
		/**
		 * 非套材包材新品试样
		 */
		public final static String FTCBC2="FTCBC2";
		/**
		 * 日计划
		 */
		public final static String RJH="RJH";
		/**
		 * 裁剪日计划
		 */
		public final static String CRJH="CRJH";
		/**
		 * 编制日计划（一车间）
		 */
		public final static String RJH1="RJH1";
		/**
		 * 编制日计划（二车间）
		 */
		public final static String RJH2="RJH2";
		/**
		 * 编制日计划（三车间）
		 */
		public final static String RJH3="RJH3";
		/**
		 * 销售（国内）
		 */
		public final static String XS1="XS1";
		/**
		 * 销售（国外）
		 */
		public final static String XS2="XS2";
		
		/**
		 * 国内销售变更审核
		 */
		public final static String XS1BG="XS1BG";
		
		/**
		 * 国外销售变更审核
		 */
		public final static String XS2BG="XS2BG";
		/**
		 * 成品套材常规
		 */
		public final static String CPTC="CPTC";
		/**
		 * 成品套材试用
		 */
		public final static String CPTS="CPTS";
		/**
		 * 成品非套材常规
		 */
		public final static String CPFC="CPFC";
		/**
		 * 成品非套材试用
		 */
		public final static String CPFS="CPFS";
		/**
		 * 成品胚布常规
		 */
		public final static String CPPC="CPPC";
		/**
		 * 成品胚布试用
		 */
		public final static String CPPS="CPPS";
	}
}
