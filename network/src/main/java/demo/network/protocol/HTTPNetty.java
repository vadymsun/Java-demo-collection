package demo.network.protocol;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;

public class HTTPNetty {
    public static void main(String[] args) {
        NioEventLoopGroup boss =  new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup(10);


        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.group(boss, worker);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {

                    // 既是入站处理器也是出站处理器
                    socketChannel.pipeline().addLast(new HttpServerCodec());

                    // SimpleChannelInboundHandler只处理符合泛型类型的msg类型，忽略其他类型
                    socketChannel.pipeline().addLast(new SimpleChannelInboundHandler<HttpRequest>() {

                        @Override
                        protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpRequest httpRequest) throws Exception {
                            // 获取请求信息
                            System.out.println("请求头");
                            System.out.println(httpRequest.uri());
                            System.out.println(httpRequest.headers());

                            // 返回响应 HttpServerCodec识别该类型
                            // 非空参数 http协议版本和相应码
                            DefaultFullHttpResponse httpResponse = new DefaultFullHttpResponse(
                                    httpRequest.protocolVersion(),
                                    HttpResponseStatus.OK
                            );
                            // 写入content
                            byte[] bytes = "<h1>Hello netty</h1>".getBytes();
                            // 告知浏览器数据的长度
                            httpResponse.headers().setInt(CONTENT_LENGTH, bytes.length);
                            httpResponse.content().writeBytes(bytes);

                            // 写回相应
                            channelHandlerContext.writeAndFlush(httpResponse);

                        }
                    });
                  /*  socketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            // netty将一个请求分成两个 DefaultHttpRequest （请求头） LastHttpContent$1（请求体）
                            System.out.println( "数据类型是"+msg.getClass());
                            if(msg instanceof HttpContent){
                                System.out.println("收到请求体");
                            } else if (msg instanceof HttpRequest) {
                                System.out.println("收到请求头");
                            }
                        }
                    });*/
                }
            });
            ChannelFuture channelFuture = serverBootstrap.bind(8888).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
            throw new RuntimeException(e);
        }
    }
}
