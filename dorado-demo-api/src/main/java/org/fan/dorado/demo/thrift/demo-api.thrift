namespace java org.fan.dorado.demo.api

include "bean.thrift"
typedef bean.Response Response
typedef bean.User User

service  GreetingService {
  string sayHello(1:string name);
  Response sayBean(1: User user);
}