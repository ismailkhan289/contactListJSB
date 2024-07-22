package my.contact.contactlist.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import my.contact.contactlist.model.Contact;

//After creating the entity for Contact in the model folder we create a ContactRepo for 
//interacting with the Service database which we will create after this. Here we create
// only the interface for the repo and its implementation will be in Service
//and we can also define our own functions like findById etc here. 
@Repository
public interface ContactRepo extends JpaRepository<Contact, String>{
    Optional<Contact> findById(String id);
}
