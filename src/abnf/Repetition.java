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

import java.util.HashSet;
import java.util.Set;
import automata.NFA;
import automata.NFAState;
import java.util.Map;

//import sip.parser.generator.automata.NFA;
//import sip.parser.generator.automata.NFAState;

public class Repetition implements Abnf {//implements DependenceAnalyzer {//implements Operation {
	private Repeat repeat;
    public Repeat getRepeat() { return repeat; }

	private Element element;
    public Element getElement() { return element; }
	
	public Repetition(Repeat repeat, Element element) {
		this.repeat = repeat;
		this.element = element;
	}
	public Repetition(Element element) {
		this(null, element);
	}

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!EqualHelper.equal(repeat, ((Repetition)o).repeat)) return false;
        return EqualHelper.equal(element, ((Repetition)o).element);
    }
	
	@Override
	public String toString() {
		String str = "";
		if (repeat != null) {
			str += repeat.toString();
		}
		str += element.toString();
		return str;
	}
//	@Override
//	public void constructABNF() {
//		if (repeat != null) {
//			repeat.constructABNF();
//		} else {
////			System.out.print("***repeat is null***");
//		}
//		element.constructABNF();
//	}
//	@Override
//	public void constructCode() {
//		if (repeat == null) {
//			element.constructCode();
//		} else {
//			System.out.print("new Repetition(");
//			repeat.constructCode();
//			System.out.print(",");
//			element.constructCode();
//			System.out.print(")");
//		}
//	}
    @Override
    public Set<RuleName> getDependentRuleNames() {
        return element.getDependentRuleNames();
    }
//    @Override
//    public boolean canBeDefinedBy(Set<String> rulenames) {
//        if (repeat == null) {
//            return element.canBeDefinedBy(rulenames);
//        } else {
//            return repeat.canBeDefinedBy(rulenames) && element.canBeDefinedBy(rulenames);
//        }
//    }
	/*
	@Override
	public int getConcatenationLength() {
		return element.getConcatenationLength();
	}
	@Override
	public DependenceAnalyzer getConcatenationSymbol(int index) {
		return element.getConcatenationSymbol(index);
	}
	@Override
	public List<DependenceAnalyzer> getDerivations() {
//		System.out.println("DEBUG 15");
		return element.getDerivations();
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
		if (this.repeat == null) {
            this.element.toNFA(startState, acceptingState, rules);
            return;
		}

        int min = repeat.getMin();
        int max = repeat.getMax();

        if (min < 0 || max < 0) {
            throw new IllegalAbnfException("Min and max value of a repeat element can not be less than zero.");
        }

        if (max == 0) {
            if (min == 0) {
//              min == 0 && max == 0
                startState.addTransit(acceptingState);
                this.element.toNFA(acceptingState, acceptingState, rules);
                return;
            } else {
//              min > 0 && max == 0
                NFAState current = startState;
                NFAState next = null;
                for(int j = 0; j < min - 1; j ++) {
                    next = new NFAState();
                    this.element.toNFA(current, next, rules);
                    current = next;
                }
                this.element.toNFA(current, acceptingState, rules);
                this.element.toNFA(acceptingState, acceptingState, rules);
                return;
            }
        } else {
            if (min == 0) {
//              min == 0 && max > 0
                NFAState current = startState;
                for(int j = 0; j < max - 1; j ++) {
                    current.addTransit(acceptingState);
                    NFAState next = new NFAState();
                    this.element.toNFA(current, next, rules);
                    current = next;
                }
                current.addTransit(acceptingState);
                this.element.toNFA(current, acceptingState, rules);
                return;
            } else if (min == max) {
//              0 < min == max
                NFAState current = startState;
                for(int j = 0; j < max - 1; j ++) {
                    NFAState next = new NFAState();
                    this.element.toNFA(current, next, rules);
                    current = next;
                }
                this.element.toNFA(current, acceptingState, rules);
                return;
            } else if (min < max) {
//              0 < min < max
                NFAState current = startState;
                for(int j = 0; j < min; j ++) {
                    NFAState next = new NFAState();
                    this.element.toNFA(current, next, rules);
                    current = next;
                }
                for(int j = 0; j < max - min - 1; j ++) {
                    current.addTransit(acceptingState);
                    NFAState next = new NFAState();
                    this.element.toNFA(current, next, rules);
                    current = next;
                }
                current.addTransit(acceptingState);
                this.element.toNFA(current, acceptingState, rules);
                return;
            } else {
                throw new IllegalAbnfException("Max can not less than min");
            }
        }

//        if (0 < max && max < min) {
//            throw new IllegalAbnfException("Max can not less than min");
//        } else if (min == 0 && max == 0) {
//            NFAState acceptingState = new NFAState();
//            NFA nfa = new NFA(acceptingState, acceptingState);
//
//            NFA repetition = this.element.toNFA(rules);
//
//            acceptingState.addTransit(repetition.getStartState());
//            acceptingState.addTransitFrom(repetition.getAcceptingStates());
//
//            return nfa;
//        } else if (min == 0 && max > 0) {
//            NFAState state = new NFAState();
//            NFA nfa = new NFA(state, state);
//            NFA current = nfa;
//            for(int index = 0; index < max; index ++) {
//                NFA sub = this.element.toNFA(rules);
//                sub.getStartState().addTransitFrom(current.getAcceptingStates());
//                nfa.addAcceptingStates(sub.getAcceptingStates());
//                current = sub;
//            }
//            return nfa;
//        } else if (min > 0 && max == 0) {
//            NFA nfa = this.element.toNFA(rules);
//            NFA current = nfa;
//            for(int index = 1; index < min; index ++) {
//                current = this.element.toNFA(rules);
//                nfa.append(current);
//            }
//            current.getStartState().addTransitFrom(current.getAcceptingStates());
//            return nfa;
//        } else { //if ( min > 0 && max > 0) {
//            NFA nfa = this.element.toNFA(rules);
//            for(int index = 1; index < min; index ++) {
//                nfa.append(this.element.toNFA(rules));
//            }
//            NFA current = nfa;
//            for(int index = 0; index < max - min; index ++) {
//                NFA sub = this.element.toNFA(rules);
//                sub.getStartState().addTransitFrom(current.getAcceptingStates());
//                nfa.addAcceptingStates(sub.getAcceptingStates());
//                current = sub;
//            }
//            return nfa;
//        }
	}

}
