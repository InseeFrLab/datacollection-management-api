package fr.insee.survey.datacollectionmanagement.constants;

public class Constants {

    private Constants() {
        throw new IllegalStateException("Constants class");
    }

    // API CONTACTS DOMAIN
    public static final String API_CONTACTS = "/api/contacts/";
    public static final String API_CONTACTS_ALL = "/api/contacts";
    public static final String API_CONTACTS_ID = "/api/contacts/{id}";
    public static final String ADDRESS = "/api/address";
    public static final String CONTACT_EVENTS = "/api/contact-events";
    public static final String API_CONTACTS_ID_ADDRESS = "/api/contacts/{id}/address";
    public static final String API_CONTACTS_ID_CONTACTEVENTS = "/api/contacts/{id}/contact-events";
    public static final String API_CONTACTEVENTS = "/api/contacts/contact-events";
    public static final String API_CONTACTEVENTS_ID = "/api/contacts/contact-events/{id}";

    // API USER DOMAIN

    public static final String API_USERS_ALL = "/api/users";
    public static final String API_USERS_ID = "/api/users/{id}";
    public static final String API_USERS_ID_USEREVENTS = "/api/users/{id}/user-events";
    public static final String API_USEREVENTS = "/api/users/contact-events";
    public static final String API_USEREVENTS_ID = "/api/users/user-events/{id}";
    public static final String API_SOURCE_ID_SOURCE_ACCREDITATIONS = "/api/source/{id}/source-accreditations";
    public static final String API_USERS_ID_SOURCES = "/api/users/{id}/accredited-sources";

    //API QUESTIONING DOMAIN
    public static final String API_QUESTIONINGS = "/api/questionings";
    public static final String API_QUESTIONINGS_ID = "/api/questionings/{id}";
    public static final String API_SURVEY_UNITS ="/api/survey-units";
    public static final String API_SURVEY_UNITS_ID = "/api/survey-units/{id}";
    public static final String API_SURVEY_UNITS_ID_QUESTIONINGS = "/api/survey-units/{id}/questionings";
    public static final String API_QUESTIONING_ACCREDITATIONS = "/api/questioning-accreditations";
    public static final String API_QUESTIONINGS_ID_QUESTIONING_ACCREDITATIONS = "/api/questionings/{id}/questioning-accreditations";
    public static final String API_QUESTIONING_QUESTIONING_EVENTS = "/api/questionings/questioning-events";
    public static final String API_QUESTIONING_ID_QUESTIONING_EVENTS = "/api/questionings/{id}/questioning-events";
    public static final String API_QUESTIONING_QUESTIONING_EVENTS_ID = "/api/questionings/questioning-events/{id}";
    public static final String API_MAIN_CONTACT = "/api/main-contact";


    // API METADATA DOMAIN
    public static final String API_SOURCES = "/api/sources";
    public static final String API_SOURCES_ID = "/api/sources/{id}";
    public static final String API_SOURCES_ID_SURVEYS = "/api/sources/{id}/surveys";
    public static final String API_SURVEYS_ID = "/api/surveys/{id}";
    public static final String API_SURVEYS_ID_CAMPAIGNS = "/api/surveys/{id}/campaigns";
    public static final String API_CAMPAIGNS = "/api/campaigns";
    public static final String API_CAMPAIGNS_ID = "/api/campaigns/{id}";
    public static final String API_CAMPAIGNS_ID_PARTITIONINGS = "/api/campaigns/{id}/partitionings";
    public static final String API_PARTITIONINGS = "/api/partitionings";
    public static final String API_PARTITIONINGS_ID = "/api/partitionings/{id}";
    public static final String API_METADATA_ID = "/api/metadata/{id}";
    public static final String API_OWNERS = "/api/owners";
    public static final String API_OWNERS_ID = "/api/owners/{id}";
    public static final String API_OWNERS_ID_SOURCES = "/api/owners/{id}/sources";
    public static final String API_SUPPORTS = "/api/supports";
    public static final String API_SUPPORTS_ID = "/api/supports/{id}";
    public static final String API_PERIODICITIES = "/api/periodicities";
    public static final String API_PERIODICITIES_ID_PERIODS = "/api/periodicities/{id}/periods";
    public static final String API_PERIODS = "/api/periods";





    // API CROSS DOMAIN
    public static final String API_CHECK_HABILITATION = "/api/check-habilitation";
    public static final String MOOG_API_CAMPAIGNS = "/api/moog/campaigns";
    public static final String MOOG_API_CAMPAIGNS_ID = "/api/moog/campaigns/{id}";
    public static final String API_MOOG_SEARCH = "/api/moog/campaigns/survey-units";
    public static final String API_MOOG_EVENTS = "/api/moog/campaigns/{campaign}/survey-units/{id}/management-monitoring-infos";
    public static final String API_MOOG_MAIL = "/api/moog/contact/{id}/mail";
    public static final String API_CONTACTS_SEARCH = "/api/contacts/search";
    public static final String API_CONTACTS_ACCREDITATIONS = "/api/contacts/{id}/accreditations";
    public static final String API_MY_QUESTIONINGS_ID = "/api/contacts/questionings";
    
    // API WEBCLIENT
    public static final String API_WEBCLIENT_FOLLOWUP = "/api/partitionings/{idPartitioning}/survey-units/{idSu}/follow-up";
    public static final String API_WEBCLIENT_EXTRACT = "/api/partitionings/{idPartitioning}/survey-units/{idSu}/extract";
    public static final String API_WEBCLIENT_STATE = "/api/partitionings/{idPartitioning}/survey-units/{idSu}/state";
    public static final String API_WEBCLIENT_METADATA = "/api/metadata";
    public static final String API_WEBCLIENT_METADATA_ID = "/api/metadata/{id}";
    public static final String API_WEBCLIENT_QUESTIONINGS = "/api/questionings";

    // CHECK HABILITATION ROLES

    public static final String INTERVIEWER = "interviewer";
    public static final String REVIEWER = "reviewer";

}