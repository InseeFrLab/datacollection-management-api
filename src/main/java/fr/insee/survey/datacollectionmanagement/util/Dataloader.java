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
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jeasy.random.EasyRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.github.javafaker.Animal;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;

import fr.insee.survey.datacollectionmanagement.contact.domain.Address;
import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;
import fr.insee.survey.datacollectionmanagement.contact.domain.Contact.Gender;
import fr.insee.survey.datacollectionmanagement.contact.domain.ContactEvent;
import fr.insee.survey.datacollectionmanagement.contact.domain.ContactEvent.ContactEventType;
import fr.insee.survey.datacollectionmanagement.contact.repository.AddressRepository;
import fr.insee.survey.datacollectionmanagement.contact.repository.ContactEventRepository;
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
import fr.insee.survey.datacollectionmanagement.questioning.domain.Operator;
import fr.insee.survey.datacollectionmanagement.questioning.domain.OperatorService;
import fr.insee.survey.datacollectionmanagement.questioning.domain.Questioning;
import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningAccreditation;
import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningEvent;
import fr.insee.survey.datacollectionmanagement.questioning.domain.SurveyUnit;
import fr.insee.survey.datacollectionmanagement.questioning.domain.SurveyUnitAddress;
import fr.insee.survey.datacollectionmanagement.questioning.domain.TypeQuestioningEvent;
import fr.insee.survey.datacollectionmanagement.questioning.repository.EventOrderRepository;
import fr.insee.survey.datacollectionmanagement.questioning.repository.OperatorRepository;
import fr.insee.survey.datacollectionmanagement.questioning.repository.OperatorServiceRepository;
import fr.insee.survey.datacollectionmanagement.questioning.repository.QuestioningAccreditationRepository;
import fr.insee.survey.datacollectionmanagement.questioning.repository.QuestioningEventRepository;
import fr.insee.survey.datacollectionmanagement.questioning.repository.QuestioningRepository;
import fr.insee.survey.datacollectionmanagement.questioning.repository.SurveyUnitAddressRepository;
import fr.insee.survey.datacollectionmanagement.questioning.repository.SurveyUnitRepository;
import fr.insee.survey.datacollectionmanagement.view.domain.View;
import fr.insee.survey.datacollectionmanagement.view.repository.ViewRepository;

@Component
@Profile("poc")
public class Dataloader {

    private static final Logger LOGGER = LogManager.getLogger(Dataloader.class);

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ContactEventRepository contactEventRepository;

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
    private SurveyUnitAddressRepository surveyUnitAddressRepository;

    @Autowired
    private OperatorServiceRepository operatorServiceRepository;

    @Autowired
    private OperatorRepository operatorRepository;

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
    private ViewRepository viewRepository;

    @PostConstruct
    public void init() {

        Faker faker = new Faker();
        EasyRandom generator = new EasyRandom();

        // initOrder();
        // initContact(faker);
        // initMetadata(faker, generator);
        // initQuestionning(faker, generator);
        // initView();
        initSurveyUnitAddressAndOperators(faker);

    }

    private void initSurveyUnitAddressAndOperators(Faker faker) {
        if (surveyUnitAddressRepository.count() == 0) {
            for (SurveyUnit su : surveyUnitRepository.findAll()) {
                SurveyUnitAddress a = new SurveyUnitAddress();
                com.github.javafaker.Address fakeAddress = faker.address();

                a.setCountryName(fakeAddress.country());
                a.setStreetNumber(fakeAddress.buildingNumber());
                a.setStreetName(fakeAddress.streetName());
                a.setZipCode(fakeAddress.zipCode());
                a.setCity(fakeAddress.cityName());
                su.setSurveyUnitAddress(a);
                surveyUnitRepository.save(su);
            }
        }

        if (operatorServiceRepository.count() == 0) {
            Set<Operator> setOpConj = new HashSet<>();
            OperatorService operatorServiceConj = new OperatorService();
            operatorServiceConj.setName("Conjoncture");
            operatorServiceConj.setMail("conjoncture@Cocorico.fr");
            for (int i = 0; i < 9; i ++ ) {
                setOpConj.add(createOperator(faker));
            }
            operatorServiceConj.setOperators(setOpConj);
            operatorServiceRepository.save(operatorServiceConj);

            Set<Operator> setOpLogement = new HashSet<>();
            OperatorService operatorServiceLogement = new OperatorService();
            operatorServiceLogement.setName("Logement");
            operatorServiceLogement.setMail("logement@Cocorico.fr");
            operatorServiceRepository.save(operatorServiceLogement);
            for (int i = 0; i < 9; i ++ ) {
                setOpLogement.add(createOperator(faker));
            }
            operatorServiceLogement.setOperators(setOpLogement);
            operatorServiceRepository.save(operatorServiceLogement);

            Set<Operator> setOpEmploi = new HashSet<>();
            OperatorService operatorServiceEmploi = new OperatorService();
            operatorServiceEmploi.setName("Emploi");
            operatorServiceEmploi.setMail("emploi@Cocorico.fr");
            operatorServiceRepository.save(operatorServiceEmploi);
            for (int i = 0; i < 9; i ++ ) {
                setOpEmploi.add(createOperator(faker));
            }
            operatorServiceEmploi.setOperators(setOpEmploi);
            operatorServiceRepository.save(operatorServiceEmploi);

            Set<Operator> setOpPrix = new HashSet<>();
            OperatorService operatorServicePrix = new OperatorService();
            operatorServicePrix.setName("Prix");
            operatorServicePrix.setMail("prix@Cocorico.fr");
            operatorServiceRepository.save(operatorServicePrix);
            for (int i = 0; i < 9; i ++ ) {
                setOpPrix.add(createOperator(faker));
            }
            operatorServicePrix.setOperators(setOpPrix);
            operatorServiceRepository.save(operatorServicePrix);
        }

        if (operatorServiceRepository.count() == 0) {
            for (SurveyUnit su : surveyUnitRepository.findAll()) {
                SurveyUnitAddress a = new SurveyUnitAddress();
                com.github.javafaker.Address fakeAddress = faker.address();

                a.setCountryName(fakeAddress.country());
                a.setStreetNumber(fakeAddress.buildingNumber());
                a.setStreetName(fakeAddress.streetName());
                a.setZipCode(fakeAddress.zipCode());
                a.setCity(fakeAddress.cityName());
                su.setSurveyUnitAddress(a);
                surveyUnitRepository.save(su);
            }
        }

    }

    private Operator createOperator(Faker faker) {
        Operator operator = new Operator();
        Name n = faker.name();
        String name = n.lastName();
        String firstName = n.firstName();
        operator.setLastName(name);
        operator.setFirstName(firstName);
        operator.setPhoneNumber(faker.phoneNumber().phoneNumber());
        return operatorRepository.save(operator);
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

        Long nbContactEvents = contactEventRepository.count();

        for (Long j = nbContactEvents; j < 300; j ++ ) {
            Contact contact = contactRepository.findRandomContact();
            ContactEvent contactEvent = new ContactEvent();
            contactEvent.setType(ContactEventType.create);
            contactEvent.setEventDate(new Date());
            contactEvent.setContact(contact);
            contactEventRepository.save(contactEvent);
            Set<ContactEvent> setContactEvents = new HashSet<>();
            setContactEvents.add(contactEvent);
            contact.setContactEvents(setContactEvents);
            contactRepository.save(contact);
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

        LOGGER.info("{} campaigns exist in database", campaignRepository.count());

        while (sourceRepository.count() < 10) {

            Source source = new Source();
            Animal animal = faker.animal();
            String animalName = StringUtils.trim(animal.name().toUpperCase());
            if ( !StringUtils.contains(animalName, " ") && sourceRepository.findById(animalName).isEmpty()) {

                source.setId(animalName);
                source.setLongWording("Have you ever heard about " + animalName + " ?");
                source.setShortWording("Source about " + animalName);
                source.setPeriodicity("M");
                sourceRepository.save(source);
                Set<Survey> setSurveys = new HashSet<>();
                Integer i = new Random().nextInt();
                if (i % 2 == 0)
                    setSourcesInsee.add(source);
                else {
                    setSourcesSsp.add(source);
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
                        campaign.setId(animalName + (year - j) + period);
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

        }

    }

    private void initQuestionning(Faker faker, EasyRandom generator) {

        Long nbExistingQuestionings = questioningRepository.count();

        LOGGER.info("{} questionings exist in database", nbExistingQuestionings);

        long start = System.currentTimeMillis();
        Questioning qu;
        QuestioningEvent qe;
        Set<Questioning> setQuestioning;
        QuestioningAccreditation accreditation;
        Set<QuestioningAccreditation> questioningAccreditations;
        String fakeSiren;
        Random qeRan = new Random();

        LOGGER.info("{} survey units exist in database", surveyUnitRepository.count());

        for (Long i = surveyUnitRepository.count(); i < 1000000; i ++ ) {
            SurveyUnit su = new SurveyUnit();
            fakeSiren = RandomStringUtils.randomNumeric(9);

            su.setIdSu(fakeSiren);
            su.setIdentificationName(faker.company().name());
            su.setIdentificationCode(fakeSiren);
            surveyUnitRepository.save(su);

        }
        for (Long i = nbExistingQuestionings; i < 1000000; i ++ ) {
            qu = new Questioning();
            qe = new QuestioningEvent();
            List<QuestioningEvent> qeList = new ArrayList<>();
            questioningAccreditations = new HashSet<>();

            setQuestioning = new HashSet<>();
            qu.setModelName("m" + RandomStringUtils.randomNumeric(2));
            qu.setIdPartitioning(partitioningRepository.findRandomPartitioning().getId());
            SurveyUnit su = surveyUnitRepository.findRandomSurveyUnit();
            qu.setSurveyUnit(su);
            questioningRepository.save(qu);
            setQuestioning.add(qu);
            su.setQuestionings(setQuestioning);

            // questioning events
            // everybody in INITLA
            Optional<Partitioning> part = partitioningRepository.findById(qu.getIdPartitioning());
            Date eventDate = faker.date().between(part.get().getOpeningDate(), part.get().getClosingDate());
            qe.setType(TypeQuestioningEvent.INITLA);
            qe.setDate(eventDate);
            qe.setQuestioning(qu);
            qeList.add(qe);

            int qeProfile = qeRan.nextInt(10);

            switch (qeProfile) {
                case 0:
                    qeList.add(new QuestioningEvent(faker.date().between(part.get().getOpeningDate(), part.get().getClosingDate()),
                        TypeQuestioningEvent.REFUSAL, qu));
                break;
                case 1:
                    qeList.add(new QuestioningEvent(faker.date().between(part.get().getOpeningDate(), part.get().getClosingDate()),
                        TypeQuestioningEvent.PND, qu));
                break;
                case 2:
                    qeList.add(new QuestioningEvent(faker.date().between(part.get().getOpeningDate(), part.get().getClosingDate()),
                        TypeQuestioningEvent.FOLLOWUP, qu));
                    qeList.add(new QuestioningEvent(faker.date().between(part.get().getOpeningDate(), part.get().getClosingDate()),
                        TypeQuestioningEvent.FOLLOWUP, qu));
                    qeList.add(new QuestioningEvent(faker.date().between(part.get().getOpeningDate(), part.get().getClosingDate()),
                        TypeQuestioningEvent.PARTIELINT, qu));
                break;
                case 3:
                case 4:
                    qeList.add(new QuestioningEvent(faker.date().between(part.get().getOpeningDate(), part.get().getClosingDate()),
                        TypeQuestioningEvent.FOLLOWUP, qu));
                    qeList.add(new QuestioningEvent(faker.date().between(part.get().getOpeningDate(), part.get().getClosingDate()),
                        TypeQuestioningEvent.FOLLOWUP, qu));
                    qeList.add(new QuestioningEvent(faker.date().between(part.get().getOpeningDate(), part.get().getClosingDate()),
                        TypeQuestioningEvent.VALINT, qu));
                break;
                case 5:
                case 6:
                case 7:
                    qeList.add(new QuestioningEvent(faker.date().between(part.get().getOpeningDate(), part.get().getClosingDate()),
                        TypeQuestioningEvent.PARTIELINT, qu));
                    qeList.add(new QuestioningEvent(faker.date().between(part.get().getOpeningDate(), part.get().getClosingDate()),
                        TypeQuestioningEvent.VALINT, qu));
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
                qe.setType(TypeQuestioningEvent.INITLA);
                qe.setDate(eventDate);
                qe.setQuestioning(qu);
                qeList.add(qe);

                int qeProfile = qeRan.nextInt(10);

                switch (qeProfile) {
                    case 0:
                        qeList.add(new QuestioningEvent(faker.date().between(part.get().getOpeningDate(), part.get().getClosingDate()),
                            TypeQuestioningEvent.REFUSAL, qu));
                    break;
                    case 1:
                        qeList.add(new QuestioningEvent(faker.date().between(part.get().getOpeningDate(), part.get().getClosingDate()),
                            TypeQuestioningEvent.PND, qu));
                    break;
                    case 2:
                        qeList.add(new QuestioningEvent(faker.date().between(part.get().getOpeningDate(), part.get().getClosingDate()),
                            TypeQuestioningEvent.FOLLOWUP, qu));
                        qeList.add(new QuestioningEvent(faker.date().between(part.get().getOpeningDate(), part.get().getClosingDate()),
                            TypeQuestioningEvent.FOLLOWUP, qu));
                        qeList.add(new QuestioningEvent(faker.date().between(part.get().getOpeningDate(), part.get().getClosingDate()),
                            TypeQuestioningEvent.PARTIELINT, qu));
                    break;
                    case 3:
                    case 4:
                        qeList.add(new QuestioningEvent(faker.date().between(part.get().getOpeningDate(), part.get().getClosingDate()),
                            TypeQuestioningEvent.FOLLOWUP, qu));
                        qeList.add(new QuestioningEvent(faker.date().between(part.get().getOpeningDate(), part.get().getClosingDate()),
                            TypeQuestioningEvent.FOLLOWUP, qu));
                        qeList.add(new QuestioningEvent(faker.date().between(part.get().getOpeningDate(), part.get().getClosingDate()),
                            TypeQuestioningEvent.VALINT, qu));
                    break;
                    case 5:
                    case 6:
                    case 7:
                        qeList.add(new QuestioningEvent(faker.date().between(part.get().getOpeningDate(), part.get().getClosingDate()),
                            TypeQuestioningEvent.PARTIELINT, qu));
                        qeList.add(new QuestioningEvent(faker.date().between(part.get().getOpeningDate(), part.get().getClosingDate()),
                            TypeQuestioningEvent.VALINT, qu));
                    break;
                }

                qeList.stream().forEach(questEvent -> questioningEventRepository.save(questEvent));

            }
        }

    }

    private void initView() {
        if (viewRepository.count() == 0) {

            List<QuestioningAccreditation> listAccreditations = questioningAccreditationRepository.findAll();
            listAccreditations.stream().forEach(a -> {
                Partitioning p = partitioningRepository.findById(a.getQuestioning().getIdPartitioning()).orElse(null);
                View view = new View();
                view.setIdentifier(contactRepository.findById(a.getIdContact()).orElse(null).getIdentifier());
                view.setCampaignId(p.getCampaign().getId());
                view.setIdSu(a.getQuestioning().getSurveyUnit().getIdSu());
                viewRepository.save(view);
            });

            Iterable<Contact> listContacts = contactRepository.findAll();
            for (Contact contact : listContacts) {
                if (viewRepository.findByIdentifier(contact.getIdentifier()).isEmpty()) {
                    View view = new View();
                    view.setIdentifier(contact.getIdentifier());
                    viewRepository.save(view);

                }
            }
        }
    }

}
