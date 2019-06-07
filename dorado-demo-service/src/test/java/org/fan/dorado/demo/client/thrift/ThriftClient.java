package org.fan.dorado.demo.client.thrift;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.fan.dorado.demo.api.GreetingService;

public class ThriftClient {
    public static void main(String[] args) {
        try {
            TTransport transport = new TSocket("127.0.0.1", 9090);
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);

            GreetingService.Client client = new GreetingService.Client(protocol);

            String name = "Eric";
            System.out.println("请求参数==>name为: " + name);
            String result = client.sayHello("Eric");
            System.out.println("返回结果==>为" + result);
            transport.close();
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        }
    }
}
