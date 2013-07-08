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

package automata;

import abnf.AbnfState;

import java.util.*;

public class NFAState implements Comparable<NFAState> {
	private static int COUNT = 0;
	private int id;
	public int getId() { return this.id; }

//    private static boolean stucked = false;

    public String toString() { return "(" + id + ")"; }

    private boolean printed = false;
    public boolean isPrinted() { return printed; }
    public void setPrinted() { this.printed = true; }

    public int compareTo(NFAState other) {
        return this.id - other.id;
    }

//    private AbnfState abnfState;
//    public AbnfState getAbnfState() { return this.abnfState; }
//    public void setAbnfState(AbnfState abnfState) { this.abnfState = abnfState; }

//    @Override
//    public int hashCode() {
//        return id;
//    }
//    @Override
//	public boolean equals(Object obj) {
//        return this.id == ((NFAState)obj).id;
//	}
	
	private List<NFATransit> transits = new ArrayList<NFATransit>();
	public List<NFATransit> getTransits() { return transits; }

    private Map<Integer, Set<NFAState>> intTransits = new HashMap<Integer, Set<NFAState>>();
    public Map<Integer, Set<NFAState>> getIntegerTransits() { return intTransits; }

    private Map<String, Set<NFAState>> nonterminalTransits = new HashMap<String, Set<NFAState>>();
    public Map<String, Set<NFAState>> getNonterminalTransits() { return nonterminalTransits; }

    private Set<NFAState> epsilonTransits = new HashSet<NFAState>();
    public Set<NFAState> getEpsilonTransits() { return epsilonTransits; }

	public NFAState() {
		COUNT ++;
		this.id = COUNT;
	}

	public NFAState addTransit(NFATransit transit) {
        this.transits.add(transit);
        if (transit.getTransitType() == NFATransitType.BYTE) {
            Set<NFAState> states = this.intTransits.get(transit.getInput());
            if (states != null) {
                states.add(transit.getNext());
            } else {
                states = new HashSet<NFAState>();
                states.add(transit.getNext());
                this.intTransits.put(transit.getInput(), states);
            }
        } else if (transit.getTransitType() == NFATransitType.NONTERMINAL) {
            Set<NFAState> states = this.nonterminalTransits.get(transit.getNonterminal());
            if (states != null) {
                states.add(transit.getNext());
            } else {
                states = new HashSet<NFAState>();
                states.add(transit.getNext());
                this.nonterminalTransits.put(transit.getNonterminal(), states);
            }
        } else {
            this.epsilonTransits.add(transit.getNext());
        }
		return transit.getNext();
	}
	public NFAState addTransit(char ch) {
		NFAState next = new NFAState();
		return addTransit(ch, next);		
	}
	public NFAState addTransit(char ch, NFAState next) {
		if (Character.isLetter(ch)) {
            Set<NFAState> states = new HashSet<NFAState>();
            states.add(next);
            this.addTransit(new NFATransit((int) (Character.toUpperCase(ch)), next));
            this.addTransit(new NFATransit((int)(Character.toLowerCase(ch)), next));
//            this.intTransits.put((int)(Character.toUpperCase(ch)), states);
//            this.intTransits.put((int)(Character.toLowerCase(ch)), states);
//			nfaTransits.add(new NFATransit((int) (Character.toUpperCase(ch)), next));
//			nfaTransits.add(new NFATransit((int)(Character.toLowerCase(ch)), next));
			return next;
		} else {
            Set<NFAState> states = new HashSet<NFAState>();
            states.add(next);
//            this.intTransits.put((int)ch, states);
//			nfaTransits.add(new NFATransit((int)ch, next));
            this.addTransit(new NFATransit((int)ch, next));
			return next;
		}
	}
	public NFAState addTransit(int b) {
		NFAState next = new NFAState();
		return addTransit(b, next);
	}
	public NFAState addTransit(int b, NFAState next) {
        return this.addTransit(new NFATransit(b, next));
//        if (this.intTransits.get(b) == null) {
//            Set<NFAState> states = new HashSet<NFAState>();
//            states.add(next);
//            this.intTransits.put(b, states);
//            nfaTransits.add(new NFATransit(b, next));
//            return next;
//        } else {
//            Set<NFAState> states = this.intTransits.get(b);
//            states.add(next);
//            this.intTransits.put(b, states);
//            nfaTransits.add(new NFATransit(b, next));
//            return next;
//        }
	}
//	public NFAState addTransit(NFA nfa) {
//		nfaTransits.add(new NFATransit(nfa.getStart()));
//		return nfa.getAccept();
//	}
	public NFAState addTransit(NFAState next) {
//        this.epsilonTransits.add(next);
//		nfaTransits.add(new NFATransit(next));
//		return next;
        return this.addTransit(new NFATransit(next));
	}
	public NFAState addTransit(String nonterminal, NFAState next) {
        return this.addTransit(new NFATransit(nonterminal, next));
//        nfaTransits.add(new NFATransit(nonterminal, next));
//        if (this.nonterminalTransits.get(nonterminal) == null) {
//            Set<NFAState> states = new HashSet<NFAState>();
//            states.add(next);
//            this.nonterminalTransits.put(nonterminal, states);
//            return next;
//        } else {
//            Set<NFAState> states = this.nonterminalTransits.get(nonterminal);
//            states.add(next);
//            this.nonterminalTransits.put(nonterminal, states);
//            return next;
//        }
//		return next;
	}
//    public void addTransitFrom(Set<NFAState> fromStats) {
//        Iterator<NFAState> it = fromStats.iterator();
//        while (it.hasNext()) {
//            it.next().addTransit(this);
//        }
//    }
	
//	public void print() {
//		for(int index = 0; index < this.getNFATransits().size(); index ++) {
//			if (this.getNFATransits().get(index).isPrinted()) break;
//			System.out.print(this.id);
//			System.out.print(" == ");
//			if (this.getNFATransits().get(index).getTransitType() == NFATransitType.BYTE) {
//				System.out.print(String.format("%02X", this.getNFATransits().get(index).getInput()));
//				if (this.getNFATransits().get(index).getInput() >= 0x21 && this.getNFATransits().get(index).getInput() <= 0x7E) {
//					System.out.print(", '" + (char)this.getNFATransits().get(index).getInput() + "'");
//				}
//			} else if (this.getNFATransits().get(index).getTransitType() == NFATransitType.EPSILON)
////			if (this.getNFATransits().get(index).getTransitType() == NFATransitType.BYTE)
////				System.out.print(String.format("%02X", this.getNFATransits().get(index).getInput()));
////			else if (this.getNFATransits().get(index).getTransitType() == NFATransitType.EPSILON)
//				System.out.print("EPSILON");
//			else System.out.print(this.getNFATransits().get(index).getNonterminal());
//			System.out.print(" ==> ");
//			System.out.println(this.getNFATransits().get(index).getNext().getId());
//			this.getNFATransits().get(index).setPrinted();
//			this.getNFATransits().get(index).getNext().print();
//		}
////		for(int index = 0; index < this.getNFATransits().size(); index ++) {
////			this.getNFATransits().get(index).getNext().print();
////		}
//	}
	public void printToDot() {
//        if (stucked) System.out.println("Printing state[" + this.getId() + "]");
        if (this.isPrinted()) return;

        this.setPrinted();

//        Iterator<NFAState> it;
//        Iterator<Integer> inputIt;
//
//        it = this.epsilonTransits.iterator();
//        while (it.hasNext()) {
//            NFAState next = it.next();
//            System.out.print(this.id);
//            System.out.print(" -> ");
//            System.out.print(next.id);
//            System.out.print(" [label=\"");
//            System.out.print("EPSILON");
//            System.out.println("\"];");
//        }
//        inputIt = this.intTransits.keySet().iterator();
//        while (inputIt.hasNext()) {
//            int input = inputIt.next();
//            it = this.intTransits.get(input).iterator();
//            while (it.hasNext()) {
//                NFAState next = it.next();
//                System.out.print(this.id);
//                System.out.print(" -> ");
//                System.out.print(next.id);
//                System.out.print(" [label=\"");
//                System.out.print(String.format("%02X", input));
//                if ((char)input == '\\') System.out.print(", '\\\\'");
//                else if ((char)input == '\"') System.out.print(", '\\\"'");
//                else if (input >= 0x21 && input <= 0x7E) System.out.print(", '" + (char)input + "'");
//                System.out.println("\"];");
//            }
//        }
//
//        Iterator<String> nonterminalIt = this.nonterminalTransits.keySet().iterator();
//        while (nonterminalIt.hasNext()) {
//            String nonterminal = nonterminalIt.next();
//            it = this.nonterminalTransits.get(nonterminal).iterator();
//            while (it.hasNext()) {
//                NFAState next = it.next();
//                System.out.print(this.id);
//                System.out.print(" -> ");
//                System.out.print(next.id);
//                System.out.print(" [label=\"");
//                System.out.print(nonterminal);
//                System.out.println("\"];");
//            }
//        }
//
//        it = this.epsilonTransits.iterator();
//        while (it.hasNext()) {
//            it.next().printToDot();
//        }
//        inputIt = this.intTransits.keySet().iterator();
//        while (inputIt.hasNext()) {
//            int input = inputIt.next();
//            it = this.intTransits.get(input).iterator();
//            while (it.hasNext()) {
//                it.next().printToDot();
//            }
//        }
//
//        nonterminalIt = this.nonterminalTransits.keySet().iterator();
//        while (nonterminalIt.hasNext()) {
//            String nonterminal = nonterminalIt.next();
//            it = this.nonterminalTransits.get(nonterminal).iterator();
//            while (it.hasNext()) {
//                it.next().printToDot();
//            }
//        }

		for(int index = 0; index < this.getTransits().size(); index ++) {
//            if (stucked) System.out.println("Printing transit[" + this.getId() + ", " + index);
//            if (this.getId() == 1
//                    && this.getNFATransits().get(index).getTransitType() == NFATransitType.BYTE
//                    && this.getNFATransits().get(index).getInput() == (int)0x73
//                    && this.getNFATransits().get(index).getNext().getId() == 29922
//                    ) {
//                NFAState.stucked = true;
//                System.out.println("Stuck here...");
//            }
			System.out.print(this.id);
			System.out.print(" -> ");
			System.out.print(this.getTransits().get(index).getNext().getId());
			System.out.print(" [label=\"");
			if (this.getTransits().get(index).getTransitType() == NFATransitType.BYTE) {
				int val = this.getTransits().get(index).getInput();
				System.out.print(String.format("%02X", val));
				if ((char)val == '\\') System.out.print(", '\\\\'");
				else if ((char)val == '\"') System.out.print(", '\\\"'");
				else if (val >= 0x21 && val <= 0x7E) System.out.print(", '" + (char)val + "'");
			} else if (this.getTransits().get(index).getTransitType() == NFATransitType.EPSILON)
				System.out.print("EPSILON");
			else System.out.print(this.getTransits().get(index).getNonterminal());
//            if (stucked) System.out.print("**1**");
			System.out.println("\"];");
//            if (stucked) System.out.println("Entering next state...");
			this.getTransits().get(index).getNext().printToDot();
//            if (stucked) System.out.println("Return from next state...");
		}
//        if (stucked) System.out.println("Returning...");
	}
	
}
