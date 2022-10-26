package fr.insee.survey.datacollectionmanagement.metadata.controller;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.NoSuchElementException;
import java.util.Optional;

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
import fr.insee.survey.datacollectionmanagement.metadata.domain.Source;
import fr.insee.survey.datacollectionmanagement.metadata.repository.SourceRepository;
import fr.insee.survey.datacollectionmanagement.metadata.service.SourceService;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class SourceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SourceService sourceService;

    @Autowired
    private SourceRepository sourceRepository;

    @Test
    public void getSourceOk() throws Exception {
        String identifier = "SOURCE1";
        Optional<Source> source = sourceService.findById(identifier);
        assertTrue(source.isPresent());
        String json = createJson(source.get());
        this.mockMvc.perform(get(Constants.API_SOURCES + "/" + identifier)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().json(json, false));
    }

    @Test
    public void getSourceNotFound() throws Exception {
        String identifier = "SOURCENOTFOUND";
        this.mockMvc.perform(get(Constants.API_SOURCES + "/" + identifier)).andDo(print())
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));

    }

    @Test
    public void getSourcesOk() throws Exception {
        JSONObject jo = new JSONObject();
        jo.put("totalElements", sourceRepository.count());
        jo.put("numberOfElements", sourceRepository.count());

        this.mockMvc.perform(get(Constants.API_SOURCES)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().json(jo.toString(), false));
    }

    @Test
    public void putSourceCreateUpdateDelete() throws Exception {
        String identifier = "SOURCEPUT";

        // create source - status created
        Source source = initSource(identifier);
        String jsonSource = createJson(source);
        mockMvc.perform(
                put(Constants.API_SOURCES + "/" + identifier).content(jsonSource)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(jsonSource.toString(), false));
        Optional<Source> sourceFound = sourceService.findById(identifier);
        assertTrue(sourceFound.isPresent());
        assertEquals(source.getLongWording(), sourceFound.get().getLongWording());
        assertEquals(source.getShortWording(), sourceFound.get().getShortWording());
        assertEquals(source.getPeriodicity(), sourceFound.get().getPeriodicity());

        // update source - status ok
        source.setLongWording("Long wording update");
        String jsonSourceUpdate = createJson(source);
        mockMvc.perform(put(Constants.API_SOURCES + "/" + identifier).content(jsonSourceUpdate)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().json(jsonSourceUpdate.toString(), false));
        Optional<Source> sourceFoundAfterUpdate = sourceService.findById(identifier);
        assertTrue(sourceFoundAfterUpdate.isPresent());
        assertEquals("Long wording update", sourceFoundAfterUpdate.get().getLongWording());
        assertEquals(source.getId(), sourceFoundAfterUpdate.get().getId());

        // delete source
        mockMvc.perform(delete(Constants.API_SOURCES + "/" + identifier).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        assertFalse(sourceService.findById(identifier).isPresent());

        // delete source not found
        mockMvc.perform(delete(Constants.API_SOURCES + "/" + identifier).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }


    @Test
    public void putSourcesErrorId() throws Exception {
        String identifier = "NEWONE";
        String otherIdentifier = "WRONG";
        Source source = initSource(identifier);
        String jsonSource = createJson(source);
        mockMvc.perform(put(Constants.API_SOURCES + "/" + otherIdentifier).content(jsonSource)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andExpect(content().string("id and source identifier don't match"));

    }

    private Source initSource(String identifier) {
        Source sourceMock = new Source();
        sourceMock.setLongWording("Long wording about " + identifier);
        sourceMock.setShortWording("Short wording about " + identifier);
        sourceMock.setPeriodicity("T");
        sourceMock.setMandatoryMySurveys(true);
        return sourceMock;
    }

    private String createJson(Source source) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("longWording", source.getLongWording());
        jo.put("shortWording", source.getShortWording());
        jo.put("periodicity", source.getPeriodicity());
        return jo.toString();
    }

}
