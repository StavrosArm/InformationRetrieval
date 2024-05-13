

// tested for lucene 7.7.3 and jdk13
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.List;
import java.nio.file.Files;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.store.FSDirectory;

import txtparsing.MyDoc;
import txtparsing.TXTParsing;

/**
 *
 * @author Tonia Kyriakopoulou
 */
public class SearcherDemo {
    static String filepath = System.getProperty("user.dir");
    public SearcherDemo(){
        try{
            String indexLocation = ("index"); //define where the index is stored
            String field = "text"; //define which field will be searched            
            
            //Access the index using indexReaderFSDirectory.open(Paths.get(index))
            IndexReader indexReader = DirectoryReader.open(FSDirectory.open(Paths.get(indexLocation))); //IndexReader is an abstract class, providing an interface for accessing an index.
            IndexSearcher indexSearcher = new IndexSearcher(indexReader); //Creates a searcher searching the provided index, Implements search over a single IndexReader.
            indexSearcher.setSimilarity(new ClassicSimilarity());
            
            //Search the index using indexSearcher
            search(indexSearcher, field);
            
            //Close indexReader
            indexReader.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /**
     * Searches the index given a specific user query.
     */
    private void search(IndexSearcher indexSearcher, String field){
        try{
            // define which analyzer to use for the normalization of user's query
            Analyzer analyzer = new EnglishAnalyzer();
            
            // create a query parser on the field "contents"
            QueryParser parser = new QueryParser(field, analyzer);
            
            // read user's query from stdin
            /* 
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter query or 'q' to quit: ");
            System.out.print(">>");
            String line = br.readLine();
            */
            
            BufferedWriter writer = new BufferedWriter(new FileWriter("search_results.txt"));
            List<String> querry = TXTParsing.parseQuerry(filepath + "\\docs\\queries.txt"); //takes the querry.txt and creates a List containing only the querrys
            int question = 1;
            for (String q : querry){

                // parse the query according to QueryParser
                Query query = parser.parse(q);
                System.out.println("Searching for: " + query.toString(field));
                
                // search the index using the indexSearcher
                TopDocs results = indexSearcher.search(query, 50);
                ScoreDoc[] hits = results.scoreDocs;
                long numTotalHits = results.totalHits;
                System.out.println(numTotalHits + " total matching documents");

                //display results
                for(int i=0; i<hits.length; i++){
                    Document hitDoc = indexSearcher.doc(hits[i].doc);
                    System.out.println("\tScore "+hits[i].score +" Document ID="+hitDoc.get("Document ID"));
                    //String resultLine = String.format("%s Q0 %s %d %f", "Q"+question, hitDoc.get("Document ID"), i+1, hits[i].score);
                    String resultLine = String.format("%s %s %d %f", "Q"+question, hitDoc.get("Document ID"), i+1, hits[i].score);
                    writer.write(resultLine);
                    writer.newLine();
                }
                






                question++;
                //System.out.println(q);
            }
            writer.close();

            







            /* 
            BufferedWriter writer = new BufferedWriter(new FileWriter("search_results.txt"));

            int question=1;
            while(line!=null && !line.equals("") && !line.equalsIgnoreCase("q")){
                // parse the query according to QueryParser
                Query query = parser.parse(line);
                System.out.println("Searching for: " + query.toString(field));
                
                // search the index using the indexSearcher
                TopDocs results = indexSearcher.search(query, 50);
                ScoreDoc[] hits = results.scoreDocs;
                long numTotalHits = results.totalHits;
                System.out.println(numTotalHits + " total matching documents");

                //display results
                for(int i=0; i<hits.length; i++){
                    Document hitDoc = indexSearcher.doc(hits[i].doc);
                    System.out.println("\tScore "+hits[i].score +" Document ID="+hitDoc.get("Document ID"));
                    String resultLine = String.format("%s Q0 %s %d %f", "Q"+question, hitDoc.get("Document ID"), i+1, hits[i].score);
                    writer.write(resultLine);
                    writer.newLine();
                }
                writer.close();
                System.out.println("Enter query or 'q' to quit: ");
                System.out.print(">>");
                line = br.readLine();

                question++;
            }
            */
            
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Initialize a SearcherDemo
     */
    public static void main(String[] args){
        SearcherDemo searcherDemo = new SearcherDemo();
    }
}