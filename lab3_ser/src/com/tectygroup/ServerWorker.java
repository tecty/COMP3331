package com.tectygroup;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class ServerWorker implements Runnable {

    private Socket client = null;

    public ServerWorker(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            // get the in and output stream
            OutputStream out = client.getOutputStream();

            HttpRequest hr = new HttpRequest(client.getInputStream());

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