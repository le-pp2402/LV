package com.phatpl.learnvocabulary.services;

import com.phatpl.learnvocabulary.dtos.BaseDTO;
import com.phatpl.learnvocabulary.filters.BaseFilter;
import com.phatpl.learnvocabulary.mappers.BaseMapper;
import com.phatpl.learnvocabulary.models.BaseModel;
import com.phatpl.learnvocabulary.repositories.BaseRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.MappedSuperclass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
@MappedSuperclass
public class BaseService<E extends BaseModel,
        DTO extends BaseDTO,
        FT extends BaseFilter,
        ID extends Integer> {
    private final BaseMapper<E, DTO> baseMapper;
    private final BaseRepository<E, FT, ID> repo;

    @Autowired
    public BaseService(BaseMapper<E, DTO> baseMapper, BaseRepository<E, FT, ID> repo) {
        this.baseMapper = baseMapper;
        this.repo = repo;
    }
    
    public List<E> findAll() {
        return repo.findAll();
    }

    public List<DTO> findAllDTO() {
        return baseMapper.toListDTO(repo.findAll());
    }

    public E findById(Integer id) {
        return repo.findById((ID) id).orElseThrow(EntityNotFoundException::new);
    }

    public DTO findDTOById(Integer id) {
        return baseMapper.toDTO(findById(id));
    }

    public E persistEntity(E entity) {
        return repo.save(entity);
    }

    public DTO createDTO(E entity) {
        return baseMapper.toDTO(persistEntity(entity));
    }

    public void deleteById(ID Id) {
        repo.deleteById(Id);
    }

    public List<DTO> findByFilter(BaseFilter filter) {
        return baseMapper.toListDTO(repo.findAll(filter.getPageable()));
    }
}
