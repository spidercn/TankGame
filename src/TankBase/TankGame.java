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


    //构造器，设定游戏区域，确定绘图区域
    public TankGame(){
        System.out.println("请输入选择:1:新游戏 2:上局");
        String key=scanner.next();
        mp=new MyPanel(key);//绘图区域
        Thread thread=new Thread(mp);
        //因为坦克的变化和子弹的变化需要绘制，因此将面板设置成线程
        thread.start();
        this.add(mp);//面板游戏绘图区域
        //确定框架的大小
        this.setSize(1300,950);
        //键盘事件监听
        this.addKeyListener(mp);
        //当关闭页面，程序关闭
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //图形区域是否可见
        this.setVisible(true);

        //游戏音乐设置
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Recorder.keepRecord();
                System.exit(0);
            }
        });
    }
}
