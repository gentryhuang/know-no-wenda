
package com.know.wenda.execption;


import javax.servlet.http.HttpServletResponse;

/**
 * ErrorEnum
 *
 * @author hlb
 */
public enum GlobalErrorEnum {
    // 系统异常
    REQUEST_FAIL_EXCEPTION(0, "服务异常,请稍后重试! "),
    NO_HANDLER_FOUND_EXCEPTION(HttpServletResponse.SC_NOT_FOUND, "DispatcherServlet noHandlerFoundException"),
    HTTP_REQUEST_METHOD_NOT_SUPPORTED_EXCEPTION(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "DispatcherServlet HttpRequestMethodNotSupportedException"),
    HTTP_MEDIA_TYPE_NOT_SUPPORTED_EXCEPTION(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "DispatcherServlet HttpMediaTypeNotSupportedException"),
    MISSING_PATH_VARIABLE_EXCEPTION(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "DispatcherServlet MissingPathVariableException"),
    MISSING_SERVLET_REQUEST_PARAMETER_EXCEPTION(HttpServletResponse.SC_BAD_REQUEST, "DispatcherServlet MissingServletRequestParameterException"),
    SERVLET_REQUEST_BINDING_EXCEPTION(HttpServletResponse.SC_BAD_REQUEST, "DispatcherServlet ServletRequestBindingException"),
    CONVERSION_NOTSUPPORTED_EXCEPTION(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "DispatcherServlet ConversionNotSupportedException"),
    HTTP_MEDIA_TYPE_NOT_ACCEPTABLE_EXCEPTION(HttpServletResponse.SC_NOT_ACCEPTABLE, "DispatcherServlet HttpMediaTypeNotAcceptableException"),
    // 自定义一些业务异常
    SEARCH_CONDITION_NULL_EXCEPTION(1000, "搜索参数不能为空，小主请再试试哟！"),
    SETTING_ERROR_USER_EXCEPTION(1001, "没有这个用户，小主请再检查下啦！"),
    SETTING_ERROR_PASSWORD_EXCEPTION(1002, "密码不正确，小主请再检查下啦！"),
    FILE_TYPE_EXCEPTION(1003, "上传头像文件类型错误，小主请再试试哟！"),
    ADD_COMMENT_EXCEPTION(1004,"新增评论失败！"),
    SEARCH_EXCEPTION(1005,"solr搜索异常"),
    SOLR_INDEX_EXCEPTION(1006,"solr索引异常"),
    SOLR_DELETE_INDEX_EXCEPTION(1007,"删除solr索引失败");


    private int code;
    private String message;

    GlobalErrorEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

    