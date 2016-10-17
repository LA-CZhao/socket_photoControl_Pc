package com.example.laczhao.client;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by idea on 16/8/16.
 */
public class Socket_Pc implements Runnable {
    public static final String SERVERIP = "192.168.0.52";
    public static final int SERVERPORT = 30000;
    private String str;

//    public static final String filePath= Environment.getExternalStorageDirectory()+File.separator+"socket.txt";

    public void run() {
        try {
            System.out.println("S: Connecting...");

            ServerSocket serverSocket = new ServerSocket(SERVERPORT);
            while (true) {
                Socket client = serverSocket.accept();

                System.out.println("S: Receiving...");

                try {
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(client.getInputStream()));

                    PrintWriter out = new PrintWriter(new BufferedWriter(
                            new OutputStreamWriter(client.getOutputStream())),true);

                    System.out.println("S: 111111");
                    str = in.readLine();
                    System.out.println("S: 222222");

                    if (str != null ) {
                        out.println("You sent to server message is:" + str);
                        out.flush();

                        String path="D://socket_Pc.txt";


                        File file = new File (path);
                        createFile(file);
                        FileOutputStream fops = new FileOutputStream(file);
                        byte [] b = str.getBytes();
                        for ( int i = 0 ; i < b.length; i++ )
                        {
                            fops.write(b[i]);
                        }
                        System.out.println("S: Received: '" + str + "'");

                    } else {
                        System.out.println("Not receiver anything from client!");
                    }
                } catch (Exception e) {
                    System.out.println("S: Error 1");
                    e.printStackTrace();
                } finally {
                    client.close();
                    System.out.println("S: Done.");
                }

                switch(str){
                    case "shutdown":
                        try {
                            Runtime.getRuntime().exec("shutdown -s -t 60");
                            System.out.println("shutdown ,60 seconds later ");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "reboot":
                        try {
                            Runtime.getRuntime().exec("shutdown -r -t 60");
                            System.out.println("reboot ,60 seconds later ");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "cancel":
                        try {
                            Runtime.getRuntime().exec("shutdown -a");
                            System.out.println("cancel shutdown or restart");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                }

            }
        } catch (Exception e) {
            System.out.println("S: Error 2");
            e.printStackTrace();
        }

    }

    private boolean createFile(File fileName) {
        boolean flag=false;
        try{
            if(!fileName.exists()){
                fileName.createNewFile();
                flag=true;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }

    public static void main(String[] args ) {
        Thread desktopServerThread = new Thread(new Socket_Pc());
        desktopServerThread.start();

    }


}

