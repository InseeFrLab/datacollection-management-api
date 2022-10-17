package fr.insee.survey.datacollectionmanagement.metadata.controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import fr.insee.survey.datacollectionmanagement.constants.Constants;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Campaign;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Partitioning;
import fr.insee.survey.datacollectionmanagement.metadata.dto.PartitioningDto;
import fr.insee.survey.datacollectionmanagement.metadata.service.CampaignService;
import fr.insee.survey.datacollectionmanagement.metadata.service.PartitioningService;
import fr.insee.survey.datacollectionmanagement.questioning.service.QuestioningService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin
@Tag(name = "3 - Metadata", description = "Enpoints to create, update, delete and find entities in metadata domain")
public class PartitioningController {

    static final Logger LOGGER = LoggerFactory.getLogger(PartitioningController.class);
    @Autowired
    private PartitioningService partitioningService;

    @Autowired
    private CampaignService campaignService;

    @Autowired
    private ModelMapper modelmapper;

    private QuestioningService questioningService;

    @Operation(summary = "Search for partitionings by the campaign id")
    @GetMapping(value = Constants.API_CAMPAIGNS_ID_PARTITIONINGS, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = PartitioningDto.class)))),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> getPartitioningsByCampaign(@PathVariable("id") String id) {
        try {
            Campaign campaign = campaignService.findById(id);
            return ResponseEntity.ok()
                    .body(campaign.getPartitionings().stream().map(s -> convertToDto(s)).collect(Collectors.toList()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("campaign does not exist");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error");
        }

    }

    @Operation(summary = "Search for a partitioning by its id")
    @GetMapping(value = Constants.API_PARTITIONINGS_ID, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = PartitioningDto.class))),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> getPartitioning(@PathVariable("id") String id) {
        Partitioning partitioning = null;
        try {
            partitioning = partitioningService.findById(StringUtils.upperCase(id));
            return ResponseEntity.ok().body(convertToDto(partitioning));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("partitioning does not exist");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error");
        }

    }

    @Operation(summary = "Update or create a partitioning")
    @PutMapping(value = Constants.API_PARTITIONINGS_ID, produces = "application/json", consumes = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = PartitioningDto.class))),
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = PartitioningDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> putPartitioning(@PathVariable("id") String id,
            @RequestBody PartitioningDto partitioningDto) {
        if (StringUtils.isBlank(partitioningDto.getId()) || !partitioningDto.getId().equalsIgnoreCase(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("id and idPartitioning don't match");
        }
        Partitioning partitioning;
        try {
            campaignService.findById(partitioningDto.getCampaignId());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Campaign does not exist");
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(HttpHeaders.LOCATION,
                ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(partitioningDto.getId()).toUriString());
        HttpStatus httpStatus;

        try {
            partitioningService.findById(id);
            httpStatus = HttpStatus.OK;

        } catch (NoSuchElementException e) {
            LOGGER.info("Creating partitioning with the id {}", partitioningDto.getId());
            httpStatus = HttpStatus.CREATED;
        }

        partitioning = partitioningService.updatePartitioning(convertToEntity(partitioningDto));
        Campaign campaign = partitioning.getCampaign();
        campaign.getPartitionings().add(partitioning);
        campaignService.updateCampaign(campaign);
        return ResponseEntity.status(httpStatus).headers(responseHeaders).body(convertToDto(partitioning));
    }

    @Operation(summary = "Delete a partitioning, its partitionings, partitionings, questionings ...")
    @DeleteMapping(value = Constants.API_PARTITIONINGS_ID)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @Transactional
    public ResponseEntity<?> deletePartitioning(@PathVariable("id") String id) {
        try {
            Partitioning partitioning = partitioningService.findById(id);
            Campaign campaign = partitioning.getCampaign();
            campaign.getPartitionings().remove(partitioning);
            campaignService.updateCampaign(campaign);
            partitioningService.deletePartitioningById(id);
            questioningService.findByIdPartitioning(partitioning.getId()).stream()
                    .forEach(q -> questioningService.deleteQuestioning(q.getId()));
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Partitioning deleted");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Partitioning does not exist");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error");
        }
    }

    private PartitioningDto convertToDto(Partitioning partitioning) {
        return modelmapper.map(partitioning, PartitioningDto.class);
    }

    private Partitioning convertToEntity(PartitioningDto partitioningDto) {
        return modelmapper.map(partitioningDto, Partitioning.class);
    }

    class PartitioningPage extends PageImpl<PartitioningDto> {

        public PartitioningPage(List<PartitioningDto> content, Pageable pageable, long total) {
            super(content, pageable, total);
        }
    }

}
