package org.fan.dorado.demo.client.thrift;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.fan.dorado.demo.api.GreetingService;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThriftClient {

    @Test
    public void testNormal() {
        try {
            TTransport transport = new TSocket("127.0.0.1", 9090);
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);

            final GreetingService.Client client = new GreetingService.Client(protocol);

            Utils.doGreetingRequest(client);

        } catch (TException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConcurrentError() {
        try {
            TTransport transport = new TSocket("127.0.0.1", 9090);
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);

            final GreetingService.Client client = new GreetingService.Client(protocol);

            final CountDownLatch countDownLatch = new CountDownLatch(2);

            ExecutorService executorService = Executors.newFixedThreadPool(10);
            for (int i = 0; i < 2; i++) {
                executorService.execute(new Runnable() {
                    public void run() {
                        try {
                            Utils.doGreetingRequest(client);
                        } catch (Exception e) {
                            System.out.println("exception occurred.....");
                            e.printStackTrace();
                        } finally {
                            System.out.println("count down latch......");
                            countDownLatch.countDown();
                        }
                    }
                });
            }

            try {
                countDownLatch.await();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("countdown latch Exception caught....");
            }
            transport.close();
            executorService.shutdownNow();
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception caught....");
        }
    }
}
