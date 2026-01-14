package io.github.bendeguznadasy.contact.service

import io.github.bendeguznadasy.contact.model.Contact
import io.github.bendeguznadasy.contact.model.dto.ContactDto
import io.github.bendeguznadasy.contact.model.dto.toEntity
import io.github.bendeguznadasy.contact.repository.ContactRepository
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class ContactService(
    private val contactRepository: ContactRepository,
    private val imgStorageService: ImageStorageService
) {
    fun getAllContacts(): List<Contact> {
        return contactRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))
    }

    fun getContactById(id: Long): Contact {
        return contactRepository.findById(id).orElseThrow { RuntimeException("Contact not found with id: $id") }
    }

    fun createContact(contactDto: ContactDto, image: MultipartFile? = null): Contact {
        var imagePath: String? = null

        if (image != null && !image.isEmpty) {
            imagePath = imgStorageService.store(image)
        }

        val newContact = contactDto.toEntity(imagePath)

        return contactRepository.save(newContact)
    }

    fun updateContact(id: Long, contactDto: ContactDto, image: MultipartFile?): Contact {
        val existingContact = getContactById(id)

        existingContact.name = contactDto.name
        existingContact.email = contactDto.email
        existingContact.phone = contactDto.phone
        existingContact.address = contactDto.address

        if (image != null && !image.isEmpty) {
            existingContact.imagePath?.let { oldFilename ->
                imgStorageService.delete(oldFilename)
            }
            val newFilename = imgStorageService.store(image)
            existingContact.imagePath = newFilename
        }

        return contactRepository.save(existingContact)
    }

    fun deleteContact(id: Long) {
        val contact = getContactById(id)
        contact.imagePath?.let { filename ->
            imgStorageService.delete(filename)
        }
        contactRepository.delete(contact)
    }

}