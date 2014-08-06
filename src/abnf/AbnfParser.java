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
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;


public class AbnfParser {
    protected PeekableInputStream is;
    public PeekableInputStream getInputStream() { return is; }
	protected String prefix;

    public boolean match(int value, int expected) {
        return value == expected;
    }

    public boolean match(int value, int lower, int upper) {
        return value >= lower && value <= upper;
    }

    public boolean match(int value, char expected) {
        return Character.toUpperCase(value) == Character.toUpperCase(expected);
    }

    public boolean match(int value, int[] expected) {
        for(int index = 0; index < expected.length; index ++) {
            if (value == expected[index]) return true;
        }
        return false;
    }

    public void assertMatch(int value, int expected) throws MatchException {
        if (!match(value, expected)) {
            throw(new MatchException("'" + (char)expected +"' [" + String.format("%02X", expected) + "]", value, is.getPos(), is.getLine()));
        }
    }

    public void assertMatch(int value, int lower, int upper) throws MatchException {
        if (!match(value, lower, upper)) {
            throw(new MatchException(
                    "'" + (char)lower +"'~'" + (char)upper + "' " +
                            "[" + String.format("%02X", lower) + "~" + String.format("%02X", lower) + "]",
                    value, is.getPos(), is.getLine()));
        }
    }

    public void assertMatch(int value, char expected) throws IOException, MatchException {
        if (!match(value, expected)) {
            throw(new MatchException("'" + expected +"' [" + String.format("%02X", (int)expected) + "]", value, is.getPos(), is.getLine()));
        }
    }


//	   ALPHA          =  %x41-5A / %x61-7A   ; A-Z / a-z
//     BIT            =  "0" / "1"

//     CHAR           =  %x01-7F

//     CR             =  %x0D

//     CRLF           =  CR LF

//     CTL            =  %x00-1F / %x7F

//     DIGIT          =  %x30-39

//     DQUOTE         =  %x22

//     HEXDIG         =  DIGIT / "A" / "B" / "C" / "D" / "E" / "F"

//     HTAB           =  %x09


//     LWSP           =  *(WSP / CRLF WSP)

//     OCTET          =  %x00-FF

//     SP             =  %x20
//     WSP            =  SP / HTAB

//	   rulelist       =  1*( rule / (*c-wsp c-nl) )
	protected List<Rule> rulelist() throws IOException, MatchException, CollisionException {
        Map<RuleName, Rule> ruleMap = new HashMap<RuleName, Rule>();
        List<Rule> ruleList = new ArrayList<Rule>();

        while (match(is.peek(), 0x41, 0x5A) || match(is.peek(), 0x61, 0x7A) || match(is.peek(), 0x20) || match(is.peek(), ';') || match(is.peek(), 0x0D)) {
            if (match(is.peek(), 0x41, 0x5A) || match(is.peek(), 0x61, 0x7A)) {
                Rule rule = rule();
                if (null == ruleMap.get(rule.getRuleName())) {
                    ruleMap.put(rule.getRuleName(), rule);
                    ruleList.add(rule);
                } else {
                    Rule defined = ruleMap.get(rule.getRuleName());
                    if ("=".equals(rule.getDefinedAs()) && "=".equals(defined.getDefinedAs())) {
                        throw new CollisionException(rule.getRuleName().toString() + " is redefined.", is.getPos(), is.getLine());
                    }
                    if ("=".equals(rule.getDefinedAs())) defined.setDefinedAs("=");
                    defined.getElements().getAlternation().getConcatenations().addAll(rule.getElements().getAlternation().getConcatenations());
                }
            } else {
//                while (match(is.peek(), 0x20) || match(is.peek(), ';') || match(is.peek(), 0x0D)) {
//                    c_wsp();
//                }
                c_nl();
            }
        }
        return ruleList;
	}

//		        rule           =  rulename defined-as elements c-nl
	protected Rule rule() throws IOException, MatchException {
		RuleName rulename = rulename();
		String definedAs = defined_as();
		Elements elements = elements();
		c_nl();
//        System.out.println("rulename=" + rulename.toString() + ", defined_as=" + definedAs + ", elements=" + elements);
		return new Rule(rulename, definedAs, elements);
	}

//		        rulename       =  ALPHA *(ALPHA / DIGIT / "-")
	protected RuleName rulename() throws IOException, MatchException {
//		 ALPHA          =  %x41-5A / %x61-7A   ; A-Z / a-z
//	     DIGIT          =  %x30-39
        if (!(match(is.peek(), 0x41, 0x5A) || match(is.peek(), 0x61, 0x7A))) {
            throw new MatchException("'A'-'Z'/'a'-'z'", is.peek(), is.getPos(), is.getLine());
        }
        String rulename = "";
        rulename += (char)is.read();
        while (match(is.peek(), 0x41, 0x5A) || match(is.peek(), 0x61, 0x7A) || match(is.peek(), 0x30, 0x39) |match(is.peek(), '-')) {
            rulename += (char)is.read();
        }
        return new RuleName(prefix, rulename);
	}

//		        defined-as     =  *c-wsp ("=" / "=/") *c-wsp
	protected String defined_as() throws IOException, MatchException {
        String value = "";
        while (match(is.peek(), 0x20) || match(is.peek(), 0x09) || match(is.peek(), ';') || match(is.peek(), (char)0x0D)) {
            c_wsp();
        }

        assertMatch(is.peek(), '=');
        value = String.valueOf((char)is.read());

        if (match(is.peek(), '/')) {
            value += (char)is.read();
		}
        while (match(is.peek(), 0x20) || match(is.peek(), 0x09) || match(is.peek(), ';') || match(is.peek(), (char)0x0D)) {
            c_wsp();
        }
        return value;
	}

//		        elements       =  alternation *c-wsp
	protected Elements elements() throws IOException, MatchException {
		Alternation alternation = alternation();
        while (match(is.peek(), 0x20) || match(is.peek(), 0x09) || match(is.peek(), ';')) {
			c_wsp();
		}
		return new Elements(alternation);
	}

//  HTAB           =  %x09
	protected String HTAB() throws IOException, MatchException {
        assertMatch(is.peek(), 0x09);
        int value = is.read();
        return String.valueOf((char)value);
	}

//  LF             =  %x0A
	protected String LF() throws IOException, MatchException {
        assertMatch(is.peek(), 0x0A);
        int value = is.read();
        return String.valueOf((char)value);
	}

//  LWSP           =  *(WSP / CRLF WSP)

//  OCTET          =  %x00-FF

//  SP             =  %x20
	protected String SP() throws IOException, MatchException {
        assertMatch(is.peek(), 0x20);
        int value = is.read();
        return String.valueOf((char)value);
	}

//WSP            =  SP / HTAB
	protected String WSP() throws IOException, MatchException {
		switch (is.peek()) {
		case 0x20: return SP();
		case 0x09: return HTAB();
		default: throw new MatchException("[0x20, 0x09]", is.peek(), is.getPos(), is.getLine());
		}
	}

//  CR             =  %x0D
	protected String CR() throws IOException, MatchException {
        assertMatch(is.peek(), 0x0D);
        int value = is.read();
        return String.valueOf((char)value);
	}

//  CRLF           =  CR LF
	protected String CRLF() throws IOException, MatchException {
		return CR() + LF();
	}

//    c-wsp          =  WSP / (c-nl WSP)
	protected String c_wsp() throws IOException, MatchException {
		switch (is.peek()) {
            case 0x20: case 0x09: return WSP();
		    case ';':case 0x0D: return c_nl() + WSP();
		    default: throw new MatchException("[0x20, ';']", is.peek(), is.getPos(), is.getLine());
		}
	}

//		        c-nl           =  comment / CRLF
	protected String c_nl() throws IOException, MatchException {
		switch (is.peek()) {
		case ';': return comment();
		case 0x0D: return CRLF();
		default: throw new MatchException("[';', 0x0D]", is.peek(), is.getPos(), is.getLine());
		}
	}

//  VCHAR          =  %x21-7E
	protected String VCHAR() throws IOException, MatchException {
        assertMatch(is.peek(), 0x21, 0x7E);
        int value = is.read();
        return String.valueOf((char)value);
	}

//    comment        =  ";" *(WSP / VCHAR) CRLF
	protected String comment() throws IOException, MatchException {
        String comment = "";
        assertMatch(is.peek(), ';');
        int value = is.read();
        comment += (char)value;
        
        while (match(is.peek(), 0x20) || match(is.peek(), 0x09) || match(is.peek(), 0x21, 0x7E)) {
            if (match(is.peek(), 0x20) || match(is.peek(), 0x09)) comment += WSP();
            else comment += VCHAR();
//			if (peekMatch ==0x20 || peekMatch == 0x09) WSP();
//			else if (peekMatch >= 0x21 && peekMatch <= 0x7E) VCHAR();
		}
		comment += CRLF();
        return comment;
	}

//		        alternation    =  concatenation
//		                          *(*c-wsp "/" *c-wsp concatenation)
	protected Alternation alternation() throws IOException, MatchException {
		Alternation alternation = new Alternation();
		alternation.addConcatenation(concatenation());
        while (match(is.peek(), new int[] { 0x20, ';', '/'})) {
            while (match(is.peek(), 0x20) || match(is.peek(), ';')) {
				c_wsp();
			}
            assertMatch(is.peek(), '/');
            is.read();
            while (match(is.peek(), 0x20) || match(is.peek(), ';')) {
				c_wsp();
			}
			alternation.addConcatenation(concatenation());
		}
		return alternation;
	}

//		        concatenation  =  repetition *(1*c-wsp repetition)
	protected Concatenation concatenation() throws IOException, MatchException {
		Concatenation concatenation = new Concatenation();
		concatenation.addRepetition(repetition());
        while (match(is.peek(), 0x20) || match(is.peek(), ';')) {
            while (match(is.peek(), 0x20) || match(is.peek(), ';')) {
				c_wsp();
			}
			concatenation.addRepetition(repetition());
		}
		return concatenation;
	}

//		        repetition     =  [repeat] element
//    DIGIT          =  %x30-39
	protected Repetition repetition() throws IOException, MatchException {
		Repeat repeat = null;
        if (match(is.peek(), 0x30, 0x39) || match(is.peek(), '*')) {
			repeat = repeat();
		}
		Element element = element();
		return new Repetition(repeat, element);
	}

//		        repeat         =  1*DIGIT / (*DIGIT "*" *DIGIT)
	protected Repeat repeat() throws IOException, MatchException {
		int min = 0, max = 0;
        if (match(is.peek(), '*')) {
            is.read();
            if (match(is.peek(), 0x30, 0x39)) {
                while (match(is.peek(), 0x30, 0x39)) {
                    max = max * 10 + Integer.valueOf(String.valueOf((char)is.read()));
                }
            }
            return new Repeat(min, max);
        } else if (match(is.peek(), 0x30, 0x39)) {
			while (match(is.peek(), 0x30, 0x39)) {
				min = min * 10 + Integer.valueOf(String.valueOf((char)is.read()));
			}
            if (match(is.peek(), '*')) {
                is.read();
                if (match(is.peek(), 0x30, 0x39)) {
                    while (match(is.peek(), 0x30, 0x39)) {
                        max = max * 10 + Integer.valueOf(String.valueOf((char)is.read()));
                    }
                }
                return new Repeat(min, max);
            } else {
                return new Repeat(min, min);
            }
		} else {
            throw new MatchException("['0'-'9', '*']", is.peek(), is.getPos(), is.getLine());
        }
	}
//	 ALPHA          =  %x41-5A / %x61-7A   ; A-Z / a-z

//		        element        =  rulename / group / option /
//		                          char-val / num-val / prose-val
	protected Element element() throws IOException, MatchException {
        if (match(is.peek(), 0x41, 0x5A) || match(is.peek(), 0x61, 0x7A)) {
            return rulename();
        }

        switch (is.peek()) {
            case '(': return  group();
            case '[': return option();
            case 0x22: return char_val();
            case '%': return num_val();
            case '<': return prose_val();
            default: throw new MatchException("['(', '[', 0x22, '%', '<']", is.peek(), is.getPos(), is.getLine());
        }
	}

//		        group          =  "(" *c-wsp alternation *c-wsp ")"
	protected Group group() throws IOException, MatchException {
        assertMatch(is.peek(), '(');
        is.read();
		while (match(is.peek(), new int[] {0x20, ';', 0x0D})) {
			c_wsp();
		}
		Alternation alternation = alternation();
        while (match(is.peek(), new int[] {0x20, ';', 0x0D})) {
			c_wsp();
		}
        assertMatch(is.peek(), ')');
        is.read();
		return new Group(alternation);
	}

//		        option         =  "[" *c-wsp alternation *c-wsp "]"
	protected Option option() throws IOException, MatchException {
		assertMatch(is.peek(), '[');
        is.read();
        while (match(is.peek(), new int[] {0x20, ';', 0x0D})) {
			c_wsp();
		}
		Alternation alternation = alternation();
        while (match(is.peek(), new int[] {0x20, ';', 0x0D})) {
			c_wsp();
		}
		assertMatch(is.peek(), ']');
        is.read();
		return new Option(alternation);
	}

//		        char-val       =  DQUOTE *(%x20-21 / %x23-7E) DQUOTE
//  DQUOTE         =  %x22
	protected CharVal char_val() throws IOException, MatchException {
		String char_val = "";
        assertMatch(is.peek(), 0x22);
        is.read();
        while (match(is.peek(), 0x20, 0x21) || match(is.peek(), 0x23, 0x7E)) {
			char_val += (char)is.read();
		}
		assertMatch(is.peek(), 0x22);
        is.read();
		return new CharVal(char_val);
	}
    
    //		        bin-val        =  "b" 1*BIT
//		                          [ 1*("." 1*BIT) / ("-" 1*BIT) ]
//  BIT            =  "0" / "1"

    protected Element bin_val() throws IOException, MatchException {
        return val('b', new Matcher() {
            @Override
            public boolean match(int value) {
                return value == '0' || value == '1';
            }

            @Override
            public String expected() {
                return "['0', '1']";
            }
        });
    }

//		        dec-val        =  "d" 1*DIGIT
//		                          [ 1*("." 1*DIGIT) / ("-" 1*DIGIT) ]
    protected Element dec_val() throws IOException, MatchException {
        return val('d', new Matcher() {
            @Override
            public boolean match(int value) {
                return (value >= 0x30 && value <= 0x39);// || (value >= 'A' && value <= 'F') || (value >= 'a' && value <= 'f');
            }

            @Override
            public String expected() {
                return "['0'-'9']";//, 'A'-'F', 'a'-'f']";
            }
        });
    }

//		        hex-val        =  "x" 1*HEXDIG
//		                          [ 1*("." 1*HEXDIG) / ("-" 1*HEXDIG) ]
    protected Element hex_val() throws IOException, MatchException {
        return val('x', new Matcher() {
            @Override
            public boolean match(int value) {
                return (value >= 0x30 && value <= 0x39) || (value >= 'A' && value <= 'F') || (value >= 'a' && value <= 'f');
            }

            @Override
            public String expected() {
                return "['0'-'9', 'A'-'F', 'a'-'f']";
            }
        });
    }

    protected Element val(char base, Matcher matcher) throws IOException, MatchException {
        assertMatch(is.peek(), base);
        int baseValue = is.read();
        String from = "";
        String val = "";

        if (matcher.match(is.peek())) {
            while (matcher.match(is.peek())) {
                from += (char)is.read();
            }
            if (match(is.peek(), '.')) {
                NumVal numval = new NumVal(String.valueOf((char)baseValue));
                numval.addValue(from);
                while (match(is.peek(), '.')) {
                    int next = is.peek(1);
                    if (!(matcher.match(next))) {
                        break;
                    }
                    is.read();
                    val = "";
                    while (matcher.match(is.peek())) {
                        val += (char)is.read();
                    }
                    numval.addValue(val);
                }
                return numval;
            } else if (match(is.peek(), '-')) {
                int next = is.peek(1);
                if (!(matcher.match(next))) {
                    NumVal numval = new NumVal(String.valueOf((char)baseValue));
                    numval.addValue(from);
                    return numval;
                }
                is.read();
                val ="";
                val += (char)is.read();
                while (matcher.match(is.peek())) {
                    val += (char)is.read();
                }
                return new RangedNumVal(String.valueOf((char)baseValue), from, val);
            } else {
                NumVal numval = new NumVal(String.valueOf((char)baseValue));
                numval.addValue(from);
                return numval;
            }
        } else {
            throw new MatchException(matcher.expected(), is.peek(), is.getPos(), is.getLine());
        }

    }

    //		        num-val        =  "%" (bin-val / dec-val / hex-val)
	protected Element num_val() throws IOException, MatchException {
		String base = "", from ="", val ="";
		assertMatch(is.peek(), '%');
        is.read();
		switch ((char)is.peek()) {
            case 'b': case 'B': return bin_val();
		    case 'd': case 'D': return dec_val();
		    case 'x': case 'X': return hex_val();
    		default: throw new MatchException("['b', 'd', 'x']", is.peek(), is.getPos(), is.getLine());
		}
	}



//		        prose-val      =  "<" *(%x20-3D / %x3F-7E) ">"
	protected ProseVal prose_val() throws IOException, MatchException {
		String proseval = "";
		assertMatch(is.peek(), '<');
        is.read();
		while (match(is.peek(), 0x20, 0x3D) || match(is.peek(), 0x3F, 0x7E)) {
			proseval += (char)is.read();
		}
		assertMatch(is.peek(), '>');
        is.read();
		return new ProseVal(proseval);
	}
	
	public List<Rule> parse() throws IOException, MatchException, CollisionException {
//        Map<RuleName, Rule> ruleMap = new HashMap<RuleName, Rule>();
//        List<Rule> ruleList = rulelist();
//        List<Rule> result = new ArrayList<Rule>();
//        for(int index = 0; index < ruleList.size(); index ++) {
//            if (null == ruleMap.get(ruleList.get(index).getRuleName())) {
//                result.add(ruleList.get(index));
//                ruleMap.put(ruleList.get(index).getRuleName(), ruleList.get(index));
//            } else {
//                Rule defined = ruleMap.get(ruleList.get(index).getRuleName());
//                if ("=".equals(defined.getDefinedAs()) && "=".equals(ruleList.get(index).getDefinedAs())) {
//                    throw new CollisionException("Rule is redefined: " + ruleList.get(index).toString());
//                }
//            }
//        }

        return rulelist();
	}
	public AbnfParser(String prefix, InputStream inputStream) {
		this.prefix = prefix;
        this.is = new PeekableInputStream(inputStream);
	}

    public static AbnfParser newInstance(String prefix, String input) {
        return new AbnfParser(prefix, new ByteArrayInputStream(input.getBytes()));
    }
    public static AbnfParser newInstance(String input) {
        return new AbnfParser("", new ByteArrayInputStream(input.getBytes()));
    }
    public static AbnfParser newInstance(String prefix, char[] input) {
        return new AbnfParser(prefix, new ByteArrayInputStream(String.valueOf(input).getBytes()));
    }
    public static AbnfParser newInstance(char[] input) {
        return new AbnfParser("", new ByteArrayInputStream(String.valueOf(input).getBytes()));
    }

}
