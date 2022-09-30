package fr.insee.survey.datacollectionmanagement.query.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.insee.survey.datacollectionmanagement.constants.Constants;
import fr.insee.survey.datacollectionmanagement.query.dto.MoogSearchDto;
import fr.insee.survey.datacollectionmanagement.query.service.MoogSearchService;
import fr.insee.survey.datacollectionmanagement.view.domain.View;

@RestController
@CrossOrigin
public class MoogSearchController {

    static final Logger LOGGER = LoggerFactory.getLogger(MoogSearchController.class);

    @Autowired
    private MoogSearchService moogSearchService;

    @GetMapping(path = Constants.API_MOOG_SEARCH)
    public ResponseEntity<?> moogSearch(@RequestParam(required = false) String filter1,
            @RequestParam(required = false) String filter2,
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "20", required = false) int pageSize) {

        List<View> listView = moogSearchService.moogSearch(filter1);

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        int start = (int) pageable.getOffset();
        int end = (int) ((start + pageable.getPageSize()) > listView.size() ? listView.size()
                : (start + pageable.getPageSize()));

        if (start <= end) {
            Page<MoogSearchDto> page = new PageImpl<MoogSearchDto>(
                    moogSearchService.transformListViewToListMoogSearchDto(listView.subList(start, end)), pageable,
                    listView.size());
            return new ResponseEntity<>(page, HttpStatus.OK);

        } else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

}
