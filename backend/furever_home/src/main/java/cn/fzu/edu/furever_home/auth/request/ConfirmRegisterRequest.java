package cn.fzu.edu.furever_home.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "ConfirmRegisterRequest", description = "确认注册请求体")
public class ConfirmRegisterRequest {
    @Schema(description = "邮箱", example = "user@example.com")
    @NotBlank
    @Email
    private String email;

    @Schema(description = "验证码", example = "123456")
    @NotBlank
    @Pattern(regexp = "\\d{6}")
    private String code;

    @Schema(description = "昵称", example = "alice")
    @NotBlank
    @Size(min = 3, max = 50)
    private String userName;

    @Schema(description = "密码", example = "123456")
    @NotBlank
    @Size(min = 6, max = 128)
    private String password;
}