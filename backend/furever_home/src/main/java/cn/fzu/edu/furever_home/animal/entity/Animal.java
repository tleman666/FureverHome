package cn.fzu.edu.furever_home.animal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("animal")
public class Animal {
    @TableId(value = "animal_id", type = IdType.AUTO)
    private Integer animalId;
    private Integer userId;
    private String animalName;
    private String photoUrls;
    private String species;
    private String breed;
    private String gender;
    private Integer animalAge;
    private String healthStatus;
    private String isSterilized;
    private String adoptionStatus;
    private String shortDescription;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}