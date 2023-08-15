package com.automationanywhere.botcommand;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.BotCommand;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import static com.automationanywhere.commandsdk.model.AttributeType.*;
import static com.automationanywhere.commandsdk.model.DataType.STRING;

//@BotCommand

//@CommandPkg(
        //Unique name inside a package and label to display.
  //      name = "GetCategory", label = "[[GetCategory.label]]",
   //     node_label = "[[GetCategory.node_label]]", description = "[[GetCategory.description]]", icon = "pkg.svg",

        //Return type information. return_type ensures only the right kind of variable is provided on the UI.
     //   return_label = "[[GetCategory.return_label]]", return_type = STRING, return_required = true , return_description ="[[GetCategory.return_label_description]]" )
public class GetCategory {
    ObjectMapper objectMapper = new ObjectMapper();
    //@Sessions
    private Map<String, Object> sessionMap;//Map<String, Object>();


    //Identify the entry point for the action. Returns a Value<String> because the return type is String.
    //@Execute
    public StringValue action(//Idx 1 would be displayed first, with a text box for entering the value.
                            //  @Idx(index = "1", type = NUMBER)
                    //   @Pkg(label = "[[GetCategory.categoryId.label]]")
                      // @NotEmpty
                       Long categoryId,
                             // @Idx(index = "2", type = TEXT)
                       //UI labels.
                      // @Pkg(label = "[[GetCategory.userName.label]]")
                       //Ensure that a validation error is thrown when the value is null.
                      // @NotEmpty
                       String userName,

                             // @Idx(index = "3", type = TEXT)
                     //  @Pkg(label = "[[GetCategory.password.label]]")
                     //  @NotEmpty
                       String password)throws Exception{
        if (categoryId <=0)
            throw new BotCommandException("Please make sure to provide a valid category id!");
        if ("".equals(userName.trim()))
            throw new BotCommandException("Please make sure to provide a user name!");
        if ("".equals(password.trim()))
            throw new BotCommandException("Please make sure to provide a password!");
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://app.konfuzio.com/api/v3/categories/"+categoryId+"/"))
                    .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((userName+":"+password).getBytes()))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Category category = objectMapper.readValue(response.body(), Category.class);
            return new StringValue(category.api_name);
        }catch(Exception e){
            throw new BotCommandException("Error in GetCategory!\nsThe full error message: "+e.toString());
        }
    }

    public void setSessionMap(Map<String, Object> sessionMap) {
        this.sessionMap = sessionMap;
    }

    public Map<String, Object> getSessionMap() {
        return this.sessionMap;
    }

    static class Category{
        String api_name="null";

        public String getApi_name() {
            return api_name;
        }

        public void setApi_name(String api_name) {
            this.api_name = api_name;
        }
    }






}


