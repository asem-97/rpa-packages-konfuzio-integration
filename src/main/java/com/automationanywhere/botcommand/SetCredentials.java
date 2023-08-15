package com.automationanywhere.botcommand;
import com.automationanywhere.botcommand.exception.BotCommandException;
import java.util.HashMap;
import java.util.Map;

//BotCommand makes a class eligible for being considered as an action.
//@BotCommand

//CommandPks adds required information to be dispalable on GUI.
//@CommandPkg(
        //Unique name inside a package and label to display.
        //name = "SetCredentials", label = "[[SetCredentials.label]]",
       // node_label = "[[SetCredentials.node_label]]", description = "[[SetCredentials.description]]"
//)
public class SetCredentials {

    //@Sessions
    private Map<String, Object> sessionMap=new HashMap<>();
    //Identify the entry point for the action. Returns a Value<String> because the return type is String.
    //@Execute
    public void action(
            ////Idx 1 would be displayed first, with a text box for entering the value.
            //@Idx(index = "1", type = TEXT)
            //UI labels.
            //@Pkg(label = "[[SetCredentials.userName.label]]")
            //Ensure that a validation error is thrown when the value is null.
            //@NotEmpty
            String userName,

            //@Idx(index = "2", type = TEXT)
            //@Pkg(label = "[[SetCredentials.password.label]]")
           // @NotEmpty
            String password) {

        //Internal validation, to disallow empty strings. No null check needed as we have NotEmpty on firstString.
        if ("".equals(userName.trim()))
            throw new BotCommandException("Please make sure to provide a user name!");

        if ("".equals(password.trim()))
            throw new BotCommandException("Please make sure to provide a password!");
        if (!sessionMap.containsKey("userName"))
            sessionMap.put("userName", userName);
        if (!sessionMap.containsKey("password"))
            sessionMap.put("password", password);
    }

    // Ensure that a public setter exists.
    public void setSessionMap(Map<String, Object> sessionMap) {
        this.sessionMap = sessionMap;
    }

}

