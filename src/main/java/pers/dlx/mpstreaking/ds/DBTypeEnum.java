package pers.dlx.mpstreaking.ds;

/**
 * @author dinglingxiang
 */
public enum DBTypeEnum {

    stat1("stat1"),

    stat2("stat2");


    private String value;

    DBTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
