package TankBase;

import java.util.Vector;

//自己的坦克
public class Hero extends Tank{
    //子弹
    Shot shot=null;
//    Vector<Shot> shots=new Vector<>();


    //构造器，确定我们的坦克的坐标
    public Hero(int x, int y) {
        super(x, y);
    }

    //发射子弹射击
    public void shotEnemyTank(){
        //控制连发最高数量
//        if(shots.size()==5){
//            return;
//        }
        //根据朝向发射子弹
        switch (this.getDirect()){
            case 0:
                shot=new Shot(getX()+20,getY(),0);
                break;
            case 1:
                shot=new Shot(getX()+60,getY()+20,1);
                break;
            case 2:
                shot=new Shot(getX()+20,getY()+60,2);
                break;
            case 3:
                shot=new Shot(getX(),getY()+20,3);
                break;
        }
        //将子弹加入到子弹集合中
//        shots.add(shot);
        //启动线程
        new Thread(shot).start();
    }
}
