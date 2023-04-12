package builder.geoms;

import builder.IElem5e;
import common.UCom;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

//
//https://www.geeksforgeeks.org/line-clipping-set-2-cyrus-beck-algorithm/
//
public class UGeo {

    //Скалярное произведение
    public static int dot(Point2D p0, Point2D p1) {
        return (int) (p0.getX() * p1.getX() + p0.getY() * p1.getY());
    }

    //Вычисления максимума из вектора с плавающей запятой
    private static double max(List<Double> t) {
        double maximum = 0;
        for (int i = 0; i < t.size(); i++) {
            if (t.get(i) > maximum) {
                maximum = t.get(i);
            }
        }
        return maximum;
    }

    //Вычисления минимума из вектора с плавающей запятой
    private static double min(List<Double> t) {
        double minimum = 500;
        for (int i = 0; i < t.size(); i++) {
            if (t.get(i) < minimum) {
                minimum = t.get(i);
            }
        }
        return minimum;
    }

    //Функция Сайруса Бека возвращает пару значений, которые затем отображаются в виде строки
    public static Point2D[] cut(Point2D vertices[], Point2D line[], int n) {

        Point2D newPair[] = {new Point2D.Double(), new Point2D.Double()}; //значение временного держателя, которое будет возвращено        
        Point2D normal[] = new Point2D.Double[n]; //нормали инициализируются динамически (можно и статически, не имеет значения)

        //Расчет нормалей
        for (int i = 0; i < n; i++) {
            double y = vertices[(i + 1) % n].getX() - vertices[i].getX();
            double x = vertices[i].getY() - vertices[(i + 1) % n].getY();
            normal[i] = new Point2D.Double();
            normal[i].setLocation(x, y);
        }

        Point2D P1_P0 = new Point2D.Double((int) (line[1].getX() - line[0].getX()), (int) (line[1].getY() - line[0].getY())); //расчет P1 - P0       
        Point2D[] P0_PEi = new Point2D.Double[n];  //инициализация всех значений P0 - PEi

        //Вычисление значений P0 - PEi для всех ребер
        for (int i = 0; i < n; i++) {
            //Вычисление PEi - P0, чтобы знаменатель не умножался на -1
            double P0_PEx = vertices[i].getX() - line[0].getX();
            double P0_PEy = vertices[i].getY() - line[0].getY();
            P0_PEi[i] = new Point2D.Double();
            P0_PEi[i].setLocation(P0_PEx, P0_PEy); //при расчете t 
        }

        int numerator[] = new int[n], denominator[] = new int[n];
        //Вычисление числителя и знаменателя с помощью точечной функции
        for (int i = 0; i < n; i++) {
            numerator[i] = dot(normal[i], P0_PEi[i]);
            denominator[i] = dot(normal[i], P1_P0);
        }

        double t[] = new double[n]; //динамическая инициализация значений t
        //Создание двух векторов, называемых «не входящими» и 
        //«не выходящими», для группировки «t» в соответствии с их знаменателями
        LinkedList<Double> tE = new LinkedList(), tL = new LinkedList();

        // Вычисление 't' и их группировка соответственно
        for (int i = 0; i < n; i++) {
            t[i] = (double) (numerator[i]) / (double) (denominator[i]);
            if (denominator[i] > 0) {
                tE.add(t[i]);
            } else {
                tL.add(t[i]);
            }
        }
        double temp[] = new double[2]; //инициализация последних двух значений 't'      
        tE.add(0.0);  //берем максимум всех «tE» и 0, поэтому нажимаем 0
        temp[0] = max(tE);
        tL.add(1.0); //принимая минимальное значение всех «tL» и 1, поэтому нажмите 1
        temp[1] = min(tL);

        //Ввод значения 't' не может быть больше, чем выход значения 't', 
        //следовательно, это тот случай, когда линия полностью выходит за пределы
        if (temp[0] > temp[1]) {
            newPair[0] = new Point2D.Double(-1, -1);
            newPair[1] = new Point2D.Double(-1, -1);
            return newPair;
        }

        //Вычисление координат по x и y
        double newPair0x = line[0].getX() + P1_P0.getX() * temp[0];
        double newPair0y = line[0].getY() + P1_P0.getY() * temp[0];
        double newPair1x = line[0].getX() + P1_P0.getX() * temp[1];
        double newPair1y = line[0].getY() + P1_P0.getY() * temp[1];
        newPair[0].setLocation(newPair0x, newPair0y);
        newPair[1].setLocation(newPair1x, newPair1y);

        //System.out.println("(" + newPair[0].getX() + ", " + newPair[0].getY() + ") (" + newPair[1].getX() + ", " + newPair[1].getY() + ")");
        return newPair;
    }

    //Точка пересечения двух векторов 
    //https://habr.com/ru/articles/523440/ 
    public static double[] cross(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        double n;
        double dot[] = {0, 0};
        if (y2 - y1 != 0) {  // a(y)
            double q = (x2 - x1) / (y1 - y2);
            double sn = (x3 - x4) + (y3 - y4) * q;
            if (sn == 0) {
                return null;
            }  // c(x) + c(y)*q

            double fn = (x3 - x1) + (y3 - y1) * q;   // b(x) + b(y)*q
            n = fn / sn;
        } else {
            if ((y3 - y4) == 0) {
                return null;
            }  // b(y)

            n = (y3 - y1) / (y3 - y4);   // c(y)/b(y)
        }
        dot[0] = x3 + (x4 - x3) * n;  // x3 + (-b(x))*n
        dot[1] = y3 + (y4 - y3) * n;  // y3 +(-b(y))*n
        return dot;
    }

    //Точка пересечения двух векторов 
    public static double[] cross(IElem5e e1, IElem5e e2) {
        return cross(e1.x1(), e1.y1(), e1.x2(), e1.y2(), e2.x1(), e2.y1(), e2.x2(), e2.y2());
    }

    //Точка пересечения двух векторов 
    static Point cross(Point A, Point B, Point C, Point D) {

        // Line AB represented as a1x + b1y = c1
        double a1 = B.y - A.y;
        double b1 = A.x - B.x;
        double c1 = a1 * (A.x) + b1 * (A.y);

        // Line CD represented as a2x + b2y = c2
        double a2 = D.y - C.y;
        double b2 = C.x - D.x;
        double c2 = a2 * (C.x) + b2 * (C.y);

        double determinant = a1 * b2 - a2 * b1;

        if (determinant == 0) {
            // The lines are parallel. This is simplified
            // by returning a pair of FLT_MAX
            return new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
        } else {
            double x = (b2 * c1 - b1 * c2) / determinant;
            double y = (a1 * c2 - a2 * c1) / determinant;
            return new Point((int) x, (int) y);
        }
    }

    //Ширина рамки по оси x и y
    public static double[] diff(IElem5e e, double dh) {

        double x = -1 * cos(e.anglHoriz());
        double y = -1 * sin(e.anglHoriz());

        if (Math.abs(x) >= Math.abs(y)) {
            return new double[]{0, dh / x};
        } else {
            return new double[]{dh / y, 0};
        }
    }

    private static double min(double d) {
        for (int i = 0; i < 4; i++) {
            if (d > 90) {
                d = d - 90;
            }
        }
        return d;
    }

    public static double sin(double angl) {
        return Math.sin(Math.toRadians(angl));
    }
    
    public static double asin(double angl) {
        return Math.toDegrees(Math.asin(angl));
    }

    public static double cos(double angl) {
        return Math.cos(Math.toRadians(angl));
    }

    public static double tan(double angl) {
        return Math.tan(Math.toRadians(angl));
    }
        
    public static double acos(double angl) {
        return Math.toDegrees(Math.acos(angl));
    }

    public static double atan(double angl) {
        return Math.toDegrees(Math.atan(angl));
    }

    //http://ru.solverbook.com/spravochnik/vektory/ugol-mezhdu-vektorami/
    public static double betweenAngl(IElem5e e1, IElem5e e2) {

        double dx1 = e1.x2() - e1.x1();
        double dy1 = e1.y2() - e1.y1();
        double dx2 = e2.x2() - e2.x1();
        double dy2 = e2.y2() - e2.y1();

        double s = dx1 * dx2 + dy1 * dy2;
        double a = Math.sqrt(Math.pow(dx1, 2) + Math.pow(dy1, 2));
        double b = Math.sqrt(Math.pow(dx2, 2) + Math.pow(dy2, 2));
        double c = s / (a * b);
        return 180 - acos(c);
    }

    //https://www.onemathematicalcat.org/Math/Precalculus_obj/horizVertToDirMag.htm
    public static double horizontAngl(IElem5e e) {
        double x = e.x2() - e.x1();
        double y = e.y1() - e.y2();

        if (x > 0 && y == 0) {
            return 0;
        } else if (x < 0 && y == 0) {
            return 180;
        } else if (x == 0 && y > 0) {
            return 90;
        } else if (x == 0 & y < 0) {
            return 270;
        } else if (x > 0 && y > 0) {
            return atan(y / x);
        } else if (x < 0 && y > 0) {
            return 180 + atan(y / x);
        } else if (x < 0 && y < 0) {
            return 180 + atan(y / x);
        } else if (x > 0 && y < 0) {
            return 360 + atan(y / x);
        } else {
            return 0;
        }
    }
        
    //Пример PathIterator
    public static void testArea() {
        Area a = new Area(new Rectangle(1, 1, 5, 5));
        PathIterator iterator = a.getPathIterator(null);
        float[] floats = new float[6];
        Polygon polygon = new Polygon();
        while (!iterator.isDone()) {
            int type = iterator.currentSegment(floats);
            int x = (int) floats[0];
            int y = (int) floats[1];
            if (type != PathIterator.SEG_CLOSE) {
                polygon.addPoint(x, y);
                System.out.println("adding x = " + x + ", y = " + y);
            }
            iterator.next();
        }
    }
}
// <editor-fold defaultstate="collapsed" desc="Исходники"> 
// C++ Program to implement Cyrus Beck  
//#include <SFML/Graphics.hpp>
//#include <iostream>
//#include <utility>
//#include <vector> 
//        
//using namespace std;
//using namespace sf;
//  
//// Функция рисования линии в SFML
//void drawline(RenderWindow* window, pair<int, int> p0, pair<int, int> p1`) {
//    Vertex line[] = {
//        Vertex(Vector2f(p0.first, p0.second)),
//        Vertex(Vector2f(p1.first, p1.second))
//    };
//    window->draw(line, 2, Lines);
//}
//  
//// Функция для рисования многоугольника с заданными вершинами
//void drawPolygon(RenderWindow* window, pair<int, int> vertices[], int n) {
//    for (int i = 0; i < n - 1; i++) {
//        drawline(window, vertices[i], vertices[i + 1]);
//    }
//    drawline(window, vertices[0], vertices[n - 1]);
//}
//  
//// Функция для скалярного произведения
//int dot(pair<int, int> p0, pair<int, int> p1) {
//    return p0.first * p1.first + p0.second * p1.second;
//}
//  
//// Функция для вычисления максимума из вектора с плавающей запятой
//float max(vector<float> t) {
//    float maximum = INT_MIN;
//    for (int i = 0; i < t.size(); i++) {
//        if (t[i] > maximum) {
//            maximum = t[i];
//	    }		
//	}
//    return maximum;
//}
//  
//// Функция для вычисления минимума из вектора с плавающей запятой
//float min(vector<float> t) {
//    float minimum = INT_MAX;
//    for (int i = 0; i < t.size(); i++) {
//        if (t[i] < minimum) {
//            minimum = t[i];
//	    }
//    }		
//    return minimum;
//}
//  
////Функция Сайруса Бека возвращает пару значений, которые затем отображаются в виде строки
//pair<int, int>* CyrusBeck(pair<int, int> vertices[], pair<int, int> line[], int n) {
//  
//    // Значение временного держателя, которое будет возвращено
//    pair<int, int>* newPair = new pair<int, int>[2];
//  
//    // Нормали инициализируются динамически (можно и статически, не имеет значения)
//    pair<int, int>* normal = new pair<int, int>[n];
//  
//    // Расчет нормалей
//    for (int i = 0; i < n; i++) {
//        normal[i].second = vertices[(i + 1) % n].first - vertices[i].first;
//        normal[i].first = vertices[i].second - vertices[(i + 1) % n].second;
//    }
//  
//    // Расчет P1 - P0
//    pair<int, int> P1_P0 = make_pair(line[1].first - line[0].first, line[1].second - line[0].second);
//  
//    // Инициализация всех значений P0 - PEi
//    pair<int, int>* P0_PEi = new pair<int, int>[n];
//  
//    // Вычисление значений P0 - PEi для всех ребер
//    for (int i = 0; i < n; i++) {
//		
//        // Вычисление PEi - P0, чтобы знаменатель не умножался на -1
//        P0_PEi[i].first = vertices[i].first - line[0].first; 
//		
//        // при расчете t
//        P0_PEi[i].second = vertices[i].second - line[0].second;
//    }
//  
//    int *numerator = new int[n], *denominator = new int[n];
//  
//    // Вычисление числителя и знаменателя с помощью точечной функции
//    for (int i = 0; i < n; i++) {
//        numerator[i] = dot(normal[i], P0_PEi[i]);
//        denominator[i] = dot(normal[i], P1_P0);
//    }
//  
//    // Initializing the 't' values dynamically
//    float* t = new float[n];
//  
//    // Создание двух векторов, называемых «не входящими» и «не выходящими», для группировки «t» в соответствии с их знаменателями
//    vector<float> tE, tL;
//  
//    // Вычисление 't' и их группировка соответственно
//    for (int i = 0; i < n; i++) {
//  
//        t[i] = (float)(numerator[i]) / (float)(denominator[i]);
//  
//        if (denominator[i] > 0)   tE.push_back(t[i]);
//        else                      tL.push_back(t[i]);
//    }
//  
//    // Инициализация последних двух значений 't'
//    float temp[2];
//  
//    // Берем максимум всех «tE» и 0, поэтому нажимаем 0
//    tE.push_back(0.f);
//    temp[0] = max(tE);
//  
//    // Принимая минимальное значение всех «tL» и 1, поэтому нажмите 1
//    tL.push_back(1.f);
//    temp[1] = min(tL);
//  
//    // Ввод значения 't' не может быть больше, чем выход значения 't', следовательно, это тот случай, когда линия полностью выходит за пределы
//    if (temp[0] > temp[1]) {
//        newPair[0] = make_pair(-1, -1);
//        newPair[1] = make_pair(-1, -1);
//        return newPair;
//    }
//  
//    // Вычисление координат по x и y
//    newPair[0].first t  = (float)line[0].first + (float)P1_P0.first * (float)temp[0];
//    newPair[0].second = (float)line[0].second + (float)P1_P0.second * (float)temp[0];
//    newPair[1].first = (float)line[0].first + (float)P1_P0.first * (float)temp[1];
//    newPair[1].second = (float)line[0].second + (float)P1_P0.second * (float)temp[1];
//	
//    cout << '(' << newPair[0].first << ", " << newPair[0].second << ") (" << newPair[1].first << ", " << newPair[1].second << ")";
//  
//    return newPair;
//}
//  
//// Код драйвера
//int main() {
//  
//    // Настройка окна и цикла и вершин полигона и линии
//    RenderWindow window(VideoMode(500, 500), "Cyrus Beck");
//    pair<int, int> vertices[]  = { 
//	           make_pair(200, 50),  make_pair(250, 100),
//               make_pair(200, 150), make_pair(100, 150),  
//			   make_pair(50, 100),  make_pair(100, 50) 
//		}; 
//    // Убедитесь, что вершины расположены по часовой стрелке
//    int n = sizeof(vertices) / sizeof(vertices[0]);
//    pair<int, int> line[] = { make_pair(10, 10), make_pair(450, 200) };
//    pair<int, int>* temp1 = cyrusBeck(vertices, line, n);
//    pair<int, int> temp2[2];
//    temp2[0] = line[0];
//    temp2[1] = line[1];
//  
//    // Чтобы разрешить отсечение и отсечение линии простым нажатием клавиши
//    bool trigger = false;
//    while (window.isOpen()) {
//        window.clear();
//        Event event;
//        if (window.pollEvent(event)) {
//            if (event.type == Event::Closed)
//                window.close();
//            if (event.type == Event::KeyPressed)
//                trigger = !trigger;
//        }
//        drawPolygon(&window, vertices, n);
//  
//        // Использование значения триггера для обрезки и отсечения строки
//        if (trigger) {
//            line[0] = temp1[0];
//            line[1] = temp1[1];
//        }
//        else {
//            line[0] = temp2[0];
//            line[1] = temp2[1];
//        }
//        drawline(&window, line[0], line[1]);
//        window.display();
//    }
//    return 0;
// } 
//
// https://habr.com/ru/articles/267037/
//bool crossing(vector<T, 2> const &v11, vector<T, 2> const &v12, vector<T, 2> const &v21, vector<T, 2> const &v22, vector<T, 2> *crossing) {
//   vector<T, 3> cut1(v12-v11), cut2(v22-v21);
//   vector<T, 3> prod1, prod2;
// 
//   prod1 = cross(cut1 * (v21-v11));
//   prod2 = cross(cut1 * (v22-v11));
// 
//   if(sign(prod1[Z]) == sign(prod2[Z]) || (prod1[Z] == 0) || (prod2[Z] == 0)) // Отсекаем также и пограничные случаи
//     return false;
// 
//   prod1 = cross(cut2 * (v11-v21));
//   prod2 = cross(cut2 * (v12-v21));
// 
//   if(sign(prod1[Z]) == sign(prod2[Z]) || (prod1[Z] == 0) || (prod2[Z] == 0)) // Отсекаем также и пограничные случаи
//     return false;
// 
//   if(crossing) { // Проверяем, надо ли определять место пересечения
//     (*crossing)[X] = v11[X] + cut1[X]*fabs(prod1[Z])/fabs(prod2[Z]-prod1[Z]);
//     (*crossing)[Y] = v11[Y] + cut1[Y]*fabs(prod1[Z])/fabs(prod2[Z]-prod1[Z]);
//   }
// 
//   return true;
// }
// </editor-fold> 
