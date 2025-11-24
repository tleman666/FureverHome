package cn.fzu.edu.furever_home.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "LoginRequest", description = "登录请求体")
public class LoginRequest {
    @Schema(description = "账号（昵称或邮箱）", example = "alice")
    @NotBlank
    private String account;

    @Schema(description = "密码", example = "123456")
    @NotBlank
    @Size(min = 6, max = 128)
    private String password;
}