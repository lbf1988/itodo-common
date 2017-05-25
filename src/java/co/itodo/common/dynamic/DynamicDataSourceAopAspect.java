package co.itodo.common.dynamic;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @Desc TODO 动态数据源AOP注入方式
 *
 * <bean id="dynamicDataSourceAspect" class="cc.alino.core.aop.DynamicDataSourceAopAspect"></bean>
 *   <aop:aspect id="c" ref="dynamicDataSourceAspect">
 *       <aop:pointcut id="tx" expression="execution(* cc.alino.business.*.dao.*.*(..)) or execution(* cc.alino.notify.persistence.dao.*.*(..))"/>
 *       <aop:before pointcut-ref="tx" method="before"/>
 *       <aop:before pointcut-ref="tx" method="after"/>
 *   </aop:aspect>
 * @Author by Brant
 * @Date 2017/05/25
 */
public class DynamicDataSourceAopAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDataSourceAopAspect.class);

    private DataSource findDataSourceHandleAnnotation(JoinPoint joinPoint) throws ClassNotFoundException {
        Object target = joinPoint.getTarget();
        String methodName = joinPoint.getSignature().getName();
        Class<?>[] clazz = target.getClass().getInterfaces();
        Class<?>[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getMethod().getParameterTypes();
        DataSource dataSource = null;
        try {
            Method method = clazz[0].getMethod(methodName,parameterTypes);
            if(method!=null && method.isAnnotationPresent(DataSource.class)){
                dataSource = method.getAnnotation(DataSource.class);
            }
        } catch (Exception e) {
            LOGGER.debug("选择数据源错误：method:[%s],msg:[%s]",methodName,e.getMessage());
        }
        return dataSource;
    }
    /**
     * 在dao层方法之前获取datasource对象之前在切面中指定当前线程数据源路由的key
     */
    public void before(JoinPoint joinPoint) throws ClassNotFoundException {
        String methodName = joinPoint.getSignature().getName();
        DataSource dataSource = findDataSourceHandleAnnotation(joinPoint);
        if(dataSource!=null){
            LOGGER.debug("dao["+joinPoint.getSignature().toLongString()+"] method before:["+methodName+"] dataSource:["+dataSource.value().name()+"]");
            DynamicDataSourceHolder.putDataSource(dataSource.value());
        }else{
            LOGGER.debug("dao["+joinPoint.getSignature().toLongString()+"] method before:["+methodName+"]");
        }
    }

    public void after(JoinPoint point) {

    }
}
