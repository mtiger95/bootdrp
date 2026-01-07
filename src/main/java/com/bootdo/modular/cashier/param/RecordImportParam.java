package com.bootdo.modular.cashier.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 导入
 *
 * @author L
 * @since 2023-03-27 14:53
 */
@Data
public class RecordImportParam {

    @NotBlank
    @Schema(description = "所属店铺")
    private String shopNo;

    @NotNull
    @Schema(description = "单据文件流")
    private MultipartFile file;
}
