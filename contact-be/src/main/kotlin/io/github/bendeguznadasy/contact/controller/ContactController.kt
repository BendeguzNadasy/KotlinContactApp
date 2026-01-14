package io.github.bendeguznadasy.contact.controller

import io.github.bendeguznadasy.contact.model.Contact
import io.github.bendeguznadasy.contact.model.CreateContactRequest
import io.github.bendeguznadasy.contact.model.UpdateContactRequest
import io.github.bendeguznadasy.contact.service.ContactService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.nio.file.Paths

@RestController
@RequestMapping("/api/contacts")
@CrossOrigin // let the frontend (pl. localhost:3000) reach backend
@Tag(name = "Contact Management", description = "Operations for managing contacts (CRUD + Image Upload).")
class ContactController(
    private val contactService: ContactService
) {
    @GetMapping
    @Operation(summary = "List all contacts", description = "Returns a list of all contacts, sorted by name.")
    fun getAllContacts(): List<Contact> {
        return contactService.getAllContacts()
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a contact by ID", description = "Returns a single contact details.")
    fun getContact(@PathVariable id: Long): ResponseEntity<Contact> {
        return try {
            val contact = contactService.getContactById(id)
            ResponseEntity.ok(contact)
        } catch (e: Exception) {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @Operation(
        summary = "Create a new contact",
        description = "Creates a new contact with an optional image."
    )
    fun createContact(
        @ModelAttribute request: CreateContactRequest
    ): ResponseEntity<Contact> {
        val contactDto = request.toContactDto()
        val image = request.image

        val newContact = contactService.createContact(contactDto, image)
        return ResponseEntity.status(HttpStatus.CREATED).body(newContact)
    }

    @PutMapping(value = ["/{id}"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @Operation(summary = "Update an existing contact", description = "Updates contact details and/or profile image.")
    fun updateContact(
        @PathVariable id: Long,
        @ModelAttribute request: UpdateContactRequest
    ): ResponseEntity<Contact> {
        return try {
            val updatedContact = contactService.updateContact(
                id,
                request.toContactDto(),
                request.image
            )
            ResponseEntity.ok(updatedContact)
        } catch (e: Exception) {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a contact", description = "Removes the contact and the associated image file.")
    fun deleteContact(@PathVariable id: Long): ResponseEntity<Void> {
        return try {
            contactService.deleteContact(id)
            ResponseEntity.noContent().build()
        } catch (e: Exception) {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/images/{filename}")
    @Operation(summary = "Download/View profile image", description = "Serves the image file from the storage.")
    fun getImage(@PathVariable filename: String): ResponseEntity<Resource> {
        val path = Paths.get("uploads").resolve(filename)
        val resource = UrlResource(path.toUri())

        return if (resource.exists() && resource.isReadable) {
            ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource)
        } else {
            ResponseEntity.notFound().build()
        }
    }
}