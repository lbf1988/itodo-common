package co.itodo.common.dynamic;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Desc TODO 编译器将把注释记录在类文件中，在运行时 VM 将保留注释，因此可以反射性地读取
 * @Author by Brant
 * @Date 2017/05/25
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DataSource {
    /**
     * 默认是写入数据的数据源
     * @return
     */
    DataSourceEnum value() default DataSourceEnum.master;
}
