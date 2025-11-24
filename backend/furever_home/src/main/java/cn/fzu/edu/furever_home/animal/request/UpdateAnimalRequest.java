package cn.fzu.edu.furever_home.animal.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "UpdateAnimalRequest", description = "更新动物信息请求体")
public class UpdateAnimalRequest {
    @Size(max = 50)
    private String animalName;
    private String photoUrls;
    private String species;
    @Size(max = 50)
    private String breed;
    private String gender;
    @Min(0)
    private Integer animalAge;
    @Size(max = 200)
    private String healthStatus;
    private String isSterilized;
    private String adoptionStatus;
    @Size(max = 200)
    private String shortDescription;
}