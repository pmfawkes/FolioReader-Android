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

open class ContainerEpub : EpubContainer, ZipArchiveContainer {

    override fun xmlDocumentForFile(relativePath: String): XmlParser {
        val containerData = data(relativePath)
        val document = XmlParser()
        document.parseXml(containerData.inputStream())
        return document
    }

    override fun xmlDocumentForResource(link: Link?): XmlParser {
        var pathFile = link?.href ?: throw Exception("Missing Link : ${link?.title}")
        if (pathFile.first() == '/')
            pathFile = pathFile.substring(1)
        return xmlDocumentForFile(pathFile)
    }

    override var rootFile: RootFile
    override var zipFile: ZipFile
    override var drm: Drm? = null
    override var successCreated: Boolean = false

    constructor(path: String) {

        if (File(path).exists()) {
            successCreated = true
        }
        zipFile = ZipFile(path)
        rootFile = RootFile(path, mimetype)
    }
}