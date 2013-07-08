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

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 5/30/13
 * Time: 11:00 AM
 * To change this template use File | Settings | File Templates.
 */
import java.io.*;

/**
 * The Heaton Research Spider Copyright 2007 by Heaton
 * Research, Inc.
 *
 * HTTP Programming Recipes for Java ISBN: 0-9773206-6-9
 * http://www.heatonresearch.com/articles/series/16/
 *
 * PeekableInputStream: This is a special input stream that
 * allows the program to peek one or more characters ahead
 * in the file.
 *
 * This class is released under the:
 * GNU Lesser General Public License (LGPL)
 * http://www.gnu.org/copyleft/lesser.html
 *
 * @author Jeff Heaton
 * @version 1.1
 */
public class PeekableInputStream extends InputStream
{
    protected int pos = 1;
    public int getPos() { return pos; }
    protected int line = 1;
    public int getLine() { return line; }
    protected void updatePosition(int value) {
        if (value == (byte)0x0D) pos = 1;
        else if (value == (byte)0x0A) line ++;
        else pos ++;

    }

    /**
     * The underlying stream.
     */
    private InputStream stream;

    /**
     * Bytes that have been peeked at.
     */
    private byte peekBytes[];

    /**
     * How many bytes have been peeked at.
     */
    private int peekLength;

    /**
     * The constructor accepts an InputStream to setup the
     * object.
     *
     * @param is
     *          The InputStream to parse.
     */
    public PeekableInputStream(InputStream is)
    {
        this.stream = is;
        this.peekBytes = new byte[10];
        this.peekLength = 0;
    }

    /**
     * Peek at the next character from the stream.
     *
     * @return The next character.
     * @throws IOException
     *           If an I/O exception occurs.
     */
    public int peek() throws IOException
    {
        return peek(0);
    }

    /**
     * Peek at a specified depth.
     *
     * @param depth
     *          The depth to check.
     * @return The character peeked at.
     * @throws IOException
     *           If an I/O exception occurs.
     */
    public int peek(int depth) throws IOException
    {
        // does the size of the peek buffer need to be extended?
        if (this.peekBytes.length <= depth)
        {
            byte temp[] = new byte[depth + 10];
            for (int i = 0; i < this.peekBytes.length; i++)
            {
                temp[i] = this.peekBytes[i];
            }
            this.peekBytes = temp;
        }

        // does more data need to be read?
        if (depth >= this.peekLength)
        {
            int offset = this.peekLength;
            int length = (depth - this.peekLength) + 1;
            int lengthRead = this.stream.read(this.peekBytes, offset, length);

            if (lengthRead == -1)
            {
                return -1;
            }

            this.peekLength = depth + 1;
        }

        return this.peekBytes[depth];
    }

    /*
     * Read a single byte from the stream. @throws IOException
     * If an I/O exception occurs. @return The character that
     * was read from the stream.
     */
    @Override
    public int read() throws IOException
    {
        if (this.peekLength == 0)
        {
            int value = this.stream.read();
            updatePosition(value);
            return value;
        }

        int result = this.peekBytes[0];
        this.updatePosition(result);
        this.peekLength--;
        for (int i = 0; i < this.peekLength; i++)
        {
            this.peekBytes[i] = this.peekBytes[i + 1];
        }

        return result;
    }

}