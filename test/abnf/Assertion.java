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

import junit.framework.Assert;
import automata.NFA;
import automata.NFAState;
//import automata.NFATransitType;

import java.io.IOException;
import java.util.*;

import abnf.*;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 5/30/13
 * Time: 9:18 AM
 * To change this template use File | Settings | File Templates.
 */

public class Assertion {
    public static void assertMatch(String input, Tester tester, Object expectedOutput, int expectedPos, int expectedLine) throws MatchException, IOException, CollisionException, IllegalAbnfException {
        AbnfParser parser = AbnfParserFactory.newInstance(input);
        Object output = tester.test(parser);
        if (output == null && expectedOutput != null) Assert.fail();
        if (output != null && expectedOutput == null) Assert.fail();
        if (output != null && expectedOutput != null) Assert.assertEquals(expectedOutput, output);
        Assert.assertEquals(expectedPos, parser.getInputStream().getPos());
        Assert.assertEquals(expectedLine, parser.getInputStream().getLine());
    }

    public static void assertMatch(String input, Tester tester, int expectedPos, int expectedLine) throws MatchException, IOException, CollisionException, IllegalAbnfException {
        AbnfParser parser = AbnfParserFactory.newInstance(input);
        tester.test(parser);
        Assert.assertEquals(expectedPos, parser.getInputStream().getPos());
        Assert.assertEquals(expectedLine, parser.getInputStream().getLine());
    }

    public static void assertMatchException(String input, Tester tester, int expectedPos, int expectedLine) {
        AbnfParser parser = AbnfParserFactory.newInstance(input);
        try {
            tester.test(parser);
            Assert.fail();
        } catch (MatchException me) {
            Assert.assertEquals(expectedPos, parser.getInputStream().getPos());
            Assert.assertEquals(expectedLine, parser.getInputStream().getLine());
        } catch (IOException e) {
            Assert.fail();
        } catch (CollisionException ce) {
            Assert.fail();
        } catch (IllegalAbnfException iae) {
            Assert.fail();
        }
    }

    public static void assertCollisionException(String input, Tester tester, int expectedPos, int expectedLine) {
        AbnfParser parser = AbnfParserFactory.newInstance(input);
        try {
            tester.test(parser);
            Assert.fail();
        } catch (MatchException me) {
            Assert.fail();
        } catch (IOException e) {
            Assert.fail();
        } catch (CollisionException ce) {
            Assert.assertEquals(expectedPos, parser.getInputStream().getPos());
            Assert.assertEquals(expectedLine, parser.getInputStream().getLine());
        } catch (IllegalAbnfException iae) {
            Assert.fail();
        }
    }

    public static void assertEquivalent(NFA nfa1, NFA nfa2) {
        Set<NFAState> checked = new HashSet<NFAState>();
        assertEquivalent(nfa1, nfa1.getStartState(), nfa2, nfa2.getStartState(), checked);
    }

    public static void assertEquivalent(NFA nfa1, NFAState state1, NFA nfa2, NFAState state2, Set<NFAState> checked) {
        if (checked.contains(state1) != checked.contains(state2)) {
            if (checked.contains(state1)) System.out.print("S[" + state1.getId() + "] has been checked, while ");
            else System.out.println("S[" + state1.getId() + "] has not been checked, while ");

            if (checked.contains(state2)) System.out.println("S[" + state2.getId() + "] has been checked, ");
            else System.out.println("S[" + state1.getId() + "] has not been checked, ");

            Assert.fail();
        }

        if (checked.contains(state1) && checked.contains(state2)) return;

        checked.add(state1);
        checked.add(state2);

        System.out.println("Checking state1[" + state1.getId() + "] and state2[" + state2.getId() + "]");
        Assert.assertEquals(nfa1.getAcceptingStates().contains(state1), nfa2.getAcceptingStates().contains(state2));

        List<Integer> inputs1 = new ArrayList<Integer>();
        inputs1.addAll(state1.getTransition().keySet());
        Collections.sort(inputs1);

        List<Integer> inputs2 = new ArrayList<Integer>();
        inputs2.addAll(state2.getTransition().keySet());
        Collections.sort(inputs2);

        Assert.assertEquals(inputs1, inputs2);

        for(int j = 0; j < inputs1.size(); j ++) {
            List<NFAState> nextStates1 = new ArrayList<NFAState>();
            nextStates1.addAll(state1.getTransition(inputs1.get(j)));
            Collections.sort(nextStates1);

            List<NFAState> nextStates2 = new ArrayList<NFAState>();
            nextStates2.addAll(state2.getTransition(inputs2.get(j)));
            Collections.sort(nextStates2);

            Assert.assertEquals(nextStates1.size(), nextStates2.size());

            for(int k = 0; k < nextStates1.size(); k ++) {
                assertEquivalent(nfa1, nextStates1.get(k), nfa2, nextStates2.get(k), checked);
            }
        }

        List<NFAState> epsilon1 = new ArrayList<NFAState>();
        epsilon1.addAll(state1.getEpsilonTransition());
        Collections.sort(epsilon1);

        List<NFAState> epsilon2 = new ArrayList<NFAState>();
        epsilon2.addAll(state2.getEpsilonTransition());
        Collections.sort(epsilon2);

        Assert.assertEquals(epsilon1.size(), epsilon2.size());
        for(int j = 0; j < epsilon1.size(); j ++) {
            assertEquivalent(nfa1, epsilon1.get(j), nfa2, epsilon2.get(j), checked);
        }

    }


//    public static void assertEquivalent(List<NFAState> s, List<NFAState> t) {
//        if (s.size() != t.size()) Assert.fail();
//        for(int index = 0; index < s.size(); index ++) {
//            if (s.get(index).getTransition().size() != t.get(index).getTransits().size()) Assert.fail();
//            for(int j = 0; j < s.get(index).getTransits().size(); j ++) {
//                System.out.println("Checking s&t[" + index + "].transits[" + j + "]");
//                if (s.get(index).getTransits().get(j).getTransitType() !=
//                        t.get(index).getTransits().get(j).getTransitType()) Assert.fail();
//                if (s.get(index).getTransits().get(j).getTransitType() == NFATransitType.BYTE
//                        && s.get(index).getTransits().get(j).getInput() != t.get(index).getTransits().get(j).getInput()) {
//                    Assert.fail();
//                }
//                if (s.get(index).getTransits().get(j).getTransitType() == NFATransitType.NONTERMINAL
//                        && !s.get(index).getTransits().get(j).getNonterminal().equals(
//                        t.get(index).getTransits().get(j).getNonterminal())) {
//                    Assert.fail();
//                }
//                NFAState nextS = s.get(index).getTransits().get(j).getNext();
//                NFAState nextT = t.get(index).getTransits().get(j).getNext();
//                int subscriptS = s.indexOf(nextS);//  NFAUtil.getSubscript(s, nextS);
//                int subscriptT = t.indexOf(nextT);//NFAUtil.getSubscript(t, nextT);
//                if (subscriptS < 0 || subscriptT < 0) Assert.fail();
//                if (subscriptS != subscriptT) Assert.fail();
//            }
//        }
//    }

}
