package io.github.tinyquestcmu.quest;

public class Quest {
    private String title;
    private String objective;
    private boolean complete;

    public Quest(String title, String objective){
        this.title = title; this.objective = objective; this.complete = false;
    }
    public String title(){ return title; }
    public String objective(){ return objective; }
    public boolean isComplete(){ return complete; }
    public void complete(){ this.complete = true; }
}
