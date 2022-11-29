package fr.insee.survey.datacollectionmanagement.metadata.util;

import lombok.Getter;

@Getter
public enum PeriodEnum {

    A00(PeriodicityEnum.A, "annual"),
    S01(PeriodicityEnum.S, "1st semester"),
    S02(PeriodicityEnum.S, "2nd semester"),
    T01(PeriodicityEnum.T, "1st trimester"),
    T02(PeriodicityEnum.T, "2nd trimester"),
    T03(PeriodicityEnum.T, "3rd trimester"),
    T04(PeriodicityEnum.T, "4th trimester"),
    M01(PeriodicityEnum.M, "january"),
    M02(PeriodicityEnum.M, "february"),
    M03(PeriodicityEnum.M, "march"),
    M04(PeriodicityEnum.M, "april"),
    M05(PeriodicityEnum.M, "may"),
    M06(PeriodicityEnum.M, "june"),
    M07(PeriodicityEnum.M, "july"),
    M08(PeriodicityEnum.M, "august"),
    M09(PeriodicityEnum.M, "september"),
    M10(PeriodicityEnum.M, "october"),
    M11(PeriodicityEnum.M, "november"),
    M12(PeriodicityEnum.M, "december"),
    B01(PeriodicityEnum.B, "1st bimester"),
    B02(PeriodicityEnum.B, "1st bimester"),
    B03(PeriodicityEnum.B, "1st bimester"),
    B04(PeriodicityEnum.B, "1st bimester"),

    ;

    PeriodEnum(PeriodicityEnum period, String value) {
        this.value = value;
        this.period = period;
    }

    final PeriodicityEnum period;

    final String value;
    
    public static PeriodEnum fromValue(String v) {
        for (PeriodEnum c : PeriodEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
