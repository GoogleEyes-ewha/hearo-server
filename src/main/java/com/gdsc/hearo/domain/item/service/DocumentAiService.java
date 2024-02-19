package com.gdsc.hearo.domain.item.service;


import com.google.cloud.documentai.v1.*;
import com.google.protobuf.ByteString;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
public class DocumentAiService {
    @Value("${document.project-id}")
    private String projectId;
    @Value("${document.location}")
    private String location;
    @Value("${document.parser}")
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
            List<Document.Page> pages = documentResponse.getPagesList();
            System.out.printf("There are %s page(s) in this document.\n", pages.size());

            for (Document.Page page : pages) {
                System.out.printf("\n\n**** Page %d ****\n", page.getPageNumber());

                List<Document.Page.Table> tables = page.getTablesList();
                System.out.printf("Found %d table(s):\n", tables.size());
                for (Document.Page.Table table : tables) {
                    nutritionText += extractTableContents(table, documentResponse.getText());
                }

                // ... (Other code for additional processing, if needed)
            }


        }
        return nutritionText;
    }

    private static String extractTableContents(Document.Page.Table table, String text) {

        String tableText="";
        Document.Page.Table.TableRow headerRow = table.getHeaderRows(0);
        // Extract and print header
        StringBuilder headerRowText = new StringBuilder();
        for (Document.Page.Table.TableCell cell : headerRow.getCellsList()) {
            String columnName = getLayoutText(cell.getLayout().getTextAnchor(), text);
            headerRowText.append(String.format("%s | ", removeNewlines(columnName)));
        }
        headerRowText.setLength(headerRowText.length() - 3);
        System.out.printf("Columns: %s\n", headerRowText.toString());
        tableText += headerRowText.toString();
        for (Document.Page.Table.TableRow bodyRow : table.getBodyRowsList()) {
            for (Document.Page.Table.TableCell cell : bodyRow.getCellsList()) {
                String cellText = getLayoutText(cell.getLayout().getTextAnchor(), text);
                System.out.printf("Table cell text: '%s'\n", removeNewlines(cellText));

                //tableText += String.format("  '%s' |", removeNewlines(cellText));
                tableText += (removeNewlines(cellText)+" | ");
            }
        }
        return tableText;
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
    // Extract shards from the text field
    private static String getLayoutText(Document.TextAnchor textAnchor, String text) {
        /*if (textAnchor.getTextSegmentsList().size() > 0) {
            int startIdx = (int) textAnchor.getTextSegments(0).getStartIndex();
            int endIdx = (int) textAnchor.getTextSegments(0).getEndIndex();
            return text.substring(startIdx, endIdx);
        }
        return "[NO TEXT]";*/

        StringBuilder result = new StringBuilder();

        for (Document.TextAnchor.TextSegment textSegment : textAnchor.getTextSegmentsList()) {
            int startIdx = (int) textSegment.getStartIndex();
            int endIdx = (int) textSegment.getEndIndex();
            String segmentText = text.substring(startIdx, endIdx);
            result.append(segmentText);
        }

        return result.toString();
    }

    private static String removeNewlines(String s) {
        return s.replace("\n", "").replace("\r", "");
    }

}
