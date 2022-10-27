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

    //API QUESTIONING DOMAIN
    public static final String API_QUESTIONINGS = "/api/questionings";
    public static final String API_QUESTIONINGS_ID = "/api/questionings/{id}";
    public static final String API_SURVEY_UNITS ="/api/survey-units";
    public static final String API_SURVEY_UNITS_ID = "/api/survey-units/{id}";
    public static final String API_SURVEY_UNITS_ID_QUESTIONINGS = "/api/survey-units/{id}/questionings";
    public static final String API_QUESTIONING_ACCREDITATIONS = "/api/questioning-accreditations";
    public static final String API_QUESTIONINGS_ID_QUESTIONING_ACCREDITATIONS_ID = "/api/questionings/{id}/questioning-accreditations";
    public static final String API_QUESTIONING_QUESTIONING_EVENTS = "/api/questionings/questioning-events";
    public static final String API_QUESTIONING_ID_QUESTIONING_EVENTS = "/api/questionings/{id}/questioning-events";
    public static final String API_QUESTIONING_QUESTIONING_EVENTS_ID = "/api/questionings/questioning-events/{id}";

    // API METADATA DOMAIN
    public static final String API_SOURCES = "/api/sources";
    public static final String API_SOURCES_ID = "/api/sources/{id}";
    public static final String API_SOURCES_ID_SURVEYS = "/api/sources/{id}/surveys";
    public static final String API_SURVEYS = "/api/surveys";
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




    // API CROSS DOMAIN
    public static final String API_CHECK_ACCREDITATIONS = "/api/check-accreditations";
    public static final String API_MOOG_SEARCH = "/api/moog/campaigns/survey-units";
    public static final String API_MOOG_EVENTS = "/api/moog/campaigns/{campaign}/survey-units/{id}/management-monitoring-infos";
    public static final String API_CONTACTS_SEARCH = "/api/contacts/search";
    public static final String API_CONTACTS_ACCREDITATIONS = "/api/contacts/{id}/accreditations";
}