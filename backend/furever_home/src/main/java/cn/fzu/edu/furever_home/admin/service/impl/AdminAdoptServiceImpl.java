package cn.fzu.edu.furever_home.admin.service.impl;

import cn.fzu.edu.furever_home.admin.dto.AdminAdoptDetailDTO;
import cn.fzu.edu.furever_home.admin.dto.AdminAdoptSummaryDTO;
import cn.fzu.edu.furever_home.admin.service.AdminAdoptService;
import cn.fzu.edu.furever_home.adopt.entity.Adopt;
import cn.fzu.edu.furever_home.adopt.mapper.AdoptMapper;
import cn.fzu.edu.furever_home.animal.entity.Animal;
import cn.fzu.edu.furever_home.animal.mapper.AnimalMapper;
import cn.fzu.edu.furever_home.auth.entity.User;
import cn.fzu.edu.furever_home.auth.mapper.UserMapper;
import cn.fzu.edu.furever_home.common.enums.ApplicationStatus;
import cn.fzu.edu.furever_home.common.enums.ReviewStatus;
import cn.fzu.edu.furever_home.common.enums.ReviewTargetType;
import cn.fzu.edu.furever_home.common.result.PageResult;
import cn.fzu.edu.furever_home.review.entity.Review;
import cn.fzu.edu.furever_home.review.mapper.ReviewMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminAdoptServiceImpl implements AdminAdoptService {

    private final AdoptMapper adoptMapper;
    private final AnimalMapper animalMapper;
    private final UserMapper userMapper;
    private final ReviewMapper reviewMapper;

    @Override
    public PageResult<AdminAdoptSummaryDTO> listPending(int page, int pageSize, String keyword) {
        Page<Adopt> mpPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Adopt> wrapper = new LambdaQueryWrapper<Adopt>()
                .eq(Adopt::getReviewStatus, ReviewStatus.PENDING)
                .orderByAsc(Adopt::getAdoptId);
        if (keyword != null && !keyword.isBlank()) {
            applyKeywordFilter(wrapper, keyword);
        }
        Page<Adopt> resultPage = adoptMapper.selectPage(mpPage, wrapper);
        List<AdminAdoptSummaryDTO> records = resultPage.getRecords().stream()
                .map(this::toSummaryDTO)
                .collect(Collectors.toList());
        return new PageResult<>(resultPage.getCurrent(), resultPage.getSize(), resultPage.getTotal(), records);
    }

    @Override
    public PageResult<AdminAdoptSummaryDTO> listProcessed(int page, int pageSize, String keyword) {
        Page<Adopt> mpPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Adopt> wrapper = new LambdaQueryWrapper<Adopt>()
                .ne(Adopt::getReviewStatus, ReviewStatus.PENDING)
                .orderByAsc(Adopt::getAdoptId);
        // 处理已审核列表的关键字模糊查询（申请人姓名 / 宠物名称）
        if (keyword != null && !keyword.isBlank()) {
            applyKeywordFilter(wrapper, keyword);
        }
        Page<Adopt> resultPage = adoptMapper.selectPage(mpPage, wrapper);
        List<AdminAdoptSummaryDTO> records = resultPage.getRecords().stream()
                .map(this::toSummaryDTO)
                .collect(Collectors.toList());
        return new PageResult<>(resultPage.getCurrent(), resultPage.getSize(), resultPage.getTotal(), records);
    }

    @Override
    public AdminAdoptDetailDTO getDetail(Integer adoptId) {
        Adopt adopt = adoptMapper.selectById(adoptId);
        if (adopt == null) {
            return null;
        }
        AdminAdoptDetailDTO dto = new AdminAdoptDetailDTO();
        dto.setAdoptId(adopt.getAdoptId());
        dto.setAnimalId(adopt.getAnimalId());
        dto.setUserId(adopt.getUserId());
        dto.setApplicationStatus(adopt.getApplicationStatus());
        dto.setReviewStatus(adopt.getReviewStatus());
        // 目前 Adopt 实体中只保留了基础信息，这里只映射仍然存在的字段
        dto.setAdoptReason(adopt.getAdoptReason());
        dto.setCreateTime(adopt.getCreateTime());
        dto.setPassTime(adopt.getPassTime());

        if (adopt.getAnimalId() != null) {
            Animal animal = animalMapper.selectById(adopt.getAnimalId());
            if (animal != null) {
                dto.setAnimalName(animal.getAnimalName());
                dto.setAnimalSpecies(animal.getSpecies());
                dto.setAnimalGender(animal.getGender());
                // 被申请用户 = 宠物发布者
                if (animal.getUserId() != null) {
                    User owner = userMapper.selectById(animal.getUserId());
                    if (owner != null) {
                        dto.setTargetUserId(owner.getUserId());
                        dto.setTargetUserName(owner.getUserName());
                        dto.setTargetUserAvatar(owner.getAvatarUrl());
                    }
                }
            }
        }
        if (adopt.getUserId() != null) {
            User user = userMapper.selectById(adopt.getUserId());
            if (user != null) {
                dto.setUserName(user.getUserName());
                dto.setUserAvatar(user.getAvatarUrl());
                dto.setAddress(user.getLocation());
            }
        }
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approve(Integer reviewerId, Integer adoptId, String reason) {
        updateReviewAndAdoptStatus(reviewerId, adoptId, reason, ReviewStatus.APPROVED, ApplicationStatus.SUCCESS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reject(Integer reviewerId, Integer adoptId, String reason) {
        updateReviewAndAdoptStatus(reviewerId, adoptId, reason, ReviewStatus.REJECTED, ApplicationStatus.FAIL);
    }

    private void applyKeywordFilter(LambdaQueryWrapper<Adopt> wrapper, String keyword) {
        String kw = keyword.trim();
        Integer id = tryParseInt(kw);

        // 先根据关键字查出匹配的用户和宠物ID，再在申请记录里按 userId / animalId 过滤
        List<Integer> userIds = userMapper.selectList(
                        new LambdaQueryWrapper<User>().like(User::getUserName, kw))
                .stream().map(User::getUserId).toList();
        List<Integer> animalIds = animalMapper.selectList(
                        new LambdaQueryWrapper<Animal>().like(Animal::getAnimalName, kw))
                .stream().map(Animal::getAnimalId).toList();

        // 如果关键字可以解析成数字，则额外按 ID 维度进行匹配
        if (id != null) {
            List<Integer> tmpUserIds = new ArrayList<>(userIds);
            tmpUserIds.add(id);
            userIds = tmpUserIds;

            List<Integer> tmpAnimalIds = new ArrayList<>(animalIds);
            tmpAnimalIds.add(id);
            animalIds = tmpAnimalIds;
        }

        List<Integer> finalUserIds = userIds;
        List<Integer> finalAnimalIds = animalIds;

        wrapper.and(w -> {
            boolean hasCond = false;
            if (!finalUserIds.isEmpty()) {
                w.in(Adopt::getUserId, finalUserIds);
                hasCond = true;
            }
            if (!finalAnimalIds.isEmpty()) {
                if (hasCond) {
                    w.or();
                }
                w.in(Adopt::getAnimalId, finalAnimalIds);
                hasCond = true;
            }
            // 直接按申请 ID 精确匹配
            if (id != null) {
                if (hasCond) {
                    w.or();
                }
                w.eq(Adopt::getAdoptId, id);
                hasCond = true;
            }
            // 如果都没有匹配到，则强制无结果
            if (!hasCond) {
                w.apply("1 = 0");
            }
        });
    }

    private AdminAdoptSummaryDTO toSummaryDTO(Adopt a) {
        AdminAdoptSummaryDTO dto = new AdminAdoptSummaryDTO();
        dto.setAdoptId(a.getAdoptId());
        dto.setAnimalId(a.getAnimalId());
        dto.setUserId(a.getUserId());
        dto.setApplicationStatus(a.getApplicationStatus());
        dto.setReviewStatus(a.getReviewStatus());
        dto.setCreateTime(a.getCreateTime());
        dto.setPassTime(a.getPassTime());
        if (a.getAnimalId() != null) {
            Animal animal = animalMapper.selectById(a.getAnimalId());
            if (animal != null) {
                dto.setAnimalName(animal.getAnimalName());
                // 被申请用户 = 宠物发布者
                if (animal.getUserId() != null) {
                    User owner = userMapper.selectById(animal.getUserId());
                    if (owner != null) {
                        dto.setTargetUserId(owner.getUserId());
                        dto.setTargetUserName(owner.getUserName());
                        dto.setTargetUserAvatar(owner.getAvatarUrl());
                    }
                }
            }
        }
        if (a.getUserId() != null) {
            User user = userMapper.selectById(a.getUserId());
            if (user != null) {
                dto.setUserName(user.getUserName());
                dto.setUserAvatar(user.getAvatarUrl());
            }
        }
        return dto;
    }

    private void updateReviewAndAdoptStatus(Integer reviewerId, Integer adoptId, String reason,
                                            ReviewStatus reviewStatus, ApplicationStatus applicationStatus) {
        if (adoptId == null) {
            throw new IllegalArgumentException("adoptId 不能为空");
        }
        // 使用悲观锁（SELECT FOR UPDATE）锁定当前申请记录，防止并发审核
        Adopt current = adoptMapper.selectOne(
                new LambdaQueryWrapper<Adopt>()
                        .eq(Adopt::getAdoptId, adoptId)
                        .last("FOR UPDATE"));
        if (current == null) {
            throw new IllegalStateException("领养申请不存在");
        }
        if (current.getReviewStatus() != ReviewStatus.PENDING) {
            throw new IllegalStateException("领养申请已被其他管理员处理");
        }

        // ------------------------
        // 处理「同一只动物只允许一条申请通过」的业务规则
        // ------------------------
        ReviewStatus finalReviewStatus = reviewStatus;
        ApplicationStatus finalApplicationStatus = applicationStatus;
        String finalReason = reason;
        boolean conflictAlreadyAdopted = false;
        List<Integer> otherPendingIds = null;
        String autoRejectReason = "该动物已被其他申请人成功领养";

        if (reviewStatus == ReviewStatus.APPROVED && applicationStatus == ApplicationStatus.SUCCESS) {
            // 锁定同一只动物的所有申请记录，避免并发场景下一只动物被多次通过
            List<Adopt> sameAnimalAdopts = adoptMapper.selectList(
                    new LambdaQueryWrapper<Adopt>()
                            .eq(Adopt::getAnimalId, current.getAnimalId())
                            .last("FOR UPDATE"));

            // 是否已经存在其它「已通过」的申请
            boolean hasOtherApproved = sameAnimalAdopts.stream()
                    .anyMatch(a -> !a.getAdoptId().equals(current.getAdoptId())
                            && a.getReviewStatus() == ReviewStatus.APPROVED
                            && a.getApplicationStatus() == ApplicationStatus.SUCCESS);

            if (hasOtherApproved) {
                // 已经有别的申请通过了，本次申请不能再通过，直接按拒绝处理
                conflictAlreadyAdopted = true;
                finalReviewStatus = ReviewStatus.REJECTED;
                finalApplicationStatus = ApplicationStatus.FAIL;
                if (finalReason == null || finalReason.isBlank()) {
                    finalReason = autoRejectReason;
                } else {
                    finalReason = finalReason.trim() + "；" + autoRejectReason;
                }
            } else {
                // 当前这条作为首个通过的申请，其它待审核的同动物申请自动批量拒绝
                otherPendingIds = sameAnimalAdopts.stream()
                        .filter(a -> !a.getAdoptId().equals(current.getAdoptId())
                                && a.getReviewStatus() == ReviewStatus.PENDING)
                        .map(Adopt::getAdoptId)
                        .toList();
            }
        }

        Review review = reviewMapper.selectOne(
                new LambdaQueryWrapper<Review>()
                        .eq(Review::getTargetType, ReviewTargetType.ADOPT)
                        .eq(Review::getTargetId, adoptId)
                        .orderByDesc(Review::getCreateTime)
                        .last("limit 1"));
        if (review != null) {
            review.setStatus(finalReviewStatus);
            review.setReviewerId(reviewerId);
            if (finalReason != null && !finalReason.isBlank()) {
                review.setReason(finalReason.trim());
            }
            reviewMapper.updateById(review);
        }
        LambdaUpdateWrapper<Adopt> updateWrapper = new LambdaUpdateWrapper<Adopt>()
                .eq(Adopt::getAdoptId, adoptId)
                .eq(Adopt::getReviewStatus, ReviewStatus.PENDING)
                .set(Adopt::getReviewStatus, finalReviewStatus)
                .set(Adopt::getApplicationStatus, finalApplicationStatus)
                .set(Adopt::getPassTime, LocalDateTime.now());
        int affected = adoptMapper.update(null, updateWrapper);
        if (affected == 0) {
            throw new IllegalStateException("领养申请状态已变化，请刷新后重试");
        }

        // 如果当前是首个通过的申请，则自动将同一只动物的其他待审核申请批量拒绝
        if (otherPendingIds != null && !otherPendingIds.isEmpty()) {
            // 更新 adopt 表
            adoptMapper.update(null, new LambdaUpdateWrapper<Adopt>()
                    .in(Adopt::getAdoptId, otherPendingIds)
                    .eq(Adopt::getReviewStatus, ReviewStatus.PENDING)
                    .set(Adopt::getReviewStatus, ReviewStatus.REJECTED)
                    .set(Adopt::getApplicationStatus, ApplicationStatus.FAIL)
                    .set(Adopt::getPassTime, LocalDateTime.now()));

            // 更新对应的 review 记录
            reviewMapper.update(null, new LambdaUpdateWrapper<Review>()
                    .eq(Review::getTargetType, ReviewTargetType.ADOPT)
                    .in(Review::getTargetId, otherPendingIds)
                    .set(Review::getStatus, ReviewStatus.REJECTED)
                    .set(Review::getReviewerId, reviewerId)
                    .set(Review::getReason, autoRejectReason));
        }

        // 如果发现该动物已经被其他申请人成功领养，则在更新当前记录为拒绝后抛出业务提示，让前端弹出友好信息
        if (conflictAlreadyAdopted) {
            throw new IllegalStateException("该动物已被其他申请人成功领养，不能再次通过该申请");
        }
    }

    /**
     * 尝试将关键字解析为整数，失败则返回 null（用于按 ID 精确匹配）。
     */
    private Integer tryParseInt(String s) {
        try {
            return Integer.valueOf(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}


