import com.automationanywhere.botcommand.GetCategory;
import com.automationanywhere.botcommand.GetDocumentDetails;
import com.automationanywhere.botcommand.UploadFile;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.DictionaryValue;
import com.automationanywhere.botcommand.data.impl.NumberValue;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.data.impl.TableValue;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class TestUploadFile {

    //if function doesn't upload the file will return -1 as the document id
    @Test
    public void TestUploadFile() {
        UploadFile f = new UploadFile();
        try {
            NumberValue id=f.action("C:\\Users\\hp\\Downloads\\DP_2023_LEC05.pdf", Long.valueOf(12442), "m.fararjeh@comptechco.com", "bZKgihqRzi68t6B");
            System.out.println(id);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @Test
    public void play(){
        Map<String, Value> returnValue = new HashMap<>();

        //Category:
        returnValue.put("category",new StringValue("www"));

        //Initialize Empty lists for labels and multiple_labels:
        returnValue.put("labels",new DictionaryValue());
        returnValue.put("multiple_labels",new DictionaryValue());
        System.out.println(((DictionaryValue)returnValue.get("labels")).get());
        //Filling Labels as follows :
        //label[i] information :
        {
            Map<String, Value> labels = ((DictionaryValue) returnValue.get("labels")).get();

            Map<String, Value> labelInformation = new HashMap<>();
            labelInformation.put("type", new StringValue("www"));
            labelInformation.put("normalized", new StringValue("www"));
            labelInformation.put("confidence", new NumberValue(1111.33));
            labels.put("0",new DictionaryValue(labelInformation));
            labelInformation = new HashMap<>();
            labelInformation.put("type", new StringValue("www22"));
            labelInformation.put("normalized", new StringValue("22"));
            labelInformation.put("confidence", new NumberValue(222.33));
            labels.put("1",new DictionaryValue(labelInformation));
            //And so on

            System.out.println(returnValue);
            System.out.println(((DictionaryValue) returnValue.get("labels")).get().get("0").get());
            System.out.println(((DictionaryValue) returnValue.get("labels")).get().get("1").get());
            //label1.set(labelInformation);
            //label[i+1]


        }


        //Filling multiple_labels as follows :
        //multiple_label[i] information

        {
            Map<String, Value> multiple_labels = ((DictionaryValue) returnValue.get("multiple_labels")).get();
            Map<String, Value> multiple_labels_information = new HashMap<>();
            Map<String, Value> labels_group = ((DictionaryValue) returnValue.get("multiple_labels")).get();

            Map<String, Value> labelInformation = new HashMap<>();
            labelInformation.put("type", new StringValue("www"));
            labelInformation.put("normalized", new StringValue("www"));
            labelInformation.put("confidence", new NumberValue(1111.33));
            multiple_labels_information.put("0", new DictionaryValue(labelInformation));
            labelInformation = new HashMap<>();
            labelInformation.put("type", new StringValue("www2"));
            labelInformation.put("normalized", new StringValue("www2"));
            labelInformation.put("confidence", new NumberValue(222));
            multiple_labels_information.put("1", new DictionaryValue(labelInformation));

            multiple_labels.put("0", new DictionaryValue(multiple_labels_information));

            System.out.println(((DictionaryValue)((DictionaryValue) returnValue.get("multiple_labels")).get().get("0")).get().get("1"));
        }

        //Filling multiple_labels as follows [Table] :
        //multiple_label[i] information
        {
            returnValue.put("multiple_labels",new TableValue());
            Map<String, Value> multiple_labels = ((DictionaryValue) returnValue.get("multiple_labels")).get();
            Map<String, Value> multiple_labels_information = new HashMap<>();
            Map<String, Value> labels_group = ((DictionaryValue) returnValue.get("multiple_labels")).get();

            Map<String, Value> labelInformation = new HashMap<>();
            labelInformation.put("type", new StringValue("www"));
            labelInformation.put("normalized", new StringValue("www"));
            labelInformation.put("confidence", new NumberValue(1111.33));
            multiple_labels_information.put("0", new DictionaryValue(labelInformation));
            labelInformation = new HashMap<>();
            labelInformation.put("type", new StringValue("www2"));
            labelInformation.put("normalized", new StringValue("www2"));
            labelInformation.put("confidence", new NumberValue(222));
            multiple_labels_information.put("1", new DictionaryValue(labelInformation));

            multiple_labels.put("0", new DictionaryValue(multiple_labels_information));
        }




    }
}