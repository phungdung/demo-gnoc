package com.phungdung.gnoc2.common.untils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
public class FileUtils {

    public static String saveUploadFile(String originalFileName, byte[] bytes, String uploadFolder, Date date) throws IOException {
        if (date == null) {
            date = new Date();
        }
        String fileName = createFileName(originalFileName, date);
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

    public static String saveTempFileZip(List<File> files, String originalFilename, String tempFolder) throws Exception {
        ZipOutputStream out = null;
        FileInputStream in = null;
        try {
            Date date = new Date();
            String fileName = createFileName(originalFilename, date);
            File zipfile = new File(tempFolder + File.separator + createPathByDate(date));
            if (!zipfile.exists()) {
                zipfile.mkdirs();
            }
            File fileWrite = new File(tempFolder + File.separator + createPathByDate(date) + File.separator + fileName);
            byte[] buf = new byte[1024];
            //create the ZIP file
            out = new ZipOutputStream(new FileOutputStream(fileWrite));
            // comperss the files
            for (int i = 0; i < files.size(); i++) {
                in = new FileInputStream(files.get(i).getAbsolutePath());
//                Add Zip entry to outputstream
                out.putNextEntry(new ZipEntry(files.get(i).getName()));
//                transfer bytes from the file to the ZIP file
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
//                complete the entry
                out.closeEntry();
                in.close();
            }
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

    public static void coppyFile(File source, File dest) throws Exception {
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
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
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
        } catch (Exception e) {
            log.error(e.getMessage(), e);
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
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            fis.close();
        }
        return bos.toByteArray();
    }

    public static String createFileName(String originalFileName, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY) + "" + calendar.get(Calendar.MINUTE) + "" + calendar.get(Calendar.SECOND) + ""
                + calendar.get(Calendar.MILLISECOND) + "_" + originalFileName;
    }

    public static String createPathByDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String pathByDate = year + File.separator + (month < 10 ? "0" + month : month) + File.separator + (day < 10 ? "0" + day : day);
        return pathByDate;
    }

    public static String getFilePath(String fullPath) {
        if (StringUtils.isStringNotNullOrEmtry(fullPath)) {
            fullPath = fullPath.replaceAll("[/\\\\]+", Matcher.quoteReplacement(System.getProperty("file.separator")));
            return fullPath;
        }
        return null;
    }

    public static String getFileName(String fullPath) {
        if (StringUtils.isStringNotNullOrEmtry(fullPath)) {
            fullPath = fullPath.replaceAll("[/\\\\]+", Matcher.quoteReplacement(System.getProperty("file.separator")));
            return fullPath;
        }
        return null;
    }
}

