package txtparsing;

import utils.IO;
import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.List;

public class TXTParsing {

    public static List<txtparsing.MyDoc> parse(String file) throws Exception {
        try {
            // Parse txt file
            String txt_file = IO.ReadEntireFileIntoAString(file);
            String[] docs = txt_file.split("\\s*///\\s*");

            // Parse each document from the txt file
            List<MyDoc> parsed_docs = new ArrayList<>();
            for (String doc : docs) {
                String[] parts = doc.split("\\s+", 2); // Splitting by whitespace, maximum of 2 parts
                if (parts.length == 2) {
                    String docID = parts[0].trim();
                    String text = parts[1].trim();
                    parsed_docs.add(new MyDoc(Integer.parseInt(docID), text));
                }
            }

            return parsed_docs;
        } catch (Throwable err) {
            err.printStackTrace();
            return null;
        }
    }
}
