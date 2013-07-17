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

        nfa = tester.test(AbnfParserFactory.newInstance("%x30 [%x31 %x32] (%x33/%x34) *%x35 1*3%x36 2*%x37 %x11.22"));
        nfa.getStartState().printToDot();
        s = NFAStateFactory.newInstances(13);
        s[0]    .addTransit('0', s[2])
                .addTransit(s[3]);
        s[2]    .addTransit('1', s[4])
                .addTransit('2', s[3]);
        s[3]    .addTransit('3', s[5]);
        s[3]    .addTransit('4', s[5]);
        s[5]    .addTransit(s[6])
                .addTransit('5', s[6])
                .addTransit('6', s[8])
                .addTransit(s[7]);
        s[8]    .addTransit('6', s[9])
                .addTransit(s[7]);
        s[9]    .addTransit('6', s[7]);
        s[7]    .addTransit('7', s[11])
                .addTransit('7', s[10])
                .addTransit('7', s[10])
                .addTransit(0x11, s[12])
                .addTransit(0x22, s[1]);
        expected = new NFA(s[0], s[1]);
        Assertion.assertEquivalent(expected, nfa);
    }
}
