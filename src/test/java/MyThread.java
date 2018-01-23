import java.util.HashMap;
import java.util.Map;

public class MyThread extends Thread {


    private String name;
    private Map<String,Object> map;
    public MyThread(String name,Map<String,Object> map)
    {
        this.name = name;
        this.map = map;
    }
    public void run()
    {
        taskExec(name,map);
    }
    public static void main(String[] args)
    {

        for(int i=0;i<2;i++){
            Map<String,Object> map2=new HashMap<String,Object>();
            map2.put("key"+i,"v"+i);
            Thread thread2 = new MyThread(i+"",map2);
            thread2.start();
        }
        System.out.println("子线程开启");
        //获取man线程
        Thread main = Thread.currentThread();
        System.out.println(main.getName());
        main.interrupt();
        System.out.println("main线程已经退出了，但是不影响其他线程运行!");
    }


    private void taskExec(String name,Map<String,Object> map){
       for(int i=0;i<3;i++){
       // while(true){
            System.out.println(name);
            System.out.println(map.get("key"));
            System.out.println("---------------");
            try {
                Thread.sleep(1000l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}