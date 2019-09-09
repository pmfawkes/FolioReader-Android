/*
 * Module: r2-streamer-kotlin
 * Developers: Aferdita Muriqi, Clément Baumann
 *
 * Copyright (c) 2018. Readium Foundation. All rights reserved.
 * Use of this source code is governed by a BSD-style license which is detailed in the
 * LICENSE file present in the project repository where this source code is maintained.
 */

package org.readium.r2.streamer.container

import org.readium.r2.shared.drm.Drm
import java.io.File
import java.util.zip.ZipFile
import org.readium.r2.shared.Link
import org.readium.r2.shared.RootFile
import org.readium.r2.shared.parser.xml.XmlParser
import org.readium.r2.streamer.parser.mimetype
import timber.log.Timber
import java.io.InputStream
import java.util.Arrays
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class EncryptedContainerEpub(path: String, private val keyString: String) : ContainerEpub(path) {

    private var headerInfo = ByteArray(HEADER_INFO_DISPLACEMENT)

    override fun dataInputStream(relativePath: String): InputStream {
        Timber.tag("TRAVIS").v("IT'S CALLING INTO THE CODE, IT'S HAPPENING!!!")
        val key = AesHelper.getAesKey(keyString)
        val skeySpec = SecretKeySpec(key, AesHelper.ENCRYPTION_ALGORITHM)

        val ivSpec = IvParameterSpec(Arrays.copyOfRange(headerInfo, FILE_SIZE_BYTE_DISPLACEMENT, HEADER_INFO_DISPLACEMENT))

        val cipher = Cipher.getInstance(CIPHER_ENCODING)
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivSpec)

        val encrypted = super.dataInputStream(relativePath)
        Timber.tag("TRAVIS").v("RETURNING!!")
        return CipherInputStream(encrypted, cipher)
    }

    companion object {
        private const val FILE_SIZE_BYTE_DISPLACEMENT = 8
        private const val INITIAL_VALUE_BYTE_DISPLACEMENT = 16
        private const val HEADER_INFO_DISPLACEMENT = FILE_SIZE_BYTE_DISPLACEMENT + INITIAL_VALUE_BYTE_DISPLACEMENT

        private const val CIPHER_ENCODING = "AES/CBC/NoPadding"
    }
}