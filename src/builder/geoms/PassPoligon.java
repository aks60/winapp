package builder.geoms;

import java.awt.Point;
import java.util.Arrays;

public class PassPoligon {

    //Квадрат расстояния между двумя точками
    public static long dist2(Point a1, Point a2) {
        return (long) (a2.x - a1.x) * (a2.x - a1.x)
                + (long) (a2.y - a1.y) * (a2.y - a1.y);
    }

    //"Косое" произведение
    public static long cross(Point a1, Point a2, Point b1, Point b2) {
        return (long) (a2.x - a1.x) * (b2.y - b1.y) - (long) (b2.x - b1.x) * (a2.y - a1.y);
    }

    //Алгоритм Джарвиса
    //https://informatics.msk.ru/mod/book/view.php?id=11835
    public static Point[] passJarvis(Point a[]) {

        int m = 0;
        for (int i = 1; i < a.length; i++) {
            if (a[i].x > a[m].x) {
                m = i;
            } else if (a[i].x == a[m].x && a[i].y < a[m].y) {
                m = i;
            }
        }

        Point p[] = new Point[a.length + 1];

        p[0] = a[m];
        a[m] = a[0];
        a[0] = p[0];

        int k = 0;
        int min = 0;

        do {
            for (int j = 1; j < a.length; j++) {
                if (cross(p[k], a[min], p[k], a[j]) < 0
                        || cross(p[k], a[min], p[k], a[j]) == 0
                        && dist2(p[k], a[min]) < dist2(p[k], a[j])) {
                    min = j;
                }
            }
            k++;
            p[k] = a[min];
            min = 0;
        } while (!(p[k].x == p[0].x && p[k].y == p[0].y));

        return Arrays.copyOf(p, k);
    }

//Алгоритм Грэхэма
//https://informatics.msk.ru/mod/book/view.php?id=11835    
    static Point[] passGraham(Point a[]) {

        int m = 0;
        for (int i = 1; i < a.length; i++) {
            if (a[i].x < a[m].x) {
                m = i;
            } else if (a[i].x == a[m].x && a[i].y < a[m].y) {
                m = i;
            }
        }

        Point p[] = new Point[a.length];

        p[0] = a[m];
        a[m] = a[1];
        a[1] = p[0];
        int k = 0;

        for (int i = 1; i < a.length - 1; i++) {
            for (int j = 1; j < a.length - 1; i++) {
                if (cross(a[1], a[j], a[1], a[j + 1]) < 0
                        || cross(a[1], a[j], a[1], a[j + 1]) == 0
                        && dist2(a[1], a[j]) > dist2(a[1], a[j + 1])) {
                    Point t = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = t;
                }
            }
        }

        p[1] = a[1];
        p[2] = a[2];
        k = 2;
        for (int i = 3; i < a.length; i++) {
            while (cross(p[k - 1], p[k], p[k], a[i]) <= 0) {
                k--;
            }
            k = k + 1;
            p[k] = a[i];
        }
        return Arrays.copyOf(p, k);
    }
}
