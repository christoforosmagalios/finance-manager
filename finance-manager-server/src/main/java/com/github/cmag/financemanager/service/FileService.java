package com.github.cmag.financemanager.service;

import com.github.cmag.financemanager.config.AppConstants;
import com.github.cmag.financemanager.util.exception.FinanceManagerException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;
import liquibase.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
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
  private static final String JPG_EXT = ".jpg";

  @Value("${finance.manager.max.image.size}")
  private int maxSize;

  @Value("${finance.manager.bill.images.path}")
  private String imagesPath;

  /**
   * Validate and convert the given file to Base64.
   *
   * @param file The image file.
   * @return A Base64 representation of the given image.
   */
  public String upload(MultipartFile file) {
    // Check the file size.
    if (file.getSize() > maxSize) {
      throw new FinanceManagerException(AppConstants.INVALID_FILE_SIZE);
    }
    // Check the file type.
    if (!PNG_CONTENT.equals(file.getContentType()) && !JPG_CONTENT.equals(file.getContentType())) {
      throw new FinanceManagerException(AppConstants.INVALID_FILE_TYPE);
    }
    return AppConstants.DATA_IMAGE + encodeToBase64(file);
  }

  /**
   * Encode the given file to Base64.
   *
   * @param file The image file.
   * @return The Base64.
   */
  private String encodeToBase64(MultipartFile file) {
    try {
      byte[] bytes = file.getBytes();
      return new String(Base64.encodeBase64(bytes), StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new FinanceManagerException(AppConstants.GENERIC_ERROR);
    }
  }

  /**
   * If the given image path is not null return it and do not create a new image, otherwise create a
   * new image based on the given Base64.
   *
   * @param imgPath The image path of a Bill.
   * @param base64 The Base64 representation of a Bill image.
   * @return The path of the Bill image.
   */
  public String createFile(String imgPath, String base64) {
    if (!Objects.isNull(imgPath)) {
      return imgPath;
    } else if (Objects.isNull(base64)) {
      return null;
    }
    // Convert the Base64 to a byte array.
    byte[] data = base64ToBytes(base64);
    // Create a random file name.
    String fileName = UUID.randomUUID() + JPG_EXT;
    // Get the username of the logged in user.
    String username = File.separator
        + SecurityContextHolder.getContext().getAuthentication().getName();
    String directoryName = imagesPath + username;
    // Create directory if not already in place.
    createDirectories(directoryName);
    // Create the file.
    try (OutputStream stream = new FileOutputStream(directoryName + File.separator + fileName)) {
      stream.write(data);
    } catch (IOException e) {
      throw new FinanceManagerException(AppConstants.GENERIC_ERROR);
    }
    return username + File.separator + fileName;
  }

  /**
   * Delete the file with the given path.
   *
   * @param path The path of the file.
   */
  public void delete(String path) {
    try {
      // If the given path is not empty delete the file.
      if (StringUtils.isNotEmpty(path)) {
        java.nio.file.Files.delete(Path.of(imagesPath + File.separator + path));
      }
    } catch (IOException e) {
      throw new FinanceManagerException(AppConstants.GENERIC_ERROR);
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
   * Validate the given Base64 representation and convert it ti a byte array.
   *
   * @param base64 The Base64 representation.
   * @return A byte array.
   */
  private byte[] base64ToBytes(String base64) {
    String[] split = base64.split(",");
    if (split.length <= 1 || BooleanUtils.isFalse(Base64.isBase64(split[1]))) {
      throw new FinanceManagerException(AppConstants.INVALID_FILE_TYPE);
    }
    byte[] data = Base64.decodeBase64(split[1].trim());
    // Check the size of the image.
    if (data.length > maxSize) {
      throw new FinanceManagerException(AppConstants.INVALID_FILE_SIZE);
    }
    return data;
  }

}
