package pgdp.iter;

import java.util.*;

public class PasswordIterator implements Iterator<String> {
    private boolean[] usedCodes; //boolean array to keep track of already usedCodes codes
    private int sameDigitCodes = 0, increasingCodes = 0, decreasingCodes = 0, counter = 0, passwordLength, biggestNr; //counters needed to remember codes that we can iterate
    private boolean length1complete = false; //variable needed only when passwordLength = 1

    public PasswordIterator(int passwordLength) {
        if (passwordLength < 1 || passwordLength > 9)
            Util.badArgument("password length must be between 1 and 9");
        this.passwordLength = passwordLength;

        sameDigitCodes = 10; //we always have 10 codes of repeating digits (0-9)

        //if always have 11 - passwordLength other codes (inceasing & decreasing)
        if (passwordLength != 1) {
            increasingCodes = 11 - passwordLength;
            decreasingCodes = 11 - passwordLength;
        } else //when passLength is 1 we have none
            increasingCodes = decreasingCodes = -1;

        biggestNr = getBiggestNumber();
        usedCodes = new boolean[biggestNr + 1];
    }

    public boolean hasNext() {
        if (passwordLength == 1 && !length1complete)
            return true;
        else if (passwordLength ==1 && length1complete)
            return false;
        //when passwordLength is not 1, we have to iterate increasing, decreasing and all other codes as well
        //counter represents the actual code
        else if (counter < sameDigitCodes || counter < increasingCodes || counter < decreasingCodes || counter < biggestNr)
            return true;
        return false;
    }

    public String next() {
        if (!hasNext())
            Util.noSuchElement("PasswordIterator has no more elements");

        //we have 4 categories of code that we have to use
        //after using all codes of one category we set the number to -1 and the counter to 0 as there are no more left to use
        //for every code we use we set its index in the boolean array as true
        //in the end return the code in String format of passwordLength using the Util.class
        if (counter < sameDigitCodes) {
            int code = getSameDigitCode(counter++);
            usedCodes[code] = true;
            if (counter == sameDigitCodes) {
                sameDigitCodes = -1;
                counter = 0;
                length1complete = true;
            }
            return code == 0 ? Util.longToStringWithLength(code,passwordLength) : "" + code;

        } else if (counter < increasingCodes) {
            int code = getNextIncreasing(counter++);
            usedCodes[code] = true;
            if (counter == increasingCodes) {
                increasingCodes = -1;
                counter = 0;
            }
            return Util.longToStringWithLength(code,passwordLength);

        } else if (counter < decreasingCodes) {
            int code = getNextDecreasing(counter++);
            usedCodes[code] = true;
            if (counter == decreasingCodes) {
                decreasingCodes = -1;
                counter = 0;
            }
            return Util.longToStringWithLength(code,passwordLength);

        } else if (counter < biggestNr) {
            //as long as codes are used we increase to the next one
            while (usedCodes[counter])
                counter++;
            usedCodes[counter] = true;
            int ret = counter; //we mark the code that we'll return
            //we let the counter increase if there are no more codes left to use --- helping so the hasNext() method function correctly
            while (counter < usedCodes.length && usedCodes[counter])
                counter++;
            return Util.longToStringWithLength(ret,passwordLength);

        } else Util.noSuchElement("error! we only return the codes above!");

        throw new RuntimeException("error! we only return the codes above!");
    }

    //returns number made of all 9s based on passwordLength
    private int getBiggestNumber() {
        return getSameDigitCode(9);
    }

    //helper method
    //returns a code of repeating digits based on @passwordLength
    private int getSameDigitCode(int digit) {
        int max = 1;
        for (int i = 1; i <= passwordLength; i++) {
            max *= 10;
        }

        int sum = 0;
        for (int i = 1; i < max; i*=10) {
            sum += digit * i;
        }
        return sum;
    }

    //helper method
    //returns the next code in increasing order based on @passwordLength
    private int getNextIncreasing(int number) {
        int res = 0;
        for (int i = passwordLength - 1; i >= 0; i--) {
            res += number++ * Math.pow(10,i);
        }
        return res;
    }

    //helper method
    //returns the next code in decreasing order based on @passwordLength
    private int getNextDecreasing(int number) {
        int res = 0;
        for (int i = 0; i < passwordLength; i++) {
            res += number++ * Math.pow(10,i);
        }
        return res;
    }

}
