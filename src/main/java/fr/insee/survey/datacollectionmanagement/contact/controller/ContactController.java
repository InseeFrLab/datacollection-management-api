package fr.insee.survey.datacollectionmanagement.contact.controller;

import java.text.ParseException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import fr.insee.survey.datacollectionmanagement.constants.Constants;
import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;
import fr.insee.survey.datacollectionmanagement.contact.domain.Contact.Gender;
import fr.insee.survey.datacollectionmanagement.contact.dto.ContactDto;
import fr.insee.survey.datacollectionmanagement.contact.service.AddressService;
import fr.insee.survey.datacollectionmanagement.contact.service.ContactService;
import fr.insee.survey.datacollectionmanagement.questioning.service.QuestioningAccreditationService;
import fr.insee.survey.datacollectionmanagement.view.service.ViewService;
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
    private AddressService addressService;

    @Autowired
    private ViewService viewService;

    @Autowired
    private QuestioningAccreditationService questioningAccreditationService;

    @Autowired
    private ModelMapper modelMapper;

    @Operation(summary = "Search for contacts, paginated")
    @GetMapping(value = Constants.API_CONTACTS_ALL, produces = "application/json")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ContactPage.class))))
    })
    public ResponseEntity<?> getContacts(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "20") Integer size,
        @RequestParam(defaultValue = "identifier") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<Contact> pageC = contactService.findAll(pageable);
        List<ContactDto> listC = pageC.stream().map(c -> convertToDto(c)).collect(Collectors.toList());
        return ResponseEntity.ok().body(new ContactPage(listC, pageable,pageC.getTotalElements()));
    }

    @Operation(summary = "Search for a contact by its identifier")
    @GetMapping(value = Constants.API_CONTACTS_ID, produces = "application/json")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ContactDto.class))),
        @ApiResponse(responseCode = "404", description = "Not found"), @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    public ResponseEntity<?> getContact(@PathVariable("id") String id) {
        Contact contact = null;
        try {
            contact = contactService.findByIdentifier(StringUtils.upperCase(id));
            return ResponseEntity.ok().body(convertToDto(contact));
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Contact does not exist");
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error");
        }

    }

    @Operation(summary = "Update or create a contact")
    @PutMapping(value = Constants.API_CONTACTS_ID, produces = "application/json", consumes = "application/json")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ContactDto.class))),
        @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = ContactDto.class))),
        @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> putContact(@PathVariable("id") String id, @RequestBody ContactDto contactDto) {
        if (StringUtils.isBlank(contactDto.getIdentifier()) || !contactDto.getIdentifier().equalsIgnoreCase(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("id and contact identifier don't match");
        }
        Contact contact;
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(HttpHeaders.LOCATION, ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(contactDto.getIdentifier()).toUriString());

        try {
            contact = convertToEntity(contactDto);

        }
        catch (ParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Impossible to parse contact");
        }
        catch (NoSuchElementException e) {
            LOGGER.info("Creating contact with the identifier {}", contactDto.getIdentifier());
            contact = convertToEntityNewContact(contactDto);
            if (contactDto.getAddress() != null) contact.setAddress(addressService.convertToEntity(contactDto.getAddress()));
            Contact contactCreate = contactService.createContactAddressEvent(contact);
            viewService.createView(id, null, null);
            return ResponseEntity.status(HttpStatus.CREATED).headers(responseHeaders).body(convertToDto(contactCreate));

        }
        if (contactDto.getAddress() != null) contact.setAddress(addressService.convertToEntity(contactDto.getAddress()));
        Contact contactUpdate = contactService.updateContactAddressEvent(contact);
        return ResponseEntity.ok().headers(responseHeaders).body(convertToDto(contactUpdate));
    }

    @Operation(summary = "Delete a contact, its address, its contactEvents and its accreditations")
    @DeleteMapping(value = Constants.API_CONTACTS_ID)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "No Content"), @ApiResponse(responseCode = "404", description = "Not found"),
        @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @Transactional
    public ResponseEntity<?> deleteContact(@PathVariable("id") String id) {
        try {
            Contact contact = contactService.findByIdentifier(id);
            contactService.deleteContactAddressEvent(contact);
            
            viewService.findViewByIdentifier(id).stream().forEach(c-> viewService.deleteView(c));
            questioningAccreditationService.findByContactIdentifier(id).stream().forEach(c-> questioningAccreditationService.deleteAccreditation(c));

            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Contact deleted");
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Contact does not exist");
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error");
        }
    }

    private ContactDto convertToDto(Contact contact) {
        ContactDto contactDto = modelMapper.map(contact, ContactDto.class);
        String civility = contact.getGender().equals(Gender.Male) ? "Mr" : "Mme";
        contactDto.setCivility(civility);
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
