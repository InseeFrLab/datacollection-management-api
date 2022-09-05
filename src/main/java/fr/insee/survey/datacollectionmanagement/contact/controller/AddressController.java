package fr.insee.survey.datacollectionmanagement.contact.controller;

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

import fr.insee.survey.datacollectionmanagement.contact.domain.Address;
import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;
import fr.insee.survey.datacollectionmanagement.contact.service.AddressService;
import fr.insee.survey.datacollectionmanagement.contact.service.ContactService;

@RestController
@CrossOrigin
public class AddressController {

    static final Logger LOGGER = LoggerFactory.getLogger(AddressController.class);

    @Autowired
    private AddressService addressService;

    @Autowired
    private ContactService contactService;

    @GetMapping(value = "addresses")
    public Page<Address> findAddresss(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "20") Integer size,
        @RequestParam(defaultValue = "id") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return addressService.findAll(pageable);
    }

    @GetMapping(value = "contacts/{id}/address")
    public ResponseEntity<?> getContactAddress(@PathVariable("id") String identifier) {
        try {
            Contact contact = contactService.findByIdentifier(identifier);
            return new ResponseEntity<>(contact.getAddress(), HttpStatus.OK);
        }
        catch (NoSuchElementException e) {
            return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<String>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping(value = "addresses/{id}")
    public ResponseEntity<?> getAddress(@PathVariable("id") Long id) {
        try {
            Address address = addressService.findById(id);
            return new ResponseEntity<>(address, HttpStatus.OK);
        }
        catch (NoSuchElementException e) {
            return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<String>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping(value = "addresses/{id}")
    public ResponseEntity<?> putAddress(@PathVariable("id") Long id, @RequestBody Address address) {
        if ( !address.getId().equals(id)) {
            return new ResponseEntity<>("id and address identifier don't match", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(addressService.updateAddress(address), HttpStatus.OK);
    }

    @PutMapping(value = "contacts/{id}/address")
    public ResponseEntity<?> putAddress(@PathVariable("id") String identifier, @RequestBody Address address) {
        Contact contact = contactService.findByIdentifier(identifier);
        if ( !address.getId().equals(contact.getAddress().getId())) {
            return new ResponseEntity<>("address and contact identifier don't match", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(addressService.updateAddress(address), HttpStatus.OK);
    }

    // @DeleteMapping(value = "address/{id}")
    // public ResponseEntity<?> deleteAddress(@PathVariable("id") String id) {
    // try {
    // addressService.deleteAddress(id);
    // return new ResponseEntity<>("Address deleted", HttpStatus.OK);
    // }
    // catch (EmptyResultDataAccessException e) {
    // return new ResponseEntity<>("Address not found", HttpStatus.NOT_FOUND);
    // }
    // catch (Exception e) {
    // return new ResponseEntity<String>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
    // }
    // }

}
