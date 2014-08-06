package automata;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 7/18/13
 * Time: 8:49 AM
 * To change this template use File | Settings | File Templates.
 */
public class Function<Independent, Dependent> {
    public Map<Dependent, Set<Independent>> getInverseOneToMany(Map<Independent, Set<Dependent>> function) {
        Map<Dependent, Set<Independent>> inverse = new HashMap<Dependent, Set<Independent>>();

        Iterator<Independent> independentIt = function.keySet().iterator();
        while (independentIt.hasNext()) {
            Independent independent = independentIt.next();
            Iterator<Dependent> dependentIt = function.get(independent).iterator();
            while (dependentIt.hasNext()) {
                Dependent dependent = dependentIt.next();
                Set<Independent> independents = inverse.get(dependent);
                if (independents == null) {
                    independents = new HashSet<Independent>();
                    inverse.put(dependent, independents);
                }
                independents.add(independent);
            }
        }
        return inverse;
    }
    public Map<Dependent, Set<Independent>> getInverseOneToOne(Map<Independent, Dependent> function) {
        Map<Dependent, Set<Independent>> inverse = new HashMap<Dependent, Set<Independent>>();

        Iterator<Independent> independentIt = function.keySet().iterator();
        while (independentIt.hasNext()) {
            Independent independent = independentIt.next();
            Dependent dependent = function.get(independent);
            Set<Independent> independents = inverse.get(dependent);
            if (independents == null) {
                independents = new HashSet<Independent>();
                inverse.put(dependent, independents);
            }
            independents.add(independent);
        }
        return inverse;
    }
}
