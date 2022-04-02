import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

public class ZplPrinter {
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		
		for(int i=0;i<1;i++){
			long x=System.currentTimeMillis();
			Socket sk=new Socket(InetAddress.getByName("192.168.1.100"),7777);
			InputStream is= sk.getInputStream();
			InputStreamReader isr =new InputStreamReader(is,"UTF-8");
			BufferedReader reader=new BufferedReader(isr);
			OutputStream writer = sk.getOutputStream();
			writer.write("test,D:\\muban\\恒石条码(订单).btw,D:\\muban\\恒石条码(订单).txt,db".getBytes(Charset.forName("UTF-8")));
			String data=reader.readLine();
			writer.close();
			sk.close();
			if(data.startsWith("10")){
				System.out.println("打印成功");
			}else if(data.startsWith("20")){
				System.out.println("找不到打印机");
			}else{
				System.out.println("打印错误");
			}
			long y=System.currentTimeMillis();
			System.out.println("耗时："+(y-x)+"毫秒");
		}
	}

}