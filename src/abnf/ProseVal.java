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

public class ProseVal implements Element {//, Terminal {
	private String value;
    public String getValue() { return value; }

	public ProseVal(String value) {
		this.value = value;
	}

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        return EqualHelper.equal(value, ((ProseVal)o).value);
    }

	@Override
	public String toString() {
		return "<" + value + ">";
	}
//	@Override
//	public void constructABNF() {
//		System.out.print("<" + value + ">");		
//	}
//	@Override
//	public void constructCode() {
//		System.out.print("new ProseVal(" + "\"" + value + "\"" + ")");
//	}
//	@Override
//	public boolean canBeDefinedBy(Set<String> rulenames) {
//		return true;
//	}
    @Override
    public Set<RuleName> getDependentRuleNames() {
        return new HashSet<RuleName>();
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
//	public int getConcatenationLength() {
//		// TODO Auto-generated method stub
//		return 1;
//	}
//
//	@Override
//	public DependenceAnalyzer getConcatenationSymbol(int index) {
//		if (index == 0) return this;
//		else return null;
//	}
//
//	@Override
//	public List<DependenceAnalyzer> getDerivations() {
////		System.out.println("DEBUG 13");
//		List<DependenceAnalyzer> derivations = new ArrayList<DependenceAnalyzer>();
//		derivations.add(this);
//		return derivations;
//	}

//		        prose-val      =  "<" *(%x20-3D / %x3F-7E) ">"


//	public NFA toNFA(Map<String, Rule> rules) throws IllegalAbnfException {
//        return toNFA(new NFAState(), rules);
//	}
//
    @Override
    public NFA toNFA(Map<String, Rule> rules) throws IllegalAbnfException {
        NFAState startState = new NFAState();
        NFAState acceptingState = new NFAState();
        this.toNFA(startState, acceptingState, rules);
        return new NFA(startState, acceptingState);
    }


    @Override
    public void toNFA(NFAState startState, NFAState acceptingState, Map<String, Rule> rules) throws IllegalAbnfException {
        if (value.length() == 0) {
            startState.addTransit(acceptingState);
            return;
        }

        NFAState current = startState;
        for(int j = 0; j < value.length(); j ++) {
            if (j < value.length() -1) current = current.addTransit(value.charAt(j));
            else current = current.addTransit(value.charAt(j), acceptingState);
        }
    }

//    public NFA toNFA(NFAState start, Map<String, Rule> rules) throws IllegalAbnfException {
//        if (value.length() == 0) {
//            return new NFA(start, start);
//        }
//
//        NFA nfa = new NFA(start);
//        NFAState current = start;
//        for(int j = 0; j < value.length(); j ++) {
//            current = current.addTransit(value.charAt(j));
//        }
//        nfa.addAcceptingState(current);
//        return nfa;
//    }
}
