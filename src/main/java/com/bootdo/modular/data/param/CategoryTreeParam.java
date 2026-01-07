package com.bootdo.modular.data.param;

import com.bootdo.core.enums.CategoryType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 类目树
 *
 * @author L
 * @since 2024-01-26 15:48
 */
@Data
public class CategoryTreeParam {

    @Schema(description = "类目ID")
    private String categoryId;

    @Schema(description = "类目分类")
    private CategoryType type;

}
