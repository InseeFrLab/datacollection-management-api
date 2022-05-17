package fr.insee.survey.datacollectionmanagement.util;


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
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashSet;

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
        for (int i = 0; i < 10000; i++) {
            final Contact c = new Contact();
            final Address a = new Address();


            a.setCountryName("country-"+ RandomStringUtils.randomAlphabetic(4));
            a.setStreetNumber(15);
            a.setStreetName("streetname-"+ RandomStringUtils.randomAlphabetic(4));
            addressRepository.save(a);

            c.setIdentifier("id-"+RandomStringUtils.randomAlphabetic(4));
            c.setName("name-"+RandomStringUtils.randomAlphabetic(4));
            c.setFunction("function-"+RandomStringUtils.randomAlphabetic(4));
            c.setPhone("phone-"+RandomStringUtils.randomAlphabetic(4));
            c.setZipCode("zipcode-"+RandomStringUtils.randomAlphabetic(4));
            c.setGender(Contact.Gender.male);
            c.setComment("comment-"+RandomStringUtils.randomAlphabetic(4));
            c.setEmail("email-"+RandomStringUtils.randomAlphabetic(4));
            c.setAddress(a);
            contactRepository.save(c);
        }

        Source source1 = new Source();
        source1.setIdSource("POULET");
        source1.setShortWording("Enquete poulet");
        source1.setPeriodicity("A");
        sourceRepository.save(source1);

        Source source2 = new Source();
        source2.setIdSource("CAILLE");
        source2.setShortWording("Enquete caille");
        source1.setPeriodicity("T");
        sourceRepository.save(source2);



        Survey survey1 = new Survey();
        survey1.setId("POULET2022");
        survey1.setYear(2022);
        source1.setSurveys(new HashSet<>(Arrays.asList(survey1)));
        surveyRepository.save(survey1);

        Survey survey2 = new Survey();
        survey2.setId("CAILLE2022");
        survey2.setYear(2022);
        source2.setSurveys(new HashSet<>(Arrays.asList(survey2)));
        surveyRepository.save(survey2);


        Campaign campaign1 = new Campaign();
        campaign1.setCampaignId("POULET2022A00");
        campaignRepository.save(campaign1);

        Campaign campaign2 = new Campaign();
        campaign2.setCampaignId("CAILLE2022T01");
        campaignRepository.save(campaign2);

        Campaign campaign3 = new Campaign();
        campaign3.setCampaignId("CAILLE2022T02");
        campaignRepository.save(campaign3);

        for(int i = 0;i<10;i++){
            Partitioning part3= new Partitioning();
            part3.setId("CAILLE2022T02-0"+i);
            part3.setCampaign(campaign3);
            partitioningRepository.save(part3);
        }
        Partitioning part1 = new Partitioning();
        part1.setId("POULET2022A00-01");
        part1.setCampaign(campaign1);
        partitioningRepository.save(part1);




    }
}
