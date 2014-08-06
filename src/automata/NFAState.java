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

public class NFAState implements Comparable<NFAState> {
    private static int COUNT = 0;

    public static NFAState[] newInstances(int size) {
        NFAState[] instances = new NFAState[size];
        for(int index = 0; index < size; index ++) {
            instances[index] = new NFAState();
        }
        return instances;
    }


    private int id;

    public int getId() { return this.id; }

    public int compareTo(NFAState other) {
        return this.id - other.id;
    }

    public NFAState() {
        this.id = COUNT ++;
    }

    protected Map<Integer, Set<NFAState>> transition = new HashMap<Integer, Set<NFAState>>();
    public Map<Integer, Set<NFAState>> getTransition() { return this.transition; }

    protected Set<NFAState> epsilonTransition = new HashSet<NFAState>();
    public Set<NFAState> getEpsilonTransition() { return this.epsilonTransition; }

    public Map<NFAState, Set<Integer>> getInverseTransition() {
        return new Function<Integer, NFAState>().getInverseOneToMany(this.transition);
    }

    public NFAState addTransit(int input) {
        return addTransit(input, new NFAState());
    }

    public NFAState addTransit(int input, NFAState next) {
        Set<NFAState> states = this.transition.get(input);
        if (states == null) {
            states = new HashSet<NFAState>();
            this.transition.put(input, states);
        }
        states.add(next);
        return next;
    }

    public NFAState addTransit(char input) {
        return addTransit(input, new NFAState());
    }

    public NFAState addTransit(char input, NFAState next) {
        if (Character.isLetter(input)) {
            this.addTransit((int) (Character.toUpperCase(input)), next);
            this.addTransit((int)(Character.toLowerCase(input)), next);
            return next;
        }
        this.addTransit((int)input, next);
        return next;
    }

    public NFAState addTransit(NFAState next) {
        this.epsilonTransition.add(next);
        return next;
    }

    public Set<NFAState> getTransition(int input) {
        return this.transition.get(input);
    }

    public Set<NFAState> getNextStates() {
        Set<NFAState> states = new HashSet<NFAState>();

        Iterator<Set<NFAState>> itStates;

        itStates = this.transition.values().iterator();
        while (itStates.hasNext()) {
            states.addAll(itStates.next());
        }

        states.addAll(this.epsilonTransition);

        return states;
    }

    public void printToDot(Set<NFAState> printed) {
        if (printed.contains(this)) return;
        printed.add(this);

        Set<NFAState> nextStates = this.getNextStates();

        Map<NFAState, Set<Integer>> inverseTransition = this.getInverseTransition();

        Iterator<NFAState> itNextState = nextStates.iterator();
        while (itNextState.hasNext()) {
            NFAState nextState = itNextState.next();

            System.out.print(this.id);
            System.out.print(" -> ");
            System.out.print(nextState.id);
            System.out.print(" [label=\"");

            StringBuilder label = new StringBuilder();

            if (this.epsilonTransition.contains(nextState)) label.append("EPSILON");

            if (inverseTransition.get(nextState) != null) {
                if (label.length() > 0) label.append(", ");
                List<Integer> terms = new ArrayList<Integer>();
                terms.addAll(inverseTransition.get(nextState));
                Collections.sort(terms);
                for(int j = 0; j < terms.size(); j ++) {
                    if (j > 0) label.append(", ");
                    int val = terms.get(j);
                    label.append(String.format("%02X", val));
                    if ((char)val == '\\') label.append("(\\\\)");
                    else if ((char)val == '\"') label.append("(\\\")");
                    else if (val >= 0x21 && val <= 0x7E) label.append("(" + (char) val + ")");
                }
            }
            System.out.print(label);

            System.out.println("\"];");

            nextState.printToDot(printed);
        }
    }

    public void printToDot() {
        Set<NFAState> printed = new HashSet<NFAState>();
        this.printToDot(printed);
    }

}
