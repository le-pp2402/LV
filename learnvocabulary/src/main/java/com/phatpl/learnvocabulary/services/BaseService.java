package com.phatpl.learnvocabulary.services;

import com.phatpl.learnvocabulary.dtos.BaseDTO;
import com.phatpl.learnvocabulary.filters.BaseFilter;
import com.phatpl.learnvocabulary.mappers.BaseMapper;
import com.phatpl.learnvocabulary.models.BaseModel;
import com.phatpl.learnvocabulary.repositories.BaseRepository;
import jakarta.persistence.MappedSuperclass;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@MappedSuperclass
public class BaseService<E extends BaseModel,
        DTO extends BaseDTO,
        FT extends BaseFilter,
        ID extends Integer> {
    private final BaseMapper<E, DTO> baseMapper;
    private final BaseRepository<E, FT, ID> baseRepository;

    @Autowired
    public BaseService(BaseMapper<E, DTO> baseMapper, BaseRepository<E, FT, ID> repo) {
        this.baseMapper = baseMapper;
        this.baseRepository = repo;
    }

    public List<DTO> findAll() {
        return baseMapper.toListDTO(baseRepository.findAll());
    }


    public DTO findById(Integer id) {
        Optional<E> opt = baseRepository.findById((ID) id);
        if (opt.isEmpty()) return null;
        return baseMapper.toDTO(opt.get());
    }

    public DTO save(E entity) {
        return baseMapper.toDTO(baseRepository.save(entity));
    }

    public DTO update(E entity) {
        if (baseRepository.findById((ID) entity.getId()).isEmpty()) return null;
        baseRepository.deleteById((ID) entity.getId());
        return baseMapper.toDTO(baseRepository.save(entity));
    }
    
}
