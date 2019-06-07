package org.fan.dorado.demo.service;

import org.apache.thrift.TException;
import org.fan.dorado.demo.api.GreetingService;

public class GreetingServiceImpl implements GreetingService.Iface {
    public String sayHello(String name) throws TException {
        System.out.println("hello " + name);
        return name;
    }
}
