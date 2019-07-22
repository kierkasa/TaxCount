// Book.aidl
package com.kierkasa.mvptest;

//第一类AIDL文件类型
//这个文件的作用是引入了一个序列化对象Book供其他AIDL文件使用
//注意：Book.aidl与Book.java的包名是一样的

parcelable Book;