package com.bluebirdme.mes.ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.text.html.HTMLEditorKit;

import org.jsoup.nodes.Document;
import org.xdemo.superutil.j2se.MathUtils;
import org.xdemo.superutil.j2se.StringUtils;

import com.bluebirdme.mes.resources.Prop;
import com.bluebirdme.mes.resources.Resources;
import com.bluebirdme.mes.resources.Utils;
import com.bluebirdme.mes.serial.SerialPortUtils;
import com.bluebirdme.mes.serial.callback.AbstractReadCallback;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * 主界面
 *
 * @author Goofy
 * @Date 2017年3月13日 上午10:26:55
 */
public class Main {
    private JFrame frmmes;
    static Resources res = new Resources();
    // 条码框
    private static JTextField barcode;
    // 总重
    private static JTextField totalWeight;
    //皮重
    private static JTextField tareWeight;
    //净重
    private static JTextField grossWeight;
    private static final JEditorPane EDITOR_PANE = new JEditorPane("text/html", "");
    private static JLabel tipInfo;
    public static Prop prop = new Prop();

    static Color RED = Color.RED;
    static Color GREEN = Color.GREEN;

    // 地磅COM口
    static String COMPORT = "";

    static String SERVER_ADDR = "";

    // 重量是否稳定
    private static boolean isOK = false;

    // 稳定的次数
    private static int keepOkTimes = 0;

    // 超过5次，视为稳定
    private static final int okTimes = 3;

    // 有效的条码
    private static boolean validCode = false;

    private static String lastTimeWeight = "";

    // 重量读取的回调方法
    static AbstractReadCallback callback;

    Document doc;
    private static JTextField carrier;
    private JLabel label_2;
    private JLabel tareWeightLabel;
    private JLabel label_4;
    private static JButton saveButton;
    private static JTextField codeScan;
    private static JTextField minWeight;
    private static JTextField maxWeight;
    private JLabel label_3;
    private static JComboBox grade;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    COMPORT = prop.getProp(Prop.KEY_PORT);
                    SERVER_ADDR = prop.getProp(Prop.KEY_SERVER_ADDR);

                    if (StringUtils.isBlank(COMPORT)) {
                        res.error("未配置COM口");
                        return;
                    }
                    if (StringUtils.isBlank(SERVER_ADDR)) {
                        res.error("未配置服务器地址");
                        return;
                    }
                    Main window = new Main();
                    window.frmmes.setVisible(true);

                    callback = (AbstractReadCallback) Class.forName(prop.getProp(Prop.KEY_DEVICE_CALLBACK)).newInstance();
                    new SerialPortUtils().open().read(callback);
                    new StableWeight().start();
                } catch (Exception e) {
                    e.printStackTrace();
                    res.error(StringUtils.isBlank(e.getMessage()) ? "空指针异常" : e.getMessage());
                    System.exit(0);
                }
            }
        });
    }

    /**
     * Create the application.
     *
     * @throws IOException
     */
    public Main() throws IOException {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     *
     * @throws IOException
     */
    private void initialize() throws IOException {
        frmmes = new JFrame();
        frmmes.setIconImage(Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/com/bluebirdme/mes/resources/logo_z.png")));
        frmmes.setTitle("恒石纤维基业MES系统称重");
        frmmes.setBounds(100, 100, 1200, 600);
        frmmes.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frmmes.setAlwaysOnTop(true);
        frmmes.setExtendedState(JFrame.MAXIMIZED_BOTH); //最大化
//		frmmes.setAlwaysOnTop(true);    //总在最前面
//		frmmes.setResizable(false);    //不能改变大小
//		frmmes.setUndecorated(true);    //不要边框

        // frmmes.setLocationRelativeTo(null);
        // frmmes.setExtendedState(JFrame.MAXIMIZED_BOTH);

        JLabel scanText = new JLabel("请扫码");
        scanText.setHorizontalAlignment(SwingConstants.LEFT);
        scanText.setFont(new Font("微软雅黑", Font.BOLD, 40));

        barcode = new JTextField();
        barcode.setEditable(false);
        barcode.setBackground(Color.LIGHT_GRAY);

        barcode.setForeground(Color.RED);
        barcode.setHorizontalAlignment(SwingConstants.CENTER);
        barcode.setFont(new Font("微软雅黑", Font.BOLD, 35));

        totalWeight = new JTextField();
        totalWeight.setEditable(false);
        totalWeight.setText("0");
        totalWeight.setHorizontalAlignment(SwingConstants.CENTER);
        totalWeight.setForeground(Color.BLUE);
        totalWeight.setFont(new Font("微软雅黑", Font.BOLD, 35));

        HTMLEditorKit hk = new HTMLEditorKit();
        hk.getStyleSheet().addRule("table{width:100%;font-size:14px;table-layout:fixed;}");
        hk.getStyleSheet().addRule("td{text-align:center;color:red;font-weight:bold;width:12%;}");
        EDITOR_PANE.setBorder(null);
        EDITOR_PANE.setEditorKit(hk);
        EDITOR_PANE.setEditable(false);

        tipInfo = new JLabel("请扫卷条码");
        tipInfo.setHorizontalAlignment(SwingConstants.CENTER);
        tipInfo.setBackground(new Color(0, 128, 128));
        tipInfo.setForeground(new Color(0, 128, 0));
        tipInfo.setFont(new Font("微软雅黑", Font.BOLD, 35));

        JLabel label = new JLabel("卷条码");
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setFont(new Font("微软雅黑", Font.BOLD, 40));

        JLabel label_1 = new JLabel("介质条码");
        label_1.setFont(new Font("微软雅黑", Font.BOLD, 40));

        carrier = new JTextField();
        carrier.setEditable(false);
        carrier.setBackground(Color.LIGHT_GRAY);
        carrier.setHorizontalAlignment(SwingConstants.CENTER);
        carrier.setForeground(Color.RED);
        carrier.setFont(new Font("微软雅黑", Font.BOLD, 35));

        label_2 = new JLabel("毛重");
        label_2.setForeground(new Color(0, 0, 255));
        label_2.setHorizontalAlignment(SwingConstants.CENTER);
        label_2.setFont(new Font("微软雅黑", Font.BOLD, 40));

        tareWeightLabel = new JLabel("皮重");
        tareWeightLabel.setForeground(new Color(160, 82, 45));
        tareWeightLabel.setHorizontalAlignment(SwingConstants.CENTER);
        tareWeightLabel.setFont(new Font("微软雅黑", Font.BOLD, 40));

        tareWeight = new JTextField();
        tareWeight.setEditable(false);
        tareWeight.setText("0");
        tareWeight.setHorizontalAlignment(SwingConstants.CENTER);
        tareWeight.setForeground(new Color(160, 82, 45));
        tareWeight.setFont(new Font("微软雅黑", Font.BOLD, 35));

        label_4 = new JLabel("净重");
        label_4.setForeground(new Color(0, 128, 0));
        label_4.setHorizontalAlignment(SwingConstants.CENTER);
        label_4.setFont(new Font("微软雅黑", Font.BOLD, 40));

        grossWeight = new JTextField();
        grossWeight.setEditable(false);
        grossWeight.setText("0");
        grossWeight.setHorizontalAlignment(SwingConstants.CENTER);
        grossWeight.setForeground(new Color(0, 128, 0));
        grossWeight.setFont(new Font("微软雅黑", Font.BOLD, 35));

        saveButton = new JButton("保 存", new ImageIcon(getClass().getResource("/com/bluebirdme/mes/resources/ok.png")));
        saveButton.setBackground(new Color(192, 192, 192));
        saveButton.setUI(new BasicButtonUI());// 恢复基本视觉效果
        saveButton.setMargin(new Insets(0, 0, 0, 0));// 按钮内容与边框距离
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveWeight();
            }
        });
        saveButton.setFont(new Font("微软雅黑", Font.BOLD, 40));

        codeScan = new JTextField();
        codeScan.setHorizontalAlignment(SwingConstants.CENTER);
        codeScan.setForeground(Color.RED);
        codeScan.setFont(new Font("微软雅黑", Font.BOLD, 35));
        codeScan.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                saveButton.setEnabled(false);
                // 回车键
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    final String codeScanText = codeScan.getText().replaceAll(" |;", "");
                    if (StringUtils.isBlank(codeScanText)) {
                        return;
                    }
                    String roll = barcode.getText();
                    String carr = carrier.getText();
                    codeScan.setText("");
                    tipInfo.setText("");
                    if (codeScanText.startsWith("R")) {
                        clearForm();
                        EDITOR_PANE.setText("加载中，请稍后");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    tipInfo.setText("");
                                    JsonObject json = res.getBarCodeInfo(SERVER_ADDR, codeScanText);
                                    if (json.get("ERROR").getAsBoolean()) {
                                        EDITOR_PANE.setText(json.get("MSG").getAsString());
                                        tipInfo.setText(json.get("MSG").getAsString());
                                        barcode.setText("");
                                    } else {
                                        if (!json.get("ROLL").getAsJsonObject().get("ROLLAUTOWEIGHT").isJsonNull()) {
                                            grossWeight.setText(json.get("ROLL").getAsJsonObject().get("ROLLAUTOWEIGHT").getAsString());
                                        }
                                        barcode.setText(codeScanText);
                                        Map<String, String> map = new HashMap<String, String>();
                                        map.put("consumer", json.get("CONSUMER").getAsJsonObject().get("CONSUMERSIMPLENAME").getAsString());
                                        map.put("order", json.get("ORDER").getAsJsonObject().get("SALESORDERSUBCODE").getAsString());
                                        map.put("batch", json.get("CODE").getAsJsonObject().get("BATCHCODE").getAsString());
                                        map.put("deliveryDate", json.get("ORDER").getAsJsonObject().get("DELIVERYTIME").getAsString().substring(0, 10));
                                        map.put("model", json.get("PRODUCT").getAsJsonObject().get("PRODUCTMODEL").getAsString());
                                        map.put("factoryProductName", json.get("PRODUCT").getAsJsonObject().get("FACTORYPRODUCTNAME").getAsString());
                                        map.put("comsumerProductName", json.get("PRODUCT").getAsJsonObject().get("CONSUMERPRODUCTNAME").getAsString());

                                        if (!json.get("QUALITY").isJsonNull()) {
                                            map.put("grade", json.get("QUALITY").getAsString());
                                        } else {
                                            map.put("grade", "尚未判级");
                                        }
                                        EDITOR_PANE.setText(Utils.generateToString("info.ftl", map));
                                        JsonElement obj = json.get("PRODUCT").getAsJsonObject().get("MINWEIGHT");
                                        if (obj.isJsonNull()) {
                                            clearForm();
                                            warn("未设置产品重量偏差");
                                            return;
                                        }
                                        minWeight.setText(obj.getAsString());
                                        obj = json.get("PRODUCT").getAsJsonObject().get("MAXWEIGHT");
                                        if (obj.isJsonNull()) {
                                            clearForm();
                                            warn("未设置产品重量偏差");
                                            return;
                                        }
                                        maxWeight.setText(json.get("PRODUCT").getAsJsonObject().get("MAXWEIGHT").getAsString());
                                        if (json.get("ROLL").getAsJsonObject().get("ROLLQUALITYGRADECODE").isJsonNull()) {
                                            grade.setSelectedItem("A");
                                        } else {
                                            grade.setSelectedItem(json.get("ROLL").getAsJsonObject().get("ROLLQUALITYGRADECODE").getAsString());
                                        }


                                        warn("请扫载具条码");
										
										/*isOK = false;
										keepOkTimes = 0;*/
                                    }
                                } catch (Exception e) {
                                    validCode = false;
                                    e.printStackTrace();
                                    warn("条码不存在或者查询错误");
                                }
                            }
                        }).start();

                    } else {
                        carr = codeScanText;
                        carrier.setText("");
                        tareWeight.setText("0");
                        grossWeight.setText("0");
                        if (StringUtils.isBlank(barcode.getText())) {
                            tip("请先扫描卷条码");
                            return;
                        }
                        JsonObject json = res.getTare(SERVER_ADDR, roll, carr);
                        if (json != null) {
                            if (json.get("ERROR").getAsBoolean()) {
                                tipInfo.setText(json.get("MSG").getAsString());
                                validCode = false;
                                isOK = false;
                                keepOkTimes = 0;
                                return;
                            } else {
                                tareWeight.setText(json.get("WEIGHT").getAsString());
                                carrier.setText(carr);
                                validCode = true;
                                isOK = false;
                                keepOkTimes = 0;
                                tip("正在获取重量");
                            }
                        } else {
                            validCode = false;
                            isOK = false;
                            keepOkTimes = 0;
                        }
                    }
                }
            }
        });

        JLabel label_5 = new JLabel("下限");
        label_5.setForeground(Color.ORANGE);
        label_5.setHorizontalAlignment(SwingConstants.CENTER);
        label_5.setFont(new Font("微软雅黑", Font.BOLD, 40));

        minWeight = new JTextField();
        minWeight.setText("0");
        minWeight.setHorizontalAlignment(SwingConstants.CENTER);
        minWeight.setForeground(Color.ORANGE);
        minWeight.setFont(new Font("微软雅黑", Font.BOLD, 35));
        minWeight.setEditable(false);

        maxWeight = new JTextField();
        maxWeight.setText("0");
        maxWeight.setHorizontalAlignment(SwingConstants.CENTER);
        maxWeight.setForeground(Color.ORANGE);
        maxWeight.setFont(new Font("微软雅黑", Font.BOLD, 35));
        maxWeight.setEditable(false);

        JLabel label_6 = new JLabel("上限");
        label_6.setForeground(Color.ORANGE);
        label_6.setHorizontalAlignment(SwingConstants.CENTER);
        label_6.setFont(new Font("微软雅黑", Font.BOLD, 40));

        label_3 = new JLabel("质量");
        label_3.setForeground(Color.RED);
        label_3.setHorizontalAlignment(SwingConstants.CENTER);
        label_3.setFont(new Font("微软雅黑", Font.BOLD, 40));

        String[] x = res.getGrade(SERVER_ADDR);
        grade = new JComboBox(x);
        grade.setForeground(Color.RED);
        grade.setFont(new Font("微软雅黑", Font.BOLD, 40));
        grade.setRenderer(new DefaultListCellRenderer() {
            {
                setHorizontalAlignment(DefaultListCellRenderer.CENTER);
            }
        });


        GroupLayout groupLayout = new GroupLayout(frmmes.getContentPane());
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.TRAILING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addComponent(EDITOR_PANE, GroupLayout.DEFAULT_SIZE, 1172, Short.MAX_VALUE)
                                        .addComponent(tipInfo, GroupLayout.DEFAULT_SIZE, 1172, Short.MAX_VALUE)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addGap(67)
                                                .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                                                        .addComponent(label_5, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(label_2, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                                        .addGroup(groupLayout.createSequentialGroup()
                                                                .addComponent(totalWeight, GroupLayout.PREFERRED_SIZE, 170, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                                                .addComponent(tareWeightLabel, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                                                .addComponent(tareWeight, GroupLayout.PREFERRED_SIZE, 170, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                                                .addComponent(label_4, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                                                .addComponent(grossWeight, GroupLayout.PREFERRED_SIZE, 170, GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(groupLayout.createSequentialGroup()
                                                                .addComponent(minWeight, GroupLayout.PREFERRED_SIZE, 170, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                                                .addComponent(label_6, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                                                .addComponent(maxWeight, GroupLayout.PREFERRED_SIZE, 170, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                                                .addComponent(label_3, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                                                .addComponent(grade, GroupLayout.PREFERRED_SIZE, 170, GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18)
                                                                .addComponent(saveButton, GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)))
                                                .addGap(48))
                                        .addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
                                                .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                                                        .addGroup(groupLayout.createSequentialGroup()
                                                                .addComponent(label_1, GroupLayout.PREFERRED_SIZE, 170, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                                .addComponent(carrier, GroupLayout.DEFAULT_SIZE, 986, Short.MAX_VALUE))
                                                        .addGroup(groupLayout.createSequentialGroup()
                                                                .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
                                                                        .addComponent(scanText, Alignment.LEADING, 0, 0, Short.MAX_VALUE)
                                                                        .addComponent(label, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE))
                                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                                                        .addComponent(barcode, GroupLayout.DEFAULT_SIZE, 986, Short.MAX_VALUE)
                                                                        .addComponent(codeScan, GroupLayout.DEFAULT_SIZE, 986, Short.MAX_VALUE))))
                                                .addGap(12)))
                                .addContainerGap())
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(scanText)
                                        .addComponent(codeScan, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addComponent(label)
                                        .addComponent(barcode, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(18)
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addComponent(label_1, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(carrier, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE))
                                .addGap(18)
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addGap(18)
                                                .addComponent(label_2, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addGap(20)
                                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                                        .addComponent(totalWeight, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(tareWeightLabel, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(tareWeight, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(label_4, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(grossWeight, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE))))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
                                        .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                                .addComponent(label_5, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(minWeight, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(label_6, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(maxWeight, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(label_3, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE, false)
                                                .addComponent(grade, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(saveButton, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(tipInfo, GroupLayout.PREFERRED_SIZE, 59, GroupLayout.PREFERRED_SIZE)
                                .addGap(10)
                                .addComponent(EDITOR_PANE, GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                                .addContainerGap())
        );
        frmmes.getContentPane().setLayout(groupLayout);
    }

    private static void saveWeight() {
        saveButton.setEnabled(false);
        if (!isOK) {
            warn("重量尚未稳定，请稍后");
            return;
        }
        if (barcode.getText().equals("") || totalWeight.getText().equals("")) {
            warn("条码号和重量都不能为空");
            return;
        }
        if (Double.parseDouble(totalWeight.getText()) == 0D) {
            warn("重量不能为0");
            return;
        }
        tip("正在保存...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 保存重量
                    res.saveWeight(SERVER_ADDR, barcode.getText(), MathUtils.add(Double.parseDouble(grossWeight.getText()), 0, 2), grade.getSelectedItem().toString());
                    tip("保存成功，请扫新条码");
                    Thread.sleep(300L);
                    clearForm();
                } catch (Exception e2) {
                    e2.printStackTrace();
                    warn("保存失败");
                }
            }
        }).start();
    }

    static void clearForm() {
        EDITOR_PANE.setText("");
        barcode.setText("");
        carrier.setText("");
        totalWeight.setText("0");
        tareWeight.setText("0");
        grossWeight.setText("0");
        validCode = false;
        keepOkTimes = 0;
        isOK = false;
        minWeight.setText("");
        maxWeight.setText("");
        tip("请扫卷条码");
    }

    /**
     * 提示信息
     *
     * @param text 文字
     */
    static void tip(String text) {
        tipInfo.setForeground(GREEN);
        tipInfo.setText(text);
    }

    /**
     * 警告
     *
     * @param text
     */
    static void warn(String text) {
        tipInfo.setForeground(RED);
        tipInfo.setText(text);
    }

    static class StableWeight extends Thread {

        @Override
        public void run() {
            while (true) {
                try {
                    sleep(1000L);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                codeScan.requestFocus(true);
//				String result = null;
//				try {
//					result = "1000";
//				} catch (Exception e1) {
//					e1.printStackTrace();
//				}
                String result = callback.getResult();
                if (!StringUtils.isBlank(barcode.getText()) && !StringUtils.isBlank(carrier.getText()) && validCode) {
                    try {
                        totalWeight.setText(MathUtils.add(Double.parseDouble(result), 0D, 2) + "");
                        Double total = Double.parseDouble(totalWeight.getText());
                        Double tare = Double.parseDouble(tareWeight.getText());
                        Double gross = MathUtils.sub(total, tare, 2);
                        grossWeight.setText(gross + "");
                        if (result.equals(lastTimeWeight)) {
                            if (++keepOkTimes >= okTimes) {
                                isOK = true;
                                keepOkTimes = okTimes;// 防止一直放在上面不拿下去，可能自增超出整型范围
                                //saveWeight();
                                saveButton.setEnabled(true);
                                tip("重量已稳定，请保存");
                                if (new Double(grossWeight.getText()) > new Double(maxWeight.getText()) || new Double(grossWeight.getText()) < new Double(minWeight.getText())) {
                                    warn("重量已稳定，但超限");
                                }
                            }
                        } else {
                            lastTimeWeight = result;
                            isOK = false;
                            keepOkTimes = 0;
                            saveButton.setEnabled(false);
                        }
                    } catch (Exception e) {
                    }
                } else {
                    totalWeight.setText(MathUtils.add(Double.parseDouble(result), 0D, 2) + "");
                }
            }
        }
    }
}
