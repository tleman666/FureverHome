package cn.fzu.edu.furever_home.admin.dto;

import cn.fzu.edu.furever_home.common.enums.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "管理端领养申请详情")
public class AdminAdoptDetailDTO {

    @Schema(description = "领养申请ID")
    private Integer adoptId;

    @Schema(description = "宠物ID")
    private Integer animalId;

    @Schema(description = "宠物名称")
    private String animalName;

    @Schema(description = "宠物物种")
    private Species animalSpecies;

    @Schema(description = "宠物性别")
    private Gender animalGender;

    @Schema(description = "申请人ID")
    private Integer userId;

    @Schema(description = "申请人名称")
    private String userName;

    @Schema(description = "申请人居住地址（来自用户所在地或详细地址）")
    private String address;

    @Schema(description = "申请状态")
    private ApplicationStatus applicationStatus;

    @Schema(description = "审核状态")
    private ReviewStatus reviewStatus;

    @Schema(description = "居住环境")
    private LivingEnvironment livingEnvironment;

    @Schema(description = "房屋类型")
    private HouseType houseType;

    @Schema(description = "是否有其他宠物")
    private Boolean hasOtherPets;

    @Schema(description = "家庭成员数量")
    private Integer familyMemberCount;

    @Schema(description = "是否有小孩")
    private Boolean hasChild;

    @Schema(description = "领养原因")
    private String adoptReason;

    @Schema(description = "月收入")
    private Integer monthSalary;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "审核完成时间")
    private LocalDateTime passTime;
}


