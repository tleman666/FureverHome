package cn.fzu.edu.furever_home.admin.service;

import cn.fzu.edu.furever_home.admin.dto.AdminAnimalDetailDTO;
import cn.fzu.edu.furever_home.admin.dto.AdminAnimalSummaryDTO;
import cn.fzu.edu.furever_home.common.enums.AdoptionStatus;
import cn.fzu.edu.furever_home.common.result.PageResult;

public interface AdminAnimalService {

    PageResult<AdminAnimalSummaryDTO> listPendingShortTerm(int page, int pageSize, String keyword);

    PageResult<AdminAnimalSummaryDTO> listApprovedByType(int page, int pageSize, String keyword, AdoptionStatus adoptionStatus);

    AdminAnimalDetailDTO getDetail(Integer animalId);

    void approve(Integer reviewerId, Integer animalId, String reason);

    void reject(Integer reviewerId, Integer animalId, String reason);

    void delete(Integer animalId);
}


