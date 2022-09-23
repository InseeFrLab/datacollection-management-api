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

    // API CONTACTS METADATA

    // API CROSS DOMAIN
    public static final String API_CHECK_ACCREDITATIONS_V2 = "/checkAccreditationV2";
    public static final String API_CHECK_ACCREDITATIONS_V3 = "/checkAccreditationV3";
}