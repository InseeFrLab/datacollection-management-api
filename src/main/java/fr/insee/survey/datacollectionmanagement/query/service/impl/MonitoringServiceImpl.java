package fr.insee.survey.datacollectionmanagement.query.service.impl;

import fr.insee.survey.datacollectionmanagement.config.JSONCollectionWrapper;
import fr.insee.survey.datacollectionmanagement.query.dto.MoogFollowUpDto;
import fr.insee.survey.datacollectionmanagement.query.dto.MoogProgressDto;
import fr.insee.survey.datacollectionmanagement.query.dto.MoogRowProgressDto;
import fr.insee.survey.datacollectionmanagement.query.repository.MonitoringRepository;
import fr.insee.survey.datacollectionmanagement.query.service.MonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class MonitoringServiceImpl implements MonitoringService {

    @Autowired
    MonitoringRepository monitoringRepository;

    @Override
    public JSONCollectionWrapper<MoogProgressDto> getProgress(String idCampaign) {
        List<MoogRowProgressDto> rows = monitoringRepository.getProgress(idCampaign);

        HashMap<Integer, MoogProgressDto> lots = new HashMap<Integer, MoogProgressDto>();

        if (!rows.isEmpty()) {
            for (MoogRowProgressDto row : rows) {

                int batchNumber = row.getBatchNum();

                if (!lots.containsKey(Integer.valueOf(batchNumber))) {
                    lots.put(Integer.valueOf(batchNumber), new MoogProgressDto(batchNumber));
                }

                MoogProgressDto lot = lots.get(Integer.valueOf(batchNumber));

                lot.setNbSu(lot.getNbSu() + row.getTotal());

                switch (row.getStatus().trim()) {
                    case "REFUSAL":
                        lot.setNbRefusal(row.getTotal());
                        break;
                    case "VALINT":
                        lot.setNbIntReceived(row.getTotal());
                        break;
                    case "VALPAP":
                        lot.setNbPapReceived(row.getTotal());
                        break;
                    case "HC":
                        lot.setNbHC(row.getTotal());
                        break;
                    case "PARTIELINT":
                        lot.setNbIntPart(row.getTotal());
                        break;
                    case "WASTE":
                        lot.setNbOtherWastes(row.getTotal());
                        break;
                    case "PND":
                        lot.setNbPND(row.getTotal());
                        break;
                    case "INITLA":
                        break;
                }
            }
        }
        return new JSONCollectionWrapper<>(lots.values());
    }

    @Override
    public JSONCollectionWrapper<MoogFollowUpDto> getFollowUp(String idCampaign) {
        return new JSONCollectionWrapper<MoogFollowUpDto>(monitoringRepository.getFollowUp(idCampaign));
    }
}
