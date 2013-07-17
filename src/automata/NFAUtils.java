///*
//    This file is one of the component a Context-free Grammar Parser Generator,
//    which accept a piece of text as the input, and generates a parser
//    for the inputted context-free grammar.
//    Copyright (C) 2013, Junbiao Pan (Email: panjunbiao@gmail.com)
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>.
// */
//
//package automata;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Queue;
//import java.util.Set;
//import java.util.HashSet;
//import java.util.Iterator;
//
//public class NFAUtils {
////    public static Set<NFAState> getEpsilonClosure(Set<NFAState> states) {
////        Set<NFAState> closure = new HashSet<NFAState>();
////        Set<NFAState> stack = new HashSet<NFAState>();
////        stack.addAll(states);
////        while (!stack.isEmpty()) {
////            Iterator<NFAState> it = stack.iterator();
////            NFAState state = it.next();
////            if (closure.contains(state)) continue;
////            closure.add(state);
////            stack.remove(state);
////
////
////
////        }
////    }
//
////    protected static void getEpsilonClosure(NFAState state, Set<NFAState> closure) {
////        if (closure.contains(state)) return;
////        closure.add(state);
////        Iterator<NFAState> it = state.getEpsilonTransits().iterator();
////        while (it.hasNext()) {
////            getEpsilonClosure(it.next(), closure);
////        }
////    }
////
////    public static Set<NFAState> getEpsilonClosure(NFAState state) {
////        Set<NFAState> closure = new HashSet<NFAState>();
////        getEpsilonClosure(state, closure);
////        return closure;
////    }
//
////	public static Set<NFAState> getEpsilonClosure(NFAState state) {
////        System.out.println("Input state: " + state.getId());
////		HashSet<NFAState> EpsilonClosureSet = new HashSet<NFAState>();
////		List<NFAState> EpsilonClosureList = new ArrayList<NFAState>();
////		EpsilonClosureSet.add(state);
////		EpsilonClosureList.add(state);
////
////		// Calculate epsilon-closure of current state.
////        int j = 0;
////        while (j < EpsilonClosureList.size()) {
//////        }
//////		for(int j = 0; j < EpsilonClosureList.size(); j ++) {
////			List<NFATransit> nexts = EpsilonClosureList.get(j).getNFATransits();
////			for(int k = 0; k < nexts.size(); k ++) {
////				if (NFATransitType.EPSILON ==nexts.get(k).getTransitType()
////						&& !EpsilonClosureSet.contains(nexts.get(k).getNext())) {
////					EpsilonClosureSet.add(nexts.get(k).getNext());
////					EpsilonClosureList.add(nexts.get(k).getNext());
////                }
//////                else if (NFATransitType.NONTERMINAL ==nexts.get(k).getTransitType()
//////                        && "RFC3261-comment-suffix".equals(nexts.get(k).getNonterminal())
//////                        && !EpsilonClosureSet.contains(nexts.get(k).getNext())) {
//////                    System.out.println("Comment suffix hit");
//////                    EpsilonClosureSet.add(nexts.get(k).getNext());
//////                    EpsilonClosureList.add(nexts.get(k).getNext());
//////                }
////			}
////            j ++;
////		}
////        System.out.print("Output epsilon closure: ");
////        for(int index = 0; index < EpsilonClosureList.size(); index ++) {
////            System.out.print(EpsilonClosureList.get(index).getId() + ", ");
////        }
////        System.out.println();
////		return EpsilonClosureSet;
////	}
////    public static Set<NFAState> getEpsilonClosure(Set<NFAState> states) {
////        Set<NFAState> closure = new HashSet<NFAState>();
////        Iterator<NFAState> it = states.iterator();
////        while (it.hasNext()) {
////            getEpsilonClosure(it.next(), closure);
////        }
////        return closure;
////
////    }
////	public static HashSet<NFAState> getEpsilonClosure(Set<NFAState> states) {
////		HashSet<NFAState> EpsilonClosureSet = new HashSet<NFAState>();
////		List<NFAState> EpsilonClosureList = new ArrayList<NFAState>();
////		EpsilonClosureSet.addAll(states);
////		EpsilonClosureList.addAll(states);
////		// Calculate epsilon-closure of current state.
////        int j = 0;
////        while (j < EpsilonClosureList.size()) {
//////
//////        }
//////		for(int j = 0; j < EpsilonClosureList.size(); j ++) {
////			List<NFATransit> nexts = EpsilonClosureList.get(j).getNFATransits();
////			for(int k = 0; k < nexts.size(); k ++) {
////				if (NFATransitType.EPSILON ==nexts.get(k).getTransitType()
////						&& !EpsilonClosureSet.contains(nexts.get(k).getNext())) {
////					EpsilonClosureSet.add(nexts.get(k).getNext());
////					EpsilonClosureList.add(nexts.get(k).getNext());
////				}
//////                else if (NFATransitType.NONTERMINAL ==nexts.get(k).getTransitType()
//////                        && "RFC3261-comment-suffix".equals(nexts.get(k).getNonterminal())
//////                        && !EpsilonClosureSet.contains(nexts.get(k).getNext())) {
//////                    System.out.println("Comment suffix hit");
//////                    EpsilonClosureSet.add(nexts.get(k).getNext());
//////                    EpsilonClosureList.add(nexts.get(k).getNext());
//////                }
////			}
////            j ++;
////		}
////		return EpsilonClosureSet;
////
////	}
//
////    public static HashSet<NFAState> move(HashSet<NFAState> states, char a) {
////        HashSet<NFAState> nexts = new HashSet<NFAState>();
////        Iterator<NFAState> it = states.iterator();
////        while (it.hasNext()) {
////            NFAState state = it.next();
////            Set<NFAState> nexts = state.getByteTransits().get()
////
////            for (int index = 0; index < state.getNFATransits().size(); index ++) {
//////                if (NFATransitType.NONTERMINAL == state.getNFATransits().get(index).getTransitType()
//////                        && "RFC3261-comment-suffix".equals(state.getNFATransits().get(index).getNonterminal())) {
//////                }
////                if (NFATransitType.BYTE == state.getNFATransits ().get(index).getTransitType()
////                && (byte)a == state.getNFATransits().get(index).getInput()) {
////                    nexts.add(state.getNFATransits().get(index).getNext());
////                }
////            }
////        }
////        return nexts;
////    }
////    public static HashSet<NFAState> move(HashSet<NFAState> states, String transit) {
////        HashSet<NFAState> nexts = new HashSet<NFAState>();
////        Iterator<NFAState> it = states.iterator();
////        while (it.hasNext()) {
////            NFAState state = it.next();
////            for (int index = 0; index < state.getNFATransits().size(); index ++) {
////                if (NFATransitType.NONTERMINAL == state.getNFATransits().get(index).getTransitType()
////                    && transit.equals(state.getNFATransits().get(index).getNonterminal())) {
////                    nexts.add(state.getNFATransits().get(index).getNext());
////                }
////            }
////        }
////        return nexts;
////    }
//
////    public static DFAState genrateDFA(NFAState s0) {
////        HashSet<DFAState> unmarkedDStates = new HashSet<DFAState>();
////        HashSet<DFAState> markedDStates = new HashSet<DFAState>();
////        DFAState d0 = new DFAState(false, getEpsilonClosure(s0));
////        unmarkedDStates.add(d0);
////        while (!unmarkedDStates.isEmpty()) {
////            DFAState T = unmarkedDStates.iterator().next();
////            unmarkedDStates.remove(T);
////            markedDStates.add(T);
////            for(char a =  0x00; a <= 0xff; a ++) {
////                HashSet<NFAState> next = move(T.getNFAStates(),a);
////                if (next.isEmpty()) continue;
////                DFAState U = new DFAState(false, getEpsilonClosure(next));
////                if (unmarkedDStates.contains(U)) {
////                    Iterator<DFAState> it = unmarkedDStates.iterator();
////                    DFAState u = it.next();
////                    while (!u.equals(U)) u = it.next();
////                    U = u;
////                } else if (markedDStates.contains(U)) {
////                    Iterator<DFAState> it = markedDStates.iterator();
////                    DFAState u = it.next();
////                    while (!u.equals(U)) u = it.next();
////                    U = u;
////                } else {
////                    unmarkedDStates.add(U);
////                }
////                T.addTransit((byte)a, U);
////            }
////            HashSet<NFAState> comment = move(T.getNFAStates(), "RFC3261-comment-suffix");
////            if (!comment.isEmpty()) {
////                DFAState U = new DFAState(false, getEpsilonClosure(comment));
////                if (unmarkedDStates.contains(U)) {
////                    Iterator<DFAState> it = unmarkedDStates.iterator();
////                    DFAState u = it.next();
////                    while (!u.equals(U)) u = it.next();
////                    U = u;
////                } else if (markedDStates.contains(U)) {
////                    Iterator<DFAState> it = markedDStates.iterator();
////                    DFAState u = it.next();
////                    while (!u.equals(U)) u = it.next();
////                    U = u;
////                } else {
////                    unmarkedDStates.add(U);
////                }
////                T.addTransit("RFC3261-comment-suffix", U);
////
////            }
////            System.out.println("markedDStates.size()= " + markedDStates.size() + ", unmarkedDStates.size()= " + unmarkedDStates.size());
////        }
////        return d0;
////    }
//
//}
