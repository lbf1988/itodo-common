package co.itodo.common.freemarker.base;

import freemarker.core.Environment;
import freemarker.template.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Writer;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Desc TODO
 * @Author by Brant
 * @Date 2017/05/25
 */
public abstract class BaseTag extends WrappingTemplateModel implements TemplateDirectiveModel {
    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseTag.class);
    private Environment mEnv;
    private Map<?, ?> mParams;

    private TemplateModel[] mTemplateModels;
    private TemplateDirectiveBody mBody;

    @Override
    public void execute(Environment env, Map params, TemplateModel[] templateModels, TemplateDirectiveBody body) throws TemplateException, IOException {
        this.mEnv = env;
        this.mParams = params;
        this.mTemplateModels = templateModels;
        this.mBody = body;
        onRender();
    }

    abstract void onRender();

    /**
     * 渲染属性
     * @param key
     * @param value
     */
    protected void setVariable(String key, Object value) {
        try {
            mEnv.setVariable(key, super.getDefaultObjectWrapper().wrap(value));
        } catch (TemplateModelException e) {
            LOGGER.error("setVariable(String key,Object value) is error!", e);
        }
    }

    /**
     * 渲染文本
     * @param text
     */
    protected void renderText(String text) {
        try {
            mEnv.getOut().write(text == null ? "null" : text);
        } catch (IOException e) {
            LOGGER.error("BaseTag renderText error", e);
        }
    }

    /**
     * 渲染
     */
    protected void renderBody() {
        try {
            mBody.render(mEnv.getOut());
        } catch (TemplateException e) {
            LOGGER.error("BaseTag renderBody is error!", e);
        } catch (IOException e) {
            LOGGER.error("BaseTag renderBody is error!", e);
        }
    }

    /**
     * 渲染
     * @param writer
     */
    protected void renderBody(Writer writer) {
        try {
            mBody.render(writer);
        } catch (TemplateException e) {
            LOGGER.error("BaseTag renderBody(Writer writer) is error!", e);
        } catch (IOException e) {
            LOGGER.error("BaseTag renderBody(Writer writer) is error!", e);
        }
    }

    /**
     * 获取参数...................................................
     */
    public String getParam(String key) {
        TemplateModel model = (TemplateModel) mParams.get(key);
        if (model == null) {
            return null;
        }
        try {
            if (model instanceof TemplateScalarModel) {
                return ((TemplateScalarModel) model).getAsString();
            }
            if ((model instanceof TemplateNumberModel)) {
                return ((TemplateNumberModel) model).getAsNumber().toString();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public String getParam(String key, String defaultValue) {
        String value = getParam(key);
        if (value != null)
            return value;
        return defaultValue;
    }

    public Long getParamToLong(String key, long defaultValue) {
        Long value = getParamToLong(key);
        if (value != null)
            return value;
        return defaultValue;
    }

    public Byte getParamToByte(String key, byte defaultValue) {
        String value = getParam(key);
        if (value != null)
            return Byte.parseByte(value);
        return defaultValue;
    }

    public Long getParamToLong(String key) {
        TemplateModel model = (TemplateModel) mParams.get(key);
        if (model == null) {
            return null;
        }
        try {
            if (model instanceof TemplateNumberModel) {
                return ((TemplateNumberModel) model).getAsNumber().longValue();
            }
            if (model instanceof TemplateScalarModel) {
                String string = ((TemplateScalarModel) model).getAsString();
                if (null == string || "".equals(string.trim())) {
                    return null;
                }
                return Long.parseLong(string);
            }
        } catch (Exception e) {
            throw new RuntimeException("must number!", e);
        }
        return null;
    }

    public BigInteger getParamToBigInteger(String key, BigInteger defaultValue) {
        BigInteger value = getParamToBigInteger(key);
        if (value != null)
            return value;
        return defaultValue;
    }

    public BigInteger getParamToBigInteger(String key) {
        TemplateModel model = (TemplateModel) mParams.get(key);
        if (model == null) {
            return null;
        }
        try {
            if (model instanceof TemplateNumberModel) {
                long number = ((TemplateNumberModel) model).getAsNumber().longValue();
                return BigInteger.valueOf(number);
            }
            if (model instanceof TemplateScalarModel) {
                String string = ((TemplateScalarModel) model).getAsString();
                if (null == string || "".equals(string.trim())) {
                    return null;
                }
                return new BigInteger(string);
            }
        } catch (Exception e) {
            throw new RuntimeException("must number!", e);
        }
        return null;
    }

    public Integer getParamToInt(String key, Integer defaultValue) {
        Integer value = getParamToInt(key);
        if (null != value)
            return value;
        return defaultValue;
    }

    public Integer getParamToInt(String key) {
        TemplateModel model = (TemplateModel) mParams.get(key);
        if (model == null) {
            return null;
        }
        try {
            if (model instanceof TemplateNumberModel) {
                return ((TemplateNumberModel) model).getAsNumber().intValue();
            }
            if (model instanceof TemplateScalarModel) {
                String string = ((TemplateScalarModel) model).getAsString();
                if (null == string || "".equals(string.trim())) {
                    return null;
                }
                return Integer.parseInt(string);
            }
        } catch (Exception e) {
            throw new RuntimeException("must number!", e);
        }
        return null;
    }

    public Integer[] getParamToIntArray(String key) {
        String string = getParam(key);
        if (null == string || "".equals(string.trim())) {
            return null;
        }

        if (!string.contains(",")) {
            return new Integer[] { Integer.valueOf(string.trim()) };
        }

        String[] array = string.split(",");
        Integer[] ids = new Integer[array.length];
        int i = 0;
        try {
            for (String str : array) {
                ids[i++] = Integer.valueOf(str.trim());
            }
            return ids;
        } catch (NumberFormatException e) {
            throw e;
        }
    }

    public Long[] getParamToLongArray(String key) {
        String string = getParam(key);
        if (null == string || "".equals(string.trim())) {
            return null;
        }

        if (!string.contains(",")) {
            return new Long[] { Long.valueOf(string.trim()) };
        }

        String[] array = string.split(",");
        Long[] ids = new Long[array.length];
        int i = 0;
        try {
            for (String str : array) {
                ids[i++] = Long.valueOf(str.trim());
            }
            return ids;
        } catch (NumberFormatException e) {
            throw e;
        }
    }

    public BigInteger[] getParamToBigIntegerArray(String key) {
        String string = getParam(key);
        if (null == string || "".equals(string.trim())) {
            return null;
        }
        if (!string.contains(",")) {
            return new BigInteger[] { new BigInteger(string.trim()) };
        }
        String[] array = string.split(",");
        BigInteger[] ids = new BigInteger[array.length];
        int i = 0;
        try {
            for (String str : array) {
                ids[i++] = new BigInteger(str.trim());
            }
            return ids;
        } catch (NumberFormatException e) {
            throw e;
        }
    }

    public String[] getParamToStringArray(String key) {
        String string = getParam(key);
        if (null == string || "".equals(string.trim())) {
            return null;
        }
        if (!string.contains(",")) {
            return new String[] { string };
        }
        return string.split(",");
    }

    public ArrayList<String> getParamToStringArrayList(String key){
        String[] strings = getParamToStringArray(key);
        if(strings==null){
            return new ArrayList<>();
        }
        List<String> strArrays = Arrays.asList(strings);
        return new ArrayList<>(strArrays);
    }

    public Boolean getParamToBool(String key, Boolean defaultValue) {
        Boolean value = getParamToBool(key);
        if (value != null)
            return value;
        return defaultValue;
    }

    public Boolean getParamToBool(String key) {
        TemplateModel model = (TemplateModel) mParams.get(key);
        if (model == null) {
            return null;
        }
        try {
            if (model instanceof TemplateBooleanModel) {
                return ((TemplateBooleanModel) model).getAsBoolean();
            }
            if (model instanceof TemplateNumberModel) {
                return !(((TemplateNumberModel) model).getAsNumber().intValue() == 0);
            }
            if (model instanceof TemplateScalarModel) {
                String string = ((TemplateScalarModel) model).getAsString();
                if (null != string && !"".equals(string.trim())) {
                    return !(string.equals("0") || string.equalsIgnoreCase("false"));
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("param must is \"0\",\"1\"  or \"true\",\"false\"", e);
        }
        return null;
    }

    public TemplateModel[] getmTemplateModels(){
        return this.mTemplateModels;
    }

    public TemplateDirectiveBody getBody(){
        return this.mBody;
    }

    public Writer getWriter(){
        return mEnv.getOut();
    }
}
