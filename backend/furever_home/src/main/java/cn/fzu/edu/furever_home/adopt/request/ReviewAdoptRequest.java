package cn.fzu.edu.furever_home.adopt.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(name = "ReviewAdoptRequest", description = "审核领养申请请求体")
public class ReviewAdoptRequest {
    @NotBlank
    @Pattern(regexp = "申请成功|申请失败")
    @Schema(description = "审核状态（申请成功/申请失败）")
    private String applicationStatus;
}
