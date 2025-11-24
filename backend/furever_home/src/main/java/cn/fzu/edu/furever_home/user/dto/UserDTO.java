package cn.fzu.edu.furever_home.user.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {
    private Integer userId;
    private String userName;
    private Integer userAge;
    private String email;
    private String avatarUrl;
    private String sex;
    private String location;
    private String proofText;
    private String proofPhoto;
    private Double creditScore;
    private Integer creditScoreCount;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLoginAt;
}