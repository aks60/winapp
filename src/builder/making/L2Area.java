import java.util.ArrayList;
 //http://forums.balancer.ru/tech/forum/2005/03/t32285--zadacha-o-prinadlezhnosti-tochki-figure.html
public class L2Area
{
        protected class Point
        {
                protected double x, y;
                Point (double _x, double _y) {x=_x; y=_y;}
        }
 
        private ArrayList<Point> _points;
 
        L2Area()
        {
                _points = new ArrayList<Point>();
        }
 
        public void add(double x, double y)
        {
                _points.add(new Point(x,y));
        }
 
        public void print()
        {
                for(Point p : _points)
                        System.out.println("("+p.x+","+p.y+")");
        }
 
        public boolean isIntersect(double x, double y, Point p1, Point p2)
        {
                double dy1 = p1.y - y;
                double dy2 = p2.y - y;
 
                if(Math.signum(dy1) == Math.signum(dy2))
                        return false;
               
                double dx1 = p1.x - x;
                double dx2 = p2.x - x;
 
                if(dx1 >= 0 && dx2 >= 0)
                        return true;
               
                if(dx1 < 0 && dx2 < 0)
                        return false;
 
                double dx0 = dy1 * (p1.x-p2.x)/(p1.y-p2.y);
 
                return dx0 <= dx1;
        }
 
        public boolean isInside(double x, double y)
        {
                int intersect_count = 0;
                for(int i=0; i<_points.size(); i++)
                {
                        Point p1 = _points.get(i>0 ? i-1 : _points.size()-1);
                        Point p2 = _points.get(i);
 
                        System.out.println("("+p1.x+","+p1.y+")-("+p2.x+","+p2.y+") => "+isIntersect(x,y,p1,p2));
                       
                        if(isIntersect(x,y,p1,p2))
                                intersect_count++;
                }
 
                return intersect_count%2 == 1;
        }
 
        public void test(L2Area a)
        {
 
        a.add(0,5);
        a.add(10,0);
        a.add(0,0);
 
                System.out.println(a.isInside(1,2.5));
        }
 
    public static void main(String[] args)
    {
        L2Area a = new L2Area();
 
        a.test(a);
        }
}
