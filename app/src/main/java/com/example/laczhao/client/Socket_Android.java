package com.example.laczhao.client;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by idea on 16/8/16.
 */
public class Socket_Android extends Activity {
    private static final String TAG = "Socket_Android";

    private EditText mEditText = null;
    private TextView tx1 = null;
    private Button mButton = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = (Button)findViewById(R.id.btn);
        mEditText = (EditText)findViewById(R.id.et);
        tx1 = (TextView)findViewById(R.id.tv);

        mButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                setTitle("测试Socket连接");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Socket socket = null;

                        try {
                    /* 指定Server的IP地址，此地址为局域网地址，如果是使用WIFI上网，则为PC机的WIFI IP地址
                     * 在ipconfig查看到的IP地址如下：
                     * Ethernet adapter 无线网络连接:
                     * Connection-specific DNS Suffix  . : IP Address. . . . . . . . . . . . : 192.168.0.162
                     */
                            InetAddress serverAddr = InetAddress.getByName("192.168.0.52");// TCPServer.SERVERIP
                            Log.d("TCP", "C: Connecting...");

                            // 应用Server的IP和端口建立Socket对象
                            socket = new Socket(serverAddr, 30000);
                            String message = "---Test_Socket_Android---";

                            Log.d("TCP", "C: Sending: '" + message + "'");

                            // 将信息通过这个对象来发送给Server
                            PrintWriter out = new PrintWriter(new BufferedWriter(
                                    new OutputStreamWriter(socket.getOutputStream())),
                                    true);

                            // 把用户输入的内容发送给server
                            String toServer = mEditText.getText().toString();
                            Log.d(TAG, "To server:'" + toServer + "'");
                            out.println(toServer);
                            out.flush();


                            // 接收服务器信息
                            BufferedReader in = new BufferedReader(
                                    new InputStreamReader(socket.getInputStream()));
                            // 得到服务器信息
                            String msg = in.readLine();
                            Log.d(TAG, "From server:'" + msg + "'");
                            // 在页面上进行显示
                            tx1.setText(msg);
                        } catch(UnknownHostException e) {
                            Log.e(TAG, "192.168.0.162 is unkown server!");
                        } catch(Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                socket.close();
                            } catch(Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();

            }

        });
    }
}

