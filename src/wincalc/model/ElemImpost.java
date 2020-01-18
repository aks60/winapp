package wincalc.model;

import dataset.Record;
import domain.eArtikl;
import domain.eSysprof;
import enums.LayoutArea;
import enums.ProfileSide;
import enums.TypeElem;
import enums.TypeProfile;
import wincalc.Wincalc;
import wincalc.constr.Specification;

public class ElemImpost extends ElemComponent {

    protected float truncation = 0; //усечение параметр Артикула1/Артикула2, мм
    protected float anglCut1 = 90; //угол реза импоста
    protected float anglCut2 = 90; //угол реза импоста

    public ElemImpost(String id) {
        super(id);
    }

    public ElemImpost(Wincalc iwin, AreaContainer owner, String id) {

        super(id);
        this.side = (owner.layout() == LayoutArea.HORIZONTAL) ? LayoutArea.VERTICAL : LayoutArea.HORIZONTAL;
        color1 = iwin.color1;
        color2 = iwin.color2;
        color3 = iwin.color3;
        initСonstructiv();

        //Коррекция положения импоста арки
        if ((TypeElem.ARCH == owner.typeElem() || TypeElem.TRAPEZE == owner.typeElem()) && owner.listChild().isEmpty()) {

                float dh = articlRec.getFloat(eArtikl.height) / 2;     
                owner.addElem(new AreaScene(iwin, owner, genId(), LayoutArea.HORIZONTAL, owner.width, dh));
        }
        //Установка координат
        for (int index = owner.listChild().size() - 1; index >= 0; --index) {
            if (owner.listChild().get(index) instanceof AreaContainer) {
                Common prevArea = owner.listChild().get(index); //index указывает на предыдущий элемент
                float dx = articlRec.getFloat(eArtikl.size_centr);

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
        articlRec = eArtikl.query.select().stream().filter(rec -> rec.getInt(eArtikl.id) == sysprofRec.getInt(eSysprof.artikl_id)).findFirst().orElse(null);
        if (articlRec.get(eArtikl.analog) != null && articlRec.getStr(eArtikl.analog).isEmpty() == false) {
            articlRec = eArtikl.query.select().stream().filter(rec -> rec.getStr(eArtikl.code).equals(articlRec.getStr(eArtikl.analog))).findFirst().orElse(null);
        }
    }

    public void setSpecifElement(Record sysproaRec) {
   /*
        indexUniq(specificationRec);
        specificationRec.element = (LayoutArea.HORIZONTAL == owner.getLayout()) ? LayoutArea.VERTICAL.name : LayoutArea.HORIZONTAL.name;
        specificationRec.setArticlRec(Artikls.get(getConst(), sysproaRec.anumb, false));
        specificationRec.colorBase = colorBase;
        specificationRec.colorInternal = colorInternal;
        specificationRec.colorExternal = colorExternal;
        specificationRec.discount = 0;
        specificationRec.anglHoriz = anglHoriz;

        if (LayoutArea.HORIZONTAL == owner.getLayout()) { //слева направо
            ElemBase elemPrior = prevElem();

            //Элемент слева
            String keyJoinLeft = String.valueOf(elemPrior.x2) + ":" + String.valueOf(elemPrior.y1);
            ElemJoinig joinLeft = getRoot().getHmJoinElem().get(keyJoinLeft);
            ElemBase elemLeft = (joinLeft.joinElement1.equals(this)) ? joinLeft.joinElement2 : joinLeft.joinElement1;
            y1 = elemLeft.y2;
            //элемент справа
            String keyJoinRight = String.valueOf(elemPrior.x2) + ":" + String.valueOf(elemPrior.y2);
            ElemJoinig joinRight = getRoot().getHmJoinElem().get(keyJoinRight);
            ElemBase elemRight = (joinRight.joinElement1.equals(this)) ? joinRight.joinElement2 : joinRight.joinElement1;
            y2 = elemRight.y1;

            specificationRec.width = y2 - y1 + getRoot().iwin.syssizeRec.ssizi * 2 + elemLeft.articlesRec.asizn + elemRight.articlesRec.asizn;;
            specificationRec.height = articlesRec.aheig;

        } else if (LayoutArea.VERTICAL == owner.getLayout()) { //сверху вниз
            ElemBase elemPrior = prevElem();

            //Элемент слева
            String keyJoinLeft = String.valueOf(elemPrior.x1) + ":" + String.valueOf(elemPrior.y2);
            ElemJoinig joinLeft = getRoot().getHmJoinElem().get(keyJoinLeft);
            ElemBase elemLeft = (joinLeft.joinElement1.equals(this)) ? joinLeft.joinElement2 : joinLeft.joinElement1;
            x1 = elemLeft.x2;
            //Элемент справа
            String keyJoinRight = String.valueOf(elemPrior.x2) + ":" + String.valueOf(elemPrior.y2);
            ElemJoinig joinRight = getRoot().getHmJoinElem().get(keyJoinRight);
            ElemBase elemRight = (joinRight.joinElement1.equals(this)) ? joinRight.joinElement2 : joinRight.joinElement1;
            x2 = elemRight.x1;

            specificationRec.width = x2 - x1 + getRoot().iwin.syssizeRec.ssizi * 2 + elemLeft.articlesRec.asizn + elemRight.articlesRec.asizn;
            specificationRec.height = articlesRec.aheig;
        }
        specificationRec.anglCut2 = 90;
        specificationRec.anglCut1 = 90;
*/
    }
    
    /**
     * Добавление спесификаций зависимых элементов
     */    
    @Override
    public void addSpecifSubelem(Specification specif) {
/*
        indexUniq(specif);
        Artikls specifArtikl = specif.getArticRec();

        //Импост (если элемент включен в список состава)
        if (TypeArtikl.IMPOST.value2 == specifArtikl.atypp && specifArtikl.atypm == 1) {
            specificationRec.setArticlRec(specif.getArticRec()); //= (specif.artikl.equals("-")) ? specif.artikl : "-";
            specificationRec.name = (specif.name.equals("-")) ? specif.name : "-";
            specificationRec.setArticlRec(specif.getArticRec());
            return;

            //Теперь армирование
        } else if (TypeArtikl.ARMIROVANIE.value2 == specifArtikl.atypp && specifArtikl.atypm == 1) {
            specif.element = side.name;

            //if (LayoutArea.HORIZONTAL == side) specif.width = owner.x2 - owner.x1;
            //else if(LayoutArea.VERTICAL == side) specif.width = owner.y2 - owner.y1;

            specif.width = specificationRec.width;
            specif.anglCut2 = 90;
            specif.anglCut1 = 90;

            //Соединитель
        } else if(TypeArtikl.SOEDINITEL.isType(specifArtikl) == true) {

            //specif.colorBase = getRoot().getIwin().getColorNone();
            //specif.colorInternal = getRoot().getIwin().getColorNone();
            //specif.colorExternal = getRoot().getIwin().getColorNone();

            //Остальные
        } else {
            //specif.element = "ЗАП";
        }

        quantityMaterials(specif);
        specificationRec.getSpecificationList().add(specif);
        */
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
