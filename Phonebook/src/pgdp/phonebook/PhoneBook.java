package pgdp.phonebook;

public class PhoneBook {
    private Entry[] entries;

    public PhoneBook(Entry[] entries) {
        this.entries = entries;
    }

    public String find(String firstName, String lastName) {
        int left = 0, right = entries.length - 1;

        //iterate as long as left doesn't cross right
        while (left <= right) {
            int mid = (left + right) / 2;

            //when both persons have the same lastName, check their firstNames otherwise move left to the right by 1
            if (lastName.compareTo(entries[mid].getLastName()) == 0) {
                //when lastNames match, check firstNames otherwise continue
                if (firstName.compareTo(entries[mid].getFirstName()) == 0)
                    return entries[mid].getPhoneNumber();
                else {
                    left++;
                    continue;
                }
            }

            //if lastName of actual person is smaller than the one of entries[mid] means we have to keep searching left
            if (lastName.compareTo(entries[mid].getLastName()) < 0) {
                right = mid - 1;
                continue;
            }

            //if lastName of acutal person is bigger than the one of entries[mid] means we have to keep searching right
            if (lastName.compareTo(entries[mid].getLastName()) > 0) {
                left = mid + 1;
            }
        }

        return null;
    }

//    public static void main (String[] args) {
//        Entry[] entries = new Entry[5];
//        entries[0] = new Entry("Agathe", "Ackermann", "555-11133322");
//        entries[1] = new Entry("Bernd", "Ackermann", "555-2251243");
//        entries[2] = new Entry("Winfried", "Becker", "555-225123");
//        entries[3] = new Entry("Alex", "Dieß", "555-343112");
//        entries[4] = new Entry("Beatrix", "Dieß",  "555-2123123");
//        PhoneBook phoneBook = new PhoneBook(entries);
//        // Hier sollte true ausgegeben werden
//        System.out.println(entries[0].getPhoneNumber().equals(phoneBook.find("Agathe", "Ackermann")));
//    }
}