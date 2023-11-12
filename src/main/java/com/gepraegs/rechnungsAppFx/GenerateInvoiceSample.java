// package com.gepraegs.rechnungsAppFx;

// import java.io.FileOutputStream;
// import java.io.IOException;
// import java.text.DecimalFormat;
// import java.text.DecimalFormatSymbols;
// import java.util.Locale;

// import com.itextpdf.text.Document;
// import com.itextpdf.text.DocumentException;
// import com.itextpdf.text.Image;
// import com.itextpdf.text.PageSize;
// import com.itextpdf.text.pdf.BaseFont;
// import com.itextpdf.text.pdf.PdfContentByte;
// import com.itextpdf.text.pdf.PdfWriter;

// public class GenerateInvoiceSample {

// private BaseFont bfBold;
// private BaseFont bf;

// private int pageNumber = 0;

// public static void main(String[] args) {

// String pdfFilename = "";
// GenerateInvoiceSample generateInvoice = new GenerateInvoiceSample();
// if (args.length < 1) {
// System.err.println("Usage: java " + generateInvoice.getClass().getName() +
// " PDF_Filename");
// System.exit(1);
// }

// pdfFilename = args[0].trim();
// generateInvoice.createPDF(pdfFilename);

// }

// private void createPDF(String pdfFilename) {

// Document doc = new Document(PageSize.A4);
// PdfWriter docWriter = null;
// initializeFonts();

// try {
// String path = pdfFilename;
// docWriter = PdfWriter.getInstance(doc, new FileOutputStream(path));
// doc.addAuthor("betterThanZero");
// doc.addCreationDate();
// doc.addProducer();
// doc.addCreator("MySampleCode.com");
// doc.addTitle("Invoice");
// doc.setPageSize(PageSize.LETTER);

// doc.open();
// PdfContentByte cb = docWriter.getDirectContent();

// boolean beginPage = true;
// int y = 0;

// for (int i = 0; i < 5; i++) {
// if (beginPage) {
// beginPage = false;
// generateLayout(doc, cb);
// createAddressHeader(cb, 39, 665);
// y = 396;
// }
// generateDetail(doc, cb, i, y);
// y = y - 20;
// if (y < 50) {
// printPageNumber(cb);
// doc.newPage();
// beginPage = true;
// }
// }
// printPageNumber(cb);

// } catch (DocumentException dex) {
// dex.printStackTrace();
// } catch (Exception ex) {
// ex.printStackTrace();
// } finally {
// if (doc != null) {
// doc.close();
// }
// if (docWriter != null) {
// docWriter.close();
// }
// }
// }

// private void createInvoiceDetails(PdfContentByte cb, float x, float y) {
// try {
// createHeadings(cb, x - 391, y + 20, "Rechnung", 12);

// createText(cb, x - 390, y, "Rechnungs-Nr.:");
// createText(cb, x - 325, y, "1054");

// createText(cb, x, y, "Rechnungsdatum:");
// createText(cb, x + 80, y, "31.10.2023");
// createText(cb, x, y - 10, "Lieferdatum:");
// createText(cb, x + 80, y - 10, "31.10.2023");
// createText(cb, x, y - 20, "Kunden-Nr.:");
// createText(cb, x + 80, y - 20, "123456");
// } catch (Exception ex) {
// ex.printStackTrace();
// }
// }

// private void generateLayout(Document doc, PdfContentByte cb) {
// try {
// cb.setLineWidth(1f);

// // add the images
// Image companyLogo = Image.getInstance("src/main/resources/images/logo.png");
// companyLogo.setAbsolutePosition(400, 700);
// companyLogo.scalePercent(45);
// doc.add(companyLogo);

// createInvoiceDetails(cb, 430, 510);
// createText(cb, 40, 450,
// "Wir bedanken uns für die gute Zusammenarbeit und stellen Ihnen wie
// vereinbart folgende Leistung in Rechnung.");
// createInvoiceListHeader(cb, 42, 420);

// } catch (DocumentException dex) {
// dex.printStackTrace();
// } catch (Exception ex) {
// ex.printStackTrace();
// }

// }

// private void createInvoiceListHeader(PdfContentByte cb, float x, float y) {
// try {
// // horizontal line
// cb.moveTo(38, 690);
// cb.lineTo(562, 690);

// // Invoice Detail box layout
// cb.moveTo(x - 2, y + 12);
// cb.lineTo(x + 520, y + 12);
// cb.moveTo(x - 2, y - 8);
// cb.lineTo(x + 520, y - 8);
// cb.stroke();

// createHeadings(cb, x, y, "Pos", 8);
// createHeadings(cb, x + 30, y, "Art-Nr.", 8);
// createHeadings(cb, x + 86, y, "Beschreibung", 8);
// createHeadings(cb, x + 280, y, "Menge", 8);
// createHeadings(cb, x + 332, y, "Einheit", 8);
// createHeadings(cb, x + 400, y, "E-Preis", 8);
// createHeadings(cb, x + 473, y, "Gesamt", 8);
// } catch (Exception ex) {
// ex.printStackTrace();
// }
// }

// private void createAddressHeader(PdfContentByte cb, float x, float y) {
// try {
// createText(cb, x, y, "TPT Schaude - Ennostr. 10 - 89604 Ennahofen", 6);

// createText(cb, x, y - 15, "Schalltec GmbH & Co. KG");
// createText(cb, x, y - 25, "Hans Schaude");
// createText(cb, x, y - 35, "Steißlinger Str. 14");
// createText(cb, x, y - 45, "89604 Weilersteußlingen");
// createText(cb, x, y - 55, "Deutschland");

// createText(cb, x + 390, y - 5, "TPT Schaude");
// createText(cb, x + 390, y - 15, "Annina Geprägs");
// createText(cb, x + 390, y - 25, "Ennostr. 10");
// createText(cb, x + 390, y - 35, "89604 Ennahofen");

// createText(cb, x + 390, y - 55, "Tel.: 0176/123455");
// createText(cb, x + 390, y - 65, "E-Mail: info@tpt-schaude.de");
// } catch (Exception ex) {
// ex.printStackTrace();
// }
// }

// private void generateDetail(Document doc, PdfContentByte cb, int index, int
// y) {
// DecimalFormat df = new DecimalFormat("###,###.### €", new
// DecimalFormatSymbols(Locale.GERMAN));
// df.setMinimumFractionDigits(2);

// try {

// createContent(cb, 50, y, String.valueOf(index + 1),
// PdfContentByte.ALIGN_CENTER);
// createContent(cb, 73, y, "100" + String.valueOf(index + 1),
// PdfContentByte.ALIGN_LEFT);
// createContent(cb, 129, y, "Produktbeschreibung " + String.valueOf(index + 1),
// PdfContentByte.ALIGN_LEFT);
// createContent(cb, 346, y, "2", PdfContentByte.ALIGN_RIGHT);
// createContent(cb, 400, y, "Stück", PdfContentByte.ALIGN_RIGHT);

// double singlePrice = 500; // Double.valueOf(df.format(Math.random() * 1000));
// double totalPrice = singlePrice * 2;
// createContent(cb, 478, y, df.format(singlePrice),
// PdfContentByte.ALIGN_RIGHT);
// createContent(cb, 558, y, df.format(totalPrice), PdfContentByte.ALIGN_RIGHT);
// }

// catch (Exception ex) {
// ex.printStackTrace();
// }

// }

// private void createText(PdfContentByte cb, float x, float y, String text,
// float size) {
// cb.beginText();
// cb.setFontAndSize(bf, size);
// cb.setTextMatrix(x, y);
// cb.showText(text.trim());
// cb.endText();
// }

// private void createText(PdfContentByte cb, float x, float y, String text) {
// createText(cb, x, y, text, 9);
// }

// private void createHeadings(PdfContentByte cb, float x, float y, String text,
// float size) {
// cb.beginText();
// cb.setFontAndSize(bfBold, size);
// cb.setTextMatrix(x, y);
// cb.showText(text.trim());
// cb.endText();
// }

// private void printPageNumber(PdfContentByte cb) {
// cb.beginText();
// cb.setFontAndSize(bfBold, 8);
// cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Page No. " + (pageNumber +
// 1), 570, 25, 0);
// cb.endText();
// pageNumber++;
// }

// private void createContent(PdfContentByte cb, float x, float y, String text,
// int align) {
// cb.beginText();
// cb.setFontAndSize(bf, 9);
// cb.showTextAligned(align, text.trim(), x, y, 0);
// cb.endText();
// }

// private void initializeFonts() {
// try {
// bfBold = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252,
// BaseFont.NOT_EMBEDDED);
// bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252,
// BaseFont.NOT_EMBEDDED);

// } catch (DocumentException e) {
// e.printStackTrace();
// } catch (IOException e) {
// e.printStackTrace();
// }
// }
// }
