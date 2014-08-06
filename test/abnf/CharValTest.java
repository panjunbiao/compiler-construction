package abnf;

import java.util.*;

import org.junit.Test;
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

        nfa = tester.test(AbnfParser.newInstance("\"\""));
        s = NFAState.newInstances(2);
        s[0].addTransit(s[1]);
        expected = new NFA(s[0], s[1]);
        Assertion.assertEquivalent(expected, nfa);

        nfa = tester.test(AbnfParser.newInstance("\"123\""));
        s = NFAState.newInstances(4);
        s[0].addTransit('1', s[2]).addTransit('2', s[3]).addTransit('3', s[1]);
        expected = new NFA(s[0], s[1]);
        Assertion.assertEquivalent(expected, nfa);


        nfa = tester.test(AbnfParser.newInstance("\"ABcD\""));
        s = NFAState.newInstances(5);
        s[0].addTransit('A', s[2]).addTransit('B', s[3]).addTransit('c', s[4]).addTransit('D', s[1]);
        expected = new NFA(s[0], s[1]);
        Assertion.assertEquivalent(expected, nfa);

        nfa = tester.test(AbnfParser.newInstance("\"9X#y\""));
        s = NFAState.newInstances(5);
        s[0].addTransit('9', s[2]).addTransit('X', s[3]).addTransit('#', s[4]).addTransit('y', s[1]);
        expected = new NFA(s[0], s[1]);
        Assertion.assertEquivalent(expected, nfa);
    }
}
