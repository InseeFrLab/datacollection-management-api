package fr.insee.survey.datacollectionmanagement.metadata.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
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
        + "|| @AuthorizeMethodDecider.isWebClient() "
        + "|| @AuthorizeMethodDecider.isAdmin() ")
@Tag(name = "3 - Metadata", description = "Enpoints to create, update, delete and find entities in metadata domain")
public class PeriodPeriodicityController {

    @Operation(summary = "Search for periodicities")
    @GetMapping(value = Constants.API_PERIODICITIES, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    public ResponseEntity<?> getPeriodicities() throws JsonProcessingException, JSONException {
        JSONArray jsonArray = new JSONArray();
        for (PeriodicityEnum periodicity : PeriodicityEnum.values()) {
            JSONObject json = new JSONObject();
            json.put("key", periodicity.name());
            json.put("label", periodicity.getValue());
            jsonArray.put(json);
        }
        return ResponseEntity.ok().body(jsonArray.toString());
    }

    @Operation(summary = "Search for periods")
    @GetMapping(value = Constants.API_PERIODS, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    public ResponseEntity<?> getPeriods() throws JsonProcessingException, JSONException {
        JSONArray jsonArray = new JSONArray();

        for (PeriodEnum period : PeriodEnum.values()) {
            JSONObject json = new JSONObject();
            json.put("key", period.name());
            json.put("label", period.getValue());
            json.put("period",period.getPeriod().name());
            jsonArray.put(json);
        }
        return ResponseEntity.ok().body(jsonArray.toString());
    }

    @Operation(summary = "Search for periods of a periodicity")
    @GetMapping(value = Constants.API_PERIODICITIES_ID_PERIODS, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    public ResponseEntity<?> getPeriodsOdPeriodicity(String periodicity) throws JsonProcessingException, JSONException {
        try {
            PeriodicityEnum.valueOf(periodicity);
            JSONArray jsonArray = new JSONArray();
            for (PeriodEnum period : PeriodEnum.values()) {
                if (period.getPeriod().equals(PeriodicityEnum.valueOf(periodicity))) {
                    JSONObject json = new JSONObject();
                    json.put("key", period.name());
                    json.put("label", period.getValue());
                    json.put("period",period.getPeriod().name());
                    jsonArray.put(json);
                }          
            }
            return ResponseEntity.ok().body(jsonArray.toString());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("periodicity does not exist");       
        }

    }

}
