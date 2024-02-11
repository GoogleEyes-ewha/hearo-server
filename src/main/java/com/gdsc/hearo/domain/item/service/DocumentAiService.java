package com.gdsc.hearo.domain.item.service;


import com.google.cloud.documentai.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.UUID;

@Service
public class DocumentAiService {
    @Value("${ai.project.id}")
    private String projectId;
    @Value("${ai.location}")
    private String location;
    @Value("${ai.parser}")
    private String parser;



    public Processor syncCreateProcessor() throws Exception {
        String uuid = UUID.randomUUID().toString();
        try (DocumentProcessorServiceClient documentProcessorServiceClient =
                     DocumentProcessorServiceClient.create()) {
            CreateProcessorRequest request =
                    CreateProcessorRequest.newBuilder()
                            .setParent(LocationName.of(projectId, location).toString())
                            .setProcessor(
                                    Processor.newBuilder()
                                            .setType(parser)
                                            .setDisplayName("lim-"+uuid)
                                            .build())
                            .build();

            //System.out.println("response" + documentProcessorServiceClient.createProcessor(request));
            return documentProcessorServiceClient.createProcessor(request);
        }
    }

    public String execute(String filePath)
            throws Exception, IOException, InterruptedException, ExecutionException, TimeoutException {

        Processor response = this.syncCreateProcessor();
        String name = response.getName();
        String nutritionText = "";
        String endpoint = String.format("%s-documentai.googleapis.com:443", location);
        DocumentProcessorServiceSettings settings =
                DocumentProcessorServiceSettings.newBuilder().setEndpoint(endpoint).build();
        try (DocumentProcessorServiceClient client = DocumentProcessorServiceClient.create(settings)) {
            // Read the file.
            byte[] imageFileData = readImageDataFromUrl(filePath);

            // Convert the image data to a Buffer and base64 encode it.
            ByteString content = ByteString.copyFrom(imageFileData);

            RawDocument document =
                    RawDocument.newBuilder().setContent(content).setMimeType("image/jpeg").build();

            // Configure the process request.
            ProcessRequest request =
                    ProcessRequest.newBuilder().setName(name).setRawDocument(document).build();

            // Recognizes text entities in the PDF document
            ProcessResponse result = client.processDocument(request);
            Document documentResponse = result.getDocument();

            //System.out.println("Document processing complete.");

            // Get all of the document text as one big string
            String text = documentResponse.getText();

            // Read the text recognition output from the processor
            //System.out.println("The document contains the following paragraphs:");
            Document.Page firstPage = documentResponse.getPages(0);
            List<Document.Page.Paragraph> paragraphs = firstPage.getParagraphsList();

            // Read the text recognition output from the processor
            List<Document.Page> pages = documentResponse.getPagesList();
            //System.out.printf("There are %s page(s) in this document.\n", pages.size());

            for (Document.Page page : pages) {

                List<Document.Page.Table> tables = page.getTablesList();

                //영양성분표 없는 상황 추가해야함

                for (Document.Page.Table table : tables) {
                    nutritionText += printTableInfo(table, text);
                }

                List<Document.Page.FormField> formFields = page.getFormFieldsList();
                for (Document.Page.FormField formField : formFields) {
                    String fieldName = getLayoutText(formField.getFieldName().getTextAnchor(), text);
                    String fieldValue = getLayoutText(formField.getFieldValue().getTextAnchor(), text);

                    System.out.printf(
                            "    '%s': '%s'\n", removeNewlines(fieldName), removeNewlines(fieldValue));
                    nutritionText += String.format("    '%s': '%s'", removeNewlines(fieldName), removeNewlines(fieldValue));
                }
            }
        }
        return nutritionText;
    }

    private static byte[] readImageDataFromUrl(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);

        try (InputStream inputStream = url.openStream()) {
            // Dynamically adjust the buffer size based on the available data
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] tempBuffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(tempBuffer)) != -1) {
                outputStream.write(tempBuffer, 0, bytesRead);
            }
            return outputStream.toByteArray();
        }
    }

    private static String printTableInfo(Document.Page.Table table, String text) {

        String tableText = "";
        Document.Page.Table.TableRow firstBodyRow = table.getBodyRows(0);
        int columnCount = firstBodyRow.getCellsCount();
        /*System.out.printf(
                "    Table with %d columns and %d rows:\n", columnCount, table.getBodyRowsCount());*/

        Document.Page.Table.TableRow headerRow = table.getHeaderRows(0);
        StringBuilder headerRowText = new StringBuilder();
        for (Document.Page.Table.TableCell cell : headerRow.getCellsList()) {
            String columnName = getLayoutText(cell.getLayout().getTextAnchor(), text);
            headerRowText.append(String.format("%s  ", removeNewlines(columnName)));
        }
        headerRowText.setLength(headerRowText.length() - 3);
        //System.out.printf("        Collumns: %s\n", headerRowText.toString());
        tableText += headerRowText.toString();

        StringBuilder firstRowText = new StringBuilder();
        for (Document.Page.Table.TableCell cell : firstBodyRow.getCellsList()) {
            String cellText = getLayoutText(cell.getLayout().getTextAnchor(), text);
            firstRowText.append(String.format("%s  ", removeNewlines(cellText)));
        }
        firstRowText.setLength(firstRowText.length() - 3);
        //System.out.printf("        First row data: %s\n", firstRowText.toString());
        tableText += firstRowText;

        return tableText;
    }

    // Extract shards from the text field
    private static String getLayoutText(Document.TextAnchor textAnchor, String text) {
        if (textAnchor.getTextSegmentsList().size() > 0) {
            int startIdx = (int) textAnchor.getTextSegments(0).getStartIndex();
            int endIdx = (int) textAnchor.getTextSegments(0).getEndIndex();
            return text.substring(startIdx, endIdx);
        }
        return "[NO TEXT]";
    }

    private static String removeNewlines(String s) {
        return s.replace("\n", "").replace("\r", "");
    }

}
