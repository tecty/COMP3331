import java.net.ServerSocket;  
import java.net.Socket;  



public class WebServer {  
    public static void main(String[] args) throws Exception{  
        System.out.println("Listen on: " +args[0]);
        ServerSocket server = new ServerSocket(args[0]);  
        Socket client = null;  
        boolean f = true;  
        while(f){  
            //等待客户端的连接，如果没有获取连接  
            client = server.accept();  
            System.out.println("与客户端连接成功！");  
            //为每个客户端连接开启一个线程  
            // new Thread(new ServerThread(client)).start();  
        }  
        server.close();  
    }  
} 