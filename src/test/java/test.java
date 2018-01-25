import com.yuanwang.util.ShellUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class test {
    public static void main(String[] args)
    {


       // SoftStatus();
    }
    public static void SoftStatus(){



        try{

            System.out.println( ShellUtils.exec("/Users/chenxiaokai/Documents/workspace/check/target/find.sh"));


        }catch(Exception e){
            System.out.println(e.toString());
        }


    }
}
