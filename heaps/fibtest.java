import org.junit.*;
import org.junit.rules.Timeout;

import java.util.*;
import java.io.*;
import java.util.concurrent.TimeUnit;

import static edu.gvsu.mipsunit.munit.MUnit.Register.*;
import static edu.gvsu.mipsunit.munit.MUnit.*;
import static edu.gvsu.mipsunit.munit.MARSSimulator.*;

import org.junit.rules.Timeout;
import java.util.concurrent.TimeUnit;

public class fibtest {

  int sp = 0;
  int s0 = 0;
  int s1 = 0;
  int s2 = 0;
  int s3 = 0;
  int s4 = 0;
  int s5 = 0;
  int s6 = 0;
  int s7 = 0;
  int gp = 0;

  private int[] fibonacci_nums = {
    0, 1, 1, 2, 3, 5, 8, 13, 21, 34,
    55, 89, 144, 233, 377, 610, 987, 1597, 2584, 4181,
    6765, 10946, 17711, 28657, 46368, 75025, 121393, 196418, 317811, 514229,
    832040, 1346269, 2178309, 3524578, 5702887, 9227465, 14930352, 24157817, 39088169, 63245986,
    102334155, 165580141, 267914296, 433494437, 701408733, 1134903170, 1836311903
  };

  @Before
  public void preTest() {
    s0 = get(s0);
    s1 = get(s1);
    s2 = get(s2);
    s3 = get(s3);
    s4 = get(s4);
    s5 = get(s5);
    s6 = get(s6);
    s7 = get(s7);
    sp = get(sp);
    gp = get(gp);
  }

  @After
  public void postTest() {
    Assert.assertEquals("Register convention violated $s0", s0, get(s0));
    Assert.assertEquals("Register convention violated $s1", s1, get(s1));
    Assert.assertEquals("Register convention violated $s2", s2, get(s2));
    Assert.assertEquals("Register convention violated $s3", s3, get(s3));
    Assert.assertEquals("Register convention violated $s4", s4, get(s4));
    Assert.assertEquals("Register convention violated $s5", s5, get(s5));
    Assert.assertEquals("Register convention violated $s6", s6, get(s6));
    Assert.assertEquals("Register convention violated $s7", s7, get(s7));
    Assert.assertEquals("Register convention violated $sp", sp, get(sp));
    Assert.assertEquals("Register convention violated $gp", gp, get(gp));
  }

  @Rule
  public Timeout timeout = new Timeout(30000, TimeUnit.MILLISECONDS);


  @Test
  public void test_zero() {
    Label output_buffer = wordData(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0); 
    run("fibonacci", 0,output_buffer);
    Assert.assertEquals(0, get(v0));
  }

  @Test
  public void test_one() {
    Label output_buffer = wordData(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0); 
    run("fibonacci", 1,output_buffer);
    Assert.assertEquals(1, get(v0));
  }

  @Test
  public void fib_25() {
    Label output_buffer = wordData(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0); 
    for (int n=2; n<25; n++){
      run("fibonacci", n, output_buffer); 
      Assert.assertEquals("fibonacci failed at n=" + n, fibonacci_nums[n], get(v0));
      // test buffer
      for (Integer i=0; i<n; i++) Assert.assertEquals("failed at buffer_val=" + i.toString(), fibonacci_nums[i], getWord(getWord(output_buffer.address()+i*4)));
    }
  }

  @Test
  public void test_fib_0() {
    Label output_buffer = wordData(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0); 
    run("test_fib_0", output_buffer); 
    Assert.assertEquals(fibonacci_nums[21], get(v0));
    for (int n=0;n<21;n++) Assert.assertEquals("fibonacci failed at n="+n, fibonacci_nums[n], getWord(getWord(output_buffer.address()+n*4)));
  }
}