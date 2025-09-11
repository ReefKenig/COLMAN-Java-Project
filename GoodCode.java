
package test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GoodCode {

    public static int getNum(List<Point2D> ps) {
        int x = 0, y = 0;
        boolean found = false;

        // Find the duplicate using a set
        Set<Point2D> seen = new HashSet<>();
        for (Point2D p : ps) {
            if (!seen.add(p)) {
                // Duplicate found
                x = p.x;
                y = p.y;
                found = true;
                break;
            }
        }

        // Count how many times this point appears
        int n = 0;
        if (found) {
            for (Point2D p : ps) {
                if (p.x == x && p.y == y) {
                    n++;
                }
            }
        }

        return n;
    }
}
