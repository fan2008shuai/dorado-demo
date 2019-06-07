package org.fan.dorado.demo.client.dorado;

import com.meituan.dorado.bootstrap.ServiceBootstrap;
import org.fan.dorado.demo.api.GreetingService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DoradoClient {
    public static void main(String[] args) {
        try {
            ClassPathXmlApplicationContext beanFactory = new ClassPathXmlApplicationContext("dorado-consumer.xml");

            GreetingService.Iface userservice = (GreetingService.Iface) beanFactory.getBean("helloService");
            System.out.println(userservice.sayHello("fan"));

            beanFactory.destroy();
            ServiceBootstrap.clearGlobalResource();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
