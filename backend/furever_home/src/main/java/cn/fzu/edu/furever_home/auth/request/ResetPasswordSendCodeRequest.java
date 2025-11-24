package cn.fzu.edu.furever_home.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(name = "ResetPasswordSendCodeRequest", description = "发送重置密码验证码请求体")
public class ResetPasswordSendCodeRequest {
    @Schema(description = "邮箱")
    @NotBlank
    @Email
    private String email;
}