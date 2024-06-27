package com.phatpl.learnvocabulary.services;

import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import com.phatpl.learnvocabulary.dtos.BaseDTO;
import com.phatpl.learnvocabulary.filters.BaseFilter;
import com.phatpl.learnvocabulary.mappers.BaseMapper;
import com.phatpl.learnvocabulary.models.BaseModel;
import com.phatpl.learnvocabulary.repositories.BaseRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.MappedSuperclass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

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

    public Integer extractUserId(JwtAuthenticationToken jwtAuth) {
        var data = (LinkedTreeMap<String, Object>) jwtAuth.getTokenAttributes().get("data");
        return ((Long) data.get("id")).intValue();
    }

    public E findById(Integer id) {
        var entity = repo.findById((ID) id).orElseThrow(EntityNotFoundException::new);
        return entity;
    }

    public DTO save(E entity) {
        return baseMapper.toDTO(repo.save(entity));
    }

    public DTO update(E entity) {
        return baseMapper.toDTO(repo.save(entity));
    }

    public List<DTO> findAllDTO() {
        return baseMapper.toListDTO(repo.findAll());
    }

    public DTO findByIdDTO(Integer id) {
        return baseMapper.toDTO(this.findById(id));
    }

    public void deleteById(ID Id) {
        repo.deleteById(Id);
    }


//    public List<DTO> findWithFilter(FT ft) {
//        return baseMapper.toListDTO(repo.findWithFilter(ft.getPageable()));
//    }
}
