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

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 7/3/13
 * Time: 3:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class RangedDFATransit {
    private byte from, to;
    public byte getFrom() { return this.from; }
    public byte getTo() { return this.to; }

    private DFAState next;
    public DFAState getNext() { return this.next; }

    public RangedDFATransit(byte from, byte to, DFAState next) {
        this.from = from;
        this.to = to;
        this.next = next;
    }
}
