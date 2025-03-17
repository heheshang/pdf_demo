package com.example.demo;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileOutputStream;
import java.io.IOException;

@SpringBootTest
class DemoApplicationTests {

    @Test
    void contextLoads() throws IOException, DocumentException {
        // 假设这部分代码用于根据HTML模板生成PDF文件
        // ...
        // 读取生成的PDF文件
        PdfReader reader = new PdfReader("template-generated.pdf");
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream("output.pdf"));

        // 获取PDF中的页数
        int pageCount = reader.getNumberOfPages();

        // 添加水印
        for (int i = 1; i <= pageCount; i++) {
            PdfContentByte contentByte = stamper.getUnderContent(i); // 或者getOverContent()
            contentByte.beginText();
            contentByte.setFontAndSize(BaseFont.createFont(), 36f);
            contentByte.setColorFill(BaseColor.LIGHT_GRAY);
            contentByte.showTextAligned(Element.ALIGN_CENTER, "Watermark", 300, 400, 45);
            contentByte.endText();
        }

        // 保存修改后的PDF文件并关闭文件流
        stamper.close();
        reader.close();
    }

}
