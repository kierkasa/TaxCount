package com.kierkasa.mvptest;

import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class ServerNIO {
    private Selector selector;
    private ByteBuffer readBuffer = ByteBuffer.allocate(1024);//调整缓存大小
    private ByteBuffer writeBuffer = ByteBuffer.allocate(1024);

    String str;
    String str3 = "全局string";
    public void initServer() throws IOException {
        Log.d("NIO", "init start");
        ServerSocketChannel serviceSocketChannel = ServerSocketChannel.open();
        serviceSocketChannel.configureBlocking(false);
        Log.d("NIO", "channel create");
        serviceSocketChannel.socket().bind(new InetSocketAddress(13911));
        Log.d("NIO", "bind port");
        this.selector = Selector.open();
        Log.d("NIO", "selector create");
        serviceSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        Log.d("NIO", "register");
    }

    public void listen() throws IOException {
        Log.d("NIO", "服务启动成功");
        while(true) {
            selector.select();
            Iterator iterator = this.selector.selectedKeys().iterator();
            while(iterator.hasNext()) {
                SelectionKey key = (SelectionKey) iterator.next();
                iterator.remove();
                if (!key.isValid()) {
                    continue;
                }
                if (key.isAcceptable()) {
                    accept(key);
                }
                if (key.isReadable()) {
                    read(key);
                }
                if (key.isWritable()) {
                    write(key ,str3);
                }
            }
        }
    }

    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = serverSocketChannel.accept();
        clientChannel.configureBlocking(false);
        clientChannel.write(ByteBuffer.wrap(new String("来自服务端，发向客户端").getBytes()));
        clientChannel.register(selector, SelectionKey.OP_READ);
        Log.d("NIO", "client connected " + clientChannel.getRemoteAddress());
    }

    public void read(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();

        this.readBuffer.clear();
        int numRead;
        try {
            numRead = socketChannel.read(this.readBuffer);
        } catch (IOException e) {
            key.cancel();
            socketChannel.close();
            return;
        }
        Log.d("NIO", "readBuffer.array() : " + readBuffer.array());
        str = new String(readBuffer.array(), 0, numRead);
        Log.d("NIO", "message " + str);
        this.write(key, "read 发出");
        socketChannel.register(selector, SelectionKey.OP_WRITE);
    }

    public void write(SelectionKey key ,String str2) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        Log.d("NIO", "write : " + str2);

        writeBuffer.clear();
        writeBuffer.put(str2.getBytes());
        writeBuffer.flip();
        socketChannel.write(writeBuffer);
        socketChannel.register(selector, SelectionKey.OP_READ);
    }
}
