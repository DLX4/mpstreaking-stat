package pers.dlx.mpstreaking.streaking;

/**
 * @author dinglingxiang  2020/3/16 16:26
 */
public class StatementUuid {

    private static final ThreadLocal<String> STATEMENT_UUID = new ThreadLocal<>();

    /**
     * 取得当前数据源
     *
     * @return
     */
    public static String get() {
        return (String) STATEMENT_UUID.get();
    }

    /**
     * 设置数据源
     *
     * @param uuid
     */
    public static void set(String uuid) {
        STATEMENT_UUID.set(uuid);
    }

    /**
     * 清除上下文数据
     */
    public static void clearDbType() {
        STATEMENT_UUID.remove();
    }
}
