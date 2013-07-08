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
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import automata.NFA;
import automata.NFAState;
//import sip.parser.generator.automata.NFA;
//import sip.parser.generator.automata.NFAState;
//import sip.parser.generator.automata.NFATransit;

public class RangedNumVal implements Element {//, Terminal {
	private String base, from ,to;
    public String getBase() { return base; }
    public String getFrom() { return from; }
    public String getTo() { return to; }
	
	public RangedNumVal(String base, String from, String to) {
		this.base = base;
		this.from = from;
		this.to = to;
	}

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!EqualHelper.equal(base, ((RangedNumVal)o).base)) return false;
        if (!EqualHelper.equal(from, ((RangedNumVal)o).from)) return false;
        return EqualHelper.equal(to, ((RangedNumVal)o).to);
    }

	@Override
	public String toString() {
		return "%" + base + from + "-" + to;
	}

//	@Override
//	public Set<Terminal> getFirstTerminalSet() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public DependenceAnalyzer getSymbol(int position) {
//		// TODO Auto-generated method stub
//		return null;
//	}

//	@Override
//	public void constructABNF() {
//		System.out.print("%" + base + from + "-" + to);
//		
//	}
//
//	@Override
//	public void constructCode() {
//		System.out.print("new RangedNumVal(");
//		System.out.print("\"" + base + "\"");
//		System.out.print(", ");
//		System.out.print("\"" + from + "\"");
//		System.out.print(", ");
//		System.out.print("\"" + to + "\"");
//		System.out.print(")");
//	}
//
//	@Override
//	public boolean canBeDefinedBy(Set<String> rulenames) {
//		return true;
//	}
    @Override
    public Set<RuleName> getDependentRuleNames() {
        return new HashSet<RuleName>();
    }

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
////		System.out.println("DEBUG 14");
//		List<DependenceAnalyzer> derivations = new ArrayList<DependenceAnalyzer>();
//		derivations.add(this);
//		return derivations;
//	}
//	public NFAState toNFAStatus() throws Exception {
//		NFAState start = new NFAState();
//		return start;
//	}

    @Override
    public NFA toNFA(Map<String, Rule> rules) throws IllegalAbnfException {
        NFAState startState = new NFAState();
        NFAState acceptingState = new NFAState();
        this.toNFA(startState, acceptingState, rules);
        return new NFA(startState, acceptingState);
    }

    @Override
    public void toNFA(NFAState startState, NFAState acceptingState, Map<String, Rule> rules) throws IllegalAbnfException {
		int radix;
		if ("B".equalsIgnoreCase(this.base)) radix = 2;
		else if ("D".equalsIgnoreCase(this.base)) radix = 10;
		else if ("X".equalsIgnoreCase(this.base)) radix = 16;
		else throw new IllegalAbnfException("NumVal base can not be handled.");

		int fromVal = Integer.valueOf(this.from, radix);
		int toVal = Integer.valueOf(this.to, radix);
        if (fromVal > toVal) throw new IllegalAbnfException("Illegal range of NumVal.");
		for( int j = fromVal; j <= toVal; j ++) {
            startState.addTransit((int)j, acceptingState);
		}
	}
	
}
