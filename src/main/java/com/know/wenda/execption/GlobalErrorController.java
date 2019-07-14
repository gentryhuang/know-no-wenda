
package com.know.wenda.execption;


import com.alibaba.fastjson.JSON;
import com.know.wenda.system.config.ApiEnvironmentConfig;
import com.know.wenda.system.logger.KnwLoggerFactory;
import com.know.wenda.system.logger.KnwLoggerMarkers;
import com.know.wenda.system.loggerfactory.LoggerUtil;
import com.know.wenda.util.TargetExceptionUtil;
import com.know.wenda.vo.ExceptionVO;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 统一异常处理
 * GlobalErrorController
 *
 * @author hlb
 */
@ControllerAdvice
//@EnableWebMvc
public class GlobalErrorController {

    @Resource
    private ApiEnvironmentConfig apiEnvironmentConfig;

    /**
     * 自定义异常处理
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(value = {ConsumerException.class})
    public String handConsumerException(HttpServletRequest request, ConsumerException e) {
        // TODO 异常捕获时，一定要注意哪些@ResponseBody 返回给前端处理的结果，应该特殊处理以下，使用HttpServletResponse 返回
        TargetExceptionUtil.toErrorVivw(e, request);
        writeLog(request, e.getMessage(), e);
        return "error";
    }


    /**
     * 转换异常
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(value = {ConversionNotSupportedException.class})
    public String handConversionNotSupportedException(HttpServletRequest request, ConversionNotSupportedException e) {
        String message = e.getMessage() + JSON.toJSONString(e.getStackTrace());
        writeLog(request, message, e);
        ExceptionVO exceptionVO = new ExceptionVO();
        exceptionVO.setCode(GlobalErrorEnum.CONVERSION_NOTSUPPORTED_EXCEPTION.getCode());
        exceptionVO.setMsg(message);
        request.setAttribute("exceptionVO", exceptionVO);
        return "error";
    }

    /**
     * 没有对应的处理方法
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(value = {NoHandlerFoundException.class, NoSuchRequestHandlingMethodException.class})
    public String handNoHandlerFoundException(HttpServletRequest request, Exception e) {
        String message = GlobalErrorEnum.NO_HANDLER_FOUND_EXCEPTION.getMessage();
        writeLog(request, message, e);
        ExceptionVO exceptionVO = new ExceptionVO();
        exceptionVO.setCode(GlobalErrorEnum.NO_HANDLER_FOUND_EXCEPTION.getCode());
        exceptionVO.setMsg(message);
        request.setAttribute("exceptionVO", exceptionVO);
        return "error";
    }

    /**
     * 请求的方法不支持对应的请求
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    public String handMethodNotSupportedException(HttpServletRequest request, HttpRequestMethodNotSupportedException e) {
        String[] supportedMethods = e.getSupportedMethods();
        String message = e.getMessage() + ", supportedMethods: " + org.springframework.util.StringUtils.arrayToDelimitedString(supportedMethods, ", ");
        writeLog(request, message, e);
        ExceptionVO exceptionVO = new ExceptionVO();
        exceptionVO.setCode(GlobalErrorEnum.HTTP_REQUEST_METHOD_NOT_SUPPORTED_EXCEPTION.getCode());
        exceptionVO.setMsg(message);
        request.setAttribute("exceptionVO", exceptionVO);
        return "error";

    }

    /**
     * 不支持对应的媒介类型
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(value = {HttpMediaTypeNotSupportedException.class})
    public String handHttpMediaTypeNotSupportedException(HttpServletRequest request, HttpMediaTypeNotSupportedException e) {
        List<MediaType> mediaTypes = e.getSupportedMediaTypes();
        StringBuilder messageBuild = new StringBuilder();
        messageBuild.append("message:").append(e.getMessage());
        if (!CollectionUtils.isEmpty(mediaTypes)) {
            messageBuild.append("mediaTypes:")
                    .append(MediaType.toString(mediaTypes));
        }
        String message = messageBuild.toString();
        writeLog(request, message, e);
        ExceptionVO exceptionVO = new ExceptionVO();
        exceptionVO.setCode(GlobalErrorEnum.HTTP_MEDIA_TYPE_NOT_SUPPORTED_EXCEPTION.getCode());
        exceptionVO.setMsg(message);
        request.setAttribute("exceptionVO", exceptionVO);
        return "error";
    }

    /**
     * 丢失请求路径（{path}）
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(value = {MissingPathVariableException.class})
    public String handMissingPathVariableException(HttpServletRequest request, MissingPathVariableException e) {
        String message = "url error，lack placeholder ！ ";
        writeLog(request, message, e);
        ExceptionVO exceptionVO = new ExceptionVO();
        exceptionVO.setCode(GlobalErrorEnum.MISSING_PATH_VARIABLE_EXCEPTION.getCode());
        exceptionVO.setMsg(message);
        request.setAttribute("exceptionVO", exceptionVO);
        return "error";
    }

    /**
     * 丢失参数
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    public String handMissingServletRequestParameterException(HttpServletRequest request, MissingServletRequestParameterException e) {
        String message = "request error ，lack params，please check your params ！";
        writeLog(request, message, e);
        ExceptionVO exceptionVO = new ExceptionVO();
        exceptionVO.setCode(GlobalErrorEnum.MISSING_SERVLET_REQUEST_PARAMETER_EXCEPTION.getCode());
        exceptionVO.setMsg(message);
        request.setAttribute("exceptionVO", exceptionVO);
        return "error";
    }


    /**
     * 数据转换异常
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(value = {HttpMediaTypeNotAcceptableException.class})
    public String handConversionNotSupportedException(HttpServletRequest request, HttpMediaTypeNotAcceptableException e) {
        String message = GlobalErrorEnum.SERVLET_REQUEST_BINDING_EXCEPTION.getMessage();
        writeLog(request, message, e);
        ExceptionVO exceptionVO = new ExceptionVO();
        exceptionVO.setCode(GlobalErrorEnum.HTTP_MEDIA_TYPE_NOT_ACCEPTABLE_EXCEPTION.getCode());
        exceptionVO.setMsg(message);
        request.setAttribute("exceptionVO", exceptionVO);
        return "error";
    }


    /**
     * 参数绑定失败
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(value = {ServletRequestBindingException.class})
    public String handServletRequestBindingException(HttpServletRequest request, ServletRequestBindingException e) {
        String message = GlobalErrorEnum.SERVLET_REQUEST_BINDING_EXCEPTION.getMessage();
        writeLog(request, message, e);
        ExceptionVO exceptionVO = new ExceptionVO();
        exceptionVO.setCode(GlobalErrorEnum.SERVLET_REQUEST_BINDING_EXCEPTION.getCode());
        exceptionVO.setMsg(message);
        request.setAttribute("exceptionVO", exceptionVO);
        return "error";
    }

    /**
     * 处理其他异常
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(value = {Exception.class})
    public String handOtherException(HttpServletRequest request, Exception e) {
        String message = GlobalErrorEnum.REQUEST_FAIL_EXCEPTION.getMessage();
        writeLog(request, message, e);
        ExceptionVO exceptionVO = new ExceptionVO();
        exceptionVO.setMsg(message);
        request.setAttribute("exceptionVO", exceptionVO);
        return "error";
    }

    private void writeLog(HttpServletRequest request, String message, Exception e) {
        try {
            StringBuilder builder = new StringBuilder();
            Map<String, String[]> params = request.getParameterMap();

            builder.append("url:").append(request.getRequestURL())
                    .append(", remote user:").append(request.getRemoteUser())
                    .append(", method:").append(request.getMethod())
                    .append(", auth type:").append(request.getAuthType())
                    .append(", remoteAddr:").append(request.getRemoteAddr())
                    .append(", serverName:").append(request.getServerName())
                    .append(", params:").append(JSON.toJSONString(params));
            LoggerUtil.error(KnwLoggerFactory.EXCEPTION_HANDLER_LOGGER, KnwLoggerMarkers.EXCEPTION_HANDLER, e, builder.toString(), message);
        } catch (Exception ex) {
            LoggerUtil.error(KnwLoggerFactory.EXCEPTION_HANDLER_LOGGER, KnwLoggerMarkers.EXCEPTION_HANDLER, ex);
        }
    }
}
