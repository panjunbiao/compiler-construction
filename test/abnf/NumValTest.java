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
 * Time: 10:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class NumValTest {

    //		        num-val        =  "%" (bin-val / dec-val / hex-val)
    @Test
    public void testToNFA() throws MatchException, IOException, CollisionException, IllegalAbnfException {
        Tester<NFA> tester = new Tester<NFA>() {
            @Override
            public NFA test(AbnfParser parser) throws MatchException, IOException, CollisionException, IllegalAbnfException {
                Element numVal = parser.num_val();
                return numVal.toNFA(new HashMap<String, Rule>());
            }
        };

        NFA nfa;
        NFAState state;
        NFA expected;
        NFAState[] s;

        nfa = tester.test(AbnfParser.newInstance("%x00.11.eE.Ff"));

        nfa.getStartState().printToDot();

        s = NFAState.newInstances(5);
        s[0].addTransit(0x00, s[2])
            .addTransit(0x11, s[3])
            .addTransit(0xee, s[4])
            .addTransit(0xff, s[1]);
        expected = new NFA(s[0], s[1]);
        Assertion.assertEquivalent(expected, nfa);

        nfa = tester.test(AbnfParser.newInstance("%d56"));
        nfa.getStartState().printToDot();

        s = NFAState.newInstances(2);
        s[0].addTransit(56, s[1]);
        expected = new NFA(s[0], s[1]);
        Assertion.assertEquivalent(expected, nfa);

        nfa = tester.test(AbnfParser.newInstance("%b00.11.1111"));
        nfa.getStartState().printToDot();
        s = NFAState.newInstances(4);
        s[0].addTransit(0x00, s[2])
                .addTransit(0x03, s[3])
                .addTransit(0x0f, s[1]);
        expected = new NFA(s[0], s[1]);
        Assertion.assertEquivalent(expected, nfa);
    }
}
