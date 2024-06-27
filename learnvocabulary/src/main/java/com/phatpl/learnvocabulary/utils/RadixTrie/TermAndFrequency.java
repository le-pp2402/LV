package com.phatpl.learnvocabulary.utils.RadixTrie;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TermAndFrequency {
    private String term;
    private long termFrequencyCount;

    @Override
    public String toString() {
        return "TermAndFrequency [term=" + term + ", termFrequencyCount=" + termFrequencyCount + "]";
    }

    public TermAndFrequency(String term, long termFrequencyCount) {
        super();
        this.term = term;
        this.termFrequencyCount = termFrequencyCount;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((term == null) ? 0 : term.hashCode());
        result = prime * result + Long.hashCode(termFrequencyCount);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TermAndFrequency other = (TermAndFrequency) obj;
        if (term == null) {
            if (other.term != null)
                return false;
        } else if (!term.equals(other.term))
            return false;
        return termFrequencyCount == other.termFrequencyCount;
    }
}
