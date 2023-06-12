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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import fr.insee.survey.datacollectionmanagement.constants.Constants;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class QuestionningControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getQuestioningOk() throws Exception {
        int id = 11;
        String json = createJson(id).toString();
        this.mockMvc.perform(get(Constants.API_QUESTIONINGS_ID, id)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().json(json, false));
    }

    @Test
    public void getQuestioningNotFound() throws Exception {
        String id = "300";
        this.mockMvc.perform(get(Constants.API_QUESTIONINGS_ID, id)).andDo(print())
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));

    }

    @Test
    public void getQuestioningsBySurveyUnit() throws Exception {
        String idSu = "100000000";
        String json = createJsonQuestionings(idSu);
        this.mockMvc.perform(get(Constants.API_SURVEY_UNITS_ID_QUESTIONINGS, idSu)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(json, false));

    }

    private JSONObject createJson(int id) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("id", id);
        return jo;
    }

    private String createJsonQuestionings(String id) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("surveyUnitId", id);
        JSONArray ja = new JSONArray();
        ja.put(jo);
        System.out.println(ja.toString());
        return ja.toString();
    }

}
