package com.gepraegs.rechnungsAppFx;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;

import com.gepraegs.rechnungsAppFx.model.AddressDetails;
import com.gepraegs.rechnungsAppFx.model.InvoiceDetails;
import com.gepraegs.rechnungsAppFx.model.ProductTableHeader;
import com.gepraegs.rechnungsAppFx.services.PdfInvoiceCreator;

public class GeneratePDFInvoice {
        private static final String LOGO = "src/main/resources/images/logo.png";

        public static void main(String[] args) throws FileNotFoundException {
                String invoiceNo = "1054";
                String pdfName = "Rechnung_" + invoiceNo + ".pdf";

                PdfInvoiceCreator invoiceCreator = new PdfInvoiceCreator(pdfName);

                List<String> footerData = new LinkedList<>();

                StringJoiner joiner = new StringJoiner(PdfInvoiceCreator.BULLET_POINT);
                joiner.add("TPT-Schaude").add("Steuernummer: 58405/02231");
                footerData.add(joiner.toString());
                joiner = new StringJoiner(PdfInvoiceCreator.BULLET_POINT);
                joiner.add("Postbank Ehingen")
                                .add("BLZ: 10010010 ")
                                .add("IBAN: DE12 1001 0010 0672 1591 20")
                                .add("BIC: PBNKDEFF");
                footerData.add(joiner.toString());

                invoiceCreator.createDocument(LOGO, footerData, new ProductTableHeader());

                // Create Address start
                AddressDetails addressDetails = new AddressDetails();
                addressDetails
                                .setBillingCompany("Schalltec GmbH & Co. KG")
                                .setBillingContactPerson("Hans Schaude")
                                .setBillingAddress("Steißlinger Str. 14")
                                .setBillingZipCity("89604 Weilersteußlingen")
                                .setShippingCompany("TPT Schaude")
                                .setShippingContactPerson("Annina Geprägs")
                                .setShippingAddress("Ennostr. 10")
                                .setShippingZipCity("89604 Ennahofen")
                                .setShippingPhone("0176/123456789")
                                .setShippingEmail("info@tpt-schaude.de")
                                .build();

                invoiceCreator.addAddress(addressDetails);
                // Address end

                invoiceCreator.addVSpace(50);

                // Invoice Informations
                InvoiceDetails invoiceDetails = new InvoiceDetails();
                invoiceDetails.setInvoiceNo(invoiceNo)
                                .setInvoiceDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                                .setShippingDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                                .setCustomerNo("123456")
                                .build();
                invoiceCreator.addInvoiceDetails(invoiceDetails);
                // Invoice Informations end

                invoiceCreator.addVSpace(8);

                invoiceCreator.addTextLine(
                                "Wir bedanken uns für die gute Zusammenarbeit und stellen Ihnen wie vereinbart folgende Leistung in Rechnung.");

                invoiceCreator.addVSpace(15);

                // Positions Start
                List<Position> positionList = invoiceCreator.getDummyPositionList(50);
                invoiceCreator.addPositionList(new ProductTableHeader(), positionList);
                // Positions End

                invoiceCreator.addVSpace(40);
                invoiceCreator.addTextLine("Vielen Dank für Ihre Bestellung!");

                // Close document
                invoiceCreator.closeDocument();

                System.out.println("PDF genrated!");
        }
}