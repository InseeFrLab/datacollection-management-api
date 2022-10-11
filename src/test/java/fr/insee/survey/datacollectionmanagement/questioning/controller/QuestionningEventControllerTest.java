package fr.insee.survey.datacollectionmanagement.questioning.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
public class QuestionningEventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getQuestioningEventOk() throws Exception {
        String identifier = "12";
        String json = createJsonQuestioningEvent(identifier);
        this.mockMvc.perform(get("/api/questionings/" + identifier
                + "/questioning-events")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().json(json, false));
    }

    @Test
    public void getQuestioningEventNotFound() throws Exception {
        String identifier = "300";
        this.mockMvc.perform(get("/api/questionings/" + identifier + "/questioning-events")).andDo(print())
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));

    }

    private String createJsonQuestioningEvent(String identifier) throws JSONException {
        JSONObject joEventInitla = new JSONObject();
        joEventInitla.put("type", "INITLA");

        JSONObject joPartiel = new JSONObject();
        joPartiel.put("type", "PARTIELINT");

        JSONObject joValint = new JSONObject();
        joValint.put("type", "VALINT");

        JSONArray ja = new JSONArray();
        ja.put(joEventInitla);
        ja.put(joPartiel);
        ja.put(joValint);

        System.out.println(ja.toString());
        return ja.toString();
    }

}
