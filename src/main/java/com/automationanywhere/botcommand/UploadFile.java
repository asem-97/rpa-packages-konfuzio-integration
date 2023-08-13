package com.automationanywhere.botcommand;
import com.automationanywhere.botcommand.data.impl.NumberValue;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.BotCommand;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.model.DataType;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.entity.ContentType;
import org.apache.http.HttpEntity;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.channels.Channels;
import java.nio.channels.Pipe;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import static com.automationanywhere.commandsdk.model.AttributeType.*;

@BotCommand
@CommandPkg(
        name = "UploadFile", label = "[[UploadFile.label]]",
        node_label = "[[UploadFile.node_label]]", description = "[[UploadFile.description]]", icon = "pkg.svg",

        //Return type information. return_type ensures only the right kind of variable is provided on the UI.
        return_label = "[[UploadFile.return_label]]", return_type = DataType.NUMBER, return_required = true , return_description ="[[UploadFile.return_label_description]]" )
public class UploadFile {
    ObjectMapper objectMapper = new ObjectMapper();
    @Sessions
    private Map<String, Object> sessionMap;//Map<String, Object>();

    @Execute
    public NumberValue action(
                              @Idx(index = "1", type = FILE)
                       //UI labels.
                       @Pkg(label = "[[UploadFile.filePath.label]]")
                       //Ensure that a validation error is thrown when the value is null.
                       @NotEmpty
                       String filePath,

                              @Idx(index = "2", type = NUMBER)
                           @Pkg(label = "[[UploadFile.projectId.label]]")
                           @NotEmpty
                           Long projectId,
                              @Idx(index = "3", type = TEXT)
                           //UI labels.
                           @Pkg(label = "[[UploadFile.userName.label]]")
                           //Ensure that a validation error is thrown when the value is null.
                           @NotEmpty
                           String userName,

                              @Idx(index = "4", type = TEXT)
                           @Pkg(label = "[[UploadFile.password.label]]")
                           @NotEmpty
                           String password)throws Exception{

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        if ("".equals(filePath.trim()))
            throw new BotCommandException("Please make sure to provide a file!");
        if (projectId <=0)
             throw new BotCommandException("Please make sure to provide a valid project id!");
        if ("".equals(userName.trim()))
            throw new BotCommandException("Please make sure to provide a user name!");
        if ("".equals(password.trim()))
            throw new BotCommandException("Please make sure to provide a password!");
        //if (sessionMap==null || !sessionMap.containsKey("userName") || !sessionMap.containsKey("password"))
            //throw new BotCommandException("Please make sure to use Set Credentials action before using any action in Konfuzio package!");

        try{
            MultipartEntityBuilder entitybuilder = MultipartEntityBuilder.create()
                    .addTextBody("project", String.valueOf(projectId))
                    .addTextBody("sync", String.valueOf(true))
                    .addBinaryBody("data_file", new File(filePath),ContentType.APPLICATION_OCTET_STREAM,filePath);
            HttpEntity mutiPartHttpEntity = entitybuilder.build();
            Pipe pipe = Pipe.open();
            new Thread(() -> {
                try (OutputStream outputStream = Channels.newOutputStream(pipe.sink())) {
                    mutiPartHttpEntity.writeTo(outputStream);
                } catch (IOException e) {
                    throw new BotCommandException("Error While writing to the Pipe in the secondary thread! the full error message: "+e.toString());
                }

            }).start();
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(new URI("https://app.konfuzio.com/api/v3/documents/"))
                    .header("Content-Type", mutiPartHttpEntity.getContentType().getValue())
                    .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((userName+":"+password).getBytes()))
                    .POST(java.net.http.HttpRequest.BodyPublishers.ofInputStream(() -> Channels.newInputStream(pipe.source())))
                    .build();
            HttpResponse<String> responseBody = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            Document document = objectMapper.readValue(responseBody.body(), Document.class);
            return new NumberValue(document.id);
        }catch(Exception e){
            throw new BotCommandException("Error! The full error message: "+e.toString());
        }


    }



    public void setSessionMap(Map<String, Object> sessionMap) {
        this.sessionMap = sessionMap;
    }


    public Map<String, Object> getSessionMap() {
        return this.sessionMap;
    }


   static class Document{
        int id=-1;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }




}


