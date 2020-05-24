package pgdp.strings;

public class MyString {
    private char[] data; //saves characters for this object
    private MyString next; //point to the next element of this string list
    private MyString tail; //points to last element of this string list

    //constructor
    public MyString(char[] data) {
        this.data = data;
        next = null;
        tail = null;
    }

    //returns length of the list
    public int length() {
        int length = data.length;

        //iterate list and increment length by size of each @data array of every object in the list
        MyString temp = this.next;
        while (temp != null) {
            length += temp.data.length;
            temp = temp.next;
        }
        return length;
    }

    //concatenates a new MyString object to this object by initializing it with the parameter @data
    public void concat(char[] data) {
    	MyString myNewString = new MyString(data);
    	if(next == null) {
    	    //list was empty so update both next and tail
    		next = myNewString;
    		tail = myNewString;
    	} else {
    	    //if list is not empty, update only tail
    		tail.next = myNewString;
    		tail = myNewString;
    	}
    }

    //returns String representing all the characters that the list has
    public String toString() {
        char[] chainedString = new char[this.length()];
        int index = 0;

        //copy all chars of @data of this object to a bigger array of the length of the list
        for (Character tempChar : data) {
        	chainedString[index++] = tempChar;
        }

        //iterate to the end of the list and copy all chars into the char array
        MyString tempNext = this.next;
        while (tempNext != null) {
            //copy new letter to new char Array
            for (Character letterNeighbour : tempNext.data) {
                chainedString[index++] = letterNeighbour;
            }
            tempNext = tempNext.next;
        }
        return new String(chainedString);
    }


    //returns true @this points to the same list as @other
    public boolean equals(MyString other) {
        if (other == null)
            return false;

        //we need to variables to point to the characters of each object in the lists that we'll be comparing
        int index1 = 0, index2 = 0;

        //iterate two lists simultaneously using two pointers
        MyString str1 = this;
        MyString str2 = other;
        while (true) {

            //when end of one object is reached update pointer to point to the next one
            if (index1 == str1.data.length){
                str1 = str1.next;
                index1 = 0;
            }

            //end of object in list two is reached so update 2nd pointer
            if (index2 == str2.data.length) {
                str2 = str2.next;
                index2 = 0;
            }

            //we reached end so both lists are the same and we return true
            if (str1 == null && str2 == null)
                return true;

            //if we reach end for one list but not for the other we return false
            if (str1 == null && str2 != null || str1 != null && str2 == null)
                return false;

            //if two characters don't match we return false
            if (str1.data[index1++] != str2.data[index2++]) {
                return false;
            }

        }
    }

    //returns the first index in which the param @c was found
    public int indexOf(char c) {
        int index = 0;
        for (; index < data.length; index++) {
            if (data[index] == c)
                return index;
        }

        //c was not found in the char array of this object so search list
        int longerIndex = index;
        MyString temp = this.next;
        while (temp != null) {
            //search the char array for every object in the list
            //update index along the way as it gets bigger
            for (index = 0; index < temp.data.length; index++, longerIndex++) {
                if (temp.data[index] == c)
                    return longerIndex;
            }
            temp = temp.next;
        }

        return -1;
    }


    //returns the last index in which the param @c was found
    public int lastIndexOf(char c) {
        int lastIndex = 0, index = 0, updateCounter = 0;

        //i use a boolean isPresent to not get confused with the index if the last index happened to be 0
        boolean isPresent = false;

        //iterate char array of this object and update index as well as counter
        for (; index < data.length; index++) {
            if (data[index] == c) {
                //if @c is found we update lastIndex
                lastIndex = updateCounter;
                isPresent = true;
            }
            updateCounter++;
        }

        //we return nothing yet since we have to search the whole list
        MyString temp = this.next;
        while (temp != null) {
            //do the same search for every object in the list
            for (index = 0; index < temp.data.length; index++) {
                if (temp.data[index] == c){
                    lastIndex = updateCounter;
                    isPresent = true;
                }
               	updateCounter++;
            }
            temp = temp.next;
        }

        if (isPresent)
            return lastIndex;
        return -1;
    }
}