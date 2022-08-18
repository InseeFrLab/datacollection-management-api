package fr.insee.survey.datacollectionmanagement.questioning.controller;

import java.util.NoSuchElementException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.insee.survey.datacollectionmanagement.questioning.domain.Questioning;
import fr.insee.survey.datacollectionmanagement.questioning.service.QuestioningService;

@RestController
@CrossOrigin
public class QuestioningController {

    static final Logger LOGGER = LoggerFactory.getLogger(QuestioningController.class);

    @Autowired
    private QuestioningService questioningService;

    @GetMapping(value = "questionings")
    public Page<Questioning> findQuestionings(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "20") Integer size,
        @RequestParam(defaultValue = "id") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return questioningService.findAll(pageable);
    }

    @GetMapping(value = "questionings/{id}")
    public ResponseEntity<?> findQuestioning(@PathVariable("id") Long id) {
        Questioning questioning = null;
        try {
            questioning = questioningService.findbyId(id);
            return new ResponseEntity<>(questioning, HttpStatus.OK);
        }
        catch (NoSuchElementException e) {
            return new ResponseEntity<>(questioning, HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<String>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping(value = "questionings/{id}")
    public ResponseEntity<?> putQuestioning(@PathVariable("id") Long id, @RequestBody Questioning questioning) {
        if (questioning.getId() == null || questioning.getId() != id) {
            return new ResponseEntity<>("id and questioning identifier don't match", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(questioningService.updateQuestioning(questioning), HttpStatus.OK);
    }

}
