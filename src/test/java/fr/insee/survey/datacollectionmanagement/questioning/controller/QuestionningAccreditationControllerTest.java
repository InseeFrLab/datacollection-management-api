package fr.insee.survey.datacollectionmanagement.questioning.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;
import java.util.stream.Collectors;

import org.json.JSONArray;
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
import fr.insee.survey.datacollectionmanagement.questioning.domain.Questioning;
import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningAccreditation;
import fr.insee.survey.datacollectionmanagement.questioning.service.QuestioningService;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class QuestionningAccreditationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private QuestioningService questioningService;

    @Test
    public void getQuestioningAccreditationOk() throws Exception {
        String identifier = "84";
        String json = createJsonQuestioningAcreditation(identifier);
        this.mockMvc.perform(get("/api/questionings/" + identifier
                + "/questioning-accreditations")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().json(json, false));
    }

    @Test
    public void getQuestioningAccreditationNotFound() throws Exception {
        String identifier = "300";
        this.mockMvc.perform(get("/api/questionings/" + identifier + "/questioning-accreditations")).andDo(print())
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));

    }

    @Test
    public void postAccreditationQuestioningNotFound() throws Exception {
        int idQuestioning = 1000;
        String idContact = "CONT1";

        // create contact - status created
        QuestioningAccreditation accreditation = initAccreditation(idContact);
        String jsonAccreditation = createJson(accreditation);
        mockMvc.perform(
                post(Constants.API_QUESTIONINGS + "/" + idQuestioning + "/questioning-accreditations")
                        .content(jsonAccreditation).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void postAccreditationContactNotFound() throws Exception {
        int idQuestioning = 12;
        String idContact = "CONT7500";

        // create contact - status created
        QuestioningAccreditation accreditation = initAccreditation(idContact);
        String jsonAccreditation = createJson(accreditation);
        mockMvc.perform(
                post(Constants.API_QUESTIONINGS + "/" + idQuestioning + "/questioning-accreditations")
                        .content(jsonAccreditation).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void postAccreditationCreateUpdate() throws Exception {
        int idQuestioning = 12;
        String idContact = "CONT5";

        // create accreditation - status created
        QuestioningAccreditation accreditation = initAccreditation(idContact);
        String jsonAccreditation = createJson(accreditation);
        mockMvc.perform(
                post(Constants.API_QUESTIONINGS + "/" + idQuestioning + "/questioning-accreditations")
                        .content(jsonAccreditation).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(jsonAccreditation.toString(), false));
        Questioning questioning = questioningService.findbyId((long) idQuestioning);
        Set<QuestioningAccreditation> setAccreditationFound = questioning.getQuestioningAccreditations();
        QuestioningAccreditation accreditationFound = setAccreditationFound.stream()
                .filter(acc -> acc.getIdContact().equals(idContact))
                .collect(Collectors.toList()).get(0);
        assertEquals(accreditationFound.getCreationAuthor(), accreditation.getCreationAuthor());
        assertEquals(accreditationFound.getIdContact(), accreditation.getIdContact());

        // update accreditation - status ok
        accreditation.setMain(true);
        String jsonAccreditationUpdate = createJson(accreditation);
        mockMvc.perform(
                post(Constants.API_QUESTIONINGS +"/"+ idQuestioning + "/questioning-accreditations")
                        .content(jsonAccreditationUpdate).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonAccreditationUpdate.toString(), false));

        QuestioningAccreditation accreditationFoundAfterUpdate = questioningService.findbyId((long) idQuestioning)
                .getQuestioningAccreditations().stream().filter(acc -> acc.getIdContact().equals(idContact))
                .collect(Collectors.toList()).get(0);
        assertEquals(true, accreditationFoundAfterUpdate.isMain());

    }

    private QuestioningAccreditation initAccreditation(String idContact) {
        QuestioningAccreditation questionnAccreditation = new QuestioningAccreditation();
        questionnAccreditation.setIdContact(idContact);
        questionnAccreditation.setMain(false);
        questionnAccreditation.setCreationAuthor("ME");
        return questionnAccreditation;
    }

    private String createJson(QuestioningAccreditation accreditation) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("creationAuthor", accreditation.getCreationAuthor());
        jo.put("idContact", accreditation.getIdContact());
        jo.put("main", accreditation.isMain());
        return jo.toString();
    }

    private String createJsonQuestioningAcreditation(String identifier) throws JSONException {
        JSONObject jo1 = new JSONObject();
        jo1.put("idContact", "CONT1");

        JSONObject jo2 = new JSONObject();
        jo2.put("idContact", "CONT2");

        JSONObject jo3 = new JSONObject();
        jo3.put("idContact", "CONT3");

        JSONObject jo4 = new JSONObject();
        jo4.put("idContact", "CONT4");

        JSONArray ja = new JSONArray();
        ja.put(jo1);
        ja.put(jo2);
        ja.put(jo3);
        ja.put(jo4);

        return ja.toString();
    }

}
