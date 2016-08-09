package com.tyaer.ProxyModel;

/**
 * Created by Twin on 2016/6/28.
 */
public class HelloImpl implements Hello{

    @Override
    public void say(String str) {
        System.out.println(str);
    }
}
