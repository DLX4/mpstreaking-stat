package pers.dlx.mpstreaking.ds;

/**
 * @author dinglingxiang
 */
public class DbContextHolder {
    private static final ThreadLocal contextHolder = new ThreadLocal<>();

    /**
     * 取得当前数据源
     *
     * @return
     */
    public static String getDbType() {
        return (String) contextHolder.get();
    }

    /**
     * 设置数据源
     *
     * @param dbTypeEnumValue
     */
    public static void setDbType(String dbTypeEnumValue) {
        contextHolder.set(dbTypeEnumValue);
    }

    /**
     * 设置数据源
     *
     * @param dbTypeEnum
     */
    public static void setDbType(DBTypeEnum dbTypeEnum) {
        contextHolder.set(dbTypeEnum.getValue());
    }

    /**
     * 清除上下文数据
     */
    public static void clearDbType() {
        contextHolder.remove();
    }
}
