package io.github.tinyquestcmu.dialogue;

public class DialogueNode {
    public final String text;
    public final DialogueNode next;
    public DialogueNode(String text, DialogueNode next){
        this.text = text; this.next = next;
    }
}
