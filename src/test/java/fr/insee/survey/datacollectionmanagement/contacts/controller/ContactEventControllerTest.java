package fr.insee.survey.datacollectionmanagement.contacts.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class ContactEventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getContactEventOk() throws Exception {
        String identifier = "CONT1";
        this.mockMvc.perform(get("/contacts/" + identifier + "/contact-events")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void getContactEventNotFound() throws Exception {
        String identifier = "CONT500";
        this.mockMvc.perform(get("/contacts/" + identifier + "/contact-events")).andDo(print()).andExpect(status().is(HttpStatus.NOT_FOUND.value()));

    }

}
