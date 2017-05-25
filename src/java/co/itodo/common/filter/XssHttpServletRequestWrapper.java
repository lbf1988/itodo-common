package co.itodo.common.filter;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @Desc TODO
 * @Author by Brant
 * @Date 2017/05/25
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private HttpServletRequest orgRequest = null;

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        orgRequest = request;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return  super.getParameterNames();
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(xssEncode(name));
        if(values!=null){
            for (int i=0; i<values.length; i++) {
                values[i] = xssEncode(values[i]);
            }
        }
        return values;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> parameterMap = super.getParameterMap();
        Map<String, String[]> map = null;
        if(parameterMap!=null){
            map = new HashMap<>();
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                map.put(entry.getKey(), getParameterValues(entry.getKey()));
            }
        }
        return map;
    }

    /**
     * 覆盖getParameter方法，将参数名和参数值都做xss过滤。<br/>
     * 如果需要获得原始的值，则通过super.getParameterValues(name)来获取<br/>
     * getParameterNames,getParameterValues和getParameterMap也可能需要覆盖
     */
    @Override
    public String getParameter(String name) {
        String value = super.getParameter(xssEncode(name));
        if (value != null) {
            value = xssEncode(value);
        }
        return value;
    }

    /**
     * 覆盖getHeader方法，将参数名和参数值都做xss过滤。<br/>
     * 如果需要获得原始的值，则通过super.getHeaders(name)来获取<br/>
     * getHeaderNames 也可能需要覆盖
     */
    @Override
    public String getHeader(String name) {
        String value = super.getHeader(xssEncode(name));
        if (value != null) {
            value = xssEncode(value);
        }
        return value;
    }

    /**
     * 将容易引起xss漏洞的半角字符直接替换成全角字符
     *
     * @param s
     * @return
     */
    private static String xssEncode(String s) {
        if (s == null || "".equals(s)) {
            return s;
        }
        String result = escapeString(s);
        return result;
    }

    private static String escapeString(String str) {
        //转义 <, >, &
        str = HtmlUtils.htmlEscape(str);
        //sql 注入转义
        str = StringEscapeUtils.escapeSql(str);
        return str;
    }

    /**
     * 获取最原始的request
     *
     * @return
     */
    public HttpServletRequest getOrgRequest() {
        return orgRequest;
    }

    /**
     * 获取最原始的request的静态方法
     *
     * @return
     */
    public static HttpServletRequest getOrgRequest(HttpServletRequest req) {
        if (req instanceof XssHttpServletRequestWrapper) {
            return ((XssHttpServletRequestWrapper) req).getOrgRequest();
        }
        return req;
    }
}
