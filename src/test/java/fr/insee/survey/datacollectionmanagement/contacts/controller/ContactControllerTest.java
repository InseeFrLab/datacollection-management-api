package fr.insee.survey.datacollectionmanagement.contacts.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import fr.insee.survey.datacollectionmanagement.constants.Constants;
import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;
import fr.insee.survey.datacollectionmanagement.contact.service.ContactService;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContactService contactService;

    @Test
    public void getContactOk() throws Exception {
        String identifier = "IDEC";
        Contact contactMock = initContactMock(identifier);
        String jsonMock = createJson(contactMock);
        when(contactService.findByIdentifier(identifier)).thenReturn(contactMock);
        this.mockMvc.perform(get("/contacts/IDEC")).andDo(print()).andExpect(status().isOk()).andExpect(content().json(jsonMock, false));
    }

    @Test
    public void getContactNotFound() throws Exception {
        String identifier = "IDEC";
        when(contactService.findByIdentifier(identifier)).thenThrow(NoSuchElementException.class);
        this.mockMvc.perform(get("/contacts/IDEC")).andDo(print()).andExpect(status().is(HttpStatus.NOT_FOUND.value()));

    }

    @Test
    public void getContactError() throws Exception {
        String identifier = "IDEC";
        when(contactService.findByIdentifier(identifier)).thenThrow(NoSuchElementException.class);
        this.mockMvc.perform(get("/contacts//%nzeuihf")).andDo(print()).andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void getContactsOk() throws Exception {
        List<Contact> listC = initListContactMock();
        Pageable pageable = PageRequest.of(0, 20, Sort.by("identifier"));
        Page<Contact> page = new PageImpl<>(listC);
        JSONObject jo = new JSONObject();
        jo.put("totalElements", listC.size());
        jo.put("numberOfElements", listC.size());

        when(contactService.findAll(pageable)).thenReturn(page);
        this.mockMvc.perform(get(Constants.API_CONTACTS)).andDo(print()).andExpect(status().isOk()).andExpect(content().json(jo.toString(), false));
    }

    @Test
    public void putContactsCreate() throws Exception {
        String jsonContact = createJson(initContactMock("TESTPUT"));

        when(contactService.findByIdentifier("TESTPUT")).thenThrow(NoSuchElementException.class);
        mockMvc.perform(put("/contacts/TESTPUT").content(jsonContact).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
            .andExpect(content().json(jsonContact.toString(), false));

    }

    @Test
    public void putContactsUpdate() throws Exception {
        Contact contact = initContactMock("TESTPUT");
        String jsonContact = createJson(contact);

        when(contactService.findByIdentifier("TESTPUT")).thenReturn(contact);
        mockMvc.perform(put("/contacts/TESTPUT").content(jsonContact).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
            .andExpect(content().json(jsonContact.toString(), false));;

    }

    @Test
    public void putContactsErrorId() throws Exception {
        Contact contact = initContactMock("WRONG");
        String jsonContact = createJson(contact);

        when(contactService.findByIdentifier("TESTPUT")).thenReturn(contact);
        mockMvc.perform(put("/contacts/TESTPUT").content(jsonContact).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
            .andExpect(content().string("id and contact identifier don't match"));

    }

    private Contact initContactMock(String identifier) {
        Contact contactMock = new Contact();
        contactMock.setIdentifier(identifier);
        contactMock.setEmail("test@insee.fr");
        contactMock.setFirstName("firstName" + identifier);
        contactMock.setLastName("lastName" + identifier);
        contactMock.setGender(Contact.Gender.Male);

        return contactMock;
    }

    private String createJson(Contact contactMock) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("identifier", contactMock.getIdentifier());
        jo.put("lastName", contactMock.getLastName());
        jo.put("civility", "Mr");
        return jo.toString();
    }

    private List<Contact> initListContactMock() {

        List<Contact> listContact = new ArrayList<>();
        listContact.add(initContactMock("TEST1"));
        listContact.add(initContactMock("TEST2"));
        listContact.add(initContactMock("TEST3"));
        return listContact;
    }

}
