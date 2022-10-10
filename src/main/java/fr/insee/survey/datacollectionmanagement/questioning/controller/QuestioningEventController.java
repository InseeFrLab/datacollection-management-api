package fr.insee.survey.datacollectionmanagement.questioning.controller;

import java.text.ParseException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import fr.insee.survey.datacollectionmanagement.constants.Constants;
import fr.insee.survey.datacollectionmanagement.questioning.domain.Questioning;
import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningEvent;
import fr.insee.survey.datacollectionmanagement.questioning.dto.QuestioningEventDto;
import fr.insee.survey.datacollectionmanagement.questioning.service.QuestioningEventService;
import fr.insee.survey.datacollectionmanagement.questioning.service.QuestioningService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin
@Tag(name = "2 - Questioning", description = "Enpoints to create, update, delete and find entities around the questioning")
public class QuestioningEventController {

    static final Logger LOGGER = LoggerFactory.getLogger(QuestioningEventController.class);

    @Autowired
    private QuestioningEventService questioningEventService;

    @Autowired
    private QuestioningService questioningService;

    @Autowired
    private ModelMapper modelMapper;

    @Operation(summary = "Search for a questioning event by questioning id")
    @GetMapping(value = Constants.API_QUESTIONING_ID_QUESTIONING_EVENTS, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = QuestioningEventDto.class)))),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    public ResponseEntity<?> findQuestioningEventsByQuestioning(@PathVariable("id") Long id) {
        try {
            Questioning questioning = questioningService.findbyId(id);
            Set<QuestioningEvent> setQe = questioning.getQuestioningEvents();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(setQe.stream()
                            .map(q -> convertToDto(q)).collect(Collectors.toList()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("questioning not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error");
        }
    }

    @Operation(summary = "Create a questioning event")
    @PostMapping(value = Constants.API_QUESTIONING_QUESTIONING_EVENTS, produces = "application/json", consumes = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = QuestioningEventDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> postQuestioningEvent(@Parameter(description = "questioning id") Long id,
            @RequestBody QuestioningEventDto questioningEventDto) {
        try {
            Questioning questioning = questioningService.findbyId(id);
            QuestioningEvent questioningEvent = convertToEntity(questioningEventDto);
            QuestioningEvent newQuestioningEvent = questioningEventService.saveQuestioningEvent(questioningEvent);
            Set<QuestioningEvent> setQuestioningEvents = questioning.getQuestioningEvents();
            setQuestioningEvents.add(newQuestioningEvent);
            questioning.setQuestioningEvents(setQuestioningEvents);
            questioningService.saveQuestioning(questioning);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set(HttpHeaders.LOCATION, ServletUriComponentsBuilder.fromCurrentRequest().toUriString());
            return ResponseEntity.status(HttpStatus.CREATED).headers(responseHeaders)
                    .body(convertToDto(newQuestioningEvent));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Questioning does not exist");
        } catch (ParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error");
        }

    }

    @Operation(summary = "Delete a questioning event")
    @DeleteMapping(value = Constants.API_QUESTIONING_QUESTIONING_EVENTS_ID, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    public ResponseEntity<?> deleteQuestioningEvent(@PathVariable("id") Long id) {
        try {
            QuestioningEvent questioningEvent = questioningEventService.findbyId(id);
            Questioning quesitoning = questioningEvent.getQuestioning();
            quesitoning.setQuestioningEvents(quesitoning.getQuestioningEvents().stream().filter(qe->!qe.equals(questioningEvent)).collect(Collectors.toSet()));
            questioningService.saveQuestioning(quesitoning);
            questioningEventService.deleteQuestioningEvent(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Questioning event deleted");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Questioning event does not exist");
        } catch (Exception e) {
            return new ResponseEntity<String>("Error", HttpStatus.BAD_REQUEST);
        }
    }

    private QuestioningEventDto convertToDto(QuestioningEvent questioningEvent) {
        return modelMapper.map(questioningEvent, QuestioningEventDto.class);
    }

    private QuestioningEvent convertToEntity(QuestioningEventDto questioningEventDto) throws ParseException {
        return modelMapper.map(questioningEventDto, QuestioningEvent.class);
    }

    class QuestioningEventPage extends PageImpl<QuestioningEventDto> {

        private static final long serialVersionUID = 656181199902518234L;

        public QuestioningEventPage(List<QuestioningEventDto> content, Pageable pageable, long total) {
            super(content, pageable, total);
        }
    }
}
