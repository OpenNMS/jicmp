import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

import org.opennms.protocols.icmp.*;


public class TestInitialization {

    public static void main(final String[] args) {
        try {
            final IcmpSocket socket = new IcmpSocket();

            Runnable r = new Runnable() {
                public void run() {
                    System.err.println("Starting receiver");
                    while(true) {
                        try {
                            processReply(socket);
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.exit(1);
                        }
                    }
                }

                private void processReply(final IcmpSocket socket) throws IOException {
                    System.err.println("Waiting for packet");
                    DatagramPacket responsePacket = socket.receive();

                    ICMPEchoPacket icmpPacket = new ICMPEchoPacket(responsePacket.getData());
                    System.err.printf("Recieved packet of type %s\n", icmpPacket.getType());
                    double rtt = icmpPacket.getPingRTT()/1000.0;
                    String host = responsePacket.getAddress().getHostAddress();
                    System.err.printf("%d bytes from %s, icmp_seq=%d time=%f\n", responsePacket.getLength(), host, icmpPacket.getSequenceId(), rtt);
                }
            };

            Thread receiver = new Thread(r);
            receiver.start();

            int id = (int)(Math.random()*Short.MAX_VALUE);
            long threadId = (long)(Math.random()*Integer.MAX_VALUE);

            for(int seqNum = 0; seqNum < 10; seqNum++) {
                ICMPEchoPacket request = new ICMPEchoPacket(threadId);

                byte[] bytes = request.toBytes();
                DatagramPacket packet = new DatagramPacket(bytes, 0, bytes.length, InetAddress.getByName("127.0.0.1"), 0);

                System.err.println("Sending packet\n");
                socket.setTrafficClass(46); // expedited forwarding
                socket.send(packet);

                Thread.sleep(1000);
            }


        } catch (final Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.exit(0);
    }

}