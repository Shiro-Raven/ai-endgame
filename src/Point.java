public class Point implements Comparable<Point>, Cloneable {
	int x, y;

	public Point(int a, int b) {
		x = a;
		y = b;
	}
	
	public Object clone() throws CloneNotSupportedException{
		return super.clone();
	}

	@Override
	public int compareTo(Point o) {
		return x != o.x ? x - o.x : y - o.y;
	}
}
