package fr.insee.survey.datacollectionmanagement.metadata.service.impl;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Partitioning;
import fr.insee.survey.datacollectionmanagement.metadata.repository.PartitioningRepository;
import fr.insee.survey.datacollectionmanagement.metadata.service.PartitioningService;
import fr.insee.survey.datacollectionmanagement.metadata.util.PartitioningStatusEnum;

@Service
public class PartioningServiceImpl implements PartitioningService {

    @Autowired
    private PartitioningRepository partitioningRepository;

    @Override
    public Partitioning findById(String id) {
        return partitioningRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Partitioning not found"));

    }

    @Override
    public List<String> findIdPartitioningsBySourceIdYearPeriod(String sourceId, String year, String period) {
        return partitioningRepository.findIdPartitioningBySourceIdYearPeriod(sourceId, year, period);
    }

    @Override
    public List<String> findIdPartitioningsBySourceId(String sourceId) {
        return partitioningRepository.findIdPartitioningBySourceId(sourceId);
    }

    @Override
    public List<String> findIdPartitioningsByYear(String year) {
        return partitioningRepository.findIdPartitioningByYear(year);
    }

    @Override
    public List<String> findIdPartitioningsByPeriod(String period) {
        return partitioningRepository.findIdPartitioningByPeriod(period);
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
            if (today.compareTo(openingDate) > 0 && today.compareTo(closingDate) < 0) return PartitioningStatusEnum.OPEN;
            if (today.compareTo(closingDate) > 0) return PartitioningStatusEnum.CLOSED;
            if (today.compareTo(openingDate) < 0 && today.compareTo(closingDate) < 0) return PartitioningStatusEnum.FORTHCOMING;
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

}
