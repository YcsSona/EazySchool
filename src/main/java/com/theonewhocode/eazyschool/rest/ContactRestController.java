package com.theonewhocode.eazyschool.rest;

import com.theonewhocode.eazyschool.constants.EazySchoolConstansts;
import com.theonewhocode.eazyschool.model.Contact;
import com.theonewhocode.eazyschool.model.Response;
import com.theonewhocode.eazyschool.repository.ContactRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(path = "/api/contact", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
@CrossOrigin(origins = "*")
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

    @PatchMapping("/closeMsg") // partial update
    public ResponseEntity<?> closeMsg(@RequestBody Contact contact) {
        Response response = new Response();

        Optional<Contact> contactOptional = contactRepository.findById(contact.getContactId());
        if (contactOptional.isPresent()) {
            contactOptional.get().setStatus(EazySchoolConstansts.CLOSE);
            contactRepository.save(contactOptional.get());
        } else {
            response.setStatusCode("400");
            response.setStatusMsg("Invalid Contact ID received!!!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        response.setStatusCode("200");
        response.setStatusMsg("Message closed successfully!");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
