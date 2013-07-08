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

import abnf.AbnfParser;
import abnf.Rule;
//import analyzer.RegularAnalyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DFA {
	public static int MAXDEPTH = 3;
	public static int depth = 0;
//	public static String ANONYMOUS = "#ANONYMOUS-DFA#";
		
	private String name;
	public String getName() { return name; }
	
	private DFAState start;
	public DFAState getStart() { return start; }
	public void setStart(DFAState start) { this.start = start; } 
	
	private DFAState accept;
	public DFAState getAccept() { return accept; }
	public void setAccept(DFAState accept) { this.accept = accept; }
//	public DFA(String name) throws Exception {
//		if (ANONYMOUS.equalsIgnoreCase(name)) throw new Exception("Name conflicts with ANONYMOUS NFA.");
//		this.name = name;
//	}

    protected void getStateSet(DFAState current, Set<DFAState> states) {
        if (states.contains(current)) return;
        states.add(current);
        for(int index = 0; index < current.getTransits().size(); index ++) {
            this.getStateSet(current.getTransits().get(index).getNext(), states);
        }
    }
    public Set<DFAState> getStateSet() {
        Set<DFAState> states = new HashSet<DFAState>();
        this.getStateSet(this.getStart(), states);
        return states;
    }


    public DFA(DFAState start) {
        this.start = start;
    }
	
//	public void printToDot() {
//		System.out.println("digraph " + this.name.replaceAll("-", "_") + "{");
//		this.start.printToDot();
//		System.out.println("}");
//	}
//
//	public void print() {
//		this.start.print();
//	}
	
//	public static void main(String[] args) throws Exception {
//        Map<String, Rule> rules = new HashMap<String, Rule>();
//        RegularAnalyzer analyzer = new RegularAnalyzer();
//        AbnfParser parser = new AbnfParser("", System.in);
//        List<Rule> ruleList = parser.parse();
//        analyzer.analyze(ruleList);
//        for(int index = 0; index < analyzer.getRegularRules().size(); index ++) {
////			System.out.println(analyzer.getRegularRules().get(index).toString());
////			analyzer.getRegularRules().get(index).toNFA().printToDot();
//            rules.put(analyzer.getRegularRules().get(index).getRuleName().toString(), analyzer.getRegularRules().get(index));
////			System.out.println();
//        }
////		rules.get("RFC3261-Accept").toNFA(rules).printToDot();
////		rules.get("RFC3261-comment").toNFA(rules).printToDot();
//        rules.get("RFC3261-SIP-message").toNFA(rules).printToDot();
////		rules.get("RFC2234-ALPHA").toNFA(rules).printToDot();//.toDFA().print();
////		rules.get("RFC2234-LWSP").toNFA(rules).toDFA().printToDot();
//
//	}
}
