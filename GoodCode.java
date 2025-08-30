
package test;

import java.util.*;

public class GoodCode {

    private static long dist2(Point2D a, Point2D b) {
        long dx = (long) a.x - b.x;
        long dy = (long) a.y - b.y;
        return dx * dx + dy * dy;
    }

    private static long cross(Point2D o, Point2D a, Point2D b) {
        return (long) (a.x - o.x) * (b.y - o.y) - (long) (a.y - o.y) * (b.x - o.x);
    }

    private static List<Point2D> convexHull(List<Point2D> points) {
        int n = points.size();
        if (n <= 1) return new ArrayList<>(points);

        List<Point2D> ps = new ArrayList<>(points);
        ps.sort(Comparator.<Point2D>comparingInt(p -> p.x).thenComparingInt(p -> p.y));

        List<Point2D> uniq = new ArrayList<>();
        Point2D prev = null;
        for (Point2D p : ps) {
            if (prev == null || p.x != prev.x || p.y != prev.y) {
                uniq.add(p);
                prev = p;
            }
        }

        if (uniq.size() <= 1) return new ArrayList<>(uniq);

        List<Point2D> lower = new ArrayList<>();
        for (Point2D p : uniq) {
            while (lower.size() >= 2 &&
                    cross(lower.get(lower.size() - 2), lower.get(lower.size() - 1), p) <= 0) {
                lower.remove(lower.size() - 1);
            }
            lower.add(p);
        }

        List<Point2D> upper = new ArrayList<>();
        for (int i = uniq.size() - 1; i >= 0; i--) {
            Point2D p = uniq.get(i);
            while (upper.size() >= 2 &&
                    cross(upper.get(upper.size() - 2), upper.get(upper.size() - 1), p) <= 0) {
                upper.remove(upper.size() - 1);
            }
            upper.add(p);
        }

        lower.remove(lower.size() - 1);
        upper.remove(upper.size() - 1);
        lower.addAll(upper);
        return lower;
    }

    public static double getMaxDist(List<Point2D> ps) {
        if (ps == null || ps.size() < 2) return 0.0;

        List<Point2D> hull = convexHull(ps);
        int m = hull.size();
        if (m == 0 || m == 1) return 0.0;
        if (m == 2) return Math.sqrt(dist2(hull.get(0), hull.get(1)));

        int j = 1;
        long best2 = 0;

        for (int i = 0; i < m; i++) {
            int ni = (i + 1) % m;

            while (true) {
                int nj = (j + 1) % m;
                long areaNow = Math.abs(cross(hull.get(i), hull.get(ni), hull.get(j)));
                long areaNext = Math.abs(cross(hull.get(i), hull.get(ni), hull.get(nj)));
                if (areaNext > areaNow) {
                    j = nj;
                } else {
                    break;
                }
            }

            long d2 = dist2(hull.get(i), hull.get(j));
            if (d2 > best2) best2 = d2;
            long d2b = dist2(hull.get(ni), hull.get(j));
            if (d2b > best2) best2 = d2b;
        }
        return Math.sqrt((double) best2);
    }
}
