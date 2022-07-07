package com.dust.utils;

/**
 * @author DUST
 * @description
 * @date 2022/5/26
 */
public class GenericTokenParser {

    private final String openToken; //开始标记
    private final String closeToken; //结束标记
    private TokenHandler tokenHandler;

    public GenericTokenParser(String openToken, String closeToken, TokenHandler tokenHandler) {
        this.openToken = openToken;
        this.closeToken = closeToken;
        this.tokenHandler = tokenHandler;
    }

    /**
     * @author DUST
     * @description 解析sql语句中的 ${}和#{}
     * 该方法主要实现了配置文件、脚本等片段中占位符的解析、处理工作，并返回最终需要的数据。
     *    * 其中，解析工作由该方法完成，处理工作是由处理器handler的handleToken()方法来实现
     * @date 2022/5/27
     * @param text
     * @return java.lang.String
    */
    public String parse(String text) {
        if(text == null || text.isEmpty()){
            return "";
        }

        //下面继续验证是否包含开始标签，如果不包含，默认不是占位符，直接原样返回即可，否则继续执行。
        int start = text.indexOf(openToken);
        if(start == -1){
            return text;
        }

        // 把text转成字符数组src，并且定义默认偏移量offset=0、存储最终需要返回字符串的变量builder，
        char[] src = text.toCharArray();
        int offset = 0;
        final StringBuilder builder =  new StringBuilder();
        StringBuilder expression = null;
        //遍历sql语句，将对应的字符进行转换
        while(start > -1){
            //判断如果开始标记前如果有转义字符，就不作为openToken进行处理，否则继续处理
            if(start > 0 && src[start-1] == '\\'){
                builder.append(src,offset,start-offset-1).append(openToken);
                offset = start+openToken.length();
            }else{
                //重置expression变量，避免空指针或者老数据干扰。
                if(expression == null){
                    expression = new StringBuilder();
                }else{
                    expression.setLength(0);
                }
                builder.append(src,offset,start-offset);
                offset = start+openToken.length();
                int end = text.indexOf(closeToken, offset);
                //存在结束标记时
                while(end > -1){
                    //如果结束标记前面有转义字符时
                    if(end > offset && src[end-1] == '\\'){
                        expression.append(src,offset,end-offset-1).append(closeToken);
                        offset = end + closeToken.length();
                        end = text.indexOf(closeToken, offset);
                    }else{ //不存在转义字符，即需要作为参数进行处理
                        expression.append(src,offset,end-offset);
                        offset = end + closeToken.length();
                        break;
                    }
                }
                if(end == -1){
                    // close token was not found.
                    builder.append(src,start,src.length-start);
                    offset = src.length;
                }else{
                    //首先根据参数的key（即expression）进行参数处理，返回?作为占位符
                    builder.append(tokenHandler.handleToken(expression.toString()));
                    offset = end + closeToken.length();
                }
            }
            start = text.indexOf(openToken,offset);
        }

        if(offset < src.length){
            builder.append(src,offset,src.length-offset);
        }

        return builder.toString();
    }
}
