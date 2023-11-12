// package com.gepraegs.rechnungsAppFx;

// import com.spire.doc.FileFormat;

// public class GenerateInvoiceTemplate {

// public static void main(String[] args) {

// // create a document instance
// Document doc = new Document();

// // load the template file
// doc.loadFromFile("Invoice-Template.docx");

// // replace text in the document
// doc.replace("#InvoiceNo", "1234", true, true);
// doc.replace("#InvoiceDate", "31.10.2023", true, true);
// doc.replace("#DeliveryDate", "31.10.2023", true, true);
// doc.replace("#CustomerNo", "9999", true, true);

// doc.replace("#CustomerName", "Schalltec GmbH", true, true);
// doc.replace("#CustomerStreet", "Steißlinger Str. 4", true, true);
// doc.replace("#CustomerZipCity", "89604 Weilersteußlingen", true, true);
// doc.replace("#CustomerContactPerson", "Hans Schaude", true, true);

// doc.replace("#CompanyName", "TPT Schaude", true, true);
// doc.replace("#CompanyStreet", "Ennostr. 10", true, true);
// doc.replace("#CompanyZipCity", "89604 Ennahofen", true, true);
// doc.replace("#CompanyContact", "Annina Geprägs", true, true);
// doc.replace("#CompanyPhone", "07384/294550", true, true);
// doc.replace("#CompanyEmail", "info@tpt-schaude.de", true, true);

// // define purchase data
// String[][] purchaseData = {
// new String[] { "1", "1001", "Artikel 1", "2", "Stück", "120,00" },
// new String[] { "2", "1002", "Artikel 2", "2", "Stück", "500,00" },
// // new String[] { "Product A", "5", "22.8" },
// // new String[] { "Product B", "4", "35.3" },
// };

// // write the purchase data to the document
// writeDataToDocument(doc, purchaseData);

// // update fields
// doc.isUpdateFields(true);

// // save file in pdf format
// doc.saveToFile("Invoice.pdf", FileFormat.PDF);
// }

// private static void writeDataToDocument(Document doc, String[][]
// purchaseData) {
// // get the second table
// Table table = doc.getSections().get(0).getTables().get(2);

// // determine if it needs to add rows
// if (purchaseData.length > 1) {
// // add rows
// addRows(table, purchaseData.length - 1);
// }

// //
// table.getRows().get(4).getCells().get(6).getParagraphs().get(0).setText("1.240.000.000");

// // fill the table cells with value
// fillTableWithData(table, purchaseData);
// }

// private static void fillTableWithData(Table table, String[][] data) {
// for (int r = 0; r < data.length; r++) {
// for (int c = 0; c < data[r].length; c++) {
// // fill data in cells
// table.getRows().get(r +
// 2).getCells().get(c).getParagraphs().get(0).setText(data[r][c]);
// }
// }
// }

// private static void addRows(Table table, int rowNum) {
// for (int i = 0; i < rowNum; i++) {
// // insert specific number of rows by cloning the second row
// table.getRows().insert(3 + i, table.getRows().get(2).deepClone());
// // update formulas for Total
// for (Object object : table.getRows().get(3 +
// i).getCells().get(6).getParagraphs().get(0)
// .getChildObjects()) {
// if (object instanceof Field) {
// Field field = (Field) object;
// field.setCode(String.format("=(D%d*F%d)/1000000\\# \"0,00\"", 4 + i, 4 + i));
// }
// break;
// }
// }
// // update formula for total MWST
// for (Object object : table.getRows().get(5 +
// rowNum).getCells().get(6).getParagraphs().get(0)
// .getChildObjects()) {
// if (object instanceof Field) {
// Field field = (Field) object;
// field.setCode(String.format("=G%d*0.19\\# \"0,00\"", 4 + rowNum));
// }
// break;
// }
// // update formula for total price
// for (Object object : table.getRows().get(6 +
// rowNum).getCells().get(6).getParagraphs().get(0)
// .getChildObjects()) {
// if (object instanceof Field) {
// Field field = (Field) object;
// field.setCode(String.format("=G%d+G%d\\# \"#.##0,00 €\"", 5 + rowNum, 7 +
// rowNum));
// }
// break;
// }
// }
// }
