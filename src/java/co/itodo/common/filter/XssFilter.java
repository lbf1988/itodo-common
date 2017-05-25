package co.itodo.common.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Desc TODO 防止常见XSS 过滤 SQL注入
 * @Author by Brant
 * @Date 2017/05/25
 */
public class XssFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper((HttpServletRequest)servletRequest);
        filterChain.doFilter(xssRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
