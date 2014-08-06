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

import java.io.InputStream;
import java.util.*;

public class DFAState {
    public static DFAState[] newInstances(int size) {
        DFAState[] instances = new DFAState[size];
        for(int index = 0; index < size; index ++) {
            instances[index] = new DFAState();
        }
        return instances;
    }

    protected static int COUNT = 0;

    protected int id;
    public int getId() { return this.id; }

//	protected Set<NFAState> nfaStates = new HashSet<NFAState>();
//	public Set<NFAState> getNFAStates() { return nfaStates; }

//    @Override
//    public int hashCode() {
//        return nfaStates.hashCode();
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        return nfaStates.equals(((DFAState)o).getNFAStates());
//    }

    private Map<Integer, DFAState> transition = new HashMap<Integer, DFAState>();
    public Map<Integer, DFAState> getTransition() { return this.transition; }
    public DFAState getTransition(int input) { return this.transition.get(input); }

	public DFAState() { //(boolean accepted) {
		COUNT ++;
		this.id = COUNT;
//		this.accepted = accepted;
	}
//	public DFAState/*(boolean accepted,*/(Set<NFAState> NFAStates) {
//        this();
////		this(accepted);
//		this.NFAStates.addAll(NFAStates);
//	}

    public DFAState addTransition(int input, DFAState next) throws DuplicateTransitionException {
        if (this.transition.get(input) != null) throw new DuplicateTransitionException();
        this.transition.put(input, next);
        return next;
    }

    public DFAState addTransition(int input) throws DuplicateTransitionException {
        return this.addTransition(input, new DFAState());
    }

    public DFAState addTransition(char input, DFAState next) throws DuplicateTransitionException {
        if (Character.isLetter(input)) {
            this.addTransition((int)Character.toUpperCase(input), next);
            this.addTransition((int)Character.toLowerCase(input), next);
            return next;
        }
        return this.addTransition((int)input, next);
    }


//	public DFAState addTransit(DFATransit transit) throws Exception {
//		this.transits.add(transit);
//        if (transit.getTransitType() == DFATransitType.BYTE) {
//            if (this.intTransits.get(transit.getInput()) != null) throw new Exception("Duplicate input within DFAState.");
//            this.intTransits.put(transit.getInput(), transit.getNext());
//        } else {
//            if (this.nonterminalTransits.get(transit.getNonterminal()) != null) throw new Exception("Duplicate nonterminal within DFAState.");
//            this.nonterminalTransits.put(transit.getNonterminal(), transit.getNext());
//        }
//		return transit.getNext();
//	}
//
//	public DFAState addTransit(int b, boolean accepted) throws Exception {
////		DFAState next = new DFAState();//accepted);
//		return addTransit(b, new DFAState());
//	}
//	public DFAState addTransit(int b, DFAState next) throws Exception {
//        return addTransit(new DFATransit(b, next));
////        this.transits.add();
////		return next;
//	}
//    public DFAState addTransit(String nonterminal, DFAState next) throws Exception {
//        return addTransit(new DFATransit(nonterminal, next));
////        this.transits.add(new DFATransit(nonterminal, next));
////        return next;
//    }

//	public void print() {
//		for(int index = 0; index < this.transits.size(); index ++) {
//			if (this.transits.get(index).isPrinted()) break;
//			System.out.print(this.id);
//			System.out.print(" == ");
//			System.out.print(String.format("%02X", this.transits.get(index).getInput()));
//			if (this.transits.get(index).getInput() >= 0x21 && this.transits.get(index).getInput() <= 0x7E) {
//				System.out.print(", '" + (char)this.transits.get(index).getInput() + "'");
//			}
//			System.out.print(" ==> ");
//			System.out.println(this.transits.get(index).getNext().getId());
//			this.transits.get(index).setPrinted();
//			this.transits.get(index).getNext().print();
//		}
////		for(int index = 0; index < this.transits.size(); index ++) {
////			this.transits.get(index).getNext().print();
////		}
//	}
    public Map<DFAState, Set<Integer>> getInverseTransition() {
        return new Function<Integer, DFAState>().getInverseOneToOne(this.transition);
    }

    public Set<DFAState> getNextStates() {
        Set<DFAState> states = new HashSet<DFAState>();
        Iterator<Integer> it = this.transition.keySet().iterator();
        while (it.hasNext()) {
            DFAState state = this.getTransition(it.next());
            if (state != null && !states.contains(state)) states.add(state);
        }
        return states;
    }

    public void printToDot(Set<DFAState> printed) {
        if (printed.contains(this)) return;
        printed.add(this);

        Collection<DFAState> nextStates = this.getNextStates();
//        System.out.println("State[" + this.id + "].nextStates.size=" + nextStates.size());

        Map<DFAState, Set<Integer>> inverseTransition = this.getInverseTransition();

        Iterator<DFAState> nextStateIt = nextStates.iterator();
        boolean circle = false;
        while (nextStateIt.hasNext()) {
            DFAState nextState = nextStateIt.next();
            if (this.equals(nextState) && circle) continue;
            if (this.equals(nextState)) circle = true;

            System.out.print(this.id);
            System.out.print(" -> ");
            System.out.print(nextState.id);//(this.getTransits().get(index).getNext().getId());
            System.out.print(" [label=\"");

            StringBuilder label = new StringBuilder();

            List<Integer> terms = new ArrayList<Integer>();
            terms.addAll(inverseTransition.get(nextState));
            Collections.sort(terms);
            boolean range = false;
            for(int j = 0; j < terms.size(); j ++) {
                if (j > 0 && j < terms.size() - 1) {
                    int before, current, next;
                    before = terms.get(j-1);
                    current = terms.get(j);
                    next = terms.get(j+1);
                    if (    (before >='A' && before + 1 == current && current + 1 == next && next <= 'Z') ||
                            (before >='a' && before + 1 == current && current + 1 == next && next <= 'z') ||
                            (before >='0' && before + 1 == current && current + 1 == next && next <= '9')) {
                        if (!range) {
                            range = true;
                            label.append("~");
                        }
                        continue;
                    } else {
                        range = false;
                    }

                }
                if (j > 0) label.append(", ");
                int val = terms.get(j);
                label.append(String.format("%02X", val));
                if ((char)val == '\\') label.append("(\\\\)");
                else if ((char)val == '\"') label.append("(\\\")");
                else if (val >= 0x21 && val <= 0x7E) label.append("(" + (char) val + ")");
            }

            System.out.print(label);

            System.out.println("\"];");

            nextState.printToDot(printed);
        }
    }

    public void printToDot() {
        Set<DFAState> printed = new HashSet<DFAState>();
        this.printToDot(printed);
    }

}
