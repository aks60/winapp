package builder.model;

import dataset.Record;
import enums.LayoutJoin;
import enums.TypeJoin;
import builder.Wincalc;
import builder.making.Specific;
import dataset.Query;
import domain.eArtikl;
import domain.eJoining;
import domain.eJoinvar;
import java.util.Arrays;

public class ElemJoining {

    public float id = -1; //идентификатор соединения
    private Wincalc iwin;
    public Record joiningRec = eJoining.up.newRecord();
    public Record joinvarRec = eJoinvar.up.newRecord();
    public LayoutJoin layout = LayoutJoin.NONE; //расположение соединения 
    public TypeJoin type = TypeJoin.EMPTY;      //тип соединения (то что пишет )
    public int vid = 0; //вид соединения ("0-Простое L-обр", "1-Крестовое †-обр") или ("0-Простое T-обр", "1-Крестовое †-обр", "2-Сложное Y-обр)

    public ElemSimple elem1 = null;  //элемент соединения 1
    public ElemSimple elem2 = null;  //элемент соединения 2

    public float angl = 90;    //угол между профилями
    public String costs = "";  //трудозатраты, ч/ч.

    public static void create(String point, Wincalc iwin, TypeJoin type, LayoutJoin layout, ElemSimple elem1, ElemSimple elem2, float angl) {
        if (elem1 != null && elem2 != null) {
            iwin.mapJoin.put(point, new ElemJoining(iwin, type, layout, elem1, elem2, angl));
        } 
//        else {
//            System.err.println("Неудача:model.ElemJoining.create(). Соединение " + layout + "  не обработано.");
//        }
    }

    public ElemJoining(Wincalc iwin, TypeJoin type, LayoutJoin layout, ElemSimple elem1, ElemSimple elem2, float angl) {
        this.id = ++iwin.genId;
        this.iwin = iwin;
        this.type = type;
        this.layout = layout;
        this.elem1 = elem1;
        this.elem2 = elem2;
        this.angl = angl;
    }

    public void init(TypeJoin type, LayoutJoin layoutJoin, ElemSimple joinElement1, ElemSimple joinElement2) {
        this.type = type;
        this.layout = layoutJoin;
        this.elem1 = joinElement1;
        this.elem2 = joinElement2;
    }

    public void addSpecific(Specific spcAdd) { //добавление спесификаций зависимых элементов
        UMod uti3 = elem1.uti3;
        Specific spcRec = elem1.spcRec;

        spcAdd.count = uti3.get_11030_12060_14030_15040_25060_33030_34060_38030_39060(spcRec, spcAdd); //кол. ед. с учётом парам. 
        spcAdd.count += uti3.get_11050(spcAdd, this); //кол. ед. с шагом
        spcAdd.width = uti3.get_12050_15050_34051_39020(spcRec, spcAdd); //поправка мм
        if (Arrays.asList(1, 3, 5).contains(spcAdd.artiklRec.getInt(eArtikl.level1))) {
            spcAdd.width += spcRec.width;
        }
        uti3.get_12075_34075_39075(elem1, spcAdd); //углы реза
        spcAdd.width = uti3.get_12065_15045_25040_34070_39070(spcRec, spcAdd); //длина мм       
        spcAdd.width = spcAdd.width * uti3.get_12030_15030_25035_34030_39030(spcRec, spcAdd);//"[ * коэф-т ]"
        spcAdd.width = spcAdd.width / uti3.get_12040_15031_25036_34040_39040(spcRec, spcAdd);//"[ / коэф-т ]"
        spcAdd.count = uti3.get_11070_12070_33078_34078(spcAdd); //ставить однократно

        elem1.spcRec.spcList.add(spcAdd);
    }

    public String elemsName() {
        String name1 = eArtikl.query().stream().filter(rec -> rec.getInt(eArtikl.id) == elem1.artiklRecAn.getInt(eArtikl.id)).findFirst().orElse(eArtikl.up.newRecord()).getStr(eArtikl.code);
        String name2 = eArtikl.query().stream().filter(rec -> rec.getInt(eArtikl.id) == elem2.artiklRecAn.getInt(eArtikl.id)).findFirst().orElse(eArtikl.up.newRecord()).getStr(eArtikl.code);
        return name1 + "-" + name2;
    }

    public boolean equals(Object obj) {
        return id == ((Com5t) obj).id();
    }

    public String toString() {
        return "id=" + id + ", type=" + type + " (" + elem1.spcRec.artikl + ":" + elem2.spcRec.artikl + "), " + layout.name;
    }
}
