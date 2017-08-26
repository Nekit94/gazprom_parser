import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

class Excel {
    private static HSSFWorkbook BOOK = new HSSFWorkbook();
    private static Sheet SHEET = BOOK.createSheet();
    private static int rowCount = 0;
    private static Row row = SHEET.createRow(0);
    private static CreationHelper createHelper = BOOK.getCreationHelper();

    static void writeToXls(int cell, String data) throws IOException {
        if (rowCount == 0) {
            row.createCell(0).setCellValue("№");
            row.createCell(1).setCellValue("Номер извещения");
            row.createCell(2).setCellValue("Уникальный номер закупки");
            row.createCell(3).setCellValue("Номер закупки");
            row.createCell(4).setCellValue("Наименование закупки");
            row.createCell(5).setCellValue("Валюта процедуры");
            row.createCell(6).setCellValue("Начальная цена с НДС");
            row.createCell(7).setCellValue("Предмет договора");
            row.createCell(8).setCellValue("Наименование заказчика");
            row.createCell(9).setCellValue("Дата публикации");
            row.createCell(10).setCellValue("Дата и время окончания срока приема заявок");
            row.createCell(11).setCellValue("Номер лота");
            row.createCell(12).setCellValue("Ссылка на протокол");
            row.createCell(13).setCellValue("Ссылка на тендер");


        }
        if (Parse.ROW != rowCount) {
            row = SHEET.createRow(Parse.ROW);
            rowCount = Parse.ROW;
            row.createCell(0).setCellValue(Parse.ROW);
        }
        if (cell == 12 || cell == 13) {
            CellStyle hlink_style = BOOK.createCellStyle();
            Font hlink_font = BOOK.createFont();
            hlink_font.setUnderline(Font.U_SINGLE);
            hlink_font.setColor(IndexedColors.BLUE.getIndex());
            hlink_style.setFont(hlink_font);
            Hyperlink link = createHelper.createHyperlink(HyperlinkType.URL);
            link.setAddress(data);
            Cell cellLink = row.createCell(cell);
            cellLink.setHyperlink(link);
            switch (cell) {
                case 12: cellLink.setCellValue("Протокол"); break;
                case 13: cellLink.setCellValue("Тендер"); break;
            }
            cellLink.setCellStyle(hlink_style);
        } else {
            row.createCell(cell).setCellValue(data);
        }
    }

    static void createXls() throws IOException {
        try {
            FileOutputStream output = new FileOutputStream(new File(mainFrame.getFileName() + ".xls"));
            BOOK.write(output);
            output.close();
        } catch (FileNotFoundException e) {
            FileOutputStream output = new FileOutputStream(new File("close_excel_before_start.xls"));
            BOOK.write(output);
            output.close();
        }
    }
}
