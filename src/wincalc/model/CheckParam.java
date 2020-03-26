package wincalc.model;

import common.ParamCheck;

public class CheckParam {

    public static ParamCheck check0000 = (Object... obj) -> {
        System.out.println("wincalc.model.CheckParam.methodName()");
        return false;
    };
}
