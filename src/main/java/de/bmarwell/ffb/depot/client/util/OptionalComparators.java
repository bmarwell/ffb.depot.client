/*
 *  Copyright 2018 The ffb.depot.client contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package de.bmarwell.ffb.depot.client.util;

import java.util.Comparator;
import java.util.Optional;

public class OptionalComparators {

  private static final OptionalComparator<? extends Comparable<?>> ABSENT_FIRST = new AbsentFirst<>();

  private static final OptionalComparator<? extends Comparable<?>> ABSENT_LAST = new AbsentLast<>();

  public static <T extends Comparable<T>> OptionalComparator<T> absentFirstComparator() {
    final OptionalComparator<T> comp = (OptionalComparator<T>) ABSENT_FIRST;
    return comp;
  }

  public static <T extends Comparable<T>> OptionalComparator<T> absentLastComparator() {
    final OptionalComparator<T> comp = (OptionalComparator<T>) ABSENT_LAST;
    return comp;
  }


  private interface OptionalComparator<T extends Comparable<T>> extends Comparator<Optional<T>> {

  }

  private static class AbsentFirst<T extends Comparable<T>> implements OptionalComparator<T> {

    @Override
    public int compare(final Optional<T> obj1, final Optional<T> obj2) {
      if (obj1.isPresent() && obj2.isPresent()) {
        return obj1.get().compareTo(obj2.get());
      } else if (obj1.isPresent()) {
        return -1;
      } else if (obj2.isPresent()) {
        return 1;
      } else {
        return 0;
      }
    }
  }

  private static class AbsentLast<T extends Comparable<T>> implements OptionalComparator<T> {

    @Override
    public int compare(final Optional<T> obj1, final Optional<T> obj2) {
      if (obj1.isPresent() && obj2.isPresent()) {
        return obj1.get().compareTo(obj2.get());
      } else if (obj1.isPresent()) {
        return 1;
      } else if (obj2.isPresent()) {
        return -1;
      } else {
        return 0;
      }
    }
  }

}
