package co.itodo.common.dynamic;

/**
 * @Desc TODO
 * @Author by Brant
 * @Date 2017/05/25
 */
public class DynamicDataSourceHolder {
    private static final ThreadLocal<DataSourceEnum> holder = new ThreadLocal<DataSourceEnum>();

    private DynamicDataSourceHolder(){}

    public static void putDataSource(DataSourceEnum name) {
        holder.set(name);
    }

    public static DataSourceEnum getDataSouce() {
        return holder.get();
    }

    public static void clearDataSource() {
        holder.remove();
    }
}
