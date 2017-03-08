package com.yiche.ycanalytics.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.os.Environment;

/**
 * 文件操作帮助类，判断文件是否存在，移除文件、创建目录、读取等操作
 * 
 * @author wanglirong
 * 
 */
public final class FileHelper
{

    /**
     * 判断文件是否存在
     * 
     * @param filepath
     * @return
     */
    public static boolean fileIfExists(String filepath)
    {
        File file = new File(filepath);
        if (file.exists())
            return true;
        return false;
    }

    /**
     * 删除文件
     * 
     * @param filepath
     * @return
     */
    public static boolean removeFile(String filepath)
    {

        File file = new File(filepath);
        if (file.exists())
        {
            return removeFile(file);
        }
        return false;
    }

    /**
     * 从JSON文件中返回JSON字符串
     * 
     * @param filePath
     * @return
     */
    public static String getJsonStringFromJsonFile(String filePath)
    {

        String result = "";
        if (fileIfExists(filePath))
        {
            FileInputStream fis = null;
            ByteArrayOutputStream outBa = null;
            DataOutputStream dos = null;
            try
            {
                fis = new FileInputStream(new File(filePath));
                outBa = new ByteArrayOutputStream();
                dos = new DataOutputStream(outBa);
                int currentCount = 0;
                byte[] tempBuffer = new byte[1024];
                while ((currentCount = fis.read(tempBuffer)) != -1)
                {
                    dos.write(tempBuffer, 0, currentCount);
                }
                dos.flush();
                result = outBa.toString();
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally
            {

            }

        }
        return result;
    }

    /**
     * 删除文件及其子文件
     * 
     * @param file
     * @return
     */
    public static boolean removeFile(File file)
    {
        if (file != null && file.isDirectory())
        {

            String[] childlist = file.list();

            for (int i = 0; i < childlist.length; i++)
            {
                boolean success = removeFile(new File(file, childlist[i]));

                if (!success)
                {
                    return false;
                }
            }
        }
        else if (file != null)
        {
            return file.delete();
        }
        return false;
    }

    /**
     * 获取文件大小
     * 
     * @param filepath
     * @return
     */
    public static long getFileSize(String filepath)
    {
        File f = null;
        FileInputStream fs = null;
        byte[] buffer = new byte[512];
        int totalCount = 0;
        try
        {
            f = new File(filepath);
            fs = new FileInputStream(f);

            int currentReadCount = 0;

            while ((currentReadCount = fs.read(buffer)) != -1)
            {
                totalCount += currentReadCount;
            }

            fs.close();

        }
        catch (Exception e)
        {

        }

        return totalCount;
    }

    /**
     * 判断是否有Sdcard
     * 
     * @return
     */
    public static boolean isSupportSDCard()
    {
        String status = Environment.getExternalStorageState();

        if (status.equals(Environment.MEDIA_MOUNTED))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * 创建文件夹
     * 
     * @param filepath
     * @return
     */
    public static boolean createDirectory(String filepath)
    {
        boolean r = isSupportSDCard();
        File file = null;
        if (r)
        {
            file = Environment.getExternalStorageDirectory();
        }
        else
        {
            // file = ReaderApplication.instance().getFilesDir();
        }

        MyLogger.getLogger("FileHelper").v(file.toString());

        File newfile = new File(file, filepath);

        if (!newfile.exists())
            r = newfile.mkdirs();

        return r;
    }

}
