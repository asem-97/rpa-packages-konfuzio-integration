import com.automationanywhere.botcommand.GetCategory;
import com.automationanywhere.botcommand.data.impl.StringValue;
import org.testng.annotations.Test;

public class TestGetCategory {

    //if category doesn't exist will return "null" as the category name
    @Test
    public void TestGetCategory() {
        try {
            GetCategory g = new GetCategory();
            StringValue category= g.action(new Long(16267), "m.fararjeh@comptechco.com", "bZKgihqRzi68t6B");
            System.out.println(category);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

}
