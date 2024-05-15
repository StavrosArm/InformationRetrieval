import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.Similarity;


import java.io.IOException;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;

import txtparsing.TXTParsing;
import txtparsing.MyDoc;
import java.util.List;



// tested for lucene 7.7.3 and jdk13
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import txtparsing.*;


public class IndexerDemo {
    static String  filepath = System.getProperty("user.dir"); // user dir gives the directory the projects is in
   
    public IndexerDemo() throws Exception{
        String txtfile =  "\\docs\\documents.txt";
        
        String indexLocation = ("index"); //define were to store the index        
        
        Date start = new Date();
        try {
            System.out.println("Indexing to directory '" + indexLocation + "'...");
            
            Directory dir = FSDirectory.open(Paths.get(indexLocation));
           
            EnglishAnalyzer analyzer = new EnglishAnalyzer();
           
            Similarity similarity = new ClassicSimilarity();
            
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
            iwc.setSimilarity(similarity);

            
            iwc.setOpenMode(OpenMode.CREATE); //create new index delete previus ones

            
            IndexWriter indexWriter = new IndexWriter(dir, iwc);
            
            
            List<MyDoc> docs = TXTParsing.parse(filepath + txtfile);
            for (MyDoc doc : docs){
                indexDoc(indexWriter, doc);
            }
            
            indexWriter.close();
            
            Date end = new Date();
            System.out.println(end.getTime() - start.getTime() + " total milliseconds");
            
        } catch (IOException e) {
            System.out.println(" caught a " + e.getClass() +
                    "\n with message: " + e.getMessage());
        }
        
        
    }
    
    
    private void indexDoc(IndexWriter indexWriter, MyDoc mydoc){
        
        try {
            
           
            Document doc = new Document();
            
           
            StoredField docID = new StoredField("Document ID", mydoc.getID());
            doc.add(docID);
            String fullSearchableText = mydoc.getID() + " " + mydoc.getText();            
            TextField text = new TextField("text", fullSearchableText, Field.Store.NO);
            doc.add(text);
            
            if (indexWriter.getConfig().getOpenMode() == OpenMode.CREATE) {
                
                System.out.println("adding " + mydoc);
                indexWriter.addDocument(doc);
            } 
        } catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
   
    public static void main(String[] args) {
        try {
            IndexerDemo indexerDemo = new IndexerDemo();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
}