################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
../Core/Src/FlashMemory_F3/ReadWrite.c 

OBJS += \
./Core/Src/FlashMemory_F3/ReadWrite.o 

C_DEPS += \
./Core/Src/FlashMemory_F3/ReadWrite.d 


# Each subdirectory must supply rules for building sources it contributes
Core/Src/FlashMemory_F3/ReadWrite.o: ../Core/Src/FlashMemory_F3/ReadWrite.c
	arm-none-eabi-gcc "$<" -mcpu=cortex-m4 -std=gnu11 -g3 -DUSE_HAL_DRIVER -DSTM32F373xC -DDEBUG -c -I../Core/Inc -I../Drivers/STM32F3xx_HAL_Driver/Inc -I../Drivers/STM32F3xx_HAL_Driver/Inc/Legacy -I../Drivers/CMSIS/Device/ST/STM32F3xx/Include -I../Drivers/CMSIS/Include -O0 -ffunction-sections -fdata-sections -Wall -fstack-usage -MMD -MP -MF"Core/Src/FlashMemory_F3/ReadWrite.d" -MT"$@" --specs=nano.specs -mfpu=fpv4-sp-d16 -mfloat-abi=hard -mthumb -o "$@"

