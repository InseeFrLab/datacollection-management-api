package fr.insee.survey.datacollectionmanagement.metadata.controller;

import java.util.NoSuchElementException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Source;
import fr.insee.survey.datacollectionmanagement.metadata.service.SourceService;

@RestController
@CrossOrigin
public class SourceController {

    static final Logger LOGGER = LoggerFactory.getLogger(SourceController.class);

    @Autowired
    private SourceService sourceService;

    @GetMapping(value = "sources")
    public Page<Source> findSources(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "20") Integer size,
        @RequestParam(defaultValue = "idSource") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return sourceService.findAll(pageable);
    }

    @GetMapping(value = "sources/{id}")
    public ResponseEntity<?> findSource(@PathVariable("id") String id) {
        Source source = null;
        try {
            source = sourceService.findbyId(StringUtils.upperCase(id));
            return new ResponseEntity<>(source, HttpStatus.OK);
        }
        catch (NoSuchElementException e) {
            return new ResponseEntity<>(source, HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<String>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping(value = "sources/{id}")
    public ResponseEntity<?> putSource(@PathVariable("id") String id, @RequestBody Source source) {
        if (StringUtils.isBlank(source.getIdSource()) || !source.getIdSource().equalsIgnoreCase(id)) {
            return new ResponseEntity<>("id and source identifier don't match", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(sourceService.updateSource(source), HttpStatus.OK);
    }


}
