package org.ksens.demo.java.springboot.clothstore.controller;

import org.ksens.demo.java.springboot.clothstore.models.CategoryModel;
import org.ksens.demo.java.springboot.clothstore.models.ResponseModel;
import org.ksens.demo.java.springboot.clothstore.models.SizeModel;
import org.ksens.demo.java.springboot.clothstore.services.interfaces.ISizeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SizeController {

    private final ISizeService service;

    public SizeController(ISizeService service) {
        this.service = service;
    }

    @GetMapping("/sizes")
    public ResponseEntity<ResponseModel> getSizes() {
        return new ResponseEntity<>(service.getSizes(), HttpStatus.OK);
    }

    @PostMapping("/sizes")
    public ResponseEntity<ResponseModel> createSize(@RequestBody SizeModel size) {
        return new ResponseEntity<>(service.createSize(size), HttpStatus.CREATED);
    }

    @PatchMapping(value = "/sizes/{id}")
    public ResponseEntity<ResponseModel> updateSize(@PathVariable Long id, @RequestBody SizeModel size) {
        size.setId(id);
        return new ResponseEntity<>(service.updateSize(size), HttpStatus.OK);
    }

    @DeleteMapping(value = "/sizes/{id}")
    public ResponseEntity<ResponseModel> deleteSize(@PathVariable Long id) {
        ResponseModel responseModel = service.deleteSize(id);
        return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }

}
