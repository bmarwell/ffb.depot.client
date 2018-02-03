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
