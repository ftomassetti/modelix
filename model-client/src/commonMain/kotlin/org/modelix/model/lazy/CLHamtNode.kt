/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License. 
 */

package org.modelix.model.lazy

import org.modelix.model.persistent.CPHamtInternal
import org.modelix.model.persistent.CPHamtLeaf
import org.modelix.model.persistent.CPHamtNode
import org.modelix.model.util.BiPredicate

expect abstract class CLHamtNode<E : CPHamtNode?>(store_: IDeserializingKeyValueStore) {
    protected var store: IDeserializingKeyValueStore

    abstract operator fun get(key: Long, shift: Int, bulkQuery: IBulkQuery?): IBulkQuery.Value<String?>?
    abstract fun put(key: Long, value: String?, shift: Int): CLHamtNode<*>?
    abstract fun remove(key: Long, shift: Int): CLHamtNode<*>?
    abstract fun visitEntries(visitor: BiPredicate<Long?, String?>?): Boolean
    abstract fun visitChanges(oldNode: CLHamtNode<*>?, visitor: IChangeVisitor?)

    abstract fun getData(): CPHamtNode?

    protected fun createEmptyNode(): CLHamtNode<*>

    interface IChangeVisitor {
        fun entryAdded(key: Long, value: String?)
        fun entryRemoved(key: Long, value: String?)
        fun entryChanged(key: Long, oldValue: String?, newValue: String?)
    }
}

object CLHamtNodeCompanion {
    const val BITS_PER_LEVEL = 5
    const val ENTRIES_PER_LEVEL = 1 shl BITS_PER_LEVEL
    const val LEVEL_MASK = -0x1 ushr 32 - BITS_PER_LEVEL
    const val MAX_BITS = 64
    const val MAX_SHIFT = MAX_BITS - BITS_PER_LEVEL

    fun create(data: CPHamtNode?, store: IDeserializingKeyValueStore?): CLHamtNode<*>? {
        if (data == null) {
            return null
        }
        return when (data) {
            is CPHamtLeaf -> {
                CLHamtLeaf((data as CPHamtLeaf?)!!, store)
            }
            is CPHamtInternal -> {
                CLHamtInternal((data as CPHamtInternal?)!!, store)
            }
            else -> {
                throw RuntimeException("Unknown type: $data")
            }
        }
    }

    fun logicalToPhysicalIndex(bitmap: Int, logicalIndex: Int): Int {
        return bitCount(bitmap and (1 shl logicalIndex) - 1)
    }

    fun isBitNotSet(bitmap: Int, logicalIndex: Int): Boolean {
        return bitmap and (1 shl logicalIndex) == 0
    }
}

fun bitCount(i: Int): Int {
    var i = i
    i -= (i ushr 1 and 0x55555555)
    i = (i and 0x33333333) + (i ushr 2 and 0x33333333)
    i = i + (i ushr 4) and 0x0f0f0f0f
    i += (i ushr 8)
    i += (i ushr 16)
    return i and 0x3f
}
