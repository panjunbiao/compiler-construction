package analyzer;

import abnf.Rule;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 8/2/13
 * Time: 10:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class DependanceClosure {
    private List<Rule> ruleList = new ArrayList<Rule>();
    public List<Rule> getRuleList() { return this.ruleList; }

    public DependanceClosure(List<Rule> ruleList, String start) throws Exception {
        Map<String, Rule> ruleMap = new HashMap<String, Rule>();
        for(int index = 0; index < ruleList.size(); index ++) {
            String name = ruleList.get(index).getRuleName().toString();
            Rule rule = ruleList.get(index);
            if (ruleMap.get(name) != null) {
                throw new Exception("The definition of rule " + ruleList.get(index).getRuleName().toString() + " duplicates.");
            }
            ruleMap.put(name, rule);
        }
        Set<String> ruleNameSet = new HashSet<String>();
        ruleNameSet.add(start);

        Set<String> marked = new HashSet<String>();
        Set<String> unmarked = new HashSet<String>();

        unmarked.add(start);

        while (!unmarked.isEmpty()) {
            String name = unmarked.iterator().next();
            Set<String> dependant = ruleMap.get(name).getElements().getDependentRuleNames();
            Iterator<String> it = dependant.iterator();
            while (it.hasNext()) {
                String dep = it.next();
                if (marked.contains(dep) || unmarked.contains(dep)) continue;
                unmarked.add(dep);
            }
            this.ruleList.add(ruleMap.get(name));
            marked.add(name);
            unmarked.remove(name);
        }
    }
}
