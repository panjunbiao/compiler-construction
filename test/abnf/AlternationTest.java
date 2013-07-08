package abnf;

import junit.framework.Assert;
import org.junit.Test;
import automata.NFA;
import automata.NFAState;
import automata.NFAStateFactory;
import automata.NFATransitType;
import java.util.List;
import java.util.ArrayList;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 6/14/13
 * Time: 10:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class AlternationTest {
    @Test
    public void testToNFA() throws Exception {
        Tester<NFA> tester = new Tester<NFA>() {
            @Override
            public NFA test(AbnfParser parser) throws MatchException, IOException, CollisionException, IllegalAbnfException {
                Alternation alternation = parser.alternation();
                return alternation.toNFA(new HashMap<String, Rule>());
            }
        };

        NFA nfa;
        NFAState state;
        NFA expected;
        NFAState[] s;

        nfa = tester.test(AbnfParserFactory.newInstance("%x11"));
        s = NFAStateFactory.newInstances(2);
        s[0].addTransit((byte)0x11, s[1]);
        expected = new NFA(s[0], s[1]);
        Assertion.assertEquivalent(expected, nfa);

        nfa = tester.test(AbnfParserFactory.newInstance("%x11/\"1\""));

        s = NFAStateFactory.newInstances(2);
        s[0]    .addTransit((byte)0x11, s[1]);
        s[0]    .addTransit('1', s[1]);
        expected = new NFA(s[0], s[1]);

        Assertion.assertEquivalent(expected, nfa);

        nfa = tester.test(AbnfParserFactory.newInstance("ABC/(B C)/[E F]"));
        s = NFAStateFactory.newInstances(12);
        s[0]    .addTransit("ABC", s[1]);
        s[0]    .addTransit("B", s[2])
                .addTransit("C", s[1]);
        s[0]    .addTransit(s[1]);
        s[0]    .addTransit("E", s[4])
                .addTransit("F", s[1]);
        expected = new NFA(s[0] ,s[1]);
        Assertion.assertEquivalent(expected, nfa);
    }
}
