package fr.insee.survey.datacollectionmanagement.contacts.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.NoSuchElementException;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import fr.insee.survey.datacollectionmanagement.contact.domain.Address;
import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;
import fr.insee.survey.datacollectionmanagement.contact.service.ContactService;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private ContactService contactService;

    @Test
    public void getAddressOk() throws Exception {
        String identifier = "IDEC";
        Contact contactMock = initContactAsddressMock(identifier);
        String jsonMock = createJsonAddress(contactMock);
        when(contactService.findByIdentifier(identifier)).thenReturn(contactMock);
        this.mockMvc.perform(get("/contacts/IDEC/address")).andDo(print()).andExpect(status().isOk()).andExpect(content().json(jsonMock, false));
    }

    @Test
    public void getAddressContacttNotFound() throws Exception {
        String identifier = "IDEC";
        when(contactService.findByIdentifier(identifier)).thenThrow(NoSuchElementException.class);
        this.mockMvc.perform(get("/contacts/IDEC/address")).andDo(print()).andExpect(status().is(HttpStatus.NOT_FOUND.value()));

    }
    
    //
    // @Test
    // public void getContactError() throws Exception {
    // String identifier = "IDEC";
    // when(contactService.findByIdentifier(identifier)).thenThrow(NoSuchElementException.class);
    // this.mockMvc.perform(get("/contacts//%nzeuihf")).andDo(print()).andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    // }
    //
    // @Test
    // public void getContactsOk() throws Exception {
    // List<Contact> listC = initListContactMock();
    // Pageable pageable = PageRequest.of(0, 20, Sort.by("identifier"));
    // Page<Contact> page = new PageImpl<>(listC);
    // JSONObject jo = new JSONObject();
    // jo.put("totalElements", listC.size());
    // jo.put("numberOfElements", listC.size());
    //
    // when(contactService.findAll(pageable)).thenReturn(page);
    // this.mockMvc.perform(get(Constants.API_CONTACTS)).andDo(print()).andExpect(status().isOk()).andExpect(content().json(jo.toString(), false));
    // }
    //
    // @Test
    // public void putContactsCreate() throws Exception {
    // String jsonContact = createJson(initContactMock("TESTPUT"));
    //
    // when(contactService.findByIdentifier("TESTPUT")).thenThrow(NoSuchElementException.class);
    // mockMvc.perform(put("/contacts/TESTPUT").content(jsonContact).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
    // .andExpect(content().json(jsonContact.toString(), false));
    //
    // }
    //
    // @Test
    // public void putContactsUpdate() throws Exception {
    // Contact contact = initContactMock("TESTPUT");
    // String jsonContact = createJson(contact);
    //
    // when(contactService.findByIdentifier("TESTPUT")).thenReturn(contact);
    // mockMvc.perform(put("/contacts/TESTPUT").content(jsonContact).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
    // .andExpect(content().json(jsonContact.toString(), false));;
    //
    // }
    //
    // @Test
    // public void putContactsErrorId() throws Exception {
    // Contact contact = initContactMock("WRONG");
    // String jsonContact = createJson(contact);
    //
    // when(contactService.findByIdentifier("TESTPUT")).thenReturn(contact);
    // mockMvc.perform(put("/contacts/TESTPUT").content(jsonContact).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
    // .andExpect(content().string("id and contact identifier don't match"));
    //
    // }

    private Contact initContactAsddressMock(String identifier) {
        Contact contactMock = new Contact();
        contactMock.setIdentifier(identifier);
        Address address = new Address();
        address.setCity("city " + identifier);
        address.setStreetName("street name " + identifier);
        address.setStreetNumber(Integer.toString(identifier.length()));
        address.setCountryName("country " + identifier);
        contactMock.setAddress(address);
        return contactMock;
    }

    private String createJsonAddress(Contact contactMock) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("city", contactMock.getAddress().getCity());
        jo.put("streetName", contactMock.getAddress().getStreetName());
        jo.put("countryName", contactMock.getAddress().getCountryName());
        return jo.toString();
    }

}
