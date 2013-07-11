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

import java.util.*;

import abnf.CharVal;
import abnf.NumVal;
import abnf.AbnfParser;
import abnf.RangedNumVal;
import abnf.Repeat;
import abnf.Repetition;
import abnf.Rule;
import abnf.RuleName;
//import analyzer.RegularAnalyzer;

public class NFA {
//	public static final int MAXDEPTH = 0;
//	public static int depth = 0;
    private static int count = 0;
    private int id = 0;

    private int hit = 0;
    private int missed = 0;

	private NFAState startState = null;
	public NFAState getStartState() { return startState; }

//    private NFAState acceptingState = new NFAState();
//    public NFAState getAcceptingState() { return acceptingState; }

//    private NFAState acceptingState = null;
//    public NFAState getAcceptingState() { return acceptingState; }
//    public void setAcceptingState(NFAState acceptingState) { this.acceptingState = acceptingState; }

    private Set<NFAState> acceptingStates = new HashSet<NFAState>();
    public Set<NFAState> getAcceptingStates() { return acceptingStates; }
    public void addAcceptingState(NFAState state) {
        this.acceptingStates.add(state);
    }
//    public void addAcceptingStates(Set<NFAState> states) { acceptingStates.addAll(states); }

//    public NFA append(NFA nfa) {
//        Iterator<NFAState> it = acceptingStates.iterator();
//        while (it.hasNext()) {
//            NFAState state = it.next();
//            state.addTransit(nfa.getStartState());
//        }
//        acceptingStates.clear();
//        acceptingStates.addAll(nfa.acceptingStates);
//        return this;
//    }

//    public void merge(NFA nfa) {
//        if (nfa.acceptingStates.contains(nfa.startState)) {
//            this.acceptingStates.add(this.startState);
//        }
//
//        this.startState.getNFATransits().addAll(nfa.getStartState().getNFATransits());
//
//        Iterator<NFATransit> it = nfa.getAcceptingTransits().iterator();
//        while (it.hasNext()) {
//
//        }
//        acceptingStates.addAll(nfa.getAcceptingStates());
//    }

//    public void checkAcceptingTransits(NFAState currentState,
//                                     Set<NFAState> checkedStates,
//                                     Set<NFATransit> acceptingTransits) {
//        if (checkedStates.contains(currentState)) return;
//        List<NFATransit> transits = currentState.getNFATransits();
//        for(int index = 0; index < transits.size(); index ++) {
//            checkedStates.add(currentState);
//            if (acceptingStates.contains(transits.get(index).getNext())) acceptingTransits.add(transits.get(index));
//            checkAcceptingTransits(transits.get(index).getNext(), checkedStates, acceptingTransits);
//        }
//    }

//    public Set<NFATransit> getAcceptingTransits() {
//        Set<NFAState> checkedStates = new HashSet<NFAState>();
//        Set<NFATransit> acceptingTransits = new HashSet<NFATransit>();
//        checkAcceptingTransits(startState, checkedStates, acceptingTransits);
//        return acceptingTransits;
//    }

//	public DFAState toDFA() throws Exception {
//		DFAState dfa = new DFAState(false, NFAUtils.getEpsilonClosure(this.start));
//		dfa.expand();
//		return dfa;
//
//	}
	public NFA() {
        this(new NFAState(), new NFAState());
	}

    public NFA(NFAState startState) {
        this(startState, new NFAState());
    }

    public NFA(NFAState startState, NFAState acceptingState) {
        this.count ++;
        this.id = this.count;
        this.startState = startState;
        this.addAcceptingState(acceptingState);
    }

//	public void printToDot() {
//		System.out.println("digraph " + .name.replaceAll("-", "_") + "{");
//		this.start.printToDot();
//		System.out.println("}");
//	}
//
//	public void print() {
//		this.start.print();
//	}
    protected void getStateSet(NFAState current, Set<NFAState> states) {
        if (states.contains(current)) return;
        states.add(current);
//        Iterator<NFAState> it;
//        Iterator<Set<NFAState>> valuesIt;
//
//        it = current.getEpsilonTransits().iterator();
//        while (it.hasNext()) this.getStateSet(it.next(), states);
//
//        valuesIt = current.getIntegerTransits().values().iterator();
//        while (valuesIt.hasNext()) {
//            it = valuesIt.next().iterator();
//            while (it.hasNext()) this.getStateSet(it.next(), states);
//        }
//
//        valuesIt = current.getNonterminalTransits().values().iterator();
//        while (valuesIt.hasNext()) {
//            it = valuesIt.next().iterator();
//            while (it.hasNext()) this.getStateSet(it.next(), states);
//        }

        for(int index = 0; index < current.getTransits().size(); index ++) {
            this.getStateSet(current.getTransits().get(index).getNext(), states);
        }
    }
    public List<NFAState> getStateList() {
        List<NFAState> states =  new ArrayList<NFAState>();
        states.addAll(this.getStateSet());
        Collections.sort(states, new Comparator<NFAState>() {
            @Override
            public int compare(NFAState o1, NFAState o2) {
                if (o1.getId() < o2.getId()) return -1;
                else if (o1.getId() > o2.getId()) return 1;
                else return 0;
            }
        });
        return states;
    }
    public Set<NFAState> getStateSet() {
        Set<NFAState> states = new HashSet<NFAState>();
        this.getStateSet(this.getStartState(), states);
        return states;
    }

    public void getEpsilonClosure(NFAState state, Set<NFAState> closure) {
        if (closure.contains(state)) return;
        closure.add(state);
        Iterator<NFAState> it = state.getEpsilonTransits().iterator();
        while (it.hasNext()) {
            this.getEpsilonClosure(it.next(), closure);
        }
//        for(int index = 0; index < state.getNFATransits().size(); index ++) {
//            if (NFATransitType.EPSILON == state.getNFATransits().get(index).getTransitType()) {
//                this.getEpsilonClosure(state.getNFATransits().get(index).getNext(), closure);
//            }
//        }
    }
    public Set<NFAState> getEpsilonClosure(NFAState state) {
        Set<NFAState> closure = new HashSet<NFAState>();
        this.getEpsilonClosure(state, closure);
        return closure;
    }
    public Set<NFAState> getEpsilonClosure(Set<NFAState> states) {
        Set<NFAState> closure = new HashSet<NFAState>();
        Iterator<NFAState> it = states.iterator();
        while (it.hasNext()) {
            this.getEpsilonClosure(it.next(), closure);
        }
        return closure;
    }

    public void move(NFAState state, int input, Set<NFAState> moveTo) {
        Set<NFAState> states = state.getIntegerTransits().get(input);
        if (states == null) return;
        moveTo.addAll(states);
    }

    public Set<NFAState> move(NFAState state, int input) {
        Set<NFAState> moveTo = new HashSet<NFAState>();
        move(state, input, moveTo);
        return moveTo;
    }

    public void move(Set<NFAState> states, int input, Set<NFAState> moveTo) {
        Iterator<NFAState> it = states.iterator();
        while (it.hasNext()) move(it.next(), input, moveTo);
    }

    public Set<NFAState> move(Set<NFAState> states, int input) {
        Set<NFAState> moveTo = new HashSet<NFAState>();
        move(states, input, moveTo);
        return moveTo;
    }

    public void move(NFAState state, String nonterminal, Set<NFAState> moveTo) {
        Set<NFAState> states = state.getNonterminalTransits().get(nonterminal);
        if (states == null) return;
        moveTo.addAll(states);
    }

    public Set<NFAState> move(NFAState state, String nonterminal) {
        Set<NFAState> moveTo = new HashSet<NFAState>();
        move(state, nonterminal, moveTo);
        return moveTo;
    }

    public void move(Set<NFAState> states, String nonterminal, Set<NFAState> moveTo) {
        Iterator<NFAState> it = states.iterator();
        while (it.hasNext()) move(it.next(), nonterminal, moveTo);
    }

    public Set<NFAState> move(Set<NFAState> states, String nonterminal) {
        Set<NFAState> moveTo = new HashSet<NFAState>();
        move(states, nonterminal, moveTo);
        return moveTo;
    }

//    public Set<NFAState> move(Set<NFAState> states, int input) {
//        Set<NFAState> nexts = new HashSet<NFAState>();
//
//        Iterator<NFAState> it = states.iterator();
//        while (it.hasNext()) {
//            NFAState state = it.next();
//            for(int index = 0; index < state.getNFATransits().size(); index ++) {
//                if (state.getNFATransits().get(index).getTransitType() == NFATransitType.BYTE
//                        && state.getNFATransits().get(index).getInput() == input) {
//                    nexts.add(state.getNFATransits().get(index).getNext());
//                }
//            }
////            nexts.addAll(nfaState.getIntegerTransits().get(input));
//        }
//        return nexts;
//    }

    public Set<NFAState> getImportantStates(Set<NFAState> states) {
        Set<NFAState> important = new HashSet<NFAState>();
        Iterator<NFAState> it = states.iterator();
        while (it.hasNext()) {
            NFAState state = it.next();
            if (state.getNonterminalTransits().size() != 0 || state.getIntegerTransits().size() != 0) {
                important.add(state);
            }
        }
        return important;
    }

    public DFA toDFA() throws Exception {
        Set<DFAState> unmarked = new HashSet<DFAState>();
        Set<DFAState> marked = new HashSet<DFAState>();
        Map<Integer, DFAState> dfaStatesMap = new HashMap<Integer, DFAState>();

        DFAState start = new DFAState(this.getImportantStates(this.getEpsilonClosure(this.startState)));
        dfaStatesMap.put(start.getNFAStates().hashCode(), start);

        unmarked.add(start);
        int lastMissed = 0;
        int lastHit = 0;

        while (!unmarked.isEmpty()) {
//            if (missed > lastMissed + 1000 || hit > lastHit + 100000) {
//                System.out.println("marked=" + marked.size() + ", unmarked=" + unmarked.size()
//                        + ", hit=" + hit + ", missed=" + missed);
//                lastMissed = missed;
//                lastHit = hit;
//            }
            DFAState dfaState = unmarked.iterator().next();
            Set<NFAState> nfaStates = dfaState.getNFAStates();
            Iterator<NFAState> it = nfaStates.iterator();
            Map<Integer, Set<NFAState>> intTransits = new HashMap<Integer, Set<NFAState>>();
            Map<String, Set<NFAState>> nonterminalTransits = new HashMap<String, Set<NFAState>>();
            while (it.hasNext()) {
                NFAState nfaState = it.next();

                Iterator<Integer> inputIt = nfaState.getIntegerTransits().keySet().iterator();
                while (inputIt.hasNext()) {
                    Integer input = inputIt.next();
                    Set<NFAState> transits = nfaState.getIntegerTransits().get(input);
                    Set<NFAState> moveSet = intTransits.get(input);
                    if (moveSet != null) {
                        moveSet.addAll(transits);
                    } else {
                        moveSet = new HashSet<NFAState>();
                        moveSet.addAll(transits);
                        intTransits.put(input, moveSet);
                    }
                }

                Iterator<String> nonterminalIt = nfaState.getNonterminalTransits().keySet().iterator();
                while (nonterminalIt.hasNext()) {
                    String nonterminal = nonterminalIt.next();
                    Set<NFAState> transits = nfaState.getNonterminalTransits().get(nonterminal);
                    Set<NFAState> moveSet = intTransits.get(nonterminal);
                    if (moveSet != null) {
                        moveSet.addAll(transits);
                    } else {
                        moveSet = new HashSet<NFAState>();
                        moveSet.addAll(transits);
                        nonterminalTransits.put(nonterminal, moveSet);
                    }
                }

//                for(int index = 0; index < nfaState.getNFATransits().size(); index ++) {
//                    if (nfaState.getNFATransits().get(index).getTransitType() == NFATransitType.EPSILON) continue;
//                    if (nfaState.getNFATransits().get(index).getTransitType() == NFATransitType.BYTE) {
//                        if (intTransits.get(nfaState.getNFATransits().get(index).getInput()) == null) {
//                            Set<NFAState> nexts = new HashSet<NFAState>();
//                            nexts.add(nfaState.getNFATransits().get(index).getNext());
//                            intTransits.put(nfaState.getNFATransits().get(index).getInput(), nexts);
//                        } else {
//                            Set<NFAState> nexts = intTransits.get(nfaState.getNFATransits().get(index).getInput());
//                            nexts.add(nfaState.getNFATransits().get(index).getNext());
//                        }
//                        continue;
//                    }
////                    Nonterminal
//                    if (nonterminalTransits.get(nfaState.getNFATransits().get(index).getInput()) == null) {
//                        Set<NFAState> nexts = new HashSet<NFAState>();
//                        nexts.add(nfaState.getNFATransits().get(index).getNext());
//                        nonterminalTransits.put(nfaState.getNFATransits().get(index).getNonterminal(), nexts);
//                    } else {
//                        Set<NFAState> nexts = nonterminalTransits.get(nfaState.getNFATransits().get(index).getNonterminal());
//                        nexts.add(nfaState.getNFATransits().get(index).getNext());
//                    }
//                }
//                Iterator<Integer> inputIt = intTransits.keySet().iterator();
//                while (inputIt.hasNext()) {
//                    Integer input = inputIt.next();
//                    DFAState nextDFAState = new DFAState(intTransits.get(input));
//                    if (marked.contains(nextDFAState)) {
//
//                    }
//
//                }
            }
            Iterator<Integer> inputIt = intTransits.keySet().iterator();
            while (inputIt.hasNext()) {
                Integer input = inputIt.next();
                Set<NFAState> transits = this.getImportantStates(this.getEpsilonClosure(intTransits.get(input)));
                if (dfaStatesMap.get(transits.hashCode()) != null) {
                    DFAState next = dfaStatesMap.get(transits.hashCode());
                    dfaState.addTransit(input, next);
                    hit ++;
                } else {
                    DFAState next = new DFAState(transits);
                    unmarked.add(next);
                    dfaState.addTransit(input, next);
                    dfaStatesMap.put(next.getNFAStates().hashCode(), next);
                    missed ++;
                }
            }

            Iterator<String> nonterminalIt = nonterminalTransits.keySet().iterator();
            while (nonterminalIt.hasNext()) {
                String nonterminal = nonterminalIt.next();
                Set<NFAState> transits = this.getImportantStates(this.getEpsilonClosure(nonterminalTransits.get(nonterminal)));
                if (dfaStatesMap.get(transits.hashCode()) != null) {
                    DFAState next = dfaStatesMap.get(transits.hashCode());
                    dfaState.addTransit(nonterminal, next);
                    hit ++;
                } else {
                    DFAState next = new DFAState(transits);
                    unmarked.add(next);
                    dfaState.addTransit(nonterminal, next);
                    dfaStatesMap.put(next.getNFAStates().hashCode(), next);
                    missed ++;
                }
            }
            marked.add(dfaState);
            unmarked.remove(dfaState);
//            dfaState.mergeTransits();
//            dfaState.printToDot();

        }
        return new DFA(start);
    }
	
}
