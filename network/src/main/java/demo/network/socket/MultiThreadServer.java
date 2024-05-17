package demo.network.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadServer {
    static Runtime runtime = Runtime.getRuntime();
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8888);
        System.out.println("服务器启动，等待连接。。。");
        System.out.println("最大内存"+ runtime.maxMemory());
        while(true){
            Socket socket = serverSocket.accept();
            System.out.println("获取到新的连接");
            System.out.println("最大内存"+ runtime.maxMemory());
            new ClientHandler(socket).start();

        }
    }
    static class ClientHandler extends Thread{
        Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }
        @Override
        public void run() {
            try {
                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream();

                int len = 0;
                byte[] data = new byte[1024];
                StringBuilder stringBuilder = new StringBuilder();
                while ((len = inputStream.read(data)) != -1){
                    stringBuilder.append(new String(data,0,len));
                }
                System.out.println("客户端发送的消息是："+stringBuilder);
                outputStream.write("欢迎！".getBytes());
                outputStream.flush();
                inputStream.close();
                outputStream.close();
                socket.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
