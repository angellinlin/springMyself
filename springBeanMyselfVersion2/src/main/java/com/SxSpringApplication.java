package com;

import com.jiuge.server.OrderServer;
import com.jiuge.server.UserServer;
import com.spring.JiugeApplictionContent;

public class SxSpringApplication {
    public static void main(String[] args) {
        JiugeApplictionContent jiugeApplictionContent = new JiugeApplictionContent(Appconfig.class);

        UserServer userServer = (UserServer) jiugeApplictionContent.getBean("userServer");
        // UserServer userServer1 = (UserServer) jiugeApplictionContent.getBean("userServer");
        // UserServer userServer2 = (UserServer) jiugeApplictionContent.getBean("userServer");

        // System.out.println("userServer == " + userServer);

        // System.out.println("userServer1 == " + userServer1);

        // System.out.println("userServer2 == " + userServer2);

        userServer.test();

        OrderServer orderServer = (OrderServer) jiugeApplictionContent.getBean("orderServer");

        orderServer.test();
    }


}
