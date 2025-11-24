package cn.fzu.edu.furever_home.adopt.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdoptDTO {
    private Integer adoptId;
    private Integer animalId;
    private Integer userId;
    private String applicationStatus;
    private String livingEnvironment;
    private String houseType;
    private Boolean hasOtherPets;
    private Integer familyMemberCount;
    private Boolean hasChild;
    private String adoptReason;
    private Integer monthSalary;
    private LocalDateTime createTime;
    private LocalDateTime passTime;
}