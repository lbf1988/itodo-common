package co.itodo.common.filter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Desc TODO 防止常见XSS 过滤 SQL注入 && 拦截Response
 * @Author by Brant
 * @Date 2017/05/25
 */
public class XssFilterInterceptorResponse extends XssFilter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper((HttpServletRequest)servletRequest);
        CustomHttpServletResponseWrapper xssResponse = new CustomHttpServletResponseWrapper((HttpServletResponse) servletResponse);
        filterChain.doFilter(xssRequest,xssResponse);
        String responseContent = new String(xssResponse.getDataStream(), xssResponse.DEFAULT_CHARSET);
        servletResponse.getOutputStream().write(responseContent.getBytes());
    }
}
