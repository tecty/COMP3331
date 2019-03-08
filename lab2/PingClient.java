import java.io.*;
import java.net.*;
import java.time.Instant;
import java.util.*;

/*
 * Server to process ping requests over UDP. 
 * The server sits in an infinite loop listening for incoming UDP packets. 
 * When a packet comes in, the server simply sends the encapsulated data back to the client.
 */

public class PingClient {
   private static final double LOSS_RATE = 0.3;
   private static final int AVERAGE_DELAY = 100; // milliseconds

   public static void main(String[] args) throws Exception {
      // Get command line argument.
      if (args.length != 2) {
         System.out.println("Required arguments: host port");
         return;
      }
      // port we want to send
      int port = Integer.parseInt(args[1]);
      // the ip addr of the host
      InetAddress hostAddr = InetAddress.getByName(args[0]);

      // Create a datagram socket for receiving and sending UDP packets
      // through the port specified on the command line.
      DatagramSocket socket = new DatagramSocket();

      // set the timeout to 1 sec
      socket.setSoTimeout(1000);

      // initial the list of rtt
      ArrayList<Long> rtt_l = new ArrayList<>();

      for (int i = 0; i < 10; i++) {
         long start = System.currentTimeMillis();
         String str = "PING " + i + " " + start + "\r\n";

         // Create a datagram packet to hold incomming UDP packet.
         DatagramPacket request = new DatagramPacket(str.getBytes(), str.length(), hostAddr, port);
         // sent the packet
         socket.send(request);

         str = "ping to " + args[0] + ", seq = " + i + ", ";
         // not sure it will be received
         // Create a datagram packet to hold incomming UDP packet.
         try {
            DatagramPacket response = new DatagramPacket(new byte[1024], 1024);
            socket.receive(response);

            // receive the package
            long end = System.currentTimeMillis();

            // calculate the rtt and push into list
            long rtt = end - start;
            rtt_l.add(rtt);

            System.out.println(str + "rtt = " + (rtt) + " ms");

            // this is the print string for output
         } catch (SocketTimeoutException e) {
            System.out.println(str + "time out");
         }

      }

      rtt_l.sort(new Comparator<Long>() {
         @Override
         public int compare(Long a, Long b) {
            return a > b ? 1 : -1;
         }
      });

      System.out.println("min = " + rtt_l.get(0) + " ms, max = " + rtt_l.get(rtt_l.size() - 1) + " ms, package lost: "
            + (10 - rtt_l.size()) * 10 + "%");

      socket.close();

      // // Processing loop.
      // while (true) {
      // // Create a datagram packet to hold incomming UDP packet.
      // DatagramPacket request = new DatagramPacket(new byte[1024], 1024);

      // // Block until the host receives a UDP packet.
      // socket.receive(request);

      // // Print the recieved data.
      // printData(request);

      // // Decide whether to reply, or simulate packet loss.
      // if (random.nextDouble() < LOSS_RATE) {
      // System.out.println(" Reply not sent.");
      // continue;
      // }

      // // Simulate network delay.
      // Thread.sleep((int) (random.nextDouble() * 2 * AVERAGE_DELAY));

      // // Send reply.
      // InetAddress clientHost = request.getAddress();
      // int clientPort = request.getPort();
      // byte[] buf = request.getData();
      // DatagramPacket reply = new DatagramPacket(buf, buf.length, clientHost,
      // clientPort);
      // socket.send(reply);

      // System.out.println(" Reply sent.");
      // }
   }

   /*
    * Print ping data to the standard output stream.
    */
   private static void printData(DatagramPacket request) throws Exception {
      // Obtain references to the packet's array of bytes.
      byte[] buf = request.getData();

      // Wrap the bytes in a byte array input stream,
      // so that you can read the data as a stream of bytes.
      ByteArrayInputStream bais = new ByteArrayInputStream(buf);

      // Wrap the byte array output stream in an input stream reader,
      // so you can read the data as a stream of characters.
      InputStreamReader isr = new InputStreamReader(bais);

      // Wrap the input stream reader in a bufferred reader,
      // so you can read the character data a line at a time.
      // (A line is a sequence of chars terminated by any combination of \r and \n.)
      BufferedReader br = new BufferedReader(isr);

      // The message data is contained in a single line, so read this line.
      String line = br.readLine();

      // Print host address and data received from it.
      System.out.println("Received from " + request.getAddress().getHostAddress() + ": " + new String(line));
   }
}
