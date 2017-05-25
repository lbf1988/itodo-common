package co.itodo.common.filter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * @Desc TODO
 * @Author by Brant
 * @Date 2017/05/25
 */
public class CustomHttpServletResponseWrapper extends HttpServletResponseWrapper {
    Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    ByteArrayOutputStream output;
    CustomServletOutputStream filterOutput;
    XssPrintWriter printWriter;

    public CustomHttpServletResponseWrapper(HttpServletResponse response) {
        super(response);
        output = new ByteArrayOutputStream();
        printWriter = new XssPrintWriter(output);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (filterOutput == null) {
            filterOutput = new CustomServletOutputStream(output);
        }
        return filterOutput;
    }

    public String getContent() {
        try {
            return printWriter.getByteArrayOutputStream().toString(DEFAULT_CHARSET.name());
        } catch (UnsupportedEncodingException e) {
            return "UnsupportedEncoding";
        }
    }

    //覆盖getWriter()方法，使用我们自己定义的Writer
    public PrintWriter getWriter() throws IOException {
        return printWriter;
    }

    public byte[] getDataStream() {
        return output.toByteArray();
    }

    private static class XssPrintWriter extends PrintWriter {
        //此即为存放response输入流的对象
        ByteArrayOutputStream myOutput;

        public XssPrintWriter(ByteArrayOutputStream output) {
            super(output);
            myOutput = output;
        }

        public ByteArrayOutputStream getByteArrayOutputStream() {
            return myOutput;
        }
    }
}
