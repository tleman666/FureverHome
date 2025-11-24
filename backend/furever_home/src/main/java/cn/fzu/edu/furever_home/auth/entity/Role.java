package cn.fzu.edu.furever_home.auth.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("roles")
public class Role {
    @TableId(value = "role_id", type = IdType.AUTO)
    private Integer roleId;
    private String roleCode;
    private String roleName;
    private String description;
    private LocalDateTime createdAt;
}