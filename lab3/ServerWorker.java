package com.tectygroup;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

public class ServerWorker implements Runnable {

    private Socket client = null;

    public ServerWorker(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {

            OutputStream out = client.getOutputStream();
            HttpRequest hr = new HttpRequest(client.getInputStream());

//            System.out.println("GET: "+ hr.getUri());

            PrintStream outprint = new PrintStream(out);
//            outprint.println("200 OK\n\r\n\rhello world");

            if (hr.getMethod() != HttpRequest.METHOD.GET){
                // we only support the get mothod
                this.client.close();
            }

            HttpResponse hq = new HttpResponse(out,hr.getUri());

            hq.response();

        } catch (Exception e) {
            // close the connection for any reason
            e.printStackTrace();
        } finally {
            try {
                System.out.println("connection close");
                this.client.close();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }
}