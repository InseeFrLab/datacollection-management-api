package fr.insee.survey.datacollectionmanagement.contact.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.text.ParseException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;
import fr.insee.survey.datacollectionmanagement.contact.domain.Contact.Gender;
import fr.insee.survey.datacollectionmanagement.contact.dto.ContactDto;
import fr.insee.survey.datacollectionmanagement.contact.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@CrossOrigin
public class ContactController {

    static final Logger LOGGER = LoggerFactory.getLogger(ContactController.class);

    @Autowired
    private ContactService contactService;

    @Autowired
    private ModelMapper modelMapper;

    @Operation(summary = "Search for contacts, paginated")
    @GetMapping(value = "contacts", produces = "application/hal+json")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ContactPage.class))))
    })
    public ResponseEntity<?> getContacts(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "20") Integer size,
        @RequestParam(defaultValue = "identifier") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        List<ContactDto> listC = contactService.findAll(pageable).stream().map(c -> convertToDto(c)).collect(Collectors.toList());
        return new ResponseEntity<>(new ContactPage(listC, pageable, listC.size()), HttpStatus.OK);
    }

    @Operation(summary = "Search for a contact by its identifier")
    @GetMapping(value = "contacts/{id}", produces = "application/hal+json")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ContactDto.class)))),
        @ApiResponse(responseCode = "404", description = "Not found"), @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    public ResponseEntity<?> getContact(@PathVariable("id") String id) {
        Contact contact = null;
        try {
            contact = contactService.findByIdentifier(StringUtils.upperCase(id));
            return new ResponseEntity<>(convertToDto(contact), HttpStatus.OK);
        }
        catch (NoSuchElementException e) {
            return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<String>("Error", HttpStatus.BAD_REQUEST);
        }

    }

    @Operation(summary = "Update or create a contact")
    @PutMapping(value = "contacts/{id}")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ContactDto.class)))),
        @ApiResponse(
            responseCode = "201",
            description = "Created",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ContactDto.class)))),
        @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> updateContact(@PathVariable("id") String id, @RequestBody ContactDto contactDto) {
        if (StringUtils.isBlank(contactDto.getIdentifier()) || !contactDto.getIdentifier().equalsIgnoreCase(id)) {
            return new ResponseEntity<>("id and contact identifier don't match", HttpStatus.BAD_REQUEST);
        }
        Contact contact;
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(HttpHeaders.LOCATION,
            ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(contactDto.getIdentifier()).toUriString());

        try {
            contact = convertToEntity(contactDto);
        }
        catch (ParseException e) {
            return new ResponseEntity<>("Impossible to parse contact", HttpStatus.BAD_REQUEST);
        }
        catch (NoSuchElementException e) {
            LOGGER.info("Creating contact with the identifier {}, ", contactDto.getIdentifier());
            contact = convertToEntityNewContact(contactDto);
            Contact contactUpdate = contactService.createContact(contact);
            return ResponseEntity.status(HttpStatus.CREATED).headers(responseHeaders).body(convertToDto(contactUpdate));

        }
        Contact contactUpdate = contactService.updateExistingContact(contact);
        return ResponseEntity.ok().headers(responseHeaders).body(convertToDto(contactUpdate));
    }

    @Operation(summary = "Delete a contact (including its address and its assiociated ContactEvents)")
    @DeleteMapping(value = "contacts/{id}")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "No Content"), @ApiResponse(responseCode = "404", description = "Not found"),
        @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    public ResponseEntity<?> deleteContact(@PathVariable("id") String id) {
        try {
            contactService.deleteContact(id);
            return new ResponseEntity<>("Contact deleted", HttpStatus.NO_CONTENT);
        }
        catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>("Contact not found", HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<String>("Error", HttpStatus.BAD_REQUEST);
        }
    }

    private ContactDto convertToDto(Contact contact) {
        ContactDto contactDto = modelMapper.map(contact, ContactDto.class);
        String civility = contact.getGender().equals(Gender.Male) ? "Mr" : "Mme";
        contactDto.setCivility(civility);
        WebMvcLinkBuilder selfLinkBuider = linkTo(methodOn(this.getClass()).getContact(contact.getIdentifier()));
        contactDto.add(selfLinkBuider.withSelfRel());
        contactDto.add(selfLinkBuider.withRel("contact"));
        Link linkAddress = linkTo(methodOn(AddressController.class).getContactAddress(contact.getIdentifier())).withRel("address");
        contactDto.add(linkAddress);
        Link linkContactEvents = linkTo(methodOn(ContactEventController.class).getContactContactEvent(contact.getIdentifier())).withRel("contactEvents");
        contactDto.add(linkContactEvents);

        return contactDto;
    }

    private Contact convertToEntity(ContactDto contactDto) throws ParseException {
        Contact contact = modelMapper.map(contactDto, Contact.class);
        contact.setGender(contactDto.getCivility().equals("Mr") ? Gender.Male : Gender.Female);

        Contact oldContact = contactService.findByIdentifier(contactDto.getIdentifier());
        contact.setComment(oldContact.getComment());
        contact.setAddress(oldContact.getAddress());
        contact.setContactEvents(oldContact.getContactEvents());

        return contact;
    }

    private Contact convertToEntityNewContact(ContactDto contactDto) {
        Contact contact = modelMapper.map(contactDto, Contact.class);
        contact.setGender(contactDto.getCivility().equals("Mr") ? Gender.Male : Gender.Female);

        return contact;
    }

    class ContactPage extends PageImpl<ContactDto> {

        private static final long serialVersionUID = 656181199902518234L;

        public ContactPage(List<ContactDto> content, Pageable pageable, long total) {
            super(content, pageable, total);
        }
    }

}
