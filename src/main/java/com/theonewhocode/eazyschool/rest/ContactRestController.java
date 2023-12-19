package com.theonewhocode.eazyschool.rest;

import com.theonewhocode.eazyschool.model.Contact;
import com.theonewhocode.eazyschool.model.Response;
import com.theonewhocode.eazyschool.repository.ContactRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/contact")
public class ContactRestController {

    @Autowired
    private ContactRepository contactRepository;

    @GetMapping("/getMessagesByStatus")
    public List<Contact> getMessagesByStatus(@RequestParam String status) {
        return contactRepository.findByStatus(status);
    }

    @GetMapping("/getAllMsgsByStatus")
    public List<Contact> getAllMsgsByStatus(@RequestBody Contact contact) {
        if (contact != null && contact.getStatus() != null) {
            return contactRepository.findByStatus(contact.getStatus());
        }
        return List.of();
    }

    @PostMapping("/saveMsg")
    public ResponseEntity<?> saveMsg(@RequestHeader String invocationFrom,
                                     @Valid @RequestBody Contact contact) {
        log.info(String.format("Header invocationFrom = %s", invocationFrom));
        contactRepository.save(contact);

        Response response = new Response("200", "Message saved successfully!");
        return ResponseEntity.status(HttpStatus.CREATED).header("isMsgSaved", "true").body(response);
    }

    @DeleteMapping("/deleteMsg")
    public ResponseEntity<?> deleteMsg(RequestEntity<Contact> requestEntity) {
        HttpHeaders headers = requestEntity.getHeaders();
        headers.forEach((key, value) -> {
            log.info(String.format("Header '%s' = %s", key, String.join("|", value)));
        });

        Contact contact = requestEntity.getBody();
        if (contact != null) {
            contactRepository.deleteById(contact.getContactId());
        }

        Response response = new Response("200", "Message deleted successfully!");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
