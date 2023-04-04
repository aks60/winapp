
package builder.geom;

//https://www.geeksforgeeks.org/line-clipping-set-2-cyrus-beck-algorithm/
public class CyruAndBeckAlgoritm {

/**
// C++ Program to implement Cyrus Beck  
#include <SFML/Graphics.hpp>
#include <iostream>
#include <utility>
#include <vector> 
        
using namespace std;
using namespace sf;
  
// Функция рисования линии в SFML
void drawline(RenderWindow* window, pair<int, int> p0, pair<int, int> p1) {
    Vertex line[] = {
        Vertex(Vector2f(p0.first, p0.second)),
        Vertex(Vector2f(p1.first, p1.second))
    };
    window->draw(line, 2, Lines);
}
  
// Функция для рисования многоугольника с заданными вершинами
void drawPolygon(RenderWindow* window, pair<int, int> vertices[], int n) {
    for (int i = 0; i < n - 1; i++)
        drawline(window, vertices[i], vertices[i + 1]);
    drawline(window, vertices[0], vertices[n - 1]);
}
  
// Функция для скалярного произведения
int dot(pair<int, int> p0, pair<int, int> p1) {
    return p0.first * p1.first + p0.second * p1.second;
}
  
// Функция для вычисления максимума из вектора с плавающей запятой
float max(vector<float> t) {
    float maximum = INT_MIN;
    for (int i = 0; i < t.size(); i++)
        if (t[i] > maximum)
            maximum = t[i];
    return maximum;
}
  
// Функция для вычисления минимума из вектора с плавающей запятой
float min(vector<float> t) {
    float minimum = INT_MAX;
    for (int i = 0; i < t.size(); i++)
        if (t[i] < minimum)
            minimum = t[i];
    return minimum;
}
  
//Функция Сайруса Бека возвращает пару значений, которые затем отображаются в виде строки
pair<int, int>* CyrusBeck(pair<int, int> vertices[], pair<int, int> line[], int n) {
  
    // Значение временного держателя, которое будет возвращено
    pair<int, int>* newPair = new pair<int, int>[2];
  
    // Нормали инициализируются динамически (можно и статически, не имеет значения)
    pair<int, int>* normal = new pair<int, int>[n];
  
    // Расчет нормалей
    for (int i = 0; i < n; i++) {
        normal[i].second = vertices[(i + 1) % n].first - vertices[i].first;
        normal[i].first = vertices[i].second - vertices[(i + 1) % n].second;
    }
  
    // Расчет P1 - P0
    pair<int, int> P1_P0
        = make_pair(line[1].first - line[0].first,
                    line[1].second - line[0].second);
  
    // Инициализация всех значений P0 - PEi
    pair<int, int>* P0_PEi = new pair<int, int>[n];
  
    // Вычисление значений P0 - PEi для всех ребер
    for (int i = 0; i < n; i++) {
  
        // Вычисление PEi - P0, чтобы знаменатель не умножался на -1
        P0_PEi[i].first
            = vertices[i].first - line[0].first;
  
        // при расчете t
        P0_PEi[i].second = vertices[i].second - line[0].second;
    }
  
    int *numerator = new int[n], *denominator = new int[n];
  
    // Вычисление числителя и знаменателя с помощью точечной функции
    for (int i = 0; i < n; i++) {
        numerator[i] = dot(normal[i], P0_PEi[i]);
        denominator[i] = dot(normal[i], P1_P0);
    }
  
    // Initializing the 't' values dynamically
    float* t = new float[n];
  
    // Создание двух векторов, называемых «не входящими» и «не выходящими», для группировки «t» в соответствии с их знаменателями
    vector<float> tE, tL;
  
    // Вычисление 't' и их группировка соответственно
    for (int i = 0; i < n; i++) {
  
        t[i] = (float)(numerator[i]) / (float)(denominator[i]);
  
        if (denominator[i] > 0)
            tE.push_back(t[i]);
        else
            tL.push_back(t[i]);
    }
  
    // Инициализация последних двух значений 't'
    float temp[2];
  
    // Берем максимум всех «tE» и 0, поэтому нажимаем 0
    tE.push_back(0.f);
    temp[0] = max(tE);
  
    // Принимая минимальное значение всех «tL» и 1, поэтому нажмите 1
    tL.push_back(1.f);
    temp[1] = min(tL);
  
    // Ввод значения 't' не может быть больше, чем выход значения 't', следовательно, это тот случай, когда линия полностью выходит за пределы
    if (temp[0] > temp[1]) {
        newPair[0] = make_pair(-1, -1);
        newPair[1] = make_pair(-1, -1);
        return newPair;
    }
  
    // Вычисление координат по x и y
    newPair[0].first
        t
        = (float)line[0].first
          + (float)P1_P0.first * (float)temp[0];
    newPair[0].second
        = (float)line[0].second
          + (float)P1_P0.second * (float)temp[0];
    newPair[1].first
        = (float)line[0].first
          + (float)P1_P0.first * (float)temp[1];
    newPair[1].second
        = (float)line[0].second
          + (float)P1_P0.second * (float)temp[1];
    cout << '(' << newPair[0].first << ", "
         << newPair[0].second << ") ("
         << newPair[1].first << ", "
         << newPair[1].second << ")";
  
    return newPair;
}
  
// Код драйвера
int main() {
  
    // Настройка окна и цикла и вершин полигона и линии
    RenderWindow window(VideoMode(500, 500), "Cyrus Beck");
    pair<int, int> vertices[]
        = { make_pair(200, 50),
            make_pair(250, 100),
            make_pair(200, 150),
            make_pair(100, 150),
            make_pair(50, 100),
            make_pair(100, 50) };
  
    // Убедитесь, что вершины расположены по часовой стрелке
    int n = sizeof(vertices) / sizeof(vertices[0]);
    pair<int, int> line[] = { make_pair(10, 10), make_pair(450, 200) };
    pair<int, int>* temp1 = CyrusBeck(vertices, line, n);
    pair<int, int> temp2[2];
    temp2[0] = line[0];
    temp2[1] = line[1];
  
    // Чтобы разрешить отсечение и отсечение линии простым нажатием клавиши
    bool trigger = false;
    while (window.isOpen()) {
        window.clear();
        Event event;
        if (window.pollEvent(event)) {
            if (event.type == Event::Closed)
                window.close();
            if (event.type == Event::KeyPressed)
                trigger = !trigger;
        }
        drawPolygon(&window, vertices, n);
  
        // Использование значения триггера для обрезки и отсечения строки
        if (trigger) {
            line[0] = temp1[0];
            line[1] = temp1[1];
        }
        else {
            line[0] = temp2[0];
            line[1] = temp2[1];
        }
        drawline(&window, line[0], line[1]);
        window.display();
    }
    return 0;
 } 
*/
}
