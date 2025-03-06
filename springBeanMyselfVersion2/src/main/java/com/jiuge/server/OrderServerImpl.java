package com.jiuge.server;

import com.jiuge.server.OrderServer;
import com.jiuge.server.UserServer;
import com.spring.Autowired;
import com.spring.Compontent;

/**
 * @author Administrator
 */
@Compontent("orderServer")
public class OrderServerImpl implements OrderServer {


    @Autowired
    private UserServer userServer;


    @Override
    public void test() {

        userServer.test();
        System.out.println("hello orderServer");

    }
}
