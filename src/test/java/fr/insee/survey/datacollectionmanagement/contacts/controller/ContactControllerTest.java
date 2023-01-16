package fr.insee.survey.datacollectionmanagement.contacts.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

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

import fr.insee.survey.datacollectionmanagement.constants.Constants;
import fr.insee.survey.datacollectionmanagement.contact.domain.Address;
import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;
import fr.insee.survey.datacollectionmanagement.contact.domain.Contact.Gender;
import fr.insee.survey.datacollectionmanagement.contact.domain.ContactEvent;
import fr.insee.survey.datacollectionmanagement.contact.domain.ContactEvent.ContactEventType;
import fr.insee.survey.datacollectionmanagement.contact.repository.ContactRepository;
import fr.insee.survey.datacollectionmanagement.contact.service.ContactEventService;
import fr.insee.survey.datacollectionmanagement.contact.service.ContactService;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContactService contactService;

    @Autowired
    private ContactEventService contactEventService;

    @Autowired
    private ContactRepository contactRepository;

    @Test
    public void getContactOk() throws Exception {
        String identifier = "CONT1";
        Contact contact = contactService.findByIdentifier(identifier).get();
        String json = createJson(contact);
        this.mockMvc.perform(get(Constants.API_CONTACTS_ID, identifier)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().json(json, false));
    }

    @Test
    public void getContactNotFound() throws Exception {
        String identifier = "CONT500";
        this.mockMvc.perform(get(Constants.API_CONTACTS_ID, identifier)).andDo(print())
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));

    }

    @Test
    public void getContactsOk() throws Exception {
        JSONObject jo = new JSONObject();
        jo.put("totalElements", contactRepository.count());
        jo.put("numberOfElements", contactRepository.count());

        this.mockMvc.perform(get(Constants.API_CONTACTS_ALL)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().json(jo.toString(), false));
    }

    @Test
    public void putContactCreateUpdateDelete() throws Exception {
        String identifier = "TESTPUT";

        // create contact - status created
        Contact contact = initContact(identifier);
        String jsonContact = createJson(contact);
        mockMvc.perform(
                put(Constants.API_CONTACTS_ID, identifier).content(jsonContact).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(jsonContact.toString(), false));
        Contact contactFound = contactService.findByIdentifier(identifier).get();
        assertEquals(contact.getLastName(), contactFound.getLastName());
        assertEquals(contact.getFirstName(), contactFound.getFirstName());
        assertEquals(contact.getEmail(), contactFound.getEmail());
        List<ContactEvent> list = new ArrayList<>(contactEventService.findContactEventsByContact(contactFound));
        // List<ContactEvent> list = new ArrayList<>(contactFound.getContactEvents());
        assertEquals(list.size(), 1);
        assertEquals(list.get(0).getType(), ContactEventType.create);

        // update contact - status ok
        contact.setLastName("lastNameUpdate");
        String jsonContactUpdate = createJson(contact);
        mockMvc.perform(put(Constants.API_CONTACTS_ID, identifier).content(jsonContactUpdate)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().json(jsonContactUpdate.toString(), false));
        Contact contactFoundAfterUpdate = contactService.findByIdentifier(identifier).get();
        assertEquals("lastNameUpdate", contactFoundAfterUpdate.getLastName());
        assertEquals(contact.getFirstName(), contactFoundAfterUpdate.getFirstName());
        assertEquals(contact.getEmail(), contactFoundAfterUpdate.getEmail());
        List<ContactEvent> listUpdate = new ArrayList<>(
                contactEventService.findContactEventsByContact(contactFoundAfterUpdate));
        assertEquals(listUpdate.size(), 2);
        assertEquals(listUpdate.get(1).getType(), ContactEventType.update);

        // delete contact
        mockMvc.perform(delete(Constants.API_CONTACTS_ID, identifier).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        assertFalse(contactService.findByIdentifier(identifier).isPresent());
        assertTrue(contactEventService.findContactEventsByContact(contactFoundAfterUpdate).isEmpty());

        // delete contact not found
        mockMvc.perform(delete("/contacts/" + identifier).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    public void putContactAddressCreateUpdateDelete() throws Exception {
        String identifier = "TESTADDRESS";

        // create contact - status created
        Contact contact = initContactAddress(identifier);
        String jsonContact = createJsonContactAddress(contact);
        mockMvc.perform(
                put(Constants.API_CONTACTS_ID, identifier).content(jsonContact).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(jsonContact.toString(), false));
        Contact countactFound = contactService.findByIdentifier(identifier).get();
        assertEquals(contact.getAddress().getCityName(), countactFound.getAddress().getCityName());

        // update contact - status ok
        String newCityName = "cityUpdate";
        contact.getAddress().setCityName(newCityName);
        String jsonContactUpdate = createJsonContactAddress(contact);
        mockMvc.perform(put(Constants.API_CONTACTS_ID, identifier).content(jsonContactUpdate)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().json(jsonContactUpdate.toString(), false));
        Contact countactFoundAfterUpdate = contactService.findByIdentifier(identifier).get();
        assertEquals(contact.getAddress().getCityName(), countactFoundAfterUpdate.getAddress().getCityName());

        // delete contact
        mockMvc.perform(delete(Constants.API_CONTACTS_ID, identifier).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        assertFalse(contactService.findByIdentifier(identifier).isPresent());

    }

    @Test
    public void putContactsErrorId() throws Exception {
        String identifier = "NEWONE";
        String otherIdentifier = "WRONG";
        Contact contact = initContact(identifier);
        String jsonContact = createJson(contact);
        mockMvc.perform(put(Constants.API_CONTACTS_ID, otherIdentifier).content(jsonContact)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("id and contact identifier don't match"));

    }

    private Contact initContact(String identifier) {
        Contact contactMock = new Contact();
        contactMock.setIdentifier(identifier);
        contactMock.setEmail("test@insee.fr");
        contactMock.setFirstName("firstName" + identifier);
        contactMock.setLastName("lastName" + identifier);
        contactMock.setGender(Contact.Gender.Male);

        return contactMock;
    }

    private Contact initContactAddress(String identifier) {
        Contact contact = initContact(identifier);
        Address address = initAddress(identifier);
        contact.setAddress(address);
        return contact;
    }

    private Address initAddress(String identifier) {
        Address address = new Address();
        address.setCityName("city " + identifier);
        address.setCountryName("country " + identifier);
        address.setStreetName("steet " + identifier);
        address.setStreetNumber(identifier);
        return address;
    }

    private String createJson(Contact contact) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("identifier", contact.getIdentifier());
        jo.put("lastName", contact.getLastName());
        jo.put("firstName", contact.getFirstName());
        jo.put("email", contact.getEmail());
        jo.put("civility", contact.getGender().equals(Gender.Male) ? "Mr" : "Mme");
        return jo.toString();
    }

    private String createJsonContactAddress(Contact contact) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("identifier", contact.getIdentifier());
        jo.put("lastName", contact.getLastName());
        jo.put("firstName", contact.getFirstName());
        jo.put("email", contact.getEmail());
        jo.put("civility", contact.getGender().equals(Gender.Male) ? "Mr" : "Mme");
        jo.put("address", createJsonAddress(contact));
        return jo.toString();

    }

    private JSONObject createJsonAddress(Contact contact) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("cityName", contact.getAddress().getCityName());
        jo.put("streetName", contact.getAddress().getStreetName());
        jo.put("countryName", contact.getAddress().getCountryName());
        return jo;
    }

}
