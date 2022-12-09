package makeZip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

// 1. zip 파일을 만들고 2. zip 파일에 압축할 파일들을 넣는다.
public class MakeZip {
    private static String HOME_DIR = "C:\\FILES";
    private static String ZIP_NAME_DEFAULT = "download.zip";
    private static String TARGET_DIR = "";

    public static void main(String[] args) {
        String targetFilepath = HOME_DIR + File.separator + TARGET_DIR;
        File dir = new File(targetFilepath);
        MakeZip makeZip = new MakeZip();
        try {
            // makeZip.createZip(dir, null,HOME_DIR);
            makeZip.createZip(dir.listFiles(), null, HOME_DIR);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public void createZip(File file, String zipName, String homePath) throws Exception {

        File outputFile = null;
        FileOutputStream fileOutputStream = null;
        ZipOutputStream zipOutputStream = null;
        try {
            // zip 파일 만든후 stream 연결
            if (zipName == null || "".equals(zipName)) {
                zipName = ZIP_NAME_DEFAULT;
            }

            outputFile = new File(homePath + File.separator + zipName);
            fileOutputStream = new FileOutputStream(outputFile);
            zipOutputStream = new ZipOutputStream(fileOutputStream);

            // 만들어진 zip파일에 데이터 쓰기
            if (file.isDirectory()) {
                zipForDir(file, zipOutputStream, homePath);
            } else if (file.isFile()) {
                zipForFile(file, zipOutputStream, homePath);
            }
        } catch (Exception err) {
            throw err;
        } finally {
            close(zipOutputStream);
            close(fileOutputStream);
        }
    }

    public void createZip(File[] files, String zipName, String homePath) throws Exception {

        File outputFile = null;
        FileOutputStream fileOutputStream = null;
        ZipOutputStream zipOutputStream = null;

        try {
            // zip 파일 만든후 stream 연결
            if (zipName == null || "".equals(zipName)) {
                zipName = ZIP_NAME_DEFAULT;
            }
            outputFile = new File(homePath + File.separator + zipName);
            fileOutputStream = new FileOutputStream(outputFile);
            zipOutputStream = new ZipOutputStream(fileOutputStream);

            for (File file : files) {
                String tmpName = file.getName();
                if (!"zip".equals(tmpName.substring(tmpName.lastIndexOf(".") + 1))) {
                    if (file.isFile()) {
                        zipForFile(file, zipOutputStream, homePath);
                    }
                }
            }         
        } catch (Exception e) {
            throw e;
        } finally {   
            close(zipOutputStream);         
            close(fileOutputStream);
        }
    }

    public void zipForDir(File dir, ZipOutputStream zipOutputStream, String homePath) throws Exception {
        File[] files = dir.listFiles();
        String filePath = null;
        try {
            // zip 파일에 들어갈 파일들을 넣어준다.
            if (!homePath.equals(dir.getAbsolutePath())) {
                filePath = homePath + File.separator + dir.getName();
            } else {
                filePath = homePath;
            }

            for (File file : files) {
                String tmpName = file.getName();
                if (!"zip".equals(tmpName.substring(tmpName.lastIndexOf(".") + 1))) {
                    if (file.isFile()) {
                        zipForFile(file, zipOutputStream, filePath);
                    } else {
                        zipForDir(file, zipOutputStream, filePath);
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public void zipForFile(File file, ZipOutputStream zipOutputStream, String homePath) throws Exception {
        String fileName = null;
        byte[] utf8Byte = null;
        byte[] buf = new byte[4096];
        ZipEntry zipEntry = null;
        FileInputStream fileInputStream = null;
        int read;
        try {
            fileName = homePath + File.separator + file.getName();
            utf8Byte = fileName.getBytes("UTF-8");
            zipEntry = new ZipEntry(new String(utf8Byte, "UTF-8"));
            zipOutputStream.putNextEntry(zipEntry);
            fileInputStream = new FileInputStream(file);
            
            while ((read = fileInputStream.read(buf, 0, 4096)) > 0) {
                zipOutputStream.write(buf, 0, read);
            }
            zipOutputStream.flush();
        } catch (Exception e) {
            throw e;
        } finally {
            closeEntry(zipOutputStream);
            close(fileInputStream);
        }
    }

    public static void closeEntry(ZipOutputStream zipOutputStream) throws Exception {
        try {
            if (zipOutputStream != null) {
                zipOutputStream.closeEntry();
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public static void close(ZipOutputStream zipOutputStream) throws Exception {
        try {
            if (zipOutputStream != null) {
                zipOutputStream.close();
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public static void close(FileInputStream fileInputStream) throws Exception {
        try {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public static void close(FileOutputStream fileOutputStream) throws Exception {
        try {
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
