
/*
 * This file is public domain.
 *
 * SWIRLDS MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF 
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED 
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. SWIRLDS SHALL NOT BE LIABLE FOR 
 * ANY DAMAGES SUFFERED AS A RESULT OF USING, MODIFYING OR 
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


import com.swirlds.platform.Address;
import com.swirlds.platform.AddressBook;
import com.swirlds.platform.FCDataInputStream;
import com.swirlds.platform.FCDataOutputStream;
import com.swirlds.platform.FastCopyable;
import com.swirlds.platform.Platform;
import com.swirlds.platform.SwirldState;


public class MyFirstDappState implements SwirldState {

    private AddressBook addressBook;
    private Map<String, String> balanceBook = Collections
            .synchronizedMap(new HashMap<>());


	public synchronized String getReceived() {
        return null;
	}

	public String toString() {
		return null;
	}

    @Override
    public synchronized AddressBook getAddressBookCopy() {
        return addressBook.copy();
    }

    @Override
    public synchronized FastCopyable copy() {
        MyFirstDappState copy = new MyFirstDappState();
        copy.copyFrom(this);
        return copy;
    }

    @Override
    public void copyTo(FCDataOutputStream outStream) {

    }

    @Override
    public void copyFrom(FCDataInputStream inStream) {

    }

    @Override
    public synchronized void copyFrom(SwirldState old) {

    }

    @Override
    public synchronized void handleTransaction(long id, boolean consensus,
                                               Instant timeCreated, byte[] transaction, Address address) {
    }

    @Override
    public void noMoreTransactions() {
    }

    @Override
    public synchronized void init(Platform platform, AddressBook addressBook) {
        this.addressBook = addressBook;
    }

}