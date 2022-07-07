package com.dust.io;

import java.io.InputStream;

/**
 * @author DUST
 * @description 加载映射声明xml文件成为流
 * @date 2022/5/25
 */
public class Resources {

    public static InputStream getResourceAsStream(String path){
        return Resources.class.getClassLoader().getResourceAsStream(path);
    }
}
