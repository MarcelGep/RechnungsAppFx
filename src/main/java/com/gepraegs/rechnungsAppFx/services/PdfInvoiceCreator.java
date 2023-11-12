package com.gepraegs.rechnungsAppFx.services;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.gepraegs.rechnungsAppFx.Position;
import com.gepraegs.rechnungsAppFx.model.AddressDetails;
import com.gepraegs.rechnungsAppFx.model.FooterEventHandler;
import com.gepraegs.rechnungsAppFx.model.HeaderEventHandler;
import com.gepraegs.rechnungsAppFx.model.InvoiceDetails;
import com.gepraegs.rechnungsAppFx.model.ProductTableHeader;
import com.gepraegs.rechnungsAppFx.utils.ConstantUtil;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.DoubleBorder;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

public class PdfInvoiceCreator {
    public static final String BULLET_POINT = " \u2022 ";

    Document document;
    PdfDocument pdfDocument;
    String pdfName;
    float threecol = 190f;
    float twocol = 285f;
    float twocol150 = twocol + 150f;
    float oneColumnWidth[] = { twocol150 };
    float twoColumnWidth[] = { twocol150, 180f };
    float threeColumnWidth[] = { 100f, 100f, 100f };
    float sevenColumnWidth[] = { 50, 100, 350, 80, 100, 150, 150 };
    float fullwidth[] = { threecol * 3 };
    DecimalFormat df = new DecimalFormat("0.00 €", new DecimalFormatSymbols(Locale.GERMAN));

    public PdfInvoiceCreator(String pdfName) {
        this.pdfName = pdfName;
    }

    public void createDocument(String imagePath, List<String> footerData, ProductTableHeader productTableHeader)
            throws FileNotFoundException {
        PdfWriter pdfWriter = new PdfWriter(pdfName);
        pdfDocument = new PdfDocument(pdfWriter);
        pdfDocument.setDefaultPageSize(PageSize.A4);
        document = new Document(pdfDocument);

        HeaderEventHandler headerEventHandler = new HeaderEventHandler(document, imagePath, productTableHeader);
        pdfDocument.addEventHandler(PdfDocumentEvent.START_PAGE, headerEventHandler);

        FooterEventHandler footerEventHandler = new FooterEventHandler(document, footerData);
        pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, footerEventHandler);

        float topMargin = 40 + headerEventHandler.getTableHeight();
        float bottomMargin = 20 + footerEventHandler.getTableHeight();
        document.setMargins(topMargin, 50, bottomMargin, 50);
    }

    public void closeDocument() {
        document.close();
    }

    public void addPositionList(ProductTableHeader header, List<Position> positions) {
        Table table = new Table(sevenColumnWidth);
        createTableHeader(table, header);
        createEmptyRow(table);
        createPositions(table, positions);
        document.add(table);
        document.add(createTotalPrice(positions));
    }

    public void createTableHeader(Table table, ProductTableHeader header) {
        table.addCell(
                getPositionTableHeaderCell(header.getPos(), TextAlignment.LEFT, true)
                        .setBorderBottom(new SolidBorder(0.5f)));
        table.addCell(getPositionTableHeaderCell(header.getArtNr(), TextAlignment.LEFT, true));
        table.addCell(getPositionTableHeaderCell(header.getDescription(), TextAlignment.LEFT, true));
        table.addCell(getPositionTableHeaderCell(header.getQuantity(), TextAlignment.LEFT, true));
        table.addCell(getPositionTableHeaderCell(header.getUnit(), TextAlignment.LEFT, true));
        table.addCell(getPositionTableHeaderCell(header.getSinglePrice(), TextAlignment.RIGHT, true));
        table.addCell(getPositionTableHeaderCell(header.getTotalPrice(), TextAlignment.RIGHT, true));
    }

    public void createEmptyRow(Table table) {
        for (int i = 0; i < 7; i++) {
            table.addCell(
                    getPositionTableCell(ConstantUtil.EMPTY, TextAlignment.LEFT, false));
        }
    }

    public void createPositions(Table table, List<Position> positions) {
        for (Position position : positions) {
            table.addCell(
                    getPositionTableCell(String.valueOf(positions.indexOf(position) + 1), TextAlignment.LEFT, false));
            table.addCell(getPositionTableCell(String.valueOf(position.getArtNr()), TextAlignment.LEFT, false));
            table.addCell(
                    getPositionTableCell(String.valueOf(position.getDescription()), TextAlignment.LEFT, false));
            table.addCell(getPositionTableCell(String.valueOf(position.getQuantity()), TextAlignment.CENTER, false));
            table.addCell(getPositionTableCell(String.valueOf(position.getUnit()), TextAlignment.LEFT, false));
            table.addCell(getPositionTableCell(df.format(position.getPriceExcl()), TextAlignment.RIGHT, false));
            double total = position.getQuantity() * position.getPriceIncl();
            table.addCell(getPositionTableCell(df.format(total), TextAlignment.RIGHT, false));
        }
    }

    public Table createTotalPrice(List<Position> positions) {
        float columnWidths[] = { 650f, 150f, 400f };
        Table threeColumnTable = new Table(columnWidths);
        double totalSumIncl = getTotalSumIncl(positions);
        double totalSumExcl = getTotalSumExcl(positions);
        double totalSumUst = totalSumIncl - totalSumExcl;

        threeColumnTable.addCell(new Cell().add(new Paragraph(ConstantUtil.EMPTY))
                .setBorder(Border.NO_BORDER));
        threeColumnTable.addCell(new Cell().add(new Paragraph("Nettobetrag:").setFontSize(10))
                .setTextAlignment(TextAlignment.LEFT)
                .setBorderTop(new SolidBorder(0.6f))
                .setBorder(Border.NO_BORDER));
        threeColumnTable.addCell(new Cell().add(new Paragraph(df.format(totalSumExcl)).setFontSize(10))
                .setBold()
                .setTextAlignment(TextAlignment.RIGHT)
                .setBorderTop(new SolidBorder(0.6f))
                .setBorder(Border.NO_BORDER));

        threeColumnTable.addCell(new Cell().add(new Paragraph(ConstantUtil.EMPTY))
                .setBorder(Border.NO_BORDER));
        threeColumnTable.addCell(new Cell().add(new Paragraph("MwSt. 19%:").setFontSize(10))
                .setTextAlignment(TextAlignment.LEFT)
                .setBorder(Border.NO_BORDER));
        threeColumnTable.addCell(new Cell().add(new Paragraph(df.format(totalSumUst)).setFontSize(10))
                .setBold()
                .setTextAlignment(TextAlignment.RIGHT)
                .setBorder(Border.NO_BORDER));

        threeColumnTable.addCell(new Cell().add(new Paragraph(ConstantUtil.EMPTY))
                .setBorder(Border.NO_BORDER));
        threeColumnTable.addCell(new Cell().add(new Paragraph("Gesamtbetrag:").setFontSize(10))
                .setBold()
                .setTextAlignment(TextAlignment.LEFT)
                .setBorderTop(new DoubleBorder(3f))
                .setBorder(Border.NO_BORDER));
        threeColumnTable.addCell(new Cell().add(new Paragraph(df.format(totalSumIncl)).setFontSize(10))
                .setBold()
                .setTextAlignment(TextAlignment.RIGHT)
                .setBorderTop(new DoubleBorder(3f))
                .setBorder(Border.NO_BORDER));

        return threeColumnTable.setMarginTop(15);
    }

    public float getTotalSumIncl(List<Position> positionList) {
        return (float) positionList.stream().mapToLong((p) -> (long) (p.getQuantity() * p.getPriceIncl())).sum();
    }

    public float getTotalSumExcl(List<Position> positionList) {
        return (float) positionList.stream().mapToLong((p) -> (long) (p.getQuantity() * p.getPriceExcl())).sum();
    }

    public List<Position> getDummyPositionList(int size) {
        List<Position> positionList = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            positionList.add(new Position("100" + i, "105" + i, "Produkt " + i, "Stück", "", 2, 100, 119, 19));

        }
        return positionList;
    }

    public void addAddress(AddressDetails addressDetails) {
        String shippingAddress = addressDetails.getShippingCompany() + BULLET_POINT
                + addressDetails.getShippingAddress() + BULLET_POINT
                + addressDetails.getShippingZipCity();

        Table twoColTable = new Table(twoColumnWidth);
        twoColTable.addCell(getCellLeft(shippingAddress, 6));
        twoColTable.addCell(getCell10Left(addressDetails.getShippingCompany(), false));
        twoColTable.addCell(getCell10Left(addressDetails.getBillingCompany(), false));
        twoColTable.addCell(getCell10Left(addressDetails.getShippingContactPerson(), false));
        twoColTable.addCell(getCell10Left(addressDetails.getBillingContactPersion(), false));
        twoColTable.addCell(getCell10Left(addressDetails.getShippingAddress(), false));
        twoColTable.addCell(getCell10Left(addressDetails.getBillingAddress(), false));
        twoColTable.addCell(getCell10Left(addressDetails.getShippingZipCity(), false));
        twoColTable.addCell(getCell10Left(addressDetails.getBillingZipCity(), false));
        twoColTable.addCell(getCell10Left(ConstantUtil.EMPTY, false));
        twoColTable.addCell(getCell10Left(ConstantUtil.EMPTY, false));
        twoColTable.addCell(getCell10Left(addressDetails.getShippingPhone(), false));
        twoColTable.addCell(getCell10Left(ConstantUtil.EMPTY, false));
        twoColTable.addCell(getCell10Left(addressDetails.getShippingEmail(), false));
        twoColTable.addCell(getCell10Left(ConstantUtil.EMPTY, false));

        document.add(twoColTable);
    }

    public void addInvoiceDetails(InvoiceDetails invoiceDetails) {
        document.add(new Paragraph(invoiceDetails.getInvoiceTitle()).setFontSize(14).setBold());

        Table twoColTableInvoiceInformations = new Table(UnitValue.createPercentArray(new float[] { 68, 31 }));
        twoColTableInvoiceInformations
                .addCell(getCell10Left(invoiceDetails.getInvoiceNoText() + " " + invoiceDetails.getInvoiceNo(), false)
                        .setPaddingLeft(0.5f));

        Table twoColTableRight = new Table(UnitValue.createPercentArray(2));
        twoColTableRight.addCell(getCell10Left(invoiceDetails.getInvoiceDateText(), false));
        twoColTableRight.addCell(getCell10Left(invoiceDetails.getInvoiceDate(), false));
        twoColTableRight.addCell(getCell10Left(invoiceDetails.getShippingDateText(), false));
        twoColTableRight.addCell(getCell10Left(invoiceDetails.getShippingDate(), false));
        twoColTableRight.addCell(getCell10Left(invoiceDetails.getCustomerNoText(), false));
        twoColTableRight.addCell(getCell10Left(invoiceDetails.getCustomerNo(), false));

        twoColTableInvoiceInformations.addCell(new Cell().add(twoColTableRight).setBorder(Border.NO_BORDER));

        document.add(twoColTableInvoiceInformations.setMarginBottom(20));
    }

    public void addVSpace(float height) {
        document.add(getDividerTable(fullwidth).setMarginBottom(height));
    }

    public void addNewLine() {
        document.add(new Paragraph("\n"));
    }

    public void addTextLine(String text) {
        document.add(new Paragraph(text).setFontSize(10));
    }

    static Table getDividerTable(float[] fullwidth) {
        return new Table(fullwidth);
    }

    static Table getSeparaterLine(float[] width1) {
        float width[] = { 300f };
        Table tableDivider = new Table(width);
        Border dgb = new SolidBorder(ColorConstants.GRAY, 0.5f);
        tableDivider.setBorder(dgb);
        return tableDivider;
    }

    static Cell getCellLeft(String textValue, double fontSize) {

        return new Cell().add(new Paragraph(textValue)).setFontSize((float) fontSize).setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.LEFT).setMarginBottom(20f);
    }

    static Cell getCell10Left(String textValue, Boolean isBold) {
        Cell myCell = new Cell().add(new Paragraph(textValue)).setFontSize(10).setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.LEFT).setPaddingBottom(-4).setBorder(Border.NO_BORDER);
        return isBold ? myCell.setBold() : myCell;

    }

    static Cell getPositionTableHeaderCell(String textValue, TextAlignment textAlignment, Boolean isBold) {
        Cell myCell = new Cell().add(new Paragraph(textValue).setFontSize(10))
                .setTextAlignment(textAlignment)
                .setBorder(Border.NO_BORDER)
                .setBorderBottom(new SolidBorder(0.6f))
                .setBorderTop(new SolidBorder(0.6f))
                .setPaddingTop(7)
                .setPaddingBottom(7);
        return isBold ? myCell.setBold() : myCell;
    }

    static Cell getPositionTableCell(String textValue, TextAlignment textAlignment, Boolean isBold) {
        Cell myCell = new Cell().add(new Paragraph(textValue).setFontSize(10))
                .setTextAlignment(textAlignment)
                .setBorder(Border.NO_BORDER)
                .setPaddingBottom(6);
        return isBold ? myCell.setBold() : myCell;
    }

    static Table getDivider(float[] width, float thickness) {
        return getDividerTable(width).setBorder(new SolidBorder(ColorConstants.GRAY,
                thickness));
    }
}
