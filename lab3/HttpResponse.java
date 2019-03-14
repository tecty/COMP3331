package com.tectygroup;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

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
//        headers.put("Content-Type", "text/html; charset=utf-8");
        headers.put("Connection", "Keep-Alive");
        headers.put("Content-Language", "en-US");

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

    public String headerMapToString(){
        String ret = "";
        for (String key :
                headers.keySet()) {
            ret += key +": "+ headers.get(key)+"\r\n";
        }
        return ret;
    }

    public String getHeader(){
        return getStatusStr() +
                headerMapToString()+
                "\r\n" ;
    }

    public void response(){
        FileInputStream fin = null;
        File file = Paths.get(uri).toFile();


        try {
            fin =new FileInputStream(file);
            headers.put(
                "Content-Type",
                Files.probeContentType(file.toPath())
            );

            headers.put(
                "Content-Length",
                Long.toString(file.length())
            );
        }catch (Exception e){
            this.status = STATUS.NOT_FOUND;
        }

        try {
            out.write(getHeader().getBytes());

            byte[] buff = new byte[8196];
            if (fin != null){
                int size ;
                while ((size = fin.read(buff))!= 0){
                    out.write(buff,0,size);
                }
            }
            else {
                out.write("404 Not Found".getBytes());
            }

        } catch (Exception e){

        }

    }


}
