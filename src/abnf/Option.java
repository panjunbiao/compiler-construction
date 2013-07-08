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
import java.util.List;
import java.util.Map;
import java.util.Set;
import automata.NFA;
import automata.NFAState;
import java.util.Map;

//import sip.parser.generator.automata.NFA;
//import sip.parser.generator.automata.NFAState;


public class Option implements Element {//, Operation {
	private Alternation alternation;
    public Alternation getAlternation() { return alternation; }

	public Option(Alternation alternation) {
		this.alternation = alternation;
	}

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        return EqualHelper.equal(alternation, ((Option)o).alternation);
    }

	@Override
	public String toString() {
		return "[" + alternation.toString() + "]";
	}
//	@Override
//	public void constructABNF() {
//		System.out.print("[");
//		alternation.constructABNF();
//		System.out.print("]");		
//	}
//	@Override
//	public void constructCode() {
//		System.out.print("new Option(");
//		alternation.constructCode();
//		System.out.print(")");
//	}
//	@Override
//	public boolean canBeDefinedBy(Set<String> rulenames) {
//		return alternation.canBeDefinedBy(rulenames);
//	}
    @Override
	public Set<RuleName> getDependentRuleNames() {
        return alternation.getDependentRuleNames();
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
		return alternation.getConcatenationLength();
	}
	@Override
	public List<DependenceAnalyzer> getDerivations() {
//		System.out.println("DEBUG 12");
		return alternation.getDerivations();
	}
	@Override
	public DependenceAnalyzer getConcatenationSymbol(int index) {
		return alternation.getConcatenationSymbol(index);
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
        startState.addTransit(acceptingState);
        this.alternation.toNFA(startState, acceptingState, rules);
	}

}
