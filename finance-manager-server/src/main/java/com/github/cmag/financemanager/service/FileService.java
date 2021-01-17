package com.github.cmag.financemanager.service;

import com.github.cmag.financemanager.config.AppConstants;
import com.github.cmag.financemanager.dto.UploadDTO;
import com.github.cmag.financemanager.util.exception.FinanceManagerException;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.UUID;
import liquibase.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * The File Service.
 */
@Service
@Slf4j
public class FileService {

  private static final String PNG_CONTENT = "image/png";
  private static final String JPG_CONTENT = "image/jpeg";

  @Value("${finance.manager.max.image.size}")
  private int maxSize;

  /**
   * Copy the given image to the temp folder.
   *
   * @param multipartFile MultipartFile
   * @param directory The directory where the file will be saved.
   * @return An UploadDTO containing the image information.
   */
  public UploadDTO upload(MultipartFile multipartFile, String directory) throws IOException {

    // Check the file size.
    if (multipartFile.getSize() > maxSize) {
      throw new FinanceManagerException(AppConstants.INVALID_FILE_SIZE);
    }
    // Check the file type.
    if (multipartFile.getContentType() == null || (
        !multipartFile.getContentType().equals(PNG_CONTENT)
            && !multipartFile.getContentType().equals(JPG_CONTENT))) {
      throw new FinanceManagerException(AppConstants.INVALID_FILE_TYPE);
    }

    // Create directory if not already in place.
    createDirectories(directory);

    try (InputStream initialStream = multipartFile.getInputStream()) {
      // Create the new temp file
      String uuid = UUID.randomUUID().toString();
      String filePath =
          directory + File.separator + uuid + "." + getExtension(
              multipartFile.getOriginalFilename());
      File targetFile = new File(filePath);

      // Write into the new temp file
      byte[] buffer = new byte[initialStream.available()];
      initialStream.read(buffer);
      Files.write(buffer, targetFile);

      return new UploadDTO("data:" + multipartFile.getContentType()
          + ";base64," + Base64.encodeBase64String(buffer), filePath,
          multipartFile.getContentType());
    } catch (IOException e) {
      log.error(e.getMessage());
      throw e;
    }
  }

  /**
   * Delete the file with the given old path, and copy the new one to the geven new path.
   *
   * @param newPath The new file path.
   * @param oldPath The old file path.
   * @param directory The directory where the file will be saved.
   * @return The new file path.
   */
  public String copy(String newPath, String oldPath, String directory) throws IOException {
    // If the new and the old path are the same do not proceed.
    if ((StringUtils.isNotEmpty(newPath) && StringUtils.isNotEmpty(oldPath)
        && newPath.equals(oldPath))) {
      return newPath;
    }

    // Delete the old file.
    delete(oldPath);
    // If the new path is not empty copy the File.
    if (StringUtils.isNotEmpty(newPath)) {
      // Create directory if not already in place.
      createDirectories(directory);

      try {
        File source = new File(newPath);
        String newFilePath = directory + File.separator + source.getName();
        File dest = new File(newFilePath);
        Files.copy(source, dest);
        return newFilePath;
      } catch (IOException e) {
        log.error(e.getMessage());
        throw e;
      }
    }
    return null;
  }

  /**
   * Read the file at the given path and create the base64 encoded String.
   *
   * @param path File Path
   * @param imageType Image type
   * @return Base64 String
   */
  public String getBase64(String path, String imageType) {
    File file = new File(path);
    try {
      byte[] buffer = Files.toByteArray(file);
      return "data:" + imageType + ";base64," + Base64.encodeBase64String(buffer);
    } catch (IOException e) {
      log.error(e.getMessage());
      return null;
    }
  }

  /**
   * Delete the file with the given path.
   *
   * @param path The path of the file.
   */
  private void delete(String path) throws IOException {
    // If the given path is not empty delete the file.
    if (StringUtils.isNotEmpty(path)) {
      java.nio.file.Files.delete(Path.of(path));
    }
  }

  /**
   * Create the directories of the given path in case they do not already exist.
   *
   * @param path The path.
   */
  public void createDirectories(String path) {
    File directory = new File(path);
    if (!directory.exists()) {
      directory.mkdirs();
    }
  }

  /**
   * Get the file extension from the given String.
   *
   * @return The extension.
   */
  private String getExtension(String name) {
    String[] parts = name.split("\\.");
    return parts[parts.length - 1];
  }

}
