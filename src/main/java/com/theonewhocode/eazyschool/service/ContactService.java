package com.theonewhocode.eazyschool.service;

import com.theonewhocode.eazyschool.constants.EazySchoolConstansts;
import com.theonewhocode.eazyschool.model.Contact;
import com.theonewhocode.eazyschool.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    public boolean saveMessageDetails(Contact contact) {
        contact.setStatus(EazySchoolConstansts.OPEN);
        contact.setCreatedBy(EazySchoolConstansts.ANONYMOUS);
        contact.setCreatedAt(LocalDateTime.now());

        Contact savedContact = contactRepository.save(contact);
        return savedContact != null && savedContact.getContactId() > 0;
    }

    public List<Contact> findMsgsWithOpenStatus() {
        return contactRepository.findByStatus(EazySchoolConstansts.OPEN);
    }

    public boolean updateMsgStatus(int contactId, String updatedBy) {
        Optional<Contact> optionalContact = contactRepository.findById(contactId);

        if (optionalContact.isPresent()) {
            Contact contact = optionalContact.get();
            contact.setStatus(EazySchoolConstansts.CLOSE);
            contact.setUpdatedBy(updatedBy);
            contact.setUpdatedAt(LocalDateTime.now());

            Contact updatedContact = contactRepository.save(contact);
            return updatedContact != null && updatedContact.getUpdatedBy() != null;
        }

        return false;
    }
}
