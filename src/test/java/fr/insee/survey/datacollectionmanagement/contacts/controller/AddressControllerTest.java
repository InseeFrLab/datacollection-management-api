package fr.insee.survey.datacollectionmanagement.contacts.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import fr.insee.survey.datacollectionmanagement.contact.domain.Address;
import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;
import fr.insee.survey.datacollectionmanagement.contact.service.AddressService;
import fr.insee.survey.datacollectionmanagement.contact.service.ContactService;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContactService contactService;
    
    @Autowired
    private AddressService addressService;

    @Test
    public void getAddressOk() throws Exception {
        String identifier = "CONT1";
        Contact contact = contactService.findByIdentifier(identifier);
        String json = createJsonAddress(contact);
        this.mockMvc.perform(get("/contacts/" + identifier + "/address")).andDo(print()).andExpect(status().isOk()).andExpect(content().json(json, false));
    }

    @Test
    public void getAddressContacttNotFound() throws Exception {
        String identifier = "CONT500";
        this.mockMvc.perform(get("/contacts/" + identifier + "/address")).andDo(print()).andExpect(status().is(HttpStatus.NOT_FOUND.value()));

    }

    @Test
    public void putAddressCreateUpdate() throws Exception {

        String identifier = "CONT5";
        Contact contact = contactService.findByIdentifier(identifier);
        Address addressBefore = contact.getAddress();

        // Before: delete existing address
        contact.setAddress(null);
        contact = contactService.saveContact(contact);
        addressService.deleteAddressById(addressBefore.getId());
        
        this.mockMvc.perform(get("/contacts/" + identifier + "/address")).andDo(print()).andExpect(status().is(HttpStatus.NOT_FOUND.value()));

        // Create address - status created
        Address addressCreated = initAddressMock(identifier);
        contact.setAddress(addressCreated);
        String jsonCreate = createJsonAddress(contact);
        this.mockMvc.perform(put("/contacts/" + identifier + "/address").content(jsonCreate).contentType(MediaType.APPLICATION_JSON)).andDo(print())
            .andExpect(status().isCreated()).andExpect(content().json(jsonCreate.toString(), false));
        Contact contactAfterCreate = contactService.findByIdentifier(identifier);
        assertEquals(contactAfterCreate.getAddress().getCity(), addressCreated.getCity());
        assertEquals(contactAfterCreate.getAddress().getStreetName(), addressCreated.getStreetName());
        assertEquals(contactAfterCreate.getAddress().getCountryName(), addressCreated.getCountryName());
        
        // Update address - status OK
        Address addressUpdated = initAddressMock("UPDATE");
        contact.setAddress(addressUpdated);
        String jsonUpdate = createJsonAddress(contact);
        this.mockMvc.perform(put("/contacts/" + identifier + "/address").content(jsonUpdate).contentType(MediaType.APPLICATION_JSON)).andDo(print())
            .andExpect(status().isOk()).andExpect(content().json(jsonUpdate.toString(), false));
        Contact contactAfterUpdate = contactService.findByIdentifier(identifier);
        assertEquals(contactAfterUpdate.getAddress().getCity(), addressUpdated.getCity());
        assertEquals(contactAfterUpdate.getAddress().getStreetName(), addressUpdated.getStreetName());
        assertEquals(contactAfterUpdate.getAddress().getCountryName(), addressUpdated.getCountryName());
        
        // back to before
        contact.setAddress(addressBefore);
        addressService.saveAddress(addressBefore);
        contactService.saveContact(contact);
        assertEquals(contact.getAddress().getCity(), addressBefore.getCity());
        assertEquals(contact.getAddress().getStreetName(), addressBefore.getStreetName());
        assertEquals(contact.getAddress().getCountryName(), addressBefore.getCountryName());
        
    }

    private Address initAddressMock(String identifier) {
        Address address = new Address();
        address.setCity("city " + identifier);
        address.setStreetName("street name " + identifier);
        address.setStreetNumber(Integer.toString(identifier.length()));
        address.setCountryName("country " + identifier);
        return address;
    }

    private String createJsonAddress(Contact contact) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("city", contact.getAddress().getCity());
        jo.put("streetName", contact.getAddress().getStreetName());
        jo.put("countryName", contact.getAddress().getCountryName());
        return jo.toString();
    }

}
