import com.automationanywhere.botcommand.SetCredentials;
import com.automationanywhere.botcommand.UploadFile;
import org.testng.annotations.Test;

public class TestSetCredentials {
    @Test
    public void action(){
        SetCredentials s=new SetCredentials();
        try{
            s.action("asem","dreibati");
            UploadFile  f=new UploadFile();
                //f.action("C:\\Users\\hp\\Downloads\\DP_2023_LEC05.pdf",Long.valueOf(3));
            System.out.println("done");
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }
}
