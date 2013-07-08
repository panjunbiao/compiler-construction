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

package abnf;

import java.util.Set;

//import sip.parser.generator.automata.NFA;
//import sip.parser.generator.automata.NFAState;

public class Repeat {//implements DependenceAnalyzer {
	private int min = 0, max = 0;
	public int getMin() { return this.min; }
	public int getMax() { return this.max; }
	
	public Repeat(int min, int max) {
		this.min = min;
		this.max = max;
	}

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        Repeat object = (Repeat)o;
        return (min == object.min && max == object.max);
    }
	
	@Override
	public String toString() {
		String str = "";
		if (min > 0) str += min;
		str += "*";
		if (max > 0) str += max;
		return str;
	}
//	@Override
//	public void constructABNF() {
//		if (min > 0) System.out.print(min);
//		System.out.print("*");
//		if (max > 0) System.out.print(max);
//	}
//	@Override
//	public void constructCode() {
//		System.out.print("new Repeat(" + min + ", " + max + ")");
//	}
//	@Override
//	public boolean canBeDefinedBy(Set<String> rulenames) {
//		return true;
//	}
//    @Override
//	public void getDependentRuleNames(Set<String> dependent) {
//	}

}
