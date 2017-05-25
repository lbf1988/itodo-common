package co.itodo.common.dynamic;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Desc TODO 动态数据源实现读写分离  支持多读库
 * <bean id="dataSource" class="org.alino.core.dynamic.DynamicDataSource">
 *     <property name="masterDataSource" ref="masterDataSource" />
 *     <property name="slaveDataSources">
 *     <list>
 *         <ref bean="slaveDataSource1" />
 *     </list>
 *     </property>
 *     <property name="slaveDataSourcePollPattern" value="1" />
 *     <property name="defaultTargetDataSource" ref="masterDataSource"/>
 * </bean>
 * @Author by Brant
 * @Date 2017/05/25
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    /**
     * 写数据源
     */
    private Object masterDataSource;
    /**
     * 多个读数据源
     */
    private List<Object> slaveDataSources;
    /**
     * 读数据源数量
     */
    private int slaveDataSourceSize;
    /**
     * 获取读数据源方式，0：随机，1：轮询
     */
    private int slaveDataSourcePollPattern = 0;

    private AtomicLong counter = new AtomicLong(0);

    private static final Long MAX_POOL = Long.MAX_VALUE;

    private final Lock lock = new ReentrantLock();

    @Override
    public void afterPropertiesSet() {
        if(null == this.masterDataSource){
            throw new IllegalArgumentException("写数据源：masterDataSource必须初始化！！！");
        }
        setDefaultTargetDataSource(this.masterDataSource);
        setTargetDataSources(initTargetDataSources());
        super.afterPropertiesSet();
    }

    private Map<Object,Object> initTargetDataSources(){
        Map<Object,Object> targetDataSources = new HashMap<>();
        //TODO put写数据源
        targetDataSources.put(DataSourceEnum.master.name(),this.masterDataSource);
        //TODO put读数据源
        if(null == this.slaveDataSources){
            this.slaveDataSourceSize = 0;
        }else{
            for(int i=0;i<this.slaveDataSources.size();i++){
                targetDataSources.put(DataSourceEnum.slave.name()+i,this.slaveDataSources.get(i));
            }
            this.slaveDataSourceSize = this.slaveDataSources.size();
        }
        return targetDataSources;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        DataSourceEnum dataSourceGlobal = DynamicDataSourceHolder.getDataSouce();
        if(null == dataSourceGlobal || (dataSourceGlobal == DataSourceEnum.master) || (this.slaveDataSourceSize <=0)){
            return DataSourceEnum.master.name();
        }
        return dataSourceGlobal.name() + getSlaveDataSourceIndex();
    }

    private int getSlaveDataSourceIndex(){
        int index = 1;
        if(this.slaveDataSourcePollPattern == 1){
            long currentValue = counter.incrementAndGet();
            if((currentValue + 1) >= this.MAX_POOL){
                try {
                    lock.lock();
                    if ((currentValue + 1) >= this.MAX_POOL) {
                        counter.set(0);
                    }
                }finally {
                    lock.unlock();
                }
            }
            index = (int) (currentValue % this.slaveDataSourceSize);
        }else{
            index = ThreadLocalRandom.current().nextInt(0,this.slaveDataSourceSize);
        }
        return index;
    }

    public Object getMasterDataSource() {
        return masterDataSource;
    }

    public void setMasterDataSource(Object masterDataSource) {
        this.masterDataSource = masterDataSource;
    }

    public List<Object> getSlaveDataSources() {
        return slaveDataSources;
    }

    public void setSlaveDataSources(List<Object> slaveDataSources) {
        this.slaveDataSources = slaveDataSources;
    }

    public int getSlaveDataSourcePollPattern() {
        return slaveDataSourcePollPattern;
    }

    public void setSlaveDataSourcePollPattern(int slaveDataSourcePollPattern) {
        this.slaveDataSourcePollPattern = slaveDataSourcePollPattern;
    }
}
