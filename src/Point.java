public class Point implements Comparable<Point>, Cloneable {
    int x, y;

    public Point(int a, int b) {
        x = a;
        y = b;
    }

    public Point(Point p) {
        x = p.x;
        y = p.y;
    }

    void move(String direction) {
        switch (direction) {
            case "up":
                x--;
                return;
            case "down":
                x++;
                return;
            case "left":
                y--;
                return;
            case "right":
                y++;
                return;
        }
        return;
    }

    @Override
    public int compareTo(Point o) {
        return x != o.x ? x - o.x : y - o.y;
    }
    
    @Override
	public boolean equals(Object obj) {
		Point otherPoint = (Point) obj;
    	return (otherPoint.x == this.x) && (otherPoint.y == this.y);
	}
    
    @Override
    public String toString(){
    	return "{" + this.x + ", " + this.y + "}";
    }
}
