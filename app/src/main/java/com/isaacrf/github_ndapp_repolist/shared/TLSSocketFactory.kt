package com.isaacrf.github_ndapp_repolist.shared

import android.os.Build
import java.net.InetAddress
import java.net.Socket
import java.security.KeyStore
import javax.net.ssl.*

/**
 * TLS Socket Factory to patch and enable TLSv1.2 on Android 4.1>, which only has
 * TLSv1.0 by default enabled. This causes SSL handshake problems with GitHub API, that only
 * supports TLSv1.2 and 1.3
 */
class TLSSocketFactory : SSLSocketFactory {

    private val internalSSLSocketFactory: SSLSocketFactory

    constructor(internalSSLSocketFactory: SSLSocketFactory) {
        this.internalSSLSocketFactory = internalSSLSocketFactory
    }

    constructor() {
        val context: SSLContext = SSLContext.getInstance("TLS")
        context.init(null, null, null)
        this.internalSSLSocketFactory = context.socketFactory
    }

    private val protocols = arrayOf("TLSv1.2")
    private val requiredCiphers = listOf<String>(
        // TLS 1.2
        "TLS_RSA_WITH_AES_256_GCM_SHA384",
        "TLS_RSA_WITH_AES_128_GCM_SHA256",
        "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256",
        "TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256",
        "TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384",
        "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256",
        "TLS_ECHDE_RSA_WITH_AES_128_GCM_SHA256",
        // maximum interoperability
        "TLS_RSA_WITH_3DES_EDE_CBC_SHA",
        "TLS_RSA_WITH_AES_128_CBC_SHA",
        // additionally
        "TLS_RSA_WITH_AES_256_CBC_SHA",
        "TLS_ECDHE_ECDSA_WITH_3DES_EDE_CBC_SHA",
        "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA",
        "TLS_ECDHE_RSA_WITH_3DES_EDE_CBC_SHA",
        "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA")

    override fun getDefaultCipherSuites(): Array<String> = internalSSLSocketFactory.defaultCipherSuites

    override fun getSupportedCipherSuites(): Array<String> = internalSSLSocketFactory.supportedCipherSuites

    override fun createSocket(s: Socket, host: String, port: Int, autoClose: Boolean) =
        enableTLSOnSocket(internalSSLSocketFactory.createSocket(s, host, port, autoClose))

    override fun createSocket(host: String, port: Int) =
        enableTLSOnSocket(internalSSLSocketFactory.createSocket(host, port))

    override fun createSocket(host: String, port: Int, localHost: InetAddress, localPort: Int) =
        enableTLSOnSocket(internalSSLSocketFactory.createSocket(host, port, localHost, localPort))

    override fun createSocket(host: InetAddress, port: Int) =
        enableTLSOnSocket(internalSSLSocketFactory.createSocket(host, port))

    override fun createSocket(address: InetAddress, port: Int, localAddress: InetAddress, localPort: Int) =
        enableTLSOnSocket(internalSSLSocketFactory.createSocket(address, port, localAddress, localPort))

    /**
     * Enables TLS on specified socket
     */
    private fun enableTLSOnSocket(socket: Socket?) = socket?.apply {
        if (this is SSLSocket && isTLSServerEnabled(this)) {
            enabledProtocols = protocols

            //Enable required TLS Ciphers for Android <5.0
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                val preferredCiphers = requiredCiphers.filter { supportedCipherSuites.contains(it) }
                enabledCipherSuites = preferredCiphers.toTypedArray()
            }
        }
    }

    /**
     * Determines if TLS Server is enabled for specified socket
     */
    private fun isTLSServerEnabled(sslSocket: SSLSocket) = sslSocket.supportedProtocols.any { it in protocols }

    /**
     * Gets X509 Trust Manager for this Socket Factory
     */
    fun getTrustManager(): X509TrustManager {
        val trustManagerFactory: TrustManagerFactory =
            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(null as KeyStore?)
        val trustManagers: Array<TrustManager> = trustManagerFactory.trustManagers
        check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
            "Unexpected default trust managers:" + trustManagers.contentToString()
        }

        return trustManagers[0] as X509TrustManager
    }

}