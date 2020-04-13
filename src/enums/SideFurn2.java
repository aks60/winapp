/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enums;

import static enums.SideProfile.values;
import static enums.TypeOpen.values;

//Ограничение сторон, назначение стороны фурнитуры
public enum SideFurn2 implements Enam {
    P1(1, "сторона"),
    P2(2, "ось поворота"),
    P3(3 , "крепление петель");

    public int id;
    public String name;

    SideFurn2(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int numb() {
        return Integer.valueOf(id);
    }

    public String text() {
        return name;
    }
    
    public Enam[] fields() {
        return values();
    }
}
