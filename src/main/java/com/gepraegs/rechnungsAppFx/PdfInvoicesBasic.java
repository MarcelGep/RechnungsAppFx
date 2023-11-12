// package com.gepraegs.rechnungsAppFx;

// import java.io.FileOutputStream;
// import java.io.IOException;
// import java.sql.SQLException;
// import java.text.ParseException;
// import java.text.SimpleDateFormat;
// import java.util.ArrayList;
// import java.util.Date;
// import java.util.List;

// import javax.xml.parsers.ParserConfigurationException;
// import javax.xml.transform.TransformerException;

// import org.xml.sax.SAXException;

// import com.itextpdf.text.Chunk;
// import com.itextpdf.text.Document;
// import com.itextpdf.text.DocumentException;
// import com.itextpdf.text.Element;
// import com.itextpdf.text.Font;
// import com.itextpdf.text.Paragraph;
// import com.itextpdf.text.pdf.BaseFont;
// import com.itextpdf.text.pdf.PdfArray;
// import com.itextpdf.text.pdf.PdfDate;
// import com.itextpdf.text.pdf.PdfDictionary;
// import com.itextpdf.text.pdf.PdfName;
// import com.itextpdf.text.pdf.PdfPCell;
// import com.itextpdf.text.pdf.PdfPTable;
// import com.itextpdf.text.pdf.PdfWriter;
// import com.itextpdf.xmp.XMPException;

// /**
// * Reads invoice data from a test database and creates ZUGFeRD invoices
// * (Basic profile).
// *
// * @author Bruno Lowagie
// */
// public class PdfInvoicesBasic {
// public static final String DEST = "neuertest.pdf";

// protected Font font10;
// protected Font font10b;
// protected Font font12;
// protected Font font12b;
// protected Font font14;

// public static void main(String[] args)
// throws IOException, DocumentException, ParserConfigurationException,
// SQLException, SAXException,
// TransformerException, XMPException, ParseException {
// PdfInvoicesBasic app = new PdfInvoicesBasic();
// Invoice invoice = new Invoice();
// invoice.setCustomer(new Customer("123", "Schalltec", "Hans", "Schaude",
// "Steißlinger Str. 14", DEST, DEST, DEST,
// DEST, DEST, DEST, DEST, DEST, 0));
// invoice.setReNr("12345");
// invoice.setDeliveryDate("31.10.2023");
// invoice.setDueDate("31.10.2023");
// app.createPdf(new Invoice());
// }

// public PdfInvoicesBasic() throws DocumentException, IOException {
// BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI,
// BaseFont.EMBEDDED);
// BaseFont bfb = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI,
// BaseFont.EMBEDDED);
// font10 = new Font(bf, 10);
// font10b = new Font(bfb, 10);
// font12 = new Font(bf, 12);
// font12b = new Font(bfb, 12);
// font14 = new Font(bf, 14);
// }

// public void createPdf(Invoice invoice) throws DocumentException, IOException
// {
// String dest = String.format(DEST, invoice.getReNr());

// // step 1
// Document document = new Document();
// // step 2
// PdfWriter writer = PdfWriter.getInstance(document, new
// FileOutputStream(dest));
// writer.setPdfVersion(PdfWriter.VERSION_1_7);
// writer.createXmpMetadata();
// // step 3
// document.open();

// // header
// Paragraph p;
// p = new Paragraph(invoice.getCustomer().getName1() + " " + invoice.getReNr(),
// font14);
// p.setAlignment(Element.ALIGN_RIGHT);
// document.add(p);
// p = new Paragraph(invoice.getDeliveryDate(), font12);
// p.setAlignment(Element.ALIGN_RIGHT);
// document.add(p);

// // Address seller / buyer
// PdfPTable table = new PdfPTable(2);
// table.setWidthPercentage(100);
// PdfPCell seller = getPartyAddress("From:",
// "basic.getSellerName()",
// " basic.getSellerLineOne()",
// "basic.getSellerLineTwo()",
// "basic.getSellerCountryID()",
// "basic.getSellerPostcode()",
// "basic.getSellerCityName()");
// table.addCell(seller);
// PdfPCell buyer = getPartyAddress("To:",
// "basic.getBuyerName()",
// "basic.getBuyerLineOne()",
// "basic.getBuyerLineTwo()",
// "basic.getBuyerCountryID()",
// "basic.getBuyerPostcode()",
// "basic.getBuyerCityName()");
// table.addCell(buyer);
// // seller = getPartyTax("basic.getSellerTaxRegistrationID()",
// // " basic.getSellerTaxRegistrationSchemeID()");
// table.addCell(seller);
// // buyer = getPartyTax(basic.getBuyerTaxRegistrationID(),
// // basic.getBuyerTaxRegistrationSchemeID());
// table.addCell(buyer);
// document.add(table);

// // line items
// table = new PdfPTable(7);
// table.setWidthPercentage(100);
// table.setSpacingBefore(10);
// table.setSpacingAfter(10);
// table.setWidths(new int[] { 7, 2, 1, 2, 2, 2, 2 });
// table.addCell(getCell("Pos:", Element.ALIGN_LEFT, font12b));
// table.addCell(getCell("ArtNr.:", Element.ALIGN_LEFT, font12b));
// table.addCell(getCell("Description:", Element.ALIGN_LEFT, font12b));
// table.addCell(getCell("Quantity:", Element.ALIGN_LEFT, font12b));
// table.addCell(getCell("Unit:", Element.ALIGN_LEFT, font12b));
// table.addCell(getCell("E-Preis:", Element.ALIGN_RIGHT, font12b));
// table.addCell(getCell("Total:", Element.ALIGN_RIGHT, font12b));
// document.add(table);

// List<Position> positionList = new ArrayList<>();
// positionList.add(new Position("1001", "1000", "Apfel", "Stück", "", 2, 100,
// 119, 19));
// positionList.add(new Position("1002", "1000", "Mango", "Stück", "", 2, 200,
// 219, 19));

// table = new PdfPTable(7);

// for (Position position : positionList) {
// table.addCell(getCell(String.valueOf(positionList.indexOf(position) + 1),
// Element.ALIGN_LEFT, font12));
// table.addCell(getCell(String.valueOf(position.getArtNr()),
// Element.ALIGN_LEFT, font12));
// table.addCell(getCell(String.valueOf(position.getDescription()),
// Element.ALIGN_LEFT, font12));
// table.addCell(getCell(String.valueOf(position.getQuantity()),
// Element.ALIGN_CENTER, font12));
// table.addCell(getCell(String.valueOf(position.getUnit()), Element.ALIGN_LEFT,
// font12));
// table.addCell(getCell(String.valueOf(position.getPriceExcl()),
// Element.ALIGN_RIGHT, font12));
// double total = position.getQuantity() * position.getPriceIncl();
// table.addCell(getCell(String.valueOf(String.valueOf(total)),
// Element.ALIGN_RIGHT, font12));
// }
// document.add(table);

// // grand totals
// // document.add(getTotalsTable(
// // basic.getTaxBasisTotalAmount(), basic.getTaxTotalAmount(),
// // basic.getGrandTotalAmount(),
// // basic.getGrandTotalAmountCurrencyID(),
// // basic.getTaxTypeCode(), basic.getTaxApplicablePercent(),
// // basic.getTaxBasisAmount(), basic.getTaxCalculatedAmount(),
// // basic.getTaxCalculatedAmountCurrencyID()));

// // // payment info
// // document.add(getPaymentInfo(basic.getPaymentReference(),
// // basic.getPaymentMeansPayeeFinancialInstitutionBIC(),
// // basic.getPaymentMeansPayeeAccountIBAN()));

// // XML version
// PdfDictionary parameters = new PdfDictionary();
// parameters.put(PdfName.MODDATE, new PdfDate());
// PdfArray array = new PdfArray();
// writer.getExtraCatalog().put(PdfName.AF, array);

// // step 5
// document.close();
// }

// public PdfPCell getPartyAddress(String who, String name, String line1, String
// line2, String countryID,
// String postcode, String city) {
// PdfPCell cell = new PdfPCell();
// cell.setBorder(PdfPCell.NO_BORDER);
// cell.addElement(new Paragraph(who, font12b));
// cell.addElement(new Paragraph(name, font12));
// cell.addElement(new Paragraph(line1, font12));
// cell.addElement(new Paragraph(line2, font12));
// cell.addElement(new Paragraph(String.format("%s-%s %s", countryID, postcode,
// city), font12));
// return cell;
// }

// public PdfPCell getPartyTax(String[] taxId, String[] taxSchema) {
// PdfPCell cell = new PdfPCell();
// cell.setBorder(PdfPCell.NO_BORDER);
// cell.addElement(new Paragraph("Tax ID(s):", font10b));
// if (taxId.length == 0) {
// cell.addElement(new Paragraph("Not applicable", font10));
// } else {
// int n = taxId.length;
// for (int i = 0; i < n; i++) {
// cell.addElement(new Paragraph(String.format("%s: %s", taxSchema[i],
// taxId[i]), font10));
// }
// }
// return cell;
// }

// public PdfPTable getTotalsTable(String tBase, String tTax, String tTotal,
// String tCurrency,
// String[] type, String[] percentage, String base[], String tax[], String
// currency[])
// throws DocumentException {
// PdfPTable table = new PdfPTable(6);
// table.setWidthPercentage(100);
// table.setWidths(new int[] { 1, 1, 3, 3, 3, 1 });
// table.addCell(getCell("TAX", Element.ALIGN_LEFT, font12b));
// table.addCell(getCell("%", Element.ALIGN_RIGHT, font12b));
// table.addCell(getCell("Base amount:", Element.ALIGN_LEFT, font12b));
// table.addCell(getCell("Tax amount:", Element.ALIGN_LEFT, font12b));
// table.addCell(getCell("Total:", Element.ALIGN_LEFT, font12b));
// table.addCell(getCell("", Element.ALIGN_LEFT, font12b));
// int n = type.length;
// for (int i = 0; i < n; i++) {
// table.addCell(getCell(type[i], Element.ALIGN_RIGHT, font12));
// table.addCell(getCell(percentage[i], Element.ALIGN_RIGHT, font12));
// table.addCell(getCell(base[i], Element.ALIGN_RIGHT, font12));
// table.addCell(getCell(tax[i], Element.ALIGN_RIGHT, font12));
// double total = Double.parseDouble(base[i]) + Double.parseDouble(tax[i]);
// table.addCell(getCell(currency[i], Element.ALIGN_LEFT, font12));
// }
// PdfPCell cell = getCell("", Element.ALIGN_LEFT, font12b);
// cell.setColspan(2);
// cell.setBorder(PdfPCell.NO_BORDER);
// table.addCell(cell);
// table.addCell(getCell(tBase, Element.ALIGN_RIGHT, font12b));
// table.addCell(getCell(tTax, Element.ALIGN_RIGHT, font12b));
// table.addCell(getCell(tTotal, Element.ALIGN_RIGHT, font12b));
// table.addCell(getCell(tCurrency, Element.ALIGN_LEFT, font12b));
// return table;
// }

// public PdfPCell getCell(String value, int alignment, Font font) {
// PdfPCell cell = new PdfPCell();
// cell.setUseAscender(true);
// cell.setUseDescender(true);
// Paragraph p = new Paragraph(value, font);
// p.setAlignment(alignment);
// cell.addElement(p);
// return cell;
// }

// public Paragraph getPaymentInfo(String ref, String[] bic, String[] iban) {
// Paragraph p = new Paragraph(String.format(
// "Please wire the amount due to our bank account using the following
// reference: %s",
// ref), font12);
// int n = bic.length;
// for (int i = 0; i < n; i++) {
// p.add(Chunk.NEWLINE);
// p.add(String.format("BIC: %s - IBAN: %s", bic[i], iban[i]));
// }
// return p;
// }

// public String convertDate(Date d, String newFormat) throws ParseException {
// SimpleDateFormat sdf = new SimpleDateFormat(newFormat);
// return sdf.format(d);
// }
// }
