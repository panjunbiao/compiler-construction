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

public class DFAState {
	private static int COUNT = 0;
	private int id;
	public int getId() { return this.id; }
	
//	private boolean accepted;
//	public boolean isAccepted() { return accepted; }
//	public void setAccepted(boolean accepted) { this.accepted = accepted; }
	
	private HashSet<NFAState> NFAStates = new HashSet<NFAState>();
	public HashSet<NFAState> getNFAStates() { return NFAStates; }

    @Override
    public int hashCode() {
        return NFAStates.hashCode();
    }
    @Override
    public boolean equals(Object o) {
        return NFAStates.equals(((DFAState)o).getNFAStates());
    }

    private boolean printed = false;
    public boolean isPrinted() { return printed; }
    public void setPrinted() { this.printed = true; }


    private List<DFATransit> transits = new ArrayList<DFATransit>();
    public List<DFATransit> getTransits() { return this.transits; }

    private List<DFATransit> mergedTransits = new ArrayList<DFATransit>();
    public List<DFATransit> getMergedTransits() { return this.mergedTransits; }

    private Map<Integer,DFAState> intTransits = new HashMap<Integer, DFAState>();
    public Map<Integer, DFAState> getIntegerTransits() { return intTransits; }

    private Map<String, DFAState> nonterminalTransits = new HashMap<String, DFAState>();
    public Map<String, DFAState> getNonterminalTransits() { return nonterminalTransits; }

//	private List<DFATransit> dfaTransits = new ArrayList<DFATransit>();
//	public List<DFATransit> getDFATransits() { return dfaTransits; }
	
	public DFAState() { //(boolean accepted) {
		COUNT ++;
		this.id = COUNT;
//		this.accepted = accepted;
	}
	public DFAState/*(boolean accepted,*/(Set<NFAState> NFAStates) {
        this();
//		this(accepted);
		this.NFAStates.addAll(NFAStates);
	}

	public DFAState addTransit(DFATransit transit) throws Exception {
		this.transits.add(transit);
        if (transit.getTransitType() == DFATransitType.BYTE) {
            if (this.intTransits.get(transit.getInput()) != null) throw new Exception("Duplicate input within DFAState.");
            this.intTransits.put(transit.getInput(), transit.getNext());
        } else {
            if (this.nonterminalTransits.get(transit.getNonterminal()) != null) throw new Exception("Duplicate nonterminal within DFAState.");
            this.nonterminalTransits.put(transit.getNonterminal(), transit.getNext());
        }
		return transit.getNext();
	}
	public DFAState addTransit(int b, boolean accepted) throws Exception {
//		DFAState next = new DFAState();//accepted);
		return addTransit(b, new DFAState());
	}
	public DFAState addTransit(int b, DFAState next) throws Exception {
        return addTransit(new DFATransit(b, next));
//        this.transits.add();
//		return next;
	}
    public DFAState addTransit(String nonterminal, DFAState next) throws Exception {
        return addTransit(new DFATransit(nonterminal, next));
//        this.transits.add(new DFATransit(nonterminal, next));
//        return next;
    }

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
	public void printToDot() {
        if (this.isPrinted()) {
//            System.out.println(this.id + " has printed, returning.");
            return;
        }
//        System.out.println(this.id + " is being printed.");
        this.setPrinted();
        this.mergeTransits();
//        System.out.println(this.id + " has " + this.transits.size() + " transits or " + this.mergedTransits.size() + " merged transits.");
		for(int index = 0; index < this.mergedTransits.size(); index ++) {
//			if (this.transits.get(index).isPrinted()) break;
			System.out.print(this.id);
//            System.out.print(" (");
//            Iterator<NFAState> itFrom = this.getNFAStates().iterator();
//            while (itFrom.hasNext()) System.out.print(itFrom.next().getId() + ", ");
//            System.out.print(" )");
			System.out.print(" -> ");
			System.out.print(this.mergedTransits.get(index).getNext().getId());
//            System.out.print(" (");
//            Iterator<NFAState> itTo = this.transits.get(index).getNext().getNFAStates().iterator();
//            while (itTo.hasNext()) System.out.print(itTo.next().getId() + ", ");
//            System.out.print(" )");
			System.out.print(" [label=\"");
//            System.out.print(" [label=");
            System.out.print(this.mergedTransits.get(index).getInputLabel());
            System.out.println("\"];");
//            if (DFATransitType.BYTE == this.mergedTransits.get(index).getTransitType()) {
//                System.out.print(" [label=(");
//                int val = this.mergedTransits.get(index).getInput();
//                System.out.print(String.format("%02X", val));
//                if ((char)val == '\\') System.out.print(", '\\\\'");
//                else if ((char)val == '\"') System.out.print(", '\\\"'");
//                else if (val >= 0x21 && val <= 0x7E) System.out.print(", '" + (char)val + "'");
//                System.out.println(")];");
//            } else if (DFATransitType.NONTERMINAL == this.mergedTransits.get(index).getTransitType()) {
//                System.out.print(" [label=(");
//                System.out.print(this.transits.get(index).getNonterminal());
//                System.out.println(")];");
//            } else if (DFATransitType.RANGED_BYTE == this.mergedTransits.get(index).getTransitType()) {
//                System.out.print(" [label=(");
//                int val = this.mergedTransits.get(index).getFrom();
//                System.out.print(String.format("%02X", val));
//                if ((char)val == '\\') System.out.print(", '\\\\'");
//                else if ((char)val == '\"') System.out.print(", '\\\"'");
//                else if (val >= 0x21 && val <= 0x7E) System.out.print(", '" + (char)val + "'");
//                System.out.print(")-(");
//                val = this.mergedTransits.get(index).getTo();
//                System.out.print(String.format("%02X", val));
//                if ((char)val == '\\') System.out.print(", '\\\\'");
//                else if ((char)val == '\"') System.out.print(", '\\\"'");
//                else if (val >= 0x21 && val <= 0x7E) System.out.print(", '" + (char)val + "'");
//                System.out.println(")];");
//            }
//			System.out.println("\"];");
//			this.transits.get(index).setPrinted();
			this.mergedTransits.get(index).getNext().printToDot();
		}
//		for(int index = 0; index < this.transits.size(); index ++) {
//			this.transits.get(index).getNext().print();
//		}
	}

    public void mergeTransits() {
        this.mergedTransits.clear();
        this.mergedTransits.addAll(this.transits);
        boolean mergeRange;
        do {
            mergeRange = false;
            for(int j = 0; j < mergedTransits.size() - 1; j ++) {
                for(int k = j + 1; k < mergedTransits.size(); k ++) {
                    if (    mergedTransits.get(j).getTransitType() == DFATransitType.BYTE &&
                            mergedTransits.get(k).getTransitType() == DFATransitType.BYTE &&
                            mergedTransits.get(j).getNext() == mergedTransits.get(k).getNext() &&
                            Math.abs(mergedTransits.get(j).getInput() - mergedTransits.get(k).getInput()) == 1) {
                        int from = (int)Math.min(mergedTransits.get(j).getInput(), mergedTransits.get(k).getInput());
                        int to = (int)Math.max(mergedTransits.get(j).getInput(), mergedTransits.get(k).getInput());
                        DFATransit transit = new DFATransit(from, to, mergedTransits.get(j).getNext());
                        this.mergedTransits.remove(k);
                        this.mergedTransits.remove(j);
                        this.mergedTransits.add(transit);
                        mergeRange = true;
                        break;
                    }
                    if (    mergedTransits.get(j).getTransitType() == DFATransitType.BYTE &&
                            mergedTransits.get(k).getTransitType() == DFATransitType.RANGED_BYTE &&
                            mergedTransits.get(j).getNext() == mergedTransits.get(k).getNext() &&
                            mergedTransits.get(j).getInput() + 1 == mergedTransits.get(k).getFrom()) {
                        int from = mergedTransits.get(j).getInput();
                        int to = mergedTransits.get(k).getTo();
                        DFATransit transit = new DFATransit(from, to, mergedTransits.get(j).getNext());
                        this.mergedTransits.remove(k);
                        this.mergedTransits.remove(j);
                        this.mergedTransits.add(transit);
                        mergeRange = true;
                        break;
                    }
                    if (    mergedTransits.get(j).getTransitType() == DFATransitType.BYTE &&
                            mergedTransits.get(k).getTransitType() == DFATransitType.RANGED_BYTE &&
                            mergedTransits.get(j).getNext() == mergedTransits.get(k).getNext() &&
                            mergedTransits.get(j).getInput() - 1 == mergedTransits.get(k).getTo()) {
                        int from = mergedTransits.get(k).getFrom();
                        int to = mergedTransits.get(j).getInput();
                        DFATransit transit = new DFATransit(from, to, mergedTransits.get(j).getNext());
                        this.mergedTransits.remove(k);
                        this.mergedTransits.remove(j);
                        this.mergedTransits.add(transit);
                        mergeRange = true;
                        break;
                    }

                    if (    mergedTransits.get(j).getTransitType() == DFATransitType.RANGED_BYTE &&
                            mergedTransits.get(k).getTransitType() == DFATransitType.BYTE &&
                            mergedTransits.get(j).getNext() == mergedTransits.get(k).getNext() &&
                            mergedTransits.get(j).getFrom() - 1 == mergedTransits.get(k).getInput()) {
                        int from = mergedTransits.get(k).getInput();
                        int to = mergedTransits.get(j).getTo();
                        DFATransit transit = new DFATransit(from, to, mergedTransits.get(j).getNext());
                        this.mergedTransits.remove(k);
                        this.mergedTransits.remove(j);
                        this.mergedTransits.add(transit);
                        mergeRange = true;
                        break;
                    }
                    if (    mergedTransits.get(j).getTransitType() == DFATransitType.RANGED_BYTE &&
                            mergedTransits.get(k).getTransitType() == DFATransitType.BYTE &&
                            mergedTransits.get(j).getNext() == mergedTransits.get(k).getNext() &&
                            mergedTransits.get(j).getTo() + 1 == mergedTransits.get(k).getInput()) {
                        int from = mergedTransits.get(j).getFrom();
                        int to = mergedTransits.get(k).getInput();
                        DFATransit transit = new DFATransit(from, to, mergedTransits.get(j).getNext());
                        this.mergedTransits.remove(k);
                        this.mergedTransits.remove(j);
                        this.mergedTransits.add(transit);
                        mergeRange = true;
                        break;
                    }
                    if (    mergedTransits.get(j).getTransitType() == DFATransitType.RANGED_BYTE &&
                            mergedTransits.get(k).getTransitType() == DFATransitType.RANGED_BYTE &&
                            mergedTransits.get(j).getNext() == mergedTransits.get(k).getNext() &&
                            mergedTransits.get(j).getTo() + 1 == mergedTransits.get(k).getFrom()) {
                        int from = mergedTransits.get(j).getFrom();
                        int to = mergedTransits.get(k).getTo();
                        DFATransit transit = new DFATransit(from, to, mergedTransits.get(j).getNext());
                        this.mergedTransits.remove(k);
                        this.mergedTransits.remove(j);
                        this.mergedTransits.add(transit);
                        mergeRange = true;
                        break;
                    }
                    if (    mergedTransits.get(j).getTransitType() == DFATransitType.RANGED_BYTE &&
                            mergedTransits.get(k).getTransitType() == DFATransitType.RANGED_BYTE &&
                            mergedTransits.get(j).getNext() == mergedTransits.get(k).getNext() &&
                            mergedTransits.get(k).getTo() + 1 == mergedTransits.get(j).getFrom()) {
                        int from = mergedTransits.get(k).getFrom();
                        int to = mergedTransits.get(j).getTo();
                        DFATransit transit = new DFATransit(from, to, mergedTransits.get(j).getNext());
                        this.mergedTransits.remove(k);
                        this.mergedTransits.remove(j);
                        this.mergedTransits.add(transit);
                        mergeRange = true;
                        break;
                    }
                }
                if (mergeRange) break;
            }
        } while (mergeRange);
        boolean mergeSet;
        do {
            mergeSet = false;
            for(int j = 0; j < mergedTransits.size() - 1; j ++) {
                for(int k = j + 1; k < mergedTransits.size(); k ++) {
                    if (    mergedTransits.get(j).getNext() == mergedTransits.get(k).getNext() &&
                            mergedTransits.get(j).getTransitType() == DFATransitType.SET &&
                            mergedTransits.get(k).getTransitType() != DFATransitType.SET) {
                        mergedTransits.get(j).getInputSet().add(mergedTransits.get(k));
                        this.mergedTransits.remove(k);
                        mergeSet = true;
                        break;
                    }
                    if (    mergedTransits.get(j).getNext() == mergedTransits.get(k).getNext() &&
                            mergedTransits.get(j).getTransitType() != DFATransitType.SET &&
                            mergedTransits.get(k).getTransitType() == DFATransitType.SET) {
                        mergedTransits.get(k).getInputSet().add(mergedTransits.get(j));
                        this.mergedTransits.remove(j);
                        mergeSet = true;
                        break;
                    }
                    if (    mergedTransits.get(j).getNext() == mergedTransits.get(k).getNext() &&
                            mergedTransits.get(j).getTransitType() == DFATransitType.SET &&
                            mergedTransits.get(k).getTransitType() == DFATransitType.SET) {
                        mergedTransits.get(j).getInputSet().addAll(mergedTransits.get(k).getInputSet());
                        this.mergedTransits.remove(k);
                        mergeSet = true;
                        break;
                    }
                    if (    mergedTransits.get(j).getNext() == mergedTransits.get(k).getNext() &&
                            mergedTransits.get(j).getTransitType() != DFATransitType.SET &&
                            mergedTransits.get(k).getTransitType() != DFATransitType.SET) {
                        DFATransit transit = new DFATransit(this.mergedTransits.get(j).getNext());
                        transit.getInputSet().add(mergedTransits.get(j));
                        transit.getInputSet().add(mergedTransits.get(k));
                        this.mergedTransits.remove(k);
                        this.mergedTransits.remove(j);
                        this.mergedTransits.add(transit);
                        mergeSet = true;
                        break;
                    }
                }
                if (mergeSet) break;
            }
        } while (mergeSet);
    }

    /*
	public void expand() throws Exception {
		this.NFAStates = NFAUtils.getEpsilonClosure(this.NFAStates);
		Iterator<NFAState> it = this.getNFAStates().iterator();
//		System.out.println("Expand 1");
		while (it.hasNext()) {
			NFAState nfa = it.next();
//			System.out.println("Expand 2");
			for(int j = 0; j < nfa.getNFATransits().size(); j ++) {
//				System.out.println("Expand 3");
				if (nfa.getNFATransits().get(j).getTransitType() == NFATransitType.COMMENT) {
					System.out.println("COMMENT hit on NFA id " + nfa.getNFATransits().get(j).getNext().getId());
					continue;
				}
				if (nfa.getNFATransits().get(j).getTransitType() == NFATransitType.EPSILON) {
					continue;
				}
				if (nfa.getNFATransits().get(j).getTransitType() != NFATransitType.BYTE) throw new Exception("DFA could not handle transit types other than BYTE.");
				boolean found = false;
//				System.out.println("Expand 4");
				for(int k = 0; k < this.transits.size(); k ++) {
					if (this.transits.get(k).getInput() == nfa.getNFATransits().get(j).getInput()) {
						found = true;
						if (!this.transits.get(k).getNext().getNFAStates().contains(nfa.getNFATransits().get(j).getNext())) {
							this.transits.get(k).getNext().getNFAStates().add(nfa.getNFATransits().get(j).getNext());
						}
						break;
					}
				}
//				System.out.println("Expand 5, found = " + found);
				if (!found) {
					DFAState next = this.addTransit(nfa.getNFATransits().get(j).getInput(), false);
					next.getNFAStates().add(nfa.getNFATransits().get(j).getNext());
				}
			}
		}
		
//		System.out.println("Expand 6");
		for(int j = 0; j < this.transits.size() - 1; j ++)
			for(int k = 0; k < this.transits.size(); k ++) {
				if (this.transits.get(j).getNext().getNFAStates().equals(this.transits.get(k).getNext().getNFAStates())) {
					this.transits.get(k).setNext(this.transits.get(j).getNext());
				}
			}
//		System.out.println("Expand 7");
		this.printToDot();

		for(int j = 0; j < this.transits.size(); j ++) {
			this.transits.get(j).getNext().expand();
		}
//		System.out.println("Expand 8");
		
	}

*/
}
