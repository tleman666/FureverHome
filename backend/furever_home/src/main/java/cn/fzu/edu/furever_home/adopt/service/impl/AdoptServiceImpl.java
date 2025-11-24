package cn.fzu.edu.furever_home.adopt.service.impl;

import cn.fzu.edu.furever_home.adopt.dto.AdoptDTO;
import cn.fzu.edu.furever_home.adopt.entity.Adopt;
import cn.fzu.edu.furever_home.adopt.mapper.AdoptMapper;
import cn.fzu.edu.furever_home.adopt.request.SubmitAdoptRequest;
import cn.fzu.edu.furever_home.adopt.service.AdoptService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdoptServiceImpl implements AdoptService {
    private final AdoptMapper adoptMapper;

    @Override
    public Integer submit(Integer userId, SubmitAdoptRequest req) {
        Long exists = adoptMapper.selectCount(new LambdaQueryWrapper<Adopt>()
                .eq(Adopt::getAnimalId, req.getAnimalId())
                .eq(Adopt::getUserId, userId));
        if (exists != null && exists > 0) {
            throw new IllegalStateException("已提交过该动物的领养申请");
        }
        Adopt a = new Adopt();
        a.setAnimalId(req.getAnimalId());
        a.setUserId(userId);
        a.setApplicationStatus("申请中");
        a.setLivingEnvironment(req.getLivingEnvironment());
        a.setHouseType(req.getHouseType());
        a.setHasOtherPets(req.getHasOtherPets());
        a.setFamilyMemberCount(req.getFamilyMemberCount());
        a.setHasChild(req.getHasChild());
        a.setAdoptReason(req.getAdoptReason());
        a.setMonthSalary(req.getMonthSalary());
        a.setCreateTime(LocalDateTime.now());
        adoptMapper.insert(a);
        return a.getAdoptId();
    }

    @Override
    public AdoptDTO getById(Integer id) {
        Adopt a = adoptMapper.selectById(id);
        return toDTO(a);
    }

    @Override
    public void review(Integer id, String status) {
        Adopt a = adoptMapper.selectById(id);
        if (a == null) throw new IllegalStateException("申请不存在");
        if (!"申请成功".equals(status) && !"申请失败".equals(status)) {
            throw new IllegalArgumentException("状态不合法");
        }
        a.setApplicationStatus(status);
        a.setPassTime(LocalDateTime.now());
        adoptMapper.updateById(a);
    }

    private AdoptDTO toDTO(Adopt a) {
        if (a == null) return null;
        AdoptDTO d = new AdoptDTO();
        d.setAdoptId(a.getAdoptId());
        d.setAnimalId(a.getAnimalId());
        d.setUserId(a.getUserId());
        d.setApplicationStatus(a.getApplicationStatus());
        d.setLivingEnvironment(a.getLivingEnvironment());
        d.setHouseType(a.getHouseType());
        d.setHasOtherPets(a.getHasOtherPets());
        d.setFamilyMemberCount(a.getFamilyMemberCount());
        d.setHasChild(a.getHasChild());
        d.setAdoptReason(a.getAdoptReason());
        d.setMonthSalary(a.getMonthSalary());
        d.setCreateTime(a.getCreateTime());
        d.setPassTime(a.getPassTime());
        return d;
    }
}