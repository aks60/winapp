package enums;

public enum eParamJson {

    //paramJson("Уник. идентиф."),
    funic("Уник. идентиф."),
    typeOpen("Тип открывания"),
    colorHandl("Цвет ручки створки"),
    pro4Params("Параметры iokna"),
    pro4Params2("Параметры iokna"),
    nunic_iwin("Уникальный идентификатор артикула");
    String name = "";

    eParamJson(String name) {
        this.name = name;
    }

}
