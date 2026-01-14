package io.github.bendeguznadasy.contact.controller

import io.github.bendeguznadasy.contact.model.Contact
import io.github.bendeguznadasy.contact.model.CreateContactRequest
import io.github.bendeguznadasy.contact.model.dto.ContactDto
import io.github.bendeguznadasy.contact.service.ContactService
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.multipart.support.StringMultipartFileEditor
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Paths

@RestController
@RequestMapping("/api/contacts")
@CrossOrigin // Ez engedi, hogy a frontend (pl. localhost:3000) elérje a backendet
class ContactController(
    private val contactService: ContactService
) {
    @GetMapping
    fun getAllContacts(): List<Contact> {
        return contactService.getAllContacts()
    }

    @GetMapping("/{id}")
    fun getContact(@PathVariable id: Long): ResponseEntity<Contact> {
        return try {
            val contact = contactService.getContactById(id)
            ResponseEntity.ok(contact)
        } catch (e: Exception) {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun createContact(
        @ModelAttribute request: CreateContactRequest
    ): ResponseEntity<Contact> {
        val contactDto = request.toContactDto()
        val image = request.image

        val newContact = contactService.createContact(contactDto, image)
        return ResponseEntity.status(HttpStatus.CREATED).body(newContact)
    }

    @PutMapping("/{id}", consumes = ["multipart/form-data"])
    fun updateContact(
        @PathVariable id: Long,
        @ModelAttribute contactDto: ContactDto,
        @RequestParam("image", required = false) image: MultipartFile?
    ): ResponseEntity<Contact> {
        return try {
            val updatedContact = contactService.updateContact(id, contactDto, image)
            ResponseEntity.ok(updatedContact)
        } catch (e: Exception) {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteContact(@PathVariable id: Long): ResponseEntity<Void> {
        return try {
            contactService.deleteContact(id)
            ResponseEntity.noContent().build()
        } catch (e: Exception) {
            ResponseEntity.notFound().build()
        }
    }

    // 6. Kép kiszolgálása (Hogy a frontend meg tudja jeleníteni)
    // URL pl.: http://localhost:8080/api/contacts/images/a1b2-kep.jpg
    @GetMapping("/images/{filename}")
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