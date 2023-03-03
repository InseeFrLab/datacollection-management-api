package fr.insee.survey.datacollectionmanagement.contact.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import fr.insee.survey.datacollectionmanagement.contact.domain.ContactEvent.ContactEventType;
import fr.insee.survey.datacollectionmanagement.contact.dto.ContactEventDto;
import fr.insee.survey.datacollectionmanagement.contact.service.ContactEventService;
import fr.insee.survey.datacollectionmanagement.contact.service.ContactService;
import fr.insee.survey.datacollectionmanagement.exception.EventException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController(value = "contactEvents")
@PreAuthorize("@AuthorizeMethodDecider.isInternalUser() "
        + "|| @AuthorizeMethodDecider.isWebClient() "+ "|| @AuthorizeMethodDecider.isRespondent() "
        + "|| @AuthorizeMethodDecider.isAdmin() ")
@Tag(name = "1 - Contacts", description = "Enpoints to create, update, delete and find contacts")
public class ContactEventController {

    static final Logger LOGGER = LoggerFactory.getLogger(ContactEventController.class);

    @Autowired
    private ContactEventService contactEventService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private ModelMapper modelMapper;

    @Operation(summary = "Search for contactEvents by the contact id")
    @GetMapping(value = Constants.API_CONTACTS_ID_CONTACTEVENTS, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ContactEventDto.class)))),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Internal servor error")
    })
    public ResponseEntity<?> getContactContactEvents(@PathVariable("id") String identifier) {
        try {

            Optional<Contact> optContact = contactService.findByIdentifier(identifier);
            if (optContact.isPresent()) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(optContact.get().getContactEvents().stream().map(ce -> convertToDto(ce))
                                .collect(Collectors.toList()));

            } else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Contact does not exist");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error");
        }

    }

    @Operation(summary = "Create a contactEvent")
    @PostMapping(value = Constants.API_CONTACTEVENTS, produces = "application/json", consumes = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = ContactEventDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<?> postContactEvent(@RequestBody ContactEventDto contactEventDto) {
        try {

            Optional<Contact> optContact = contactService.findByIdentifier(contactEventDto.getIdentifier());
            if (optContact.isPresent()) {
                Contact contact = optContact.get();
                ContactEvent contactEvent = convertToEntity(contactEventDto);
                ContactEvent newContactEvent = contactEventService.saveContactEvent(contactEvent);
                Set<ContactEvent> setContactEvents = contact.getContactEvents();
                setContactEvents.add(newContactEvent);
                contact.setContactEvents(setContactEvents);
                contactService.saveContact(contact);
                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.set(HttpHeaders.LOCATION,
                        ServletUriComponentsBuilder.fromCurrentRequest().toUriString());
                return ResponseEntity.status(HttpStatus.CREATED).headers(responseHeaders)
                        .body(convertToDto(newContactEvent));
            } else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Contact does not exist");
        } catch (EventException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Event not recognized: only [" + Stream.of(ContactEventType.values())
                            .map(ContactEventType::name).collect(Collectors.joining(", ")) + "] are possible");
        }

    }

    @Operation(summary = "Delete a contact event")
    @DeleteMapping(value = Constants.API_CONTACTEVENTS_ID, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    public ResponseEntity<?> deleteContactEvent(@PathVariable("id") Long id) {
        try {
            Optional<ContactEvent> optContactEvent = contactEventService.findById(id);
            if (optContactEvent.isPresent()) {
                ContactEvent contactEvent = optContactEvent.get();
                Contact contact = contactEvent.getContact();
                contact.setContactEvents(contact.getContactEvents().stream().filter(ce -> !ce.equals(contactEvent))
                        .collect(Collectors.toSet()));
                contactService.saveContact(contact);
                contactEventService.deleteContactEvent(id);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Contact event deleted");
            } else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Contact event does not exist");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error");

        }
    }

    private ContactEventDto convertToDto(ContactEvent contactEvent) {
        ContactEventDto ceDto = modelMapper.map(contactEvent, ContactEventDto.class);
        ceDto.setIdentifier(contactEvent.getContact().getIdentifier());
        return ceDto;
    }

    private ContactEvent convertToEntity(ContactEventDto contactEventDto) throws EventException {
        ContactEvent contactEvent = modelMapper.map(contactEventDto, ContactEvent.class);
        if (contactEvent.getType() == null)
            throw new EventException("Contact event not recognized");
        return contactEvent;
    }

    class ContactEventPage extends PageImpl<ContactEventDto> {

        private static final long serialVersionUID = 3619811755902956158L;

        public ContactEventPage(List<ContactEventDto> content, Pageable pageable, long total) {
            super(content, pageable, total);
        }
    }

}
