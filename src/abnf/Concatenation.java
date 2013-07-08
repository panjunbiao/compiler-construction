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

//import sip.parser.generator.automata.NFA;
//import sip.parser.generator.automata.NFAState;
import automata.NFA;
import automata.NFAState;

public class Concatenation implements Abnf {//implements DependenceAnalyzer {//implements Operation {
	private List<Repetition> repetitions = new ArrayList<Repetition>();
	public void addRepetition(Repetition repetition) {
		repetitions.add(repetition);
	}
	public List<Repetition> getRepetitions() { return repetitions; }


    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        return EqualHelper.equal(repetitions, ((Concatenation)o).repetitions);
    }

	@Override
	public String toString() {
		String str = "";
		for(int index = 0; index < repetitions.size(); index ++) {
			if (index > 0) str += " ";
			str += repetitions.get(index).toString();
		}
		return str;
	}
//	@Override
//	public void constructCode() {
//		System.out.print("new Concatenation(new Production[] {");
//		for(int index = 0; index < repetitions.size(); index ++) {
//			if (index > 0) System.out.print(", ");
//			repetitions.get(index).constructCode();
//		}
//		System.out.print("})");
//	}
//	@Override
//	public boolean canBeDefinedBy(Set<String> rulenames) {
//		boolean result = true;
//		for(int index = 0; index < repetitions.size(); index ++) {
//			result = result && repetitions.get(index).canBeDefinedBy(rulenames);
//		}
//		return result;
//	}
    @Override
    public Set<RuleName> getDependentRuleNames() {
        Set<RuleName> ruleNames = new HashSet<RuleName>();
		for(int index = 0; index < repetitions.size(); index ++) {
			ruleNames.addAll(repetitions.get(index).getDependentRuleNames());
		}
        return ruleNames;
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
//    public NFA toNFA(Map<String, Rule> rules) throws IllegalAbnfException{
//        NFA nfa = repetitions.get(0).toNFA(rules);
//
//        for(int index = 1; index < repetitions.size(); index ++) {
//            nfa.append(repetitions.get(index).toNFA(rules));
//        }
//
//        return nfa;
//    }

    @Override
    public NFA toNFA(Map<String, Rule> rules) throws IllegalAbnfException {
        NFAState startState = new NFAState();
        NFAState acceptingState = new NFAState();
        this.toNFA(startState, acceptingState, rules);
        return new NFA(startState, acceptingState);
    }

    @Override
    public void toNFA(NFAState startState, NFAState acceptingState, Map<String, Rule> rules) throws IllegalAbnfException{
        NFAState current = startState;
        NFAState next = null;
        for(int index = 0; index < repetitions.size(); index ++) {
            if (index < repetitions.size() - 1) {
                next = new NFAState();
                repetitions.get(index).toNFA(current, next, rules);
                current = next;
            } else {
                repetitions.get(index).toNFA(current, acceptingState, rules);
            }
        }
    }

/*
	@Override
	public List<DependenceAnalyzer> getDerivations() {
//		System.out.println("DEBUG 9");
		if (repetitions.size() <= 0) {
			return null;
		} else if (repetitions.size() == 1) {
//			System.out.println("DEBUG 9.1");
			return repetitions.get(0).getDerivations();
		} else {
			List<DependenceAnalyzer> derivations = new ArrayList<DependenceAnalyzer>();
			derivations.add(this);
			return derivations;
		}
	}
	@Override
	public int getConcatenationLength() {
		return repetitions.size();
	}
	@Override
	public DependenceAnalyzer getConcatenationSymbol(int index) {
		return repetitions.get(index);
	}
*/
}
