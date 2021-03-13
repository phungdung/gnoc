package com.viettel.gnoc.commons.utils;

import com.google.gson.Gson;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * Created by TungPV
 */
@Slf4j
public class FileUtils {

  public static boolean deleteFtpFile(String server, int port, String user, String pass,
      String fullPath) throws IOException {
    FTPClient ftpClient = FTPUtil.connectionFTPClient(server, port, user, pass);
    boolean deleted = ftpClient.deleteFile(fullPath);
    if (deleted) {
      FTPUtil.disconnectionFTPClient(ftpClient);
      return true;
    } else {
      FTPUtil.disconnectionFTPClient(ftpClient);
      return false;
    }
  }

  public static boolean existsFtpFile(String server, int port, String user, String pass,
      String fullPath) throws IOException {
    FTPClient ftpClient = FTPUtil.connectionFTPClient(server, port, user, pass);
    FTPFile[] remoteFiles = ftpClient.listFiles(fullPath);
    if (remoteFiles.length > 0) {
      FTPUtil.disconnectionFTPClient(ftpClient);
      return true;
    } else {
      FTPUtil.disconnectionFTPClient(ftpClient);
      return false;
    }
  }

  public static byte[] getFtpFile(String server, int port, String user, String pass,
      String fullPath)
      throws IOException {
    FTPClient ftpClient = FTPUtil.connectionFTPClient(server, port, user, pass);
    InputStream inputStream = ftpClient.retrieveFileStream(fullPath);
    if (inputStream != null) {
      FTPUtil.disconnectionFTPClient(ftpClient);
      byte[] bytes = FileUtils.convertFileToByte(inputStream);
      return bytes;
    } else {
      System.out.println("File: " + fullPath + " not exist ! ");
      FTPUtil.disconnectionFTPClient(ftpClient);
      return null;
    }
  }

  public static String saveFtpFile(String server, int port, String user, String pass,
      String ftpFolder, String originalFilename, byte[] bytes) throws IOException {
    FTPClient ftpClient = FTPUtil.connectionFTPClient(server, port, user, pass);
    FTPUtil.uploadDirectory(ftpClient, ftpFolder, originalFilename, bytes);
    FTPUtil.disconnectionFTPClient(ftpClient);
    return ftpFolder + "/" + originalFilename;
  }

  public static String saveFtpFile(String server, int port, String user, String pass,
      String ftpFolder, String originalFilename, byte[] bytes, Date date) throws IOException {
    if (date == null) {
      date = new Date();
    }
    String fileName = createFileName(originalFilename, date);
    FTPClient ftpClient = FTPUtil.connectionFTPClient(server, port, user, pass);
    FTPUtil.uploadDirectory(ftpClient, ftpFolder + "/"
        + createPathFtpByDate(date), fileName, bytes);
    FTPUtil.disconnectionFTPClient(ftpClient);
    return ftpFolder + "/" + createPathFtpByDate(date) + "/" + fileName;
  }

  public static String saveUploadFile(String originalFilename, byte[] bytes, String uploadFolder)
      throws IOException {
    File file = new File(uploadFolder);
    if (!file.exists()) {
      file.mkdirs();
    }
    File fileWrite = new File(file.getPath() + File.separator + originalFilename);
    try (FileOutputStream fos = new FileOutputStream(fileWrite)) {
      fos.write(bytes);
      fos.close();
    }
    return file.getPath() + File.separator + originalFilename;
  }

  public static String saveUploadFile(String originalFilename, byte[] bytes, String uploadFolder,
      Date date)
      throws IOException {
    if (date == null) {
      date = new Date();
    }
    String fileName = createFileName(originalFilename, date);
    File file = new File(uploadFolder + File.separator + createPathByDate(date));
    if (!file.exists()) {
      file.mkdirs();
    }
    File fileWrite = new File(file.getPath() + File.separator + fileName);
    try (FileOutputStream fos = new FileOutputStream(fileWrite)) {
      fos.write(bytes);
      fos.close();
    }
    return file.getPath() + File.separator + fileName;
  }

  public static String saveTempFile(String originalFilename, byte[] bytes, String tempFolder)
      throws IOException {
    Date date = new Date();
    String fileName = createFileName(originalFilename, date);
    File file = new File(tempFolder + File.separator + createPathByDate(date));
    if (!file.exists()) {
      file.mkdirs();
    }
    File fileWrite = new File(file.getPath() + File.separator + fileName);
    try (FileOutputStream fos = new FileOutputStream(fileWrite)) {
      fos.write(bytes);
      fos.close();
    }
    return file.getPath() + File.separator + fileName;
  }

  public static String saveTempFileZip(List<File> files, String originalFilename, String tempFolder)
      throws Exception {
    ZipOutputStream out = null;
    FileInputStream in = null;
    try {
      Date date = new Date();
      String fileName = createFileName(originalFilename, date);
      File zipfile = new File(tempFolder + File.separator + createPathByDate(date));
      if (!zipfile.exists()) {
        zipfile.mkdirs();
      }
      File fileWrite = new File(
          tempFolder + File.separator + createPathByDate(date) + File.separator + fileName);
      // Create a buffer for reading the files
      byte[] buf = new byte[1024];
      // Create the ZIP file
      out = new ZipOutputStream(
          new FileOutputStream(fileWrite));
      // Compress the files
      for (int i = 0; i < files.size(); i++) {
        in = new FileInputStream(files.get(i).getAbsolutePath());
        // Add ZIP entry to output stream
        out.putNextEntry(new ZipEntry(files.get(i).getName()));
        // Transfer bytes from the file to the ZIP file
        int len;
        while ((len = in.read(buf)) > 0) {
          out.write(buf, 0, len);
        }
        // Complete the entry
        out.closeEntry();
        in.close();
      }
      // Complete the ZIP file
      out.close();
      return tempFolder + File.separator + createPathByDate(date) + File.separator + fileName;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      if (out != null) {
        try {
          out.close();
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
      }
      if (in != null) {
        try {
          in.close();
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
      }
    }
  }

  public static void deleteFileByPath(String pathFile, String uploadFolder) {
    File file = new File(uploadFolder + File.separator + pathFile);
    if (file.exists()) {
      file.delete();
    }
  }

  public static void copyFile(File source, File dest) throws Exception {
    InputStream is = null;
    OutputStream os = null;
    try {
      is = new FileInputStream(source);
      os = new FileOutputStream(dest);
      byte[] buffer = new byte[1024];
      int length;
      while ((length = is.read(buffer)) > 0) {
        os.write(buffer, 0, length);
      }
    } catch (FileNotFoundException e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      if (os != null) {
        try {
          os.close();
        } catch (IOException io) {
          log.error(io.getMessage(), io);
        }
      }
      if (is != null) {
        try {
          is.close();
        } catch (IOException io) {
          log.error(io.getMessage(), io);
        }
      }
    }
  }

  public static byte[] convertFileToByte(File file) throws IOException {
    FileInputStream fis = new FileInputStream(file);
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    byte[] buf = new byte[1024];
    try {
      for (int readNum; (readNum = fis.read(buf)) != -1; ) {
        bos.write(buf, 0, readNum);
      }
    } finally {
      fis.close();
    }
    return bos.toByteArray();
  }

  public static byte[] convertFileToByte(InputStream fis) throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    byte[] buf = new byte[1024];
    try {
      for (int readNum; (readNum = fis.read(buf)) != -1; ) {
        bos.write(buf, 0, readNum);
      }
    } finally {
      fis.close();
    }
    return bos.toByteArray();
  }

  public static String createFileName(String originalFilename, Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar.get(Calendar.HOUR_OF_DAY) + ""
        + calendar.get(Calendar.MINUTE) + "" + calendar.get(Calendar.SECOND) + ""
        + calendar.get(Calendar.MILLISECOND) + "_" + originalFilename;
  }

  public static Date createDateOfFileName(Date date) {
    try {
      Date now = new Date();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
      SimpleDateFormat sdfNow = new SimpleDateFormat("HH:mm:ss.SSS");
      SimpleDateFormat sdfFinal = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
      String dateStr = sdf.format(date);
      String dateNowStr = sdfNow.format(now);
      return sdfFinal.parse(dateStr + " " + dateNowStr);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return date;
  }

  public static String createPathByDate(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH) + 1;
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    String pathByDate = year
        + File.separator
        + (month < 10 ? "0" + month : month)
        + File.separator
        + (day < 10 ? "0" + day : day);
    return pathByDate;
  }

  public static String createPathFtpByDate(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH) + 1;
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    String pathByDate = year + "/"
        + (month < 10 ? "0" + month : month) + "/"
        + (day < 10 ? "0" + day : day);
    return pathByDate;
  }

  public static String getFilePath(String fullPath) {
    if (StringUtils.isNotNullOrEmpty(fullPath)) {
      fullPath = fullPath
          .replaceAll("[/\\\\]+", Matcher.quoteReplacement(System.getProperty("file.separator")));
      return FilenameUtils.getPath(fullPath);
    }
    return null;
  }

  public static String getFileName(String fullPath) {
    if (StringUtils.isNotNullOrEmpty(fullPath)) {
      fullPath = fullPath
          .replaceAll("[/\\\\]+", Matcher.quoteReplacement(System.getProperty("file.separator")));
      return FilenameUtils.getName(fullPath);
    }
    return null;
  }

  public static ResponseEntity<Resource> responseSourceFromFileTemplate(String path)
      throws Exception {
    Resource resource = new ClassPathResource(path);
    InputStream inputStream = resource.getInputStream();
    InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
    HttpHeaders headers = new HttpHeaders();
    List<String> customHeaders = new ArrayList<>();
    customHeaders.add("Content-Disposition");
    headers.setAccessControlExposeHeaders(customHeaders);
    headers.setContentDispositionFormData("attachment", resource.getFilename());
    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
    return ResponseEntity.ok()
        .headers(headers)
//        .contentLength(inputStreamResource.contentLength())
        .contentType(MediaType.parseMediaType("application/octet-stream"))
        .body(inputStreamResource);
  }

  public static ResponseEntity<Resource> responseSourceFromFile(File file) throws Exception {
    InputStreamResource inputStreamResource = new InputStreamResource(new FileInputStream(file));
    HttpHeaders headers = new HttpHeaders();
    List<String> customHeaders = new ArrayList<>();
    customHeaders.add("Content-Disposition");
    headers.setAccessControlExposeHeaders(customHeaders);
    headers.setContentDispositionFormData("attachment", file.getName());
    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
    return ResponseEntity.ok()
        .headers(headers)
//        .contentLength(inputStreamResource.contentLength())
        .contentType(MediaType.parseMediaType("application/octet-stream"))
        .body(inputStreamResource);
  }

  public static ResponseEntity<Resource> responseSourceFromFile(FTPClient ftpClient, String path)
      throws Exception {
    InputStream inputStream = ftpClient.retrieveFileStream(path);
    InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
    HttpHeaders headers = new HttpHeaders();
    List<String> customHeaders = new ArrayList<>();
    customHeaders.add("Content-Disposition");
    headers.setAccessControlExposeHeaders(customHeaders);
    headers.setContentDispositionFormData("attachment", getFileName(path));
    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
    return ResponseEntity.ok()
        .headers(headers)
//        .contentLength(inputStreamResource.contentLength())
        .contentType(MediaType.parseMediaType("application/octet-stream"))
        .body(inputStreamResource);
  }

  public static ResponseEntity<Resource> responseSourceObjectFromFile(
      ResultInSideDto resultInSideDto) throws Exception {
    File fileResponse = resultInSideDto.getFile();
    Gson gson = new Gson();
    Map<String, Object> objectMap = new HashMap<>();
    objectMap.put("key", resultInSideDto.getKey());
    objectMap.put("message", resultInSideDto.getMessage());
    objectMap.put("fileName", (fileResponse == null) ? null : fileResponse.getName());
    HttpHeaders headers = new HttpHeaders();
    List<String> customHeaders = new ArrayList<>();
    customHeaders.add("Content-Disposition");
    headers.setAccessControlExposeHeaders(customHeaders);
    headers.setContentDispositionFormData("attachment", URLEncoder
        .encode(gson.toJson(objectMap), "UTF-8").replace("+", "%20"));
    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
    InputStreamResource inputStreamResource = null;
    if (fileResponse != null) {
      inputStreamResource = new InputStreamResource(new FileInputStream(fileResponse));
    }
    return ResponseEntity.ok()
        .headers(headers)
//        .contentLength((inputStreamResource == null) ? 0 : inputStreamResource.contentLength())
        .contentType(MediaType.parseMediaType("application/octet-stream"))
        .body(inputStreamResource);
  }

  public static String getDateByPath(String fullPath) {
    if (fullPath != null && !"".equals(fullPath.trim())) {
      fullPath = fullPath
          .replaceAll("[/\\\\]+", Matcher.quoteReplacement(System.getProperty("file.separator")));
      String filePath = FilenameUtils.getPath(fullPath);
      String[] paths = filePath
          .split(Matcher.quoteReplacement(System.getProperty("file.separator")));
      return paths[paths.length - 3] + File.separator + paths[paths.length - 2] + File.separator
          + paths[paths.length - 1];
    }
    return null;
  }

  public static String saveUploadFileNoDateInFile(String originalFilename, byte[] bytes,
      String uploadFolder,
      Date date)
      throws IOException {
    if (date == null) {
      date = new Date();
    }
    File file = new File(uploadFolder + File.separator + createPathByDate(date));
    if (!file.exists()) {
      file.mkdirs();
    }
    File fileWrite = new File(file.getPath() + File.separator + originalFilename);
    try (FileOutputStream fos = new FileOutputStream(fileWrite)) {
      fos.write(bytes);
      fos.close();
    }
    return file.getPath() + File.separator + originalFilename;
  }
}
