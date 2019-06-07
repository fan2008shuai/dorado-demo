package org.fan.dorado.demo.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DoradoBootstrap {
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("dorado-provider.xml");
    }
}
