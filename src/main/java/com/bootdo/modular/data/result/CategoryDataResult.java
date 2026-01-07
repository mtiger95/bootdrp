package com.bootdo.modular.data.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 缓存_类目数据
 *
 * @author L
 * @since 2024-01-26 15:48
 */
@Data
public class CategoryDataResult {

    @Schema(description = "类目ID")
    private String categoryId;
    @Schema(description = "父类目ID")
    private String parentId;
    @Schema(description = "父类目名称")
    private String name;
    @Schema(description = "类目分类")
    private String type;
    @Schema(description = "类目排序")
    private String orderNum;
    @Schema(description = "数据编号")
    private String dataId;
    @Schema(description = "数据名称")
    private String dataName;
    @Schema(description = "数据状态")
    private String status;
    @Schema(description = "店铺编号")
    private String shopNo;

}
