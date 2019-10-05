
public class Point implements Comparable<Point> {
	int x, y;

	public Point(int a, int b) {
		x = a;
		y = b;
	}

	@Override
	public int compareTo(Point o) {
		return x != o.x ? x - o.x : y - o.y;
	}
}
