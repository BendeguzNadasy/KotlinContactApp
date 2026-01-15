package io.github.bendeguznadasy.contact.service

import io.github.bendeguznadasy.contact.model.Contact
import io.github.bendeguznadasy.contact.model.dto.ContactDto
import io.github.bendeguznadasy.contact.repository.ContactRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.domain.Sort
import org.springframework.web.multipart.MultipartFile
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class ContactServiceTest {

    @Mock
    lateinit var contactRepository: ContactRepository

    @Mock
    lateinit var imgStorageService: ImageStorageService

    @InjectMocks
    lateinit var contactService: ContactService

    @Test
    fun `getAllContacts should return sorted list`() {
        val expectedList = listOf(Contact(name = "Anna"), Contact(name = "Béla"))

        `when`(contactRepository.findAll(any(Sort::class.java))).thenReturn(expectedList)

        val result = contactService.getAllContacts()

        assertEquals(2, result.size)
        assertEquals("Anna", result[0].name)

        verify(contactRepository).findAll(any(Sort::class.java))
    }

    @Test
    fun `getContactById should return contact when found`() {
        val contact = Contact(id = 1L, name = "Teszt")
        `when`(contactRepository.findById(1L)).thenReturn(Optional.of(contact))

        val result = contactService.getContactById(1L)

        assertNotNull(result)
        assertEquals("Teszt", result.name)
    }

    @Test
    fun `getContactById should throw exception when not found`() {
        `when`(contactRepository.findById(99L)).thenReturn(Optional.empty())

        val exception = assertThrows(RuntimeException::class.java) {
            contactService.getContactById(99L)
        }

        assertEquals("Contact not found with id: 99", exception.message)
    }

    @Test
    fun `createContact should save contact WITHOUT image`() {
        val dto = ContactDto(name = "Új Ember", email = "uj@mail.com", null, null)
        `when`(contactRepository.save(any(Contact::class.java))).thenAnswer { it.arguments[0] }

        val savedContact = contactService.createContact(dto, null)

        assertEquals("Új Ember", savedContact.name)
        assertNull(savedContact.imagePath)

        verifyNoInteractions(imgStorageService)
    }

    @Test
    fun `createContact should save contact WITH image`() {
        val dto = ContactDto(name = "Képes Ember", null, null, null)
        val mockImage = mock(MultipartFile::class.java)

        `when`(mockImage.isEmpty).thenReturn(false)
        `when`(imgStorageService.store(mockImage)).thenReturn("uploads/kep.jpg")
        `when`(contactRepository.save(any(Contact::class.java))).thenAnswer { it.arguments[0] }

        val savedContact = contactService.createContact(dto, mockImage)

        assertEquals("uploads/kep.jpg", savedContact.imagePath)

        verify(imgStorageService).store(mockImage)
    }

    @Test
    fun `updateContact should update fields and keep old image if no new image provided`() {
        val existing = Contact(id = 1L, name = "Régi Név", imagePath = "regi.jpg")
        val updateDto = ContactDto(name = "Frissített Név", null, null, null)

        `when`(contactRepository.findById(1L)).thenReturn(Optional.of(existing))
        `when`(contactRepository.save(any(Contact::class.java))).thenAnswer { it.arguments[0] }
        val result = contactService.updateContact(1L, updateDto, null)

        assertEquals("Frissített Név", result.name)
        assertEquals("regi.jpg", result.imagePath)
        verifyNoInteractions(imgStorageService)
    }

    @Test
    fun `updateContact should replace image if new one provided`() {
        val existing = Contact(id = 1L, name = "Valaki", imagePath = "regi_kep.jpg")
        val updateDto = ContactDto(name = "Valaki", null, null, null)
        val newImage = mock(MultipartFile::class.java)

        `when`(contactRepository.findById(1L)).thenReturn(Optional.of(existing))
        `when`(newImage.isEmpty).thenReturn(false)
        `when`(imgStorageService.store(newImage)).thenReturn("uj_kep.jpg")
        `when`(contactRepository.save(any(Contact::class.java))).thenAnswer { it.arguments[0] }

        val result = contactService.updateContact(1L, updateDto, newImage)

        assertEquals("uj_kep.jpg", result.imagePath)

        verify(imgStorageService).delete("regi_kep.jpg")
        verify(imgStorageService).store(newImage)
    }

    @Test
    fun `deleteContact should delete image and entity`() {
        val contact = Contact(id = 1L, name = "Törlendő", imagePath = "torlendo.jpg")

        `when`(contactRepository.findById(1L)).thenReturn(Optional.of(contact))

        contactService.deleteContact(1L)

        verify(imgStorageService).delete("torlendo.jpg")
        verify(contactRepository).delete(contact)
    }
}