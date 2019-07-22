package com.kierkasa.mvptest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MyService extends Service {

    public final String TAG = "AIDL";

    //包含Book对象的list
    private List<Book> mBooks = new ArrayList<>();

    //由AIDL文件生成的IBookManager
    private final IBookManager.Stub mIBookManager = new IBookManager.Stub() {
        @Override
        public List<Book> getBooks() throws RemoteException {
            synchronized (this) {
                Log.d(TAG, " invoking getBooks() method, now the list is : " + mBooks.toString());
                if (mBooks != null) {
                    return mBooks;
                }
                return new ArrayList<>();
            }
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            synchronized (this) {
                if (mBooks == null) {
                    mBooks = new ArrayList<>();
                }

                if (book == null) {
                    Log.d(TAG, "book is null in IN");
                    book = new Book();
                }

                //尝试修改book参数，观察在客户端是否有变化
                book.setPrice(333999);
                if (!mBooks.contains(book)) {
                    mBooks.add(book);
                }

                //打印mBooks列表，观察客户端传来的值
                Log.d(TAG, " invoking addBook() method, now list is : " + mBooks.toString());
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Book book = new Book();
        book.setName("Service Book ll");
        book.setPrice(200);
        mBooks.add(book);
    }

    public MyService() {
    }

    /*private TestBinder mBinder = new TestBinder();

    class TestBinder extends Binder {
        public void startTest() {
            Log.d("Binder", "start test");
        }

        public int stopTest() {
            Log.d("Binder", "stop test");
            return 0;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }*/

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "bind start connecte : " + String.format("on bind, intent = %s", intent.toString()));
        return mIBookManager;
    }
}
