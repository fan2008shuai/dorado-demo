package org.fan.dorado.demo.server;

import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.fan.dorado.demo.api.GreetingService;
import org.fan.dorado.demo.service.GreetingServiceImpl;

public class ThriftBootstrap {
    public static void main(String[] args) {

        try {

            TServerTransport serverTransport = new TServerSocket(9090);

            TBinaryProtocol.Factory proFactory = new TBinaryProtocol.Factory();

            /**
             * 关联处理器与GreetingService服务实现
             */
            TProcessor processor = new GreetingService.Processor(new GreetingServiceImpl());

            TThreadPoolServer.Args serverArgs = new TThreadPoolServer.Args(serverTransport);
            serverArgs.processor(processor);
            serverArgs.protocolFactory(proFactory);
            TServer server = new TThreadPoolServer(serverArgs);
            System.out.println("Start server on port 9090....");

            server.serve();
        } catch (TTransportException e) {
            e.printStackTrace();
        }
    }
}
