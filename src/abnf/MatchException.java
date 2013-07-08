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

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 5/29/13
 * Time: 3:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class MatchException extends Exception {
    private int actual;
    private int pos;
    private int line;
    private String expected;
    public MatchException(String expected, int actual, int pos, int line) {
        this.expected = expected;
        this.actual = actual;
        this.pos = pos;
        this.line = line;
    }
    public MatchException(String expected, char value, int pos, int line) {
        this.expected = expected;
        this.actual = (int)value;
        this.pos = pos;
        this.line = line;
    }

    public String toString() {
        return "Input stream does not match with '" + (char)actual +"' [" + String.format("%02X", actual) + "] at position " + pos + ":" + line + ". Expected value is " + expected;
    }

}
