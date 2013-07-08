package abnf;

import junit.framework.Assert;
import org.junit.Test;
import automata.NFAState;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 7/3/13
 * Time: 8:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class HashSetTest {
    @Test
    public void testHashSet() {
        NFAState s1, s2, s3;
        s1 = new NFAState();
        s2 = new NFAState();
        s3 = new NFAState();
        Set<NFAState> ss1, ss2, ss3;
        ss1 = new HashSet<NFAState>();
        ss2 = new HashSet<NFAState>();
        ss3 = new HashSet<NFAState>();
        ss1.add(s1);
        ss1.add(s2);
        ss2.add(s1);
        ss2.add(s2);
        ss3.add(s1);
        ss3.add(s3);
        System.out.println("ss1.hashCode=" + ss1.hashCode());
        System.out.println("ss2.hashCode=" + ss2.hashCode());
        System.out.println("ss3.hashCode=" + ss3.hashCode());
        Assert.assertEquals(ss1, ss2);
        Assert.assertTrue(!ss1.equals(ss3));


    }

}
