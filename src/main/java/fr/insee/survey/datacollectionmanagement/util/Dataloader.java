package fr.insee.survey.datacollectionmanagement.util;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.RandomStringUtils;
import org.jeasy.random.EasyRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.javafaker.Animal;
import com.github.javafaker.Faker;

import fr.insee.survey.datacollectionmanagement.contact.domain.Address;
import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;
import fr.insee.survey.datacollectionmanagement.contact.repository.AddressRepository;
import fr.insee.survey.datacollectionmanagement.contact.repository.ContactEventRepository;
import fr.insee.survey.datacollectionmanagement.contact.repository.ContactRepository;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Campaign;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Partitioning;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Source;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Survey;
import fr.insee.survey.datacollectionmanagement.metadata.repository.CampaignRepository;
import fr.insee.survey.datacollectionmanagement.metadata.repository.PartitioningRepository;
import fr.insee.survey.datacollectionmanagement.metadata.repository.SourceRepository;
import fr.insee.survey.datacollectionmanagement.metadata.repository.SurveyRepository;

@Component
public class Dataloader {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ContactEventRepository contactEventRepository;

    @Autowired
    private SourceRepository sourceRepository;

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private PartitioningRepository partitioningRepository;

    @PostConstruct
    public void init() {

        Faker faker = new Faker();
        EasyRandom generator = new EasyRandom();

        for (int i = 0; i < 10000; i ++ ) {
            final Contact c = new Contact();
            final Address a = new Address();

            String name = faker.name().lastName();
            String firstName = faker.name().firstName();

            a.setCountryName(faker.country().name());
            a.setStreetNumber(Integer.parseInt(RandomStringUtils.randomNumeric(3)));
            a.setStreetName(faker.address().streetName());
            a.setZipCode(faker.address().zipCode());
            a.setCity(faker.address().cityName());
            addressRepository.save(a);

            c.setIdentifier(RandomStringUtils.randomAlphanumeric(7).toUpperCase());
            c.setLastName(name);
            c.setFirstName(firstName);
            c.setPhone(faker.phoneNumber().phoneNumber());
            c.setGender(Contact.Gender.male);
            c.setFunction(faker.job().title());
            c.setComment(faker.beer().name());
            c.setEmail(name + "." + firstName + "@cocorico.fr");
            c.setAddress(a);
            contactRepository.save(c);
        }

        initMetadata(faker, generator);

    }

    private void initMetadata(Faker faker, EasyRandom generator2) {

        int year = 2022;

        for (int i = 0; i < 10; i ++ ) {
            Source source = new Source();
            Animal animal = faker.animal();
            String animalName = animal.name().toUpperCase();
            source.setIdSource(animalName);
            source.setShortWording("Source about " + animalName);
            source.setPeriodicity("M");
            Set<Survey> setSurveys = new HashSet<>();
            
            for (int j = 0; j < 4; j ++ ) {

                Survey survey = new Survey();
                String id = animalName + (year - j);
                survey.setId(id);
                survey.setYear(year - j);
                survey.setLongObjectives("The purpose of this survey is to find out everything you can about "
                    + animalName + ". Your response is essential to ensure the quality and reliability of the results of this survey.");
                survey.setShortObjectives("All about " + id);
                survey.setCommunication("Communication around " + id);
                survey.setSpecimenUrl("http://specimenUrl/" + id);
                survey.setDiffusionUrl("http://diffusion/" +id);
                survey.setCnisUrl("http://cnis/" +id);
                survey.setNoticeUrl("http://notice/" +id);
                survey.setVisaNumber(year + RandomStringUtils.randomAlphanumeric(6).toUpperCase());
                survey.setLongWording("Survey " + id);
                survey.setShortWording(id);
                survey.setSampleSize(Integer.parseInt(RandomStringUtils.randomNumeric(5)));
                setSurveys.add(survey);
                Set<Campaign> setCampaigns = new HashSet<>();
                
                
                for (int k = 0; k < 11; k ++ ) {
                    Campaign campaign = new Campaign();
                    int month = k + 1;
                    String period = "M" + month;
                    campaign.setYear(year - j);
                    campaign.setPeriod(period);
                    campaign.setCampaignId(animalName + (year - j) + period);
                    campaign.setCampaignWording("Campaign about " + animalName + " in " + year + " and period " + period);
                    setCampaigns.add(campaign);
                    Set<Partitioning> setParts = new HashSet<>();

                    
                    for (int l = 0; l < 3; l ++ ) {

                        Partitioning part = new Partitioning();
                        part.setId(animalName + (year - j) + "M" + month + "-00" + l);
                        setParts.add(part);
                        partitioningRepository.save(part);
                    }
                    campaign.setPartitionings(setParts);
                    campaignRepository.save(campaign);

                }
                survey.setCampaigns(setCampaigns);
                surveyRepository.save(survey);
            }
            source.setSurveys(setSurveys);
            sourceRepository.save(source);
            

        }
    }
}
