package wincalc.model;

import domain.eArtikls;
import domain.eSysprof;
import enums.LayoutArea;
import enums.ProfileSide;
import enums.TypeElem;
import enums.TypeProfile;
import wincalc.Wincalc;

public class ElemImpost extends ElemBase {

    private LayoutArea side = LayoutArea.NONE;
    protected float truncation = 0; //усечение параметр Артикула1/Артикула2, мм
    protected float anglCut1 = 90; //угол реза импоста
    protected float anglCut2 = 90; //угол реза импоста

    public ElemImpost(String id) {
        super(id);
    }

    public ElemImpost(Wincalc iwin, AreaBase owner, String id) {

        super(id);
        this.side = (owner.layout() == LayoutArea.HORIZONTAL) ? LayoutArea.VERTICAL : LayoutArea.HORIZONTAL;
        color1 = iwin.color1;
        color2 = iwin.color2;
        color3 = iwin.color3;
        initСonstructiv();

        //Коррекция положения импоста арки
        if ((TypeElem.ARCH == owner.typeElem() || TypeElem.TRAPEZE == owner.typeElem()) && owner.listChild().isEmpty()) {

                float dh = articlRec.getFloat(eArtikls.height) / 2;     
                owner.addElem(new AreaScene(iwin, owner, genId(), LayoutArea.HORIZONTAL, owner.width, dh));
        }
        //Установка координат
        for (int index = owner.listChild().size() - 1; index >= 0; --index) {
            if (owner.listChild().get(index) instanceof AreaBase) {
                Base prevArea = owner.listChild().get(index); //index указывает на предыдущий элемент
                float dx = articlRec.getFloat(eArtikls.size_centr);

                if (LayoutArea.VERTICAL.equals(owner.layout())) { //сверху вниз
                    dimension(owner.x1, prevArea.y2 - dx, prevArea.x2, prevArea.y2 + dx);
                    anglHoriz = 0;

                } else if (LayoutArea.HORIZONTAL.equals(owner.layout())) { //слева направо
                    dimension(prevArea.x2 - dx, prevArea.y1, prevArea.x2 + dx, prevArea.y2);
                    anglHoriz = 90;
                }
                break;
            }
        }
    }

    public void initСonstructiv() {
        if (LayoutArea.VERTICAL.equals(owner.layout())) { //сверху вниз
           sysprofRec = eSysprof.query.stream().filter(rec -> rec.getInt(eSysprof.systree_id) == iwin.nuni
           && TypeProfile.IMPOST.value == rec.getInt(eSysprof.types)
           && ProfileSide.Horiz.value == rec.getInt(eSysprof.side)).findFirst().orElse(null);
           
        } else if (LayoutArea.HORIZONTAL.equals(owner.layout())) { //слева направо
           sysprofRec = eSysprof.query.stream().filter(rec -> rec.getInt(eSysprof.systree_id) == iwin.nuni
           && TypeProfile.IMPOST.value == rec.getInt(eSysprof.types)
           && ProfileSide.Vert.value == rec.getInt(eSysprof.side)).findFirst().orElse(null);
        }
        articlRec = eArtikls.query.select().stream().filter(rec -> rec.getInt(eArtikls.id) == sysprofRec.getInt(eSysprof.artikl_id)).findFirst().orElse(null);
        if (articlRec.get(eArtikls.analog) != null && articlRec.getStr(eArtikls.analog).isEmpty() == false) {
            articlRec = eArtikls.query.select().stream().filter(rec -> rec.getStr(eArtikls.code).equals(articlRec.getStr(eArtikls.analog))).findFirst().orElse(null);
        }
    }

    @Override
    public TypeElem typeElem() {
        return TypeElem.IMPOST;
    }

    @Override
    public TypeProfile typeProfile() {
        return TypeProfile.IMPOST;
    }

    @Override
    public void anglCut(int side, float anglCut) {
        if (side == 1) {
            anglCut1 = anglCut;
        } else {
            anglCut2 = anglCut;
        }
    }
}
