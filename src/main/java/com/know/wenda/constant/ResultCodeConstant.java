package com.know.wenda.constant;

/**
 * ResultCodeConstant
 *
 * @author hlb
 */
public class ResultCodeConstant {

    public interface Code{
        /**
         * 状态码
         */
        Integer CODE = 200;

        /**
         * 过快点击
         */
        Integer FAST = 300;


        /**
         * 失败操作
         */
        Integer FALSE = 400;


    }

    public interface Result{
        /**
         * 结果
         */
        String OK = "OK";
        /**
         * 过快操作
         */
        String FAST = "操作过于频繁，请稍后重试！";
        /**
         * 操作失败
         */
        String FALSE = "操作失败！";
    }

}