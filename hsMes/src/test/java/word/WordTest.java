/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2018版权所有
 */
package word;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;

/**
 * 
 * @author Goofy
 * @Date 2018年10月23日 上午10:42:09
 */
public class WordTest {

	public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
		final String uri = "http://ntcecf3.neea.edu.cn/";
		final String format = "HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		while (true) {
			System.out.println("正在尝试连接 到 "+uri);
			Date date = new Date();
			try {
				Jsoup.connect(uri).timeout(10000).get();
				System.out.println("成功连接");
				//打开浏览器
				Runtime.getRuntime().exec("C:\\Program Files\\Internet Explorer\\iexplore "+uri);
				//程序退出
				System.exit(0);
			} catch (Exception e) {
				//记录无法访问时间
				System.out.println(sdf.format(date) + " 无法访问");
			}finally{
				System.out.println("10秒后重试\n");
				//延迟10S重试
				Thread.sleep(10000L);
			}
			
		}

	}

}
