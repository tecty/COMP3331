package com.tectygroup;

import java.io.*;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

public class HttpResponse {
    public  enum STATUS {
        OK, NOT_FOUND
    }


    private OutputStream out;
    private String uri;
    private STATUS status = STATUS.OK;
    private HashMap<String, String> headers;


    public HttpResponse(OutputStream out, String uri){
        this.out = out;
        this.uri = uri;
        // init the header map
        headers = new HashMap<>();
        // dummy headers
        headers.put("Content-Type", "text/html; charset=utf-8");
        headers.put("Connection", "Keep-Alive");


    }
    public String getStatusStr(){
        switch (this.status){
            case OK:
                return "HTTP/1.1 200 OK\r\n";
            case NOT_FOUND:
                return "HTTP/1.1 404 NOT FOUND\r\n";
        }
        return "HTTP/1.1 200 OK\r\n";
    }

//    public String getHeaderStr(){
//        String ret = "";
//        for (String key :
//                headers.keySet()) {
//            ret += key +": "+ headers.get(key)+"\r\n";
//        }
//        return ret;
//    }

    public String getHeader(){
        return getStatusStr() +
                "Content-Type: text/html; charset=utf-8\r\n" +
                "Content-Length: "+headers.get("Content-Length")+"\r\n" +
                "Connection: keep-alive\r\n" +
                "Content-Language: en-US\r\n" +
                "\r\n" ;
    }

    public void response(){
        BufferedReader fin = null;
        // we only support 8k for this version
        String str = "";

        try {
            fin =new BufferedReader(
                    new FileReader(
                            Paths.get(uri).toFile()
                    )
            );
            String st;
            while ((st= fin.readLine())!= null){
                str += st;
            }

            headers.put(
                "Content-Length",
                Integer.toString(str.length())
            );
        }catch (Exception e){
            this.status = STATUS.NOT_FOUND;
        }


        str = getHeader() + str;

        try {

            out.write(str.getBytes());
        } catch (Exception e){

        }

    }


}
