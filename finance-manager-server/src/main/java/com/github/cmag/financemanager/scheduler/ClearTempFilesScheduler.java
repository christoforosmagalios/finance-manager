package com.github.cmag.financemanager.scheduler;

import java.io.File;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduler that runs every day at 23:00. Deletes all the files inside the temp folder.
 */
@Slf4j
@Component
public class ClearTempFilesScheduler {

  @Value("${finance.manager.bill.images.temp.path}")
  private String tempFolder;

  /**
   * Delete all the files in the temp folder.
   */
  @Scheduled(cron = "0 0 23 ? * *")
  public void deleteTempFiles() throws IOException {
    File file = new File(tempFolder);
    try {
      FileUtils.cleanDirectory(file);
    } catch (IOException e) {
      log.error(e.getMessage());
      throw e;
    }
  }
}
