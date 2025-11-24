package cn.fzu.edu.furever_home.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "UpdateUserRequest", description = "更新用户信息请求体")
public class UpdateUserRequest {
    @Schema(description = "年龄")
    @Min(0)
    @Max(150)
    private Integer userAge;

    @Schema(description = "头像URL")
    @Size(max = 255)
    private String avatarUrl;

    @Schema(description = "性别", example = "男/女")
    @Pattern(regexp = "男|女")
    private String sex;

    @Schema(description = "所在地")
    @Size(max = 50)
    private String location;

    @Schema(description = "爱宠证明简介")
    @Size(max = 500)
    private String proofText;

    @Schema(description = "爱宠证明（JSON）")
    private String proofPhoto;
}