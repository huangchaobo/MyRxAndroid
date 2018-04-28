package com.huang.cn.myrxandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {
    String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void button1(View view) {
        simpleBind();

    }

    public void button2(View view) {
        beanType();

    }

    public void button3(View view) {
        justType();

    }

    public void button4(View view) {
        timeType();

    }

    public void button5(View view) {
        mapType();

    }

    public void button6(View view) {
        flatMapType();

    }
    /**
     * flatMap()将一个发射数据的Observable变换为多个Observables，
     * 然后将它们发射的数据合并后放进一个单独的Observable。
     * 即：第一次转换时，它依次将输入的数据转换成一个Observable，
     * 然后将这些Observable发射的数据集中到一个Observable里依次发射出来
     * */
    private void flatMapType() {
        Integer[] array1 = {1, 2, 3, 4}, array2 = {5, 6, 7, 8};
        Observable.just(array1,array2).flatMap(new Func1<Integer[], Observable<?>>() {
            @Override
            public Observable<?> call(Integer[] integers) {
                return Observable.from(integers);
            }
        })
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object o) {
                        Log.d(TAG, "onNext==" + o.toString());
                    }
                });
    }

    private void mapType() {
        /**
         * map()方法是最基本的变换操作只能变换一次，这里只变换了一个数据，这边是把110数字转换为110huang字符串
         * */
        Observable.just(110).map(new Func1<Integer, String>() {
            @Override
            public String call(Integer integer) {
                return integer + "huang";
            }
        })
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        Log.d(TAG, "onNext==" + s);
                    }
                });

    }

    private void timeType() {
        //延迟执行
//        Observable<Long> timerObservable = Observable.timer(5, TimeUnit.SECONDS, AndroidSchedulers.mainThread());

        //间隔执行，timer已废弃
        //每隔5妙执行一次
//        Observable<Long> timerObservable = Observable.interval(5, TimeUnit.SECONDS, AndroidSchedulers.mainThread());
        Observable.interval(5, TimeUnit.SECONDS,
                AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted==");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.d(TAG, "onNext==" + aLong);
                    }
                });

    }

    private void justType() {
        //Just类似于From，但是From会将数组或Iterable的元素具取出然后逐个发射，而Just只是简单的原样发射，将数组或Iterable当做单个数据。
        //Just接受一至九个参数，返回一个按参数列表顺序发射这些数据的Observable
        Observable<Object> justObservable = Observable.just("huang", 333, new User("huang", 22));
        justObservable.subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "just(...)  onCompleted");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {
                Log.d(TAG, "justType==" + o.toString());
            }
        });
    }

    /**
     * 根据bean生成被观察者
     */
    private void beanType() {
        ArrayList<User> users = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            User user = new User();
            user.setAge(22);
            user.setName("huang" + i);
            users.add(user);
        }
        /**
         * 　用from方法创建Observable，可以传入一个数组，或者一个继承了Iterable的类的对象作为参数，
         * 也就是说，java中常用的数据结构如List、Map等都可以直接作为参数传入from()方法用以构建Observable。这样，
         * 当Observable发射数据时，它将会依次把序列中的元素依次发射出来。
         * */
        Observable<User> fromObservable = Observable.from(users);
        fromObservable.subscribe(new Subscriber<User>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(User user) {
                Log.d(TAG, "用户信息" + user.toString());
            }
        });

    }

    /**
     * 简单绑定
     */
    private void simpleBind() {
        //创建发布者，被观察者
        Observable observable = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(1001);
//                subscriber.onNext(1002);
//                subscriber.onNext(1003);
                Log.d(TAG, "call线程名称==" + Thread.currentThread().getName() + "");
            }


        });
        //创建观察者
        Subscriber<Integer> subscriber = new Subscriber<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, integer + "================================");
                Log.d(TAG, "onNext线程名称==" + Thread.currentThread().getName() + "");

            }
        };
        //创立关联
        /**
         * AndroidSchedulers.mainThread()指主线程
         * Schedulers.immediate() 当前线程
         * 　Schedulers.io(): I/O 操作（读写文件、读写数据库、网络信息交互等）
         * Schedulers.computation(): 计算所使用的 Scheduler。
         * */
        observable
                //subscribeOn改变的是call()执行的线程;
                .subscribeOn(Schedulers.io())
                //改变的是onNext执行线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
