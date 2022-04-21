package teamunc.uncsurvival.utils;

import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.team.Team;

import java.util.ArrayList;

public class LoggerFile {
    private static ArrayList<String> linesToWrite = new ArrayList<>();
    private static ArrayList<String> ScoresToWrite = new ArrayList<>();

    public static void WriteNextLine() {
        if (linesToWrite.isEmpty()) return;
        String line = linesToWrite.remove(0);
        UNCSurvival.getInstance().getFileManager().writeInLogFile(line);
    }

    public static void WriteNextScore() {
        if (ScoresToWrite.isEmpty()) return;
        String line = ScoresToWrite.remove(0);
        UNCSurvival.getInstance().getFileManager().writeInLogFile(line);
    }

    public static void AppendLineToWrite(String line) {
        /*linesToWrite.add(line);*/
    }

    public static void AppendScoreToWrite(Team team) {
        ScoresToWrite.add("[ " + team.getChatColor().toString() + " ] : " + team.getScore());
    }
}

