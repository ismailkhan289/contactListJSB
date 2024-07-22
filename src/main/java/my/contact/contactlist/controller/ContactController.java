package my.contact.contactlist.controller;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;


import org.springframework.http.MediaType;
import org.springframework.data.domain.Page;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import my.contact.contactlist.Uploads.ContactImage;
import my.contact.contactlist.model.Contact;
import my.contact.contactlist.service.ContactService;
import static my.contact.contactlist.Uploads.ContactImage.PHOTO_DIRECTORY;


@RestController //1 declare at the initial that it is a controller
@RequestMapping("/contacts") //then gie it a url for mapping
@RequiredArgsConstructor


public class ContactController {

    
    //first declare the service injection in the controller
    private final ContactService contactService;

    @PostMapping(consumes = "application/json")
    public  ResponseEntity<Contact> createContact(@RequestBody Contact contact){
        return ResponseEntity
                .created(URI.create("/contacts/userID"))
                .body(contactService.createContact(contact));
    }
    
    @GetMapping
    public ResponseEntity<Page<Contact>> getContacts
            (@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size){
            return ResponseEntity.ok().body(contactService.getAllContacts(page, size));

             }
    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContact(@PathVariable(value = "id") String id){
        return ResponseEntity.ok().body(contactService.getContact(id));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable String id){
         contactService.deleteContact(id);
         return ResponseEntity.noContent().build();
    }
    @PutMapping("/photo")
    public ResponseEntity<String> uploadPhoto(
            @RequestParam("id") String id, 
            @RequestParam("file")MultipartFile file){
        return ResponseEntity.ok().body(contactService.uploadPhoto(id, file));
    }

    @GetMapping(path = "/image/{filename}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public byte[] getPhoto(@PathVariable("filename") String filename) throws IOException{
        return Files.readAllBytes(Paths.get(ContactImage.PHOTO_DIRECTORY +filename));
    }
}
