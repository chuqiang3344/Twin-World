package com.tyaer.ProxyModel;

import org.assertj.core.internal.cglib.proxy.Enhancer;
import org.assertj.core.internal.cglib.proxy.MethodInterceptor;
import org.assertj.core.internal.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by Twin on 2016/6/28.
 */
public class CGLibProxy implements MethodInterceptor {
    private static  CGLibProxy instance=new CGLibProxy();

    private CGLibProxy(){}

    public  static CGLibProxy getInstance(){
        return instance;
    }

    //便于我们可以快速地获取自动生成的代理对象
    public <T> T getProxy(Class<T> cls){
        return (T) Enhancer.create(cls,this);
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        before();
        Object invoke = methodProxy.invokeSuper(o, objects);
        after();
        return invoke;
    }

    private void before() {
        System.out.println("Before");
    }

    private void after() {
        System.out.println("After");
    }

}
