package addons;

import model.Stats;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DouglasPeuckerAlg {
    public Set<Stats> calculate(List<Stats> inputData, double e, int first, int last){
        Set<Stats> data = new HashSet<>();
        data.add(inputData.get(first));
        data.add(inputData.get(last));

        /* locate the farthest point */
        int farthestIndex = -1;
        double farthestDistance = 0.0;
        for(int i = first + 1; i < last; i++){
            double d = distance(inputData.get(first), inputData.get(last), inputData.get(i));
            if(d > farthestDistance){
                farthestDistance = d;
                farthestIndex = i;
            }
        }

        if(farthestDistance > e){
            data.addAll(calculate(inputData, e, first, farthestIndex));
            data.addAll(calculate(inputData, e, farthestIndex, last));
        }

        return data;
    }

    /**
     * Calculate distance from section |ab| to point c
     */
    public double distance(Stats a, Stats b, Stats c){
        double x, y; /* x (nearest point from |ab| to c) coordinates */
        double a1, b1; /* line ab params */
        double a2, b2; /* line xc params */

        /* check if vertical line */
        if(a.getX() == b.getX())
            return Math.abs(c.getX() - a.getX());

        /* check if horizontal line */
        if(a.getY() == b.getY())
            return Math.abs(c.getY() - a.getY());

        a1 = (a.getY() - b.getY()) / (a.getX() - b.getX());
        b1 = a.getY() - a1 * a.getX();

        a2  = -1 / a1;
        b2 = c.getY() - a2 * c.getX();

        x = (b2 - b1) / (a1 - a2);
        y = a1 * x + b1;

        return Math.sqrt(Math.pow(c.getX() - x, 2.0) + Math.pow(c.getY() - y, 2.0));
    }
}
