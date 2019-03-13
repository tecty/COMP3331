import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerWorker implements Runnable {

    private Socket client = null;

    public ServerWorker(Socket client) {
        this.client = client;
        http
    }

    @Override
    public void run() {
        try {
            // get the in and output stream
            InputStreamReader inputStreamReader = new InputStreamReader(client.getInputStream());
            BufferedReader in = new BufferedReader(inputStreamReader);
            OutputStream out = client.getOutputStream();

            while (in.ready()) {
                String line = in.readLine();
                System.out.println(line);
            }

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