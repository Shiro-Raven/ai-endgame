public class Point implements Comparable<Point>, Cloneable {
	int x, y;

	public Point(int a, int b) {
		x = a;
		y = b;
	}
	
	public Point(Point p){
		x = p.x;
		y = p.y;
	}

	@Override
	public int compareTo(Point o) {
		return x != o.x ? x - o.x : y - o.y;
	}
}
