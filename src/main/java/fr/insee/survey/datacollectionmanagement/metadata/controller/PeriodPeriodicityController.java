package fr.insee.survey.datacollectionmanagement.metadata.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import fr.insee.survey.datacollectionmanagement.constants.Constants;
import fr.insee.survey.datacollectionmanagement.metadata.util.PeriodEnum;
import fr.insee.survey.datacollectionmanagement.metadata.util.PeriodicityEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@PreAuthorize("@AuthorizeMethodDecider.isInternalUser() "
        + "|| @AuthorizeMethodDecider.isWebClient() ")
@Tag(name = "3 - Metadata", description = "Enpoints to create, update, delete and find entities in metadata domain")
public class PeriodPeriodicityController {

    @Operation(summary = "Search for periodicities")
    @GetMapping(value = Constants.API_PERIODICITIES, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    public ResponseEntity<?> getPeriodicities() throws JsonProcessingException, JSONException {
        JSONObject json = new JSONObject();
        for (PeriodicityEnum periodicity : PeriodicityEnum.values()) {
            json.put(periodicity.name(), new JSONObject(periodicity));
        }
        return ResponseEntity.ok().body(json.toString());
    }

    @Operation(summary = "Search for periods")
    @GetMapping(value = Constants.API_PERIODS, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    public ResponseEntity<?> getPeriods() throws JsonProcessingException, JSONException {
        JSONObject json = new JSONObject();
        for (PeriodEnum period : PeriodEnum.values()) {
            json.put(period.name(), new JSONObject(period));
        }
        return ResponseEntity.ok().body(json.toString());
    }

}
