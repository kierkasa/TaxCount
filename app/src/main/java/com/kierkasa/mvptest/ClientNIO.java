package com.kierkasa.mvptest;

import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class ClientNIO {
    private Selector selector;
    private ByteBuffer readBuffer = ByteBuffer.allocate(1024);//调整缓存大小
    private ByteBuffer writeBuffer = ByteBuffer.allocate(1024);

    String str;
    String str3 = "客户端全局string";

    public void initClient() throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        this.selector = Selector.open();

        socketChannel.connect(new InetSocketAddress("localhost", 13911));
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
    }

    public void listen() throws IOException {
        Log.d("NIO", "客户端启动成功");
        while(true) {
            selector.select();
            Iterator iterator = this.selector.selectedKeys().iterator();
            while(iterator.hasNext()) {
                SelectionKey key = (SelectionKey) iterator.next();
                iterator.remove();
                if (!key.isValid()) {
                    continue;
                }
                if (key.isConnectable()) {
                    connect(key);
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

    private void connect(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        if (socketChannel.isConnectionPending()) {
            socketChannel.finishConnect();
        }
        socketChannel.configureBlocking(false);
        socketChannel.write(ByteBuffer.wrap(new String("来自客户端，发向服务器").getBytes()));
        socketChannel.register(selector, SelectionKey.OP_READ);

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
        this.write(key, "客户端read 发出");
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
