package net.caidingke.common.excel;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

/**
 * 动态写入Excel
 *
 * @author bowen
 * @create 2016-07-13 16:30
 */

public class WritableExcel {

    // 数据列表
    private List<?> list;

    // 属性名称集
    private String[] props;

    // 标题集
    private String[] title;

    // 动态 Bean
    private DynamicBean dynaBean;

    // 工作表
    private XSSFSheet sheet;

    // 工作表的名称
    private String sheetName;

    // 工作簿
    private XSSFWorkbook workbook;

    // 数据格式
    private static Map<Class<?>, String> dataFormat;

    /**
     * 构造一个可写的 Excel 实例
     *
     * @param list
     *            数据列表
     * @param mapper
     *            数据对象的属性名称与Excel文档标题之间的映射关系
     */
    public WritableExcel(List<?> list, LinkedHashMap<String, String> mapper) {
        init(list, mapper);
    }

    /**
     * 写出到文件
     *
     * @param file
     *            文件对象
     */
    public boolean write(File file) {
        try {
            return write(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 写出到输出流
     *
     * @param out
     *            输出流
     */
    public boolean write(OutputStream out) {
        try {
            // 创建工作表
            sheet = workbook.createSheet(sheetName);
            // 构建并填充标题行
            fillTitleRow();
            // 主体行的索引
            int rowIndex = 1;
            for (Object bean : list) {
                // 创建并填充行
                fillCreatedRow(rowIndex++, bean);
            }
            workbook.write(out);
            return true;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {}
        }
    }

    /**
     * 设置工作表的名称, 默认名称 Sheet1
     *
     * @param sheetName
     *            工作表的名称
     */
    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    /**
     * 设置日期格式, 默认格式 yyyy-MM-dd
     *
     * @param dateFormat
     *            日期格式
     */
    public void setDateFormat(String dateFormat) {
        dataFormat.put(Date.class, dateFormat);
    }

    /**
     * 初始化
     *
     * @param list
     *            数据列表
     * @param mapper
     *            数据对象的属性名称与Excel文档标题之间的映射关系
     */
    private void init(List<?> list, LinkedHashMap<String, String> mapper) {
        this.list = list;
        int index = 0;
        int size = mapper.size();
        props = new String[size];
        title = new String[size];
        // 迭代取出属性名称集和标题集
        for (String prop : mapper.keySet()) {
            props[index] = prop;
            title[index] = mapper.get(prop);
            index++;
        }
        // 工作簿
        this.workbook = new XSSFWorkbook();
        // 动态 Bean
        this.dynaBean = new DynamicBean(list.get(0).getClass());
        // 默认的工作表名称
        this.sheetName = "Sheet1";
    }

    /**
     * 构建并填充标题行
     *
     * @throws Throwable
     */
    private void fillTitleRow() throws Throwable {
        // 单元格索引
        int cellIndex = 0;
        // 单元格宽度
        int cellWidth = 18 * 256;
        // 创建一行
        XSSFRow row = sheet.createRow(0);
        // 设置行高
        row.setHeightInPoints(25);
        // 标题行样式
        CellStyle style = getTitleCellStyle();
        // 迭代标题集
        for (String text : title) {
            // 设置宽度
            sheet.setColumnWidth(cellIndex, cellWidth);
            // 创建单元格
            XSSFCell cell = row.createCell(cellIndex++);
            // 设置单元格样式
            cell.setCellStyle(style);
            // 设置单元格的值
            cell.setCellValue(text);
        }
    }

    /**
     * 创建并填充行
     *
     * @param index
     *            行的索引
     * @param bean
     *            填充行的对象
     * @throws Throwable
     */
    private void fillCreatedRow(int index, Object bean) throws Throwable {
        // 单元格索引
        int cellIndex = 0;
        // 创建一行
        XSSFRow row = sheet.createRow(index);
        // 设置行高
        row.setHeightInPoints(20);
        // 迭代 Bean 属性集
        for (String prop : props) {
            // 属性的值
            Object value = dynaBean.getFieldValue(bean, prop);
            // 属性类型
            Class<?> type = dynaBean.getFieldType(prop);
            // 创建单元格
            XSSFCell cell = row.createCell(cellIndex++);
            // 值为空
            if (value == null) {
                // 设置为空串
                cell.setCellValue("");
                // 单元格样式
                cell.setCellStyle(getBodyCellStyle(String.class));
            } else {
                // 按类型设值
                if (type == Boolean.TYPE || type == Boolean.class) {
                    boolean boolValue = Boolean.parseBoolean(value.toString());
                    cell.setCellValue(boolValue);
                } else if ((Number.class.isAssignableFrom(type) || type.isPrimitive())
                        && type != Byte.TYPE && type != Character.TYPE) {
                    double doubleValue = Double.parseDouble(value.toString());
                    cell.setCellValue(doubleValue);
                } else if (type == Date.class) {
                    Date dateValue = (Date) value;
                    cell.setCellValue(dateValue);
                } else {
                    cell.setCellValue(value.toString());
                }
                // 单元格样式
                cell.setCellStyle(getBodyCellStyle(type));
            }
        }
    }

    /**
     * 主体单元格样式
     *
     * @param type
     *            单元格填充的数据的类型
     * @return CellStyle
     */
    private CellStyle getBodyCellStyle(Class<?> type) {

        // 单元格样式
        CellStyle style = workbook.createCellStyle();

        // 单元格数据格式
        String cellDataFormat = dataFormat.get(type);
        if (cellDataFormat == null) {
            cellDataFormat = dataFormat.get(String.class);
        }

        // 水平居中
        style.setAlignment(CellStyle.ALIGN_CENTER);
        // 垂直居中
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

        // 背景颜色
        setBackgroundColor(style, IndexedColors.LIGHT_TURQUOISE.index);

        // 字体
        style.setFont(getFont(11, IndexedColors.GREY_50_PERCENT.index));

        // 数据格式
        style.setDataFormat(workbook.createDataFormat().getFormat(cellDataFormat));

        // 自动换行
        style.setWrapText(true);

        return style;
    }

    /**
     * 标题行样式
     *
     * @return CellStyle
     */
    private CellStyle getTitleCellStyle() {

        // 单元格样式
        CellStyle style = workbook.createCellStyle();

        // 水平居中
        style.setAlignment(CellStyle.ALIGN_CENTER);
        // 垂直居中
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

        // 背景颜色
        setBackgroundColor(style, IndexedColors.YELLOW.index);

        // 字体
        style.setFont(getFont(12, IndexedColors.BLUE_GREY.index));

        // 数据格式
        style.setDataFormat(workbook.createDataFormat().getFormat("GENERAL"));

        // 自动换行
        style.setWrapText(true);

        return style;
    }

    /**
     * 设置单元格背景颜色
     *
     * @param style
     *            单元格样式
     * @param color
     *            颜色值
     */
    private void setBackgroundColor(CellStyle style, short color) {
        // 边框设置
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);
        // 边框颜色
        style.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.index);
        style.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.index);
        style.setRightBorderColor(IndexedColors.GREY_25_PERCENT.index);
        style.setTopBorderColor(IndexedColors.GREY_25_PERCENT.index);
        // 背景颜色
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(color);
    }

    /**
     * 获取字体
     *
     * @param fontSize
     *            字体大小
     * @param fontColor
     *            字体颜色
     * @return Font
     */
    private Font getFont(int fontSize, short fontColor) {
        // 创建字体
        Font font = workbook.createFont();
        // 字体颜色
        font.setColor(fontColor);
        // 字体大小
        font.setFontHeightInPoints((short) fontSize);
        // 字体名称
        if (System.getProperty("os.name").contains("Windows")) {
            font.setFontName("Microsoft YaHei");
        }
        return font;
    }

    // 数据格式
    static {
        dataFormat = new HashMap<Class<?>, String>();
        dataFormat.put(Short.TYPE, "0");
        dataFormat.put(Short.class, "0");
        dataFormat.put(Integer.TYPE, "0");
        dataFormat.put(Integer.class, "0");
        dataFormat.put(Long.TYPE, "0");
        dataFormat.put(Long.class, "0");
        dataFormat.put(Float.TYPE, "0.00");
        dataFormat.put(Float.class, "0.00");
        dataFormat.put(Double.TYPE, "0.00");
        dataFormat.put(Double.class, "0.00");
        dataFormat.put(String.class, "GENERAL");
        dataFormat.put(Date.class, "yyyy-MM-dd");
    }

}
