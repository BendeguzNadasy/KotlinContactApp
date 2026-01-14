package io.github.bendeguznadasy.contact.service

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.UUID

@Service
class ImageStorageService {

    private val rootLocation = Paths.get("uploads")

    init {
        try {
            Files.createDirectories(rootLocation)
        } catch (e: IOException) {
            throw RuntimeException("Nem sikerült létrehozni a feltöltési mappát!", e)
        }
    }

    fun store(file: MultipartFile): String {
        try {
            if (file.isEmpty) {
                throw RuntimeException("Üres fájlt nem lehet feltölteni.")
            }

            // generate unique id for images
            val filename = "${UUID.randomUUID()}-${file.originalFilename}"

            val destinationFile = this.rootLocation.resolve(Paths.get(filename)).normalize().toAbsolutePath()

            if (!destinationFile.parent.equals(this.rootLocation.toAbsolutePath())) {
                throw RuntimeException("Biztonsági hiba: A fájlt a mappán kívülre próbálták menteni.")
            }

            file.inputStream.use { inputStream ->
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING)
            }

            return filename
        } catch (e: IOException) {
            throw RuntimeException("Hiba a fájl mentése közben.", e)
        }
    }

    fun delete(filename: String) {
        try {
            val file = rootLocation.resolve(filename)
            Files.deleteIfExists(file)
        } catch (e: IOException) {
            System.err.println("Nem sikerült törölni a fájlt: $filename")
        }
    }
}