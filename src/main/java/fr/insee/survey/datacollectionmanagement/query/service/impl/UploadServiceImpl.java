package fr.insee.survey.datacollectionmanagement.query.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.insee.survey.datacollectionmanagement.exception.RessourceNotValidatedException;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Campaign;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Partitioning;
import fr.insee.survey.datacollectionmanagement.metadata.service.CampaignService;
import fr.insee.survey.datacollectionmanagement.query.domain.ResultUpload;
import fr.insee.survey.datacollectionmanagement.query.domain.Upload;
import fr.insee.survey.datacollectionmanagement.query.dto.MoogQuestioningEventDto;
import fr.insee.survey.datacollectionmanagement.query.dto.MoogUploadQuestioningEventDto;
import fr.insee.survey.datacollectionmanagement.query.dto.UploadDto;
import fr.insee.survey.datacollectionmanagement.query.repository.UploadRepository;
import fr.insee.survey.datacollectionmanagement.query.service.UploadService;
import fr.insee.survey.datacollectionmanagement.questioning.domain.Questioning;
import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningAccreditation;
import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningEvent;
import fr.insee.survey.datacollectionmanagement.questioning.service.QuestioningEventService;
import fr.insee.survey.datacollectionmanagement.questioning.service.QuestioningService;
import fr.insee.survey.datacollectionmanagement.questioning.util.TypeQuestioningEvent;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UploadServiceImpl implements UploadService {

    @Autowired
    UploadRepository uploadRepository;

    @Autowired
    QuestioningEventService questioningEventService;

    @Autowired
    CampaignService campaignService;

    @Autowired
    QuestioningService questioningService;

    @Override
    public ResultUpload save(String idCampaign, UploadDto uploadDto) throws RessourceNotValidatedException {

        ResultUpload result = new ResultUpload();
        Date today = new Date();

        // Check campaign exists and date in intervals
        if (!checkUploadDate(idCampaign, today))
            throw new RessourceNotValidatedException("Campaign", idCampaign);

        // Creating and saving the upload to get the id
        Upload up = new Upload(null, today.getTime(), null);
        up = saveAndFlush(up);
        // Creation of managementMonitoringInfo list and saving of link with upload
        List<QuestioningEvent> liste = new ArrayList<>();

        for (MoogUploadQuestioningEventDto mmDto : uploadDto.getData()) {
            String identifier = (mmDto.getIdSu() != null) ? mmDto.getIdSu() : mmDto.getIdContact();
            try {
                QuestioningEvent qe = new QuestioningEvent();
                Set<Questioning> questionings = questioningService.findBySurveyUnitIdSu(mmDto.getIdSu());
                Optional<Questioning> quest = questionings.stream().filter(q -> q.getQuestioningAccreditations().stream().map(QuestioningAccreditation::getIdContact).collect(Collectors.toList()).contains(mmDto.getIdContact())).findFirst();
                qe.setUpload(up);
                qe.setType(TypeQuestioningEvent.valueOf(mmDto.getStatus()));
                qe.setQuestioning(quest.get());
                JSONObject jo = new JSONObject();
                jo.put("source", "Moog IHM - Post Event by upload");
                ObjectMapper objectMapper = new ObjectMapper();
                qe.setPayload(objectMapper.readTree(jo.toString()));
                qe.setDate(today);
                liste.add(questioningEventService.saveQuestioningEvent(qe));
                if(quest.isPresent()){
                    quest.get().getQuestioningEvents().add(qe);
                    questioningService.saveQuestioning(quest.get());
                }

                result.addIdOk(identifier);
            } catch (Exception e) {
                log.error("Error in request");
                log.info("Info: id KO " + e.getMessage());
                e.printStackTrace();
                result.addIdKo(identifier, "RessourceNotFound or unprocessable request");
            }
        }
        up.setQuestioningEvents(liste);
        up = saveAndFlush(up);

        return result;
    }

    @Override
    public Optional<Upload> findById(long id) {
        return uploadRepository.findById(id);
    }

    @Override
    public List<Upload> findAllByIdCampaign(String idCampaign) {

        Optional<Campaign> campaign = campaignService.findById(idCampaign);

        List<String> partitioningIds = campaign.get().getPartitionings().stream().map(Partitioning::getId).collect(Collectors.toList());

        // Keeps the uploads which first managementMonitoringInfo belongs to the survey
        return uploadRepository.findAll().stream().filter(upload -> !upload.getQuestioningEvents().isEmpty())
                .filter(upload -> partitioningIds.contains(upload.getQuestioningEvents().stream().findFirst().get().getQuestioning().getIdPartitioning()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteUpload(Long id) {
        Optional<Upload> upOpt = findById(id);
        if(!upOpt.isPresent()) {
            return false;
        }
        Upload up = upOpt.get();
        questioningEventService.deleteAll(up.getQuestioningEvents());
        delete(up);
        return true;
    }

    @Override
    public void delete(Upload up) {
        uploadRepository.delete(up);
    }

    @Override
    public Upload saveAndFlush(Upload up) {
        return uploadRepository.saveAndFlush(up);
    }

    @Override
    public boolean checkUploadDate(String idCampaign, Date date) {
        Optional<Campaign> campaign = campaignService.findById(idCampaign);
        Long timestamp=date.getTime();
        Long start = campaign.get().getPartitionings().stream().map(Partitioning::getOpeningDate)
                .collect(Collectors.toList()).stream()
                .min(Comparator.comparing(Date::getTime)).get().getTime();
        Long end = campaign.get().getPartitionings().stream().map(Partitioning::getClosingDate)
                .collect(Collectors.toList()).stream()
                .max(Comparator.comparing(Date::getTime)).get().getTime();
        return (start < timestamp && timestamp < end);
    }

    @Override
    public void removeEmptyUploads() {
        uploadRepository.findByQuestioningEventsIsEmpty().forEach(u -> uploadRepository.delete(u));
    }
}
