package cn.fzu.edu.furever_home.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(name = "SendCodeRequest", description = "发送注册验证码请求体")
public class SendCodeRequest {
    @Schema(description = "邮箱", example = "user@example.com")
    @NotBlank
    @Email
    private String email;
}