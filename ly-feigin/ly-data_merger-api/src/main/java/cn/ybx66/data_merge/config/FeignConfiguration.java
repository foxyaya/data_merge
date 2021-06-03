package cn.ybx66.userapi.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/5/6 14:09
 * @description
 */
@Configuration
public class FeignConfiguration  {
    private Logger logger = LoggerFactory.getLogger(FeignConfiguration.class);

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                        .getRequestAttributes();
                HttpServletRequest request = attributes.getRequest();
                Enumeration<String> headerNames = request.getHeaderNames();
                if (headerNames != null) {
                    while (headerNames.hasMoreElements()) {
                        String name = headerNames.nextElement();
                        String values = request.getHeader(name);
                        template.header(name, values);
                    }
                }
                Enumeration<String> bodyNames = request.getParameterNames();
                StringBuffer body =new StringBuffer();
                if (bodyNames != null) {
                    while (bodyNames.hasMoreElements()) {
                        String name = bodyNames.nextElement();
                        String values = request.getParameter(name);
                        body.append(name).append("=").append(values).append("&");
                    }
                }
                if(body.length()!=0) {
                    body.deleteCharAt(body.length()-1);
                    template.body(body.toString());
                    logger.info("feign interceptor body:{}",body.toString());
                }
            }
        };
    }
}
