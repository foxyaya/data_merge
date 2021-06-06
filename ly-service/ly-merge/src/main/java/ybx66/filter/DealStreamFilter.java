package ybx66.filter;

import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import ybx66.configure.MyRequestWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/4/30 22:07
 * @description
 */
@Component
class DealStreamFilter implements Filter  {

    public void init(FilterConfig filterConfig) throws ServletException {

    }
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        //获取请求参数,并转换成字符串存储在ThreadLocal
        if(request instanceof  HttpServletRequest  ){//&& response instanceof HttpServletResponse
            request = new MyRequestWrapper((HttpServletRequest) request);
//            response = new MyRequestWrapper((HttpServletResponse)response);
        }
        chain.doFilter(request,response);
    }

    public void destroy() {

    }
}