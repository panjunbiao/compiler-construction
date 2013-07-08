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
//import sip.parser.generator.automata.NFATransit;

public class NumVal implements Element {//, Terminal {
	private String base;
    public String getBase() { return base; }

	private List<String> values = new ArrayList<String>();
	public List<String> getValues() { return values; }

	public NumVal(String base) {
		this.base = base;
	}
	public void addValue(String value) {
		values.add(value);
	}

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!EqualHelper.equal(base, ((NumVal)o).base)) return false;
        return EqualHelper.equal(values, ((NumVal)o).values);
    }

	@Override
	public String toString() {
		String str = "%" + base;
		for(int index = 0; index < values.size(); index ++) {
			if (index > 0) str +=".";
			str += values.get(index);
		}
		return str;
	}
//	@Override
//	public void constructABNF() {
//		System.out.print("%" + base);
//		for(int index = 0; index < values.size(); index ++) {
//			if (index > 0) System.out.print(".");
//			System.out.print(values.get(index));
//		}
//		
//	}
//	@Override
//	public void constructCode() {
//		System.out.print("new NumVal(");
//		System.out.print("\"" + base + "\"");
//		System.out.print(", ");
//		System.out.print("new String[] {");
//		for(int index = 0; index < values.size(); index ++) {
//			if (index > 0) System.out.print(", ");
//			System.out.print("\"" + values.get(index) + "\"");
//		}
//		System.out.print("})");
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
//		System.out.println("DEBUG 11");
		List<DependenceAnalyzer> derivations = new ArrayList<DependenceAnalyzer>();
		derivations.add(this);
		return derivations;
	}
*/

    //		        num-val        =  "%" (bin-val / dec-val / hex-val)
//    public NFA toNFA(Map<String, Rule> rules) throws IllegalAbnfException{
//        NFA nfa = new NFA();
//
//        NFAState current = nfa.getStartState();
//
//		int radix;
//		if ("B".equalsIgnoreCase(this.base)) radix = 2;
//		else if ("D".equalsIgnoreCase(this.base)) radix = 10;
//		else if ("X".equalsIgnoreCase(this.base)) radix = 16;
//		else throw new IllegalAbnfException("NumVal base can not be handled.");
//		for(int j = 0; j < values.size(); j ++) {
//			int val = Integer.valueOf(values.get(j), radix);
//            current = current.addTransit((byte)val);
//		}
//
//        nfa.addAcceptingState(current);
//
//		return nfa;
//	}

    @Override
    public NFA toNFA(Map<String, Rule> rules) throws IllegalAbnfException {
        NFAState startState = new NFAState();
        NFAState acceptingState = new NFAState();
        this.toNFA(startState, acceptingState, rules);
        return new NFA(startState, acceptingState);
    }


    //    @Override
    public void toNFA(NFAState startState, NFAState acceptingState, Map<String, Rule> rules) throws IllegalAbnfException {
        if (this.values.size() == 0) {
            startState.addTransit(acceptingState);
            return;
        }

        int radix;
        if ("B".equalsIgnoreCase(this.base)) radix = 2;
        else if ("D".equalsIgnoreCase(this.base)) radix = 10;
        else if ("X".equalsIgnoreCase(this.base)) radix = 16;
        else throw new IllegalAbnfException("NumVal base can not be handled.");

        NFAState current = startState;
        NFAState next = null;
        for(int j = 0; j < values.size(); j ++) {
            int val = Integer.valueOf(values.get(j), radix);
            if (j < values.size() - 1) current = current.addTransit((byte)val);
            else current = current.addTransit((int)val, acceptingState);
        }
    }

}
