package cn.fzu.edu.furever_home.admin.service;

import cn.fzu.edu.furever_home.admin.dto.AdminPostDetailDTO;
import cn.fzu.edu.furever_home.admin.dto.AdminPostSummaryDTO;
import cn.fzu.edu.furever_home.admin.dto.AdminCommentDTO;
import cn.fzu.edu.furever_home.common.result.PageResult;

public interface AdminPostService {

    PageResult<AdminPostSummaryDTO> listPending(int page, int pageSize, String keyword);

    PageResult<AdminPostSummaryDTO> listPublished(int page, int pageSize, String keyword);

    AdminPostDetailDTO getDetail(Integer postId);

    /**
     * 分页查询指定帖子的评论列表
     */
    PageResult<AdminCommentDTO> listComments(Integer postId, int page, int pageSize);

    void approve(Integer reviewerId, Integer postId, String reason);

    void reject(Integer reviewerId, Integer postId, String reason);

    void delete(Integer postId);
}

