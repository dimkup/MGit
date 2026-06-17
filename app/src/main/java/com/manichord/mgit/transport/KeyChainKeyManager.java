package com.manichord.mgit.transport;

import android.content.Context;
import android.security.KeyChain;
import android.security.KeyChainException;

import java.net.Socket;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509KeyManager;

import timber.log.Timber;

/**
 * An X509KeyManager that retrieves the client certificate and private key
 * from the Android system KeyChain for a given alias.
 *
 * Must only be used on a background thread — KeyChain IPC calls block.
 */
public class KeyChainKeyManager implements X509KeyManager {

    private final Context mContext;
    private final String mAlias;

    public KeyChainKeyManager(Context context, String alias) {
        mContext = context;
        mAlias = alias;
    }

    @Override
    public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket) {
        return mAlias;
    }

    @Override
    public X509Certificate[] getCertificateChain(String alias) {
        try {
            return KeyChain.getCertificateChain(mContext, mAlias);
        } catch (KeyChainException | InterruptedException e) {
            Timber.e(e, "Failed to retrieve certificate chain for alias: %s", mAlias);
            return null;
        }
    }

    @Override
    public PrivateKey getPrivateKey(String alias) {
        try {
            return KeyChain.getPrivateKey(mContext, mAlias);
        } catch (KeyChainException | InterruptedException e) {
            Timber.e(e, "Failed to retrieve private key for alias: %s", mAlias);
            return null;
        }
    }

    @Override
    public String[] getClientAliases(String keyType, Principal[] issuers) {
        return new String[]{mAlias};
    }

    @Override
    public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
        return null;
    }

    @Override
    public String[] getServerAliases(String keyType, Principal[] issuers) {
        return null;
    }
}
