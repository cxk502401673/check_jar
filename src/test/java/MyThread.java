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
//        Map<String,Object> map1=new HashMap<String,Object>();
//        map1.put("key","v1");
//        Thread thread1 = new MyThread("world",map1);
//        thread1.start();
        for(int i=0;i<100;i++){
            Map<String,Object> map2=new HashMap<String,Object>();
            map2.put("key"+i,"v"+i);
            Thread thread2 = new MyThread(i+"",map2);
            thread2.start();
        }

    }


    private void taskExec(String name,Map<String,Object> map){
        System.out.println(name);
        System.out.println(map.get("key"));
        System.out.println("---------------");


    }
}