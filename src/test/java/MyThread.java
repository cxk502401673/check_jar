import com.yuanwang.util.DBCPUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;


public class MyThread extends Thread {


    private String name;
    private HashMap<String,Object> map;
    public MyThread(String name,HashMap<String,Object> map)
    {
        this.name = name;
        this.map = map;
    }
    public void run()
    {
        try {
            taskExec(name,map);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void main(String[] args)
    {

        for(int i=0;i<10;i++){
            HashMap<String,Object> map2=new HashMap<String, Object>();
            map2.put("key"+i,"value"+i);
            Thread thread2 = new MyThread(i+"",map2);
            thread2.start();
        }
//        System.out.println("子线程开启");
//        //获取man线程
//        Thread main = Thread.currentThread();
//        System.out.println(main.getName());
//        main.interrupt();
//        System.out.println("main线程已经退出了，但是不影响其他线程运行!");
    }


    private      void taskExec(String name,HashMap<String,Object> map) {
//        HashMap<String,Object> m=new HashMap<String, Object>();
//        m.put("x","x");
//        m.put("x","xx");
//        m.put("x","xxx");
//        Thread now = Thread.currentThread();
//
//        System.out.println("我是线程"+now.getName()+"我的map是"+map.hashCode()+"name:"+name+";value:"+map.get("key"+name)+" x:"+m.get("x"));


        //******************************************************************************///
        try {
            Connection connection = DBCPUtil.getConnection() ;

            String sql = "insert into wx_account(accountemail,accountname) values(?,?); ";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1,name);
            st.setString(2,name);
            st.execute();
            connection.commit();
            st.close();

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        /////******************************************************************************///
//        Connection  conn= DBCPUtil.getLowConnection();
//        Statement  st = conn.createStatement();
//        String sql = "select * from wx_account where id="+name;
//       ResultSet rs= st.executeQuery(sql);
//        //5）处理结果
//        while(rs.next()){
//            System.out.print(rs.getObject(1)+"\t");
//            System.out.print(rs.getObject(2)+"\t");
////            System.out.print(rs.getObject(3)+"\t");
////            System.out.print(rs.getObject(4)+"\t");
////            System.out.print(rs.getObject(5)+"\t");
////            System.out.print(rs.getObject(6)+"\t"); //共输出要显示的字段名信息
//            System.out.println("over");
//        }
//        st.close();
//        conn.close();

        /////******************************************************************************///






    }
}