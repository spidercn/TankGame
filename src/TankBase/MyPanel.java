package TankBase;


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.security.Key;
import java.util.Vector;

@SuppressWarnings("all")
//绘图区域

public class MyPanel extends JPanel implements KeyListener, Runnable {
    Hero hero = null;//自己的坦克对象
//    EnemyTank enemyTank = null;//敌人坦克对象
    Vector<EnemyTank> enemyTanks = new Vector<>();//敌人坦克集合
    Vector<Bomb> bombs = new Vector<>();//实现爆炸
    int enemyTankSize = 3;//确定坦克的数量
    Vector<Node> nodes = new Vector<>();
    //爆炸画面的实现
    Image image1 = null;
    Image image2 = null;
    Image image3 = null;


    //构造方法，实例化自己的坦克，准备敌人坦克，准备子弹
    public MyPanel(String key) {
        File file = new File(Recorder.getRecordFile());
        if (file.exists()) {
            nodes = Recorder.getNodesAndEnemyTankRec();
        } else {
            System.out.println("文件不存在，开启新游戏");
            key = "1";
        }

        Recorder.setEnemyTanks(enemyTanks);


        hero = new Hero(800, 500);

        switch (key) {
            case "1":
                for (int i = 0; i < enemyTankSize; i++) {
                    EnemyTank enemyTank = new EnemyTank(100 * (i + 1), 0);
                    enemyTank.setEnemyTanks(enemyTanks);
                    enemyTank.setDirect(2);
                    new Thread(enemyTank).start();

                    Shot shot = new Shot(enemyTank.getX() + 20, enemyTank.getY() + 60, enemyTank.getDirect());
                    enemyTank.shots.add(shot);
                    new Thread(shot).start();
                    enemyTanks.add(enemyTank);
                }
                break;
            case "2":
                for (int i = 0; i < nodes.size(); i++) {
                    Node node = nodes.get(i);
                    EnemyTank enemyTank = new EnemyTank(node.getX(), node.getY());
                    enemyTank.setEnemyTanks(enemyTanks);
                    enemyTank.setDirect(node.getDirect());
                    new Thread(enemyTank).start();

                    Shot shot = new Shot(enemyTank.getX() + 20, enemyTank.getY() + 60, enemyTank.getDirect());
                    enemyTank.shots.add(shot);
                    new Thread(shot).start();
                    enemyTanks.add(enemyTank);
                }
                break;
            default:
                System.out.println("输入有误！");
                break;
        }

        //初始化图片对象
//        image1 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_1.gif"));
//        image2 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_2.gif"));
//        image3 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_3.gif"));
//        image1 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_1.gif"));
//        image2 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_2.gif"));
//        image3 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_3.gif"));
        new AePlayWave("src\\111.wav").start();
    }

    public void showInfo(Graphics g) {
        g.setColor(Color.BLACK);
        Font font = new Font("宋体", Font.BOLD, 30);
        g.setFont(font);

        g.drawString("累计击毁坦克数:", 1020, 30);
        drawTank(1020, 60, g, 0, 0);
        g.setColor(Color.BLACK);
        g.drawString(Recorder.getEnemyTank() + "".toString(), 1080, 100);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.fillRect(0, 0, 1000, 750);//填充矩形，默认黑色

        showInfo(g);

        if (hero != null && hero.isLive)
            drawTank(hero.getX(), hero.getY(), g, hero.getDirect(), 0);//画自己的坦克

        //自己的子弹不是空的或者满足存活条件，画出自己的子弹
        if (hero.shot != null && hero.shot.isLive) {
            //g.fill3DRect(hero.shot.x,hero.shot.y,3,3,false);
            g.draw3DRect(hero.shot.x, hero.shot.y, 1, 1, false);
        }

        //画自己的子弹
//        for (int i = 0; i < hero.shots.size(); i++) {
//            Shot shot = hero.shots.get(i);
//            if (shot != null && shot.isLive) {
//                //g.fill3DRect(hero.shot.x,hero.shot.y,3,3,false);
//                g.draw3DRect(shot.x, shot.y, 1, 1, false);
//            } else {
//                hero.shots.remove(shot);
//            }
//        }

        //根据life值实现不同爆炸画面
//        for (int i = 0; i < bombs.size(); i++) {
//            Bomb bomb = bombs.get(i);
//
//            if (bomb.life > 6) {
//                g.drawImage(image1, bomb.x, bomb.y, 60, 60, this);
//            } else if (bomb.life > 3) {
//                g.drawImage(image2, bomb.x, bomb.y, 60, 60, this);
//            } else {
//                g.drawImage(image3, bomb.x, bomb.y, 60, 60, this);
//            }
//            bomb.lifeDown();
//            if (bomb.life == 0) {
//                bombs.remove(bomb);
//            }
//        }

        //击中坦克
        for (int i = 0; i < enemyTanks.size(); i++) {
            EnemyTank enemyTank = enemyTanks.get(i);
            if (enemyTank.isLive) {
                drawTank(enemyTank.getX(), enemyTank.getY(), g, enemyTank.getDirect(), 1);
                for (int j = 0; j < enemyTank.shots.size(); j++) {
                    Shot shot = enemyTank.shots.get(j);
                    if (shot.isLive) {
                        g.draw3DRect(shot.x, shot.y, 1, 1, false);
                        this.repaint();
                    } else {
                        enemyTank.shots.remove(shot);
                    }
                }
            }
        }
    }


    //绘制坦克---------g 画笔，direct 方向，type 坦克类型
    public void drawTank(int x, int y, Graphics g, int direct, int type) {
        switch (type) {
            case 0:
                g.setColor(Color.cyan);
                break;
            case 1:
                g.setColor(Color.YELLOW);
                break;
            default:
                break;
        }
        //根据方向绘制坦克
        switch (direct) {
            case 0://向上
                g.fill3DRect(x, y, 10, 60, false);
                g.fill3DRect(x + 30, y, 10, 60, false);
                g.fill3DRect(x + 10, y + 10, 20, 40, false);
                g.fillOval(x + 10, y + 20, 20, 20);
                g.drawLine(x + 20, y + 30, x + 20, y);
                break;
            case 1://向右
                g.fill3DRect(x, y, 60, 10, false);//上
                g.fill3DRect(x, y + 30, 60, 10, false);//下
                g.fill3DRect(x + 10, y + 10, 40, 20, false);
                g.fillOval(x + 20, y + 10, 20, 20);
                g.drawLine(x + 30, y + 20, x + 60, y + 20);
                break;
            case 2://向下
                g.fill3DRect(x, y, 10, 60, false);
                g.fill3DRect(x + 30, y, 10, 60, false);
                g.fill3DRect(x + 10, y + 10, 20, 40, false);
                g.fillOval(x + 10, y + 20, 20, 20);
                g.drawLine(x + 20, y + 30, x + 20, y + 60);
                break;
            case 3:
                g.fill3DRect(x, y, 60, 10, false);//上
                g.fill3DRect(x, y + 30, 60, 10, false);//下
                g.fill3DRect(x + 10, y + 10, 40, 20, false);
                g.fillOval(x + 20, y + 10, 20, 20);
                g.drawLine(x + 30, y + 20, x, y + 20);
                break;
            default:
                break;
        }
    }


    public void hitHero() {
        for (int i = 0; i < enemyTanks.size(); i++) {
            EnemyTank enemyTank = enemyTanks.get(i);
            for (int j = 0; j < enemyTank.shots.size(); j++) {
                Shot shot = enemyTank.shots.get(j);
                if (hero.isLive && shot.isLive) {
                    hitTank(shot, hero);
                }
            }
        }
    }

    public void hitEneymyTank() {
        if (hero.shot != null && hero.shot.isLive) {//当我的子弹还存活

            //遍历敌人所有的坦克
            for (int i = 0; i < enemyTanks.size(); i++) {
                EnemyTank enemyTank = enemyTanks.get(i);
                hitTank(hero.shot, enemyTank);
            }

        }
    }

    //攻击坦克，根据子弹的坐标和坦克的坐标进行判断是否击中坦克
    public void hitTank(Shot s, Tank tank) {
        switch (tank.getDirect()) {
            case 0://上
            case 2://下
                if (s.x > tank.getX() && s.y < tank.getY() + 40
                        && s.y > tank.getY() && s.y < tank.getY() + 60) {
                    s.isLive = false;
                    tank.isLive = false;
                    if (tank instanceof EnemyTank) {
                        Recorder.addEneymyTank();
                        enemyTanks.remove(tank);
                    }
                    Bomb bomb = new Bomb(tank.getX(), tank.getY());
                    bombs.add(bomb);
                }
                break;
            case 1://右
            case 3://左
                if (s.x > tank.getX() && s.x < tank.getX() + 60
                        && s.y > tank.getY() && s.y < tank.getY() + 40) {
                    s.isLive = false;
                    tank.isLive = false;
                    if (tank instanceof EnemyTank) {
                        Recorder.addEneymyTank();
                        enemyTanks.remove(tank);
                    }
                    Bomb bomb = new Bomb(tank.getX(), tank.getY());
                    bombs.add(bomb);
                }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (hero.shot != null && hero.shot.isLive) {
            for (int i = 0; i < enemyTanks.size(); i++) {
                EnemyTank enemyTank = enemyTanks.get(i);
                hitTank(hero.shot, enemyTank);
            }
        }
    }

    //键盘事件响应
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            hero.setDirect(0);
            if (hero.getY() > 0) hero.moveUp();
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            hero.setDirect(2);
            if (hero.getX() < 750) hero.moveDown();
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            hero.setDirect(3);
            if (hero.getX() + 60 < 1000) hero.moveLeft();
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            hero.setDirect(1);
            if (hero.getX() > 0) hero.moveRight();
        }

        //攻击键J
        if (e.getKeyCode() == KeyEvent.VK_J) {
//            if(hero.shot==null||!hero.shot.isLive){
//                hero.shotEnemyTank();
//            }
            hero.shotEnemyTank();
        }
        this.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            hitEneymyTank();
            hitHero();
            this.repaint();
        }

    }
}
