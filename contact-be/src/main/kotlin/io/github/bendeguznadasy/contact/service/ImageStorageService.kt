package io.github.bendeguznadasy.contact.service

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.*

@Service
class ImageStorageService {

    private val rootLocation = Paths.get("uploads")

    init {
        try {
            Files.createDirectories(rootLocation)
        } catch (e: IOException) {
            throw RuntimeException("Could not create upload directory!", e)
        }
    }

    fun store(file: MultipartFile): String {
        try {
            if (file.isEmpty) {
                throw RuntimeException("Failed to store empty file.")
            }

            // generate unique id for images
            val filename = "${UUID.randomUUID()}-${file.originalFilename}"

            val destinationFile = this.rootLocation.resolve(Paths.get(filename)).normalize().toAbsolutePath()

            if (!destinationFile.parent.equals(this.rootLocation.toAbsolutePath())) {
                throw RuntimeException("Security error: Cannot store file outside of the target directory.")
            }

            file.inputStream.use { inputStream ->
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING)
            }

            return filename
        } catch (e: IOException) {
            throw RuntimeException("Failed to store file.", e)
        }
    }

    fun delete(filename: String) {
        try {
            val file = rootLocation.resolve(filename)
            Files.deleteIfExists(file)
        } catch (e: IOException) {
            System.err.println("Could not delete file: $filename")
        }
    }
}