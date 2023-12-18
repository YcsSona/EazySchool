package com.theonewhocode.eazyschool.service;

import com.theonewhocode.eazyschool.constants.EazySchoolConstansts;
import com.theonewhocode.eazyschool.model.Contact;
import com.theonewhocode.eazyschool.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    public boolean saveMessageDetails(Contact contact) {
        contact.setStatus(EazySchoolConstansts.OPEN);

        Contact savedContact = contactRepository.save(contact);
        return savedContact != null && savedContact.getContactId() > 0;
    }

    public Page<Contact> findMsgsWithOpenStatus(int pageNum, String sortField, String sortDir) {
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize,
                sortDir.equals("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());
        return contactRepository.findByStatus(EazySchoolConstansts.OPEN, pageable);
    }

    public boolean updateMsgStatus(int contactId) {
        Optional<Contact> optionalContact = contactRepository.findById(contactId);

        if (optionalContact.isPresent()) {
            Contact contact = optionalContact.get();
            contact.setStatus(EazySchoolConstansts.CLOSE);

            Contact updatedContact = contactRepository.save(contact);
            return updatedContact != null && updatedContact.getUpdatedBy() != null;
        }

        return false;
    }
}
