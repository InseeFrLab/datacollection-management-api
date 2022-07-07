package fr.insee.survey.datacollectionmanagement.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jeasy.random.EasyRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.javafaker.Animal;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;

import fr.insee.survey.datacollectionmanagement.contact.domain.AccreditationsCopy;
import fr.insee.survey.datacollectionmanagement.contact.domain.Address;
import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;
import fr.insee.survey.datacollectionmanagement.contact.domain.Contact.Gender;
import fr.insee.survey.datacollectionmanagement.contact.repository.AccreditationsCopyRepository;
import fr.insee.survey.datacollectionmanagement.contact.repository.AddressRepository;
import fr.insee.survey.datacollectionmanagement.contact.repository.ContactRepository;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Campaign;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Owner;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Partitioning;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Source;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Support;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Survey;
import fr.insee.survey.datacollectionmanagement.metadata.repository.CampaignRepository;
import fr.insee.survey.datacollectionmanagement.metadata.repository.OwnerRepository;
import fr.insee.survey.datacollectionmanagement.metadata.repository.PartitioningRepository;
import fr.insee.survey.datacollectionmanagement.metadata.repository.SourceRepository;
import fr.insee.survey.datacollectionmanagement.metadata.repository.SupportRepository;
import fr.insee.survey.datacollectionmanagement.metadata.repository.SurveyRepository;
import fr.insee.survey.datacollectionmanagement.questioning.domain.EventOrder;
import fr.insee.survey.datacollectionmanagement.questioning.domain.MetadataCopy;
import fr.insee.survey.datacollectionmanagement.questioning.domain.Questioning;
import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningAccreditation;
import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningEvent;
import fr.insee.survey.datacollectionmanagement.questioning.domain.SurveyUnit;
import fr.insee.survey.datacollectionmanagement.questioning.domain.TypeQuestioningEvent;
import fr.insee.survey.datacollectionmanagement.questioning.repository.EventOrderRepository;
import fr.insee.survey.datacollectionmanagement.questioning.repository.MetadataCopyRepository;
import fr.insee.survey.datacollectionmanagement.questioning.repository.QuestioningAccreditationRepository;
import fr.insee.survey.datacollectionmanagement.questioning.repository.QuestioningEventRepository;
import fr.insee.survey.datacollectionmanagement.questioning.repository.QuestioningRepository;
import fr.insee.survey.datacollectionmanagement.questioning.repository.SurveyUnitRepository;
import fr.insee.survey.datacollectionmanagement.view.domain.View;
import fr.insee.survey.datacollectionmanagement.view.repository.ViewRepository;

@Component
public class Dataloader {

    private static final Logger LOGGER = LogManager.getLogger(Dataloader.class);

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private SupportRepository supportRepository;

    @Autowired
    private SourceRepository sourceRepository;

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private SurveyUnitRepository surveyUnitRepository;

    @Autowired
    private QuestioningRepository questioningRepository;

    @Autowired
    private QuestioningAccreditationRepository questioningAccreditationRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private PartitioningRepository partitioningRepository;

    @Autowired
    private EventOrderRepository orderRepository;

    @Autowired
    private QuestioningEventRepository questioningEventRepository;

    @Autowired
    private MetadataCopyRepository metadataCopyRepository;

    @Autowired
    private AccreditationsCopyRepository accreditationsCopyRepository;
    
    @Autowired
    private ViewRepository viewRepository;

    @PostConstruct
    public void init() {

        Faker faker = new Faker();
        EasyRandom generator = new EasyRandom();

        initOrder();
        initContact(faker);
        initMetadata(faker, generator);
        initQuestionning(faker, generator);
        initMetadatacopy();
        initAccreditationsCopy();
        initView();

    }

    private void initOrder() {

        Long nbExistingOrders = orderRepository.count();
        LOGGER.info("{} orders in database", nbExistingOrders);

        if (nbExistingOrders == 0) {
            // Creating table order
            LOGGER.info("loading eventorder data");
            orderRepository.saveAndFlush(new EventOrder(Long.parseLong("8"), TypeQuestioningEvent.REFUSAL.toString(), 8));
            orderRepository.saveAndFlush(new EventOrder(Long.parseLong("7"), TypeQuestioningEvent.VALINT.toString(), 7));
            orderRepository.saveAndFlush(new EventOrder(Long.parseLong("6"), TypeQuestioningEvent.VALPAP.toString(), 6));
            orderRepository.saveAndFlush(new EventOrder(Long.parseLong("5"), TypeQuestioningEvent.HC.toString(), 5));
            orderRepository.saveAndFlush(new EventOrder(Long.parseLong("4"), TypeQuestioningEvent.PARTIELINT.toString(), 4));
            orderRepository.saveAndFlush(new EventOrder(Long.parseLong("3"), TypeQuestioningEvent.WASTE.toString(), 3));
            orderRepository.saveAndFlush(new EventOrder(Long.parseLong("2"), TypeQuestioningEvent.PND.toString(), 2));
            orderRepository.saveAndFlush(new EventOrder(Long.parseLong("1"), TypeQuestioningEvent.INITLA.toString(), 1));
        }
    }

    private void initContact(Faker faker) {

        List<Contact> listContact = new ArrayList<>();
        List<Address> listAddresses = new ArrayList<>();
        Long nbExistingContacts = contactRepository.count();

        LOGGER.info("{} contacts exist in database", nbExistingContacts);

        int nbContacts = 2000000;

        for (Long i = nbExistingContacts; i < nbContacts; i ++ ) {
            long start = System.currentTimeMillis();

            final Contact c = new Contact();
            final Address a = new Address();

            Name n = faker.name();
            String name = n.lastName();
            String firstName = n.firstName();
            com.github.javafaker.Address fakeAddress = faker.address();

            a.setCountryName(fakeAddress.country());
            a.setStreetNumber(fakeAddress.buildingNumber());
            a.setStreetName(fakeAddress.streetName());
            a.setZipCode(fakeAddress.zipCode());
            a.setCity(fakeAddress.cityName());
            // addressRepository.save(a);
            listAddresses.add(a);

            c.setIdentifier(RandomStringUtils.randomAlphanumeric(7).toUpperCase());
            c.setLastName(name);
            c.setFirstName(firstName);
            c.setPhone(faker.phoneNumber().phoneNumber());
            c.setGender(Gender.valueOf(faker.demographic().sex()));
            c.setFunction(faker.job().title());
            c.setComment(faker.beer().name());
            c.setEmail(firstName.toLowerCase() + "." + name.toLowerCase() + "@cocorico.fr");
            c.setAddress(a);
            listContact.add(c);
            // contactRepository.save(c);

            if ((i + 1) % 10000 == 0) {
                addressRepository.saveAll(listAddresses);
                contactRepository.saveAll(listContact);
                listAddresses = new ArrayList<>();
                listContact = new ArrayList<>();
                long end = System.currentTimeMillis();

                // LOGGER.info("It took {}ms to execute save() for 100 contacts.", (end - start));

                LOGGER.info("It took {}ms to execute saveAll() for 10000 contacts.", (end - start));
            }

        }
        // addressRepository.saveAll(listAddresses);
        // contactRepository.saveAll(listContact);
        // long end = System.currentTimeMillis();
        //
        // LOGGER.info("It took {}ms to execute save() for {} contacts.", (end - start), (nbContacts - nbExistingContacts));

        // LOGGER.info("It took {}ms to execute saveAll() for {} contacts.", (end - start), (nbContacts - nbExistingContacts));

    }

    private void initMetadata(Faker faker, EasyRandom generator2) {

        int year = 2022;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        Date dateEndOfYear = calendar.getTime();

        Owner ownerInsee = new Owner();
        ownerInsee.setLabel("Insee");
        Set<Source> setSourcesInsee = new HashSet<>();

        Owner ownerAgri = new Owner();
        ownerAgri.setLabel("SSM Agriculture");
        Set<Source> setSourcesSsp = new HashSet<>();

        Support supportInseeHdf = new Support();
        supportInseeHdf.setLabel("Insee Hauts-de-France");
        Set<Source> setSourcesSupportInsee = new HashSet<>();

        Support supportSsne = new Support();
        supportSsne.setLabel("Insee Normandie - SSNE");
        Set<Source> setSourcesSupportSsne = new HashSet<>();

        for (Long i = sourceRepository.count(); i < 10; i ++ ) {
            Source source = new Source();
            Animal animal = faker.animal();
            String animalName = animal.name().toUpperCase();
            source.setIdSource(animalName);
            source.setLongWording("Have you ever heard about " + animalName + " ?");
            source.setShortWording("Source about " + animalName);
            source.setPeriodicity("M");
            sourceRepository.save(source);
            Set<Survey> setSurveys = new HashSet<>();
            if (i % 2 == 0)
                setSourcesInsee.add(source);
            else {
                setSourcesSsp.add(source);
            }

            if (i % 3 == 0)
                setSourcesSupportInsee.add(source);
            else {
                setSourcesSupportSsne.add(source);
            }

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
                survey.setDiffusionUrl("http://diffusion/" + id);
                survey.setCnisUrl("http://cnis/" + id);
                survey.setNoticeUrl("http://notice/" + id);
                survey.setVisaNumber(year + RandomStringUtils.randomAlphanumeric(6).toUpperCase());
                survey.setLongWording("Survey " + animalName + " " + (year - j));
                survey.setShortWording(id);
                survey.setSampleSize(Integer.parseInt(RandomStringUtils.randomNumeric(5)));
                setSurveys.add(survey);
                surveyRepository.save(survey);
                Set<Campaign> setCampaigns = new HashSet<>();

                for (int k = 0; k < 12; k ++ ) {
                    Campaign campaign = new Campaign();
                    int month = k + 1;
                    String period = "M" + month;
                    campaign.setYear(year - j);
                    campaign.setPeriod(period);
                    campaign.setCampaignId(animalName + (year - j) + period);
                    campaign.setCampaignWording("Campaign about " + animalName + " in " + (year - j) + " and period " + period);
                    setCampaigns.add(campaign);
                    campaignRepository.save(campaign);
                    Set<Partitioning> setParts = new HashSet<>();

                    for (int l = 0; l < 3; l ++ ) {

                        Partitioning part = new Partitioning();
                        part.setId(animalName + (year - j) + "M" + month + "-00" + l);
                        Date openingDate = faker.date().past(90, 0, TimeUnit.DAYS);
                        Date closingDate = faker.date().between(openingDate, dateEndOfYear);
                        Date returnDate = faker.date().between(openingDate, closingDate);
                        Date today = new Date();

                        part.setOpeningDate(openingDate);
                        part.setClosingDate(closingDate);
                        part.setReturnDate(returnDate);
                        part.setStatus(today.compareTo(closingDate) < 0 ? "open" : "closed");
                        setParts.add(part);
                        part.setCampaign(campaign);
                        partitioningRepository.save(part);
                    }
                    campaign.setSurvey(survey);
                    campaign.setPartitionings(setParts);
                    campaignRepository.save(campaign);

                }
                survey.setSource(source);
                survey.setCampaigns(setCampaigns);
                surveyRepository.save(survey);
            }
            source.setSurveys(setSurveys);
            sourceRepository.save(source);

        }
        ownerInsee.setSources(setSourcesInsee);
        ownerAgri.setSources(setSourcesSsp);
        ownerRepository.saveAll(Arrays.asList(new Owner[] {
            ownerInsee, ownerAgri
        }));

        supportInseeHdf.setSources(setSourcesSupportInsee);
        supportSsne.setSources(setSourcesSupportSsne);
        supportRepository.saveAll(Arrays.asList(new Support[] {
            supportInseeHdf, supportSsne
        }));

    }

    private void initQuestionning(Faker faker, EasyRandom generator) {

        Long nbExistingQuestionings = questioningRepository.count();

        LOGGER.info("{} questionings exist in database", nbExistingQuestionings);

        long start = System.currentTimeMillis();
        SurveyUnit su;
        Questioning qu;
        QuestioningEvent qe;
        Set<Questioning> setQuestioning;
        QuestioningAccreditation accreditation;
        Set<QuestioningAccreditation> questioningAccreditations;
        String fakeSiren;
        Random qeRan = new Random();

        for (Long i = nbExistingQuestionings; i < 1000000; i ++ ) {
            su = new SurveyUnit();
            qu = new Questioning();
            qe = new QuestioningEvent();
            List<QuestioningEvent> qeList = new ArrayList<>();
            questioningAccreditations = new HashSet<>();

            fakeSiren = RandomStringUtils.randomNumeric(9);
            su.setIdSu(fakeSiren);
            su.setCompanyName(faker.company().name());
            su.setSurveyUnitId(fakeSiren);

            setQuestioning = new HashSet<>();
            qu.setModelName("m" + RandomStringUtils.randomNumeric(2));
            qu.setIdPartitioning(partitioningRepository.findRandomPartitioning().getId());
            surveyUnitRepository.save(su);
            qu.setSurveyUnit(su);
            questioningRepository.save(qu);
            setQuestioning.add(qu);
            su.setQuestionings(setQuestioning);

            // questioning events
            // everybody in INITLA
            Optional<Partitioning> part = partitioningRepository.findById(qu.getIdPartitioning());
            Date eventDate = faker.date().between(part.get().getOpeningDate(), part.get().getClosingDate());
            qe.setType(TypeQuestioningEvent.INITLA.toString());
            qe.setDate(eventDate);
            qe.setQuestioning(qu);
            qeList.add(qe);

            int qeProfile = qeRan.nextInt(10);

            switch (qeProfile) {
                case 0:
                    qeList.add(new QuestioningEvent(faker.date().between(part.get().getOpeningDate(), part.get().getClosingDate()),
                        TypeQuestioningEvent.REFUSAL.toString(), qu));
                break;
                case 1:
                    qeList.add(new QuestioningEvent(faker.date().between(part.get().getOpeningDate(), part.get().getClosingDate()),
                        TypeQuestioningEvent.PND.toString(), qu));
                break;
                case 2:
                    qeList.add(new QuestioningEvent(faker.date().between(part.get().getOpeningDate(), part.get().getClosingDate()),
                        TypeQuestioningEvent.FOLLOWUP.toString(), qu));
                    qeList.add(new QuestioningEvent(faker.date().between(part.get().getOpeningDate(), part.get().getClosingDate()),
                        TypeQuestioningEvent.FOLLOWUP.toString(), qu));
                    qeList.add(new QuestioningEvent(faker.date().between(part.get().getOpeningDate(), part.get().getClosingDate()),
                        TypeQuestioningEvent.PARTIELINT.toString(), qu));
                break;
                case 3:
                case 4:
                    qeList.add(new QuestioningEvent(faker.date().between(part.get().getOpeningDate(), part.get().getClosingDate()),
                        TypeQuestioningEvent.FOLLOWUP.toString(), qu));
                    qeList.add(new QuestioningEvent(faker.date().between(part.get().getOpeningDate(), part.get().getClosingDate()),
                        TypeQuestioningEvent.FOLLOWUP.toString(), qu));
                    qeList.add(new QuestioningEvent(faker.date().between(part.get().getOpeningDate(), part.get().getClosingDate()),
                        TypeQuestioningEvent.VALINT.toString(), qu));
                break;
                case 5:
                case 6:
                case 7:
                    qeList.add(new QuestioningEvent(faker.date().between(part.get().getOpeningDate(), part.get().getClosingDate()),
                        TypeQuestioningEvent.PARTIELINT.toString(), qu));
                    qeList.add(new QuestioningEvent(faker.date().between(part.get().getOpeningDate(), part.get().getClosingDate()),
                        TypeQuestioningEvent.VALINT.toString(), qu));
                break;
            }

            qeList.stream().forEach(questEvent -> questioningEventRepository.save(questEvent));

            for (int j = 0; j < 4; j ++ ) {
                accreditation = new QuestioningAccreditation();
                accreditation.setIdContact(contactRepository.findRandomIdentifierContact());
                accreditation.setQuestioning(qu);
                questioningAccreditations.add(accreditation);
                questioningAccreditationRepository.save(accreditation);
            }
            qu.setQuestioningAccreditations(questioningAccreditations);
            questioningRepository.save(qu);
            surveyUnitRepository.save(su);
            if (i % 100 == 0) {
                long end = System.currentTimeMillis();
                LOGGER.info("It took {}ms to execute save() for 100 questionings.", (end - start));
                start = System.currentTimeMillis();
            }

        }
    }

    private void initQuestioningEvents(Faker faker, EasyRandom generator) {

        Long nbExistingQuestioningEvents = questioningEventRepository.count();

        if (nbExistingQuestioningEvents == 0) {
            List<Questioning> listQu = questioningRepository.findAll();
            Random qeRan = new Random();

            for (Questioning qu : listQu) {

                QuestioningEvent qe = new QuestioningEvent();
                List<QuestioningEvent> qeList = new ArrayList<>();
                // questioning events
                // everybody in INITLA
                Optional<Partitioning> part = partitioningRepository.findById(qu.getIdPartitioning());
                Date eventDate = faker.date().between(part.get().getOpeningDate(), part.get().getClosingDate());
                qe.setType(TypeQuestioningEvent.INITLA.toString());
                qe.setDate(eventDate);
                qe.setQuestioning(qu);
                qeList.add(qe);

                int qeProfile = qeRan.nextInt(10);

                switch (qeProfile) {
                    case 0:
                        qeList.add(new QuestioningEvent(faker.date().between(part.get().getOpeningDate(), part.get().getClosingDate()),
                            TypeQuestioningEvent.REFUSAL.toString(), qu));
                    break;
                    case 1:
                        qeList.add(new QuestioningEvent(faker.date().between(part.get().getOpeningDate(), part.get().getClosingDate()),
                            TypeQuestioningEvent.PND.toString(), qu));
                    break;
                    case 2:
                        qeList.add(new QuestioningEvent(faker.date().between(part.get().getOpeningDate(), part.get().getClosingDate()),
                            TypeQuestioningEvent.FOLLOWUP.toString(), qu));
                        qeList.add(new QuestioningEvent(faker.date().between(part.get().getOpeningDate(), part.get().getClosingDate()),
                            TypeQuestioningEvent.FOLLOWUP.toString(), qu));
                        qeList.add(new QuestioningEvent(faker.date().between(part.get().getOpeningDate(), part.get().getClosingDate()),
                            TypeQuestioningEvent.PARTIELINT.toString(), qu));
                    break;
                    case 3:
                    case 4:
                        qeList.add(new QuestioningEvent(faker.date().between(part.get().getOpeningDate(), part.get().getClosingDate()),
                            TypeQuestioningEvent.FOLLOWUP.toString(), qu));
                        qeList.add(new QuestioningEvent(faker.date().between(part.get().getOpeningDate(), part.get().getClosingDate()),
                            TypeQuestioningEvent.FOLLOWUP.toString(), qu));
                        qeList.add(new QuestioningEvent(faker.date().between(part.get().getOpeningDate(), part.get().getClosingDate()),
                            TypeQuestioningEvent.VALINT.toString(), qu));
                    break;
                    case 5:
                    case 6:
                    case 7:
                        qeList.add(new QuestioningEvent(faker.date().between(part.get().getOpeningDate(), part.get().getClosingDate()),
                            TypeQuestioningEvent.PARTIELINT.toString(), qu));
                        qeList.add(new QuestioningEvent(faker.date().between(part.get().getOpeningDate(), part.get().getClosingDate()),
                            TypeQuestioningEvent.VALINT.toString(), qu));
                    break;
                }

                qeList.stream().forEach(questEvent -> questioningEventRepository.save(questEvent));

            }
        }

    }

    private void initMetadatacopy() {
        if (metadataCopyRepository.count() == 0) {
            List<Partitioning> listParts = partitioningRepository.findAll();

            listParts.stream().forEach(p -> {
                MetadataCopy mcc = new MetadataCopy();
                mcc.setIdPartitioning(p.getId());
                mcc.setIdSource(p.getCampaign().getSurvey().getSource().getIdSource());
                mcc.setYear(p.getCampaign().getSurvey().getYear());
                mcc.setPeriod(p.getCampaign().getPeriod());
                metadataCopyRepository.save(mcc);
            });
        }
    }

    private void initAccreditationsCopy() {
        if (accreditationsCopyRepository.count() == 0) {
            List<QuestioningAccreditation> listAccreditations = questioningAccreditationRepository.findAll();
            listAccreditations.stream().forEach(a -> {
                Partitioning p = partitioningRepository.findById(a.getQuestioning().getIdPartitioning()).orElse(null);
                AccreditationsCopy acc = new AccreditationsCopy();
                acc.setContact(contactRepository.findById(a.getIdContact()).orElse(null));
                acc.setSourceId(p.getCampaign().getSurvey().getSource().getIdSource());
                acc.setSurveyUnitId(a.getQuestioning().getSurveyUnit().getSurveyUnitId());
                acc.setIdSu(a.getQuestioning().getSurveyUnit().getIdSu());
                acc.setCompanyName(a.getQuestioning().getSurveyUnit().getCompanyName());
                acc.setYear(p.getCampaign().getSurvey().getYear());
                acc.setPeriod(p.getCampaign().getPeriod());
                accreditationsCopyRepository.save(acc);
            });
        }
    }
    
    private void initView() {
        if (viewRepository.count() == 0) {
            List<QuestioningAccreditation> listAccreditations = questioningAccreditationRepository.findAll();
            listAccreditations.stream().forEach(a -> {
                Partitioning p = partitioningRepository.findById(a.getQuestioning().getIdPartitioning()).orElse(null);
                View view = new View();
                view.setIdentifier(contactRepository.findById(a.getIdContact()).orElse(null).getIdentifier());
                view.setCampaignId(p.getCampaign().getCampaignId());
                view.setIdSu(a.getQuestioning().getSurveyUnit().getIdSu());
                viewRepository.save(view);
            });
        }
    }

}
