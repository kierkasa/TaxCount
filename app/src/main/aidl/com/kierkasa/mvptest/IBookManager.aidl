// IBookManager.aidl
package com.kierkasa.mvptest;

//第二类AIDL文件类型
// Declare any non-default types here with import statements
import com.kierkasa.mvptest.Book;

interface IBookManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
     //所有的返回值前都不需要加任何东西，不管是什么数据类型
    List<Book> getBooks();

    //传参时除了java基本类型以及String、CharSequence之外的类型
    //都需要在前面加上定向tag
    void addBook(in Book book);
}
