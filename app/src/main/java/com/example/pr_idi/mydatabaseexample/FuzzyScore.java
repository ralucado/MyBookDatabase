package com.example.pr_idi.mydatabaseexample;

import java.util.Locale;

public class FuzzyScore {

    private final Locale locale;

    public FuzzyScore(final Locale locale) {
        this.locale = locale;
    }


    public Integer fuzzyScore(final CharSequence term, final CharSequence query) {
        final String termLowerCase = term.toString().toLowerCase(locale);
        final String queryLowerCase = query.toString().toLowerCase(locale);

        int score = 0;

        int termIndex = 0;

        int indexCarAnterior = Integer.MIN_VALUE;

        for (int i = 0; i < queryLowerCase.length(); i++) {
            final char queryChar = queryLowerCase.charAt(i);

            boolean matchFound = false;
            for (; termIndex < termLowerCase.length()
                    && !matchFound; termIndex++) {
                final char termChar = termLowerCase.charAt(termIndex);

                if (queryChar == termChar) {

                    score++;

                    if (indexCarAnterior + 1 == termIndex) {
                        score += 2;
                    }

                    indexCarAnterior = termIndex;

                    matchFound = true;
                }
            }
        }

        return score;
    }

}