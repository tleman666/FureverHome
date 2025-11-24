package cn.fzu.edu.furever_home.auth.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("permissions")
public class Permission {
    @TableId(value = "permission_id", type = IdType.AUTO)
    private Integer permissionId;
    private String permCode;
    private String permName;
    private String description;
    private LocalDateTime createdAt;
}