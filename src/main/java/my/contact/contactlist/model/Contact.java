package my.contact.contactlist.model;

import org.hibernate.annotations.UuidGenerator;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude
@Table(name="contacts")

//After creating project we create model of Contact Entity and define its attributes.
public class Contact {
    @Id
    @UuidGenerator
    @Column(name = "id", unique = true,updatable = false )
    private String id;
    private String name;
    private String email;
    private String titles;
    private String phone;
    private String address;
    private String status;
    private String photoUrl;
}
