package cn.fzu.edu.furever_home.animal.service;

import cn.fzu.edu.furever_home.animal.dto.AnimalDTO;
import cn.fzu.edu.furever_home.animal.request.CreateAnimalRequest;
import cn.fzu.edu.furever_home.animal.request.UpdateAnimalRequest;

import java.util.List;

public interface AnimalService {
    List<AnimalDTO> listAll();
    AnimalDTO getById(Integer id);
    Integer create(Integer userId, CreateAnimalRequest req);
    void update(Integer userId, Integer id, UpdateAnimalRequest req);
    void delete(Integer userId, Integer id);
}