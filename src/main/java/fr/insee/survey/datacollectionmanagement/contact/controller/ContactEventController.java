package fr.insee.survey.datacollectionmanagement.contact.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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

@RestController
@CrossOrigin
public class ContactEventController {

    static final Logger LOGGER = LoggerFactory.getLogger(ContactEventController.class);

    @Autowired
    private ContactEventService contactEventService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private ModelMapper modelMapper;

    @Operation(summary = "Search for contactEvents, paginated")
    @GetMapping(value = "contactEvents", produces = "application/hal+json")
    public Page<ContactEventDto> getContactEvents(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "20") Integer size,
        @RequestParam(defaultValue = "id") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        List<ContactEventDto> listDto = contactEventService.findAll(pageable).stream().map(ce -> convertToDto(ce)).collect(Collectors.toList());
        return new PageImpl<>(listDto, pageable, listDto.size());
    }

    @Operation(summary = "Search for contactEvents by the contact identifier")
    @GetMapping(value = "contacts/{id}/contactEvents", produces = "application/hal+json")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ContactEventDto.class)))),
        @ApiResponse(responseCode = "404", description = "Not found"), @ApiResponse(responseCode = "500", description = "Internal servor error")
    })
    public ResponseEntity<?> getContactContactEvent(@PathVariable("id") String identifier) {
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

    @Operation(summary = "Search for a contactEvent by its id")
    @GetMapping(value = "contactEvents/{id}", produces = "application/hal+json")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ContactEventDto.class)))),
        @ApiResponse(responseCode = "404", description = "Not found"), @ApiResponse(responseCode = "500", description = "Internal servor error")
    })
    public ResponseEntity<?> getContactEvent(@PathVariable("id") Long id) {
        try {
            ContactEventDto contactEventDto = convertToDto(contactEventService.findById(id));
            return new ResponseEntity<>(contactEventDto, HttpStatus.OK);
        }
        catch (NoSuchElementException e) {
            return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<String>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Operation(summary = "Update or create a contactEvent")
    @PutMapping(value = "contactEvents/{id}")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ContactEventDto.class)))),
        @ApiResponse(
            responseCode = "201",
            description = "Created",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ContactEventDto.class)))),
        @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> putContactEvent(@PathVariable("id") Long id, @RequestBody ContactEventDto contactEventDto) {
        if ( !contactEventDto.getId().equals(id)) {
            return new ResponseEntity<>("id and contactEvent identifier don't match", HttpStatus.BAD_REQUEST);
        }
        ContactEvent contactEvent;
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(HttpHeaders.LOCATION,
            ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(contactEventDto.getId()).toUriString());
        try {
            contactEventService.findById(id);
        }
        catch (NoSuchElementException e) {
            LOGGER.info("Creating contactEvent with the identifier {}, ", contactEventDto.getId());
            contactEvent = convertToEntity(contactEventDto);
            ContactEvent contactEventUpdate = contactEventService.updateContactEvent(contactEvent);
            return ResponseEntity.status(HttpStatus.CREATED).headers(responseHeaders).body(convertToDto(contactEventUpdate));
        }
        return new ResponseEntity<>(contactEventService.updateContactEvent(convertToEntity(contactEventDto)), HttpStatus.OK);
    }

    private ContactEventDto convertToDto(ContactEvent contactEvent) {
        ContactEventDto ceDto = modelMapper.map(contactEvent, ContactEventDto.class);
        WebMvcLinkBuilder selfLinkBuider = linkTo(methodOn(this.getClass()).getContactEvent(contactEvent.getId()));
        ceDto.add(selfLinkBuider.withSelfRel());
        ceDto.add(selfLinkBuider.withRel("contactEvent"));
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
