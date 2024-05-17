package demo.network.nio;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class NioServer {
    public static void main(String[] args) throws IOException {
        // 0. ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        // 1. 创建了服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false); // 非阻塞模式
        // 2. 绑定监听端口
        ssc.bind(new InetSocketAddress(8888));
        // 3. 连接集合
        List<SocketChannel> channels = new ArrayList<>();
        while (true) {
            // 4. accept 建立与客户端连接， SocketChannel 用来与客户端之间通信
            SocketChannel sc = ssc.accept(); // 非阻塞，线程还会继续运行，如果没有连接建立，但sc是null
            if (sc != null) {
                sc.configureBlocking(false); // 非阻塞模式
                channels.add(sc);
            }
            Iterator<SocketChannel> iter = channels.iterator();
            while (iter.hasNext()) {
                // 5. 接收客户端发送的数据
                SocketChannel channel = iter.next();
                if(!channel.isConnected()){
                    iter.remove();
                    System.out.println("客户端断开连接");
                }
                int read = channel.read(buffer);// 非阻塞，线程仍然会继续运行，如果没有读到数据，read 返回 0
                if (read > 0) {
                    buffer.flip();
                    System.out.println("客户端发送消息："+ Charset.forName("utf-8").decode(buffer));
                    buffer.clear();
                }
            }
        }

    }
}
