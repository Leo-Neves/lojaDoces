package br.ufpa.lojadoces.connection;

import java.util.HashMap;

/**
 * Created by leo on 28/11/16.
 */

public class Header extends HashMap<String, String> {
    public Header(){
        super();
    }

    public void putAuthorization(){
        put("Authorization", "Basic Y3VybDpjdXJs");
    }

    public void putContentTypeJson(){
        put("Content-Type", "application/json; charset=UTF-8");
    }

}
