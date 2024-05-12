package demo.network.socket;



import java.io.*;
import java.net.Socket;

public class HelloClient {
    static class Client extends Thread{
        private String ip;
        private int port;
        private String msg;

        public Client(String ip, int port, String msg){
            this.ip = ip;
            this.port = port;
            this.msg = msg;
        }
        @Override
        public void run() {
            try {
                // 1. 指定服务ip和端口号，连接服务器获取socket
                Socket socket = new Socket(ip, port);

                // 获取输入输出流
                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream();

                // 3. 使用输入输出流及逆行数控v传输
                outputStream.write(msg.getBytes());
                socket.shutdownOutput();

                byte[] data = new byte[1024];
                int len = 0;
                StringBuilder stringBuilder = new StringBuilder();
                while ((len = inputStream.read(data)) != -1){
                    stringBuilder.append(new String(data, 0 , len));
                }
                System.out.println("服务器的消息是："+ stringBuilder);

                // 关闭连接
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        for (int i = 0; i < 10; i++) {
            Thread cur = new Client("127.0.0.1",8888, "客户端编号"+i);
            cur.start();
        }
    }

}
