package com.phatpl.learnvocabulary.controllers;

import com.phatpl.learnvocabulary.dtos.BaseDTO;
import com.phatpl.learnvocabulary.filters.BaseFilter;
import com.phatpl.learnvocabulary.models.BaseModel;
import com.phatpl.learnvocabulary.services.BaseService;
import com.phatpl.learnvocabulary.utils.BuildResponse;
import jakarta.persistence.MappedSuperclass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@MappedSuperclass
public class BaseController<E extends BaseModel, DTO extends BaseDTO, FT extends BaseFilter, ID extends Integer> {
    private final BaseService<E, DTO, FT, ID> service;

    @Autowired
    public BaseController(BaseService<E, DTO, FT, ID> service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable("id") Integer id) {
        DTO response = service.findDTOById(id);
        if (response != null) {
            return BuildResponse.ok(response);
        }
        return BuildResponse.notFound(" not found id = " + id);
    }

    @GetMapping
    public ResponseEntity findAll(@RequestBody FT ft) {
        List<DTO> lst = service.findAllDTO();
        return BuildResponse.ok(lst);
    }
}
