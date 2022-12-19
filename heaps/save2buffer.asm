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

.globl save_to_buffer
save_to_buffer:
  # a0 = buffer, a1 = value, a2 = where_to_store_in_buffer
  allocateHeapSpace(4) # saves address to v0
  sw $a1, 0($v0)       # save value to heap
  sll  $a2, $a2, 2     # Multiply by 4
  add $a0,$a0,$a2      # find correct locale in buffer to store
  sw $v0, 0($a0)       # save heap address to buffer
  jr $ra

.globl save_12_and_47_test
save_12_and_47_test:
  # a0 = buffer // do not take in // a1=value a2=where_to_store_in_buffer
  addi $sp,$sp,-4
  sw $ra, 0($sp)
  li $a1,12
  li $a2,0
  jal save_to_buffer
  lw $ra, 0($sp)
  li $a1,47
  li $a2, 1
  jal save_to_buffer
  lw $ra, 0($sp)
  addi $sp,$sp,4
  jr $ra
