package fr.insee.survey.datacollectionmanagement.contact.controller;

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
import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;
import fr.insee.survey.datacollectionmanagement.contact.domain.ContactEvent;
import fr.insee.survey.datacollectionmanagement.contact.dto.ContactEventDto;
import fr.insee.survey.datacollectionmanagement.contact.service.ContactEventService;
import fr.insee.survey.datacollectionmanagement.contact.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController(value = "contactEvents")
@CrossOrigin
public class ContactEventController {

    static final Logger LOGGER = LoggerFactory.getLogger(ContactEventController.class);

    @Autowired
    private ContactEventService contactEventService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private ModelMapper modelMapper;

    @Operation(summary = "Search for contactEvents by the contact identifier")
    @GetMapping(value = Constants.API_CONTACTS_ID_CONTACTEVENTS, produces = "application/json")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ContactEventDto.class)))),
        @ApiResponse(responseCode = "404", description = "Not found"), @ApiResponse(responseCode = "500", description = "Internal servor error")
    })
    public ResponseEntity<?> getContactContactEvents(@PathVariable("id") String identifier) {
        try {
            Contact contact = contactService.findByIdentifier(identifier);
            return new ResponseEntity<>(contact.getContactEvents().stream().map(ce -> convertToDto(ce)).collect(Collectors.toList()), HttpStatus.OK);
        }
        catch (NoSuchElementException e) {
            return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<String>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Operation(summary = "Create a contactEvent")
    @PostMapping(value = Constants.API_CONTACTEVENTS, produces = "application/json", consumes = "application/json")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Created",
            content = @Content(schema = @Schema(implementation = ContactEventDto.class))),
        @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> postContactEvent(@RequestBody ContactEventDto contactEventDto) {
        try {
            Contact contact = contactService.findByIdentifier(contactEventDto.getIdentifier());
            ContactEvent contactEvent = convertToEntity(contactEventDto);
            ContactEvent newContactEvent = contactEventService.saveContactEvent(contactEvent);
            Set<ContactEvent> setContactEvents = contact.getContactEvents();
            setContactEvents.add(newContactEvent);
            contact.setContactEvents(setContactEvents);
            contactService.saveContact(contact);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set(HttpHeaders.LOCATION, ServletUriComponentsBuilder.fromCurrentRequest().toUriString());;
            return ResponseEntity.status(HttpStatus.CREATED).headers(responseHeaders).body(convertToDto(newContactEvent));
        }
        catch (NoSuchElementException e) {
            return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
        }

    }

    @Operation(summary = "Delete a contact event")
    @DeleteMapping(value = Constants.API_CONTACTEVENTS_ID, produces = "application/json")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "No Content"), @ApiResponse(responseCode = "404", description = "Not found"),
        @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    public ResponseEntity<?> deleteContactEvent(@PathVariable("id") Long id) {
        try {
            contactEventService.deleteContactEvent(id);
            return new ResponseEntity<>("ContactEvent deleted", HttpStatus.NO_CONTENT);
        }
        catch (NoSuchElementException e) {
            return new ResponseEntity<>("ContactEvent not found", HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<String>("Error", HttpStatus.BAD_REQUEST);
        }
    }

    private ContactEventDto convertToDto(ContactEvent contactEvent) {
        ContactEventDto ceDto = modelMapper.map(contactEvent, ContactEventDto.class);
        ceDto.setIdentifier(contactEvent.getContact().getIdentifier());
        return ceDto;
    }

    private ContactEvent convertToEntity(ContactEventDto contactEventDto) {
        ContactEvent contactEvent = modelMapper.map(contactEventDto, ContactEvent.class);
        return contactEvent;
    }

    class ContactEventPage extends PageImpl<ContactEventDto> {

        private static final long serialVersionUID = 3619811755902956158L;

        public ContactEventPage(List<ContactEventDto> content, Pageable pageable, long total) {
            super(content, pageable, total);
        }
    }

}
