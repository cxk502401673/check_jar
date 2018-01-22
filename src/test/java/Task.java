import java.util.TimerTask;

public class Task extends TimerTask {
    private String tranType;
    private String reverPurFlag;
    public Task(String Type,String flag){

        this.tranType=Type;
        this.reverPurFlag=flag;
    }
    public Task(){

        super();
    }


    public void run() {

        System.out.println("ttttttt");

    }
}
