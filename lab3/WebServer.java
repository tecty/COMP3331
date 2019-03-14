package com.tectygroup;

import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {

    public static void main(String[] args) throws Exception {
	// write your code here
        System.out.println("Listen on: " + args[0]);
        ServerSocket server = new ServerSocket(Integer.parseInt(args[0]));
        Socket client ;
        while (true) {
            try {
                // set up to accept the connections
                client = server.accept();
                System.out.println("Connection Success");
                new Thread(new ServerWorker(client)).start();
            } catch (Exception e) {
                e.printStackTrace();
                server.close();
            }
        }
        
    }
}
