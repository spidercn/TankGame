package TankBase;

import javax.swing.*;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.security.cert.TrustAnchor;
import java.util.Scanner;

//主类，实现游戏的框架
public class TankGame extends JFrame {
    MyPanel mp=null;//显示屏

    Scanner scanner = new Scanner(System.in);
    //主函数入口函数
    public static void main(String[] args) {
        TankGame tankGame = new TankGame();
    }


    //构造器，设定游戏区域，确定绘图领域
    public TankGame(){
        System.out.println("请输入选择:1:新游戏 2:上局");
        String key=scanner.next();
        mp=new MyPanel(key);//绘图领域
        Thread thread=new Thread(mp);
        thread.start();
        this.add(mp);//面板游戏绘图区域
        //确定框架的大小
        this.setSize(1000,750);
        //键盘事件监听
        this.addKeyListener(mp);
        //当关闭页面，程序关闭
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //图形区域是否可见
        this.setVisible(true);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Recorder.keepRecord();
                System.exit(0);
            }
        });
    }
}
