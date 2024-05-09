package src;

import src.txtparsing.TXTParsing;
import src.txtparsing.MyDoc;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        String filename = "docs/documents.txt"; // File containing the documents
        List<MyDoc> parsedDocs = null;

        try {
            parsedDocs = TXTParsing.parse(filename); // Parsing the documents
        } catch (Exception e) {
            System.err.println("Error parsing documents: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // Printing the parsed documents
        if (parsedDocs != null) {
            for (MyDoc doc : parsedDocs) {
                System.out.println("Document ID: " + doc.getID());
                System.out.println("Text: " + doc.getText());
                System.out.println("---------------------------------------");
            }

        } else {
            System.out.println("No documents parsed.");
        }
    }
}
