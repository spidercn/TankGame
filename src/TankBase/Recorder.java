package TankBase;

import java.io.*;
import java.util.Vector;

@SuppressWarnings("all")

//记录上局坦克的位置信息
public class Recorder {

    private static int enemyTank=0;//记录击毁的坦克数量
    //读取文件
    private static FileWriter fileWriter=null;
    //字符缓冲流
    private static BufferedWriter bw=null;
    private static String recordFile="src\\myRecord.txt";//文件位置

    public static String getRecordFile() {
        return recordFile;
    }

    private static Vector<EnemyTank> enemyTanks=new Vector<>();

    private static BufferedReader br=null;
    private static Vector<Node> nodes=new Vector<>();

    public static Vector<Node> getNodesAndEnemyTankRec(){
        try {
            br=new BufferedReader(new FileReader(recordFile));
            enemyTank=Integer.parseInt(br.readLine());
            String line="";
            while ((line=br.readLine())!=null){
                String[] xyd=line.split(" ");
                Node node = new Node(Integer.parseInt(xyd[0]), Integer.parseInt(xyd[1]),
                        Integer.parseInt(xyd[2]));
                nodes.add(node);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关流
            try {
                if(br!=null) br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //将上局坦克的记录情况进行返回
        return nodes;
    }

    public static void setEnemyTanks(Vector<EnemyTank> enemyTanks) {
        Recorder.enemyTanks = enemyTanks;
    }

    public static int getEnemyTank() {
        return enemyTank;
    }

    public static void setEnemyTank(int enemies) {
        Recorder.enemyTank = enemies;
    }

    public static void addEneymyTank(){
        Recorder.enemyTank++;
    }

    //保存坦克信息
    public static void keepRecord(){
        try {
            bw=new BufferedWriter(new FileWriter(recordFile));
            bw.write(enemyTank+"\r\n");
            for (int i=0;i<enemyTanks.size();i++){
                EnemyTank enemyTank = enemyTanks.get(i);
                if(enemyTank.isLive){
                    String record=enemyTank.getX()+" "+enemyTank.getY()+" "+enemyTank.getDirect();
                    bw.write(record);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(bw!=null) bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void removeTxt(){
        File file = new File(recordFile);
        if (file.exists()){
            file.delete();
        }
    }
}
