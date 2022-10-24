package fr.insee.survey.datacollectionmanagement.metadata.controller;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Partitioning;
import fr.insee.survey.datacollectionmanagement.metadata.service.PartitioningService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@PreAuthorize("@AuthorizeMethodDecider.isInternalUser() "
        + "|| @AuthorizeMethodDecider.isWebClient() ")
public class PartitioningController {

    static final Logger LOGGER = LoggerFactory.getLogger(PartitioningController.class);

    @Autowired
    private PartitioningService partitioningService;

    @GetMapping(value = "partitionings", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<Partitioning> findPartitionings(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "20") Integer size,
        @RequestParam(defaultValue = "id") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return partitioningService.findAll(pageable);
    }

    @GetMapping(value = "partitionings/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findPartitioning(@PathVariable("id") String id) {
        Partitioning partitioning = null;
        try {
            partitioning = partitioningService.findById(StringUtils.upperCase(id));
            return new ResponseEntity<>(partitioning, HttpStatus.OK);
        }
        catch (NoSuchElementException e) {
            return new ResponseEntity<>(partitioning, HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<String>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping(value = "partitionings/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> putPartitioning(@PathVariable("id") String id, @RequestBody Partitioning partitioning) {
        if (StringUtils.isBlank(partitioning.getId()) || !partitioning.getId().equalsIgnoreCase(id)) {
            return new ResponseEntity<>("id and partitioning identifier don't match", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(partitioningService.updatePartitioning(partitioning), HttpStatus.OK);
    }


}
