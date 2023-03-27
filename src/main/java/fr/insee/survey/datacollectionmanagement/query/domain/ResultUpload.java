package fr.insee.survey.datacollectionmanagement.query.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ResultUpload {


    @JsonProperty("OK")
    private List<ResultUploadValidInfo> listIdOK;

    @JsonProperty("KO")
    private List<ResultUploadErrorInfo> listIdKO;

    public ResultUpload() {
        listIdOK = new ArrayList<>();
        listIdKO = new ArrayList<>();
    }

    public void addIdOk(String idOk) {
        ResultUploadValidInfo validInfo = new ResultUploadValidInfo(idOk);
        listIdOK.add(validInfo);
    }

    public void addIdKo(String idKo, String errorType) {
        ResultUploadErrorInfo errorInfo = new ResultUploadErrorInfo(idKo, errorType);
        listIdKO.add(errorInfo);
    }

    public List<ResultUploadValidInfo> getListIdOK() {
        return listIdOK;
    }

    public List<ResultUploadErrorInfo> getListIdKO() {
        return listIdKO;
    }

    public String toString() {
        return listIdOK.toString() + listIdKO.toString();
    }

    public boolean equals(Object objectToCompare) {
        if (objectToCompare == null || objectToCompare.getClass() != ResultUpload.class) {
            return false;
        }
        ResultUpload toCompare = (ResultUpload) objectToCompare;

        return (this.toString().equals(toCompare.toString()));
    }

    public class ResultUploadValidInfo {

        public ResultUploadValidInfo(String idOk) {
            this.setId(idOk);
        }

        @JsonProperty("id")
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

    }

    public class ResultUploadErrorInfo {

        public ResultUploadErrorInfo(String idKo, String errorType) {
            this.setId(idKo);
            this.setError(errorType);
        }

        @JsonProperty("id")
        private String id;

        @JsonProperty("error")
        private String error;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

    }
}
