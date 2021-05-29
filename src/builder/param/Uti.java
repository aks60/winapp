package builder.param;

import builder.model.ElemSimple;
import dataset.Record;
import domain.eArtikl;
import domain.eSysprof;
import domain.eSystree;
import enums.LayoutArea;
import enums.TypeElem;
import java.util.Arrays;
import java.util.List;

public class Uti {

    //Тип заполнения 
    static boolean dic_37009(String txt, ElemSimple elem5e) {
        //Все, Произвольное, Прямоугольное, Арочное                                            
        if ("Прямоугольное".equalsIgnoreCase(txt) && (TypeElem.RECTANGL.equals(elem5e.owner().type()) || TypeElem.AREA.equals(elem5e.owner().type()) == false)) {
            return false;
        } else if ("Арочное".equalsIgnoreCase(txt) && TypeElem.ARCH.equals(elem5e.owner().type()) == false) {
            return false;
        }
        return true;
    }

    //Тип проема 
    static boolean dic_13003_14005_15005_37008(String txt, ElemSimple elem5e) {
        if ("глухой".equals(txt) == true && elem5e.owner().type() == TypeElem.STVORKA == true) {
            return false;
        } else if ("не глухой".equals(txt) == true && elem5e.owner().type() == TypeElem.STVORKA == false) {
            return false;
        }
        return true;
    }

    static boolean check_dic_1005_2005_3005_4005_11005_12005_31050_33071_34071() {
      
        return true;
    }
    
    //Для технологического кода контейнера 
    static boolean check_STRING_33000_34000(String txt, ElemSimple elem5e) {
        Record sysprofRec = elem5e.sysprofRec;
        Record artiklVRec = eArtikl.find(sysprofRec.getInt(eSysprof.artikl_id), false);
        if (artiklVRec.get(eArtikl.tech_code) == null) {
            return false;
        }
        String[] strList = txt.split(";");
        String[] strList2 = artiklVRec.getStr(eArtikl.tech_code).split(";");
        boolean ret2 = false;
        for (String str : strList) {
            for (String str2 : strList2) {
                if (str.equalsIgnoreCase(str2)) {
                    ret2 = true;
                }
            }
        }
        if (ret2 == false) {
            return false;
        }
        return true;
    }

    //Если признак системы конструкции
    static boolean check_STRING_33095_34095_38095_39095_40095(String txt, ElemSimple elem5e, int nuni) {
        Record systreefRec = eSystree.find(nuni);
        String[] arr = txt.split(";");
        List<String> arrList = Arrays.asList(arr);
        boolean ret = false;
        for (String str : arrList) {
            if (systreefRec.getInt(eSystree.types) == Integer.valueOf(str) == true) {
                ret = true;
            }
        }
        if (ret == false) {
            return false;
        }
        return true;
    }

    //Если номер стороны в контуре
    static boolean check_INT_33066_34066(String txt, ElemSimple elem5e) {
        if ("1".equals(txt) == true && LayoutArea.BOTTOM != elem5e.layout()) {
            return false;
        } else if ("2".equals(txt) == true && LayoutArea.RIGHT != elem5e.layout()) {
            return false;
        } else if ("3".equals(txt) == true && LayoutArea.TOP != elem5e.layout()) {
            return false;
        } else if ("4".equals(txt) == true && LayoutArea.LEFT != elem5e.layout()) {
            return false;
        }
        return true;
    }
    
    static boolean p_13081_13082_13086_13087(ElemSimple elem5e, String txt) {
        return true;
    }    
    
}
