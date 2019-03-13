import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
    public static void main(String[] args) throws Exception {
        System.out.println("Listen on: " + args[0]);
        ServerSocket server = new ServerSocket(Integer.parseInt(args[0]));
        Socket client = null;
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