import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;

public class Main {
    static int n, m, ix, iy, tx, ty;
    static Point[] warriors;
    static Point[] stones;
    static TreeMap<Point, Integer> warriorIdx;
    static TreeMap<Point, Integer> stoneIdx;

    public static String solve(String grid, String strategy, boolean visualize) {
        return null;
    }
    
    static void parser(String grid) {
        String[] variables = grid.split(";");
        StringTokenizer st = new StringTokenizer(variables[0] + "," + variables[1] + "," + variables[2], ",");
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        ix = Integer.parseInt(st.nextToken());
        iy = Integer.parseInt(st.nextToken());
        tx = Integer.parseInt(st.nextToken());
        ty = Integer.parseInt(st.nextToken());
        stones = stonesAndWarriors(variables[3], stoneIdx = new TreeMap<>((a, b) -> (a.x != b.x ? a.x - b.x : a.y - b.y)));
        warriors = stonesAndWarriors(variables[4], warriorIdx = new TreeMap<>((a, b) -> (a.x != b.x ? a.x - b.x : a.y - b.y)));
    }

    static Point[] stonesAndWarriors(String representation, TreeMap<Point, Integer> ptsToIdx) {
        StringTokenizer st = new StringTokenizer(representation, ",");
        Point[] pts = new Point[st.countTokens()];
        for (int i = 0; i < pts.length; i++) {
            int x = Integer.parseInt(st.nextToken()), y = Integer.parseInt(st.nextToken());
            pts[i] = new Point(x, y);
            ptsToIdx.put(new Point(x, y), i);
        }
        return pts;
    }


}
