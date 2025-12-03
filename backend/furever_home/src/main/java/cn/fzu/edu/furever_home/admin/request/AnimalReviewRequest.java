package cn.fzu.edu.furever_home.admin.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "宠物审核请求")
public class AnimalReviewRequest {

    @Schema(description = "审核备注/原因", example = "资料完整，允许展示")
    private String reason;

    @Schema(description = "宠物ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "宠物ID不能为空")
    private Integer animalId;
}


