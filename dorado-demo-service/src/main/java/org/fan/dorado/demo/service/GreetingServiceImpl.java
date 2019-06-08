package org.fan.dorado.demo.service;

import org.apache.thrift.TException;
import org.fan.dorado.demo.api.GreetingService;
import org.fan.dorado.demo.bean.Response;
import org.fan.dorado.demo.bean.User;

public class GreetingServiceImpl implements GreetingService.Iface {
    public String sayHello(String name) throws TException {
        System.out.println("hello " + name);
        return name;
    }

    public Response sayBean(User user) throws TException {
        System.out.println("user: " + user);
        Response response = new Response((short) 200, "OK");
        return response;
    }
}
