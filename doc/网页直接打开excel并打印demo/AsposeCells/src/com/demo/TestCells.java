package com.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import com.aspose.cells.License;
import com.aspose.cells.SaveFormat;
import com.aspose.cells.Workbook;

/**
 * 
 * 由于ASPOSE比较吃内存，操作大一点的文件就会堆溢出，所以请先设置好java虚拟机参数：-Xms1024m -Xmx1024m(参考值)<br>
 * 
 * 如有疑问，请在CSDN下载界面留言， 或者联系QQ569925980<br>
 * 
 * @author Spark
 *
 */
public class TestCells {

    private static InputStream license;
    private static InputStream fileInput;
    private static File outputFile;

    /**
     * 获取license
     * 
     * @return
     */
    public static boolean getLicense() {
        boolean result = false;
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            license = new FileInputStream(loader.getResource("license.xml").getPath());// 凭证文件
            fileInput = new FileInputStream(loader.getResource("mytest.xls").getPath());// 待处理的文件
            outputFile = new File("D:\\test.pdf");// 输出路径

            License aposeLic = new License();
            aposeLic.setLicense(license);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
        // 验证License
        if (!getLicense()) {
            return;
        }
        try {
            long old = System.currentTimeMillis();
            Workbook wb = new Workbook(fileInput);
            FileOutputStream fileOS = new FileOutputStream(outputFile);

            wb.save(fileOS, SaveFormat.PDF);

            long now = System.currentTimeMillis();
            System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒\n\n" + "文件保存在:" + outputFile.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}