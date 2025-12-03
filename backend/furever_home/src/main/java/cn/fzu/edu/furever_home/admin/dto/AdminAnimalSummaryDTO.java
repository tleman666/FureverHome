package cn.fzu.edu.furever_home.admin.dto;

import cn.fzu.edu.furever_home.common.enums.AdoptionStatus;
import cn.fzu.edu.furever_home.common.enums.Gender;
import cn.fzu.edu.furever_home.common.enums.ReviewStatus;
import cn.fzu.edu.furever_home.common.enums.Species;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "管理端宠物列表项")
public class AdminAnimalSummaryDTO {

    @Schema(description = "动物ID")
    private Integer animalId;

    @Schema(description = "宠物名称")
    private String animalName;

    @Schema(description = "物种大类")
    private Species species;

    @Schema(description = "品种")
    private String breed;

    @Schema(description = "性别")
    private Gender gender;

    @Schema(description = "年龄（月龄）")
    private Integer animalAge;

    @Schema(description = "领养类型（短期/长期）")
    private AdoptionStatus adoptionStatus;

    @Schema(description = "审核状态")
    private ReviewStatus reviewStatus;

    @Schema(description = "发布者ID")
    private Integer ownerId;

    @Schema(description = "发布者昵称")
    private String ownerName;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
}


