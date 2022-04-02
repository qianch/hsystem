package code.generator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import onbon.bx05.Bx5GEnv;
import onbon.bx05.Bx5GException;
import onbon.bx05.Bx5GScreen;
import onbon.bx05.Bx5GScreenClient;
import onbon.bx05.Bx5GScreenProfile;
import onbon.bx05.area.TextCaptionBxArea;
import onbon.bx05.area.page.ImageFileBxPage;
import onbon.bx05.file.BxFileWriterListener;
import onbon.bx05.file.ProgramBxFile;
import onbon.bx05.utils.DisplayStyleFactory;
import onbon.bx05.utils.DisplayStyleFactory.DisplayStyle;

public class BxClientDemo implements BxFileWriterListener<Bx5GScreen>{



    public static void main(String[] args) throws Exception {

        //
        // SDK 初始化
        //Bx5GEnv.initial("log.properties");
        Bx5GEnv.initial("log.properties", 30000);

        //
        // 5Q 系列控制器
        // Bx5GScreenClient screen = new Bx5GScreenClient("MyScreen", new Bx5Q());

        //
        // 其它控制器
        // 创建 screen 对象，用于对控制器进行访问
        Bx5GScreenClient screen = new Bx5GScreenClient("MyScreen");

        //
        // 连接控制器
        // 其中, 192.168.88.199 为控制器的实际 IP，请根据实际情况填写。
        // 如你不知道控制器的 IP 是多少，请先使用 LEDSHOW TW 软件对控制器进行 IP 设置
        // 端口号默认为 5005
        if (!screen.connect("10.10.1.192", 5005)) {
            System.out.println("connect failed");
        }
		
		// 获取切换特技
		DisplayStyle[] styles = DisplayStyleFactory.getStyles().toArray(new DisplayStyle[0]);

		// 获取控制器参数
		Bx5GScreenProfile profile = screen.getProfile();

		// 创建节目
		ProgramBxFile program = new ProgramBxFile("P001", profile);
		// 无边框
		program.setFrameShow(false);
		// 移动速度
		program.setFrameSpeed(20);

		// 创建一个文本区域，待会要在文本区域加入文本页
		TextCaptionBxArea area = new TextCaptionBxArea(0, 0, profile.getWidth(), profile.getHeight(), profile);
		area.setFrameShow(false);
		
		List<File> list=org.xdemo.superutil.j2se.FileUtils.getFileSort("C:\\LED\\");
		
		// 再创建一个数据页，用于显示图片
		ImageFileBxPage iPage ;
		for(File f:list){
        	iPage = new ImageFileBxPage(f.getAbsolutePath());
    		// 调整特技方式
    		iPage.setDisplayStyle(styles[6]);
    		// 调整特技速度
    		iPage.setSpeed(5);
    		// 调整停留时间, 单位 10ms
    		iPage.setStayTime(400);
    		
    		area.addPage(iPage);
        	
		}
		
		program.addArea(area);
		
		List<ProgramBxFile> programs=new ArrayList<ProgramBxFile>();
		programs.add(program);
		
		screen.writePrograms(programs);
		
		screen.disconnect();
    }


    @Override
    public void fileWriting(Bx5GScreen bx6GScreen, String s, int i) {

    }

    @Override
    public void fileFinish(Bx5GScreen bx6GScreen, String s, int i) {

    }

    @Override
    public void progressChanged(Bx5GScreen bx6GScreen, String s, int i, int i1) {

    }

    @Override
    public void cancel(Bx5GScreen bx6GScreen, String s, Bx5GException e) {

    }

    @Override
    public void done(Bx5GScreen bx6GScreen) {

    }
}
