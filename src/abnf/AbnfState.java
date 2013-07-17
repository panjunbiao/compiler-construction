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
//package abnf;
//
///**
// * Created with IntelliJ IDEA.
// * User: james
// * Date: 6/8/13
// * Time: 9:58 AM
// * To change this template use File | Settings | File Templates.
// */
//public abstract class AbnfState {
//    private Abnf abnf;
//    private Abnf point;
//    private AbnfStatePosition position;
//
//    public AbnfState(Abnf abnf, AbnfStatePosition position, Abnf point) {
//        this.abnf = abnf;
//        this.point = point;
//        this.position = position;
//    }
//
////    public String toString() {
////        if (position == AbnfStatePosition.BEFORE) return abnf.toStringBefore(point);
////        else return abnf.toStringAfter(point);
////    }
//}
