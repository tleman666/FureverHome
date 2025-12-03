package cn.fzu.edu.furever_home.admin.service;

import cn.fzu.edu.furever_home.admin.dto.AdminAdoptDetailDTO;
import cn.fzu.edu.furever_home.admin.dto.AdminAdoptSummaryDTO;
import cn.fzu.edu.furever_home.common.result.PageResult;

public interface AdminAdoptService {

    PageResult<AdminAdoptSummaryDTO> listPending(int page, int pageSize, String keyword);

    PageResult<AdminAdoptSummaryDTO> listProcessed(int page, int pageSize, String keyword);

    AdminAdoptDetailDTO getDetail(Integer adoptId);

    void approve(Integer reviewerId, Integer adoptId, String reason);

    void reject(Integer reviewerId, Integer adoptId, String reason);
}


