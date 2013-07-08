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

public class NFATransit {
	private boolean printed = false;
	public boolean isPrinted() { return printed; }
	public void setPrinted() { this.printed = true; }

	private NFATransitType transitType;
	public NFATransitType getTransitType() { return this.transitType; }

	private int input;
	public int getInput() { return this.input; }

	private String nonterminal;
	public String getNonterminal() { return this.nonterminal; }

	private NFAState next = null;
	public NFAState getNext() { return next; }

	public NFATransit(int input, NFAState next) {
		this.transitType = NFATransitType.BYTE;
		this.input = input;
		this.next = next;
	}
	public NFATransit(NFAState next) {
		this.next = next;
		this.transitType = NFATransitType.EPSILON;
	}
	public NFATransit(String nonterminal, NFAState next) {
		this.transitType = NFATransitType.NONTERMINAL;
		this.nonterminal = nonterminal;
		this.next = next;
	}
}
