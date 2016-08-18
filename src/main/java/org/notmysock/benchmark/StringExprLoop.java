package org.notmysock.benchmark;

import java.util.Arrays;

public final class StringExprLoop {

  private final byte[] pattern; 
  private final int plen;
  public StringExprLoop(byte[] pattern) {
    this.pattern = pattern;
    this.plen = pattern.length;
  }
  
  public int index(byte[] byteS, int start, int len) {
    if (len < pattern.length) {
      return -1;
    }
    int end = start + len - pattern.length + 1;
    for (int i = start; i < end; i++) {
      if (equal(pattern, 0, pattern.length, byteS, i, pattern.length)) {
        return i;
      }
    }
    return -1;
  }

  public static boolean equal(byte[] arg1, final int start1, final int len1,
      byte[] arg2, final int start2, final int len2) {
    if (len1 != len2) {
      return false;
    }
    if (len1 == 0) {
      return true;
    }

    // do bounds check for OOB exception
    if (arg1[start1] != arg2[start2]
        || arg1[start1 + len1 - 1] != arg2[start2 + len2 - 1]) {
      return false;
    }

    if (len1 == len2) {
      // prove invariant to the compiler: len1 = len2
      // all array access between (start1, start1+len1) 
      // and (start2, start2+len2) are valid
      // no more OOB exceptions are possible
      final int step = 8;
      final int remainder = len1 % step;
      final int wlen = len1 - remainder;
      // suffix first
      for (int i = wlen; i < len1; i++) {
        if (arg1[start1 + i] != arg2[start2 + i]) {
          return false;
        }
      }
      // SIMD loop
      for (int i = 0; i < wlen; i += step) {
        final int s1 = start1 + i;
        final int s2 = start2 + i;
        boolean neq = false;
        for (int j = 0; j < step; j++) {
          neq = (arg1[s1 + j] != arg2[s2 + j]) || neq;
        }
        if (neq) {
          return false;
        }
      }
    }

    return true;
  }
}
