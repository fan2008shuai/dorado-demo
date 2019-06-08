package org.fan.dorado.demo.client.thrift;

import org.apache.thrift.TException;
import org.fan.dorado.demo.api.GreetingService;
import org.fan.dorado.demo.bean.Other;
import org.fan.dorado.demo.bean.Response;
import org.fan.dorado.demo.bean.User;

public class Utils {
    public static void doGreetingRequest(GreetingService.Client client) {
        try {
            String name = "fan";
            System.out.println("请求参数==>name为: " + name);
            String result = client.sayHello(name);
            System.out.println("返回结果==>为" + result);

            User user = new User();
            user.setName("fan");
            user.setAge((short) 30);
            user.setGender(true);
            Other other = new Other("beijing", "15800008888");
            user.setOther(other);
            System.out.println("请求参数==>user为: " + user);
            Response response = client.sayBean(user);
            System.out.println("返回结果==>为" + response);
        } catch (TException e) {
            e.printStackTrace();
        }
    }
}
