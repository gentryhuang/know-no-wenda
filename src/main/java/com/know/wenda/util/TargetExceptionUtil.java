package com.know.wenda.util;

import com.know.wenda.execption.ConsumerException;
import com.know.wenda.execption.GlobalErrorEnum;
import com.know.wenda.vo.ExceptionVO;

import javax.servlet.http.HttpServletRequest;

/**
 * TargetExceptionUtil
 *
 * @author hlb
 */
public class TargetExceptionUtil {

    /**
     * 处理自定义异常，然后跳转页面
     *
     * @param e
     * @return
     */
    public static void toErrorVivw(ConsumerException e, HttpServletRequest request) {
        // 通过枚举静态方法values()遍历
        for (GlobalErrorEnum ex : GlobalErrorEnum.values()) {
            if (e.getCode() == ex.getCode()) {
                String message = e.getMessage();
                ExceptionVO exceptionVO = new ExceptionVO();
                exceptionVO.setMsg(message);
                exceptionVO.setCode(e.getCode());
                request.setAttribute("exceptionVO", exceptionVO);
                break;
            }
        }
    }
}