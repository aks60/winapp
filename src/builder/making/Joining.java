package builder.making;

import dataset.Record;
import domain.eArtdet;
import domain.eArtikl;
import domain.eJoindet;
import domain.eJoining;
import domain.eJoinpar1;
import domain.eJoinpar2;
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

    public void calc() {
        super.calc();
        try {
            //Цикл по списку соединений
            for (Map.Entry<String, ElemJoining> hmElemJoin : iwin().mapJoin.entrySet()) {
                ElemJoining elemJoin = hmElemJoin.getValue();
                ElemSimple joinElem1 = elemJoin.elem1;
                ElemSimple joinElem2 = elemJoin.elem2;
                Record joinartRec1 = joinElem1.artiklRecAn; //берём аналог профиля
                Record joinartRec2 = joinElem2.artiklRecAn; //т.к. если его нет там будет оригинал              
                int id1 = (joinartRec1.get(eArtikl.analog_id) == null) ? joinartRec1.getInt(eArtikl.id) : joinartRec1.getInt(eArtikl.analog_id);
                int id2 = (joinartRec2.get(eArtikl.analog_id) == null) ? joinartRec2.getInt(eArtikl.id) : joinartRec2.getInt(eArtikl.analog_id);
                
                Record joiningRec = eJoining.find(id1, id2);
                List<Record> joinvarList = eJoinvar.find(joiningRec.getInt(eJoining.id));

                //Если неудача, ищем в аналоге соединения
                if (joinvarList.isEmpty() == true && joiningRec.getStr(eJoining.analog).isEmpty() == false) {
                    joiningRec = eJoining.find2(joiningRec.getStr(eJoining.analog));
                    joinvarList = eJoinvar.find(joiningRec.getInt(eJoining.id));
                }
                listVariants.add(joiningRec.getInt(eJoining.id)); //сделано для запуска формы Joining на ветке Systree               
                Collections.sort(joinvarList, (connvar1, connvar2) -> connvar1.getInt(eJoinvar.prio) - connvar2.getInt(eJoinvar.prio));

                //Цикл по вариантам соединения
                for (Record joinvarRec : joinvarList) {
                    //Если варианты соединения совпали
                    if (joinvarRec.getInt(eJoinvar.types) == elemJoin.type.id) {

                        //ФИЛЬТР вариантов  
                        if (joiningVar.filter(elemJoin, joinvarRec) == true) {
                            List<Record> joindetList = eJoindet.find(joinvarRec.getInt(eJoinvar.id));

                            //Цикл по детализации соединений
                            for (Record joindetRec : joindetList) {
                                HashMap<Integer, String> mapParam = new HashMap(); //тут накапливаются параметры

                                //ФИЛЬТР детализации 
                                if (joiningDet.filter(mapParam, elemJoin, joindetRec, joinvarRec) == true) {
                                    Record artiklRec = eArtikl.find(joindetRec.getInt(eJoindet.artikl_id), false);
                                    Specific spcAdd = new Specific(joindetRec, artiklRec, joinElem1, mapParam);
                                    if (Color.colorFromProduct(spcAdd, 1)
                                            && Color.colorFromProduct(spcAdd, 2)
                                            && Color.colorFromProduct(spcAdd, 3)) {

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
}
