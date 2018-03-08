package alohomora.model;

public class Element {
    private int id;
    private int parent;
    private String content;

    public int getId() {
        return id;
    }

    public int getParent() {
        return parent;
    }

    public String getContent() {
        return content;
    }

    public Element(int id, int parent, String content) {

        this.id = id;
        this.parent = parent;
        this.content = content;
    }
}
