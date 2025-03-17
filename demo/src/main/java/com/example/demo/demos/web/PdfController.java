package com.example.demo.demos.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

// 3. 控制器
@RestController
public class PdfController {

    @Autowired
    private PdfService pdfService;

    @GetMapping("/generate-pdf")
    public ResponseEntity<byte[]> generatePdf() {
        Map<String, Object> data = new HashMap<>();
        data.put("title", "示例文档");
        data.put("content", "这是通过模板生成的PDF内容");

        try {
            byte[] pdfBytes = pdfService.generatePdfWithWatermark(data);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(
                    ContentDisposition.builder("attachment").filename("document.pdf").build());
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
