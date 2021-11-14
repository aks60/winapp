package builder.param;

import dataset.Record;
import domain.eArtikl;
import domain.eGlasdet;
import domain.eGlaspar2;
import domain.eSetting;
import domain.eSystree;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import builder.Wincalc;
import builder.model.ElemGlass;
import builder.model.ElemSimple;
import common.UCom;
import enums.Layout;
import enums.Type;

//Заполнения
public class KitDet extends Par5s {

    public KitDet(Wincalc iwin) {
        super(iwin);
    }

    public boolean filter(HashMap<Integer, String> mapParam, ElemGlass elem5e, Record glasdetRec) {

        List<Record> paramList = eGlaspar2.find(glasdetRec.getInt(eGlasdet.id)); //список параметров детализации  
        if (filterParamDef(paramList) == false) {
            return false; //параметры по умолчанию
        }
        //Цикл по параметрам заполнения
        for (Record rec : paramList) {
            if (check(mapParam, elem5e, rec) == false) {
                return false;
            }
        }
        return true;
    }

    public boolean check(HashMap<Integer, String> mapParam, ElemGlass elem5e, Record rec) {

        int grup = rec.getInt(GRUP);
        try {
            switch (grup) {
                case 7030:  //Количество 
                    message(rec.getInt(GRUP));
                    break;
                case 7040:  //Порог расчета, мм 
                    message(rec.getInt(GRUP));
                    break;
                case 7050:  //Шаг, мм 
                    message(rec.getInt(GRUP));
                    break;
                case 7060:  //Количество на шаг 
                    message(rec.getInt(GRUP));
                    break;
                case 7078:  //Комплект одно текстурный 
                    message(rec.getInt(GRUP));
                    break;
                case 7081:  //Если ширина комплекта, мм 
                    message(rec.getInt(GRUP));
                    break;
                case 7098:  //Бригада (участок) 
                    message(rec.getInt(GRUP));
                    break;
                case 7099:  //Трудозатраты, ч/ч. 
                    message(rec.getInt(GRUP));
                    break;
                case 8050:  //Поправка, мм 
                    message(rec.getInt(GRUP));
                    break;
                case 8060:  //Количество" 
                    message(rec.getInt(GRUP));
                    break;
                case 8065:  //Ширина, мм 
                    message(rec.getInt(GRUP));
                    break;
                case 8070:  //Длина, мм 
                    message(rec.getInt(GRUP));
                    break;
                case 8075:  //Углы реза 
                    message(rec.getInt(GRUP));
                    break;
                case 8081:  //Ширина комплекта, мм 
                    message(rec.getInt(GRUP));
                    break;
                case 8083:  //Набрать_длину_с_нахлестом, мм 
                    message(rec.getInt(GRUP));
                    break;
                case 8097:  //Трудозатраты по длине 
                    message(rec.getInt(GRUP));
                    break;                    
                case 8098:  //Бригада (участок) 
                    message(rec.getInt(GRUP));
                    break;
                case 8099:  //Трудозатраты, ч/ч. 
                    message(rec.getInt(GRUP));
                    break;
                case 9050:  //Поправка длины, мм 
                    message(rec.getInt(GRUP));
                    break;
                case 9055:  //Поправка ширины, мм
                    message(rec.getInt(GRUP));
                    break;
                case 9060:  //Количество 
                    message(rec.getInt(GRUP));
                    break;
                case 9065:  //Длина, мм 
                    message(rec.getInt(GRUP));
                    break;
                case 9070:  //Ширина, мм 
                    message(rec.getInt(GRUP));
                    break;
                case 9081:  //Если ширина комплекта, мм 
                    message(rec.getInt(GRUP));
                    break;
                case 9083:  //Набрать длину с нахлестом/Длина , мм 
                    message(rec.getInt(GRUP));
                    break;
                case 9097:  //Трудозатраты по площади 
                    message(rec.getInt(GRUP));
                    break;                    
                case 9098:  //Бригада (участок) 
                    message(rec.getInt(GRUP));
                    break;
                case 9099:  //Трудозатраты, ч/ч. 
                    message(rec.getInt(GRUP));
                    break;
                default:
                    assert !(grup > 0 && grup < 50000) : "Код " + grup + "  не обработан!!!";
            }
        } catch (Exception e) {
            System.err.println("Ошибка:param.FillingDet.check()  parametr=" + grup + "    " + e);
            return false;
        }
        return true;
    }
}

