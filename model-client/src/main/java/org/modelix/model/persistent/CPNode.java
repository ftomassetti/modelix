package org.modelix.model.persistent;

/*Generated by MPS */

import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;
import jetbrains.mps.baseLanguage.closures.runtime._FunctionTypes;
import org.apache.log4j.Level;
import jetbrains.mps.internal.collections.runtime.IterableUtils;
import jetbrains.mps.internal.collections.runtime.Sequence;
import jetbrains.mps.internal.collections.runtime.ArrayUtils;
import jetbrains.mps.internal.collections.runtime.ISelector;
import java.util.Arrays;
import java.util.List;
import jetbrains.mps.internal.collections.runtime.ListSequence;
import java.util.Iterator;
import jetbrains.mps.internal.collections.runtime.IWhereFilter;
import de.q60.mps.shadowmodels.runtime.util.pmap.COWArrays;

public class CPNode extends CPElement {
  private static final Logger LOG = LogManager.getLogger(CPNode.class);
  private static final long[] EMPTY_LONG_ARRAY = new long[0];
  public static final _FunctionTypes._return_P1_E0<? extends CPNode, ? super String> DESERIALIZER = new _FunctionTypes._return_P1_E0<CPNode, String>() {
    public CPNode invoke(String s) {
      return deserialize(s);
    }
  };

  public static CPNode create(long id, String concept, long parentId, String roleInParent, long[] childrenIds, String[] propertyRoles, String[] propertyValues, String[] referenceRoles, CPElementRef[] referenceTargets) {
    checkForDuplicates(childrenIds);
    if (propertyRoles.length != propertyValues.length) {
      throw new IllegalArgumentException(propertyRoles.length + " != " + propertyValues.length);
    }
    if (referenceRoles.length != referenceTargets.length) {
      throw new IllegalArgumentException(referenceRoles.length + " != " + referenceTargets.length);
    }
    return new CPNode(id, concept, parentId, roleInParent, childrenIds, propertyRoles, propertyValues, referenceRoles, referenceTargets);
  }

  private final long[] childrenIds;
  private final String concept;
  private String[] propertyRoles;
  private String[] propertyValues;
  private String[] referenceRoles;
  private CPElementRef[] referenceTargets;

  protected CPNode(long id1, String concept, long parentId1, String roleInParent1, long[] childrenIds1, String[] propertyRoles, String[] propertyValues, String[] referenceRoles, CPElementRef[] referenceTargets) {
    super(id1, parentId1, roleInParent1);

    // TODO remove this fix once there is no more broken data 
    if (hasDuplicates(childrenIds1)) {
      if (LOG.isEnabledFor(Level.WARN)) {
        LOG.warn("Duplicate children fixed in " + Long.toHexString(id1) + ": " + IterableUtils.join(Sequence.fromIterable(ArrayUtils.fromLongArray(childrenIds1)).select(new ISelector<Long, String>() {
          public String select(Long it) {
            return Long.toHexString(it);
          }
        }), ", "), new Exception());
      }
      childrenIds1 = removeDuplicates(childrenIds1);
    }

    this.childrenIds = childrenIds1;
    this.concept = concept;
    this.propertyRoles = propertyRoles;
    this.propertyValues = propertyValues;
    this.referenceRoles = referenceRoles;
    this.referenceTargets = referenceTargets;
  }

  private static void checkForDuplicates(long[] values) {
    long[] copy = new long[values.length];
    System.arraycopy(values, 0, copy, 0, values.length);
    Arrays.sort(copy);
    for (int i = 1; i < copy.length; i++) {
      if (copy[i - 1] == copy[i]) {
        throw new RuntimeException("Duplicate value: " + copy[i]);
      }
    }
  }
  private static boolean hasDuplicates(long[] values) {
    long[] copy = new long[values.length];
    System.arraycopy(values, 0, copy, 0, values.length);
    Arrays.sort(copy);
    for (int i = 1; i < copy.length; i++) {
      if (copy[i - 1] == copy[i]) {
        return true;
      }
    }
    return false;
  }
  private static long[] removeDuplicates(long[] values) {
    List<Long> noDuplicates = Sequence.fromIterable(ArrayUtils.fromLongArray(values)).distinct().toListSequence();
    long[] result = new long[ListSequence.fromList(noDuplicates).count()];
    for (int i = 0; i < result.length; i++) {
      result[i] = ListSequence.fromList(noDuplicates).getElement(i);
    }
    return result;
  }

  @Override
  public String serialize() {
    StringBuilder sb = new StringBuilder();

    sb.append(SerializationUtil.longToHex(id));
    sb.append("/");
    sb.append(SerializationUtil.escape(concept));
    sb.append("/");
    sb.append(SerializationUtil.longToHex(parentId));
    sb.append("/");
    sb.append(SerializationUtil.escape(roleInParent));
    sb.append("/");
    sb.append(IterableUtils.join(Sequence.fromIterable(ArrayUtils.fromLongArray(childrenIds)).select(new ISelector<Long, String>() {
      public String select(Long it) {
        return SerializationUtil.longToHex(it);
      }
    }), ","));
    sb.append("/");
    boolean first = true;
    {
      Iterator<String> role_it = Sequence.fromArray(propertyRoles).iterator();
      Iterator<String> value_it = Sequence.fromArray(propertyValues).iterator();
      String role_var;
      String value_var;
      while (role_it.hasNext() && value_it.hasNext()) {
        role_var = role_it.next();
        value_var = value_it.next();
        if (first) {
          first = false;
        } else {
          sb.append(",");
        }
        sb.append(SerializationUtil.escape(role_var)).append("=").append(SerializationUtil.escape(value_var));
      }
    }
    sb.append("/");
    first = true;
    {
      Iterator<String> role_it = Sequence.fromArray(referenceRoles).iterator();
      Iterator<CPElementRef> value_it = Sequence.fromArray(referenceTargets).iterator();
      String role_var;
      CPElementRef value_var;
      while (role_it.hasNext() && value_it.hasNext()) {
        role_var = role_it.next();
        value_var = value_it.next();
        if (first) {
          first = false;
        } else {
          sb.append(",");
        }
        sb.append(SerializationUtil.escape(role_var)).append("=").append(SerializationUtil.escape(value_var.toString()));
      }
    }

    return sb.toString();
  }

  public static CPNode deserialize(String input) {
    try {
      String[] parts = input.split("/", -1);

      Iterable<String[]> properties = Sequence.fromIterable(Sequence.fromArray(parts[5].split(","))).where(new IWhereFilter<String>() {
        public boolean accept(String it) {
          return (it != null && it.length() > 0);
        }
      }).select(new ISelector<String, String[]>() {
        public String[] select(String it) {
          return it.split("=", -1);
        }
      });
      Iterable<String[]> references = Sequence.fromIterable(Sequence.fromArray(parts[6].split(","))).where(new IWhereFilter<String>() {
        public boolean accept(String it) {
          return (it != null && it.length() > 0);
        }
      }).select(new ISelector<String, String[]>() {
        public String[] select(String it) {
          return it.split("=", -1);
        }
      });

      return new CPNode(SerializationUtil.longFromHex(parts[0]), SerializationUtil.unescape(parts[1]), SerializationUtil.longFromHex(parts[2]), SerializationUtil.unescape(parts[3]), ArrayUtils.toLongArray(Sequence.fromIterable(Sequence.fromArray(parts[4].split(","))).where(new IWhereFilter<String>() {
        public boolean accept(String it) {
          return (it != null && it.length() > 0);
        }
      }).select(new ISelector<String, Long>() {
        public Long select(String it) {
          return SerializationUtil.longFromHex(it);
        }
      })), Sequence.fromIterable(properties).select(new ISelector<String[], String>() {
        public String select(String[] it) {
          return SerializationUtil.unescape(it[0]);
        }
      }).toGenericArray(String.class), Sequence.fromIterable(properties).select(new ISelector<String[], String>() {
        public String select(String[] it) {
          return SerializationUtil.unescape(it[1]);
        }
      }).toGenericArray(String.class), Sequence.fromIterable(references).select(new ISelector<String[], String>() {
        public String select(String[] it) {
          return SerializationUtil.unescape(it[0]);
        }
      }).toGenericArray(String.class), Sequence.fromIterable(references).select(new ISelector<String[], CPElementRef>() {
        public CPElementRef select(String[] it) {
          return CPElementRef.fromString(SerializationUtil.unescape(it[1]));
        }
      }).toGenericArray(CPElementRef.class));
    } catch (Exception ex) {
      throw new RuntimeException("Failed to deserialize " + input, ex);
    }
  }

  public Iterable<Long> getChildrenIds() {
    return ArrayUtils.fromLongArray(childrenIds);
  }

  public long[] getChildrenIdArray() {
    return COWArrays.copy(childrenIds);
  }

  public int getChildrenSize() {
    return childrenIds.length;
  }

  public long getChildId(int index) {
    return childrenIds[index];
  }

  public String getConcept() {
    return concept;
  }

  public String getPropertyValue(String role) {
    int index = Arrays.binarySearch(propertyRoles, role);
    return (index < 0 ? null : propertyValues[index]);
  }

  public CPElementRef getReferenceTarget(String role) {
    int index = Arrays.binarySearch(referenceRoles, role);
    return (index < 0 ? null : referenceTargets[index]);
  }

  public CPNode withPropertyValue(String role, String value) {
    int index = Arrays.binarySearch(propertyRoles, role);
    if (value == null) {
      if (index < 0) {
        return this;
      } else {
        return create(id, concept, parentId, roleInParent, childrenIds, COWArrays.removeAt(propertyRoles, index), COWArrays.removeAt(propertyValues, index), referenceRoles, referenceTargets);
      }
    } else {
      if (index < 0) {
        index = -(index + 1);
        return create(id, concept, parentId, roleInParent, childrenIds, COWArrays.insert(propertyRoles, index, role), COWArrays.insert(propertyValues, index, value), referenceRoles, referenceTargets);
      } else {
        return create(id, concept, parentId, roleInParent, childrenIds, propertyRoles, COWArrays.set(propertyValues, index, value), referenceRoles, referenceTargets);
      }
    }
  }

  public CPNode withReferenceTarget(String role, CPElementRef target) {
    int index = Arrays.binarySearch(referenceRoles, role);
    if (target == null) {
      if (index < 0) {
        return this;
      } else {
        return create(id, concept, parentId, roleInParent, childrenIds, propertyRoles, propertyValues, COWArrays.removeAt(referenceRoles, index), COWArrays.removeAt(referenceTargets, index));
      }
    } else {
      if (index < 0) {
        index = -(index + 1);
        return create(id, concept, parentId, roleInParent, childrenIds, propertyRoles, propertyValues, COWArrays.insert(referenceRoles, index, role), COWArrays.insert(referenceTargets, index, target));
      } else {
        return create(id, concept, parentId, roleInParent, childrenIds, propertyRoles, propertyValues, referenceRoles, COWArrays.set(referenceTargets, index, target));
      }
    }
  }

  public String[] getPropertyRoles() {
    return this.propertyRoles;
  }
  public String[] getPropertyValues() {
    return this.propertyValues;
  }
  public String[] getReferenceRoles() {
    return this.referenceRoles;
  }
  public CPElementRef[] getReferenceTargets() {
    return this.referenceTargets;
  }
}
