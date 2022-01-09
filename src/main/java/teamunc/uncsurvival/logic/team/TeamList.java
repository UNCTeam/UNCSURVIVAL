package teamunc.uncsurvival.logic.team;

import org.bukkit.ChatColor;

public enum TeamList {
    TEAM_1(ChatColor.RED, "Team Rouge"),
    TEAM_2(ChatColor.YELLOW, "Team Jaune"),
    TEAM_3(ChatColor.GREEN, "Team Verte"),
    TEAM_4(ChatColor.BLUE, "Team Bleue");

    private ChatColor chatColor;
    private String name;

    TeamList(ChatColor chatColor, String name) {
        this.chatColor = chatColor;
        this.name = name;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public String getName() {
        return name;
    }
}
