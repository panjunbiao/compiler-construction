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
import automata.NFATransitType;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.List;

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
        System.out.println("Comparing nfa1 and nfa2");
        System.out.println("................nfa1...............");
        nfa1.getStartState().printToDot();
        System.out.println("................nfa2...............");
        nfa2.getStartState().printToDot();
        List<NFAState> s1 = nfa1.getStateList();
        List<NFAState> s2 = nfa2.getStateList();
        assertEquivalent(s1, s2);
        Set<Integer> acc1 = new HashSet<Integer>();
        Set<Integer> acc2 = new HashSet<Integer>();
        Iterator<NFAState> it1 = nfa1.getAcceptingStates().iterator();
        while (it1.hasNext()) acc1.add(s1.indexOf(it1.next()));// NFAUtil.getSubscript(s1, it1.next()));
        Iterator<NFAState> it2 = nfa2.getAcceptingStates().iterator();
        while (it2.hasNext()) acc2.add(s2.indexOf(it2.next()));//NFAUtil.getSubscript(s2, it2.next()));
        if (!acc1.equals(acc2)) {
            System.out.println("nfa1.acceptingStates=" + acc1);
            System.out.println("nfa2.acceptingStates=" + acc2);
            Assert.fail();
        }
    }
    public static void assertEquivalent(List<NFAState> s, List<NFAState> t) {
        if (s.size() != t.size()) Assert.fail();
        for(int index = 0; index < s.size(); index ++) {
            if (s.get(index).getTransits().size() != t.get(index).getTransits().size()) Assert.fail();
            for(int j = 0; j < s.get(index).getTransits().size(); j ++) {
                System.out.println("Checking s&t[" + index + "].transits[" + j + "]");
                if (s.get(index).getTransits().get(j).getTransitType() !=
                        t.get(index).getTransits().get(j).getTransitType()) Assert.fail();
                if (s.get(index).getTransits().get(j).getTransitType() == NFATransitType.BYTE
                        && s.get(index).getTransits().get(j).getInput() != t.get(index).getTransits().get(j).getInput()) {
                    Assert.fail();
                }
                if (s.get(index).getTransits().get(j).getTransitType() == NFATransitType.NONTERMINAL
                        && !s.get(index).getTransits().get(j).getNonterminal().equals(
                        t.get(index).getTransits().get(j).getNonterminal())) {
                    Assert.fail();
                }
                NFAState nextS = s.get(index).getTransits().get(j).getNext();
                NFAState nextT = t.get(index).getTransits().get(j).getNext();
                int subscriptS = s.indexOf(nextS);//  NFAUtil.getSubscript(s, nextS);
                int subscriptT = t.indexOf(nextT);//NFAUtil.getSubscript(t, nextT);
                if (subscriptS < 0 || subscriptT < 0) Assert.fail();
                if (subscriptS != subscriptT) Assert.fail();
            }
        }
    }

}
