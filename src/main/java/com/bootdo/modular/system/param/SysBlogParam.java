package com.bootdo.modular.system.param;

import com.bootdo.core.pojo.base.param.BaseParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 博客管理
 *
 * @author L
 * @since 2024-01-26 15:48
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SysBlogParam extends BaseParam {

    @Schema(description = "类别")
    private String categories;

}
