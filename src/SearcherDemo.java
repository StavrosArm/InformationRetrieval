

// tested for lucene 7.7.3 and jdk13
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.List;
import java.nio.file.Files;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.miscellaneous.CapitalizationFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
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
import org.apache.lucene.*;
import org.apache.lucene.analysis.custom.*;

import txtparsing.MyDoc;
import txtparsing.TXTParsing;


public class SearcherDemo {
    static String filepath = System.getProperty("user.dir");
    public SearcherDemo(){
        try{
            String indexLocation = ("index"); //define where the index is stored
            String field = "text"; //define which field will be searched            
            
            
            IndexReader indexReader = DirectoryReader.open(FSDirectory.open(Paths.get(indexLocation))); //provide the indexReader where to store 
            IndexSearcher indexSearcher = new IndexSearcher(indexReader); //create index searcher 
            indexSearcher.setSimilarity(new ClassicSimilarity());
            
            
            search(indexSearcher, field);
            
            indexReader.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    

    private void search(IndexSearcher indexSearcher, String field){
        try{
            
            
            Analyzer analyzer = new EnglishAnalyzer(); // define analyzer
            
            
            QueryParser parser = new QueryParser(field, analyzer); //create querry parser on field = "text" with english analyzer
            
            
            
            BufferedWriter writer = new BufferedWriter(new FileWriter("search_results.txt"));
            List<String> querry = TXTParsing.parseQuerry(filepath + "\\docs\\queries.txt"); //creates a list of the queries from qieries.txt
            int question = 1;
            for (String q : querry){ //loop for all the queries

                
                Query query = parser.parse(q);
                System.out.println("Searching for: " + query.toString(field));
                
                
                TopDocs results = indexSearcher.search(query, 50); //give the first 50 
                ScoreDoc[] hits = results.scoreDocs;
                long numTotalHits = results.totalHits;
                System.out.println(numTotalHits + " total matching documents");

               
                
                
                
                for(int i=0; i<hits.length; i++){
                    Document hitDoc = indexSearcher.doc(hits[i].doc);
                    System.out.println("\tScore "+hits[i].score +" Document ID="+hitDoc.get("Document ID"));
                    //String resultLine = String.format("%s Q0 %s %d %f", "Q"+question, hitDoc.get("Document ID"), i+1, hits[i].score);
                    if (question!=10){
                        String resultLine = String.format("%s %s %s %d %f %s", "Q0" + question, "Q0", hitDoc.get("Document ID"), i+1, hits[i].score, "run-1");
                        writer.write(resultLine);
                        writer.newLine();
                    }else{
                        String resultLine = String.format("%s %s %s %d %f %s", "Q" + question, "Q0", hitDoc.get("Document ID"), i+1, hits[i].score, "run-1");
                        writer.write(resultLine);
                        writer.newLine();
                    }
                    
                }
                

                question++;
               
            }
            writer.close();

        
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    
    


    public static void main(String[] args){
        SearcherDemo searcherDemo = new SearcherDemo();
    }
}