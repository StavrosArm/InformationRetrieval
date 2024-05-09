package src.txtparsing;


public class MyDoc {

    private int docID;
    private String text;


    public MyDoc(int docID, String text) {
        this.docID = docID;
        this.text = text;
    }

    @Override
    public String toString() {
        String ret = "MyDoc{"
                + "\n\tDocument ID " + docID
                + "\n\tText " + text ;
        return ret + "\n}";
    }

    //---- Getters & Setters definition ----
    public int getID() {
        return docID;
    }

    public void setID(int id) {
        this.docID = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String caption) {
        this.text = caption;
    }

}