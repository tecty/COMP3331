package com.tectygroup;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpRequest {
    private BufferedReader in;


    public enum METHOD  {GET, POST};
    private METHOD method;
    private String uri;
    private HashMap<String, String> headers;


    public HttpRequest(InputStream in) {
        // initial the header hash map
        headers = new HashMap<>();

        this.in = new BufferedReader(new InputStreamReader(in));
        this.parse();
//        this.printHeaders();
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
//        System.out.println(str);
        String[] words = str.split(":",2);
        if (words.length == 2){
            headers.put(words[0], words[1]);
        }
    }
    
    public void printHeaders(){
        System.out.println("Headers");
        for (String key :
                headers.keySet()) {
            System.out.println(key + " : "+ headers.get(key));
        }
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        if (uri.equals("/")){
//            change it do index page
            uri = "/index.html";
        }

        // remove the first slash from uri
        this.uri = uri.substring(1);
        //System.out.println(this.uri);
    }

    private void parse(){
        try {
            String[] request_info  = in.readLine().split(" ");

            if (request_info[0].equals("GET") ){
                this.method= METHOD.GET;
            }
            this.setUri(request_info[1]);
            while (in.ready()){
                setHeaders(in.readLine());
            }
        } catch (Exception e){
            e.printStackTrace();
        }


    }

}
