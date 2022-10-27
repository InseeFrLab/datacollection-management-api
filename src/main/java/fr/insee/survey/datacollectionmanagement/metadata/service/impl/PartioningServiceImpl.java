package fr.insee.survey.datacollectionmanagement.metadata.service.impl;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Partitioning;
import fr.insee.survey.datacollectionmanagement.metadata.repository.PartitioningRepository;
import fr.insee.survey.datacollectionmanagement.metadata.service.PartitioningService;
import fr.insee.survey.datacollectionmanagement.metadata.util.PartitioningStatusEnum;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PartioningServiceImpl implements PartitioningService {

    @Autowired
    private PartitioningRepository partitioningRepository;

    @Override
    public Optional<Partitioning> findById(String id) {
        return partitioningRepository.findById(id);

    }

    @Override
    public Partitioning insertOrUpdatePartitioning(Partitioning partitioning) {
        Optional<Partitioning> campaignBase = findById(partitioning.getId());
        if (!campaignBase.isPresent()) {
            log.info("Create partitioning with the id {}", partitioning.getId());
            return partitioningRepository.save(partitioning);
        }
        log.info("Update partitioning with the id {}", partitioning.getId());
        return partitioningRepository.save(partitioning);

    }

    @Override
    public PartitioningStatusEnum calculatePartitioningStatus(Partitioning partitioning) {
        Date openingDate = partitioning.getOpeningDate();
        Date closingDate = partitioning.getClosingDate();
        Date returnDate = partitioning.getReturnDate();
        Date today = new Date();
        if (returnDate == null || closingDate == null || openingDate == null)
            return PartitioningStatusEnum.INCOMPLETE_DATES;
        else {
            if (today.compareTo(openingDate) > 0 && today.compareTo(closingDate) < 0)
                return PartitioningStatusEnum.OPEN;
            if (today.compareTo(closingDate) > 0)
                return PartitioningStatusEnum.CLOSED;
            if (today.compareTo(openingDate) < 0 && today.compareTo(closingDate) < 0)
                return PartitioningStatusEnum.FORTHCOMING;
            return PartitioningStatusEnum.INCOMPLETE_DATES;
        }

    }

    @Override
    public Date calculatePartitioningDate(Partitioning part, PartitioningStatusEnum partitioningStatusEnum) {
        switch (partitioningStatusEnum) {
            case OPEN:
                return part.getReturnDate();
            case CLOSED:
                return part.getReturnDate();
            case FORTHCOMING:
                return part.getOpeningDate();
            default:
                return null;
        }
    }

    @Override
    public void deletePartitioningById(String id) {
        partitioningRepository.deleteById(id);
    }

}
