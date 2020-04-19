package com.amsidh.mvc.controller;

import com.amsidh.mvc.model.Person;
import com.amsidh.mvc.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping(value = "/secured", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<Person>> getAllPerson() {
        return new ResponseEntity<>(personService.getAllPerson(), HttpStatus.OK);
    }

    @GetMapping(value = "/nonsecured", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> getNonSecured() {
        return new ResponseEntity<>("This is non secured zone and you are responsible for your own security", HttpStatus.OK);
    }
}
