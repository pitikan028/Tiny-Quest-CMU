package io.github.tinyquestcmu.dialogue;

public class Dialogue {
    private DialogueNode head;
    private DialogueNode current;
    public Dialogue(DialogueNode head){ this.head = head; this.current = head; }
    public String getCurrentText(){ return current != null ? current.text : null; }
    public boolean advance(){ if(current!=null) current = current.next; return current!=null; }
    public boolean isFinished(){ return current == null; }
    public void reset(){ current = head; }
}
