package com.phatpl.learnvocabulary.controllers;

import com.phatpl.learnvocabulary.dtos.BaseDTO;
import com.phatpl.learnvocabulary.dtos.Response;
import com.phatpl.learnvocabulary.filters.BaseFilter;
import com.phatpl.learnvocabulary.models.BaseModel;
import com.phatpl.learnvocabulary.services.BaseService;
import com.phatpl.learnvocabulary.utils.Logger;
import jakarta.persistence.MappedSuperclass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.Console;
import java.util.List;

@MappedSuperclass
public class BaseController <E extends BaseModel, DTO extends BaseDTO, FT extends BaseFilter, ID extends Integer> {
    private final BaseService<E,DTO,FT,ID> service;
    @Autowired
    public BaseController(BaseService<E, DTO, FT, ID> service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Integer id) {
        Logger.log("ok");
        DTO response = service.findById(id);
        if (service != null) {
            return ResponseEntity.ok(Response.builder().code(HttpStatus.OK.value()).data(response).message("Success").build());
        }
        return ResponseEntity.ok(Response.builder().code(HttpStatus.NOT_FOUND.value()).data("NOT FOUND").message("Failed").build());
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        List<DTO> lst = service.findAll();
        return ResponseEntity.ok(Response.builder().code(HttpStatus.OK.value()).data(lst).message("Success").build());
    }

}
