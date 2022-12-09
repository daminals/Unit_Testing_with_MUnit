.data
buffer:
  .word 1
  .word 2
  .word 3 4 5 6 7 8 9 0

output_buffer:
  .word 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0

.text

la $a0, buffer
la $a1, output_buffer

# while testing our code, everything before this function 
# (.data, defining a0,a1) will be ignored

.globl onescompl
onescompl:
  # inputs: a0=input_buffer, a1=output_buffer
  # Save the null-terminated buffer to the stack
  addi $sp, $sp, -8
  sw $s1, 0($sp)
  sw $a1, 4($sp)
  li $v0, 0

  # Iterate over the buffer
  loop:
  lw $s1, 0($a0)

  # If we have reached the end of the buffer, exit the loop
  beqz $s1, end

  # if number not positive, wipe output buffer
  bltz $s1, wipe_buffer

  # Convert the number to one's compliment form
  neg $s1, $s1
  # note that MIPS stores numbers in twos compliment form already, 
  # so we will need to subtract one to go from 1's to 2's
  addi $s1, $s1, -1

  # Save the number to the output_buffer
  sw $s1, 0($a1)
  addi $v0,$v0,1

  # next element in output_buffer
  addi $a1, $a1, 4 

  # Move to the next element in the buffer
  addi $a0, $a0, 4
  j loop

  # wipe output buffer
  wipe_buffer:
    lw $a1, 4($sp)
    # 20 numbers * 4 bytes per word
    li $s1, 80
    li $v0, -1
    wipe_buffer_loop:
      beqz $s1, end
      addi $s1, $s1, -4
      sw $0, 0($a1)
      addi $a1,$a1,4
      j wipe_buffer_loop

  # end program
  end:
    lw $s1, 0($sp)
    addi $sp, $sp, 8
    jr $ra