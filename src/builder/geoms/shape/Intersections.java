package builder.geoms.shape;

import java.awt.Point;
import static java.awt.SystemColor.info;


//====================================================
// Описание работы классов и методов исходника на:
// www.interestprograms.ru
// Исходные коды программ и игр
// Автор исходных кодов Клочков Павел
//====================================================

//https://www.interestprograms.ru/source-codes-tochka-peresecheniya-dvuh-pryamyh-na-ploskosti#uravnenie-v-programmnyj-kod
 public   class Intersections {
//     
//        private Point Cross(double a1, double b1, double c1, double a2, double b2, double c2)  {
//			/*
//			 решение
//             x = (b1c2 - c1b2) / (a1b2 - a2b1)
//             y = (a2c1 - c2a1) / (a1b2 - a2b1)
//			
//			*/
//
//            Point pCross = new Point();
//
//            pCross.X = (b1 * c2 - b2 * c1) / (a1 * b2 - a2 * b1);
//
//            pCross.Y = (a2 * c1 - a1 * c2) / (a1 * b2 - a2 * b1);
//
//
//            return pCross;
//        }
//
//        public bool LineLine(Point pABDot1, Point pABDot2, Point pCDDot1, Point pCDDot2, out Point pCross, out Info info)
//        {
//            info = new Info();
//
//
//            // Классическое уравнение прямой
//            /*
//             x  - x1      y - y1
//             -------  = --------    
//             x2 - x1     y2 - y1
//
//
//             запишем уравнение в одну строку
//             x(y2 - y1) + y(x1 - x2) - x1y2 + y1x2 = 0
//
//             a = (y2 - y1)
//             b = (x1 - x2)
//             c = (-x1y2 + y1x2)
//
//             уравнение прямой с коэффициентами
//             ax + by + c = 0
//
//             если а b не равны 0, то
//
//             a1x + b1y + c1 = 0
//             a2x + b2y + c2 = 0
//
//
//             решение
//             x = (b1c2 - c1b2) / (a1b2 - a2b1)
//             y = (a2c1 - c2a1) / (a1b2 - a2b1)
//
//             */
//
//
//            /*
//            
//             Частный случай: прямая параллельна оси Х
//
//             тогда a = 0
//
//            */
//
//
//            /*
//             
//            Частный случай: одна из прямых параллельна оси Y 
//
//            тогда b = 0
//
//            */
//
//            /*
//            если и a = 0 и b = 0, то
//            прямая не определена
//
//            */
//
//
//
//
//            double a1 = pABDot2.Y - pABDot1.Y;
//            double b1 = pABDot1.X - pABDot2.X;
//            double c1 = -pABDot1.X * pABDot2.Y + pABDot1.Y * pABDot2.X;
//
//
//            double a2 = pCDDot2.Y - pCDDot1.Y;
//            double b2 = pCDDot1.X - pCDDot2.X;
//            double c2 = -pCDDot1.X * pCDDot2.Y + pCDDot1.Y * pCDDot2.X;
//
//
//            // До нахождения точки пересечения,
//            // определим состояние параллельности данных прямых
//            /*
//             Необходимым и достаточным условием параллельности двух прямых
//             является:
//             a1     b1
//            ---- = ----- 
//             a2     b2
//
//             в одну строчку
//
//            a1*b2 - a2*b1 = 0
//
//             */
//
//
//
//            // Обе прямые неопределенны
//            if (a1 == 0 && b1 == 0 && a2 == 0 && b2 == 0)
//            {
//                info.Id = 10;
//                info.Message = "Обе прямые не определены";
//
//                return false;
//            }
//
//
//            // Направление первой прямой неопределенно
//            if (a1 == 0 && b1 == 0)
//            {
//                info.Id = 11;
//                info.Message = "Первая прямая не определена";
//
//                return false;
//            }
//
//
//            // Направление второй прямой неопределенно
//            if (a2 == 0 && b2 == 0)
//            {
//                info.Id = 12;
//                info.Message = "Вторая прямая не определена";
//
//                return false;
//            }
//
//
//
//            // Прямые параллельны
//            if ((a1 * b2 - a2 * b1) == 0)
//            {
//
//                info.Id = 40;
//                info.Message = "Прямые параллельны";
//
//                if (a1 == 0)
//                {
//                    // Прямые паралельны оси Х
//                    info.Id = 41;
//                    info.Message = "Прямые паралельны оси Х";
//                }
//
//                if (b1 == 0)
//                {
//                    // Прямые паралелльны оси Y
//                    info.Id = 42;
//                    info.Message = "Прямые паралельны оси Y";
//                }
//
//
//                // Прямые совпадают
//                /*
//                 Необходимым и достаточным условием совпадения прямых
//                 является равенство:
//
//                 a1/a2 = b1/b2 = c1/c2
//                 
//                 */
//                if (a1 * b2 == b1 * a2 && a1 * c2 == a2 * c1 && b1 * c2 == c1 * b2)
//                {
//                    info.Id = 43;
//                    info.Message = "Прямые совпадают";
//                }
//
//
//                return false;
//            }
//
//
//
//
//
//            //  *** Прямые пересекаются ***
//
//
//            pCross = Cross(a1, b1, c1, a2, b2, c2);
//
//
//
//            /*
//             Необходимым и достаточным условием перпендикулярности двух прямых
//             является:
//
//            a1a2 + b1b2 = 0
//             */
//
//            // Прямые перпендикулярны
//            if ((a1 * a2 + b1 * b2) == 0)
//            {
//                info.Id = 50;
//                info.Message = "Прямые перпендикулярны";
//
//                return true;
//            }
//
//
//
//            // Первая прямая паралельна оси Х
//            if (a1 == 0)
//            {
//                info.Id = 60;
//                info.Message = "Первая прямая параллельна оси Х";
//
//                return true;
//            }
//
//            // Вторая прямая паралельна оси Х
//            if (a2 == 0)
//            {
//                info.Id = 61;
//                info.Message = "Вторая прямая параллельна оси Х";
//
//                return true;
//            }
//
//
//            // Первая прямая параллельна оси Y
//            if (b1 == 0)
//            {
//                info.Id = 70;
//                info.Message = "Первая прямая параллельна оси Y";
//
//                return true;
//            }
//
//            // Вторая прямая параллельна оси Y
//            if (b2 == 0)
//            {
//
//                info.Id = 71;
//                info.Message = "Вторая прямая параллельна оси Y";
//
//                return true;
//            }
//
//
//            info.Id = 0;
//            info.Message = "Общий случай";
//
//
//            return true;
//        }
//
//        
//         public class Info  {
//           public String Message;
//           public int Id;
//        }   
}


