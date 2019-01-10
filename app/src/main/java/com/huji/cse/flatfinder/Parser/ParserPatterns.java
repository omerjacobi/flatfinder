package com.huji.cse.flatfinder.Parser;

import java.util.regex.Pattern;

/**
 * Class that holds all of the necessary patterns for the parser
 */
class ParserPatterns {

    static final Pattern pricePattern = Pattern.compile("(?:p|P)rice");
    static final Pattern addressPattern = Pattern.compile("(?:a|A)ddress");
    static final Pattern noRoommatesPattern = Pattern.compile("(?:of)* *(?:r|R)oom(?:m)?ates");
    static final Pattern numberPattern = Pattern.compile("\\d+");
    static final Pattern stringPattern = Pattern.compile("((?:\\w+ *)+):*-*\\\\*(?:\\n)*\\.*");
    static final Pattern namePattern = Pattern.compile("Photos from (.+)'s post");
}
