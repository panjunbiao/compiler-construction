package abnf;

import org.junit.Test;
import automata.NFA;
import automata.NFAState;
import automata.NFAStateFactory;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 6/17/13
 * Time: 3:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConcatenationTest {
    @Test
    public void testToNFA() throws Exception {
        Tester<NFA> tester = new Tester<NFA>() {
            @Override
            public NFA test(AbnfParser parser) throws MatchException, IOException, CollisionException, IllegalAbnfException {
                Concatenation concatenation = parser.concatenation();
                return concatenation.toNFA(new HashMap<String, Rule>());
            }
        };

        NFA nfa;
        NFAState state;
        NFA expected;
        NFAState[] s;

        nfa = tester.test(AbnfParserFactory.newInstance("%x11 \"1\" \"J\""));
        s = NFAStateFactory.newInstances(5);
        s[0].addTransit((byte)0x11, s[3]).addTransit('1', s[4]).addTransit('J', s[1]);
        expected = new NFA(s[0], s[1]);
        Assertion.assertEquivalent(expected, nfa);

        nfa = tester.test(AbnfParserFactory.newInstance("ABC [a b] (C/D) *E 1*3F 2*G %x11.22"));
        s = NFAStateFactory.newInstances(13);
        s[0]    .addTransit("ABC", s[2])
                .addTransit(s[3]);
        s[2]    .addTransit("a", s[4])
                .addTransit("b", s[3]);
        s[3]    .addTransit("C", s[5]);
        s[3]    .addTransit("D", s[5]);
        s[5]    .addTransit(s[6])
                .addTransit("E", s[6])
                .addTransit("F", s[8])
                .addTransit(s[7]);
        s[8]    .addTransit("F", s[9])
                .addTransit(s[7]);
        s[9]    .addTransit("F", s[7]);
        s[7]    .addTransit("G", s[11])
                .addTransit("G", s[10])
                .addTransit("G", s[10])
                .addTransit((byte)0x11, s[12])
                .addTransit((byte)0x22, s[1]);
        expected = new NFA(s[0], s[1]);
        Assertion.assertEquivalent(expected, nfa);
    }
}
