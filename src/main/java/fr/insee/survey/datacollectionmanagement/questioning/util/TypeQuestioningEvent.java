package fr.insee.survey.datacollectionmanagement.questioning.util;

public enum TypeQuestioningEvent {
    INITLA("notice letter"),
    FOLLOWUP("followup letter"),
    PND("undelivered letter"),
    WASTE("waste"),
    PARTIELINT("partially online"),
    HC("out of scope"),
    VALPAP("validated by paper"),
    VALINT("validated online"),
    REFUSAL("refusal");

    private final String value;
    private static final TypeQuestioningEvent[] enums = TypeQuestioningEvent.values();

    TypeQuestioningEvent(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public String getValue() {
        return value;
    }

    public static TypeQuestioningEvent fromValue(String v) {
        for (TypeQuestioningEvent c : enums) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
