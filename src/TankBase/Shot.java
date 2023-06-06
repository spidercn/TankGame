package TankBase;

//子弹类
public class Shot implements Runnable{
    int x;//子弹横坐标
    int y;//子弹纵坐标
    int direct;//子弹方向，根据坦克的方向进行确定
    int speed=2;//子弹速度控制
    boolean isLive=true;//子弹是否击中目标或者还在图的区域内

    //构造器，确定子弹的横纵坐标和朝向
    public Shot(int x, int y, int direct) {
        this.x = x;
        this.y = y;
        this.direct = direct;
    }

    //子弹属性的get-set方法
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDirect() {
        return direct;
    }

    public void setDirect(int direct) {
        this.direct = direct;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    //重写run方法，实现子弹的射击
    @Override
    public void run() {//射击
        while (true){
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            switch (direct){
                case 0:
                    y-=speed;
                    break;
                case 1:
                    x+=speed;
                    break;
                case 2:
                    y+=speed;
                    break;
                case 3:
                    x-=speed;
            }

            //确定进程退出标志
            if(!(x>=0&&x<=1000&&y>=0&&y<=750&&isLive)){
                isLive=false;
                break;
            }
        }
    }
}
