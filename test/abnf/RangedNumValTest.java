package abnf;

import automata.NFAStateFactory;
import junit.framework.Assert;
import org.junit.Test;
import automata.NFA;
import automata.NFAState;
//import automata.NFATransitType;
//import automata.NFATransitType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 6/14/13
 * Time: 10:44 AM
 * To change this template use File | Settings | File Templates.
 */
public class RangedNumValTest {

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

        nfa = tester.test(AbnfParserFactory.newInstance("%x80-ff"));


        s = NFAStateFactory.newInstances(2);
        for(int input = 0x80; input <= 0xff; input ++) s[0].addTransit(input, s[1]);
        expected = new NFA(s[0], s[1]);
        Assertion.assertEquivalent(expected, nfa);
    }
}
