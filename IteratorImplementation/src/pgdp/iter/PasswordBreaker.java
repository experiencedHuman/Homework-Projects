package pgdp.iter;

import java.util.function.Function;

public class PasswordBreaker {

    public static String findPassword(Function<String, Integer> hashFunction, int passwordLength,
            String salt, int saltedPasswordHashValue) {
        if (salt == null)
            Util.badArgument("salt should not be null");
        if (hashFunction == null)
            Util.badArgument("hashFunction should not be null");

        PasswordIterator passwordIterator = new PasswordIterator(passwordLength);

        //iterate all passwords and concatenate them with @salt
        //if the password is found when hashed return it
        while (passwordIterator.hasNext()) {
            String password = passwordIterator.next();
            String saltedPassword = password + salt;
            if (hashFunction.apply(saltedPassword) == saltedPasswordHashValue) {
                return password;
            }
        }
        return null;
    }

}
