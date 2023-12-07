package com.theonewhocode.eazyschool.service;

import com.theonewhocode.eazyschool.constants.EazySchoolConstansts;
import com.theonewhocode.eazyschool.model.Contact;
import com.theonewhocode.eazyschool.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    public boolean saveMessageDetails(Contact contact) {
        contact.setStatus(EazySchoolConstansts.OPEN);
        contact.setCreatedBy(EazySchoolConstansts.ANONYMOUS);
        contact.setCreatedAt(LocalDateTime.now());

        int result = contactRepository.saveContactMsg(contact);
        return result > 0;
    }

    public List<Contact> findMsgsWithOpenStatus() {
        return contactRepository.findMsgsWithStatus(EazySchoolConstansts.OPEN);
    }

    public boolean updateMsgStatus(int contactId, String updatedBy) {
        int result = contactRepository.updateMsgStatus(contactId, EazySchoolConstansts.CLOSE, updatedBy);
        return result > 0;
    }
}
