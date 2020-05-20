package calculate.constr;

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
import calculate.Wincalc;
import calculate.constr.param.ElementDet;
import calculate.constr.param.JoiningDet;
import calculate.constr.param.JoiningVar;
import calculate.model.ElemJoining;
import calculate.model.ElemSimple;

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
            ElemSimple joinElement1 = elemJoin.joinElement1;
            ElemSimple joinElement2 = elemJoin.joinElement2;

            Record joiningRec = eJoining.find(joinElement1.artiklRec.getInt(eArtikl.id), joinElement2.artiklRec.getInt(eArtikl.id));
            List<Record> joinvarList = eJoinvar.find(joiningRec.getInt(eJoining.id));

            if (joinvarList.isEmpty() && joiningRec.getStr(eJoining.analog).isEmpty() == false) {  //если неудача, ищем в аналоге
                joiningRec = eJoining.find2(joiningRec.getStr(eJoining.analog));
                joinvarList = eJoinvar.find2(joiningRec.getInt(eJoining.id));
            }
            Collections.sort(joinvarList, (connvar1, connvar2) -> connvar1.getInt(eJoinvar.prio) - connvar2.getInt(eJoinvar.prio));
            //Цикл по вариантам соединения
            for (Record joinvarRec : joinvarList) {

                if (joinvarRec.getInt(eJoinvar.types) != elemJoin.varJoin.value) {
                    continue; //если варианты соединения не совпали
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
                    out = elementDet.check(hmParam2, joinElement1, joinpar2List); //ФИЛЬТР детализации
                    if (out == true) {

                        Record artiklRec = eArtikl.find(joindetRec.getInt(eJoindet.artikl_id), false);
                        //if(artRec.anumb.equals("V132P")) System.out.println("ТЕСТОВАЯ ЗАПЛАТКА");
                        Specification specif = new Specification(artiklRec, joinElement1, hmParam2);
                        //specif.setColor(this, joinElement1, connspc);
                        //TODO Непонятное назначение цвета, надо разобратьца.
                        Record artdetRec = eArtdet.find2(artiklRec.getInt(eArtikl.id));
                        specif.color1 = artdetRec.getInt(eArtdet.color_fk);
                        specif.color2 = artdetRec.getInt(eArtdet.color_fk);
                        specif.color3 = artdetRec.getInt(eArtdet.color_fk);
                        specif.section = "СОЕД";
                        joinElement1.addSpecification(specif);
                    }
                }
            }
        }
    }
}
