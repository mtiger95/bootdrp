package com.bootdo.modular.system.param;

import com.bootdo.core.pojo.base.param.BaseParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 字典管理
 *
 * @author L
 * @since 2024-01-26 15:48
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SysDictParam extends BaseParam {

    @Schema(description = "类别")
    private String type;

    @Schema(description = "字典值")
    private String value;

}
