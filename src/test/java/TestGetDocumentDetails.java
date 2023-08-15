import com.automationanywhere.botcommand.GetDocumentDetails;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.DictionaryValue;
import com.automationanywhere.botcommand.data.impl.StringValue;
import org.testng.annotations.Test;

import java.util.Map;

public class TestGetDocumentDetails {

    @Test
    public void TestGetDocumentDetails() {
        try {
            GetDocumentDetails g = new GetDocumentDetails();
            //607098  5558759 5573396  588661
            //y.othman@comptechco.com  yasmine1996
            //m.fararjeh@comptechco.com bZKgihqRzi68t6B
            DictionaryValue documentDetails =g.action(new Long(5558759), "m.fararjeh@comptechco.com", "bZKgihqRzi68t6B");
            System.out.println("===========================================Category:================================================");
            StringValue category=(StringValue) documentDetails.get(0);
            System.out.println("Category name:"+category+'\n');
            System.out.println("===========================================Labels:================================================");
            DictionaryValue labels=(DictionaryValue) documentDetails.get(1);
            System.out.println("Labels size:"+labels.get().size()+'\n');
            Map<String, Value> labels_map=labels.get();
            System.out.println(String.format("\"labels\"=>{"));
            for (Map.Entry<String, Value> pair_label : labels_map.entrySet()) {
                System.out.println(String.format("\t\"%s\"=>{", pair_label.getKey()));
                Map<String,Value> label=((DictionaryValue)pair_label.getValue()).get();
                for (Map.Entry<String, Value> label_attributes : label.entrySet()) {
                    System.out.println(String.format("\t\t\"%s\"=>{ %s },", label_attributes.getKey(),label_attributes.getValue()));
                }
                System.out.println(String.format("\t},"));
            }
            System.out.println(String.format("}"));
            System.out.println("==========================================Clusters:===============================================");
            DictionaryValue clutesrs_of_labels=(DictionaryValue) documentDetails.get(2);
            System.out.println("Clusters size:"+clutesrs_of_labels.get().size()+'\n');
            Map<String,Value> clutesrs_of_labels_map= clutesrs_of_labels.get();
            System.out.println(String.format("\"clusters_of_labels\"=>{"));
            for (Map.Entry<String, Value> pair_cluster : clutesrs_of_labels_map.entrySet()) {
                System.out.println(String.format("\t\"%s\"=>{", pair_cluster.getKey()));
                Map<String,Value> cluster_rows=((DictionaryValue)pair_cluster.getValue()).get();
                for(Map.Entry<String, Value> cluster_row : cluster_rows.entrySet()){
                    System.out.println(String.format("\t\t\"%s\"=>{", cluster_row.getKey()));
                    Map<String,Value> cluster_row_labels=((DictionaryValue)cluster_row.getValue()).get();
                    for (Map.Entry<String, Value> pair_label : cluster_row_labels.entrySet()) {
                        System.out.println(String.format("\t\t\t\"%s\"=>{", pair_label.getKey()));
                        Map<String,Value> label=((DictionaryValue)pair_label.getValue()).get();
                        for (Map.Entry<String, Value> label_attributes : label.entrySet()) {
                            System.out.println(String.format("\t\t\t\t\"%s\"=>{ %s },", label_attributes.getKey(),label_attributes.getValue()));
                        }
                        System.out.println(String.format("\t\t\t},"));
                    }
                    System.out.println(String.format("\t\t},"));
                }
                System.out.println(String.format("\t},"));
            }
            System.out.println(String.format("}"));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
