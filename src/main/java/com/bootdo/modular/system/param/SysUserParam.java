package com.bootdo.modular.system.param;

import com.bootdo.core.pojo.base.param.BaseParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户管理
 *
 * @author L
 * @since 2024-01-26 15:48
 */
@Data
public class SysUserParam extends BaseParam {

    @Schema(description = "用户名（多个逗号分隔）")
    private String name;

    @Schema(description = "账户名（多个逗号分隔）")
    private String userName;

    @Schema(description = "部门ID（多个逗号分隔）")
    private String deptId;

}
