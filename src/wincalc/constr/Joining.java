package wincalc.constr;

import wincalc.Wincalc;

public class Joining extends Cal5e {
    
    public Joining(Wincalc iwin) {
        super(iwin);
    } 
    
//    public void joiningFirst() {
//
//        HashMap<String, ElemJoinig> hmJoinElem = root.getHmJoinElem(); //список соединений
//        //Цикл по списку соединений
//        for (Map.Entry<String, ElemJoinig> hmElemJoin : hmJoinElem.entrySet()) {
//
//            ElemJoinig elemJoin = hmElemJoin.getValue();
//            ElemBase joinElement1 = elemJoin.getJoinElement(1);
//            ElemBase joinElement2 = elemJoin.getJoinElement(2);
//
//            Connlst connlstRec = Connlst.find(constr, joinElement1.getArticlesRec().anumb, joinElement2.getArticlesRec().anumb);
//            ArrayList<Connvar> connvarList = Connvar.find(constr, connlstRec.cconn);
//
//            if (connvarList.isEmpty() && connlstRec.cequv.isEmpty() == false) {  //если неудача, ищем в аналоге
//                connlstRec = Connlst.find(constr, connlstRec.cequv);
//                connvarList = Connvar.find(constr, connlstRec.cconn);
//            }
//            Collections.sort(connvarList, (connvar1, connvar2) -> connvar1.cprio - connvar2.cprio);
//            //Цикл по вариантам соединения
//            for (Connvar connvar : connvarList) {
//
//                if (connvar.ctype != elemJoin.getVarJoin().value) continue; //если варианты соединения не совпали
//                ArrayList<ITParam> parconvList = Parconv.find(constr, connvar.cunic);
//                boolean out = paramVariant.checkParconv(elemJoin, parconvList); //ФИЛЬТР вариантов
//                if (out == false) continue;
//                ArrayList<Connspc> connspcList = Connspc.find(constr, connvar.cunic);
//                //Цикл по спецификации соединений
//                for (Connspc connspc : connspcList) {
//
//                    HashMap<Integer, String> hmParam2 = new HashMap(); //тут накапливаются параметры
//                    ArrayList<ITParam> parconsList = Parcons.find(constr, connspc.aunic);
//                    out = paramSpecific.checkSpecific(hmParam2, joinElement1, parconsList); //ФИЛЬТР спецификаций
//                    if (out == true) {
//
//                        Artikls artRec = Artikls.get(constr, connspc.anumb, false);
//                        //if(artRec.anumb.equals("V132P")) System.out.println("ТЕСТОВАЯ ЗАПЛАТКА");
//                        Specification specif = new Specification(artRec, joinElement1, hmParam2);
//                        //specif.setColor(this, joinElement1, connspc);
//                        //TODO Непонятное назначение цвета, надо разобратьца.
//                        Artsvst artsvst = Artsvst.get2(constr, artRec.anumb);
//                        specif.colorBase = artsvst.clcod;
//                        specif.colorInternal = artsvst.clcod;
//                        specif.colorExternal = artsvst.clcod;
//                        specif.element = "СОЕД";
//                        joinElement1.addSpecifSubelem(specif);
//                    }
//                }
//            }
//        }
//    }    
}
