package cn.fzu.edu.furever_home.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "ResetPasswordRequest", description = "重置密码请求体")
public class ResetPasswordRequest {
    @Schema(description = "邮箱")
    @NotBlank
    @Email
    private String email;

    @Schema(description = "验证码")
    @NotBlank
    @Pattern(regexp = "\\d{6}")
    private String code;

    @Schema(description = "新密码")
    @NotBlank
    @Size(min = 6, max = 128)
    private String newPassword;
}