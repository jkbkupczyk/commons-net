/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.apache.commons.net.ftp.parser;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.net.ftp.FTPFileEntryParserImpl;

/**
 * This abstract class implements both the older FTPFileListParser and
 * newer FTPFileEntryParser interfaces with default functionality.
 * All the classes in the parser subpackage inherit from this.
 *
 * This is the base class for all regular expression based FTPFileEntryParser classes
 */
public abstract class RegexFTPFileEntryParserImpl extends
        FTPFileEntryParserImpl {
    /**
     * internal pattern the matcher tries to match, representing a file
     * entry
     */
    private Pattern pattern;

    /**
     * internal match result used by the parser
     */
    private MatchResult result;

    /**
     * Internal PatternMatcher object used by the parser.  It has protected
     * scope in case subclasses want to make use of it for their own purposes.
     */
    protected Matcher _matcher_;

    /**
     * The constructor for a RegexFTPFileEntryParserImpl object.
     * The expression is compiled with flags = 0.
     *
     * @param regex  The regular expression with which this object is
     * initialized.
     *
     * @throws IllegalArgumentException
     * Thrown if the regular expression is unparseable.  Should not be seen in
     * normal conditions.  It it is seen, this is a sign that a subclass has
     * been created with a bad regular expression.   Since the parser must be
     * created before use, this means that any bad parser subclasses created
     * from this will bomb very quickly,  leading to easy detection.
     */

    public RegexFTPFileEntryParserImpl(final String regex) {
        compileRegex(regex, 0);
    }

    /**
     * The constructor for a RegexFTPFileEntryParserImpl object.
     *
     * @param regex  The regular expression with which this object is
     * initialized.
     * @param flags the flags to apply, see {@link Pattern#compile(String, int)}. Use 0 for none.
     *
     * @throws IllegalArgumentException
     * Thrown if the regular expression is unparseable.  Should not be seen in
     * normal conditions.  It it is seen, this is a sign that a subclass has
     * been created with a bad regular expression.   Since the parser must be
     * created before use, this means that any bad parser subclasses created
     * from this will bomb very quickly,  leading to easy detection.
     * @since 3.4
     */
    public RegexFTPFileEntryParserImpl(final String regex, final int flags) {
        compileRegex(regex, flags);
    }

    /**
     * Compile the regex and store the {@link Pattern}.
     *
     * This is an internal method to do the work so the constructor does not
     * have to call an overrideable method.
     *
     * @param regex the expression to compile
     * @param flags the flags to apply, see {@link Pattern#compile(String, int)}. Use 0 for none.
     * @throws IllegalArgumentException if the regex cannot be compiled
     */
    private void compileRegex(final String regex, final int flags) {
        try {
            pattern = Pattern.compile(regex, flags);
        } catch (final PatternSyntaxException pse) {
            throw new IllegalArgumentException("Unparseable regex supplied: " + regex);
        }
    }

    /**
     * Convenience method
     *
     * @return the number of groups() in the internal MatchResult.
     */

    public int getGroupCnt() {
        if (this.result == null) {
            return 0;
        }
        return this.result.groupCount();
    }

    /**
     * For debugging purposes - returns a string shows each match group by
     * number.
     *
     * @return a string shows each match group by number.
     */

    public String getGroupsAsString() {
        final StringBuilder b = new StringBuilder();
        for (int i = 1; i <= this.result.groupCount(); i++) {
            b.append(i).append(") ").append(this.result.group(i)).append(
                    System.getProperty("line.separator"));
        }
        return b.toString();
    }

    /**
     * Convenience method delegates to the internal MatchResult's group()
     * method.
     *
     * @param matchnum match group number to be retrieved
     *
     * @return the content of the <code>matchnum'th</code> group of the internal
     *         match or null if this method is called without a match having
     *         been made.
     */
    public String group(final int matchnum) {
        if (this.result == null) {
            return null;
        }
        return this.result.group(matchnum);
    }

    /**
     * Convenience method delegates to the internal MatchResult's matches()
     * method.
     *
     * @param s the String to be matched
     * @return true if s matches this object's regular expression.
     */

    public boolean matches(final String s) {
        this.result = null;
        _matcher_ = pattern.matcher(s);
        if (_matcher_.matches()) {
            this.result = _matcher_.toMatchResult();
        }
        return null != this.result;
    }


    /**
     * Alter the current regular expression being utilised for entry parsing
     * and create a new {@link Pattern} instance.
     * @param regex The new regular expression
     * @return  true
     * @since 2.0
     * @throws IllegalArgumentException if the regex cannot be compiled
     */
    public boolean setRegex(final String regex) {
        compileRegex(regex, 0);
        return true;
    }

    /**
     * Alter the current regular expression being utilised for entry parsing
     * and create a new {@link Pattern} instance.
     * @param regex The new regular expression
     * @param flags the flags to apply, see {@link Pattern#compile(String, int)}. Use 0 for none.
     * @return  true
     * @since 3.4
     * @throws IllegalArgumentException if the regex cannot be compiled
     */
    public boolean setRegex(final String regex, final int flags) {
        compileRegex(regex, flags);
        return true;
    }
}
