package com.know.wenda.filter;

import com.know.wenda.config.RequestManager;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * CommonFilter
 *
 * @author hlb
 */
public class CommonFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            RequestManager.setHttpServletRequest(request);
            filterChain.doFilter(request, response);
        } finally {
            //防止出现内存泄露
            RequestManager.removeHttpServletRequest();
        }
    }
}