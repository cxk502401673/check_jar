import com.yuanwang.util.Config;
import com.yuanwang.util.MyWebConstant;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimerTest {
    public static void main(String args[]){




        Integer hour=Integer.valueOf(Config.getString(MyWebConstant.HOUR,"0"));
        Integer minute= Integer.valueOf(Config.getString(MyWebConstant.MINUTE,"0"));
        Integer second=Integer.valueOf(Config.getString(MyWebConstant.SECOND,"0"));
        Long hour_l=hour*60L*60L*1000L;
        Long minute_l=minute*60L*1000L;
        Long second_l=second*1000L;



        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                System.out.println("11232");
            }
        }, 0 ,  hour_l+ minute_l+second_l);

    }
}
