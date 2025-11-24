package cn.fzu.edu.furever_home.post.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostDTO {
    private Integer postId;
    private Integer userId;
    private String title;
    private String content;
    private String mediaUrls;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private LocalDateTime createTime;
}