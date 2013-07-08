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
import java.util.Map;
import java.util.Set;
import automata.NFA;
import automata.NFAState;

//import sip.parser.generator.automata.NFA;
//import sip.parser.generator.automata.NFAState;

//		        rule           =  rulename defined-as elements c-nl

public class Rule {
	private RuleName ruleName;
	public RuleName getRuleName() { return ruleName; }

    private String definedAs;
    public String getDefinedAs() { return definedAs; }
    public void setDefinedAs(String definedAs) { this.definedAs = definedAs; }
	
	private Elements elements;
	public Elements getElements() { return elements; }
	
	public Rule(RuleName ruleName, String definedAs, Elements elements) {
		this.ruleName = ruleName;
        this.definedAs = definedAs;
		this.elements = elements;
	}

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!EqualHelper.equal(ruleName, ((Rule)o).ruleName)) return false;
        if (!EqualHelper.equal(definedAs, ((Rule)o).definedAs)) return false;
        return EqualHelper.equal(elements, ((Rule)o).elements);
    }
	
	@Override
	public String toString() {
		String str = ruleName.toString() + definedAs + elements.toString();
		return str;
	}

//	public Set<Terminal> getFirstTerminalSet();
//	public DependenceAnalyzer getSymbol(int position);
	
//	@Override
//	public void constructABNF() {
//		rulename.constructABNF();
//		System.out.print(" = ");
//		alternation.constructABNF();
//		System.out.println();
//	}
//	@Override
//	public void constructCode() {
//		System.out.print("\tprivate Alternation ");
//		rulename.constructCode();
//		System.out.print(" = ");
//		alternation.constructCode(rulename.getRuleName());
//		System.out.println(";");
//	}
//	@Override
//	public boolean canBeDefinedBy(Set<String> rulenames) {
//		return elements.canBeDefinedBy(rulenames);
//	}
//	public void getDependentRuleNames(Set<String> dependent) {
//		elements.getDependentRuleNames(dependent);
//	}
	
//	public void getPlainRuleSet(Set<PlainRule> plainRules) {
//		PlainRule plainRule = new PlainRule(ruleName, null);
//		Set<PlainRule> result = new HashSet<PlainRule>();
//		for(int j = 0; j < concatenations.size(); j ++) {
//			result
//		}
//	}
//	public NFA toNFA(Map<String, Rule> rules) throws Exception {
//		NFA.depth ++;
//		NFA nfa = new NFA(this.ruleName.toString());
//		nfa.setStart(new NFAState());
//		nfa.setAccept(new NFAState());
//		if (NFA.MAXDEPTH > 0 && NFA.depth > NFA.MAXDEPTH || "RFC3261-comment-suffix".equals(this.getRuleName().toString())) nfa.getStart().addTransit(this.ruleName.toString(), nfa.getAccept());
//		else nfa.getStart().addTransit(this.getElements().toNFA(this.ruleName.toString(), rules)).addTransit(nfa.getAccept());
//		NFA.depth --;
//		return nfa;
//	}
}
