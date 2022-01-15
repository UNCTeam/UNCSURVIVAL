package teamunc.uncsurvival.logic.interfaces;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public abstract class GameCustomInterface {

    protected Inventory inv;
    protected String name;

    public GameCustomInterface(int itemNumber) {
        switch (itemNumber) {
            case 1:
                this.name = ChatColor.WHITE +"\uF80B本";
                break;
            case 2:
                this.name = ChatColor.WHITE +"\uF80B北";
                break;
            case 3:
                this.name = ChatColor.WHITE +"\uF80B被";
                break;
            case 4:
                this.name = ChatColor.WHITE +"\uF80B背";
                break;
            case 5:
                this.name = ChatColor.WHITE +"\uF80B备";
                break;
        }


        this.name = name;
        this.inv = Bukkit.createInventory(null, 27,name);
    }

    public String translateInInterfaceDisplay(String numbers, int ligne) {
        String res = "";
        for (char c : numbers.toCharArray()) {
            switch (ligne) {
                case 1:
                    switch(c) {
                        case '0':
                            res+= "准";
                            break;
                        case '1':
                            res+= "资";
                            break;
                        case '2':
                            res+= "子";
                            break;
                        case '3':
                            res+= "仔";
                            break;
                        case '4':
                            res+= "字";
                            break;
                        case '5':
                            res+= "自";
                            break;
                        case '6':
                            res+= "总";
                            break;
                        case '7':
                            res+= "走";
                            break;
                        case '8':
                            res+= "租";
                            break;
                        case '9':
                            res+= "族";
                            break;
                    }
                    break;
                case 2:
                    switch(c) {
                        case '0':
                            res+= "住";
                            break;
                        case '1':
                            res+= "祝";
                            break;
                        case '2':
                            res+= "注";
                            break;
                        case '3':
                            res+= "著";
                            break;
                        case '4':
                            res+= "助";
                            break;
                        case '5':
                            res+= "专";
                            break;
                        case '6':
                            res+= "转";
                            break;
                        case '7':
                            res+= "庄";
                            break;
                        case '8':
                            res+= "装";
                            break;
                        case '9':
                            res+= "壮";
                            break;
                    }
                    break;
                case 3:
                    switch(c) {
                        case '0':
                            res+= "足";
                            break;
                        case '1':
                            res+= "组";
                            break;
                        case '2':
                            res+= "嘴";
                            break;
                        case '3':
                            res+= "最";
                            break;
                        case '4':
                            res+= "昨";
                            break;
                        case '5':
                            res+= "左";
                            break;
                        case '6':
                            res+= "作";
                            break;
                        case '7':
                            res+= "做";
                            break;
                        case '8':
                            res+= "坐";
                            break;
                        case '9':
                            res+= "座";
                            break;
                    }
                    break;
                case 4:
                    switch(c) {
                        case '0':
                            res+= "钟";
                            break;
                        case '1':
                            res+= "终";
                            break;
                        case '2':
                            res+= "种";
                            break;
                        case '3':
                            res+= "重";
                            break;
                        case '4':
                            res+= "众";
                            break;
                        case '5':
                            res+= "周";
                            break;
                        case '6':
                            res+= "洲";
                            break;
                        case '7':
                            res+= "州";
                            break;
                        case '8':
                            res+= "竹";
                            break;
                        case '9':
                            res+= "主";
                            break;
                    }
                    break;
                case 5:
                    switch(c) {
                        case '0':
                            res+= "只";
                            break;
                        case '1':
                            res+= "指";
                            break;
                        case '2':
                            res+= "纸";
                            break;
                        case '3':
                            res+= "止";
                            break;
                        case '4':
                            res+= "至";
                            break;
                        case '5':
                            res+= "制";
                            break;
                        case '6':
                            res+= "治";
                            break;
                        case '7':
                            res+= "致";
                            break;
                        case '8':
                            res+= "志";
                            break;
                        case '9':
                            res+= "中";
                            break;
                    }
                    break;
            }
        }

        return res;
    }

    public String reduceAt(int number) {
        String res = "";
        for (int i = 0; i < number; i++) {
            res += "\uF80B";
        }

        return res;
    }
}
