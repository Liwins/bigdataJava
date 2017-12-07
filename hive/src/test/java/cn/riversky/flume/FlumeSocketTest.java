package cn.riversky.flume;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by admin on 2017/12/2.
 */
public class FlumeSocketTest {
//    @Test
    /**
     * tcp
     */
    public void test() throws IOException {

        Socket socket=new Socket("datanode1",44444);
        OutputStream outputStream=socket.getOutputStream();
        BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream));
        bufferedWriter.write("hello\n");
        bufferedWriter.flush();
        bufferedWriter.close();
        socket.close();
    }
//    @Test
    public void testflume()throws IOException {
        Socket client = new Socket("datanode1", 44444);
        OutputStream out = client.getOutputStream();
        String event = "<4>hello\n";
        out.write(event.getBytes());
        out.flush();
        out.close();
        client.close();
    }
//    @Test
    public void testudp() throws IOException {
        //1、定义服务器的地址、端口号、数据
        InetAddress address =InetAddress.getByName("datanode1");
        int port =44444;
        byte[] data ="用户名：admin;密码：123\n".getBytes();
        //2、创建数据报，包含发送的数据信息
        DatagramPacket packet = new DatagramPacket(data,0,data.length,address,port);
        //3、创建DatagramSocket对象
        DatagramSocket socket =new DatagramSocket();
         //4、向服务器发送数据
         socket.send(packet);
    }
}
