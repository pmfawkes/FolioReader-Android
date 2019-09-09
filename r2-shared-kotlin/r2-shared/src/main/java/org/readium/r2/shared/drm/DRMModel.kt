/*
 * Module: r2-shared-kotlin
 * Developers: Aferdita Muriqi, Clément Baumann
 *
 * Copyright (c) 2018. Readium Foundation. All rights reserved.
 * Use of this source code is governed by a BSD-style license which is detailed in the
 * LICENSE file present in the project repository where this source code is maintained.
 */

package org.readium.r2.shared.drm

import java.io.Serializable

data class DRMModel(val type: String, val state: String, val provider: String, val issued: String, val updated: String, val start: String?, val end: String?, val prints: String, val copies: String) : Serializable