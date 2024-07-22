package my.contact.contactlist.service;
import org.springframework.data.domain.Page;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.Optional;




import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import my.contact.contactlist.Uploads.ContactImage;
import my.contact.contactlist.model.Contact;
import my.contact.contactlist.repo.ContactRepo;

@Getter
@Setter
@Service 
//first declare the annotation of service
@Slf4j   
//that is for saving the logs information of the database.

@Transactional(rollbackOn = Exception.class) 
// this is used for if something doesnot save 
//properly than the transaction can be roll back.

@RequiredArgsConstructor  //to automatically generate a constructor with
//required arguments for final fields and fields marked with @NonNull.

public class ContactService  {
    private final ContactRepo contactRepo; 
    //To define the ContactRepo field and ensure it is injected properly 
    //into the ContactService class and it must extend the JpaRepository inorder to use
    //service

    public Page<Contact> getAllContacts(int page, int size){
        return contactRepo.findAll(PageRequest.of(page, size, Sort.by("name")));
    }
    public Contact getContact(String id){
        return contactRepo.findById(id).orElseThrow(()
        ->new RuntimeException("Contact not found"));
    }
    //creating a new Contact and saving
    public Contact createContact(Contact contact){
        return contactRepo.save(contact);
    }
    //deleting an existing contact
    public void deleteContact(String id){
        if(contactRepo.existsById(id)){
            contactRepo.deleteById(id);

        }else{
            throw new RuntimeException("contact not found "+id);
        }
    }

    //uploadPhoto Method: This method uses the photoFunction to handle the actual 
    //file upload and then updates the photoUrl of the Contact object. The updated
    // contact is then saved back to the repository.
    public String uploadPhoto(String id, MultipartFile file){
        log.info("Saving Picture of Contact Person: {}", id);
        Contact contact=getContact(id);//get the contact of that Id;
        String photoUrl=photoFunction.apply(id, file);
        contact.setPhotoUrl(photoUrl);
        contactRepo.save(contact);
        return photoUrl;
    }
    //fileExtension Function: This function extracts the file extension from 
    //the provided filename. If the filename does not have an extension, 
    //it defaults to .png.

    private final Function<String, String> fileExtention=filename->Optional.of(filename)
    .filter(name->name.contains("."))
    .map(name->"."+name.substring(filename.lastIndexOf(".")+1)).orElse(".png");

    //The BiFunction interface in Java represents a function that accepts 
    //two arguments and produces a result. It is a part of the java.util.function package. 
    //In your context, it is used to handle the uploading and saving of a photo by 
    //accepting an id and a MultipartFile and returning a String which represents 
    //the URL of the saved photo.BiFunction: This function handles the logic 
    //for saving the photo. It constructs the filename using the id and the
    // extracted file extension. 
    private final BiFunction<String, MultipartFile, String> photoFunction=(id, image)->{
       
        String filename=id + fileExtention.apply(image.getOriginalFilename());

        try {
            Path fileStorageLocation = Paths.get(ContactImage.PHOTO_DIRECTORY).toAbsolutePath().normalize();
            if(!Files.exists(fileStorageLocation)) { Files.createDirectories(fileStorageLocation); }
            Files.copy(image.getInputStream(), fileStorageLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
            return ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/contacts/image/"+filename).toUriString();
        } catch (Exception e) {
            throw new RuntimeException("Unable to save Image");
        }
    };


}
