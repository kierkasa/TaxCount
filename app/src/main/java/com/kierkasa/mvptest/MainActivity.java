package com.kierkasa.mvptest;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

//public class MainActivity extends AppCompatActivity /*implements MainShow*/ {
    /*private TextView textView;
    private MyHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.button_url);
        handler = new MyHandler(this);
        final EditText editText = findViewById(R.id.edit_url);
        textView = findViewById(R.id.text_url);
        final ShowPresenter presenter = new ShowPresenter(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String url = editText.getText().toString();
                        presenter.getDataTrue(url);
                    }
                }).start();
            }
        });

        Log.d("MVP","Main thread id: " + getMainLooper().getThread().getId());

        Thread lThread = new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Handler lHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        Log.d("MVP", "looperThread id: " + Thread.currentThread().getId() + "msg: " + msg);
                    }
                };
                lHandler.sendMessage(lHandler.obtainMessage(0,"jkl"));
                Looper.loop();
            }
        };
        lThread.start();
    }


    @Override
    public void getMessage(String message) {
        Message msg = handler.obtainMessage(0, message);
        handler.sendMessage(msg);
    }

    @Override
    public void error() {
        Message msg = handler.obtainMessage(1, "error");
        handler.sendMessage(msg);
    }

    private static class MyHandler extends Handler {
        private WeakReference<MainActivity> reference;

        private MyHandler(MainActivity activity) {
            reference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MainActivity activity = reference.get();

            switch (msg.what) {
                case 0:
                    activity.textView.setText(msg.obj.toString());
                    Log.d("MVP", "thread id :" + Thread.currentThread().getId());
                    break;
                case 1:
                    activity.textView.setText(msg.obj.toString());
                    break;

            }
        }
    }*/


    /*ServerNIO serverNIO = new ServerNIO();
    ClientNIO clientNIO = new ClientNIO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.button_url);


        Log.d("NIO" ,"NIO create");
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("NIO" ,"NIO try");
                    serverNIO.initServer();
                    Log.d("NIO" ,"NIO init");
                    serverNIO.listen();
                    Log.d("NIO" ,"NIO listen");
                } catch (IOException e) {
                    Log.d("NIO", "error");
                }
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    clientNIO.initClient();
                    clientNIO.listen();
                } catch (IOException e) {
                    Log.d("NIO", "error client");
                }
            }
        });
        thread1.start();
        thread2.start();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("NIO", "button");
            }
        });
    }*/

    /*private MyService.TestBinder testBinder;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            testBinder = (MyService.TestBinder) service;
            testBinder.startTest();
            testBinder.stopTest();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            testBinder.stopTest();
        }
    };

    @Override
    protected void onCreate(Bundle savadInstanceState) {
        super.onCreate(savadInstanceState);
        setContentView(R.layout.activity_main);

        Intent bindIntent = new Intent(this, MyService.class);
        bindService(bindIntent, connection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }*/

public class MainActivity extends AppCompatActivity {

    //AIDL文件生成的java类
    private IBookManager iBookManager = null;

    //标志当前是否与服务端连接成功
    private boolean isBound = false;

    //包含BOOK对象的list
    private List<Book> mBooks;

    private TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.button_url);
        textView = findViewById(R.id.text_url);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBook(v);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!isBound) {
            attemptToBindService();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isBound) {
            unbindService(mServiceConnnection);
            isBound = false;
        }
    }

    public void addBook(View view) {
        if (!isBound) {
            attemptToBindService();
            textView.setText("当前与服务端未连接，正在重新连接");
            return;
        }
        if (iBookManager == null) {
            return;
        }

        Book book = new Book();
        book.setName("client book cc");
        book.setPrice(150);
        try {
            iBookManager.addBook(book);
            Log.d("AIDL", "from client add : " + book.toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void attemptToBindService() {
        Intent intent = new Intent(this, MyService.class);
        bindService(intent, mServiceConnnection, BIND_AUTO_CREATE);
    }

    private ServiceConnection mServiceConnnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("AIDL", "service connected");
            iBookManager = IBookManager.Stub.asInterface(service);
            isBound = true;

            if (iBookManager != null) {
                try {
                    mBooks = iBookManager.getBooks();
                    Log.d("AIDL", "from client : " + mBooks.toString());

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("AIDL", "service disconnected");
            isBound = false;
        }
    };



}
