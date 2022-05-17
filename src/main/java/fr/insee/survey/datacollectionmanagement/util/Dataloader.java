package fr.insee.survey.datacollectionmanagement.util;


import fr.insee.survey.datacollectionmanagement.contact.domain.Address;
import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;
import fr.insee.survey.datacollectionmanagement.contact.repository.AddressRepository;
import fr.insee.survey.datacollectionmanagement.contact.repository.ContactEventRepository;
import fr.insee.survey.datacollectionmanagement.contact.repository.ContactRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Dataloader {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ContactEventRepository contactEventRepository;

    @PostConstruct
    public void init() {
        for (int i = 0; i < 10000; i++) {
            final Contact c = new Contact();
            final Address a = new Address();

            a.setCountryName("country-"+ RandomStringUtils.randomAlphabetic(4));
            a.setStreetNumber(15);
            a.setStreetName("streetname-"+ RandomStringUtils.randomAlphabetic(4));
            addressRepository.save(a);

            c.setIdentifier("id-"+RandomStringUtils.randomAlphabetic(4));
            c.setName("name-"+RandomStringUtils.randomAlphabetic(4));
            c.setFunction("function-"+RandomStringUtils.randomAlphabetic(4));
            c.setPhone("phone-"+RandomStringUtils.randomAlphabetic(4));
            c.setZipCode("zipcode-"+RandomStringUtils.randomAlphabetic(4));
            c.setGender(Contact.Gender.male);
            c.setComment("comment-"+RandomStringUtils.randomAlphabetic(4));
            c.setEmail("email-"+RandomStringUtils.randomAlphabetic(4));
            c.setAddress(a);
            contactRepository.save(c);
        }
    }
}
