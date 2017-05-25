package co.itodo.common.freemarker.base;

import freemarker.ext.beans.BeanModel;
import freemarker.ext.beans.StringModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

import java.math.BigInteger;
import java.util.List;

/**
 * @Desc TODO
 * @Author by Brant
 * @Date 2017/05/25
 */
public abstract class BaseFunction implements TemplateMethodModelEx {
    private List<?> argList;

    @SuppressWarnings("rawtypes")
    @Override
    public Object exec(List args) throws TemplateModelException {
        argList = args;
        return onExec();
    }

    public abstract Object onExec();

    /**
     * 根据位置获取对象值
     * @param index
     * @return
     */
    public Object get(int index) {
        if (null == argList || argList.size() == 0)
            return null;
        if (index > argList.size() - 1)
            return null;
        Object obj = argList.get(index);
        if (obj instanceof BeanModel) {
            return ((BeanModel) obj).getWrappedObject();
        }
        return null;
    }

    /**
     * 根据位置获取对象模型
     * @param index
     * @return
     */
    public StringModel getToStringModel(int index) {
        if (null == argList || argList.size() == 0)
            return null;
        if (index > argList.size() - 1)
            return null;
        return (StringModel) argList.get(index);
    }

    /**
     * 根据位置获取String值
     * @param index
     * @return
     */
    public String getToString(int index) {
        if (null == argList || argList.size() == 0)
            return null;
        if (index > argList.size() - 1)
            return null;
        if (argList.get(index) == null)
            return null;
        return argList.get(index).toString();
    }

    /**
     * 根据位置获取String值
     * @param index
     * @param defaultValue
     * @return
     */
    public String getToString(int index, String defaultValue) {
        if (null == argList || argList.size() == 0)
            return defaultValue;
        if (index > argList.size() - 1)
            return defaultValue;
        return argList.get(index).toString();
    }

    /**
     * 根据位置获取Long值
     * @param index
     * @return
     */
    public Long getToLong(int index) {
        String stringValue = getToString(index);
        if (null == stringValue || "".equals(stringValue.trim())) {
            return null;
        }
        return Long.parseLong(stringValue);
    }

    /**
     * 根据位置获取Long值
     * @param index
     * @param defaultValue
     * @return
     */
    public Long getToLong(int index, long defaultValue) {
        String stringValue = getToString(index);
        if (null == stringValue) {
            return defaultValue;
        }
        return Long.parseLong(stringValue);
    }

    /**
     * 根据位置获取BigInteger值
     * @param index
     * @return
     */
    public BigInteger getToBigInteger(int index) {
        String stringValue = getToString(index);
        if (null == stringValue || "".equals(stringValue.trim())) {
            return null;
        }
        return new BigInteger(stringValue);
    }

    /**
     * 根据位置获取BigInteger值
     * @param index
     * @param defaultValue
     * @return
     */
    public BigInteger getToBigInteger(int index, BigInteger defaultValue) {
        String stringValue = getToString(index);
        if (null == stringValue) {
            return defaultValue;
        }
        return new BigInteger(stringValue);
    }
}
