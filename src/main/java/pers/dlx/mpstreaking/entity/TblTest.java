package pers.dlx.mpstreaking.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author dinglingxiang  2020/3/20 12:04
 */
@ApiModel("宽表数据")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TblTest implements Serializable {

    private String id;

    private String remark;

}
