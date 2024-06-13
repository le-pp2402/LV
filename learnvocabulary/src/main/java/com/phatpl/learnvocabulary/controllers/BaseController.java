package com.phatpl.learnvocabulary.controllers;

import com.phatpl.learnvocabulary.dtos.BaseDTO;
import com.phatpl.learnvocabulary.filters.BaseFilter;
import com.phatpl.learnvocabulary.models.BaseModel;
import com.phatpl.learnvocabulary.services.BaseService;
import jakarta.persistence.MappedSuperclass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@MappedSuperclass
public class BaseController <E extends BaseModel, DTO extends BaseDTO, FT extends BaseFilter, ID extends Integer> {
    private final BaseService<E,DTO,FT,ID> service;

    public BaseController(BaseService<E, DTO, FT, ID> service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        DTO response = service.findById(id);
        if (service != null) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NOT FOUND");
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        List<DTO> lst = service.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(lst);
    }

}
