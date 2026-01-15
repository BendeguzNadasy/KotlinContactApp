package io.github.bendeguznadasy.contact.service

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.mock.web.MockMultipartFile
import java.nio.file.Files
import java.nio.file.Paths

class ImageStorageServiceTest {

    private val imageStorageService = ImageStorageService()

    private val createdFiles = mutableListOf<String>()
    private val uploadDir = Paths.get("uploads")

    @AfterEach
    fun cleanup() {
        createdFiles.forEach { filename ->
            try {
                Files.deleteIfExists(uploadDir.resolve(filename))
            } catch (e: Exception) {
                println("Failed to delete test file: $filename")
            }
        }
    }

    @Test
    fun `store should save valid file and return generated filename`() {
        val content = "Hello World".toByteArray()
        val mockFile = MockMultipartFile(
            "image",
            "test-image.jpg",
            "image/jpeg",
            content
        )

        val generatedFilename = imageStorageService.store(mockFile)
        createdFiles.add(generatedFilename)

        assertNotNull(generatedFilename)
        assertTrue(generatedFilename.contains("test-image.jpg"))
        assertTrue(generatedFilename.length > "test-image.jpg".length)

        val savedFile = uploadDir.resolve(generatedFilename)
        assertTrue(Files.exists(savedFile), "The file must exist in the uploads directory")

        assertArrayEquals(content, Files.readAllBytes(savedFile))
    }

    @Test
    fun `store should throw exception when file is empty`() {
        val emptyFile = MockMultipartFile("image", "empty.jpg", "image/jpeg", ByteArray(0))

        val exception = assertThrows<RuntimeException> {
            imageStorageService.store(emptyFile)
        }

        assertEquals("Failed to store empty file.", exception.message)
    }

    @Test
    fun `store should throw exception on path traversal attack`() {
        val maliciousFile = MockMultipartFile(
            "image",
            "../hack.txt",
            "text/plain",
            "Malicious content".toByteArray()
        )

        val exception = assertThrows<RuntimeException> {
            imageStorageService.store(maliciousFile)
        }
        assertTrue(exception.message!!.contains("Security error") || exception.message!!.contains("Failed to store"))
    }

    @Test
    fun `delete should remove existing file`() {
        val content = "To be deleted".toByteArray()
        val mockFile = MockMultipartFile("image", "delete-me.jpg", "image/jpeg", content)
        val filename = imageStorageService.store(mockFile)

        assertTrue(Files.exists(uploadDir.resolve(filename)))

        imageStorageService.delete(filename)

        assertFalse(Files.exists(uploadDir.resolve(filename)))
    }

    @Test
    fun `delete should not throw exception if file does not exist`() {
        assertDoesNotThrow {
            imageStorageService.delete("non-existent-file.jpg")
        }
    }
}