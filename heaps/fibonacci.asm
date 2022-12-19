# Compute the nth fibonacci number
#
# n: the index of the fibonacci number to compute
#
# return: the nth fibonacci number

.macro allocateHeapSpace(%a)
  # ALLOCATE HEAP SPACE
  addi $sp, $sp, -4 
  sw $a0, 0($sp)
  li $v0, 9
  li $a0, %a 
  syscall
  lw $a0, 0($sp)
  addi $sp, $sp, 4
.end_macro

.macro getFibFrom(%address, %n)
  # preserve s registers
  addi $sp, $sp, -8
  sw $s0, 0($sp)
  sw $s1, 4($sp)
  # logic
  li $s0, 4
  mul $s0, %n, $s0
  add $s0, $s0, %address
  lw $s1, 0($s0) # s1 now contains pointer to answer
  beqz $s1, fib_not_present # if no pointer exists, return false
  li $v1, 1 # return true
  lw $s0, 0($s1) # return answer
  move $v0, $s0
  j end_getFibFrom
  fib_not_present:
  li $v0, -1 # return false
  li $v1, -1
  # end logic
  end_getFibFrom:
  lw $s0, 0($sp)
  lw $s1, 4($sp)
  addi $sp, $sp, 8
.end_macro

.macro saveFibTo(%address, %n, %fib)
  # preserve s registers
  addi $sp, $sp, -8
  sw $s0, 0($sp)
  sw $v0, 4($sp)
  # logic
  li $s0,4
  mul $s0, %n, $s0
  add $s0, $s0, %address
  allocateHeapSpace(4) # 4 bytes = 1 word
  sw %fib, 0($v0) # save fib to heap address
  sw $v0, 0($s0) # save heap address to array
  # end logic
  lw $s0, 0($sp)
  lw $v0, 4($sp)
  addi $sp, $sp, 8
.end_macro

.globl fibonacci
fibonacci:
    # inputs: a0=n a1=buffer
    # check if buffer contains answer:
    getFibFrom($a1, $a0)
    bgtz $v1, end_fibonacci

    # set elements 0, 1 in buffer as base case
    li $t0,1
    saveFibTo($a1,$0,$0)
    saveFibTo($a1,$t0,$t0)
    addi $sp, $sp, -4
    sw $ra, 0($sp)
    jal fibonacci_helper # run fib function
    lw $ra, 0($sp)
    addi $sp,$sp, 4
    end_fibonacci:
      jr $ra

.globl fibonacci_helper
fibonacci_helper:
    # inputs: a0=n a1=buffer
    # Check if n is 0 or 1
    li $t0, 1
    beqz $a0, return_0
    beq $a0, $t0, return_1

    addi $sp, $sp, -4
    sw $0, 0($sp) # marker to save result to buffer
    # check if fib(n) is in the list
    getFibFrom($a1, $a0)
    addi $sp, $sp, 4
    bgtz $v0, return_fib_result
    addi $sp, $sp, -4
    li $t0, 1
    sw $t0, 0($sp) # marker to save result to buffer

    # Compute the (n-1)th and (n-2)th fibonacci numbers
    addi $sp, $sp, -16    # allocate space on the stack
    sw $ra, 4($sp)       # save return address
    sw $a0, 0($sp)       # save n
    addi $a0, $a0, -1    # n-1
    jal fibonacci_helper # call fibonacci(n-1)
    sw $v0, 8($sp)       # store fib(n-1) in the stack
    lw $ra, 4($sp)       # save return address
    lw $a0, 0($sp)       # restore n
    addi $a0, $a0, -2    # n-2
    jal fibonacci_helper # call fibonacci(n-2)
    sw $v0, 12($sp)      # store fib(n-2) in the stack
    lw $ra, 4($sp)       # restore return address
    # Return the sum of the (n-1)th and (n-2)th fibonacci numbers
    lw $v0, 8($sp)       # load fib(n-1)
    lw $v1, 12($sp)      # load fib(n-2)
    add $v0, $v0, $v1    # solution
    lw $a0, 0($sp)       # restore n
    addi $sp, $sp, 16    # deallocate space on the stack
    j end_fibonacci_n

    return_0:
      li $v0, 0    # return 0
      jr $ra

    return_1:
      li $v0, 1    # return 1
      jr $ra

    end_fibonacci_n:
      lw $t0, 0($sp)
      beqz $t0, return_fib_result
        # save result
        move $t0, $v0 # avoid using special registers as inputs
        saveFibTo($a1,$a0,$t0)
        addi $sp, $sp, 4
      return_fib_result:
        jr $ra

.globl test_fib_0
test_fib_0:
  # inputs: a0=n a1=buffer
  li $a0, 14
  addi $sp, $sp, -4
  sw $ra, 0($sp)
  jal fibonacci
  lw $ra, 0($sp)
  li $a0, 21
  jal fibonacci
  lw $ra, 0($sp)
  addi $sp, $sp, 4
  jr $ra
