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
    
    public void build() {
        
        HashMap<String, ElemJoining> hmJoinElem = iwin().mapJoin; //список соединений
        //Цикл по списку соединений
        for (Map.Entry<String, ElemJoining> hmElemJoin : iwin().mapJoin.entrySet()) {
            
            ElemJoining elemJoin = hmElemJoin.getValue();
            ElemSimple joinElem1 = elemJoin.joinElement1;
            ElemSimple joinElem2 = elemJoin.joinElement2;
            
            int id1 = (joinElem1.artiklRec.get(eArtikl.analog_id) == null) ? joinElem1.artiklRec.getInt(eArtikl.id) : joinElem1.artiklRec.getInt(eArtikl.analog_id);
            int id2 = (joinElem2.artiklRec.get(eArtikl.analog_id) == null) ? joinElem2.artiklRec.getInt(eArtikl.id) : joinElem2.artiklRec.getInt(eArtikl.analog_id);
            Record joiningRec = eJoining.find(id1, id2);
            List<Record> joinvarList = eJoinvar.find(joiningRec.getInt(eJoining.id));
            
            if (joinvarList.isEmpty() == true && joiningRec.getStr(eJoining.analog).isEmpty() == false) {  //если неудача, ищем в аналоге
                joiningRec = eJoining.find2(joiningRec.getStr(eJoining.analog));
                joinvarList = eJoinvar.find(joiningRec.getInt(eJoining.id));
            }
            Collections.sort(joinvarList, (connvar1, connvar2) -> connvar1.getInt(eJoinvar.prio) - connvar2.getInt(eJoinvar.prio));
            //Цикл по вариантам соединения
            for (Record joinvarRec : joinvarList) {
                if (joinvarRec.getInt(eJoinvar.types) != elemJoin.typeJoin.value) {
                    continue; //если варианты соединения не совпали
                }
                if (joiningRec.getInt(1) == 1434) {
                    System.out.println("ТЕСТОВАЯ ЗАПЛАТКА");
                }                
                List<Record> joinpar1List = eJoinpar1.find(joinvarRec.getInt(eJoinvar.id));
                boolean out = joiningVar.check(elemJoin, joinpar1List); //ФИЛЬТР вариантов
                if (out == false) {
                    continue;
                }
                List<Record> joindetList = eJoindet.find(joinvarRec.getInt(eJoinvar.id));
                //Цикл по детализации соединений
                for (Record joindetRec : joindetList) {
                    
                    HashMap<Integer, String> hmParam2 = new HashMap(); //тут накапливаются параметры
                    List<Record> joinpar2List = eJoinpar2.find(joindetRec.getInt(eJoindet.id));
                    out = elementDet.check(hmParam2, joinElem1, joinpar2List); //ФИЛЬТР детализации
                    if (out == true) {
                        
                        Record artiklRec = eArtikl.find(joindetRec.getInt(eJoindet.artikl_id), false);
                        //if(artRec.anumb.equals("V132P")) System.out.println("ТЕСТОВАЯ ЗАПЛАТКА");
                        Specification specif = new Specification(artiklRec, joinElem1, hmParam2);
                        //specif.setColor(this, joinElement1, connspc);
                        //TODO Непонятное назначение цвета, надо разобратьца.
                        Record artdetRec = eArtdet.find2(artiklRec.getInt(eArtikl.id));
                        specif.color1 = artdetRec.getInt(eArtdet.color_fk);
                        specif.color2 = artdetRec.getInt(eArtdet.color_fk);
                        specif.color3 = artdetRec.getInt(eArtdet.color_fk);
                        specif.place = "СОЕД";
                        joinElem1.addSpecific(specif);
                    }
                }
            }
        }
    }
}
