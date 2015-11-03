/**
 *
 */
package com.pberger.word.counter;

/**
 * 
 * @author Parker Berger
 * 
 */
public class Word
{
    String word;
    int frequency;

    public Word(String word)
    {
        this.word = word;
        this.frequency = 1;
    }

    public void increaseFrequency()
    {
        frequency++;
    }

    public int getFrequency()
    {
        return frequency;
    }

    public String getWord()
    {
        return word;
    }
}
