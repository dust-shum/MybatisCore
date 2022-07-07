package com.dust.mybatis.plugin.intercept;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;

import java.sql.Connection;
import java.util.Properties;

/**
 * @author DUST
 * @description 自定义插件
 * @date 2022/7/7
 */
@Intercepts({ //注意看这个⼤花括号，也就这说这⾥可以定义多个@Signature对多个地⽅拦截，都⽤这个拦截器
    @Signature(type = StatementHandler.class , //这是指拦截哪个接⼝
            method = "prepare", //这个接⼝内的哪个⽅法名，不要拼错了
        args = { Connection.class, Integer .class}),
        // 这是拦截的⽅法的⼊参，按顺序写到这，不要多也不要少，如果⽅法重载，可是要通过⽅法名和⼊参来确定唯⼀的
})
public class MyPlugin implements Interceptor {

    /**
     * 逻辑增强方法
    */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        System.out.println("增强了该方法");
        return invocation.proceed();
    }

    /**
     * 包装⽬标对象 为⽬标对象创建代理对象
     * 主要是为了把这个拦截器⽣成⼀个代理放到拦截器链中
    */
    @Override
    public Object plugin(Object target) {
        System.out.println("将要包装的⽬标对象："+target);
        return Plugin.wrap(target,this);
    }

    /**
     * 获取配置⽂件的属性
     * 插件初始化的时候调⽤，也只调⽤⼀次，插件配置的属性从这⾥设置进来
    */
    @Override
    public void setProperties(Properties properties) {
        System.out.println("插件配置的初始化参数："+properties );
    }
}
