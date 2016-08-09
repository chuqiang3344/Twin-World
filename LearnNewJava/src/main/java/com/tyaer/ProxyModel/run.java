package com.tyaer.ProxyModel;

/**
 * Created by Twin on 2016/6/28.
 */
public class run {
    public static void main(String[] args) {
        HelloImpl helloImpl = CGLibProxy.getInstance().getProxy(HelloImpl.class);
        helloImpl.say("大家好！");
    }
}
