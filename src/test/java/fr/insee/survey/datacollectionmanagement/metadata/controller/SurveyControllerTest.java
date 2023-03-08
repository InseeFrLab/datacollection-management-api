package fr.insee.survey.datacollectionmanagement.metadata.controller;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import fr.insee.survey.datacollectionmanagement.constants.Constants;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Survey;
import fr.insee.survey.datacollectionmanagement.metadata.repository.SurveyRepository;
import fr.insee.survey.datacollectionmanagement.metadata.service.SurveyService;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class SurveyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SurveyService surveyService;

    @Autowired
    private SurveyRepository surveyRepository;

    @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
    @Test
    public void getSurveyOk() throws Exception {
        String identifier = "SOURCE12022";
        Optional<Survey> survey = surveyService.findById(identifier);
        assertTrue(survey.isPresent());
        String json = createJson(survey.get(), "SOURCE1");
        this.mockMvc.perform(get(Constants.API_SURVEYS_ID, identifier)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().json(json, false));
    }

    @Test
    public void getSurveyNotFound() throws Exception {
        String identifier = "SURVEYNOTFOUND";
        this.mockMvc.perform(get(Constants.API_SURVEYS_ID, identifier)).andDo(print())
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));

    }


    @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
    @Test
    public void putSurveyCreateUpdateDelete() throws Exception {
        String identifier = "SURVEYPUT";

        // create survey - status created
        Survey survey = initSurvey(identifier);
        String jsonSurvey = createJson(survey, "SOURCE1");
        mockMvc.perform(
                put(Constants.API_SURVEYS_ID, identifier).content(jsonSurvey)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(jsonSurvey.toString(), false));
        Optional<Survey> surveyFound = surveyService.findById(identifier);
        assertTrue(surveyFound.isPresent());
        assertEquals(survey.getLongWording(), surveyFound.get().getLongWording());
        assertEquals(survey.getShortWording(), surveyFound.get().getShortWording());
        assertEquals(survey.getSampleSize(), surveyFound.get().getSampleSize());

        // update survey - status ok
        survey.setLongWording("Long wording update");
        String jsonSurveyUpdate = createJson(survey,"SOURCE1");
        mockMvc.perform(put(Constants.API_SURVEYS_ID, identifier).content(jsonSurveyUpdate)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().json(jsonSurveyUpdate.toString(), false));
        Optional<Survey> surveyFoundAfterUpdate = surveyService.findById(identifier);
        assertTrue(surveyFoundAfterUpdate.isPresent());
        assertEquals("Long wording update", surveyFoundAfterUpdate.get().getLongWording());
        assertEquals(survey.getId(), surveyFoundAfterUpdate.get().getId());

        // delete survey
        mockMvc.perform(delete(Constants.API_SURVEYS_ID, identifier).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        assertFalse(surveyService.findById(identifier).isPresent());

        // delete survey not found
        mockMvc.perform(delete(Constants.API_SURVEYS_ID, identifier).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    public void putSurveysErrorId() throws Exception {
        String identifier = "NEWONE";
        String otherIdentifier = "WRONG";
        Survey survey = initSurvey(identifier);
        String jsonSurvey = createJson(survey, "SOURCE1");
        mockMvc.perform(put(Constants.API_SURVEYS_ID, otherIdentifier).content(jsonSurvey)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andExpect(content().string("id and idSurvey don't match"));

    }

    private Survey initSurvey(String identifier) {
        Survey surveyMock = new Survey();
        surveyMock.setYear(2023);
        surveyMock.setId(identifier);
        surveyMock.setLongWording("Long wording about " + identifier);
        surveyMock.setShortWording("Short wording about " + identifier);
        surveyMock.setSampleSize(1000);
        return surveyMock;
    }

    private String createJson(Survey survey, String idSource) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("id", survey.getId());
        jo.put("year", survey.getYear());
        jo.put("sourceId", idSource);
        jo.put("longWording", survey.getLongWording());
        jo.put("shortWording", survey.getShortWording());
        jo.put("sampleSize", survey.getSampleSize());
        return jo.toString();
    }

}
