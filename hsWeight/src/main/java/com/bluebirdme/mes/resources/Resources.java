package com.bluebirdme.mes.resources;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xdemo.superutil.j2se.StringUtils;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 获取UI等资源文件
 * 
 * @author Goofy
 * @Date 2017年3月13日 上午10:51:51
 */
public class Resources {
	private String UI_BASE = "com/bluebirdme/mes/resources/";

	JsonParser jsonParser = new JsonParser();

	public URL getUi(String uiName) {
		URLClassLoader urlLoader = (URLClassLoader) this.getClass().getClassLoader();
		URL url = urlLoader.findResource(UI_BASE + uiName);
		return url;
	}

	/**
	 * 根据条码查询内容
	 * 
	 * @param code
	 * @return
	 * @throws IOException 
	 */
	public JsonObject getBarCodeInfo(String server_addr,String code) {
		try {
			String infoUrl = server_addr+"/mobile/common/infos?barCode=" + code;
			Document doc = Jsoup.connect(infoUrl).timeout(30000).get();
			JsonObject json = jsonParser.parse(doc.text()).getAsJsonObject();
			return json;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 保存重量
	 *
	 * @param code
	 *            条码号
	 * @param weight
	 *            重量
	 * @return
	 * @throws IOException
	 */
	public JsonObject saveWeight(String server_addr,String code, Double weight,String grade) throws IOException {
		String saveWeightUrl = server_addr+"/mobile/weight/saveWeight";
		Document doc = Jsoup.connect(saveWeightUrl).data("code", code).data("weight", weight.toString()).data("qualityGrade",grade).timeout(30000).post();
		JsonObject json = jsonParser.parse(doc.text()).getAsJsonObject();
		return json;
	}
	
	public JsonObject getTare(String server_addr,String rollBarcode, String carrierCode){
		try {
			String infoUrl = server_addr+"/mobile/weight/tare?rollBarcode=" + (StringUtils.isBlank(rollBarcode)?"":rollBarcode)+"&carrierCode="+(StringUtils.isBlank(carrierCode)?"":carrierCode);
			System.out.println(infoUrl);
			Document doc = Jsoup.connect(infoUrl).timeout(30000).get();
			JsonObject json = jsonParser.parse(doc.text()).getAsJsonObject();
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String[] getGrade(String server_addr){
		try {
			String infoUrl = server_addr+"/mobile/produce/getQualityGradeSelections";
			Document doc = Jsoup.connect(infoUrl).timeout(30000).post();

			JsonArray array = jsonParser.parse(doc.text()).getAsJsonArray();
			String[] grades=new String[array.size()];
			
			Iterator<JsonElement> it= array.iterator();
			int i=0;
			while(it.hasNext()){
				grades[i++]=it.next().getAsJsonObject().get("text").getAsString();
			}
			return grades;
		} catch (Exception e) {
			e.printStackTrace();
			this.warn("质量等级加载失败");
			System.exit(1);
			return null;
		}
	}

	/**
	 * 警告
	 * 
	 * @param text
	 */
	public void warn(String text) {
		Object[] options = { "确认" };
		JOptionPane.showOptionDialog(null, text, "提示", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
	}

	/**
	 * 错误提示框
	 * 
	 * @param text
	 */
	public void error(String text) {
		Object[] options = { "确认" };
		JOptionPane.showOptionDialog(null, text, "提示", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
	}

	/**
	 * 信息提示框
	 * 
	 * @param text
	 */
	/*public void info(String text) {
		Object[] options = { "确认" };
		JOptionPane.showOptionDialog(null, text, "提示", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
	}*/

	/**
	 * 确认对话框
	 * 
	 * @param text
	 *            提示内容
	 * @return 0：确认，1：取消
	 */
	public int confirm(String text) {
		Object[] options = { "确认", "取消" };
		return JOptionPane.showOptionDialog(null, text, "提示", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
	}
}
