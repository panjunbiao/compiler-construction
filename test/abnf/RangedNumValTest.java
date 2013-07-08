package abnf;

import junit.framework.Assert;
import org.junit.Test;
import automata.NFA;
import automata.NFAState;
import automata.NFATransitType;
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

        nfa = tester.test(AbnfParserFactory.newInstance("%x80-ff"));

        state = nfa.getStartState();
        Assert.assertEquals(128, state.getTransits().size());
        for(int index = 0; index < 128; index ++) {
            Assert.assertEquals(NFATransitType.BYTE, state.getTransits().get(index).getTransitType());
            Assert.assertEquals((index+128), state.getTransits().get(index).getInput());
        }
        for(int index = 1; index < 128; index ++) {
            Assert.assertEquals(state.getTransits().get(index-1).getNext(),
                    state.getTransits().get(index).getNext());
        }

        state = state.getTransits().get(0).getNext();
        Assert.assertTrue(nfa.getAcceptingStates().contains(state));
    }
}
