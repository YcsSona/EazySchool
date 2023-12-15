package com.theonewhocode.eazyschool.service;

import com.theonewhocode.eazyschool.constants.EazySchoolConstansts;
import com.theonewhocode.eazyschool.model.Person;
import com.theonewhocode.eazyschool.model.Roles;
import com.theonewhocode.eazyschool.repository.PersonRepository;
import com.theonewhocode.eazyschool.repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean createNewPerson(Person person) {
        Roles role = rolesRepository.getByRoleName(EazySchoolConstansts.STUDENT_ROLE);
        person.setRoles(role);
        person.setPwd(passwordEncoder.encode(person.getPwd()));
        Person savedPerson = personRepository.save(person);

        return savedPerson != null && savedPerson.getPersonId() > 0;
    }
}
