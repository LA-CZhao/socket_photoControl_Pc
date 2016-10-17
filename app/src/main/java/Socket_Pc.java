import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by laczhao on 2016/9/19.
 */
public class Socket_Pc {


        public static final String SERVERIP = "192.168.0.52";
        public static final int SERVERPORT = 30000;

        static ServerSocket serverSocket = null;
        static DataInputStream dataInput = null;
        static DataOutputStream dataOutput = null;

        public static void main(String[] args) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("S: Connecting...");

                    try {
                        serverSocket = new ServerSocket(SERVERPORT);

                        while (true) {

                            Socket clientSocket = serverSocket.accept();
                            String send_msg = "";
                            try {
                                dataInput = new DataInputStream(
                                        clientSocket.getInputStream());
                                String msg = dataInput.readUTF();
                                System.out.println(msg);

                                dataOutput = new DataOutputStream(
                                        clientSocket.getOutputStream());
                                if ("a".equals(msg)) {
                                    shutdown();

                                    send_msg = "shutdown";
                                } else if ("b".equals(msg)) {
                                    reboot();
                                    send_msg = "reboot";
                                } else if ("c".equals(msg)) {
                                    cancel();
                                    send_msg = "cancel";
                                }
                            } catch (Exception e) {
                            } finally {
                                try {
                                    if (dataOutput != null) {
                                        dataOutput.writeUTF(send_msg);
                                        dataOutput.close();
                                    }
                                    dataInput.close();

                                    clientSocket.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }).start();


        }

        private static void shutdown() throws IOException {
            Runtime.getRuntime().exec("shutdown -s -t 60");
            System.out.println("shutdown ,60 seconds later ");
        }


        private static void reboot() throws IOException {
            Runtime.getRuntime().exec("shutdown -r -t 60");
            System.out.println("reboot ,60 seconds later ");
        }


        private static void cancel() throws IOException {
            Runtime.getRuntime().exec("shutdown -a");
            System.out.println("cancel shutdown or restart");
        }


    }
