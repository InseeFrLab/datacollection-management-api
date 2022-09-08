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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Autowired
    private ModelMapper modelMapper;

    @Operation(summary = "Search for addresses, paginated")
    @GetMapping(value = "addresses", produces = "application/hal+json")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = AddressPage.class))))
    })
    public ResponseEntity<?> findAddresss(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "20") Integer size,
        @RequestParam(defaultValue = "id") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        List<AddressDto> listAddresses = addressService.findAll(pageable).stream().map(a -> convertToDto(a)).collect(Collectors.toList());
        return new ResponseEntity<>(new AddressPage(listAddresses, pageable, listAddresses.size()), HttpStatus.OK);
    }

    @Operation(summary = "Search for a contact address by the contact identifier")
    @GetMapping(value = "contacts/{id}/address", produces = "application/hal+json")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ContactDto.class)))),
        @ApiResponse(responseCode = "404", description = "Not found"), @ApiResponse(responseCode = "500", description = "Internal servor error")
    })
    public ResponseEntity<?> getContactAddress(@PathVariable("id") String identifier) {
        try {
            Contact contact = contactService.findByIdentifier(identifier);
            return new ResponseEntity<>(convertToDto(contact.getAddress()), HttpStatus.OK);
        }
        catch (NoSuchElementException e) {
            return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<String>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Operation(summary = "Search for an address by its id")
    @GetMapping(value = "addresses/{id}", produces = "application/hal+json")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ContactDto.class)))),
        @ApiResponse(responseCode = "404", description = "Not found"), @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    public ResponseEntity<?> getAddress(@PathVariable("id") Long id) {
        try {
            Address address = addressService.findById(id);
            return new ResponseEntity<>(convertToDto(address), HttpStatus.OK);
        }
        catch (NoSuchElementException e) {
            return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<String>("Error", HttpStatus.BAD_REQUEST);
        }

    }

    @Operation(summary = "Update or create an address")
    @PutMapping(value = "addresses/{id}")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ContactDto.class)))),
        @ApiResponse(
            responseCode = "201",
            description = "Created",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ContactDto.class)))),
        @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> putAddress(@PathVariable("id") Long id, @RequestBody AddressDto addressDto) {
        if (addressDto.getId() == null || !addressDto.getId().equals(id)) {
            return new ResponseEntity<>("id and address identifier don't match", HttpStatus.BAD_REQUEST);
        }
        Address address;
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(HttpHeaders.LOCATION,
            ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(addressDto.getId()).toUriString());
        try {
            addressService.findById(id);
        }
        catch (NoSuchElementException e) {
            LOGGER.info("Creating address with the identifier {}, ", addressDto.getId());
            address = convertToEntity(addressDto);
            Address addressUpdate = addressService.updateAddress(address);
            return ResponseEntity.status(HttpStatus.CREATED).headers(responseHeaders).body(convertToDto(addressUpdate));
        }

        address = convertToEntity(addressDto);
        Address addressUpdate = addressService.updateAddress(address);
        return ResponseEntity.ok().headers(responseHeaders).body(convertToDto(addressUpdate));
    }

    @Operation(summary = "Delete an address by its id")
    @DeleteMapping(value = "addresses/{id}")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "No Content"), @ApiResponse(responseCode = "404", description = "Not found"),
        @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    public ResponseEntity<?> deleteContact(@PathVariable("id") Long id) {
        try {
            addressService.deleteAddressById(id);
            return new ResponseEntity<>("Address deleted", HttpStatus.NO_CONTENT);
        }
        catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>("Address not found", HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<String>("Error", HttpStatus.BAD_REQUEST);
        }
    }

    private AddressDto convertToDto(Address address) {
        AddressDto adressDto = modelMapper.map(address, AddressDto.class);
        WebMvcLinkBuilder selfLinkBuider = linkTo(methodOn(this.getClass()).getAddress(address.getId()));
        adressDto.add(selfLinkBuider.withSelfRel());
        adressDto.add(selfLinkBuider.withRel("address"));

        return adressDto;
    }

    private Address convertToEntity(AddressDto addressDto) {
        Address address = modelMapper.map(addressDto, Address.class);
        return address;
    }

    class AddressPage extends PageImpl<AddressDto> {

        private static final long serialVersionUID = -5570255373624396569L;

        public AddressPage(List<AddressDto> content, Pageable pageable, long total) {
            super(content, pageable, total);
        }
    }

}
