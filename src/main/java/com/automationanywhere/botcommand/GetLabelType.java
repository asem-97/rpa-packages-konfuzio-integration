package com.automationanywhere.botcommand;
/*
 * Copyright (c) 2019 Automation Anywhere.
 * All rights reserved.
 *
 * This software is the proprietary information of Automation Anywhere.
 * You shall use it only in accordance with the terms of the license agreement
 * you entered into with Automation Anywhere.
 */
/**
 *
 */

import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.BotCommand;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
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
import static com.automationanywhere.commandsdk.model.DataType.STRING;

//BotCommand makes a class eligible for being considered as an action.
@BotCommand

//CommandPks adds required information to be dispalable on GUI.
@CommandPkg(
        //Unique name inside a package and label to display.
        name = "GetLabelType", label = "[[GetLabelType.label]]",
        node_label = "[[GetLabelType.node_label]]", description = "[[GetLabelType.description]]", icon = "pkg.svg",

        //Return type information. return_type ensures only the right kind of variable is provided on the UI.
        return_label = "[[GetLabelType.return_label]]", return_type = STRING, return_required = true , return_description ="[[GetLabelType.return_label_description]]" )
public class GetLabelType {
    ObjectMapper objectMapper = new ObjectMapper();
    @Sessions
    private Map<String, Object> sessionMap;//Map<String, Object>();


    //Identify the entry point for the action. Returns a Value<String> because the return type is String.
    @Execute
    public void action(//Idx 1 would be displayed first, with a text box for entering the value.
                       @Idx(index = "1", type = NUMBER)
                       @Pkg(label = "[[GetCategory.categoryId.label]]")
                       @NotEmpty
                       Long categoryId,
                       @Idx(index = "2", type = TEXT)
                       //UI labels.
                       @Pkg(label = "[[GetCategory.userName.label]]")
                       //Ensure that a validation error is thrown when the value is null.
                       @NotEmpty
                       String userName,

                       @Idx(index = "3", type = TEXT)
                       @Pkg(label = "[[GetCategory.password.label]]")
                       @NotEmpty
                       String password)throws Exception{
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        //if ("".equals(filePath.trim()))
        // throw new BotCommandException("Please make sure to provide a user name!");
        //if (sessionMap==null || !sessionMap.containsKey("userName") || !sessionMap.containsKey("password"))
        //throw new BotCommandException("Please make sure to use Set Credentials action before using any action in Konfuzio package!");

        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://app.konfuzio.com/api/v3/categories/"+categoryId+"/"))
                    .header("Authorization", "Basic eS5vdGhtYW5AY29tcHRlY2hjby5jb206eWFzbWluZTE5OTY=")
                    //.header("Authorization", "Basic " + Base64.getEncoder().encodeToString((userName+":"+password).getBytes()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Category category = objectMapper.readValue(response.body(), Category.class);
            System.out.println(response.body());
            System.out.println(category.api_name);
            //System.out.println(response.body());
        }catch(Exception e){
            throw new BotCommandException("Error While writing to the Pipe! the full error message: "+e.toString());
            //System.out.println(e.toString());
        }


    }



    // Ensure that a public setter exists.
    public void setSessionMap(Map<String, Object> sessionMap) {
        this.sessionMap = sessionMap;
    }


    // Ensure that a public getter exists.
    public Map<String, Object> getSessionMap() {
        return this.sessionMap;
    }

    static class Category{
        String api_name;

        public String getApi_name() {
            return api_name;
        }

        public void setApi_name(String api_name) {
            this.api_name = api_name;
        }
    }






}


