package fr.insee.survey.datacollectionmanagement.query.service;

import fr.insee.survey.datacollectionmanagement.exception.RessourceNotValidatedException;
import fr.insee.survey.datacollectionmanagement.query.domain.ResultUpload;
import fr.insee.survey.datacollectionmanagement.query.domain.Upload;
import fr.insee.survey.datacollectionmanagement.query.dto.UploadDto;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public interface UploadService {

    public ResultUpload save(String idCampaign, UploadDto uploadDto) throws RessourceNotValidatedException;

    public Optional<Upload> findById(long id);

    public List<Upload> findAllByIdCampaign(String idCampaign);

    public void delete(Upload up);

    public Upload saveAndFlush(Upload up);

    public boolean checkUploadDate(String idCampaign, Date date);

    public void removeEmptyUploads();

}
