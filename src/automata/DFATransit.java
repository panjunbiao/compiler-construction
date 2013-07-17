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
//import java.util.List;
//
//public class DFATransit {
//
//    private DFATransitType transitType;
//    public DFATransitType getTransitType() { return this.transitType; }
//
//    private int from, to;
//    public int getFrom() { return this.from; }
//    public int getTo() { return this.to; }
//
//    public List<DFATransit> inputSet = new ArrayList<DFATransit>();
//    public List<DFATransit> getInputSet() { return this.inputSet; }
//
//    private int input;
//    public int getInput() { return this.input; }
//
//    private String nonterminal;
//    public String getNonterminal() { return this.nonterminal; }
//
//	private DFAState next = null;
//	public DFAState getNext() { return next; }
//	public void setNext(DFAState next) { this.next = next; }
//
//
//    public int hashCode() {
//        if (this.transitType == DFATransitType.BYTE) {
//            return ("DFA/TRANSIT/BYTE/" + this.input).hashCode();
//        } else {
//            return ("DFA/TRANSIT/NONTERMINAL/" + this.nonterminal).hashCode();
//        }
//    }
//
//    public boolean equals(Object o) {
//        if (!(o instanceof DFATransit)) return false;
//        DFATransit obj = (DFATransit)o;
//        if (this.transitType != obj.transitType) return false;
//        if (this.transitType == DFATransitType.BYTE) {
//            return this.input == obj.input;
//        }
//        return this.nonterminal.equals(obj.nonterminal);
//    }
//
//	public DFATransit(int input, DFAState next) {
//        this.transitType = DFATransitType.BYTE;
//		this.input = input;
//		this.next = next;
//	}
//
//    public DFATransit(String nonterminal, DFAState next) {
//        this.transitType = DFATransitType.NONTERMINAL;
//        this.nonterminal = nonterminal;
//        this.next = next;
//    }
//    public DFATransit(int from, int to, DFAState next) {
//        this.transitType = DFATransitType.RANGED_BYTE;
//        this.from = from;
//        this.to = to;
//        this.next = next;
//    }
//
//    public DFATransit(DFAState next) {
//        this.transitType = DFATransitType.SET;
//        this.next = next;
//    }
//
//    public String getInputLabel() {
//        String label = "";
//        if (DFATransitType.BYTE == this.transitType) {
//            label += "(";
//            int val = this.input;
//            label += String.format("%02X", val);
//            if ((char)val == '\\') label += ",'\\\\'";
//            else if ((char)val == '\"') label += ",'\\\"'";
//            else if (val >= 0x21 && val <= 0x7E) label += ",'" + (char)val + "'";
//            label += ")";
//        } else if (DFATransitType.NONTERMINAL == this.transitType) {
//            label += "(";
//            label += this.nonterminal;
//            label += ")";
//        } else if (DFATransitType.RANGED_BYTE == this.transitType) {
//            label += "(";
//            int val = this.from;
//            label += String.format("%02X", val);
//            if ((char)val == '\\') label += ",'\\\\'";
//            else if ((char)val == '\"') label += ",'\\\"'";
//            else if (val >= 0x21 && val <= 0x7E) label += ",'" + (char)val + "'";
//            label += ")-(";
//            val = this.to;
//            label += String.format("%02X", val);
//            if ((char)val == '\\') label += ",'\\\\'";
//            else if ((char)val == '\"') label += ",'\\\"'";
//            else if (val >= 0x21 && val <= 0x7E) label += ",'" + (char)val + "'";
//            label += ")";
//        } else if (DFATransitType.SET == this.transitType) {
//            for(int index = 0; index < this.inputSet.size(); index ++) {
//                if (index > 0) label += ", ";
//                label += this.inputSet.get(index).getInputLabel();
//            }
//        }
//        return label;
//    }
//}
