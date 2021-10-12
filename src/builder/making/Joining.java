package builder.making;

import dataset.Record;
import domain.eArtikl;
import domain.eJoindet;
import domain.eJoining;
import domain.eJoinvar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import builder.Wincalc;
import builder.param.ElementDet;
import builder.param.JoiningDet;
import builder.param.JoiningVar;
import builder.model.ElemJoining;
import builder.model.ElemSimple;
import dataset.Query;
import enums.TypeJoin;
import enums.LayoutJoin;
import java.util.ArrayList;

//Соединения
public class Joining extends Cal5e {

    private JoiningVar joiningVar = null;
    private JoiningDet joiningDet = null;
    private ElementDet elementDet = null;

    public Joining(Wincalc iwin) {
        super(iwin);
        joiningVar = new JoiningVar(iwin);
        joiningDet = new JoiningDet(iwin);
        elementDet = new ElementDet(iwin);
    }

    public Joining(Wincalc iwin, boolean shortPass) {
        super(iwin);
        joiningVar = new JoiningVar(iwin);
        joiningDet = new JoiningDet(iwin);
        elementDet = new ElementDet(iwin);
        this.shortPass = shortPass;
        calc();
    }

    @Override
    public void calc() {
        super.calc();
        try {
            //Цикл по списку соединений
            for (Map.Entry<String, ElemJoining> hmElemJoin : iwin.mapJoin.entrySet()) {

                ElemJoining elemJoin = hmElemJoin.getValue();
                ElemSimple joinElem1 = elemJoin.elem1;
                ElemSimple joinElem2 = elemJoin.elem2;
                Record joinartRec1 = joinElem1.artiklRecAn; //берём аналог профиля
                Record joinartRec2 = joinElem2.artiklRecAn; //т.к. если его нет там будет оригинал              
                int id1 = (joinartRec1.get(eArtikl.analog_id) == null) ? joinartRec1.getInt(eArtikl.id) : joinartRec1.getInt(eArtikl.analog_id);
                int id2 = (joinartRec2.get(eArtikl.analog_id) == null) ? joinartRec2.getInt(eArtikl.id) : joinartRec2.getInt(eArtikl.analog_id);
                Record joiningRec = eJoining.find(id1, id2);
                if (joiningRec.get(1) == null) {
                    joiningRec = eJoining.find(id2, id1);
                }

                //Список вариантов соединения для артикула1 и артикула2
                List<Record> joinvarList = eJoinvar.find(joiningRec.getInt(eJoining.id));
                //Если неудача, ищем в аналоге соединения
                if (joinvarList.isEmpty() == true && joiningRec.getStr(eJoining.analog).isEmpty() == false) {
                    joiningRec = eJoining.find2(joiningRec.getStr(eJoining.analog));
                    joinvarList = eJoinvar.find(joiningRec.getInt(eJoining.id));
                }
                //listVariants.add(joiningRec.getInt(eJoining.id)); //сделано для запуска формы Joining на ветке Systree 
                Collections.sort(joinvarList, (connvar1, connvar2) -> connvar1.getInt(eJoinvar.prio) - connvar2.getInt(eJoinvar.prio));

                //Цикл по вариантам соединения
                for (Record joinvarRec : joinvarList) {

                    //Если варианты соединения совпали
                    if (elemJoin.layout.equalType(joinvarRec.getInt(eJoinvar.types))) {

                        //ФИЛЬТР вариантов  
                        if (joiningVar.filter(elemJoin, joinvarRec) == true) {
                            listVariants.add(joiningRec.getInt(eJoining.id)); //сделано для запуска формы Joining на ветке Systree 

                            //Сохраним подхоящий вариант соединения из таблиц bd
                            elemJoin.type = TypeJoin.get(joinvarRec.getInt(eJoinvar.types));
                            elemJoin.joiningRec = joiningRec;
                            elemJoin.joinvarRec = joinvarRec;

                            if (shortPass == true) { //выход при поиске варианта соединения
                                continue;
                            }
                            List<Record> joindetList = eJoindet.find(joinvarRec.getInt(eJoinvar.id));
                            //Цикл по детализации соединений
                            for (Record joindetRec : joindetList) {
                                HashMap<Integer, String> mapParam = new HashMap(); //тут накапливаются параметры

                                //ФИЛЬТР детализации 
                                if (joiningDet.filter(mapParam, elemJoin, joindetRec) == true) {
                                    Record artiklRec = eArtikl.find(joindetRec.getInt(eJoindet.artikl_id), false);
                                    Specific spcAdd = new Specific(joindetRec, artiklRec, joinElem1, mapParam);
                                    if (UColor.colorFromProduct(spcAdd, 1)
                                            && UColor.colorFromProduct(spcAdd, 2)
                                            && UColor.colorFromProduct(spcAdd, 3)) {

                                        spcAdd.place = "СОЕД";
                                        elemJoin.addSpecific(spcAdd);
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка:Joining.calc() " + e);
        } finally {
            Query.conf = conf;
        }
    }

    public List<Record> varList(ElemJoining elemJoin) {
        List<Record> list = new ArrayList();

        Record joiningRec = eJoining.find(elemJoin.elem1.artiklRecAn, elemJoin.elem2.artiklRecAn);
        //Список вариантов соединения для артикула1 и артикула2
        List<Record> joinvarList = eJoinvar.find(joiningRec.getInt(eJoining.id));
        //Если неудача, ищем в аналоге соединения
        if (joinvarList.isEmpty() == true && joiningRec.getStr(eJoining.analog).isEmpty() == false) {
            joiningRec = eJoining.find2(joiningRec.getStr(eJoining.analog));
            joinvarList = eJoinvar.find(joiningRec.getInt(eJoining.id));
        }
        Collections.sort(joinvarList, (connvar1, connvar2) -> connvar1.getInt(eJoinvar.prio) - connvar2.getInt(eJoinvar.prio));

        //Цикл по вариантам соединения
        for (Record joinvarRec : joinvarList) {
            //Если варианты соединения совпали
            if (elemJoin.layout.equalType(joinvarRec.getInt(eJoinvar.types))) {
                //ФИЛЬТР вариантов  
                if (joiningVar.filter(elemJoin, joinvarRec) == true) {
                    joinvarRec.setNo(eJoinvar.name, joinvarRec.getStr(eJoinvar.name) + " (" + joiningRec.getStr(eJoining.name) + ")");
                    list.add(joinvarRec);
                }
            }
        }
        return list;
    }
}
