import com.ciicc.carlosgo.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GcashAppTest {
    @Test
    public void testUserAuthentication(){
        UserAuthentication user1 = new UserAuthentication();
        int loginUserID = user1.Login(9265305606L, (short) 1234);
        int wrongLoginID = user1.Login(9265305606L, (short) 1233);
        assertEquals(1, loginUserID);
        assertNotEquals(1, wrongLoginID);
    }
    @Test
    @Order(1)
    public void testCheckBalance(){
        UserAuthentication user1 = new UserAuthentication();
        int loginUserID = user1.Login(9265305606L, (short) 1234);
        CheckBalance checkBal = new CheckBalance();
        float balance = checkBal.checkBalance(loginUserID);
        assertEquals(2200f, balance); // Original balance for userID 1 is "2200f"
    }
    @Test
    public void testCashIn(){
        UserAuthentication user1 = new UserAuthentication();
        CheckBalance checkBal = new CheckBalance();
        CashIn cashIn = new CashIn();
        int loginUserID = user1.Login(9265305606L, (short) 1234);
        float oldBalance = checkBal.checkBalance(loginUserID);
        cashIn.cashIn(loginUserID, 200f, 9265305607L);
        float newBalance = checkBal.checkBalance(loginUserID);
        assertTrue(newBalance > oldBalance);
    }
    @Test
    public void testCashTransfer(){
        UserAuthentication user1 = new UserAuthentication();
        UserAuthentication user2 = new UserAuthentication();
        CheckBalance checkBal = new CheckBalance();
        CashTransfer cashTrans = new CashTransfer();
        int loginUserID1 = user1.Login(9265305606L, (short) 1234);
        int loginUserID2 = user2.Login(9265305607L, (short) 1234);
        float user1_oldBalance = checkBal.checkBalance(loginUserID1);
        float user2_oldBalance = checkBal.checkBalance(loginUserID2);
        cashTrans.cashTransfer(loginUserID1, 500f, 9265305607L);
        float user1_newBalance = checkBal.checkBalance(loginUserID1);
        float user2_newBalance = checkBal.checkBalance(loginUserID2);
        assertTrue(user1_newBalance < user1_oldBalance);
        assertTrue(user2_newBalance > user2_oldBalance);
    }
    @Test
    public void testTransactions(){
        UserAuthentication user1 = new UserAuthentication();
        Transactions transactions = new Transactions();
        int loginUserID1 = user1.Login(9265305606L, (short) 1234);
        // Capture System.out
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(output));
        transactions.viewUserAll(loginUserID1);
        System.setOut(originalOut); // Restore original System.out
        String result = output.toString();
        assertFalse(result.contains("No Transaction History Found."));

    }

}
