package com.example.laczhao.client.android;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by laczhao on 2016/9/19.
 */

/**
 * 1、首先扫描局域网内所有PC,看是否有PC端的服务器在运行并监听30000端口。
 * 2、如果没有扫描到有PC端的服务器在运行并监听30000端口，则重新扫描或者退出。
 * 3、扫描到了有PC端的服务器在运行并监听30000端口，则控制PC端关机、重启或者取消关机。
 * 4、点击关机按钮，发送指令到PC服务器端
 * 5、点击重启按钮，发送指令到PC服务器端
 * 6、点击取消按钮，发送指令到PC服务器端
 * */
public class ShutDownServer {
    static ServerSocket serverSocket = null;// 服务socket
    static DataInputStream dataInput = null;// 输入流
    static DataOutputStream dataOutput = null;// 输出流

    public static void main(String[] args) {
        try {
            // 监听30000端口
            serverSocket = new ServerSocket(30000);
            System.out.println("ShutDownServer is listening  port 30000............");

            while (true) {
                // 获取客户端套接字
                Socket clientSocket = serverSocket.accept();
                String send_msg = "";
                try {
                    // 获取输入流,读取客户端传来的数据
                    dataInput = new DataInputStream(
                            clientSocket.getInputStream());
                    String msg = dataInput.readUTF();
                    System.out.println(msg);
                    // 判断输入，进行相应的操作
                    dataOutput = new DataOutputStream(
                            clientSocket.getOutputStream());
                    if ("shutdown".equals(msg)) {
                        shutdown();
                        // 发送消息回Android端
                        send_msg = "60秒后关机 ";
                    } else if ("reboot".equals(msg)) {
                        reboot();
                        send_msg = "60秒后重启";
                    } else if ("cancel".equals(msg)) {
                        cancel();
                        send_msg = "取消关机或重启";
                    }
                } catch (Exception e) {
                } finally {
                    try {
                        if (dataOutput != null) {
                            dataOutput.writeUTF(send_msg);
                            dataOutput.close();
                        }
                        dataInput.close();
                        // 关闭连接
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 关机
    private static void shutdown() throws IOException {
        Runtime.getRuntime().exec("shutdown -s -t 60");
        System.out.println("shutdown ,60 seconds later ");
    }

    // 重启
    private static void reboot() throws IOException {
        Runtime.getRuntime().exec("shutdown -r -t 60");
        System.out.println("reboot ,60 seconds later ");
    }

    // 取消关机或重启
    private static void cancel() throws IOException {
        Runtime.getRuntime().exec("shutdown -a");
        System.out.println("cancel shutdown or restart");
    }
}
