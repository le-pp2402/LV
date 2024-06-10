package com.phatpl.learnvocabulary.services;

import com.phatpl.learnvocabulary.dtos.BaseDTO;
import com.phatpl.learnvocabulary.filters.BaseFilter;
import com.phatpl.learnvocabulary.mappers.BaseMapper;
import com.phatpl.learnvocabulary.models.BaseModel;
import com.phatpl.learnvocabulary.repositories.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

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

    public List<DTO> findAll() {
        return baseMapper.toListDTO(repo.findAll());
    }


    public DTO findById(Integer id) {
        Optional<E> opt = repo.findById((ID) id);
        if (opt.isEmpty()) return null;
        return baseMapper.toDTO(opt.get());
    }

    public E add(E entity) {
        return repo.save(entity);
    }

    public E update(E entity) {
        if (repo.findById((ID) entity.getId()).isEmpty()) return null;
        repo.deleteById((ID) entity.getId());
        return repo.save(entity);
    }

    public boolean deleteById(E e) {
        if (repo.findById((ID) e.getId()).isEmpty()) return false;
        repo.deleteById((ID) e.getId());
        return true;
    }
}
