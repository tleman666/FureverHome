package cn.fzu.edu.furever_home.post.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "UpdatePostRequest", description = "更新帖子请求体")
public class UpdatePostRequest {
    @Size(max = 100)
    private String title;
    private String content;
    private String mediaUrls;
}
