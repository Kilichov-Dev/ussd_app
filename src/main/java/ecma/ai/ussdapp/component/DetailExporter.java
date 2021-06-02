package ecma.ai.ussdapp.component;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.pdf.PdfWriter;
import ecma.ai.ussdapp.entity.Details;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;

@Component
public class DetailExporter {

    public void exportPDF(HttpServletResponse response, List<Details> detailsList, String code) throws IOException {

        Document document = new Document(PageSize.A4);

        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        document.add(new Paragraph("Information by SimCard:" + code));

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setSpacingBefore(15);

        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.white);

        cell.setPhrase(new Phrase("№", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Actiontype", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Amount", font));
        table.addCell(cell);

        int i = 1;
        for (Details details : detailsList) {
            table.addCell(String.valueOf(i));
            table.addCell(details.getActionType().name());
            table.addCell(String.valueOf(details.getAmount()));
            i++;
        }

        document.add(table);
        document.close();

    }

    public void exportExcel(HttpServletResponse response, List<Details> detailsList) throws IOException {

        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFSheet sheet = workbook.createSheet("Details");

        XSSFRow row = sheet.createRow(0);

        XSSFCell cell = row.createCell(0);
        cell.setCellValue("№");

        cell = row.createCell(1);
        cell.setCellValue("ActionType");

        cell = row.createCell(2);
        cell.setCellValue("Amount");

        int rowCount = 1;

        for (Details details : detailsList) {
            XSSFRow xssfRow = sheet.createRow(rowCount++);

            XSSFCell xssfCell = xssfRow.createCell(0);
            xssfCell.setCellValue(rowCount);
            sheet.autoSizeColumn(0);


            xssfCell = xssfRow.createCell(1);
            xssfCell.setCellValue(details.getActionType().name());
            sheet.autoSizeColumn(1);

            xssfCell = xssfRow.createCell(2);
            xssfCell.setCellValue(details.getAmount());
            sheet.autoSizeColumn(2);
        }

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }


}
