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

public class Ones {

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
  public void verify_positive() {
    Label input_buffer = wordData(1,2,3,4,5,6,7,8,9,10, 0); // null terminated = 0 at end
    Label output_buffer = wordData(0,0,0,0,0, // 20 num buffer
                                   0,0,0,0,0,
                                   0,0,0,0,0,
                                   0,0,0,0,0); 

    run("onescompl", input_buffer,output_buffer);
    Assert.assertEquals("$v0 contains incorrect count",10, get(v0)); // 10 nums written
    for (int i=0;i<10;i++) {
      Assert.assertEquals("output_buffer contains incorrect value",
            -1*(i+2), getWord(output_buffer,i*4)); 
    }
  }

  @Test
  public void verify_positive_1() {
    Label input_buffer = wordData(2,3,4,5,6,7,8,9,10,11, 0); // null terminated = 0 at end
    Label output_buffer = wordData(0,0,0,0,0, // 20 num buffer
                                   0,0,0,0,0,
                                   0,0,0,0,0,
                                   0,0,0,0,0); 

    run("onescompl", input_buffer,output_buffer);
    Assert.assertEquals("$v0 contains incorrect count",10, get(v0)); // 10 nums written
    for (int i=0;i<10;i++) {
      Assert.assertEquals("output_buffer contains incorrect value",
            -1*(i+3), getWord(output_buffer,i*4)); 
    }
  }

  @Test
  public void verify_positive_2() {
    Label input_buffer = wordData(47,99, 0); // null terminated = 0 at end
    Label output_buffer = wordData(0,0,0,0,0, // 20 num buffer
                                   0,0,0,0,0,
                                   0,0,0,0,0,
                                   0,0,0,0,0); 

    run("onescompl", input_buffer,output_buffer);
    Assert.assertEquals("$v0 contains incorrect count",2, get(v0)); // 10 nums written
    Assert.assertEquals("output_buffer contains incorrect value",
            -47-1, getWord(output_buffer,0)); 
    Assert.assertEquals("output_buffer contains incorrect value",
            -99-1, getWord(output_buffer,4)); 
  }

  @Test
  public void verify_negative() {
    Label input_buffer = wordData(1,2,3,4,5,6,7,8,9,10,-1, 0); // null terminated = 0 at end
    Label output_buffer = wordData(0,0,0,0,0, // 20 num buffer
                                   0,0,0,0,0,
                                   0,0,0,0,0,
                                   0,0,0,0,0); 

    run("onescompl", input_buffer,output_buffer);
    Assert.assertEquals("$v0 contains incorrect count",-1, get(v0)); 
    for (int i=0;i<20;i++) {
      Assert.assertEquals("output_buffer contains incorrect value",
            0, getWord(output_buffer,i*4)); 
    }
  }

}