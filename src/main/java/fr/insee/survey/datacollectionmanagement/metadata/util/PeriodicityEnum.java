package fr.insee.survey.datacollectionmanagement.metadata.util;

import lombok.Getter;

@Getter
public enum PeriodicityEnum {

    X("pluriannual"), A("annual"), S("semi-annual"), T("trimestrial"), B("bimonthly"),
    M("monthly");

    PeriodicityEnum(String value) {
        this.value = value;
    }

    final String value;

}
