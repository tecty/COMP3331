package com.tectygroup;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
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
        headers.put("Connection", "Keep-Alive");
//        headers.put("Date")
        headers.put("Content-Type", "text/html");


    }
    public String getStatusStr(){
        switch (this.status){
            case OK:
                return "200 OK\n\r";
            case NOT_FOUND:
                return "404 NOT FOUND\n\r";
        }
        return "200 OK\n\r";
    }

    public String getHeaderStr(){
        String ret = "";
        for (String key :
                headers.keySet()) {
            ret += key +": "+ headers.get(key)+"\n\r";
        }
        return ret;
    }

    public String getHeader(){
        String ret = "";
        ret += getStatusStr();
        ret += getHeaderStr();
        ret += "\n\r";
        return  ret;
    }

    public void response(){
        FileInputStream fin = null;
        try {
            fin =new FileInputStream(Paths.get(uri).toFile());
        } catch (Exception e) {
            e.printStackTrace();
            // here must be a file not found error
            this.status = STATUS.NOT_FOUND;
        }

        try {
            // write the header part
            System.out.println(this.getHeader());
            this.out.write(this.getHeader().getBytes());

            if (fin != null){
                byte[] buff = new byte[8192];
                int size = fin.read(buff) ;
                do {
                    // send all the file to remote
                    this.out.write(buff,0, size);
                    size = fin.read(buff);
                }while( size > 0);
            }
            this.out.flush();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        try {
            // close the steam
            this.out.close();
        } catch (Exception e){
            e.printStackTrace();
        }


    }


}
