package com.phatpl.learnvocabulary.services;

import com.phatpl.learnvocabulary.dtos.BaseDTO;
import com.phatpl.learnvocabulary.mappers.BaseMapper;
import com.phatpl.learnvocabulary.models.BaseEntity;
import com.phatpl.learnvocabulary.repositories.BaseRepository;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@MappedSuperclass
public class BaseService<E extends BaseEntity, REPO extends BaseRepository<E>, DTO extends BaseDTO> {
    private final BaseMapper<E, DTO> baseMapper;
    private final REPO repo;

    public BaseService(BaseMapper<E, DTO> baseMapper, REPO repo) {
        this.baseMapper = baseMapper;
        this.repo = repo;
    }

    public List<DTO> findAll() {
        return baseMapper.toListDTO(repo.findAll());
    }

    public DTO findById(Integer id) {
        Optional<E> opt = repo.findById(id);
        if (!opt.isPresent()) return null;
        return baseMapper.toDTO(opt.get());
    }

    public E add(E entity) {
        return repo.save(entity);
    }

    public E update(E entity) {
        if (!repo.findById(entity.getId()).isPresent()) return null;
        repo.deleteById(entity.getId());
        return repo.save(entity);
    }

    public boolean deleteById(E e) {
        if (repo.findById(e.getId()) == null) return false;
        repo.deleteById(e.getId());
        return true;
    }
}
