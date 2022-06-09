package fr.insee.survey.datacollectionmanagement.query.dto;

import java.io.Serializable;

public class MoogProgressDto implements Serializable {
        private int nbSu;
        private int batchNumber;
        private int nbIntReceived;
        private int nbPapReceived;
        private int nbPND;
        private int nbHC;
        private int nbRefusal;
        private int nbOtherWastes;
        private int nbIntPart;

        public MoogProgressDto(int batchNumber) {
            this.batchNumber = batchNumber;
            this.nbIntReceived = 0;
            this.nbPapReceived = 0;
            this.nbPND = 0;
            this.nbHC = 0;
            this.nbRefusal = 0;
            this.nbOtherWastes = 0;
            this.nbIntPart = 0;
        }

        public void intReceived() {
            nbIntReceived++;
        }

        public void papReceived() {
            nbPapReceived++;
        }

        public void pnd() {
            nbPND++;
        }

        public void hc() {
            nbHC++;
        }

        public void refusal() {
            nbRefusal++;
        }

        public void otherWastes() {
            nbOtherWastes++;
        }

        public void intPart() {
            nbIntPart++;
        }

        public int getNbSu() {
            return nbSu;
        }

        public void setNbSu(int nbSu) {
            this.nbSu = nbSu;
        }

        public int getBatchNumber() {
            return batchNumber;
        }

        public void setBatchNumber(int batchNumber) {
            this.batchNumber = batchNumber;
        }

        public int getNbIntReceived() {
            return nbIntReceived;
        }

        public void setNbIntReceived(int nbIntReceived) {
            this.nbIntReceived = nbIntReceived;
        }

        public int getNbPapReceived() {
            return nbPapReceived;
        }

        public void setNbPapReceived(int nbPapReceived) {
            this.nbPapReceived = nbPapReceived;
        }

        public int getNbPND() {
            return nbPND;
        }

        public void setNbPND(int nbPND) {
            this.nbPND = nbPND;
        }

        public int getNbHC() {
            return nbHC;
        }

        public void setNbHC(int nbHC) {
            this.nbHC = nbHC;
        }

        public int getNbRefusal() {
            return nbRefusal;
        }

        public void setNbRefusal(int nbRefusal) {
            this.nbRefusal = nbRefusal;
        }

        public int getNbOtherWastes() {
            return nbOtherWastes;
        }

        public void setNbOtherWastes(int nbOtherWastes) {
            this.nbOtherWastes = nbOtherWastes;
        }

        public int getNbIntPart() {
            return nbIntPart;
        }

        public void setNbIntPart(int nbIntPart) {
            this.nbIntPart = nbIntPart;
        }

}
