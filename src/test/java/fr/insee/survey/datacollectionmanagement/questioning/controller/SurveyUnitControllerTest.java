package fr.insee.survey.datacollectionmanagement.questioning.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import fr.insee.survey.datacollectionmanagement.constants.Constants;
import fr.insee.survey.datacollectionmanagement.questioning.domain.SurveyUnit;
import fr.insee.survey.datacollectionmanagement.questioning.domain.SurveyUnitAddress;
import fr.insee.survey.datacollectionmanagement.questioning.repository.SurveyUnitRepository;
import fr.insee.survey.datacollectionmanagement.questioning.service.SurveyUnitService;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class SurveyUnitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SurveyUnitService surveyUnitService;

    @Autowired
    private SurveyUnitRepository surveyUnitRepository;

    @Test
    public void getSurveyUnitOk() throws Exception {
        String identifier = "100000000";
        SurveyUnit surveyUnit = surveyUnitService.findbyId(identifier);
        String json = createJson(surveyUnit);
        this.mockMvc.perform(get(Constants.API_SURVEY_UNITS + identifier)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().json(json, false));
    }

    @Test
    public void getSurveyUnitNotFound() throws Exception {
        String identifier = "900000000";
        this.mockMvc.perform(get(Constants.API_SURVEY_UNITS + identifier)).andDo(print())
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));

    }

    @Test
    public void getSurveyUnitsOk() throws Exception {
        JSONObject jo = new JSONObject();
        jo.put("totalElements", surveyUnitRepository.count());
        jo.put("numberOfElements", surveyUnitRepository.count());

        this.mockMvc.perform(get(Constants.API_SURVEY_UNITS)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().json(jo.toString(), false));
    }

    @Test
    public void putSurveyUnitCreateUpdateDelete() throws Exception {
        String identifier = "TESTPUT";

        // create surveyUnit - status created
        SurveyUnit surveyUnit = initSurveyUnit(identifier);
        String jsonSurveyUnit = createJson(surveyUnit);
        mockMvc.perform(
                put(Constants.API_SURVEY_UNITS + identifier).content(jsonSurveyUnit)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(jsonSurveyUnit.toString(), false));
        SurveyUnit surveyUnitFound = surveyUnitService.findbyId(identifier);
        assertEquals(surveyUnit.getIdSu(), surveyUnitFound.getIdSu());
        assertEquals(surveyUnit.getIdentificationCode(), surveyUnitFound.getIdentificationCode());
        assertEquals(surveyUnit.getIdentificationName(), surveyUnitFound.getIdentificationName());

        // update surveyUnit - status ok
        surveyUnit.setIdentificationName("identificationNameUpdate");
        String jsonSurveyUnitUpdate = createJson(surveyUnit);
        mockMvc.perform(put(Constants.API_SURVEY_UNITS + identifier).content(jsonSurveyUnitUpdate)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().json(jsonSurveyUnitUpdate.toString(), false));
        SurveyUnit surveyUnitFoundAfterUpdate = surveyUnitService.findbyId(identifier);
        assertEquals("identificationNameUpdate", surveyUnitFoundAfterUpdate.getIdentificationName());
        assertEquals(surveyUnit.getIdSu(), surveyUnitFoundAfterUpdate.getIdSu());
        assertEquals(surveyUnit.getIdentificationName(), surveyUnitFoundAfterUpdate.getIdentificationName());

        // delete surveyUnit
        surveyUnitService.deleteSurveyUnit(identifier);
        assertThrows(NoSuchElementException.class, () -> {
            surveyUnitService.findbyId(identifier);
        });

    }

    @Test
    public void putSurveyUnitAddressCreateUpdateDelete() throws Exception {
        String identifier = "TESTADDRESS";

        // create surveyUnit - status created
        SurveyUnit surveyUnit = initSurveyUnitAddress(identifier);
        String jsonSurveyUnit = createJsonSurveyUnitAddress(surveyUnit);
        mockMvc.perform(
                put(Constants.API_SURVEY_UNITS + identifier).content(jsonSurveyUnit)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(jsonSurveyUnit.toString(), false));
        SurveyUnit suFound = surveyUnitService.findbyId(identifier);
        assertEquals(surveyUnit.getSurveyUnitAddress().getCity(), suFound.getSurveyUnitAddress().getCity());

        // update surveyUnit - status ok
        String newCityName = "cityUpdate";
        surveyUnit.getSurveyUnitAddress().setCity(newCityName);
        String jsonSurveyUnitUpdate = createJsonSurveyUnitAddress(surveyUnit);
        mockMvc.perform(put(Constants.API_SURVEY_UNITS + identifier).content(jsonSurveyUnitUpdate)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().json(jsonSurveyUnitUpdate.toString(), false));
        SurveyUnit countactFoundAfterUpdate = surveyUnitService.findbyId(identifier);
        assertEquals(surveyUnit.getSurveyUnitAddress().getCity(),
                countactFoundAfterUpdate.getSurveyUnitAddress().getCity());

        // delete surveyUnit
        surveyUnitService.deleteSurveyUnit(identifier);
        assertThrows(NoSuchElementException.class, () -> {
            surveyUnitService.findbyId(identifier);
        });

    }

    @Test
    public void putSurveyUnitsErrorId() throws Exception {
        String identifier = "NEWONE";
        String otherIdentifier = "WRONG";
        SurveyUnit surveyUnit = initSurveyUnit(identifier);
        String jsonSurveyUnit = createJson(surveyUnit);
        mockMvc.perform(put(Constants.API_SURVEY_UNITS + otherIdentifier).content(jsonSurveyUnit)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("id and idSu don't match"));

    }

    private SurveyUnit initSurveyUnit(String identifier) {
        SurveyUnit surveyUnitMock = new SurveyUnit();
        surveyUnitMock.setIdSu(identifier);
        surveyUnitMock.setIdentificationCode("CODE - " + identifier);
        surveyUnitMock.setIdentificationName("company name " + identifier);

        return surveyUnitMock;
    }

    private SurveyUnit initSurveyUnitAddress(String identifier) {
        SurveyUnit surveyUnit = initSurveyUnit(identifier);
        SurveyUnitAddress address = initAddress(identifier);
        surveyUnit.setSurveyUnitAddress(address);
        return surveyUnit;
    }

    private SurveyUnitAddress initAddress(String identifier) {
        SurveyUnitAddress address = new SurveyUnitAddress();
        address.setCity("city " + identifier);
        address.setCountryName("country " + identifier);
        address.setStreetName("steet " + identifier);
        address.setStreetNumber(identifier);
        return address;
    }

    private String createJson(SurveyUnit surveyUnit) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("idSu", surveyUnit.getIdSu());
        jo.put("identificationCode", surveyUnit.getIdentificationCode());
        jo.put("identificationName", surveyUnit.getIdentificationName());
        return jo.toString();
    }

    private String createJsonSurveyUnitAddress(SurveyUnit surveyUnit) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("idSu", surveyUnit.getIdSu());
        jo.put("identificationCode", surveyUnit.getIdentificationCode());
        jo.put("identificationName", surveyUnit.getIdentificationName());
        jo.put("address", createJsonAddress(surveyUnit));
        return jo.toString();

    }

    private JSONObject createJsonAddress(SurveyUnit surveyUnit) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("city", surveyUnit.getSurveyUnitAddress().getCity());
        jo.put("streetName", surveyUnit.getSurveyUnitAddress().getStreetName());
        jo.put("countryName", surveyUnit.getSurveyUnitAddress().getCountryName());
        return jo;
    }

}
