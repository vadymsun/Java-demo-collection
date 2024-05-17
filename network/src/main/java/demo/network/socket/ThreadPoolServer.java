package demo.network.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadPoolServer {
    static Runtime runtime = Runtime.getRuntime();
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8888);
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        threadPoolExecutor.setMaximumPoolSize(5);
        System.out.println("服务器启动，等待连接。。。");
       // System.out.println("最大内存"+ runtime.maxMemory());
        while(true){
            Socket socket = serverSocket.accept();

           // System.out.println("最大内存"+ runtime.maxMemory());
            threadPoolExecutor.execute(new ClientHandler(socket));
            System.out.println("活跃线程数量"+threadPoolExecutor.getActiveCount());
        }
    }
    static class ClientHandler implements Runnable{
        Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }
        @Override
        public void run() {
            try {
                System.out.println("处理新的连接");
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
