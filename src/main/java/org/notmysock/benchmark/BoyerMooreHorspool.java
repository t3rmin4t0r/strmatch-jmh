package org.notmysock.benchmark;

import java.util.Arrays;

public final class BoyerMooreHorspool {
  private int MAX_BYTE = 0xff;
  private final long[] shift = new long[MAX_BYTE];
  private final byte[] pattern;
  private final int plen; 
  public BoyerMooreHorspool(byte[] pattern) {
    this.pattern = pattern;
    this.plen = pattern.length;
    Arrays.fill(shift, plen);
    for (int i = 0; i < plen - 1; i++) {
      shift[pattern[i]] = plen - i - 1;
    }
  }
  
  public int index(byte[] input, int start, int len) {
    final int end = start + len;
    int next = start + plen - 1;
    final int plen = this.plen;
    final byte[] pattern = this.pattern;
    while (next < end) {
      int s_tmp = next;
      int p_tmp = plen - 1;
      if (input[next - plen + 1] == pattern[0]) {
        while (input[s_tmp] == pattern[p_tmp]) {
          p_tmp--;
          if (p_tmp < 0) {
            return s_tmp;
          }
          s_tmp--;
        }
      }
      next += shift[input[next]];
    }
    return -1;
  }
}
