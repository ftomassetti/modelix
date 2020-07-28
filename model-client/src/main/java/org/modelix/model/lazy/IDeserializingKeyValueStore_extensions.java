package org.modelix.model.lazy;

/*Generated by MPS */

import org.modelix.model.persistent.HashUtil;
import org.modelix.model.persistent.CPElement;

public class IDeserializingKeyValueStore_extensions {
  public IDeserializingKeyValueStore_extensions() {
  }
  public static void put(final IDeserializingKeyValueStore _this, Object deserialized, String serialized) {
    _this.put(HashUtil.sha256(serialized), deserialized, serialized);
  }
  public static void put(final IDeserializingKeyValueStore _this, CPElement element) {
    IDeserializingKeyValueStore_extensions.put(_this, element, element.serialize());
  }
}
