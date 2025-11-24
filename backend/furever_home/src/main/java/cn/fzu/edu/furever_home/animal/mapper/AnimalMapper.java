package cn.fzu.edu.furever_home.animal.mapper;

import cn.fzu.edu.furever_home.animal.entity.Animal;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AnimalMapper extends BaseMapper<Animal> {}