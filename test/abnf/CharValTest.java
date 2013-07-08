package abnf;

import java.util.*;

import junit.framework.Assert;
import org.junit.Test;
import abnf.*;
import automata.*;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 6/14/13
 * Time: 8:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class CharValTest {
    @Test
    public void testToNFA() throws Exception {
        Tester<NFA> tester = new Tester<NFA>() {
            @Override
            public NFA test(AbnfParser parser) throws MatchException, IOException, CollisionException, IllegalAbnfException {
                CharVal charVal = parser.char_val();
                return charVal.toNFA(new HashMap<String, Rule>());
            }
        };

        NFA nfa;
        NFAState state;
        NFA expected;
        NFAState[] s;

        nfa = tester.test(AbnfParserFactory.newInstance("\"\""));
        s = NFAStateFactory.newInstances(2);
        s[0].addTransit(s[1]);
        expected = new NFA(s[0], s[1]);
        Assertion.assertEquivalent(expected, nfa);

        nfa = tester.test(AbnfParserFactory.newInstance("\"123\""));
        s = NFAStateFactory.newInstances(4);
        s[0].addTransit('1', s[2]).addTransit('2', s[3]).addTransit('3', s[1]);
        expected = new NFA(s[0], s[1]);
        Assertion.assertEquivalent(expected, nfa);


        nfa = tester.test(AbnfParserFactory.newInstance("\"ABcD\""));
        s = NFAStateFactory.newInstances(5);
        s[0].addTransit('A', s[2]).addTransit('B', s[3]).addTransit('c', s[4]).addTransit('D', s[1]);
        expected = new NFA(s[0], s[1]);
        Assertion.assertEquivalent(expected, nfa);

//        state = nfa.getStartState();
//        Assert.assertEquals(2, state.getNFATransits().size());
//        Assert.assertEquals(NFATransitType.BYTE, state.getNFATransits().get(0).getTransitType());
//        Assert.assertEquals(NFATransitType.BYTE, state.getNFATransits().get(1).getTransitType());
//        Assert.assertEquals('A', state.getNFATransits().get(0).getInput());
//        Assert.assertEquals('a', state.getNFATransits().get(1).getInput());
//        Assert.assertEquals(state.getNFATransits().get(0).getNext(), state.getNFATransits().get(1).getNext());
//
//        state = state.getNFATransits().get(0).getNext();
//        Assert.assertEquals(2, state.getNFATransits().size());
//        Assert.assertEquals(NFATransitType.BYTE, state.getNFATransits().get(0).getTransitType());
//        Assert.assertEquals(NFATransitType.BYTE, state.getNFATransits().get(1).getTransitType());
//        Assert.assertEquals('B', state.getNFATransits().get(0).getInput());
//        Assert.assertEquals('b', state.getNFATransits().get(1).getInput());
//        Assert.assertEquals(state.getNFATransits().get(0).getNext(), state.getNFATransits().get(1).getNext());
//
//        state = state.getNFATransits().get(0).getNext();
//        Assert.assertEquals(2, state.getNFATransits().size());
//        Assert.assertEquals(NFATransitType.BYTE, state.getNFATransits().get(0).getTransitType());
//        Assert.assertEquals(NFATransitType.BYTE, state.getNFATransits().get(1).getTransitType());
//        Assert.assertEquals('C', state.getNFATransits().get(0).getInput());
//        Assert.assertEquals('c', state.getNFATransits().get(1).getInput());
//        Assert.assertEquals(state.getNFATransits().get(0).getNext(), state.getNFATransits().get(1).getNext());
//
//        state = state.getNFATransits().get(0).getNext();
//        Assert.assertEquals(2, state.getNFATransits().size());
//        Assert.assertEquals(NFATransitType.BYTE, state.getNFATransits().get(0).getTransitType());
//        Assert.assertEquals('D', state.getNFATransits().get(0).getInput());
//        Assert.assertEquals('d', state.getNFATransits().get(1).getInput());
//
//        state = state.getNFATransits().get(0).getNext();
//        Assert.assertEquals(0, state.getNFATransits().size());
//        Assert.assertTrue(nfa.getAcceptingStates().contains(state));

        nfa = tester.test(AbnfParserFactory.newInstance("\"9X#y\""));
        s = NFAStateFactory.newInstances(5);
        s[0].addTransit('9', s[2]).addTransit('X', s[3]).addTransit('#', s[4]).addTransit('y', s[1]);
        expected = new NFA(s[0], s[1]);
        Assertion.assertEquivalent(expected, nfa);

//        state = nfa.getStartState();
//        Assert.assertEquals(1, state.getNFATransits().size());
//        Assert.assertEquals(NFATransitType.BYTE, state.getNFATransits().get(0).getTransitType());
//        Assert.assertEquals('9', state.getNFATransits().get(0).getInput());
//
//        state = state.getNFATransits().get(0).getNext();
//        Assert.assertEquals(2, state.getNFATransits().size());
//        Assert.assertEquals(NFATransitType.BYTE, state.getNFATransits().get(0).getTransitType());
//        Assert.assertEquals(NFATransitType.BYTE, state.getNFATransits().get(1).getTransitType());
//        Assert.assertEquals('X', state.getNFATransits().get(0).getInput());
//        Assert.assertEquals('x', state.getNFATransits().get(1).getInput());
//        Assert.assertEquals(state.getNFATransits().get(0).getNext(), state.getNFATransits().get(1).getNext());
//
//        state = state.getNFATransits().get(0).getNext();
//        Assert.assertEquals(1, state.getNFATransits().size());
//        Assert.assertEquals(NFATransitType.BYTE, state.getNFATransits().get(0).getTransitType());
//        Assert.assertEquals('#', state.getNFATransits().get(0).getInput());
//
//        state = state.getNFATransits().get(0).getNext();
//        Assert.assertEquals(2, state.getNFATransits().size());
//        Assert.assertEquals(NFATransitType.BYTE, state.getNFATransits().get(0).getTransitType());
//        Assert.assertEquals('Y', state.getNFATransits().get(0).getInput());
//        Assert.assertEquals('y', state.getNFATransits().get(1).getInput());
//
//        state = state.getNFATransits().get(0).getNext();
//        Assert.assertEquals(0, state.getNFATransits().size());
//        Assert.assertTrue(nfa.getAcceptingStates().contains(state));

    }
}
