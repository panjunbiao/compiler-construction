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
import junit.framework.Assert;
//import analyzer.RegularAnalyzer;

public class NFA {
    private int hit = 0;
    private int missed = 0;

    private NFAState startState = null;
    public NFAState getStartState() { return startState; }

    private Set<NFAState> acceptingStates = new HashSet<NFAState>();
    public Set<NFAState> getAcceptingStates() { return acceptingStates; }
    public boolean accept(NFAState state) {
        return this.acceptingStates.contains(state);
    }
    public void addAcceptingState(NFAState state) {
        this.acceptingStates.add(state);
    }

    public NFA() {
        this(new NFAState(), new NFAState());
    }

    public NFA(NFAState startState) {
        this(startState, new NFAState());
    }

    public NFA(NFAState startState, NFAState acceptingState) {
        this.startState = startState;
        this.addAcceptingState(acceptingState);
    }

    protected void getStateSet(NFAState current, Set<NFAState> states) {
        if (states.contains(current)) return;
        states.add(current);

        Iterator<NFAState> it;

        it = current.getNextStates().iterator();
        while (it.hasNext()) {
            this.getStateSet(it.next(), states);
        }

        it = current.getEpsilonTransition().iterator();
        while (it.hasNext()) {
            this.getStateSet(it.next(), states);
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
        Iterator<NFAState> it = state.getEpsilonTransition().iterator();
        while (it.hasNext()) {
            this.getEpsilonClosure(it.next(), closure);
        }
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
        Set<NFAState> states = state.getTransition(input);
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

    public Set<NFAState> getImportantStates(Set<NFAState> states) {
        Set<NFAState> important = new HashSet<NFAState>();
        Iterator<NFAState> it = states.iterator();
        while (it.hasNext()) {
            NFAState state = it.next();
            if (state.getTransition().size() != 0) {
                important.add(state);
            }
        }
        return important;
    }

    public DFA toDFA() throws Exception {
        Assert.assertTrue(this.getAcceptingStates().size() == 1);
        NFAState nfaAcceptingState = this.getAcceptingStates().iterator().next();

        Set<DFAState> dfaAcceptingStates = new HashSet<DFAState>();

        Map<Set<NFAState>, DFAState> nfaMap = new HashMap<Set<NFAState>, DFAState>();
        Map<DFAState, Set<NFAState>> dfaMap = new HashMap<DFAState, Set<NFAState>>();

        Set<Set<NFAState>> unmarked = new HashSet<Set<NFAState>>();
        Set<Set<NFAState>> marked = new HashSet<Set<NFAState>>();
        Set<DFAState> dfaStates = new HashSet<DFAState>();

        Set<NFAState> nfaStart = this.getImportantStates(this.getEpsilonClosure(this.startState));
        DFAState dfaStart = new DFAState();
        if (nfaStart.contains(nfaAcceptingState)) dfaAcceptingStates.add(dfaStart);
        nfaMap.put(nfaStart, dfaStart);
        dfaMap.put(dfaStart, nfaStart);

        unmarked.add(nfaStart);

        int lastMissed = 0;
        int lastHit = 0;

        while (!unmarked.isEmpty()) {
            if (missed >= lastMissed * 1.1 || hit >= lastHit * 1.1) {
                System.out.println("marked=" + marked.size() + ", unmarked=" + unmarked.size()
                        + ", hit=" + hit + ", missed=" + missed);
                lastMissed = missed;
                lastHit = hit;
            }

            Set<NFAState> nfaStates = unmarked.iterator().next();
            unmarked.remove(nfaStates);
            marked.add(nfaStates);

            DFAState dfaState = nfaMap.get(nfaStates);

            Iterator<NFAState> it = nfaStates.iterator();
            Map<Integer, Set<NFAState>> transits = new HashMap<Integer, Set<NFAState>>();
            while (it.hasNext()) {
                NFAState nfaState = it.next();

                Iterator<Integer> inputIt = nfaState.getTransition().keySet().iterator();
                while (inputIt.hasNext()) {
                    Integer input = inputIt.next();
                    Set<NFAState> stateTransits = nfaState.getTransition(input);
                    Set<NFAState> moveSet = transits.get(input);
                    if (moveSet == null) {
                        moveSet = new HashSet<NFAState>();
                        transits.put(input, moveSet);
                    }
                    moveSet.addAll(stateTransits);
                }

            }
            Iterator<Integer> inputIt = transits.keySet().iterator();
            while (inputIt.hasNext()) {
                Integer input = inputIt.next();
                Set<NFAState> stateTransits = this.getImportantStates(this.getEpsilonClosure(transits.get(input)));
                if (nfaMap.get(stateTransits) == null) {
                    unmarked.add(stateTransits);
                    DFAState next = new DFAState();
                    if (stateTransits.contains(nfaAcceptingState)) dfaAcceptingStates.add(next);
                    dfaState.addTransition(input, next);
                    dfaMap.put(next, stateTransits);
                    nfaMap.put(stateTransits, next);
                    missed ++;
                } else {
                    DFAState next = nfaMap.get(stateTransits);
                    dfaState.addTransition(input, next);
                    hit ++;
                }
            }
        }
        return new DFA(dfaStart, dfaAcceptingStates);
    }

}
