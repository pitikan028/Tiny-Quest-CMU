package io.github.tinyquestcmu.quest;

import java.util.EnumSet;

public class QuestManager {
    private EnumSet<QuestFlag> flags = EnumSet.noneOf(QuestFlag.class);

    public void set(QuestFlag f){ flags.add(f); }
    public boolean has(QuestFlag f){ return flags.contains(f); }

    public String hud(){
        if(!has(QuestFlag.TALKED_TO_CMULEGEND)) return "Talk to Rin/Pavo";
        if(!has(QuestFlag.MET_LEGEND)) return "Find the legend at the bridge";
        if(!has(QuestFlag.GOT_FOREST_MISSION)) return "Go to village for a mission";
        if(!has(QuestFlag.FOUND_SPECIAL_TREE)) return "Find the special tree in forest";
        if(!has(QuestFlag.FREED_BROTHER)) return "Free your brother";
        if(!has(QuestFlag.FINISHED)) return "Return home";
        return "Quest complete!";
    }
}
