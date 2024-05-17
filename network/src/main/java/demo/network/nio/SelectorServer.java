package demo.network.nio;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

public class SelectorServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        Selector selector = Selector.open();
        ssc.configureBlocking(false);
        SelectionKey ssckey = ssc.register(selector,0,null);
        ssckey.interestOps(SelectionKey.OP_ACCEPT);
        ssc.bind(new InetSocketAddress(8888));
        System.out.println("服务器已启动。。。。");

        while (true){
            selector.select();
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while (iter.hasNext()){
                SelectionKey key = iter.next();
                if(key.isAcceptable()){
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    SelectionKey selectionKey = socketChannel.register(selector, 0 , null);
                    selectionKey.interestOps(SelectionKey.OP_READ);
                }else if(key.isReadable()){
                    SocketChannel socketChannel =(SocketChannel) key.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    int len = socketChannel.read(byteBuffer);
                    if(len == -1){
                        key.cancel();
                        socketChannel.close();
                        System.out.println("连接关闭。。。");
                    }else{
                        byteBuffer.flip();
                        System.out.println(Charset.defaultCharset().decode(byteBuffer));
                    }
                }
            }
            iter.remove();
        }

    }
}
