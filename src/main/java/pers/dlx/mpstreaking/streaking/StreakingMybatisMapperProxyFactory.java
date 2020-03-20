package pers.dlx.mpstreaking.streaking;

import lombok.Getter;
import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.Proxy;

/**
 * @author dinglingxiang  2020/3/16 15:55
 */
public class StreakingMybatisMapperProxyFactory<T> {

    @Getter
    private final Class<T> mapperInterface;

    public StreakingMybatisMapperProxyFactory(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }


    @SuppressWarnings("unchecked")
    protected T newInstance(StreakingMybatisMapperProxy<T> mapperProxy) {
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]{mapperInterface}, mapperProxy);
    }

    public T newInstance(SqlSession sqlSession) {
        final StreakingMybatisMapperProxy<T> mapperProxy = new StreakingMybatisMapperProxy<>(sqlSession, mapperInterface);
        return newInstance(mapperProxy);
    }
}
