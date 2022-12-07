package fr.insee.survey.datacollectionmanagement.query.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import fr.insee.survey.datacollectionmanagement.constants.Constants;
import fr.insee.survey.datacollectionmanagement.query.service.CheckHabilitationService;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class CheckHabilitationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CheckHabilitationService checkHabilitationService;

    @Test
    public void checkHabilitationV2() throws Exception {
        String identifier = "IDEC";
        String idSu = "12345";
        String campaginId = "CAMPAIGN";

        when(checkHabilitationService.checkHabilitation(identifier, idSu, campaginId)).thenReturn(true);
        this.mockMvc
                .perform(get(Constants.API_CHECK_HABILITATION).param("identifier", identifier).param("idSu", idSu)
                        .param("campaignId", campaginId))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString("true")));

        this.mockMvc
                .perform(get(Constants.API_CHECK_HABILITATION).param("identifier", identifier).param("idSu", "bidon")
                        .param("campaignId", campaginId))
                .andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString("false")));
    }

}
