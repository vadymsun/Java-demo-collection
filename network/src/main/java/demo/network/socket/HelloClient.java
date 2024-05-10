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
                Socket socket = new Socket(ip, port);

                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream();

                outputStream.write(msg.getBytes());
                socket.shutdownOutput();

                byte[] data = new byte[1024];
                int len = 0;
                StringBuilder stringBuilder = new StringBuilder();
                while ((len = inputStream.read(data)) != -1){
                    stringBuilder.append(new String(data, 0 , len));
                }

                System.out.println("服务器的消息是："+ stringBuilder);
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
