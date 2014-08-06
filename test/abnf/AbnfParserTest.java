/*
    This file is one of the component a Context-free Grammar Parser Generator,
    which accept a piece of text as the input, and generates a parser
    for the inputted context-free grammar.
    Copyright (C) 2013, Junbiao Pan (Email: panjunbiao@gmail.com)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package abnf;

import java.util.List;
import java.util.ArrayList;
import com.sun.xml.internal.bind.v2.TODO;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 5/29/13
 * Time: 5:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class AbnfParserTest {

    @Test
    public void testMatch() {
        AbnfParser parser = new AbnfParser("", null);
        Assert.assertTrue(parser.match(0x7f, 0x7f));
        Assert.assertFalse(parser.match(0x7f, 0x80));
        Assert.assertTrue(parser.match(0x7f, 0x7f, 0x7f));
        Assert.assertTrue(parser.match(0x7f, 0x00, 0xff));
        Assert.assertFalse(parser.match(0x7f, 0x80, 0xff));
        Assert.assertFalse(parser.match(0x7f, 0x00, 0x7e));
        Assert.assertTrue(parser.match(0x20, ' '));
        Assert.assertTrue(parser.match(0x12, new int[] {0x00, 0x22, 0x12, 0x23}));
        Assert.assertFalse(parser.match(0x12, new int[] {0x00, 0x22, 0x13, 0x23}));
    }

    //	   rulelist       =  1*( rule / (*c-wsp c-nl) )
    @Test
    public void testRulelist() throws Exception {
        Tester<List<Rule>> tester = new Tester<List<Rule>>() {
            @Override
            public List<Rule> test(AbnfParser parser) throws MatchException, IOException, CollisionException {
                return parser.rulelist();
            }
        };

        String input, rulesInput = "";
        input = "a=b" + (char)0x0d + (char)0x0a;
        rulesInput += input;
        List<Rule> rules = new ArrayList<Rule>();
        rules.add(AbnfParser.newInstance(input).rule());
        input = "b=*c" + (char)0x0d + (char)0x0a;
        rulesInput += input;
        rules.add(AbnfParser.newInstance(input).rule());
        input = "c=[d]" + (char)0x0d + (char)0x0a;
        rulesInput += input;
        rules.add(AbnfParser.newInstance(input).rule());
        input = "d=a/b/c/e" + (char)0x0d + (char)0x0a;
        rulesInput += input;
        rules.add(AbnfParser.newInstance(input).rule());
        input = "e=f/(g h)" + (char)0x0d + (char)0x0a;
        rulesInput += input;
        rules.add(AbnfParser.newInstance(input).rule());
        input = "g=i [j]" + (char)0x0d + (char)0x0a;
        rulesInput += input;
        rules.add(AbnfParser.newInstance(input).rule());
        input = "j=<abcd#1234>" + (char)0x0d + (char)0x0a;
        rulesInput += input;
        rules.add(AbnfParser.newInstance(input).rule());
        input = "j=/\"abcd#1234\"" + (char)0x0d + (char)0x0a;
        rulesInput += input;
        rules.add(AbnfParser.newInstance(input).rule());
        Assertion.assertMatch(rulesInput, tester, AbnfParser.newInstance(rulesInput).rulelist(), 1, 9);

    }

    //		        rule           =  rulename defined-as elements c-nl
    @Test
    public void testRule() throws Exception {
        Tester<Rule> tester = new Tester<Rule>() {
            @Override
            public Rule test(AbnfParser parser) throws MatchException, IOException {
                return parser.rule();
            }
        };

        String input;
        Elements elements;
        elements = AbnfParser.newInstance("b").elements();
        Assertion.assertMatch("a=b" + (char)0x0D + (char)0x0A, tester, new Rule(new RuleName("a"), "=", elements), 1, 2);
        Assertion.assertMatch("a=/b" + (char)0x0D + (char)0x0A, tester, new Rule(new RuleName("a"), "=/", elements), 1, 2);

        elements = AbnfParser.newInstance("b/c").elements();
        Assertion.assertMatch("a=b/c" + (char)0x0D + (char)0x0A, tester, new Rule(new RuleName("a"), "=", elements), 1, 2);
        Assertion.assertMatch("a=/b/c" + (char)0x0D + (char)0x0A, tester, new Rule(new RuleName("a"), "=/", elements), 1, 2);

        elements = AbnfParser.newInstance("b c d").elements();
        Assertion.assertMatch("a=b c d" + (char)0x0D + (char)0x0A, tester, new Rule(new RuleName("a"), "=", elements), 1, 2);
        Assertion.assertMatch("a=/b c d" + (char)0x0D + (char)0x0A, tester, new Rule(new RuleName("a"), "=/", elements), 1, 2);

        elements = AbnfParser.newInstance("[b]").elements();
        Assertion.assertMatch("a=[b]" + (char)0x0D + (char)0x0A, tester, new Rule(new RuleName("a"), "=", elements), 1, 2);
        Assertion.assertMatch("a=/[b]" + (char)0x0D + (char)0x0A, tester, new Rule(new RuleName("a"), "=/", elements), 1, 2);

        elements = AbnfParser.newInstance("*b").elements();
        Assertion.assertMatch("a=*b" + (char)0x0D + (char)0x0A, tester, new Rule(new RuleName("a"), "=", elements), 1, 2);
        Assertion.assertMatch("a=/*b" + (char)0x0D + (char)0x0A, tester, new Rule(new RuleName("a"), "=/", elements), 1, 2);
    }

    //		        rulename       =  ALPHA *(ALPHA / DIGIT / "-")
    @Test
    public void testRulename() throws Exception {
        Tester<String> tester = new Tester<String>() {
            public String test(AbnfParser parser) throws MatchException, IOException {
                return parser.rulename().toString();
            }
        };

        Assert.assertEquals("A", AbnfParser.newInstance("A").rulename().toString());
        Assert.assertEquals("a", AbnfParser.newInstance("a").rulename().toString());
        Assert.assertEquals("Z", AbnfParser.newInstance("Z").rulename().toString());
        Assert.assertEquals("z", AbnfParser.newInstance("z").rulename().toString());
        Assert.assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ", AbnfParser.newInstance("ABCDEFGHIJKLMNOPQRSTUVWXYZ").rulename().toString());
        Assert.assertEquals("abcdefghijklmnopqrstuvwxyz", AbnfParser.newInstance("abcdefghijklmnopqrstuvwxyz").rulename().toString());
        Assert.assertEquals("AbCdEfGhIjKlMnOpQrStUvWxYz", AbnfParser.newInstance("AbCdEfGhIjKlMnOpQrStUvWxYz").rulename().toString());
        Assert.assertEquals("aBcDeFgHiJkLmNoPqRsTuVwXyZ", AbnfParser.newInstance("aBcDeFgHiJkLmNoPqRsTuVwXyZ").rulename().toString());
        Assert.assertEquals("A1234567890Z", AbnfParser.newInstance("A1234567890Z").rulename().toString());
        Assert.assertEquals("a1234567890z", AbnfParser.newInstance("a1234567890z").rulename().toString());
        Assert.assertEquals("A1B2C3D4e5f6g7h8i9j0", AbnfParser.newInstance("A1B2C3D4e5f6g7h8i9j0").rulename().toString());
        Assert.assertEquals("A1B2C3D4e5f6g7h8i9j0aBcDeFgHiJkLmNoPqRsTuVwXyZAbCdEfGhIjKlMnOpQrStUvWxYz", AbnfParser.newInstance("A1B2C3D4e5f6g7h8i9j0aBcDeFgHiJkLmNoPqRsTuVwXyZAbCdEfGhIjKlMnOpQrStUvWxYz").rulename().toString());
        Assert.assertEquals("A1b2C3-4d-", AbnfParser.newInstance("A1b2C3-4d-").rulename().toString());
        Assert.assertEquals("a-------------", AbnfParser.newInstance("a-------------").rulename().toString());
        Assert.assertEquals("A1b2C3-4d-", AbnfParser.newInstance("A1b2C3-4d-#").rulename().toString());
        Assert.assertEquals("A1b2C3-4d-", AbnfParser.newInstance("A1b2C3-4d-..").rulename().toString());
        Assert.assertEquals("RFC3261-A1b2C3-4d-", AbnfParser.newInstance("RFC3261-", "A1b2C3-4d-*&^").rulename().toString());
        Assertion.assertMatchException("1", tester, 1, 1);
        Assertion.assertMatchException("1234567890", tester, 1, 1);
        Assertion.assertMatchException("-", tester, 1, 1);
        Assertion.assertMatchException("-----------", tester, 1, 1);
        Assertion.assertMatchException("#", tester, 1, 1);
        Assertion.assertMatchException(".", tester, 1, 1);
        Assertion.assertMatchException("~", tester, 1, 1);
        Assertion.assertMatchException(")", tester, 1, 1);
        Assertion.assertMatchException("", tester, 1, 1);
    }

    //WSP            =  SP / HTAB
    //  VCHAR          =  %x21-7E
    //    comment        =  ";" *(WSP / VCHAR) CRLF
    //		        c-nl           =  comment / CRLF
    //    c-wsp          =  WSP / (c-nl WSP)
    //		        defined-as     =  *c-wsp ("=" / "=/") *c-wsp
    @Test
    public void testDefined_as() throws Exception {
        Tester<String> tester = new Tester() {
            public String test(AbnfParser parser) throws MatchException, IOException {
                return parser.defined_as();
            }
        };

        Assertion.assertMatchException("", tester, 1, 1);
        Assertion.assertMatch("=", tester, "=", 2, 1);
        Assertion.assertMatchException("/", tester, 1, 1);
        Assertion.assertMatch("=A", tester, "=", 2, 1);
        Assertion.assertMatchException("A=", tester, 1, 1);
        Assertion.assertMatch("=/", tester, "=/", 3, 1);
        Assertion.assertMatchException(".=/", tester, 1, 1);
        Assertion.assertMatch("=/#", tester, "=/", 3, 1);
        Assertion.assertMatchException("#=/", tester, 1, 1);
        Assertion.assertMatch("    =    ", tester, "=", 10, 1);
        Assertion.assertMatch("    =    =", tester, "=", 10, 1);
        Assertion.assertMatch("    =/    ", tester, "=/", 11, 1);
        Assertion.assertMatch("    =/    3", tester, "=/", 11, 1);
        Assertion.assertMatch("" + (char)0x0D + (char)0x0A +" " + "=", tester, "=", 3, 2);
        Assertion.assertMatchException("" + (char)0x0D + (char)0x0A + "=", tester, 1, 2);
        Assertion.assertMatch("" + (char) 0x0D + (char) 0x0A + " " + "==", tester, 3, 2);

//        Can not handle following case
//      TODO
        System.out.println("***************************");
        Assertion.assertMatchException("" + (char) 0x0D + (char) 0x0A + " " + "=;", tester, 4, 2);
        Assertion.assertMatch("" + (char)0x0D + (char)0x0A +" " + "=" + (char)0x0D + (char)0x0A +" ", tester, "=", 2, 3);

//        Can not handle following case
//      TODO
        Assertion.assertMatchException("" + (char) 0x0D + (char) 0x0A + " " + "=" + (char) 0x0D + (char) 0x0A, tester, 1, 3);

        Assertion.assertMatch("" + (char)0x0D + (char)0x0A +" " + "=" + (char)0x0D + (char)0x0A +" =", tester, "=", 2, 3);
        Assertion.assertMatch("" + (char)0x0D + (char)0x0A +" " + "=/", tester, "=/", 4, 2);
        Assertion.assertMatch("" + (char) 0x0D + (char) 0x0A + " " + "=//", tester, "=/", 4, 2);
        Assertion.assertMatch("" + (char) 0x0D + (char) 0x0A + " " + "=/" + (char) 0x0D + (char) 0x0A + " ", tester, "=/", 2, 3);
        Assertion.assertMatch("" + (char) 0x0D + (char) 0x0A + " " + "=/" + (char) 0x0D + (char) 0x0A + " ==", tester, "=/", 2, 3);
        Assertion.assertMatch("" + (char)0x0D + (char)0x0A +(char)0x09 + "=", tester, "=", 3, 2);
        Assertion.assertMatch("" + (char)0x0D + (char)0x0A +(char)0x09 + "====", tester, 3, 2);
        Assertion.assertMatch("" + (char)0x0D + (char)0x0A +(char)0x09 + "=" + (char)0x0D + (char)0x0A +(char)0x09, tester, "=", 2, 3);
        Assertion.assertMatch("" + (char)0x0D + (char)0x0A +(char)0x09 + "=" + (char)0x0D + (char)0x0A +(char)0x09 + "=", tester, "=", 2, 3);
        Assertion.assertMatch("" + (char)0x0D + (char)0x0A +(char)0x09 + "=/", tester, "=/", 4, 2);
        Assertion.assertMatch("" + (char)0x0D + (char)0x0A +(char)0x09 + "=/=/", tester, "=/", 4, 2);
        Assertion.assertMatch("" + (char)0x0D + (char)0x0A +(char)0x09 + "=/" + (char)0x0D + (char)0x0A +(char)0x09, tester, "=/", 2, 3);
        Assertion.assertMatch("" + (char)0x0D + (char)0x0A +(char)0x09 + "=/" + (char)0x0D + (char)0x0A +(char)0x09 + "/=/=/", tester, "=/", 2, 3);
        Assertion.assertMatch("" + (char)0x0D + (char)0x0A +(char)0x09 + (char)0x0D + (char)0x0A +(char)0x09 + "=", tester, "=", 3, 3);
        Assertion.assertMatch("" + (char)0x0D + (char)0x0A +(char)0x09 + (char)0x0D + (char)0x0A +(char)0x09 + "==/=/=/", tester, "=", 3, 3);
        Assertion.assertMatch("" + (char)0x0D + (char)0x0A +(char)0x09 + (char)0x0D + (char)0x0A +(char)0x09 + "=" + (char)0x0D + (char)0x0A +(char)0x09 + (char)0x0D + (char)0x0A +(char)0x09, tester, "=", 2, 5);
        Assertion.assertMatch("" + (char)0x0D + (char)0x0A +(char)0x09 + (char)0x0D + (char)0x0A +(char)0x09 + "=" + (char)0x0D + (char)0x0A +(char)0x09 + (char)0x0D + (char)0x0A +(char)0x09 + "/", tester, "=", 2, 5);
        Assertion.assertMatch("" + (char) 0x0D + (char) 0x0A + (char) 0x09 + (char) 0x0D + (char) 0x0A + (char) 0x09 + "=/", tester, "=/", 4, 3);
        Assertion.assertMatch("" + (char) 0x0D + (char) 0x0A + (char) 0x09 + (char) 0x0D + (char) 0x0A + (char) 0x09 + "=/=", tester, "=/", 4, 3);
        Assertion.assertMatch("" + (char) 0x0D + (char) 0x0A + (char) 0x09 + (char) 0x0D + (char) 0x0A + (char) 0x09 + "=/" + (char) 0x0D + (char) 0x0A + (char) 0x09 + (char) 0x0D + (char) 0x0A + (char) 0x09, tester, "=/", 2, 5);

//        Can not handle following case
//      TODO
        Assertion.assertMatchException(
                "" + (char) 0x0D + (char) 0x0A + (char) 0x09 + (char) 0x0D + (char) 0x0A + (char) 0x09
                        + "=/" + (char) 0x0D + (char) 0x0A + (char) 0x09 + (char) 0x0D + (char) 0x0A,
                tester, 1, 5
                );

        Assertion.assertMatch(
                "" + (char) 0x0D + (char) 0x0A + (char) 0x09 + (char) 0x0D + (char) 0x0A + (char) 0x09
                        + "=/" + (char) 0x0D + (char) 0x0A + (char) 0x09 + (char) 0x0D + (char) 0x0A + (char) 0x09 + "=",
                tester, "=/", 2, 5);

        Assertion.assertMatch("" + (char)0x09 + (char)0x09 + "=", tester, "=", 4, 1);
        Assertion.assertMatch("" + (char)0x09 + (char)0x09 + "==/", tester, "=", 4, 1);
        Assertion.assertMatch("" + (char)0x09 + (char)0x09 + "=" + (char)0x09 + (char)0x09, tester, "=", 6, 1);
        Assertion.assertMatch("" + (char)0x09 + (char)0x09 + "=/", tester, "=/", 5, 1);
        Assertion.assertMatch("" + (char)0x09 + (char)0x09 + "=/=", tester, "=/", 5, 1);
        Assertion.assertMatch("" + (char) 0x09 + (char) 0x09 + "=/" + (char) 0x09 + (char) 0x09, tester, "=/", 7, 1);
        Assertion.assertMatch("" + (char) 0x09 + (char) 0x09 + "=/" + (char) 0x09 + (char) 0x09 + "=", tester, "=/", 7, 1);
        Assertion.assertMatch(
                ";  ; ; \"" + (char) 0x0D + (char) 0x0A + " " + "=" + ";  ; ; \"" + (char) 0x0D + (char) 0x0A + " ",
                tester, "=", 2, 3);
        Assertion.assertMatch(
                ";  ; ; \"" + (char)0x0D + (char)0x0A + " " + "=" + ";  ; ; \"" + (char)0x0D + (char)0x0A + " /",
                tester, "=", 2, 3);

        Assertion.assertMatch(
                ";  ; ; \"" + (char) 0x0D + (char) 0x0A + " " + "=/" + ";  ; ; \"" + (char) 0x0D + (char) 0x0A + " ",
                tester, "=/", 2, 3);

        Assertion.assertMatch(
                ";  ; ; \"" + (char)0x0D + (char)0x0A + " " + "=/" + ";  ; ; \"" + (char)0x0D + (char)0x0A + " =/",
                tester, "=/", 2, 3);
    }

    //		        elements       =  alternation *c-wsp
    @Test
    public void testElements() throws Exception {
        Tester<Elements> tester = new Tester<Elements>() {
            @Override
            public Elements test(AbnfParser parser) throws MatchException, IOException {
                return parser.elements();
            }
        };

        String input;
        input = "A/B/C";
        Assertion.assertMatch(input, tester, AbnfParser.newInstance(input).elements(), 6, 1);

//        TODO
        input = "A/B/C  ";
        Assertion.assertMatchException(input, tester, 8, 1);

    }

    //  HTAB           =  %x09
    @Test
    public void testHTAB() throws Exception {
        Tester<String> tester = new Tester<String>() {
            @Override
            public String test(AbnfParser parser) throws MatchException, IOException {
                return parser.HTAB();
            }
        };

        Assert.assertEquals(String.valueOf((char)0x09), AbnfParser.newInstance(new char[] {0x09}).HTAB());
        Assert.assertEquals(String.valueOf((char) 0x09), AbnfParser.newInstance(new char[]{0x09, 0x09}).HTAB());
        Assertion.assertMatchException("" + (char) 0x0D, tester, 1, 1);
        Assertion.assertMatchException("", tester, 1, 1);

    }

    //  LF             =  %x0A
    @Test
    public void testLF() throws Exception {
        Tester<String> tester = new Tester<String>() {
            @Override
            public String test(AbnfParser parser) throws MatchException, IOException {
                return parser.LF();
            }
        };
        Assert.assertEquals(String.valueOf((char)0x0A), AbnfParser.newInstance(new char[] {0x0A}).LF());
        Assert.assertEquals(String.valueOf((char) 0x0A), AbnfParser.newInstance(new char[]{0x0A, 0x0A}).LF());
        Assertion.assertMatchException("", tester, 1, 1);
        Assertion.assertMatchException("" + (char)0x0D, tester, 1, 1);
    }

    //  SP             =  %x20
    @Test
    public void testSP() throws Exception {
        Tester<String> tester = new Tester<String>() {
            @Override
            public String test(AbnfParser parser) throws MatchException, IOException {
                return parser.SP();
            }
        };
        Assert.assertEquals(String.valueOf((char)0x20), AbnfParser.newInstance(new char[] {0x20, 0x20}).SP());

        Assertion.assertMatchException("", tester, 1, 1);
        Assertion.assertMatchException("" + (char)0x0D, tester, 1, 1);

    }

    //WSP            =  SP / HTAB
    @Test
    public void testWSP() throws Exception {
        Tester<String> tester = new Tester<String>() {
            @Override
            public String test(AbnfParser parser) throws MatchException, IOException {
                return parser.WSP();
            }
        };
        Assert.assertEquals(String.valueOf((char)0x09), AbnfParser.newInstance(new char[] {0x09}).WSP());
        Assert.assertEquals(String.valueOf((char)0x20), AbnfParser.newInstance(new char[] {0x20}).WSP());
        Assert.assertEquals(String.valueOf((char)0x09), AbnfParser.newInstance(new char[] {0x09, 0x09}).WSP());
        Assert.assertEquals(String.valueOf((char)0x09), AbnfParser.newInstance(new char[] {0x09, 0x20}).WSP());
        Assert.assertEquals(String.valueOf((char)0x20), AbnfParser.newInstance(new char[] {0x20, 0x20}).WSP());
        Assert.assertEquals(String.valueOf((char)0x20), AbnfParser.newInstance(new char[] {0x20, 0x09}).WSP());
        Assert.assertEquals(String.valueOf((char)0x20), AbnfParser.newInstance(new char[] {0x20, 0x30}).WSP());
        Assertion.assertMatchException("", tester, 1, 1);
        Assertion.assertMatchException("" + (char)0x08, tester, 1, 1);
    }

    //  CR             =  %x0D
    @Test
    public void testCR() throws Exception {
        Tester<String> tester = new Tester<String>() {
            @Override
            public String test(AbnfParser parser) throws MatchException, IOException {
                return parser.CR();
            }
        };
        Assert.assertEquals(String.valueOf((char) 0x0D), AbnfParser.newInstance(new char[]{0x0D, 0x0D}).CR());
        Assertion.assertMatchException("", tester, 1, 1);
        Assertion.assertMatchException("" + (char)0x0A, tester, 1, 1);
    }

    //  CRLF           =  CR LF
    @Test
    public void testCRLF() throws Exception {
        Tester<String> tester = new Tester<String>() {
            @Override
            public String test(AbnfParser parser) throws MatchException, IOException {
                return parser.CRLF();
            }
        };
        Assert.assertEquals(String.valueOf((char)0x0D) + String.valueOf((char)0x0A), AbnfParser.newInstance(new char[] {0x0D, 0x0A}).CRLF());
        Assert.assertEquals(String.valueOf((char)0x0D) + String.valueOf((char)0x0A), AbnfParser.newInstance(new char[] {0x0D, 0x0A, 0x0C}).CRLF());
        Assert.assertEquals(String.valueOf((char)0x0D) + String.valueOf((char)0x0A), AbnfParser.newInstance(new char[] {0x0D, 0x0A, 0x0D}).CRLF());
        Assert.assertEquals(String.valueOf((char)0x0D) + String.valueOf((char)0x0A), AbnfParser.newInstance(new char[] {0x0D, 0x0A, 0x0A}).CRLF());
        Assertion.assertMatchException("", tester, 1, 1);
        Assertion.assertMatchException("" + (char)0x0D, tester, 1, 1);
        Assertion.assertMatchException("" + (char)0x0A, tester, 1, 1);
    }

    //WSP            =  SP / HTAB
    //  VCHAR          =  %x21-7E
    //    comment        =  ";" *(WSP / VCHAR) CRLF
    //		        c-nl           =  comment / CRLF
    //    c-wsp          =  WSP / (c-nl WSP)
    @Test
    public void testC_wsp() throws MatchException, IOException, Exception {
        Tester<String> tester = new Tester() {
            public String test(AbnfParser parser) throws MatchException, IOException {
                return parser.c_wsp();
            }
        };

        Assertion.assertMatch("" + (char)0x20, tester, 2, 1);
        Assertion.assertMatch("" + (char)0x09, tester, 2, 1);
//        TODO
//        Can not handle this case
        Assertion.assertMatchException("" + (char)0x0D + (char)0x09, tester, 1, 1);
        Assertion.assertMatch("" + (char)0x20 + (char)0x20, tester, 2, 1);
        Assertion.assertMatch("" + (char)0x0D + (char)0x0A + " ", tester, 2, 2);
        Assertion.assertMatch("" + (char)0x0D + (char)0x0A + "  ", tester, 2, 2);
        Assertion.assertMatch(";        " + (char)0x0D + (char)0x0A + " ", tester, 2, 2);
        Assertion.assertMatch(";AbCd123\"" + (char)0x0D + (char)0x0A + " ", tester, 2, 2);
        Assertion.assertMatch(";        " + (char)0x0D + (char)0x0A + " " + (char)0x0D + (char)0x0A, tester, 2, 2);
    }

    //WSP            =  SP / HTAB
    //  VCHAR          =  %x21-7E
    //    comment        =  ";" *(WSP / VCHAR) CRLF
    //		        c-nl           =  comment / CRLF
    @Test
    public void testC_nl() throws Exception {
        Tester<String> tester = new Tester() {
            public String test(AbnfParser parser) throws MatchException, IOException {
                return parser.c_nl();
            }
        };
        Assertion.assertMatch("" + (char)0x0D + (char)0x0A, tester, 1, 2);
        Assertion.assertMatch(";" + (char)0x0D + (char)0x0A, tester, 1, 2);
        Assertion.assertMatch(";" + (char)0x21 + (char)0x0D + (char)0x0A, tester, 1, 2);
        Assertion.assertMatch(";" + (char)0x20 + (char)0x0D + (char)0x0A, tester, 1, 2);
    }

    //  VCHAR          =  %x21-7E
    @Test
    public void testVCHAR() throws Exception {
        Tester<String> tester = new Tester() {
            public String test(AbnfParser parser) throws MatchException, IOException {
                return parser.VCHAR();
            }
        };

        Assert.assertEquals(String.valueOf((char)0x21), AbnfParser.newInstance(new char[] {0x21}).VCHAR());
        Assert.assertEquals(String.valueOf((char)0x22), AbnfParser.newInstance(new char[] {0x22}).VCHAR());
        Assert.assertEquals(String.valueOf((char)0x7E), AbnfParser.newInstance(new char[] {0x7E}).VCHAR());
        Assert.assertEquals("B", AbnfParser.newInstance("B").VCHAR());
        Assertion.assertMatchException("", tester, 1, 1);
        Assertion.assertMatchException(" ", tester, 1, 1);
        Assertion.assertMatchException("" + (char)0x7F, tester, 1, 1);
    }

    //  VCHAR          =  %x21-7E
    //    comment        =  ";" *(WSP / VCHAR) CRLF
    @Test
    public void testComment() throws Exception {
        Tester<String> tester = new Tester() {
            public String test(AbnfParser parser) throws MatchException, IOException {
                return parser.comment();
            }
        };
        Assertion.assertMatch(";" + (char)0x0D + (char)0x0A, tester, 1, 2);
        Assertion.assertMatch(";" + (char)0x7E + (char)0x0D + (char)0x0A, tester, 1, 2);
        Assertion.assertMatch(";" + "   " + (char)0x0D + (char)0x0A, tester, 1, 2);
        Assertion.assertMatchException(";", tester, 2, 1);
        Assertion.assertMatchException(" ", tester, 1, 1);
        Assertion.assertMatchException(";" + (char) 0x0D, tester, 1, 1);
    }

    //		        alternation    =  concatenation
//		                          *(*c-wsp "/" *c-wsp concatenation)
    @Test
    public void testAlternation() throws Exception {
        Tester<Alternation> tester = new Tester<Alternation>() {
            @Override
            public Alternation test(AbnfParser parser) throws MatchException, IOException {
                return parser.alternation();
            }
        };

        Alternation alternation;
        String input;
        input = "A B C %xff \"abc\" <12-34>";
        alternation = new Alternation();
        alternation.addConcatenation(AbnfParser.newInstance(input).concatenation());
        alternation.addConcatenation(AbnfParser.newInstance(input).concatenation());
        Assertion.assertMatch(input + "/" + input, tester, alternation, 50, 1);

        input = "A";
        alternation = new Alternation();
        alternation.addConcatenation(AbnfParser.newInstance(input).concatenation());
        alternation.addConcatenation(AbnfParser.newInstance(input).concatenation());
        Assertion.assertMatch(input + "/" + input, tester, alternation, 4, 1);

        input = "A";
        alternation = new Alternation();
        alternation.addConcatenation(AbnfParser.newInstance(input).concatenation());
        alternation.addConcatenation(AbnfParser.newInstance(input).concatenation());
//        TODO
//        Does not support currently
        Assertion.assertMatchException(input + " / " + input, tester, 3, 1);

        input = "(A B C D E)";
        alternation = new Alternation();
        alternation.addConcatenation(AbnfParser.newInstance(input).concatenation());
        alternation.addConcatenation(AbnfParser.newInstance(input).concatenation());
        Assertion.assertMatch(input + "/" + input, tester, alternation, 24, 1);

        input = "[A B C D E]";
        alternation = new Alternation();
        alternation.addConcatenation(AbnfParser.newInstance(input).concatenation());
        alternation.addConcatenation(AbnfParser.newInstance(input).concatenation());
        Assertion.assertMatch(input + "/" + input, tester, alternation, 24, 1);

        input = "*(A B C)";
        alternation = new Alternation();
        alternation.addConcatenation(AbnfParser.newInstance(input).concatenation());
        alternation.addConcatenation(AbnfParser.newInstance(input).concatenation());
        Assertion.assertMatch(input + "/" + input, tester, alternation, 18, 1);

        input = "1*2(A B C)";
        alternation = new Alternation();
        alternation.addConcatenation(AbnfParser.newInstance(input).concatenation());
        alternation.addConcatenation(AbnfParser.newInstance(input).concatenation());
        Assertion.assertMatch(input + "/" + input, tester, alternation, 22, 1);

    }

    //		        concatenation  =  repetition *(1*c-wsp repetition)
    @Test
    public void testConcatenation() throws Exception {
        Tester<Concatenation> tester = new Tester<Concatenation>() {
            @Override
            public Concatenation test(AbnfParser parser) throws MatchException, IOException {
                return parser.concatenation();
            }
        };

        String input;
        input = "a b c *a 1*b 1*2c *3d %d88 %x11.22.33 %b00-1111 *(aa bb) *2[a b c] 5*(a/b)";
        Assertion.assertMatch(input, tester, AbnfParser.newInstance(input).concatenation(), 75, 1);
        input = "a b  c  d    e";
        Assertion.assertMatch(input, tester, AbnfParser.newInstance(input).concatenation(), 15, 1);

//        TODO
//        Does not support currently
        input = " a ";
        Assertion.assertMatchException(input, tester, 1, 1);
    }

    //		        element        =  rulename / group / option /
//		                          char-val / num-val / prose-val
    //		        repetition     =  [repeat] element
//    DIGIT          =  %x30-39
    @Test
    public void testRepetition() throws Exception {
        Tester<Repetition> tester = new Tester<Repetition>() {
            @Override
            public Repetition test(AbnfParser parser) throws MatchException, IOException {
                return parser.repetition();
            }
        };
        Assertion.assertMatch(
                "B", tester,
                new Repetition(new RuleName("", "B")),
                2, 1);
        Assertion.assertMatch("1B", tester,
                new Repetition(new Repeat(1, 1), new RuleName("", "B")),
                3, 1);
        Assertion.assertMatch("2*6B", tester,
                new Repetition(new Repeat(2, 6), new RuleName("", "B")),
                5, 1);

        Assertion.assertMatch("3*B", tester,
                new Repetition(new Repeat(3, 0), new RuleName("", "B")),
                4, 1);

        Assertion.assertMatch("*8B", tester,
                new Repetition(new Repeat(0, 8), new RuleName("", "B")),
                4, 1);

        Assertion.assertMatch("*B", tester,
                new Repetition(new Repeat(0, 0), new RuleName("", "B")),
                3, 1);


        Option option = AbnfParser.newInstance("[B]").option();

        Assertion.assertMatch(
                "[B]", tester,
                new Repetition(option),
                4, 1);
        Assertion.assertMatch("1[B]", tester,
                new Repetition(new Repeat(1, 1), option),
                5, 1);
        Assertion.assertMatch("2*6[B]", tester,
                new Repetition(new Repeat(2, 6), option),
                7, 1);

        Assertion.assertMatch("3*[B]", tester,
                new Repetition(new Repeat(3, 0), option),
                6, 1);

        Assertion.assertMatch("*8[B]", tester,
                new Repetition(new Repeat(0, 8), option),
                6, 1);

        Assertion.assertMatch("*[B]", tester,
                new Repetition(new Repeat(0, 0), option),
                5, 1);


        Group group = AbnfParser.newInstance("(B)").group();

        Assertion.assertMatch(
                "(B)", tester,
                new Repetition(group),
                4, 1);
        Assertion.assertMatch("1(B)", tester,
                new Repetition(new Repeat(1, 1), group),
                5, 1);
        Assertion.assertMatch("2*6(B)", tester,
                new Repetition(new Repeat(2, 6), group),
                7, 1);

        Assertion.assertMatch("3*(B)", tester,
                new Repetition(new Repeat(3, 0), group),
                6, 1);

        Assertion.assertMatch("*8(B)", tester,
                new Repetition(new Repeat(0, 8), group),
                6, 1);

        Assertion.assertMatch("*(B)", tester,
                new Repetition(new Repeat(0, 0), group),
                5, 1);


        CharVal charVal = AbnfParser.newInstance("\"ABC\"").char_val();

        Assertion.assertMatch(
                "\"ABC\"", tester,
                new Repetition(charVal),
                6, 1);
        Assertion.assertMatch("1\"ABC\"", tester,
                new Repetition(new Repeat(1, 1), charVal),
                7, 1);
        Assertion.assertMatch("2*6\"ABC\"", tester,
                new Repetition(new Repeat(2, 6), charVal),
                9, 1);

        Assertion.assertMatch("3*\"ABC\"", tester,
                new Repetition(new Repeat(3, 0), charVal),
                8, 1);

        Assertion.assertMatch("*8\"ABC\"", tester,
                new Repetition(new Repeat(0, 8), charVal),
                8, 1);

        Assertion.assertMatch("*\"ABC\"", tester,
                new Repetition(new Repeat(0, 0), charVal),
                7, 1);


        Element numVal = AbnfParser.newInstance("%x00-FF").num_val();

        Assertion.assertMatch(
                "%x00-FF", tester,
                new Repetition(numVal),
                8, 1);
        Assertion.assertMatch("1%x00-FF", tester,
                new Repetition(new Repeat(1, 1), numVal),
                9, 1);
        Assertion.assertMatch("2*6%x00-FF", tester,
                new Repetition(new Repeat(2, 6), numVal),
                11, 1);

        Assertion.assertMatch("3*%x00-FF", tester,
                new Repetition(new Repeat(3, 0), numVal),
                10, 1);

        Assertion.assertMatch("*8%x00-FF", tester,
                new Repetition(new Repeat(0, 8), numVal),
                10, 1);

        Assertion.assertMatch("*%x00-FF", tester,
                new Repetition(new Repeat(0, 0), numVal),
                9, 1);

        ProseVal proseVal = AbnfParser.newInstance("<ABC>").prose_val();

        Assertion.assertMatch(
                "<ABC>", tester,
                new Repetition(proseVal),
                6, 1);

        Assertion.assertMatch("1<ABC>", tester,
                new Repetition(new Repeat(1, 1), proseVal),
                7, 1);

        Assertion.assertMatch("2*6<ABC>", tester,
                new Repetition(new Repeat(2, 6), proseVal),
                9, 1);

        Assertion.assertMatch("3*<ABC>", tester,
                new Repetition(new Repeat(3, 0), proseVal),
                8, 1);

        Assertion.assertMatch("*8<ABC>", tester,
                new Repetition(new Repeat(0, 8), proseVal),
                8, 1);

        Assertion.assertMatch("*<ABC>", tester,
                new Repetition(new Repeat(0, 0), proseVal),
                7, 1);


        Assertion.assertMatchException("**", tester, 2, 1);
        Assertion.assertMatchException("1", tester, 2, 1);
        Assertion.assertMatchException("*1", tester, 3, 1);
        Assertion.assertMatchException("*(", tester, 3, 1);
        Assertion.assertMatchException("*[", tester, 3, 1);
        Assertion.assertMatchException("1*", tester, 3, 1);
        Assertion.assertMatchException(".", tester, 1, 1);

    }

    //		        repeat         =  1*DIGIT / (*DIGIT "*" *DIGIT)
    @Test
    public void testRepeat() throws Exception {
        Tester<Repeat> tester = new Tester<Repeat>() {
            @Override
            public Repeat test(AbnfParser parser) throws MatchException, IOException {
                return parser.repeat();
            }
        };

        String input;
        Assert.assertEquals(new Repeat(0,0), AbnfParser.newInstance("*").repeat());
        Assert.assertEquals(new Repeat(0,0), AbnfParser.newInstance("**").repeat());
        Assert.assertEquals(new Repeat(0,0), AbnfParser.newInstance("*C").repeat());
        Assert.assertEquals(new Repeat(1,1), AbnfParser.newInstance("1").repeat());
        Assert.assertEquals(new Repeat(1,1), AbnfParser.newInstance("1M").repeat());
        Assert.assertEquals(new Repeat(2,0), AbnfParser.newInstance("2*").repeat());
        Assert.assertEquals(new Repeat(2,0), AbnfParser.newInstance("2*_").repeat());
        Assert.assertEquals(new Repeat(0,3), AbnfParser.newInstance("*3").repeat());
        Assert.assertEquals(new Repeat(0,3), AbnfParser.newInstance("*3").repeat());
        Assert.assertEquals(new Repeat(0,3), AbnfParser.newInstance("*3J").repeat());
        Assert.assertEquals(new Repeat(4,9), AbnfParser.newInstance("4*9").repeat());
        Assert.assertEquals(new Repeat(4,9), AbnfParser.newInstance("4*9#").repeat());
        Assert.assertEquals(new Repeat(5,0), AbnfParser.newInstance("5**").repeat());
        Assert.assertEquals(new Repeat(6, 0), AbnfParser.newInstance("6*B").repeat());
        Assertion.assertMatchException("", tester, 1, 1);
        Assertion.assertMatchException("#", tester, 1, 1);
    }

    //		        element        =  rulename / group / option /
//		                          char-val / num-val / prose-val
    @Test
    public void testElement() throws Exception {
        Tester<Element> tester = new Tester<Element>() {
            @Override
            public Element test(AbnfParser parser) throws MatchException, IOException {
                return parser.element();
            }
        };

        String input;
        input = "aBcD1234";
        RuleName ruleName = AbnfParser.newInstance(input).rulename();
        Assertion.assertMatch(input, tester, ruleName, 9, 1);

        input = "(aBcD1234/%d88)";
        Group group = AbnfParser.newInstance(input).group();
        Assertion.assertMatch(input, tester, group, 16, 1);

        input = "[aBcD1234/%d88]";
        Option option = AbnfParser.newInstance(input).option();
        Assertion.assertMatch(input, tester, option, 16, 1);

        input = "\"#$%^\"";
        CharVal charVal = AbnfParser.newInstance(input).char_val();
        Assertion.assertMatch(input, tester, charVal, 7, 1);

        input = "%b0101.1010.1111";
        Element numVal = AbnfParser.newInstance(input).num_val();
        Assertion.assertMatch(input, tester, numVal, 17, 1);

        input = "<aBcD1234/%d88>";
        ProseVal proseVal = AbnfParser.newInstance(input).prose_val();
        Assertion.assertMatch(input, tester, proseVal, 16, 1);

    }

    //		        group          =  "(" *c-wsp alternation *c-wsp ")"
    @Test
    public void testGroup() throws Exception {
        Tester<Group> tester = new Tester<Group>() {
            @Override
            public Group test(AbnfParser parser) throws MatchException, IOException {
                return parser.group();
            }
        };
        Alternation alternation = AbnfParser.newInstance("A/B").alternation();
        Assertion.assertMatch("(A/B)", tester, new Group(alternation), 6, 1);
//        TODO
//        Does not support this case
        Assertion.assertMatchException("(  A/B  )", tester, 9, 1);
    }

    //		        option         =  "[" *c-wsp alternation *c-wsp "]"
    @Test
    public void testOption() throws Exception {
        Tester<Option> tester = new Tester<Option>() {
            @Override
            public Option test(AbnfParser parser) throws MatchException, IOException {
                return parser.option();
            }
        };
        Alternation alternation = AbnfParser.newInstance("A/B").alternation();
        Assertion.assertMatch("[A/B]", tester, new Option(alternation), 6, 1);
//        TODO
//        Does not support this case
        Assertion.assertMatchException("[  A/B  ]", tester, 9, 1);

    }

    //		        char-val       =  DQUOTE *(%x20-21 / %x23-7E) DQUOTE
//  DQUOTE         =  %x22
    @Test
    public void testChar_val() throws Exception {
        Tester<CharVal> tester = new Tester<CharVal>() {
            @Override
            public CharVal test(AbnfParser parser) throws MatchException, IOException {
                return parser.char_val();
            }
        };
        String input;
        input = "";
        Assert.assertEquals(input, AbnfParser.newInstance("\"" + input + "\"").char_val().getValue());
        input = String.valueOf((char)0x20);
        Assert.assertEquals(input, AbnfParser.newInstance("\"" + input + "\"").char_val().getValue());
        input = String.valueOf((char)0x21);
        Assert.assertEquals(input, AbnfParser.newInstance("\"" + input + "\"").char_val().getValue());
        input = String.valueOf((char)0x23);
        Assert.assertEquals(input, AbnfParser.newInstance("\"" + input + "\"").char_val().getValue());
        input = String.valueOf((char)0x7E);
        Assert.assertEquals(input, AbnfParser.newInstance("\"" + input + "\"").char_val().getValue());
        input = String.valueOf((char)0x20) + String.valueOf((char)0x20);
        Assert.assertEquals(input, AbnfParser.newInstance("\"" + input + "\"").char_val().getValue());
        input = String.valueOf((char)0x21) + String.valueOf((char)0x21);
        Assert.assertEquals(input, AbnfParser.newInstance("\"" + input + "\"").char_val().getValue());
        input = String.valueOf((char)0x23) + String.valueOf((char)0x23);
        Assert.assertEquals(input, AbnfParser.newInstance("\"" + input + "\"").char_val().getValue());
        input = String.valueOf((char)0x7E) + String.valueOf((char)0x7E);
        Assert.assertEquals(input, AbnfParser.newInstance("\"" + input + "\"").char_val().getValue());
        input = "AbCd1234#$%^~!@#$%^&*()`-=_+[]\\{}|,./<>?;:'";
        Assert.assertEquals(input, AbnfParser.newInstance("\"" + input + "\"").char_val().getValue());
        Assert.assertEquals(input, AbnfParser.newInstance("\"" + input + "\"A").char_val().getValue());
        Assert.assertEquals(input, AbnfParser.newInstance("\"" + input + "\"\"").char_val().getValue());

        Assertion.assertMatchException("", tester, 1, 1);
        Assertion.assertMatchException("" + (char)0x19, tester, 1, 1);
        Assertion.assertMatchException("\"", tester, 2, 1);
        Assertion.assertMatchException("\"a", tester, 3, 1);
        Assertion.assertMatchException("B", tester, 1, 1);

    }

    //		        bin-val        =  "b" 1*BIT
//		                          [ 1*("." 1*BIT) / ("-" 1*BIT) ]
//  BIT            =  "0" / "1"
    @Test
    public void testBin_val() throws Exception {
        Tester<String> tester = new Tester<String>() {
            @Override
            public String test(AbnfParser parser) throws MatchException, IOException {
                return parser.bin_val().toString();
            }
        };
        String input;
        input = "b1";
        Assert.assertEquals("%" + input, AbnfParser.newInstance(input).bin_val().toString());
        input = "b1010";
        Assert.assertEquals("%" + input, AbnfParser.newInstance(input).bin_val().toString());
        input = "B1";
        Assert.assertEquals("%" + input, AbnfParser.newInstance(input).bin_val().toString());
        input = "b1.1";
        Assert.assertEquals("%" + input, AbnfParser.newInstance(input).bin_val().toString());
        input = "b0101.1111";
        Assert.assertEquals("%" + input, AbnfParser.newInstance(input).bin_val().toString());
        input = "b0000-1111";
        Assert.assertEquals("%" + input, AbnfParser.newInstance(input).bin_val().toString());
        Assert.assertEquals("%" + input, AbnfParser.newInstance(input+".00").bin_val().toString());
        Assert.assertEquals("%" + input, AbnfParser.newInstance(input+"-1234").bin_val().toString());
        input = "b00.11.00.01.10.00.11.00.11";
        Assert.assertEquals("%" + input, AbnfParser.newInstance(input).bin_val().toString());
        Assert.assertEquals("%" + input, AbnfParser.newInstance(input+".").bin_val().toString());
        Assert.assertEquals("%" + input, AbnfParser.newInstance(input+"..").bin_val().toString());
        Assert.assertEquals("%" + input, AbnfParser.newInstance(input+".bb").bin_val().toString());
        Assert.assertEquals("%" + input, AbnfParser.newInstance(input+"-00").bin_val().toString());

        Assertion.assertMatchException("", tester, 1, 1);
        Assertion.assertMatchException("b", tester, 2,1);
        Assertion.assertMatchException("bg", tester, 2, 1);
        Assertion.assertMatchException("b.", tester, 2, 1);
        Assertion.assertMatchException("b-", tester, 2, 1);
    }

    //		        dec-val        =  "d" 1*DIGIT
//		                          [ 1*("." 1*DIGIT) / ("-" 1*DIGIT) ]
    @Test
    public void testDec_val() throws Exception {
        Tester<String> tester = new Tester<String>() {
            @Override
            public String test(AbnfParser parser) throws MatchException, IOException {
                return parser.dec_val().toString();
            }
        };

        String input;
        input = "d1";
        Assert.assertEquals("%" + input, AbnfParser.newInstance(input).dec_val().toString());
        input = "d1234";
        Assert.assertEquals("%" + input, AbnfParser.newInstance(input).dec_val().toString());
        input = "D1";
        Assert.assertEquals("%" + input, AbnfParser.newInstance(input).dec_val().toString());
        input = "d1.2";
        Assert.assertEquals("%" + input, AbnfParser.newInstance(input).dec_val().toString());
        input = "d1234.5678";
        Assert.assertEquals("%" + input, AbnfParser.newInstance(input).dec_val().toString());
        input = "d1234-5678";
        Assert.assertEquals("%" + input, AbnfParser.newInstance(input).dec_val().toString());
        Assert.assertEquals("%" + input, AbnfParser.newInstance(input+".00").dec_val().toString());
        Assert.assertEquals("%" + input, AbnfParser.newInstance(input+"-1234").dec_val().toString());
        input = "d12.34.56.78.9";
        Assert.assertEquals("%" + input, AbnfParser.newInstance(input + "a.bc.de.f0").dec_val().toString());
        Assert.assertEquals("%" + input, AbnfParser.newInstance(input+".").dec_val().toString());
        Assert.assertEquals("%" + input, AbnfParser.newInstance(input+"..").dec_val().toString());
        Assert.assertEquals("%" + input, AbnfParser.newInstance(input+".##").dec_val().toString());
        Assert.assertEquals("%" + input, AbnfParser.newInstance(input+"-00").dec_val().toString());

        Assertion.assertMatchException("", tester, 1, 1);
        Assertion.assertMatchException("d", tester, 2, 1);
        Assertion.assertMatchException("dg", tester, 2, 1);
        Assertion.assertMatchException("d.", tester, 2, 1);
        Assertion.assertMatchException("d-", tester, 2, 1);
    }

    //		        hex-val        =  "x" 1*HEXDIG
//		                          [ 1*("." 1*HEXDIG) / ("-" 1*HEXDIG) ]
    @Test
    public void testHex_val() throws Exception {
        Tester<String> tester = new Tester<String>() {
            @Override
            public String test(AbnfParser parser) throws MatchException, IOException {
                return parser.hex_val().toString();
            }
        };

        String input;
        input = "x1";
        Assert.assertEquals("%" + input, AbnfParser.newInstance(input).hex_val().toString());
        input = "x1234";
        Assert.assertEquals("%" + input, AbnfParser.newInstance(input).hex_val().toString());
        input = "X1";
        Assert.assertEquals("%" + input, AbnfParser.newInstance(input).hex_val().toString());
        input = "x1.2";
        Assert.assertEquals("%" + input, AbnfParser.newInstance(input).hex_val().toString());
        input = "x1234.5678";
        Assert.assertEquals("%" + input, AbnfParser.newInstance(input).hex_val().toString());
        input = "xabcd.ef";
        Assert.assertEquals("%" + input, AbnfParser.newInstance(input).hex_val().toString());
        input = "xA1.2B";
        Assert.assertEquals("%" + input, AbnfParser.newInstance(input).hex_val().toString());
        input = "x1234-abCD";
        Assert.assertEquals("%" + input, AbnfParser.newInstance(input).hex_val().toString());
        Assert.assertEquals("%" + input, AbnfParser.newInstance(input+"-").hex_val().toString());
        input = "x12.34.56.78.9a.bc.de.f0";
        Assert.assertEquals("%" + input, AbnfParser.newInstance(input).hex_val().toString());
        Assert.assertEquals("%" + input, AbnfParser.newInstance(input + ".").hex_val().toString());
        Assert.assertEquals("%" + input, AbnfParser.newInstance(input + ".g0").hex_val().toString());
        Assert.assertEquals("%" + input, AbnfParser.newInstance(input + "-00").hex_val().toString());

        Assertion.assertMatchException("", tester, 1, 1);
        Assertion.assertMatchException("x", tester, 2, 1);
        Assertion.assertMatchException("xg", tester, 2, 1);
        Assertion.assertMatchException("x.", tester, 2, 1);
        Assertion.assertMatchException("x-", tester, 2, 1);

    }

    //		        num-val        =  "%" (bin-val / dec-val / hex-val)
    @Test
    public void testNum_val() throws Exception {
        String input;
        input = "%b0101";
        Assert.assertEquals(input, AbnfParser.newInstance(input).num_val().toString());
        input = "%b0101.1010.1111";
        Assert.assertEquals(input, AbnfParser.newInstance(input).num_val().toString());
        input = "%b0101-1111";
        Assert.assertEquals(input, AbnfParser.newInstance(input).num_val().toString());
        input = "%d1234";
        Assert.assertEquals(input, AbnfParser.newInstance(input).num_val().toString());
        input = "%d0123.4567.8901";
        Assert.assertEquals(input, AbnfParser.newInstance(input).num_val().toString());
        input = "%d12345-67890";
        Assert.assertEquals(input, AbnfParser.newInstance(input).num_val().toString());
        input = "%x0123";
        Assert.assertEquals(input, AbnfParser.newInstance(input).num_val().toString());
        input = "%x0123.4567.89ab.CDEF";
        Assert.assertEquals(input, AbnfParser.newInstance(input).num_val().toString());
        input = "%x0123456789-ABCDEFabcdef09";
        Assert.assertEquals(input, AbnfParser.newInstance(input).num_val().toString());
    }

    //		        prose-val      =  "<" *(%x20-3D / %x3F-7E) ">"
    @Test
    public void testProse_val() throws Exception {
        Tester<String> tester = new Tester<String>() {
            @Override
            public String test(AbnfParser parser) throws MatchException, IOException {
                return parser.prose_val().toString();
            }
        };
        String input;
        input = String.valueOf(new char[] {'<', '>'});
        Assert.assertEquals(input, AbnfParser.newInstance(input).prose_val().toString());
        input = String.valueOf(new char[] {'<', 0x20, '>'});
        Assert.assertEquals(input, AbnfParser.newInstance(input).prose_val().toString());
        input = String.valueOf(new char[] {'<', 0x3D, '>'});
        Assert.assertEquals(input, AbnfParser.newInstance(input).prose_val().toString());
        input = String.valueOf(new char[] {'<', 0x3F, '>'});
        Assert.assertEquals(input, AbnfParser.newInstance(input).prose_val().toString());
        input = String.valueOf(new char[] {'<', 0x7E, '>'});
        Assert.assertEquals(input, AbnfParser.newInstance(input).prose_val().toString());
        input = String.valueOf(new char[] {'<', 0x20, 0x3D, 0x3F, 0x7E, '>'});
        Assert.assertEquals(input, AbnfParser.newInstance(input).prose_val().toString());
        Assert.assertEquals("<>", AbnfParser.newInstance("<>>").prose_val().toString());
        Assertion.assertMatchException("<" + (char) 0x19 + ">", tester, 2, 1);
        Assertion.assertMatchException("<" + (char) 0x7F + ">", tester, 2, 1);
        Assertion.assertMatchException("<" + (char)0x20 + (char)0x19 + ">", tester, 3, 1);
    }

    @Test
    public void testParse() throws Exception {
        Tester<List<Rule>> tester = new Tester<List<Rule>>() {
            @Override
            public List<Rule> test(AbnfParser parser) throws MatchException, IOException, CollisionException {
                return parser.parse();
            }
        };

        String input1 ,input2;
        input1= "a=b" + (char)0x0d + (char)0x0a +
                "a=/*c/1*d/*2e/3*8f/[a b c]/(a b [%xff])" + (char)0x0d + (char)0x0a;
        input2= "a=b/*c/1*d/*2e/3*8f/[a b c]/(a b [%xff])" + (char)0x0d + (char)0x0a;

        Assertion.assertMatch(input1, tester, AbnfParser.newInstance(input2).rulelist(), 1, 3);

        input1= "a=b" + (char)0x0d + (char)0x0a +
                "a=*c/1*d/*2e/3*8f/[a b c]/(a b [%xff])" + (char)0x0d + (char)0x0a;
        Assertion.assertCollisionException(input1, tester, 1, 3);
    }
}
