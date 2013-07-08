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

import abnf.RuleName;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.List;
import abnf.Rule;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 6/7/13
 * Time: 9:40 AM
 * To change this template use File | Settings | File Templates.
 */

public class DefinitionAnalyzer {
    private Set<RuleName> definedRuleNames = new HashSet<RuleName>();
    public Set<RuleName> getDefinedRuleNames() { return definedRuleNames; }

    private Set<RuleName> undefinedRuleNames = new HashSet<RuleName>();
    public Set<RuleName> getUndefinedRuleNames() { return undefinedRuleNames; }

    public DefinitionAnalyzer(List<Rule> ruleList) {
        for(int index = 0; index < ruleList.size(); index ++) {
            definedRuleNames.add(ruleList.get(index).getRuleName());
        }
        for(int index = 0; index < ruleList.size(); index ++) {
            Set<RuleName> dependent = ruleList.get(index).getElements().getDependentRuleNames();
            Iterator<RuleName> it = dependent.iterator();
            while (it.hasNext()) {
                RuleName rulename = it.next();
                if (!definedRuleNames.contains(rulename)) {
                    undefinedRuleNames.add(rulename);
                }
            }
        }
    }
}
