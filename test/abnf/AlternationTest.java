package abnf;

import org.junit.Test;
import automata.NFA;
import automata.NFAState;
//import automata.NFATransitType;

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
    private static final char DQUOTE = '"';

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

        nfa = tester.test(AbnfParser.newInstance("%x11"));
        s = NFAState.newInstances(2);
        s[0].addTransit(0x11, s[1]);
        expected = new NFA(s[0], s[1]);
        Assertion.assertEquivalent(expected, nfa);

        nfa = tester.test(AbnfParser.newInstance("%x11/%x31"));

        s = NFAState.newInstances(2);
        s[0]    .addTransit(0x11, s[1]);
        s[0]    .addTransit('1', s[1]);
        expected = new NFA(s[0], s[1]);
        Assertion.assertEquivalent(expected, nfa);

        nfa = tester.test(AbnfParser.newInstance("%x31/(%x32 %x33)/[%x34 %x35]"));
        s = NFAState.newInstances(12);
        s[0]    .addTransit('1', s[1]);
        s[0]    .addTransit('2', s[2])
                .addTransit('3', s[1]);
        s[0]    .addTransit(s[1]);
        s[0]    .addTransit('4', s[4])
                .addTransit('5', s[1]);
        expected = new NFA(s[0] ,s[1]);
        Assertion.assertEquivalent(expected, nfa);
    }
}
