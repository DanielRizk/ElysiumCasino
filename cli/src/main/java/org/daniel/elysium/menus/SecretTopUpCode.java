package org.daniel.elysium.menus;

import org.daniel.elysium.debugUtils.DebugPrint;
import org.daniel.elysium.interfaces.MenuOptionCLI;
import org.daniel.elysium.user.profile.UserProfile;

/**
 * A hidden menu option that allows users to increase their balance by 10,000.
 * <p>
 * This class implements {@code MenuOptionCLI} and grants a secret top-up when accessed.
 * </p>
 */
public class SecretTopUpCode implements MenuOptionCLI {

    /**
     * Executes the secret top-up action.
     * <p>
     * Increases the user's balance by 10,000 and displays the new balance.
     * </p>
     *
     * @param profile the {@code UserProfile} of the current user
     */
    @Override
    public void start(UserProfile profile) {
        DebugPrint.println("Secret code entered successfully, balance increased by 10,000");
        profile.increaseBalanceBy(10000);
        DebugPrint.println("Your new balance is: " + profile.getBalance());
    }

    /** Returns Menu's exit code, default 0 */
    @Override
    public int exitCode() {
        return 0;
    }
}
