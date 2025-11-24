package cn.fzu.edu.furever_home.animal.dto;

import lombok.Data;

@Data
public class AnimalDTO {
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
}