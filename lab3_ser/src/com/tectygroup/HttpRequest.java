package com.tectygroup;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpRequest {
    private BufferedReader in;


    private enum METHOD  {GET, POST};
    private METHOD method;
    private HashMap<String, String> headers;


    public HttpRequest(InputStream in) {
        this.in = new BufferedReader(new InputStreamReader(in));
        this.parse();
    }

    public METHOD getMethod() {
        return method;
    }

    public void setMethod(METHOD method) {
        this.method = method;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(String str) {
        Pattern p = Pattern.compile('^(\w+?): (\w+)$');
        Matcher m = p.matcher(str);

        this.headers.put(m.group(1),m.group(2));
    }

    private void parse(){
        try {

            // read all the headers' key value pair 
            while (in.ready()){
                setHeaders(in.readLine());
            }
        } catch (Exception e){
            e.printStackTrace();
        }


    }

}
