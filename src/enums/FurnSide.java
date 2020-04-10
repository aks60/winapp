/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enums;

import static enums.ProfileSide.values;

//Ограничение сторон, назначение стороны фурнитуры
public enum FurnSide {
    P1("1", "сторона"),
    P2("2", "ось поворота"),
    P3("3" , "крепление петель");

    public String id;
    public String name;

    FurnSide(String id, String name) {
        this.id = id;
        this.name = name;
    }

}
