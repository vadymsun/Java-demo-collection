package demo.network.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class HelloServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocket serverSocket =  new ServerSocket(8888);
        System.out.println("服务器等待连接。。。");

        while(true){
            Socket socket = serverSocket.accept();
            System.out.println(socket.getInetAddress().getHostAddress() + "已连接----------------");

            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            byte[] data = new byte[1024];
            StringBuilder s = new StringBuilder();
            int len = 0;
            while((len = inputStream.read(data)) != -1){
                s.append(new String(data, 0 , len));
            }
            System.out.println("客户端发送的消息是："+s);
            Thread.sleep(1000);
            outputStream.write("欢迎欢迎".getBytes());
            outputStream.flush();
            socket.close();
        }
    }

}
