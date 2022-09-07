package fr.insee.survey.datacollectionmanagement.questioning.controller;

import java.util.NoSuchElementException;

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

import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningAccreditation;
import fr.insee.survey.datacollectionmanagement.questioning.service.QuestioningAccreditationService;

@RestController
@CrossOrigin
public class QuestioningAccreditationController {

    static final Logger LOGGER = LoggerFactory.getLogger(QuestioningAccreditationController.class);

    @Autowired
    private QuestioningAccreditationService questioningAccreditationService;

    @GetMapping(value = "questioningAccreditations")
    public Page<QuestioningAccreditation> findQuestioningAccreditations(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "20") Integer size,
        @RequestParam(defaultValue = "id") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return questioningAccreditationService.findAll(pageable);
    }

    @GetMapping(value = "questioningAccreditations/{id}")
    public ResponseEntity<?> findQuestioningAccreditation(@PathVariable("id") Long id) {
        QuestioningAccreditation questioningAccreditation = null;
        try {
            questioningAccreditation = questioningAccreditationService.findById(id);
            return new ResponseEntity<>(questioningAccreditation, HttpStatus.OK);
        }
        catch (NoSuchElementException e) {
            return new ResponseEntity<>(questioningAccreditation, HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<String>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "questioningAccreditations/{id}")
    public ResponseEntity<?> putQuestioningAccreditation(@PathVariable("id") Long id, @RequestBody QuestioningAccreditation questioningAccreditation) {
        if (questioningAccreditation.getId() == null || questioningAccreditation.getId() != id) {
            return new ResponseEntity<>("id and questioningAccreditation identifier don't match", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(questioningAccreditationService.updateQuestioningAccreditation(questioningAccreditation), HttpStatus.OK);
    }


}
