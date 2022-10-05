package fr.insee.survey.datacollectionmanagement.query.controller;

import java.util.List;

import fr.insee.survey.datacollectionmanagement.query.dto.AccreditationDetailDto;
import fr.insee.survey.datacollectionmanagement.query.dto.MoogQuestioningEventDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import fr.insee.survey.datacollectionmanagement.constants.Constants;
import fr.insee.survey.datacollectionmanagement.query.dto.MoogSearchDto;
import fr.insee.survey.datacollectionmanagement.query.service.MoogService;
import fr.insee.survey.datacollectionmanagement.view.domain.View;

@RestController
@CrossOrigin
public class MoogController {

    static final Logger LOGGER = LoggerFactory.getLogger(MoogController.class);

    @Autowired
    private MoogService moogService;

    @GetMapping(path = Constants.API_MOOG_SEARCH)
    public ResponseEntity<?> moogSearch(@RequestParam(required = false) String filter1,
            @RequestParam(required = false) String filter2,
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "20", required = false) int pageSize) {

        List<View> listView = moogService.moogSearch(filter1);

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        int start = (int) pageable.getOffset();
        int end = (int) ((start + pageable.getPageSize()) > listView.size() ? listView.size()
                : (start + pageable.getPageSize()));

        if (start <= end) {
            Page<MoogSearchDto> page = new PageImpl<MoogSearchDto>(
                    moogService.transformListViewToListMoogSearchDto(listView.subList(start, end)), pageable,
                    listView.size());
            return new ResponseEntity<>(page, HttpStatus.OK);

        } else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    @GetMapping(path = Constants.API_MOOG_EVENTS, produces = "application/json")
    @Operation(summary = "Get Moog questioning events by campaign and idSu")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = MoogQuestioningEventDto.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    public ResponseEntity<?> getMoogQuestioningEvents(@PathVariable("campaign") String campaignId,
                                        @PathVariable("id") String idSu) {

        return new ResponseEntity<>(moogService.getMoogEvents(campaignId, idSu), HttpStatus.OK);



    }

}
