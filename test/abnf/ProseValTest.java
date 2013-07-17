package abnf;

import junit.framework.Assert;
import org.junit.Test;
import automata.NFA;
import automata.NFAState;
import automata.NFAStateFactory;
//import automata.NFATransitType;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 6/14/13
 * Time: 9:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProseValTest {

//		        prose-val      =  "<" *(%x20-3D / %x3F-7E) ">"
    @Test
    public void testToNFA() throws Exception {
        Tester<NFA> tester = new Tester<NFA>() {
            @Override
            public NFA test(AbnfParser parser) throws MatchException, IOException, CollisionException, IllegalAbnfException {
                ProseVal proseVal = parser.prose_val();
                return proseVal.toNFA(new HashMap<String, Rule>());
            }
        };

        NFA nfa;
        NFAState state;
        NFA expected;
        NFAState[] s;

        nfa = tester.test(AbnfParserFactory.newInstance("<>"));
        s = NFAStateFactory.newInstances(2);
        s[0].addTransit(s[1]);
        expected = new NFA(s[0], s[1]);
        Assertion.assertEquivalent(expected, nfa);

        nfa = tester.test(AbnfParserFactory.newInstance("<123>"));
        s = NFAStateFactory.newInstances(4);
        s[0].addTransit('1', s[2]).addTransit('2', s[3]).addTransit('3', s[1]);
        expected = new NFA(s[0], s[1]);
        Assertion.assertEquivalent(expected, nfa);

        nfa = tester.test(AbnfParserFactory.newInstance("<ABcD>"));
        s = NFAStateFactory.newInstances(5);
        s[0].addTransit('A', s[2]).addTransit('B', s[3]).addTransit('c', s[4]).addTransit('D', s[1]);
        expected = new NFA(s[0], s[1]);
        Assertion.assertEquivalent(expected, nfa);

        nfa = tester.test(AbnfParserFactory.newInstance("<9X#y>"));
        s = NFAStateFactory.newInstances(5);
        s[0].addTransit('9', s[2]).addTransit('X', s[3]).addTransit('#', s[4]).addTransit('y', s[1]);
        expected = new NFA(s[0], s[1]);
        Assertion.assertEquivalent(expected, nfa);

    }
}
