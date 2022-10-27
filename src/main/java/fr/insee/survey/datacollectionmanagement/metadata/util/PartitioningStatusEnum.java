package fr.insee.survey.datacollectionmanagement.metadata.util;

public enum PartitioningStatusEnum {

    OPEN("open"), CLOSED("closed"), FORTHCOMING("forthcoming"),INCOMPLETE_DATES("incomplete dates");

    final String value;
    private static final  PartitioningStatusEnum[] enums = PartitioningStatusEnum.values();

    PartitioningStatusEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public String getValue() {
        return value;
    }

    public static PartitioningStatusEnum fromValue(String v) {
        for (PartitioningStatusEnum c : enums) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }


}