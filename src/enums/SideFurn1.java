/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enums;

import static enums.SideProfile.values;

//Стороны фурнитуры
public enum SideFurn1 implements Enam {
    P1("1", "Нижняя"),
    P2("2", "Правая"),
    P3("3", "Верхняя"),
    P4("4", "Левая");

    public String id;
    public String name;

    SideFurn1(String id, String name) {
        this.id = id;
        this.name = name;
    }
}