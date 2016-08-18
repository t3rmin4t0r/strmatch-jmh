/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.notmysock.benchmark;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.*;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(1)
@Measurement(batchSize=40960)
public class StrMatchBenchmark {

  private final byte[] input = "https://github.com/apache/hive/blob/fe14a9088853abc91740a54f4fe100a8a0cfced6/ql/src/java/org/apache/hadoop/hive/ql/exec/vector/expressions/AbstractFilterStringColLikeStringScalar.java"
      .getBytes(Charset.defaultCharset());
  
  private final byte[] match = "apache/hadoop"
      .getBytes(Charset.defaultCharset());
  
  private final byte[] miss = "APACHE/HADOOP"
      .getBytes(Charset.defaultCharset());
  
  private final StringExprLoop dumbMatch = new StringExprLoop(match);
  private final StringExprLoop dumbMiss = new StringExprLoop(miss);
  
  private final BoyerMooreHorspool boyerMatch = new BoyerMooreHorspool(match);
  private final BoyerMooreHorspool boyermiss = new BoyerMooreHorspool(miss);
  
  @Benchmark
  public int testDumbMatch() {
    return dumbMatch.index(input, 0, input.length);
  }

  @Benchmark
  public int testDumbMiss() {
    return dumbMiss.index(input, 0, input.length);
  }
  
  @Benchmark
  public int testBoyerMatch() {
    return boyerMatch.index(input, 0, input.length);
  }

  @Benchmark
  public int testBoyerMiss() {
    return boyermiss.index(input, 0, input.length);
  }

}
