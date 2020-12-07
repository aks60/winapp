package estimate.constr;

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
import estimate.Wincalc;
import estimate.constr.param.ElementDet;
import estimate.constr.param.JoiningDet;
import estimate.constr.param.JoiningVar;
import estimate.model.ElemJoining;
import estimate.model.ElemSimple;

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
        listVariants.clear();
        try {
            //Цикл по списку соединений
            for (Map.Entry<String, ElemJoining> hmElemJoin : iwin().mapJoin.entrySet()) {
                ElemJoining elemJoin = hmElemJoin.getValue();
                ElemSimple joinElem1 = elemJoin.joinElement1;
                ElemSimple joinElem2 = elemJoin.joinElement2;
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
                    if (joinvarRec.getInt(eJoinvar.types) == elemJoin.typeJoin.id) {
                        List<Record> joinpar1List = eJoinpar1.find(joinvarRec.getInt(eJoinvar.id));

                        //ФИЛЬТР вариантов  
                        if (joiningVar.check(elemJoin, joinpar1List) == true) {
                            List<Record> joindetList = eJoindet.find(joinvarRec.getInt(eJoinvar.id));

                            //Цикл по детализации соединений
                            for (Record joindetRec : joindetList) {
                                HashMap<Integer, String> mapParam = new HashMap(); //тут накапливаются параметры
                                List<Record> joinpar2List = eJoinpar2.find(joindetRec.getInt(eJoindet.id));

                                //ФИЛЬТР детализации 
                                if (joiningDet.check(mapParam, joinElem1, joinpar2List) == true) {
                                    Record artiklRec = eArtikl.find(joindetRec.getInt(eJoindet.artikl_id), false);
                                    Map<Integer, Integer> map = Color.colorFromProduct(joinElem1, artiklRec, joindetRec);
                                    if (map != null) {

                                        Specification specif = new Specification(joindetRec, artiklRec, joinElem1, mapParam);
                                        specif.setColor(map.get(1), map.get(2), map.get(3));
                                        specif.place = "СОЕД";
                                        joinElem1.addSpecific(specif);
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("estimate.constr.Joining.calc() " + e);
        }
    }
}
