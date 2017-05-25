package co.itodo.common.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * @Desc TODO
<bean class="org.alino.core.fastjson.MappingFastJsonHttpMessageConverter">
    <property name="supportedMediaTypes">
        <list>
            <value>text/plain;charset=UTF-8</value>
            <value>text/html;charset=UTF-8</value>
            <value>application/json</value>
        </list>
    </property>
    <property name="serializerFeature">
        <list>
            <!-- 输出key时是否使用双引号 -->
            <value>QuoteFieldNames</value>
            <!-- 是否输出值为null的字段 -->
            <!-- <value>WriteMapNullValue</value> -->
            <!-- 数值字段如果为null,输出为0,而非null -->
            <!--<value>WriteNullNumberAsZero</value>-->
            <!-- List字段如果为null,输出为[],而非null -->
            <value>WriteNullListAsEmpty</value>
            <!-- 字符类型字段如果为null,输出为"",而非null -->
            <value>WriteNullStringAsEmpty</value>
            <!-- Boolean字段如果为null,输出为false,而非null -->
            <value>WriteNullBooleanAsFalse</value>
            <!-- null String不输出  -->
            <value>WriteNullStringAsEmpty</value>
            <!-- null String也要输出  -->
            <!-- <value>WriteMapNullValue</value> -->
            <!-- Date的日期转换器 -->
            <value>WriteDateUseDateFormat</value>
        </list>
    </property>
</bean>
 * @Author by Brant
 * @Date 2017/05/25
 */
public class MappingFastJsonHttpMessageConverter extends AbstractHttpMessageConverter<Object> {
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    // fastjson特性参数
    private SerializerFeature[] serializerFeature;

    public SerializerFeature[] getSerializerFeature() {
        return serializerFeature;
    }

    public void setSerializerFeature(SerializerFeature[] serializerFeature) {
        this.serializerFeature = serializerFeature;
    }

    public MappingFastJsonHttpMessageConverter() {
        super(new MediaType("application", "json", DEFAULT_CHARSET));
    }

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        // JavaType javaType = getJavaType(clazz);
        // return this.objectMapper.canDeserialize(javaType) &&
        // canRead(mediaType);
        return true;
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        // return this.objectMapper.canSerialize(clazz) && canWrite(mediaType);
        return true;
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        // should not be called, since we override canRead/Write instead
        throw new UnsupportedOperationException();
    }

    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i;
        while ((i = inputMessage.getBody().read()) != -1) {
            baos.write(i);
        }
        return JSON.parseArray(baos.toString(), clazz);
    }

    @Override
    protected void writeInternal(Object o, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        String jsonString = JSONObject.toJSONString(o,serializerFeature);
        OutputStream out = outputMessage.getBody();
        out.write(jsonString.getBytes(DEFAULT_CHARSET));
        out.flush();
    }
}
