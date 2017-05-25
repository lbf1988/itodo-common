package co.itodo.common.dynamic;

/**
 * @Desc TODO 数据源主[写]从[读]标识
 * @Author by Brant
 * @Date 2017/05/25
 */
public enum DataSourceEnum {
    /**
     * 主表、主要用于写入数据
     */
    master,
    /**
     * 从表、主要用于读取数据
     */
    slave;
}
