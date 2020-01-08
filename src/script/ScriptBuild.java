package script;

import enums.eLayoutArea;
import enums.eTypeElem;

public class ScriptBuild {

    private static Area rootArea;

    public static void addArea(String str) {
        switch (str) {
            case ("areaSquare"):
                rootArea = new Area("1", eLayoutArea.VERTICAL, eTypeElem.SQUARE, 900, 1300, 1009, 10009, 1009, "");
                rootArea.setParam(8, "01");
                rootArea.add(new script.Element("2", eTypeElem.FRAME, eLayoutArea.LEFT));
                rootArea.add(new script.Element("3", eTypeElem.FRAME, eLayoutArea.RIGHT));
                rootArea.add(new script.Element("4", eTypeElem.FRAME, eLayoutArea.TOP));
                rootArea.add(new script.Element("5", eTypeElem.FRAME, eLayoutArea.BOTTOM));
                break;
            case ("areaArch"):
                break;
            case ("areaArch2"):
                rootArea = new Area("1", eLayoutArea.VERTICAL, eTypeElem.ARCH, 1300, 1700, 1050, 1009, 1009, 1009, "");
                rootArea.setParam(37, "01");
                rootArea.add(new script.Element("2", eTypeElem.FRAME, eLayoutArea.LEFT));
                rootArea.add(new script.Element("3", eTypeElem.FRAME, eLayoutArea.RIGHT));
                rootArea.add(new script.Element("4", eTypeElem.FRAME, eLayoutArea.ARCH));
                rootArea.add(new script.Element("5", eTypeElem.FRAME, eLayoutArea.BOTTOM));
                break;
            default:
                break;
        }
    }
    
    public static void addElem(String str) {
        switch (str) {
            case ("impostVert"):
                break;
            case ("impostGoriz"):
                break;
            default:
                break;
        }        
    }
}
