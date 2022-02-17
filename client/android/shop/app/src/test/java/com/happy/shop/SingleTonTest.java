package com.happy.shop;

import com.happy.shop.util.Utils;

import org.junit.Test;

/**
 * 双重检查锁单例单元测试
 * 创建多个线程，检查Utils对象的创建是否只执行了一次
 */
public class SingleTonTest {
    @Test
    public void isSameInstance(){
        final int length = 50;
        Thread[] threads = new Thread[length];
        Utils[] singleTons = new Utils[length];
        for(int i=0; i<threads.length;i++){
            final int index = i;
            Thread t = new Thread(() -> {
                System.out.println("Thread id: "+Thread.currentThread().getId());
                singleTons[index] = Utils.getInstance(); // 获取Utils对象
            });
            threads[i] = t;
            t.start();
        }

        // 主线程等待所有子线程运行结束
        for(int i=0; i<length; i++){
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }// join(): Waits for this thread to die.

        Utils singleTon = singleTons[0];
        int i = 1;
        boolean bSame = true;
        while (i<singleTons.length){
            bSame = singleTon==singleTons[i]?true:false;
            singleTon = singleTons[i];
            if(!bSame){
                break;
            }
            i++;
        }
        if(!bSame){
            System.out.println("The initialization of singleton is failed!");
        }
        else {
            System.out.println("The initialization of singleton is successful!");
        }
        // 最终运行结果为: The initialization of singleton is successful!
    }
}
