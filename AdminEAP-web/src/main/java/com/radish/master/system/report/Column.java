package com.radish.master.system.report;

/**
 * <pre>
 * Modify Information:
 * Author       Date        Description
 * ============ =========== ============================
 * dongyan      2018-04-27  Create this file
 * 
 * </pre>
 */
public class Column {
    public static final int ALIGN_LEFT = 1;
    public static final int ALIGN_CENTER = 2;
    public static final int ALIGN_RIGHT = 3;

    private int align = ALIGN_CENTER;
    private int width = 4000;
    
    public Column() {
    }
    
    public Column(int align, int width) {
        this.align = align;
        this.width = width;
    }

    public int getAlign() {
        return align;
    }

    public void setAlign(int align) {
        this.align = align;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

}
