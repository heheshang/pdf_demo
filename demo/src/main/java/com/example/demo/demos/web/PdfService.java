package com.example.demo.demos.web;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Service
public class PdfService {
    @Autowired
    private TemplateEngine templateEngine;

    public byte[] generatePdfWithWatermark(Map<String, Object> data) throws IOException, DocumentException {
        // 生成基础 PDF
        ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();
        generateBasePdf(data, pdfStream);

        // 添加水印
        return addWatermark(pdfStream.toByteArray());
    }

    private void generateBasePdf(Map<String, Object> data, OutputStream outputStream) {
        Context context = new Context();
        context.setVariables(data);

        try {
            String html = templateEngine.process("template", context);
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, null);
            builder.toStream(outputStream);
            builder.run();
        } catch (Exception e) {
            throw new RuntimeException("PDF生成失败", e);
        }
    }

    private byte[] addWatermark(byte[] pdfBytes) throws IOException, DocumentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfReader reader = new PdfReader(pdfBytes);
        PdfStamper stamper = new PdfStamper(reader, outputStream);

        // 水印配置
//        BaseFont baseFont = BaseFont.createFont("simsun", "utf-8", false);
        BaseFont baseFont = BaseFont.createFont();
        PdfGState gs = new PdfGState();
        gs.setFillOpacity(0.3f);

        // 逐页添加水印
        for(int i=1; i<=reader.getNumberOfPages(); i++) {
            PdfContentByte content = stamper.getOverContent(i);
            content.beginText();
            content.setGState(gs);
            content.setFontAndSize(baseFont, 48);
            content.setColorFill(BaseColor.LIGHT_GRAY);

            // 水印位置计算
            Rectangle pageSize = reader.getPageSize(i);
            float x = pageSize.getWidth()/2;
            float y = pageSize.getHeight()/2;

            content.showTextAligned(Element.ALIGN_CENTER, "CONFIDENTIAL", x, y, 45);
            content.endText();
        }

        stamper.close();
        reader.close();
        return outputStream.toByteArray();
    }
}
