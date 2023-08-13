package com.automationanywhere.botcommand;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.DictionaryValue;
import com.automationanywhere.botcommand.data.impl.NumberValue;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.BotCommand;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.Nullable;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import static com.automationanywhere.commandsdk.model.AttributeType.*;
import static com.automationanywhere.commandsdk.model.DataType.STRING;

@BotCommand
@CommandPkg(
        name = "GetDocument", label = "[[GetDocument.label]]",
        node_label = "[[GetDocument.node_label]]", description = "[[GetDocument.description]]", icon = "pkg.svg",
        return_label = "[[GetDocument.return_label]]", return_type = STRING, return_required = true , return_description ="[[GetDocument.return_label_description]]" )
public class GetDocumentDetails {
    ObjectMapper objectMapper = new ObjectMapper();
    @Sessions
    private Map<String, Object> sessionMap;

    @Execute
    public DictionaryValue action(
                       @Idx(index = "1", type = NUMBER)
                       @Pkg(label = "[[GetDocument.documentId.label]]")
                       @NotEmpty
                       Long documentId,

                       @Idx(index = "2", type = TEXT)
                       @Pkg(label = "[[GetDocument.userName.label]]")
                       @NotEmpty
                       String userName,

                       @Idx(index = "3", type = TEXT)
                       @Pkg(label = "[[GetDocument.password.label]]")
                       @NotEmpty
                       String password)throws Exception{
        if (documentId <=0)
            throw new BotCommandException("Please make sure to provide a valid document id!");
        if ("".equals(userName.trim()))
            throw new BotCommandException("Please make sure to provide a user name!");
        if ("".equals(password.trim()))
            throw new BotCommandException("Please make sure to provide a password!");
        //if (sessionMap==null || !sessionMap.containsKey("userName") || !sessionMap.containsKey("password"))
        //throw new BotCommandException("Please make sure to use Set Credentials action before using any action in Konfuzio package!");
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://app.konfuzio.com/api/v3/documents/"+documentId+"/"))
                    .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((userName+":"+password).getBytes()))
                    .build();
            HttpResponse<String> response =client.send(request, HttpResponse.BodyHandlers.ofString());
            DocumentDetails documentDetails= objectMapper.readValue(response.body(), DocumentDetails.class);
            long end = System.currentTimeMillis()+10000;
            while(System.currentTimeMillis()<end && documentDetails.category==-1) {
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
                documentDetails = objectMapper.readValue(response.body(), DocumentDetails.class);
            }
            if(documentDetails.category==-1){

            }
            DictionaryValue result = DataStructureConverter.convert(documentDetails);
            //Add Category name on the result:
            if(documentDetails.category!=-1){
                GetCategory getCategory = new GetCategory();
                result.get().put("category",getCategory.action(documentDetails.category,userName,password));
            }
            return result;
        }catch(Exception e){
            String err="";
            for(StackTraceElement st:e.getStackTrace())
                err+=st.toString()+'\n';
            throw new BotCommandException("Error,!The full error message: "+err);
        }

    }


    public void setSessionMap(Map<String, Object> sessionMap) {
        this.sessionMap = sessionMap;
    }


    public Map<String, Object> getSessionMap() {
        return this.sessionMap;
    }

    static class DataStructureConverter{
        public static DictionaryValue convert(DocumentDetails documentDetails){
            //1. Prepare The Output [Automation Anywhere Native Types] :
            Map<String, Value> returnValue = new HashMap<>();
            //Category:
            returnValue.put("category", new StringValue("null"));
            //Initialize Empty lists for labels and multiple_labels:
            returnValue.put("labels", new DictionaryValue());//[labels]
            returnValue.put("clusters_of_labels", new DictionaryValue());//type=>["0"=>[labels],"1"=>[labels]] and each label in labels: ["type"=>"","normalized"=>""..]
            //Get References for each base Entry :
            Map<String, Value> labels = ((DictionaryValue) returnValue.get("labels")).get();
            Map<String, Value> clusters_of_labels = ((DictionaryValue) returnValue.get("clusters_of_labels")).get();

            //2.Build Intermediate Data Structure Between Object Mapped to JSON and AA Native DT [Processing JSON Data] :
            ArrayList<AnnotationSet> annotationSets = documentDetails.getAnnotation_sets();
            for(AnnotationSet annotationSet : annotationSets){
                if(!annotationSet.getLabel_set().getHas_multiple_annotation_sets()) {
                    labels = fill_labels(annotationSet, labels);
                }
                else{//annotationSet.getLabel_set().getHas_multiple_annotation_sets()==true & (clusters_of_labels.containsKey() || !clusters_of_labels.containsKey())
                    Map<String, Value> row_of_cluser_labels=new LinkedHashMap<>();
                    row_of_cluser_labels=fill_labels(annotationSet,row_of_cluser_labels);
                    String cluster_name =annotationSet.getLabel_set().getName();
                    if(clusters_of_labels.containsKey(cluster_name)){
                        Map<String, Value> uncompleted_cluster= ((DictionaryValue)clusters_of_labels.get(cluster_name)).get();
                        uncompleted_cluster.put("" +(uncompleted_cluster.size()),new DictionaryValue(row_of_cluser_labels));
                    }else{
                        Map<String, Value> first_row_cluster=new LinkedHashMap<>();
                        first_row_cluster.put("0",new DictionaryValue(row_of_cluser_labels));
                        clusters_of_labels.put(cluster_name, new DictionaryValue(first_row_cluster));
                    }
                }
            }
            return new DictionaryValue(returnValue){
                @Override
                public Value get(int index) {
                    if (index == 0)
                        return get().get("category");
                    if (index == 1)
                        return get().get("labels");
                    if (index == 2)
                        return get().get("clusters_of_labels");
                    throw new BotCommandException("\nPlease specify one of the following:\n0=>category\n1=>labels\n2=>multiple_labels_dictionary\n3=>multiple_labels_table");
                }
            };
        }


        public static Map<String, Value> fill_labels(AnnotationSet annotationSet , Map<String, Value> collection){
            for (int i = 0; i < annotationSet.getLabels().size(); i++) {
                Label label = annotationSet.getLabels().get(i);
                Annotation best_annotation_for_label= getBestLabeling(label);
                if(best_annotation_for_label==null){
                    //?
                }
                Map<String, Value> label_map = new LinkedHashMap<>();
                label_map.put("type", new StringValue(annotationSet.getLabel_set().getName()));
                label_map.put("name", new StringValue(label.getName()));
                label_map.put("normalized", new StringValue(best_annotation_for_label!=null?best_annotation_for_label.getNormalized():""));
                label_map.put("confidence", new NumberValue(best_annotation_for_label!=null?best_annotation_for_label.getConfidence():0));
                collection.put(""+collection.size(), new DictionaryValue(label_map));//It's important to put collection.size() in indexing in order to not override previous labels!
            }
            return collection;
        }
        @Nullable
        public static Annotation getBestLabeling(Label label){
            if(label.getAnnotations().size()==0){
                return null;
            }
            Annotation best_annotation_for_label=label.getAnnotations().get(0);
            for(int i=1 ; i< label.getAnnotations().size();i++){
                if(label.getAnnotations().get(i).getConfidence()>best_annotation_for_label.getConfidence())
                    best_annotation_for_label=label.getAnnotations().get(i);
            }
            return best_annotation_for_label;
        }
    }

    static class DocumentDetails{
        Long category=Long.valueOf(-1);
        ArrayList<AnnotationSet> annotation_sets=new ArrayList<AnnotationSet>();

        public Long getCategory() {
            return category;
        }

        public void setCategory(Long category) {
            this.category = category;
        }

        public ArrayList<AnnotationSet> getAnnotation_sets() {
            return annotation_sets;
        }

        public void setAnnotation_sets(ArrayList<AnnotationSet> annotation_sets) {
            this.annotation_sets = annotation_sets;
        }
    }
    static class AnnotationSet{
        LabelSet label_set;
        ArrayList<Label>labels=new ArrayList<Label>();

        public LabelSet getLabel_set() {
            return label_set;
        }

        public void setLabel_set(LabelSet label_set) {
            this.label_set = label_set;
        }

        public ArrayList<Label> getLabels() {
            return labels;
        }

        public void setLabels(ArrayList<Label> labels) {
            this.labels = labels;
        }
    }
    static class LabelSet{
        String name;
        Long id;
        Boolean has_multiple_annotation_sets;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Boolean getHas_multiple_annotation_sets() {
            return has_multiple_annotation_sets;
        }

        public void setHas_multiple_annotation_sets(Boolean has_multiple_annotation_sets) {
            this.has_multiple_annotation_sets = has_multiple_annotation_sets;
        }
    }
    static class Label{
        String name;
        ArrayList<Annotation>annotations=new ArrayList<Annotation>();

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ArrayList<Annotation> getAnnotations() {
            return annotations;
        }

        public void setAnnotations(ArrayList<Annotation> annotations) {
            this.annotations = annotations;
        }
    }
    static class Annotation{
        String normalized;
        Double confidence;

        public String getNormalized() {
            return normalized;
        }

        public void setNormalized(String normalized) {
            this.normalized = normalized;
        }

        public Double getConfidence() {
            return confidence;
        }

        public void setConfidence(Double confidence) {
            this.confidence = confidence;
        }
    }

}


