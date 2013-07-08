/*
    This file is one of the component a Context-free Grammar Parser Generator,
    which accept a piece of text as the input, and generates a parser
    for the inputted context-free grammar.
    Copyright (C) 2013, Junbiao Pan (Email: panjunbiao@gmail.com)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package analyzer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import abnf.Rule;
import abnf.RuleName;

public class RegularAnalyzer {
	private List<Rule> nonRegularRules = new ArrayList<Rule>();
    public List<Rule> getNonRegularRules() { return nonRegularRules; }

	private List<Rule> regularRules = new ArrayList<Rule>();
	public List<Rule> getRegularRules() { return regularRules; }

    private List<Rule> undefinedRules = new ArrayList<Rule>();
    public List<Rule> getUndefinedRules() { return undefinedRules; }

	public RegularAnalyzer(List<Rule> rules) {
		Set<RuleName> definedRuleNames = new HashSet<RuleName>();
        List<Rule> observedRules = new ArrayList<Rule>();
        observedRules.addAll(rules);

		boolean foundRegular;
		do {
			foundRegular = false;
			for(int index = observedRules.size() - 1; index >= 0; index --) {
                Set<RuleName> dependent = observedRules.get(index).getElements().getDependentRuleNames();
                if (definedRuleNames.containsAll(dependent)) {
                    definedRuleNames.add(observedRules.get(index).getRuleName());
                    regularRules.add(observedRules.get(index));
                    observedRules.remove(index);
                    foundRegular = true;
                    continue;
                }

                if (!dependent.contains(observedRules.get(index).getRuleName())) {
                    continue;
                }

                dependent.remove(observedRules.get(index).getRuleName());
                if (definedRuleNames.containsAll(dependent)) {
                    definedRuleNames.add(observedRules.get(index).getRuleName());
                    nonRegularRules.add(observedRules.get(index));
                    observedRules.remove(index);
                    foundRegular = true;
                }
			}
		} while (foundRegular);
        undefinedRules.addAll(observedRules);
        observedRules.clear();
	}
}
