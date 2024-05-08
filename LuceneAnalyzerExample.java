import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

import java.io.IOException;
import java.io.StringReader;

public class LuceneAnalyzerExample {
    public static void main(String[] args) {
        // Create a sample text to analyze
        String text = "Lucene is a full-featured text search engine library.";

        // Create an instance of StandardAnalyzer
        Analyzer analyzer = new StandardAnalyzer();

        try {
            // Tokenize the sample text using the StandardAnalyzer
            StringReader reader = new StringReader(text);
            org.apache.lucene.analysis.TokenStream stream = analyzer.tokenStream(null, reader);
            stream.reset(); // Necessary before calling incrementToken()

            // Print out the tokens generated by the analyzer
            while (stream.incrementToken()) {
                System.out.println(stream.getAttribute(org.apache.lucene.analysis.tokenattributes.CharTermAttribute.class));
            }

            // Close the TokenStream
            stream.end();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Close the analyzer when done
            analyzer.close();
        }
    }
}

