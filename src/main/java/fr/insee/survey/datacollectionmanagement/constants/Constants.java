package fr.insee.survey.datacollectionmanagement.constants;

public class Constants {
    private Constants() {
        throw new IllegalStateException("Constants class");
    }

    // API CONTACTS DOMAIN
    public static final String API_CONTACTS = "/api/contacts/";
    public static final String API_CONTACTS_ALL = "/api/contacts";
    public static final String API_CONTACTS_ID = "/api/contacts/{id}";
    public static final String ADDRESS = "/address";
    public static final String CONTACT_EVENTS = "/contact-events";
    public static final String API_CONTACTS_ID_ADDRESS = "/api/contacts/{id}/address";
    public static final String API_CONTACTS_ID_CONTACTEVENTS = "/api/contacts/{id}/contact-events";
    public static final String API_CONTACTEVENTS = "/api/contacts/contact-events";
    public static final String API_CONTACTEVENTS_ID = "/api/contacts/contact-events/{id}";

    //API QUESTIONING DOMAIN
    public static final String API_QUESTIONINGS = "/api/questionings/";
    public static final String API_QUESTIONINGS_ID = "/api/questionings/{id}";
    public static final String API_SURVEY_UNITS ="api/survey-units";
    public static final String API_SURVEY_UNITS_ID = "/api/survey-units/{id}";
    public static final String API_QUESTIONING_ACCREDITATIONS = "/api/questioniong-accreditations";
    public static final String API_QUESTIONINGS_ID_QUESTIONING_ACCREDITATIONS_ID = "/api/questionings/{id}/questioniong-accreditations";
    
    // API METADATA DOMAIN

    // API CROSS DOMAIN
    public static final String API_CHECK_HABILITATION = "/api/check-habilitation";
    public static final String API_MOOG_SEARCH = "/api/moog/campaigns/survey-units";
    public static final String API_MOOG_EVENTS = "/api/moog/campaigns/{campaign}/survey-units/{id}/management-monitoring-infos";
    public static final String API_CONTACTS_SEARCH = "/api/contacts/search";
    public static final String API_CONTACTS_ACCREDITATIONS = "/api/contacts/{id}/accreditations";
}