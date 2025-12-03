package cn.fzu.edu.furever_home.admin.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "领养申请审核请求")
public class AdoptReviewRequest {

    @Schema(description = "审核备注/原因", example = "综合情况良好，同意领养")
    private String reason;

    @Schema(description = "申请ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "申请ID不能为空")
    private Integer adoptId;
}


