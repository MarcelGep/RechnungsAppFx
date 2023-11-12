package com.gepraegs.rechnungsAppFx.model;

import java.net.MalformedURLException;

import com.gepraegs.rechnungsAppFx.utils.ConstantUtil;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.renderer.DocumentRenderer;
import com.itextpdf.layout.renderer.TableRenderer;

public class HeaderEventHandler implements IEventHandler {
    float fullwidth[] = { 500f };
    private Document doc;
    private String logo;
    private Table table;
    private Image image;
    private float tableHeight;
    private boolean firstPage = true;
    private boolean init = false;

    TableRenderer renderer;
    LayoutResult result;
    ProductTableHeader productTableHeader;
    float sevenColumnWidth[] = { 50, 100, 350, 80, 100, 150, 150 };

    public HeaderEventHandler(Document doc, String logo, ProductTableHeader productTableHeader) {
        this.doc = doc;
        this.logo = logo;
        this.productTableHeader = productTableHeader;

        initImage();
        initTable();

        calculateSpace(table);
    }

    @Override
    public void handleEvent(Event event) {
        System.out.println(event.getType());

        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfDocument pdfDoc = docEvent.getDocument();
        PdfPage page = docEvent.getPage();
        PdfCanvas canvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);
        PageSize pageSize = pdfDoc.getDefaultPageSize();
        float coordX = pageSize.getX() + doc.getLeftMargin();
        float coordY = pageSize.getTop() - doc.getTopMargin();
        float width = pageSize.getWidth() - doc.getRightMargin() - doc.getLeftMargin();
        float height = getTableHeight();

        if (firstPage) {
            new Canvas(canvas, new Rectangle(coordX, coordY, width, height + 10))
                    .add(table)
                    .close();
            firstPage = false;
        } else {
            if (!init) {
                // add informations after second page
                table.addCell(new Cell().add(new Paragraph("Rechnung")
                        .setBold())
                        .setBorder(Border.NO_BORDER)
                        .setPaddingTop(10)
                        .setTextAlignment((TextAlignment.LEFT)));

                table.addCell(new Cell().add(new Paragraph(ConstantUtil.INVOICE_NO_TEXT + "123"))
                        .setBorder(Border.NO_BORDER)
                        .setPaddingTop(10)
                        .setTextAlignment((TextAlignment.RIGHT)));

                calculateSpace(table);
                doc.setTopMargin(100 + getTableHeight());
                init = true;
            }

            height = getTableHeight() + 20;
            coordY = pageSize.getTop() - doc.getTopMargin();

            new Canvas(canvas, new Rectangle(coordX, coordY + 50, width, height))
                    .add(table)
                    .close();

            // TABLE HEADER
            Table tableSevenColumns = new Table(sevenColumnWidth);
            addProductTableHeader(tableSevenColumns);
            new Canvas(canvas, new Rectangle(coordX, coordY - 90, width, height))
                    .add(tableSevenColumns)
                    .close();
        }
    }

    private void initImage() {
        try {
            image = new Image(ImageDataFactory.create(logo));
            float scaleFactor = (float) 0.48;
            image.scale(scaleFactor + 0.03f, scaleFactor + 0.01f);
            image.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void addProductTableHeader(Table table) {
        if (productTableHeader != null) {
            table.addCell(
                    getPositionTableHeaderCell(productTableHeader.getPos(), TextAlignment.LEFT, true)
                            .setBorderBottom(new SolidBorder(0.5f)));
            table
                    .addCell(getPositionTableHeaderCell(productTableHeader.getArtNr(), TextAlignment.LEFT, true));
            table
                    .addCell(getPositionTableHeaderCell(productTableHeader.getDescription(), TextAlignment.LEFT, true));
            table
                    .addCell(getPositionTableHeaderCell(productTableHeader.getQuantity(), TextAlignment.LEFT, true));
            table
                    .addCell(getPositionTableHeaderCell(productTableHeader.getUnit(), TextAlignment.LEFT, true));
            table.addCell(
                    getPositionTableHeaderCell(productTableHeader.getSinglePrice(), TextAlignment.RIGHT, true));
            table
                    .addCell(getPositionTableHeaderCell(productTableHeader.getTotalPrice(), TextAlignment.RIGHT, true));
        }
    }

    private void initTable() {
        table = new Table(2).useAllAvailableWidth();
        table.addCell(new Cell().add(new Paragraph(ConstantUtil.EMPTY))
                .setBorder(Border.NO_BORDER)
                .setBorderBottom(new SolidBorder(0.5f)));
        table.addCell(new Cell().add(image.setAutoScale(false))
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.RIGHT)
                .setPaddingBottom(10)
                .setBorderBottom(new SolidBorder(0.5f)));
    }

    public float getTableHeight() {
        return tableHeight;
    }

    public void createEmptyRow(Table table) {
        for (int i = 0; i < 7; i++) {
            table.addCell(
                    getPositionTableCell(ConstantUtil.EMPTY, TextAlignment.LEFT, false));
        }
    }

    private void calculateSpace(Table table) {
        renderer = (TableRenderer) table.createRendererSubTree();
        renderer.setParent(new DocumentRenderer(doc));

        // Simulate the positioning of the renderer to find out how much space the
        // header table will occupy.
        result = renderer.layout(new LayoutContext(new LayoutArea(0, PageSize.A4)));
        tableHeight = result.getOccupiedArea().getBBox().getHeight();
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
                .setPaddingBottom(15);
        return isBold ? myCell.setBold() : myCell;
    }
}
