package org.fan.dorado.demo.client.thrift;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;
import org.fan.dorado.demo.api.GreetingService;
import org.fan.dorado.demo.bean.Other;
import org.fan.dorado.demo.bean.Response;
import org.fan.dorado.demo.bean.User;
import org.fan.dorado.demo.common.ThriftClientPool;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PooledThriftClient {

    private static final int CONCURRENT_COUNT = 10;

    @Test
    public void testConnectionPool() {
        GenericObjectPool.Config poolConfig = new GenericObjectPool.Config();
        poolConfig.maxActive = 80;
        poolConfig.minIdle = 5;
        poolConfig.whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_BLOCK;
        poolConfig.testOnBorrow = true;
        poolConfig.testWhileIdle = true;
        poolConfig.numTestsPerEvictionRun = 10;
        poolConfig.maxWait = 3000;

        final ThriftClientPool<GreetingService.Client> pool = new ThriftClientPool<GreetingService.Client>(
                new ThriftClientPool.ClientFactory<GreetingService.Client>() {
                    @Override
                    public GreetingService.Client make(TProtocol tProtocol) {
                        return new GreetingService.Client(tProtocol);
                    }
                }, poolConfig, "localhost", 9090);

        final CountDownLatch countDownLatch = new CountDownLatch(CONCURRENT_COUNT);

        ExecutorService executorService = Executors.newFixedThreadPool(CONCURRENT_COUNT);
        for (int i = 0; i < CONCURRENT_COUNT; i++) {
            executorService.execute(new Runnable() {
                public void run() {
                    GreetingService.Client client = null;
                    try {
                        client = pool.getResource();
                        Utils.doGreetingRequest(client);
                    } catch (Exception e) {
                        if (client != null) {
                            pool.returnBrokenResource(client);
                        }

                        System.out.println("exception occurred.....");
                        e.printStackTrace();
                    } finally {
                        if (client != null) {
                            pool.returnResource(client);
                        }

                        System.out.println("count down latch......");
                        countDownLatch.countDown();
                    }
                }
            });
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            System.out.println("countdown latch await exception...");
            e.printStackTrace();
        }
        executorService.shutdownNow();
    }


    @Test
    public void testConnectionPool2() {
        final ThriftClientPool<GreetingService.Client> pool = new ThriftClientPool<GreetingService.Client>(
                new ThriftClientPool.ClientFactory<GreetingService.Client>() {
                    @Override
                    public GreetingService.Client make(TProtocol tProtocol) {
                        return new GreetingService.Client(tProtocol);
                    }
                }, new ThriftClientPool.ProtocolFactory() {
            public TProtocol make() {
                TFramedTransport transport = new TFramedTransport(
                        new TSocket("localhost", 9090));
                try {
                    transport.open();
                } catch (TTransportException e) {
                    throw new ThriftClientPool.ThriftClientException(
                            "Can not make protocol", e);
                }
                return new TBinaryProtocol(transport);
            }
        }, new GenericObjectPool.Config());

        final ThriftClientPool<GreetingService.Client> lambdaPool = new ThriftClientPool<GreetingService.Client>(
                GreetingService.Client::new, () -> {
                    TFramedTransport transport = new TFramedTransport(
                            new TSocket("localhost", 9090));
                    try {
                        transport.open();
                    } catch (TTransportException e) {
                        throw new ThriftClientPool.ThriftClientException(
                                "Can not make protocol", e);
                    }
                    return new TBinaryProtocol(transport);
                }, new GenericObjectPool.Config());

    }

}
