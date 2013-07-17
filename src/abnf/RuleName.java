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

package abnf;

import java.util.*;

import automata.NFA;
import automata.NFAState;
import java.util.Map;

//import sip.parser.generator.automata.NFA;
//import sip.parser.generator.automata.NFAState;

public class RuleName implements Element {//, Nonterminal {
    private static int depth = 0;
	private String prefix;
	private String rulename;
	public String toString() { return prefix + rulename; }
	
	@Override
	public boolean equals(Object o) {
        if (o == null) return false;
        if (!EqualHelper.equal(prefix, ((RuleName)o).prefix)) return false;
        return EqualHelper.equal(rulename, ((RuleName)o).rulename);
	}
	
	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}

    public RuleName(String rulename) {
        this.prefix = "";
        this.rulename = rulename;
    }

	public RuleName(String prefix, String rulename) {
		this.prefix = prefix;
		this.rulename = rulename;
	}
	
//	@Override
//	public Set<Terminal> getFirstTerminalSet() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	@Override
//	public DependenceAnalyzer getSymbol(int position) {
//		// TODO Auto-generated method stub
//		return null;
//	}
	
//	@Override
//	public void constructABNF() {
//		System.out.print(rulename);
//		
//	}
//	@Override
//	public void constructCode() {
//		System.out.print(rulename.replace('-', '_'));
//	}
//	@Override
//	public boolean canBeDefinedBy(Set<String> rulenames) {
//		if ("subdomain".equals(rulename) || "ldh-str".equals(rulename) || "comment".equals(rulename)) return true;
//		userinfo = (user / telephone-subscriber) [":" password] "@"
//		if (rulename.equals("user") || rulename.equals("telephone-subscriber") || rulename.equals("password")) {
//			System.out.print("Ruleset = ");
//			Iterator it = rulenames.iterator();
//			int index = 0;
//			while (it.hasNext()) {
//				if (index > 0) System.out.print(", ");
//				System.out.print(it.next());
//				index ++;
//			}
//			System.out.println();
//			System.out.println("Check " + rulename + ", return " + rulenames.contains(rulename));
//			
//		}
//		if (!rulenames.contains(rulename)) {
//			System.out.println("Check " + rulename + ", return " + rulenames.contains(rulename));
//		}
//		return rulenames.contains(rulename);
//	}
    @Override
    public Set<RuleName> getDependentRuleNames() {
        Set<RuleName> ruleNames = new HashSet<RuleName>();
        ruleNames.add(this);
        return ruleNames;
    }

    public void getDependentRuleNames(Set<String> dependent) {
		dependent.add(rulename);
	}

	/*
	@Override
	public int getConcatenationLength() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public DependenceAnalyzer getConcatenationSymbol(int index) {
		if (index == 0) return this;
		else return null;
	}

	@Override
	public List<DependenceAnalyzer> getDerivations() {
		List<DependenceAnalyzer> derivations = new ArrayList<DependenceAnalyzer>();
		derivations.add(this);
		return derivations;
//		System.out.println("DEBUG 16");
		// TODO Auto-generated method stub
	}

*/

    @Override
    public NFA toNFA(Map<String, Rule> rules) throws IllegalAbnfException {
        NFAState startState = new NFAState();
        NFAState acceptingState = new NFAState();
        this.toNFA(startState, acceptingState, rules);
        return new NFA(startState, acceptingState);
    }

    @Override
    public void toNFA(NFAState startState, NFAState acceptingState, Map<String, Rule> rules) throws IllegalAbnfException {
//        for(int j = 0; j < RuleName.depth; j ++) System.out.print(".");
//        System.out.println("Enter rule " + this.toString());
//        RuleName.depth ++;
        if (null == rules.get(this.toString())) {
//            startState.addTransit(this.toString(), acceptingState);
            throw new IllegalAbnfException("Fail to find the definition of " + this.toString());
        }

        Rule rule = rules.get(this.toString());
        if ("=/".equals(rule.getDefinedAs())) {
            throw new IllegalAbnfException("Can not handle incremental definition while generating NFA.");
        }

        rule.getElements().toNFA(startState, acceptingState, rules);
//        for(int j = 0; j < RuleName.depth; j ++) System.out.print(".");
//        RuleName.depth --;
//        System.out.println("Exit rule " + this.toString());
	}
}
