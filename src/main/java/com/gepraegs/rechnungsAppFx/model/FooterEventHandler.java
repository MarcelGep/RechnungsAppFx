package com.gepraegs.rechnungsAppFx.model;

import java.util.List;

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
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.renderer.DocumentRenderer;
import com.itextpdf.layout.renderer.TableRenderer;

public class FooterEventHandler implements IEventHandler {
    float fullwidth[] = { 500f };
    private Document doc;
    private List<String> data;
    private Table table;
    private float tableHeight;

    public FooterEventHandler(Document doc, List<String> data) {
        this.doc = doc;
        this.data = data;

        initTable();

        TableRenderer renderer = (TableRenderer) table.createRendererSubTree();
        renderer.setParent(new DocumentRenderer(doc));

        // Simulate the positioning of the renderer to find out how much space the
        // header table will occupy.
        LayoutResult result = renderer.layout(new LayoutContext(new LayoutArea(0, PageSize.A4)));
        tableHeight = result.getOccupiedArea().getBBox().getHeight();
    }

    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfDocument pdfDoc = docEvent.getDocument();
        PdfPage page = docEvent.getPage();
        PdfCanvas canvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);
        new Canvas(canvas, new Rectangle(50, 00, page.getPageSize().getWidth() - 100, 50))
                .add(table).close();
        ;
    }

    private void initTable() {
        table = new Table(1).useAllAvailableWidth();
        boolean firstLine = true;
        table.addCell(new Cell().add(new Paragraph(""))
                .setBorder(Border.NO_BORDER));
        for (String d : data) {
            table.addCell(new Cell()
                    .add(new Paragraph(d)
                            .setFontSize(8)
                            .setTextAlignment(TextAlignment.CENTER))
                    .setBorder(Border.NO_BORDER)
                    .setPaddingTop(firstLine ? 5 : 0)
                    .setBorderTop(firstLine ? new SolidBorder(0.6f) : Border.NO_BORDER));
            firstLine = false;
        }
    }

    public float getTableHeight() {
        return tableHeight;
    }
}