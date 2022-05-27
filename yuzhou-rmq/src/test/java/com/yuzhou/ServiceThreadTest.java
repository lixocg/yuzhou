package com.yuzhou;

import com.yuzhou.rmq.concurrent.ServiceThread;
import org.apache.commons.lang3.time.FastDateFormat;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ServiceThreadTest {

    static class HandlerService extends ServiceThread{

        @Override
        public void run() {
            while (this.isRunning()) {
                System.out.println("run..."+ FastDateFormat.getInstance("yyyy-hh-dd HH:mm:ss").format(new Date()));
                try {
                    TimeUnit.SECONDS.sleep(1);
                    this.waitForRunning();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onWaitEnd() {
            super.onWaitEnd();
            System.out.println("wait end....");
        }

        @Override
        public String getServiceName() {
            return this.getClass().getSimpleName();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        HandlerService handlerService = new HandlerService();
        handlerService.start();

        while (true) {
//            TimeUnit.SECONDS.sleep(5);
            handlerService.wakeup();
        }

    }
}
