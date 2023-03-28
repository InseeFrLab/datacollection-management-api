package fr.insee.survey.datacollectionmanagement.exception;

public class RessourceNotValidatedException extends Exception {

    private static final long serialVersionUID = -2348059622111312475L;

    public RessourceNotValidatedException(String ressource, String id) {
        super(String.format("No '%s' for value : '%s'", ressource, id));
    }

}
