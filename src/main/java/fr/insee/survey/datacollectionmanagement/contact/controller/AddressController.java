package fr.insee.survey.datacollectionmanagement.contact.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import fr.insee.survey.datacollectionmanagement.contact.domain.Address;
import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;
import fr.insee.survey.datacollectionmanagement.contact.dto.AddressDto;
import fr.insee.survey.datacollectionmanagement.contact.dto.ContactDto;
import fr.insee.survey.datacollectionmanagement.contact.service.AddressService;
import fr.insee.survey.datacollectionmanagement.contact.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@CrossOrigin
public class AddressController {

    static final Logger LOGGER = LoggerFactory.getLogger(AddressController.class);

    @Autowired
    private AddressService addressService;

    @Autowired
    private ContactService contactService;

    @Operation(summary = "Search for a contact address by the contact identifier")
    @GetMapping(value = "contacts/{identifier}/address", produces = "application/hal+json")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = AddressDto.class)))),
        @ApiResponse(responseCode = "404", description = "Not found"), @ApiResponse(responseCode = "500", description = "Internal servor error")
    })
    public ResponseEntity<?> getContactAddress(@PathVariable("identifier") String identifier) {
        try {
            Contact contact = contactService.findByIdentifier(identifier);
            if (contact.getAddress() != null)
                return new ResponseEntity<>(addressService.convertToDto(contact.getAddress()), HttpStatus.OK);
            else {
                return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
            }
        }
        catch (NoSuchElementException e) {
            return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<String>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Operation(summary = "Update or create an address by the contact identifier")
    @PutMapping(value = "contacts/{identifier}/address")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = AddressDto.class)))),
        @ApiResponse(
            responseCode = "201",
            description = "Created",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ContactDto.class)))),
        @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<?> putAddress(@PathVariable("identifier") String identifier, @RequestBody AddressDto addressDto) {
        try {
            Contact contact = contactService.findByIdentifier(identifier);

            Address address = addressService.convertToEntity(addressDto);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set(HttpHeaders.LOCATION, ServletUriComponentsBuilder.fromCurrentRequest().toUriString());

            if (contact.getAddress() != null) {
                address.setId(contact.getAddress().getId());
                Address addressUpdate = addressService.updateAddress(address);
                return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(addressService.convertToDto(addressUpdate));
            }
            else {
                LOGGER.info("Creating address for the contact {} ", identifier);
                Address addressUpdate = addressService.updateAddress(address);
                contact.setAddress(addressUpdate);
                contactService.updateOrCreateContact(contact);
                return ResponseEntity.status(HttpStatus.CREATED).headers(responseHeaders).body(addressService.convertToDto(addressUpdate));
            }
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Contact does not exist");
        }

    }

    class AddressPage extends PageImpl<AddressDto> {

        private static final long serialVersionUID = -5570255373624396569L;

        public AddressPage(List<AddressDto> content, Pageable pageable, long total) {
            super(content, pageable, total);
        }
    }

}
