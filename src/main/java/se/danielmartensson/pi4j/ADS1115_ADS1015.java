package se.danielmartensson.pi4j;

import java.io.IOException;

import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2C;
import com.pi4j.io.i2c.I2CConfig;
import com.pi4j.io.i2c.I2CProvider;

public class ADS1115_ADS1015 {

	// Gains
	public static short GAIN_TWOTHIRDS = 0x0000; // ADS1015_REG_CONFIG_PGA_6_144V,
	public static short GAIN_ONE = 0x0200; // ADS1015_REG_CONFIG_PGA_4_096V,
	public static short GAIN_TWO = 0x0400; // ADS1015_REG_CONFIG_PGA_2_048V,
	public static short GAIN_FOUR = 0x0600; // ADS1015_REG_CONFIG_PGA_1_024V,
	public static short GAIN_EIGHT = 0x0800; // ADS1015_REG_CONFIG_PGA_0_512V,
	public static short GAIN_SIXTEEN = 0x0A00; // ADS1015_REG_CONFIG_PGA_0_256V

	/*
	 * ========================================================================= I2C
	 * ADDRESS/BITS - Connect the following pin to ADDR
	 * -----------------------------------------------------------------------
	 */
	public static int ADS_ADDR_GND = 0x48; /// < 1001 000 (ADDR -> GND)
	public static int ADS_ADDR_VDD = 0x49; /// < 1001 001 (ADDR -> VDD)
	public static int ADS_ADDR_SDA = 0x4A; /// < 1001 010 (ADDR -> SDA)
	public static int ADS_ADDR_SCL = 0x4B; /// < 1001 011 (ADDR -> SCL)
	/* ========================================================================= */

	/*
	 * =========================================================================
	 * CONVERSION DELAY (in mS)
	 * -----------------------------------------------------------------------
	 */
	private static int ADS1015_CONVERSIONDELAY = 1; /// < Conversion delay
	private static int ADS1115_CONVERSIONDELAY = 8; /// < Conversion delay
	/* ========================================================================= */

	/*
	 * =========================================================================
	 * POINTER REGISTER
	 * -----------------------------------------------------------------------
	 */
	private static int ADS1015_REG_POINTER_MASK = 0x03; /// < Point mask
	private static int ADS1015_REG_POINTER_CONVERT = 0x00; /// < Conversion
	private static int ADS1015_REG_POINTER_CONFIG = 0x01; /// < Configuration
	private static int ADS1015_REG_POINTER_LOWTHRESH = 0x02; /// < Low threshold
	private static int ADS1015_REG_POINTER_HITHRESH = 0x03; /// < High threshold
	/* ========================================================================= */

	/*
	 * =========================================================================
	 * CONFIG REGISTER
	 * -----------------------------------------------------------------------
	 */
	private static int ADS1015_REG_CONFIG_OS_MASK = 0x8000; /// < OS Mask
	private static int ADS1015_REG_CONFIG_OS_SINGLE = 0x8000; /// < Write: Set to start a single-conversion
	private static int ADS1015_REG_CONFIG_OS_BUSY = 0x0000; /// < Read: Bit = 0 when conversion is in progress
	private static int ADS1015_REG_CONFIG_OS_NOTBUSY = 0x8000; /// < Read: Bit = 1 when device is not performing a conversion
	private static int ADS1015_REG_CONFIG_MUX_MASK = 0x7000; /// < Mux Mask
	private static int ADS1015_REG_CONFIG_MUX_DIFF_0_1 = 0x0000; /// < Differential P = AIN0, N = AIN1 (default)
	private static int ADS1015_REG_CONFIG_MUX_DIFF_0_3 = 0x1000; /// < Differential P = AIN0, N = AIN3
	private static int ADS1015_REG_CONFIG_MUX_DIFF_1_3 = 0x2000; /// < Differential P = AIN1, N = AIN3
	private static int ADS1015_REG_CONFIG_MUX_DIFF_2_3 = 0x3000; /// < Differential P = AIN2, N = AIN3
	private static int ADS1015_REG_CONFIG_MUX_SINGLE_0 = 0x4000; /// < Single-ended AIN0
	private static int ADS1015_REG_CONFIG_MUX_SINGLE_1 = 0x5000; /// < Single-ended AIN1
	private static int ADS1015_REG_CONFIG_MUX_SINGLE_2 = 0x6000; /// < Single-ended AIN2
	private static int ADS1015_REG_CONFIG_MUX_SINGLE_3 = 0x7000; /// < Single-ended AIN3
	private static int ADS1015_REG_CONFIG_PGA_MASK = 0x0E00; /// < PGA Mask
	private static int ADS1015_REG_CONFIG_PGA_6_144V = 0x0000; /// < +/-6.144V range = Gain 2/3
	private static int ADS1015_REG_CONFIG_PGA_4_096V = 0x0200; /// < +/-4.096V range = Gain 1
	private static int ADS1015_REG_CONFIG_PGA_2_048V = 0x0400; /// < +/-2.048V range = Gain 2 (default)
	private static int ADS1015_REG_CONFIG_PGA_1_024V = 0x0600; /// < +/-1.024V range = Gain 4
	private static int ADS1015_REG_CONFIG_PGA_0_512V = 0x0800; /// < +/-0.512V range = Gain 8
	private static int ADS1015_REG_CONFIG_PGA_0_256V = 0x0A00; /// < +/-0.256V range = Gain 16
	private static int ADS1015_REG_CONFIG_MODE_MASK = 0x0100; /// < Mode Mask
	private static int ADS1015_REG_CONFIG_MODE_CONTIN = 0x0000; /// < Continuous conversion mode
	private static int ADS1015_REG_CONFIG_MODE_SINGLE = 0x0100; /// < Power-down single-shot mode (default)
	private static int ADS1015_REG_CONFIG_DR_MASK = 0x00E0; /// < Data Rate Mask
	private static int ADS1015_REG_CONFIG_DR_128SPS = 0x0000; /// < 128 samples per second
	private static int ADS1015_REG_CONFIG_DR_250SPS = 0x0020; /// < 250 samples per second
	private static int ADS1015_REG_CONFIG_DR_490SPS = 0x0040; /// < 490 samples per second
	private static int ADS1015_REG_CONFIG_DR_920SPS = 0x0060; /// < 920 samples per second
	private static int ADS1015_REG_CONFIG_DR_1600SPS = 0x0080; /// < 1600 samples per second (default)
	private static int ADS1015_REG_CONFIG_DR_2400SPS = 0x00A0; /// < 2400 samples per second
	private static int ADS1015_REG_CONFIG_DR_3300SPS = 0x00C0; /// < 3300 samples per second
	private static int ADS1015_REG_CONFIG_CMODE_MASK = 0x0010; /// < CMode Mask
	private static int ADS1015_REG_CONFIG_CMODE_TRAD = 0x0000; /// < Traditional comparator with hysteresis (default)
	private static int ADS1015_REG_CONFIG_CMODE_WINDOW = 0x0010; /// < Window comparator
	private static int ADS1015_REG_CONFIG_CPOL_MASK = 0x0008; /// < CPol Mask
	private static int ADS1015_REG_CONFIG_CPOL_ACTVLOW = 0x0000; /// < ALERT/RDY pin is low when active (default)
	private static int ADS1015_REG_CONFIG_CPOL_ACTVHI = 0x0008; /// < ALERT/RDY pin is high when active
	private static int ADS1015_REG_CONFIG_CLAT_MASK = 0x0004; /// < Determines if ALERT/RDY pin latches once asserted
	private static int ADS1015_REG_CONFIG_CLAT_NONLAT = 0x0000; /// < Non-latching comparator (default)
	private static int ADS1015_REG_CONFIG_CLAT_LATCH = 0x0004; /// < Latching comparator
	private static int ADS1015_REG_CONFIG_CQUE_MASK = 0x0003; /// < CQue Mask
	private static int ADS1015_REG_CONFIG_CQUE_1CONV = 0x0000; /// < Assert ALERT/RDY after one conversions
	private static int ADS1015_REG_CONFIG_CQUE_2CONV = 0x0001; /// < Assert ALERT/RDY after two conversions
	private static int ADS1015_REG_CONFIG_CQUE_4CONV = 0x0002; /// < Assert ALERT/RDY after four conversions
	private static int ADS1015_REG_CONFIG_CQUE_NONE = 0x0003; /// < Disable the comparator and put ALERT/RDY in high
																/// state (default)
	/* ========================================================================= */

	// ADS settings
	private I2C hi2c;
	private int m_conversionDelay;
	private int m_bitShift;
	private int m_gain;

	public ADS1115_ADS1015(Context pi4j, Integer i2cBus, Integer i2cDevice) {
		hi2c = begin(pi4j, i2cBus, i2cDevice);
	}

	public void useADS1015() {
		this.m_conversionDelay = ADS1015_CONVERSIONDELAY;
		this.m_bitShift = 4;
		this.m_gain = GAIN_TWOTHIRDS; /* +/- 6.144V range (limited to VDD +0.3V max!) */
	}

	public void useADS1115() {
		this.m_conversionDelay = ADS1115_CONVERSIONDELAY;
		this.m_bitShift = 0;
		this.m_gain = GAIN_TWOTHIRDS; /* +/- 6.144V range (limited to VDD +0.3V max!) */
	}
	
	// This will read the ADS1115 16-bit ADC
	private I2C begin(Context pi4j, Integer i2cBus, Integer i2cDevice) {
		try {
			I2CConfig config = I2C.newConfigBuilder(pi4j)
				     .id("my-i2c-bus")
				     .name("My I2C Bus")
				     .bus(i2cBus)
				     .device(i2cDevice)
				     .build();
			I2CProvider i2CProvider = pi4j.provider("pigpio-i2c");
			return i2CProvider.create(config);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 // The ADC input range (or gain) can be changed via the following
	 // functions, but be careful never to exceed VDD +0.3V max, or to
	 // exceed the upper and lower limits if you adjust the input range!
	 // Setting these values incorrectly may destroy your ADC!
	 //                                                                ADS1015  ADS1115
	 //                                                                -------  -------
	 // ADSsetGain(ADS1115_ADS1015.GAIN_TWOTHIRDS);  // 2/3x gain +/- 6.144V  1 bit = 3mV      0.1875mV (default)
	 // ADSsetGain(ADS1115_ADS1015.GAIN_ONE);        // 1x gain   +/- 4.096V  1 bit = 2mV      0.125mV
	 // ADSsetGain(ADS1115_ADS1015.GAIN_TWO);        // 2x gain   +/- 2.048V  1 bit = 1mV      0.0625mV
	 // ADSsetGain(ADS1115_ADS1015.GAIN_FOUR);       // 4x gain   +/- 1.024V  1 bit = 0.5mV    0.03125mV
	 // ADSsetGain(ADS1115_ADS1015.GAIN_EIGHT);      // 8x gain   +/- 0.512V  1 bit = 0.25mV   0.015625mV
	 // ADSsetGain(ADS1115_ADS1015.GAIN_SIXTEEN);    // 16x gain  +/- 0.256V  1 bit = 0.125mV  0.0078125mV
	 */
	public void ADSsetGain(short gain) {
		this.m_gain = gain;
	}

	// Get the gain
	public int ADSgetGain() {
		return m_gain;
	}

	// Gets a single-ended ADC reading from the specified channel
	public int ADSreadADC_SingleEnded(int channel) {
		if (channel > 3) {
			return 0;
		}

		// Start with default values
		int config = ADS1015_REG_CONFIG_CQUE_NONE 			| // Disable the comparator (default val)
					 ADS1015_REG_CONFIG_CLAT_NONLAT 		| // Non-latching (default val)
					 ADS1015_REG_CONFIG_CPOL_ACTVLOW 		| // Alert/Rdy active low (default val)
					 ADS1015_REG_CONFIG_CMODE_TRAD 			| // Traditional comparator (default val)
					 ADS1015_REG_CONFIG_DR_1600SPS 			| // 1600 samples per second (default)
					 ADS1015_REG_CONFIG_MODE_SINGLE;		  // Single-shot mode (default)

		// Set PGA/voltage range
		config |= m_gain;

		// Set single-ended input channel
		switch (channel) {
		case (0):
			config |= ADS1015_REG_CONFIG_MUX_SINGLE_0;
			break;
		case (1):
			config |= ADS1015_REG_CONFIG_MUX_SINGLE_1;
			break;
		case (2):
			config |= ADS1015_REG_CONFIG_MUX_SINGLE_2;
			break;
		case (3):
			config |= ADS1015_REG_CONFIG_MUX_SINGLE_3;
			break;
		}

		// Set 'start single-conversion' bit
		config |= ADS1015_REG_CONFIG_OS_SINGLE;

		// Write config register to the ADC
		writeRegister(ADS1015_REG_POINTER_CONFIG, config);

		// Wait for the conversion to complete
		try {
			Thread.sleep(m_conversionDelay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Read the conversion results
		// Shift 12-bit results right 4 bits for the ADS1015
		return readRegister(ADS1015_REG_POINTER_CONVERT) >> m_bitShift;
	}
	
	/*
	 * Reads the conversion results, measuring the voltage
	 * difference between the P (AIN0) and N (AIN1) input.  Generates
	 * a signed value since the difference can be either positive or negative.
	 */
	public int ADSreadADC_Differential_0_1() {
		// Start with default values
		int config =
				ADS1015_REG_CONFIG_CQUE_NONE 	|   	// Disable the comparator (default val)
				ADS1015_REG_CONFIG_CLAT_NONLAT 	|  	// Non-latching (default val)
				ADS1015_REG_CONFIG_CPOL_ACTVLOW | 	// Alert/Rdy active low   (default val)
				ADS1015_REG_CONFIG_CMODE_TRAD 	| 	// Traditional comparator (default val)
				ADS1015_REG_CONFIG_DR_1600SPS 	| 	// 1600 samples per second (default)
				ADS1015_REG_CONFIG_MODE_SINGLE;   	// Single-shot mode (default)

		// Set PGA/voltage range
		config |= m_gain;

		// Set channels
		config |= ADS1015_REG_CONFIG_MUX_DIFF_0_1; // AIN0 = P, AIN1 = N

		// Set 'start single-conversion' bit
		config |= ADS1015_REG_CONFIG_OS_SINGLE;

		// Write config register to the ADC
		writeRegister(ADS1015_REG_POINTER_CONFIG, config);

		// Wait for the conversion to complete
		try {
			Thread.sleep(m_conversionDelay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Read the conversion results
		int res = readRegister(ADS1015_REG_POINTER_CONVERT) >> m_bitShift;
		if (m_bitShift == 0) {
			return res;
		} else {
			// Shift 12-bit results right 4 bits for the ADS1015,
			// making sure we keep the sign bit intact
			if (res > 0x07FF) {
				// negative number - extend the sign to 16th bit
				res |= 0xF000;
			}
			return res;
		}
	}
	
	/*
	 * Sets up the comparator to operate in basic mode, causing the
	 * ALERT/RDY pin to assert (go from high to low) when the ADC
	 * value exceeds the specified threshold.
	 * This will also set the ADC in continuous conversion mode.
	 */
	public void ADSstartComparator_SingleEnded(int channel, int threshold) {
		// Start with default values
		int config =
				ADS1015_REG_CONFIG_CQUE_1CONV 	|   	// Comparator enabled and asserts on 1 match
				ADS1015_REG_CONFIG_CLAT_LATCH 	|   	// Latching mode
				ADS1015_REG_CONFIG_CPOL_ACTVLOW | 		// Alert/Rdy active low   (default val)
				ADS1015_REG_CONFIG_CMODE_TRAD 	| 		// Traditional comparator (default val)
				ADS1015_REG_CONFIG_DR_1600SPS 	|	 	// 1600 samples per second (default)
				ADS1015_REG_CONFIG_MODE_CONTIN 	|  		// Continuous conversion mode
				ADS1015_REG_CONFIG_MODE_CONTIN;   		// Continuous conversion mode

		// Set PGA/voltage range
		config |= m_gain;

		// Set single-ended input channel
		switch (channel) {
		case (0):
			config |= ADS1015_REG_CONFIG_MUX_SINGLE_0;
			break;
		case (1):
			config |= ADS1015_REG_CONFIG_MUX_SINGLE_1;
			break;
		case (2):
			config |= ADS1015_REG_CONFIG_MUX_SINGLE_2;
			break;
		case (3):
			config |= ADS1015_REG_CONFIG_MUX_SINGLE_3;
			break;
		}

		// Set the high threshold register
		// Shift 12-bit results left 4 bits for the ADS1015
		writeRegister(ADS1015_REG_POINTER_HITHRESH, threshold << m_bitShift);

		// Write config register to the ADC
		writeRegister(ADS1015_REG_POINTER_CONFIG, config);
	}
	
	/*
	 * In order to clear the comparator, we need to read the conversion results.
	 * This function reads the last conversion results without changing the config value.
	 */
	public int ADSgetLastConversionResults() {
		// Wait for the conversion to complete
		try {
			Thread.sleep(m_conversionDelay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Read the conversion results
		int res = readRegister(ADS1015_REG_POINTER_CONVERT) >> m_bitShift;
		if (m_bitShift == 0) {
			return res;
		} else {
			// Shift 12-bit results right 4 bits for the ADS1015,
			// making sure we keep the sign bit intact
			if (res > 0x07FF) {
				// negative number - extend the sign to 16th bit
				res |= 0xF000;
			}
			return res;
		}
	}

	private void writeRegister(int reg, int value) {
		byte[] pData = new byte[] {(byte) reg, (byte) (value >> 8), (byte) (value & 0xFF) };
		try {
			hi2c.write(pData);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private int readRegister(int reg) {
		try {
			hi2c.write(reg);
			byte[] pData = new byte[] { 0, 0 };
			hi2c.read(pData);
			int value0 = Byte.toUnsignedInt(pData[0]) << 8;
			int value1 = Byte.toUnsignedInt(pData[1]); 
			return value0 | value1;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

}
