package fr.insee.survey.datacollectionmanagement.config;

import java.util.Collection;

public class JSONCollectionWrapper<T> {

    private Collection<T> datas;

    public Collection<T> getDatas() {
        return datas;
    }

    public void setDatas(Collection<T> datas) {
        this.datas = datas;
    }

    public JSONCollectionWrapper(Collection<T> datas) {
        super();
        this.datas = datas;
    }
}
