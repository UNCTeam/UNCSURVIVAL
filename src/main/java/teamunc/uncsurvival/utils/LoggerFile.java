package teamunc.uncsurvival.utils;

import teamunc.uncsurvival.UNCSurvival;

import java.util.ArrayList;

public class LoggerFile {
    private static ArrayList<String> linesToWrite = new ArrayList<>();

    public static void WriteNextLine() {
        if (linesToWrite.isEmpty()) return;
        String line = linesToWrite.remove(0);
        UNCSurvival.getInstance().getFileManager().writeInLogFile(line);
    }

    public static void AppendLineToWrite(String line) {
        linesToWrite.add(line);
    }
}
