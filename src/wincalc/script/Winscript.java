package wincalc.script;

import com.google.gson.Gson;
import enums.LayoutArea;
import enums.TypeElem;
import main.Main;

public class Winscript {

    ////////////////////////////////////////////////////////////////////////////
    public static int prj = 604005;
    ////////////////////////////////////////////////////////////////////////////
    
    public static AreaRoot rootArea;

    /**
     * @param prj - форма(скрипт) окна
     * @param nuni  - nini ветки системы профилей
     */
    public static String test(Integer prj, Integer nuni) {

        if (prj == 601001) {
            rootArea = new AreaRoot("1", LayoutArea.VERT, TypeElem.SQUARE, 900, 1300, 1300, 1009, 10009, 1009, "");
            rootArea.setParam(prj.toString(), 387, "KBE 58->1 ОКНА->Открывание внутрь (ств. Z77)");
            rootArea.add(new Element("2", TypeElem.FRAME_BOX, LayoutArea.LEFT));
            rootArea.add(new Element("3", TypeElem.FRAME_BOX, LayoutArea.RIGHT));
            rootArea.add(new Element("4", TypeElem.FRAME_BOX, LayoutArea.TOP));
            rootArea.add(new Element("5", TypeElem.FRAME_BOX, LayoutArea.BOTTOM));
            AreaElem area2 = (AreaElem) rootArea.add(new AreaElem("6", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':1, 'funic':23}"));
            area2.add(new Element("7", TypeElem.GLASS));

        } else if (prj == 601002) { 
            rootArea = new AreaRoot("1", LayoutArea.HORIZ, TypeElem.SQUARE, 1300, 1400, 1400, 1009, 10009, 1009, "");
            rootArea.setParam(prj.toString(), 433, "Montblanc->Nord-1 ОКНА");
            rootArea.add(new Element("2", TypeElem.FRAME_BOX, LayoutArea.LEFT));
            rootArea.add(new Element("3", TypeElem.FRAME_BOX, LayoutArea.RIGHT));
            rootArea.add(new Element("4", TypeElem.FRAME_BOX, LayoutArea.TOP));
            rootArea.add(new Element("5", TypeElem.FRAME_BOX, LayoutArea.BOTTOM));

            AreaElem area2 = (AreaElem) rootArea.add(new AreaElem("6", LayoutArea.HORIZ, TypeElem.AREA, 1300 / 2));
            rootArea.add(new Element("7", TypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem("8", LayoutArea.HORIZ, TypeElem.AREA, 1300 / 2));
            AreaElem area4 = (AreaElem) area2.add(new AreaElem("9", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':3, 'funic':20}"));
            AreaElem area5 = (AreaElem) area3.add(new AreaElem("10", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':3, 'funic':20}"));

            area4.add(new Element("11", TypeElem.GLASS));
            area5.add(new Element("12", TypeElem.GLASS));

        } else if (prj == 601003) {
            rootArea = new AreaRoot("1", LayoutArea.VERT, TypeElem.SQUARE, 1440, 1700, 1700, 1009, 1009, 1009, "");
            rootArea.setParam(prj.toString(), 4, "Darrio->DARRIO 200->1 ОКНА");
            rootArea.add(new Element("2", TypeElem.FRAME_BOX, LayoutArea.LEFT));
            rootArea.add(new Element("3", TypeElem.FRAME_BOX, LayoutArea.RIGHT));
            rootArea.add(new Element("4", TypeElem.FRAME_BOX, LayoutArea.TOP));
            rootArea.add(new Element("5", TypeElem.FRAME_BOX, LayoutArea.BOTTOM));

            AreaElem area2 = (AreaElem) rootArea.add(new AreaElem("6", LayoutArea.HORIZ, TypeElem.AREA, 400));
            rootArea.add(new Element("7", TypeElem.IMPOST));
            AreaElem area4 = (AreaElem) rootArea.add(new AreaElem("8", LayoutArea.HORIZ, TypeElem.AREA, 1300));
            
            AreaElem area5 = (AreaElem) area4.add(new AreaElem("9", LayoutArea.VERT, TypeElem.AREA, 720));
            area4.add(new Element("10", TypeElem.IMPOST));
            AreaElem area6 = (AreaElem) area4.add(new AreaElem("11", LayoutArea.VERT, TypeElem.AREA, 720));
            
            AreaElem area8 = (AreaElem) area5.add(new AreaElem("12", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':1, 'funic':337}"));
            AreaElem area9 = (AreaElem) area6.add(new AreaElem("13", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':3, 'funic':336}"));

            area2.add(new Element("14", TypeElem.GLASS));
            area8.add(new Element("15", TypeElem.GLASS));
            area9.add(new Element("16", TypeElem.GLASS));

        } else if (prj == 601004) {
            rootArea = new AreaRoot("1", LayoutArea.VERT, TypeElem.SQUARE, 1440, 1700, 1700, 1009, 1009, 1009, "");
            rootArea.setParam(prj.toString(), 387, "KBE 58->1 ОКНА->Открывание внутрь (ств. Z77)");
            rootArea.add(new Element("2", TypeElem.FRAME_BOX, LayoutArea.LEFT));
            rootArea.add(new Element("3", TypeElem.FRAME_BOX, LayoutArea.RIGHT));
            rootArea.add(new Element("4", TypeElem.FRAME_BOX, LayoutArea.TOP));
            rootArea.add(new Element("5", TypeElem.FRAME_BOX, LayoutArea.BOTTOM));

            AreaElem area2 = (AreaElem) rootArea.add(new AreaElem("6", LayoutArea.HORIZ, TypeElem.AREA, 400));
            rootArea.add(new Element("7", TypeElem.IMPOST));
            AreaElem area4 = (AreaElem) rootArea.add(new AreaElem("8", LayoutArea.HORIZ, TypeElem.AREA, 1300));
            AreaElem area5 = (AreaElem) area4.add(new AreaElem("9", LayoutArea.VERT, TypeElem.AREA, 720));
            area4.add(new Element("10", TypeElem.IMPOST));
            AreaElem area6 = (AreaElem) area4.add(new AreaElem("11", LayoutArea.VERT, TypeElem.AREA, 720));
            AreaElem area8 = (AreaElem) area5.add(new AreaElem("12", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':1, 'funic':23}"));
            AreaElem area9 = (AreaElem) area6.add(new AreaElem("13", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':4, 'funic':20}"));

            area2.add(new Element("14", TypeElem.GLASS));
            area8.add(new Element("15", TypeElem.GLASS));
            area9.add(new Element("16", TypeElem.GLASS));

        } else if (prj == 601005) {
            rootArea = new AreaRoot("1", LayoutArea.HORIZ, TypeElem.SQUARE, 1600, 1700, 1700, 1009, 1009, 1009, "");
            ////////////////////////////////////////////////////
            rootArea.setParam(prj.toString(), 8, "KBE->KBE 58->1 ОКНА->Открывание внутрь (ств. Z77)");
            rootArea.add(new Element("2", TypeElem.FRAME_BOX, LayoutArea.LEFT));
            rootArea.add(new Element("3", TypeElem.FRAME_BOX, LayoutArea.RIGHT));
            rootArea.add(new Element("4", TypeElem.FRAME_BOX, LayoutArea.TOP));
            rootArea.add(new Element("5", TypeElem.FRAME_BOX, LayoutArea.BOTTOM));

            AreaElem area2 = (AreaElem) rootArea.add(new AreaElem("6", LayoutArea.VERT, TypeElem.AREA, 800));
            rootArea.add(new Element("7", TypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem("8", LayoutArea.VERT, TypeElem.AREA, 800));
            AreaElem area4 = (AreaElem) area2.add(new AreaElem("9", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':1, 'funic':23}"));
            AreaElem area5 = (AreaElem) area3.add(new AreaElem("10", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':4, 'funic':20}"));

            area4.add(new Element("11", TypeElem.GLASS));
            area5.add(new Element("12", TypeElem.GLASS));

        } else if (prj == 601006) {
            rootArea = new AreaRoot("1", LayoutArea.HORIZ, TypeElem.SQUARE, 900, 1400, 1400, 1009, 1009, 1009, "");
            ///////////////////////////////////////////
            rootArea.setParam(prj.toString(), 110, "RAZIO->RAZIO 58 N->1 ОКНА");
            rootArea.add(new Element("2", TypeElem.FRAME_BOX, LayoutArea.LEFT));
            rootArea.add(new Element("3", TypeElem.FRAME_BOX, LayoutArea.RIGHT));
            rootArea.add(new Element("4", TypeElem.FRAME_BOX, LayoutArea.TOP));
            rootArea.add(new Element("5", TypeElem.FRAME_BOX, LayoutArea.BOTTOM));

            rootArea.add(new Element("6", TypeElem.GLASS, "{'nunic_iwin':'615496322'}")); //или 'R4x10x4x10x4'

        } else if (prj == 601007) {
            rootArea = new AreaRoot("1", LayoutArea.VERT, TypeElem.SQUARE, 1100, 1400, 1400, 1009, 10018, 10018, "");
            /////////////////////////////////////////////
            rootArea.setParam(prj.toString(), 87, "NOVOTEX->Techno 58->1 ОКНА");
            rootArea.add(new Element("2", TypeElem.FRAME_BOX, LayoutArea.LEFT));
            rootArea.add(new Element("3", TypeElem.FRAME_BOX, LayoutArea.RIGHT));
            rootArea.add(new Element("4", TypeElem.FRAME_BOX, LayoutArea.TOP));
            rootArea.add(new Element("5", TypeElem.FRAME_BOX, LayoutArea.BOTTOM));

            AreaElem area2 = (AreaElem) rootArea.add(new AreaElem("6", LayoutArea.HORIZ, TypeElem.AREA, 300));
            rootArea.add(new Element("7", TypeElem.IMPOST));
            AreaElem area4 = (AreaElem) rootArea.add(new AreaElem("8", LayoutArea.HORIZ, TypeElem.AREA, 1100));
            AreaElem area5 = (AreaElem) area4.add(new AreaElem("9", LayoutArea.VERT, TypeElem.AREA, 550));
            area4.add(new Element("10", TypeElem.IMPOST));
            AreaElem area6 = (AreaElem) area4.add(new AreaElem("11", LayoutArea.VERT, TypeElem.AREA, 550));
            AreaElem area8 = (AreaElem) area5.add(new AreaElem("12", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':1, 'funic':93}"));
            AreaElem area9 = (AreaElem) area6.add(new AreaElem("13", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':4, 'funic':92}"));

            area2.add(new Element("14", TypeElem.GLASS));
            area8.add(new Element("15", TypeElem.GLASS));
            area9.add(new Element("16", TypeElem.GLASS));

        } else if (prj == 601008) {
            rootArea = new AreaRoot("1", LayoutArea.HORIZ, TypeElem.SQUARE, 1200, 1700, 1700, 1009, 28014, 21057, "");
            //////////////////////////////////////////
            rootArea.setParam(prj.toString(), 99, "Rehau->Blitz new->1 ОКНА");
            rootArea.add(new Element("2", TypeElem.FRAME_BOX, LayoutArea.LEFT));
            rootArea.add(new Element("3", TypeElem.FRAME_BOX, LayoutArea.RIGHT));
            rootArea.add(new Element("4", TypeElem.FRAME_BOX, LayoutArea.TOP));
            rootArea.add(new Element("5", TypeElem.FRAME_BOX, LayoutArea.BOTTOM));

            AreaElem area2 = (AreaElem) rootArea.add(new AreaElem("6", LayoutArea.HORIZ, TypeElem.AREA, 600));
            rootArea.add(new Element("7", TypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem("8", LayoutArea.VERT, TypeElem.AREA, 600));
            AreaElem area4 = (AreaElem) area3.add(new AreaElem("9", LayoutArea.VERT, TypeElem.AREA, 550));
            area3.add(new Element("10", TypeElem.IMPOST));
            AreaElem area5 = (AreaElem) area3.add(new AreaElem("11", LayoutArea.VERT, TypeElem.AREA, 1150));
            AreaElem area6 = (AreaElem) area4.add(new AreaElem("12", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':4, 'funic':20}"));

            area2.add(new Element("13", TypeElem.GLASS));
            area6.add(new Element("14", TypeElem.GLASS));
            area5.add(new Element("15", TypeElem.GLASS));

        } else if (prj == 601009) {
            rootArea = new AreaRoot("1", LayoutArea.HORIZ, TypeElem.SQUARE, 700, 1400, 1400, 1009, 1009, 1009, "");
            ////////////////////////////////////////////
            rootArea.setParam(prj.toString(), 54, "KBE->KBE Эксперт->1 ОКНА->Открывание внутрь (ств. Z 77)");
            rootArea.add(new Element("2", TypeElem.FRAME_BOX, LayoutArea.LEFT));
            rootArea.add(new Element("3", TypeElem.FRAME_BOX, LayoutArea.RIGHT));
            rootArea.add(new Element("4", TypeElem.FRAME_BOX, LayoutArea.TOP));
            rootArea.add(new Element("5", TypeElem.FRAME_BOX, LayoutArea.BOTTOM));

            rootArea.add(new Element("6", TypeElem.GLASS, "{'nunic_iwin':'1685457539'}")); //или '4x12x4x12x4' для nuni = 54

        } else if (prj == 601010) {
            rootArea = new AreaRoot("1", LayoutArea.HORIZ, TypeElem.SQUARE, 1300, 1400, 1400, 1009, 1009, 1009, "{'pro4Params':[[-862071,295],[-862065,314],[-862062,325],[-862131,17],[-862097,195],[-862060,335]]}");
            rootArea.setParam(prj.toString(), 54, "KBE->KBE Эксперт->1 ОКНА->Открывание внутрь (ств. Z 77)");
            ///////////////////////////////////////////////////
            rootArea.add(new Element("2", TypeElem.FRAME_BOX, LayoutArea.LEFT));
            rootArea.add(new Element("3", TypeElem.FRAME_BOX, LayoutArea.RIGHT));
            rootArea.add(new Element("4", TypeElem.FRAME_BOX, LayoutArea.TOP));
            rootArea.add(new Element("5", TypeElem.FRAME_BOX, LayoutArea.BOTTOM));

            AreaElem area2 = (AreaElem) rootArea.add(new AreaElem("6", LayoutArea.VERT, TypeElem.AREA, 650));
            rootArea.add(new Element("7", TypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem("8", LayoutArea.VERT, TypeElem.AREA, 650));

            AreaElem area4 = (AreaElem) area2.add(new AreaElem("9", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':1,'funic':23, 'pro4Params': [[-862107,826],[-862106,830]]}"));
            AreaElem area5 = (AreaElem) area3.add(new AreaElem("10", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':4,'funic':20, 'pro4Params': [[-862107,184],[-862106,186]]}"));

            area4.add(new Element("11", TypeElem.GLASS, "{'nunic_iwin':'1685457539'}"));
            area5.add(new Element("12", TypeElem.GLASS, "{'nunic_iwin':'1685457539'}"));

        } else if (prj == 604004) {
            rootArea = new AreaRoot("1", LayoutArea.VERT, TypeElem.ARCH, 1300, 1050, 1700, 1009, 1009, 1009, "");
            rootArea.setParam(prj.toString(), 435, "Rehau->Delight->1 ОКНА");
            rootArea.add(new Element("2", TypeElem.FRAME_BOX, LayoutArea.LEFT));
            rootArea.add(new Element("3", TypeElem.FRAME_BOX, LayoutArea.RIGHT));
            rootArea.add(new Element("4", TypeElem.FRAME_BOX, LayoutArea.ARCH));
            rootArea.add(new Element("5", TypeElem.FRAME_BOX, LayoutArea.BOTTOM));

            rootArea.add(new Element("6", TypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem("7", LayoutArea.HORIZ, TypeElem.AREA, 1050));

            AreaElem area4 = (AreaElem) area3.add(new AreaElem("8", LayoutArea.VERT, TypeElem.AREA, 650));
            area3.add(new Element("9", TypeElem.IMPOST));
            AreaElem area5 = (AreaElem) area3.add(new AreaElem("10", LayoutArea.VERT, TypeElem.AREA, 650));

            AreaElem area6 = (AreaElem) area5.add(new AreaElem("11", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':4, 'funic':20}"));

            rootArea.add(new Element("12", TypeElem.GLASS));
            area4.add(new Element("13", TypeElem.GLASS));
            area6.add(new Element("14", TypeElem.GLASS));

        } else if (prj == 604005) {
            rootArea = new AreaRoot("1", LayoutArea.VERT, TypeElem.ARCH, 1300, 1200, 1500, 1009, 10009, 1009, "");
            rootArea.setParam(prj.toString(), 79, "Wintech->Termotech 742->1 ОКНА");
            rootArea.add(new Element("2", TypeElem.FRAME_BOX, LayoutArea.LEFT));
            rootArea.add(new Element("3", TypeElem.FRAME_BOX, LayoutArea.RIGHT));
            rootArea.add(new Element("4", TypeElem.FRAME_BOX, LayoutArea.ARCH));
            rootArea.add(new Element("5", TypeElem.FRAME_BOX, LayoutArea.BOTTOM));

            rootArea.add(new Element("6", TypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem("7", LayoutArea.HORIZ, TypeElem.AREA, 1200));
            
            AreaElem area4 = (AreaElem) area3.add(new AreaElem("8", LayoutArea.VERT, TypeElem.AREA, 650));
            area3.add(new Element("9", TypeElem.IMPOST));
            AreaElem area5 = (AreaElem) area3.add(new AreaElem("10", LayoutArea.VERT, TypeElem.AREA, 650));
            
            AreaElem area6 = (AreaElem) area4.add(new AreaElem("11", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':1, 'funic':23}"));
            AreaElem area7 = (AreaElem) area5.add(new AreaElem("12", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':4, 'funic':20}"));

            rootArea.add(new Element("13", TypeElem.GLASS));
            area6.add(new Element("14", TypeElem.GLASS));
            area7.add(new Element("15", TypeElem.GLASS));

        } else if (prj == 604006) {
            rootArea = new AreaRoot("1", LayoutArea.VERT, TypeElem.ARCH, 1100, 1220, 1600, 1009, 1009, 10012, "");
            //////////////////////////////////////////////////
            rootArea.setParam(prj.toString(), 135, "Wintech->Termotech 742->1 ОКНА");
            rootArea.add(new Element("2", TypeElem.FRAME_BOX, LayoutArea.LEFT));
            rootArea.add(new Element("3", TypeElem.FRAME_BOX, LayoutArea.RIGHT));
            rootArea.add(new Element("4", TypeElem.FRAME_BOX, LayoutArea.ARCH));
            rootArea.add(new Element("5", TypeElem.FRAME_BOX, LayoutArea.BOTTOM));

            rootArea.add(new Element("6", TypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem("7", LayoutArea.HORIZ, TypeElem.AREA, 1220));
            AreaElem area4 = (AreaElem) area3.add(new AreaElem("8", LayoutArea.VERT, TypeElem.AREA, 550));
            area3.add(new Element("9", TypeElem.IMPOST));
            AreaElem area5 = (AreaElem) area3.add(new AreaElem("10", LayoutArea.VERT, TypeElem.AREA, 550));
            AreaElem area6 = (AreaElem) area4.add(new AreaElem("11", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':1, 'funic':23}"));
            AreaElem area7 = (AreaElem) area5.add(new AreaElem("12", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':4, 'funic':20}"));

            rootArea.add(new Element("13", TypeElem.GLASS));
            area6.add(new Element("14", TypeElem.GLASS));
            area7.add(new Element("15", TypeElem.GLASS));

        } else if (prj == 604007) {
            rootArea = new AreaRoot("1", LayoutArea.VERT, TypeElem.ARCH, 1400, 1300, 1700, 1009, 1009, 10001, "");
            //////////////////////////////////////////////////
            rootArea.setParam(prj.toString(), 99, "Rehau->Blitz new->1 ОКНА");
            rootArea.add(new Element("2", TypeElem.FRAME_BOX, LayoutArea.LEFT));
            rootArea.add(new Element("3", TypeElem.FRAME_BOX, LayoutArea.RIGHT));
            rootArea.add(new Element("4", TypeElem.FRAME_BOX, LayoutArea.ARCH));
            rootArea.add(new Element("5", TypeElem.FRAME_BOX, LayoutArea.BOTTOM));

            rootArea.add(new Element("6", TypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem("7", LayoutArea.HORIZ, TypeElem.AREA, 1300));
            AreaElem area4 = (AreaElem) area3.add(new AreaElem("8", LayoutArea.VERT, TypeElem.AREA, 700));
            area3.add(new Element("9", TypeElem.IMPOST));
            AreaElem area5 = (AreaElem) area3.add(new AreaElem("10", LayoutArea.VERT, TypeElem.AREA, 700));
            AreaElem area6 = (AreaElem) area4.add(new AreaElem("11", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':1, 'funic':23}"));
            AreaElem area7 = (AreaElem) area5.add(new AreaElem("12", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':3, 'funic':20}"));

            rootArea.add(new Element("13", TypeElem.GLASS));
            area6.add(new Element("14", TypeElem.GLASS));
            area7.add(new Element("15", TypeElem.GLASS));

        } else if (prj == 604008) {
            rootArea = new AreaRoot("1", LayoutArea.VERT, TypeElem.ARCH, 1300, 1200, 1500, 1009, 10009, 1009, "");
            /////////////////////////////////////////////////
            rootArea.setParam(prj.toString(), 8, "KBE->KBE 58->1 ОКНА->Открывание внутрь (ств. Z77)");
            rootArea.add(new Element("2", TypeElem.FRAME_BOX, LayoutArea.LEFT));
            rootArea.add(new Element("3", TypeElem.FRAME_BOX, LayoutArea.RIGHT));
            rootArea.add(new Element("4", TypeElem.FRAME_BOX, LayoutArea.ARCH));
            rootArea.add(new Element("5", TypeElem.FRAME_BOX, LayoutArea.BOTTOM));

            rootArea.add(new Element("6", TypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem("7", LayoutArea.HORIZ, TypeElem.AREA, 1200));
            AreaElem area4 = (AreaElem) area3.add(new AreaElem("8", LayoutArea.VERT, TypeElem.AREA, 650));
            area3.add(new Element("9", TypeElem.IMPOST));
            AreaElem area5 = (AreaElem) area3.add(new AreaElem("10", LayoutArea.VERT, TypeElem.AREA, 650));
            AreaElem area6 = (AreaElem) area4.add(new AreaElem("11", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':3, 'funic':-1}"));
            AreaElem area7 = (AreaElem) area5.add(new AreaElem("12", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':3, 'funic':-1}"));

            rootArea.add(new Element("13", TypeElem.GLASS));
            area6.add(new Element("14", TypeElem.GLASS));
            area7.add(new Element("15", TypeElem.GLASS));

        } else if (prj == 604009) {
            rootArea = new AreaRoot("1", LayoutArea.VERT, TypeElem.ARCH, 1300, 1200, 1500, 1009, 10009, 1009, "");
            /////////////////////////////////////
            rootArea.setParam(prj.toString(), 8, "KBE->KBE 58->1 ОКНА->Открывание внутрь (ств. Z77)");
            rootArea.add(new Element("2", TypeElem.FRAME_BOX, LayoutArea.LEFT));
            rootArea.add(new Element("3", TypeElem.FRAME_BOX, LayoutArea.RIGHT));
            rootArea.add(new Element("4", TypeElem.FRAME_BOX, LayoutArea.ARCH));
            rootArea.add(new Element("5", TypeElem.FRAME_BOX, LayoutArea.BOTTOM));

            rootArea.add(new Element("6", TypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem("7", LayoutArea.HORIZ, TypeElem.AREA, 1200));

            rootArea.add(new Element("8", TypeElem.GLASS));
            area3.add(new Element("9", TypeElem.GLASS));

        } else if (prj == 604010) {
            rootArea = new AreaRoot("1", LayoutArea.VERT, TypeElem.ARCH, 1300, 1400, 1700, 1009, 10009, 1009, "");
            ///////////////////////////////////////////////
            rootArea.setParam(prj.toString(), 29, "Montblanc->Nord->1 ОКНА");
            rootArea.add(new Element("2", TypeElem.FRAME_BOX, LayoutArea.LEFT));
            rootArea.add(new Element("3", TypeElem.FRAME_BOX, LayoutArea.RIGHT));
            rootArea.add(new Element("4", TypeElem.FRAME_BOX, LayoutArea.ARCH));
            rootArea.add(new Element("5", TypeElem.FRAME_BOX, LayoutArea.BOTTOM));

            rootArea.add(new Element("6", TypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem("7", LayoutArea.HORIZ, TypeElem.AREA, 1400));
            AreaElem area4 = (AreaElem) area3.add(new AreaElem("8", LayoutArea.VERT, TypeElem.AREA, 650));
            area3.add(new Element("9", TypeElem.IMPOST));
            AreaElem area5 = (AreaElem) area3.add(new AreaElem("10", LayoutArea.VERT, TypeElem.AREA, 650));
            AreaElem area6 = (AreaElem) area5.add(new AreaElem("11", LayoutArea.VERT, TypeElem.AREA, 550));
            area5.add(new Element("12", TypeElem.IMPOST));
            AreaElem area7 = (AreaElem) area5.add(new AreaElem("13", LayoutArea.VERT, TypeElem.AREA, 850));

            AreaElem area8 = (AreaElem) area6.add(new AreaElem("14", LayoutArea.FULL, TypeElem.FULLSTVORKA, "{'typeOpen':4, 'funic':20}"));
            rootArea.add(new Element("15", TypeElem.GLASS));
            area4.add(new Element("16", TypeElem.GLASS));
            area7.add(new Element("17", TypeElem.GLASS));
            area8.add(new Element("18", TypeElem.GLASS));

        } else if (prj == 605001) {
            rootArea = new AreaRoot("1", LayoutArea.VERT, TypeElem.TRAPEZE, 1300, 1200, 1500, 1009, 10009, 1009, "");
            rootArea.setParam(prj.toString(), 8, "KBE->KBE 58->1 ОКНА->Открывание внутрь (ств. Z77)");
            rootArea.add(new Element("2", TypeElem.FRAME_BOX, LayoutArea.LEFT));
            rootArea.add(new Element("3", TypeElem.FRAME_BOX, LayoutArea.RIGHT));
            rootArea.add(new Element("4", TypeElem.FRAME_BOX, LayoutArea.ARCH));
            rootArea.add(new Element("5", TypeElem.FRAME_BOX, LayoutArea.BOTTOM));

            rootArea.add(new Element("6", TypeElem.IMPOST));
            AreaElem area3 = (AreaElem) rootArea.add(new AreaElem("7", LayoutArea.HORIZ, TypeElem.AREA, 1200));

            rootArea.add(new Element("8", TypeElem.GLASS));
            area3.add(new Element("9", TypeElem.GLASS));

        }
        if (nuni != null) {
            rootArea.setParam(prj.toString(), nuni);
        }
        return new Gson().toJson(rootArea);

    }
}
