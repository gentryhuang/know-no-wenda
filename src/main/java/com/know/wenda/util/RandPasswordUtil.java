package com.know.wenda.util;

import java.util.Random;

/**
 * RandPasswordUtil
 *
 * @author hlb
 */
public class RandPasswordUtil {
    /**
     * 获取密码
     * @return
     */
    public static String getPassword(){
        StringBuffer stringBuffer=new StringBuffer();
        //以当前时间生成random
        Random random=new  Random(System.currentTimeMillis());
        boolean flag=false;
        //设置默认密码的长度为1+6位
        int length=random.nextInt(1)+6;
        for (int i = 0; i < length; i++) {
            if(flag){
                stringBuffer.append(num[random.nextInt(num.length)]);
            }else{
                stringBuffer.append(word[random.nextInt(word.length)]);
            }
            flag=!flag;
        }
        return stringBuffer.toString();

    }
    public final static String[] word={
            "a","b","c","d","e","f",
            "g","h","j","k","m","n",
            "p","q","r","s","t","u",
            "v","w","x","y","z",
            "A","B","C","D","E","F",
            "G","H","J","K","M","N",
            "P","Q","R","S","T","U",
            "V","W","X","Y","Z",
    };

    public static String[] num={
            "2","3","4","5","6","7","8","9"
    };


}