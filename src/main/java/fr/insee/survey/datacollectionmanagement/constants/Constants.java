package fr.insee.survey.datacollectionmanagement.constants;

public class Constants {
    private Constants() {
        throw new IllegalStateException("Constants class");
    }

    // API CONTACTS DOMAIN
    public static final String API_CONTACTS_ID = "/contacts/{id}";
    public static final String API_CONTACTS = "/contacts";
    public static final String API_CONTACTS_ID_ADDRESS = "/contacts/{id}/address";
    public static final String API_CONTACTS_ID_CONTACTEVENTS = "/contacts/{id}/contactEvents";
    public static final String API_CONTACTEVENTS = "/contactEvents";
    public static final String API_CONTACTEVENTS_ID = "/contactEvents/{id}";
    
    // API CONTACTS METADATA

    
 // API CROSS DOMAIN
    public static final String API_CHECK_ACCREDITATIONS_V2 ="/checkAccreditationV2";
    public static final String API_CHECK_ACCREDITATIONS_V3 ="/checkAccreditationV3";
}