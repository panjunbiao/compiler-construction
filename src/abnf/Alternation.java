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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

//import sip.parser.generator.automata.NFA;
//import sip.parser.generator.automata.NFAState;
import automata.NFA;
import automata.NFAState;

public class Alternation implements Abnf {//implements DependenceAnalyzer { //implements Operation {
//    private Set<Concatenation> concatenations = new HashSet<Concatenation>();
	private List<Concatenation> concatenations = new ArrayList<Concatenation>();
	public void addConcatenation(Concatenation concatenation) {
		concatenations.add(concatenation);
	}
	public List<Concatenation> getConcatenations() {
		return concatenations;
	}

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        return EqualHelper.equal(concatenations, ((Alternation)o).concatenations);
    }

	@Override
	public String toString() {
		String str = "";
        Iterator<Concatenation> it = concatenations.iterator();
        while (it.hasNext()) {
            if (!"".equals(str)) str += " / ";
            str += it.next().toString();
        }
		return str;
	}

//	@Override
//	public boolean canBeDefinedBy(Set<String> rulenames) {
//		boolean result = true;
//        Iterator<Concatenation> it = concatenations.iterator();
//        while (it.hasNext()) {
//            result = result && it.next().canBeDefinedBy(rulenames);
//        }
//		return result;
//	}
    @Override
    public Set<RuleName> getDependentRuleNames() {
        Set<RuleName> ruleNames = new HashSet<RuleName>();
        Iterator<Concatenation> it = concatenations.iterator();
        while (it.hasNext()) {
            ruleNames.addAll(it.next().getDependentRuleNames());
        }
        return ruleNames;
	}

//	public void getPlainRuleSet(Set<PlainRule> plainRules) {
//		Set<PlainRule> result = new HashSet<PlainRule>();
//		for(int j = 0; j < concatenations.size(); j ++) {
//		}
//	}

	/*
	@Override
	public int getConcatenationLength() {
		if (concatenations.size() <= 0) {
			return 0;
		} else if (concatenations.size() == 1) {
			return concatenations.get(0).getConcatenationLength();
		} else {
			return 1;
		}
	}
	@Override
	public List<DependenceAnalyzer> getDerivations() {
		if (concatenations.size() <= 0) {
//			System.out.println("DEBUG 1");
			return null;
		} else if (concatenations.size() == 1) {
//			System.out.println("DEBUG 2");
			return concatenations.get(0).getDerivations();
		} else {
//			System.out.println("DEBUG 3");
			List<DependenceAnalyzer> derivations = new ArrayList<DependenceAnalyzer>();
			for(int index = 0; index < concatenations.size(); index ++) {
//				System.out.println("DEBUG 40, concatenations.get(index).getDerivations()=" + concatenations.get(index).getDerivations().size());
				derivations.addAll(concatenations.get(index).getDerivations());
			}
//			System.out.println("Alternation, Derivations=" + derivations.size());
			return derivations;
		}
	}
	@Override
	public DependenceAnalyzer getConcatenationSymbol(int index) {
		if (concatenations.size() == 0) {
			return null;
		}
		if (concatenations.size() == 1) {
			return concatenations.get(0).getConcatenationSymbol(index);
		} else {
			return null;
		}
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
        if (concatenations.size() == 0) throw new IllegalAbnfException("Alternation is empty.");

        if (concatenations.size() == 1) {
            concatenations.iterator().next().toNFA(startState, acceptingState, rules);
            return;
        }

        Iterator<Concatenation> it = concatenations.iterator();
        while (it.hasNext()) {
            it.next().toNFA(startState, acceptingState, rules);
        }
	}

//    @Override
//    public NFA toNFA(Map<String, Rule> rules) throws IllegalAbnfException {
//        return this.toNFA(new NFAState(), new NFAState(), rules);
//    }

}
