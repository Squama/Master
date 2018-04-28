package com.radish.master.system;

import java.io.FileOutputStream;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.radish.master.system.report.Column;



/**
 * 
 * <pre>
 * Modify Information:
 * Author       Date        Description
 * ============ =========== ============================
 * dongyan       2018-04-27  Create this file
 * </pre>
 * 
 * <pre>
 * 报表格式：
 *                              Title
 * Subhead
 * +----------------+--------------+------------+----------+--------+
 * |     Header     |    Header    |   Header   |  Header  | Header |
 * +----------------+--------------+------------+----------+--------+
 * |String          |String        |      Number|   Date   |String  |
 * +----------------+--------------+------------+----------+--------+
 * |String          |String        |      Number|   Date   |String  |
 * +----------------+--------------+------------+----------+--------+
 * |String          |String        |      Number|   Date   |String  |
 * +----------------+--------------+------------+----------+--------+
 * 说明：不支持分页
 * </pre>
 * 
 */
public class ReportSXSSF {
    
    // 标题
    private String title;
    // 副标题
    private String subhead;

    // 列数组
    private Column[] columns;
    // 报表数据
    private ArrayList<String[]> list;

    // 标题风格
    private CellStyle styleTitle;
    // 副标题风格
    private CellStyle styleSubhead;
    // 第一行风格
    private CellStyle[] styleHeaders;
    // 最后一行风格
    private CellStyle styleFoot;
    // 其他列的风格
    private CellStyle[] styles;

    private Workbook wb;
    private Sheet sheet;

    private int rowNum;
    private int colNum;

    /**
     * @param title
     *            String 标题
     * @param subhead
     *            String 子标题
     * @param columns
     *            Column[] 列对象数组
     * @param list
     *            ArrayList 报表数据
     */
    public ReportSXSSF(String title, String subhead, Column[] columns, ArrayList<String[]> list) {
        this.title = title;
        this.subhead = subhead;
        this.columns = columns;
        this.list = list;
    }

    /**
     * 把报表导出到OutputStream
     * 
     * @param stream
     *            OutputStream
     * @throws Exception
     * @since 1.2
     */
    public void export(java.io.OutputStream stream) throws Exception {
        try {
            generate();
            doExport(stream);
        } catch (Exception e) {
            throw e;
        }
    }

    public void export(String path) throws Exception {
        try {
            generate();
            FileOutputStream fos = new FileOutputStream(path);
            doExport(fos);
        } catch (Exception e) {
            throw e;
        }
    }

    public void doExport(java.io.OutputStream stream) throws Exception {
        try {
            wb.write(stream);

            // 释放内存
            wb = null;
        } catch (Exception e) {
            throw e;
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    public Workbook generate() throws Exception {
        CellStyle style;
        Row row;
        Cell cell;
        int rowNo = 0;
        try {
            init();

            // 合并一行，主标题
            sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 0, colNum - 1));
            row = sheet.createRow(rowNo);
            
            row.setHeight((short) 500);
            cell = row.createCell(0);
            cell.setCellStyle(styleTitle);
            cell.setCellValue(title);

            if (subhead != null && subhead.trim().length() > 0) {
                // 合并一行，副标题
                rowNo++;
                
                
                sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 0, colNum - 1));
                row = sheet.createRow(rowNo);
                row.setHeight((short) 400);
                cell = row.createCell(0);
                cell.setCellStyle(styleSubhead);
                cell.setCellValue(subhead);
            }

            for (int i = 0; i < rowNum; i++) {
                rowNo++;
                row = sheet.createRow(rowNo);
                row.setHeight((short) 400);
                String[] data = (String[]) list.get(i);
                
                for (int j = 0; j < colNum; j++) {
                    if (i == 0) {
                        style = styleHeaders[j];
                    } else if (i == (rowNum - 1)) {
                        style = styleFoot;
                    } else {
                        style = styles[j];
                    }
                    cell = row.createCell(j);
                    
                    cell.setCellStyle(style);
                    cell.setCellValue(data[j]);
                }
            }

            // 释放内存
            list = null;
            return wb;
        } catch (Exception e) {
            throw e;
        }
    }

    private void init() {
        Font font;
        wb = new SXSSFWorkbook();
        SXSSFWorkbook sxssfwb = (SXSSFWorkbook) wb;
        sxssfwb.setCompressTempFiles(true);
        sheet = wb.createSheet("Powered by POI");
        sheet.setMargin(Sheet.TopMargin, 0.4);
        sheet.setMargin(Sheet.BottomMargin, 0.2);
        sheet.setMargin(Sheet.LeftMargin, 0.8);
        sheet.setMargin(Sheet.RightMargin, 0.2);
        // 页面设置
        PrintSetup printSetup = sheet.getPrintSetup();
        // 纸张大小
        printSetup.setPaperSize(PrintSetup.A4_PAPERSIZE);
        // 打印方向(横向=true, 纵向=false)
        printSetup.setLandscape(false);
        rowNum = list.size();
        colNum = columns.length;
        // 设置列宽
        for (int i = 0; i < colNum; i++) {
            sheet.setColumnWidth(i, columns[i].getWidth());
        }
        // 标题风格  font
        font = wb.createFont();
        font.setFontHeightInPoints((short) 16);
        font.setFontName("宋体");
        // 粗体显示
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        styleTitle = wb.createCellStyle();
        styleTitle.setAlignment(CellStyle.ALIGN_CENTER);
        styleTitle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        styleTitle.setWrapText(false);
        styleTitle.setFont(font);
        // 副标题风格
        // font
        font = wb.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setFontName("宋体");
        styleSubhead = wb.createCellStyle();
        styleSubhead.setAlignment(CellStyle.ALIGN_LEFT);
        styleSubhead.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        styleSubhead.setWrapText(false);
        styleSubhead.setFont(font);
        // 第一行风格
        // font
        font = wb.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setFontName("宋体");
        // 粗体显示
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        styleHeaders = new CellStyle[colNum];
        for (int i = 0; i < colNum; i++) {
            styleHeaders[i] = wb.createCellStyle();
            
            int align = columns[i].getAlign();
            if (align == Column.ALIGN_CENTER) {
                styleHeaders[i].setAlignment(CellStyle.ALIGN_CENTER);
            }
            if (align == Column.ALIGN_LEFT) {
                styleHeaders[i].setAlignment(CellStyle.ALIGN_LEFT);
            }
            if (align == Column.ALIGN_RIGHT) {
                styleHeaders[i].setAlignment(CellStyle.ALIGN_RIGHT);
            }
            styleHeaders[i].setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            styleHeaders[i].setWrapText(false);
            styleHeaders[i].setFont(font);
            styleHeaders[i].setBorderBottom((short) 1);
            styleHeaders[i].setBorderTop((short) 1);
            styleHeaders[i].setBorderLeft((short) 1);
            styleHeaders[i].setBorderRight((short) 1);
        }
        // 最后一行风格
        // font
        font = wb.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setFontName("宋体");
        // 粗体显示
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        styleFoot = wb.createCellStyle();
        styleFoot.setAlignment(CellStyle.ALIGN_RIGHT);
        styleFoot.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        styleFoot.setWrapText(false);
        styleFoot.setFont(font);
        styleFoot.setBorderBottom((short) 1);
        styleFoot.setBorderTop((short) 1);
        styleFoot.setBorderLeft((short) 1);
        styleFoot.setBorderRight((short) 1);
        // 其他列风格
        // font
        font = wb.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setFontName("宋体");
        styles = new CellStyle[colNum];
        for (int i = 0; i < colNum; i++) {
            styles[i] = wb.createCellStyle();
            int align = columns[i].getAlign();

            if (align == Column.ALIGN_CENTER) {
                styles[i].setAlignment(CellStyle.ALIGN_CENTER);
            }
            if (align == Column.ALIGN_LEFT) {
                styles[i].setAlignment(CellStyle.ALIGN_LEFT);
            }
            if (align == Column.ALIGN_RIGHT) {
                styles[i].setAlignment(CellStyle.ALIGN_RIGHT);
            }
            styles[i].setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            styles[i].setWrapText(false);
            styles[i].setFont(font);
            styles[i].setBorderBottom((short) 1);
            styles[i].setBorderTop((short) 1);
            styles[i].setBorderLeft((short) 1);
            styles[i].setBorderRight((short) 1);
        }
    }

}
