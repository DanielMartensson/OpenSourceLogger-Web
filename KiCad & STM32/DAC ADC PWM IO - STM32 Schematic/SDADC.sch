EESchema Schematic File Version 4
EELAYER 30 0
EELAYER END
$Descr A4 11693 8268
encoding utf-8
Sheet 6 6
Title ""
Date ""
Rev ""
Comp ""
Comment1 ""
Comment2 ""
Comment3 ""
Comment4 ""
$EndDescr
$Comp
L Device:R R?
U 1 1 5FC4BC53
P 2100 1650
AR Path="/5FAFE50B/5FC4BC53" Ref="R?"  Part="1" 
AR Path="/5FB05F69/5FC4BC53" Ref="R?"  Part="1" 
AR Path="/5FC4BC53" Ref="R?"  Part="1" 
AR Path="/5FC2CC0E/5FC4BC53" Ref="R17"  Part="1" 
F 0 "R17" H 2170 1696 50  0000 L CNN
F 1 "150" H 2170 1605 50  0000 L CNN
F 2 "Resistor_SMD:R_1206_3216Metric" V 2030 1650 50  0001 C CNN
F 3 "660-SG73P2BTTD151G" H 2100 1650 50  0001 C CNN
	1    2100 1650
	1    0    0    -1  
$EndComp
$Comp
L Device:R R?
U 1 1 5FC4BC6E
P 2400 1350
AR Path="/5FAFE50B/5FC4BC6E" Ref="R?"  Part="1" 
AR Path="/5FB05F69/5FC4BC6E" Ref="R?"  Part="1" 
AR Path="/5FC4BC6E" Ref="R?"  Part="1" 
AR Path="/5FC2CC0E/5FC4BC6E" Ref="R21"  Part="1" 
F 0 "R21" V 2607 1350 50  0000 C CNN
F 1 "2k" V 2516 1350 50  0000 C CNN
F 2 "Resistor_SMD:R_1206_3216Metric" V 2330 1350 50  0001 C CNN
F 3 "71-CRCW12062K00FKEAC" H 2400 1350 50  0001 C CNN
	1    2400 1350
	0    -1   -1   0   
$EndComp
Wire Wire Line
	2100 1500 2100 1350
Wire Wire Line
	2100 1350 2250 1350
$Comp
L Device:Polyfuse F?
U 1 1 5FC4BC76
P 1700 1350
AR Path="/5FAFE50B/5FC4BC76" Ref="F?"  Part="1" 
AR Path="/5FB05F69/5FC4BC76" Ref="F?"  Part="1" 
AR Path="/5FC4BC76" Ref="F?"  Part="1" 
AR Path="/5FC2CC0E/5FC4BC76" Ref="F5"  Part="1" 
F 0 "F5" V 1475 1350 50  0000 C CNN
F 1 "Polyfuse" V 1566 1350 50  0000 C CNN
F 2 "Fuse:Fuse_0603_1608Metric" H 1750 1150 50  0001 L CNN
F 3 "530-0ZCM0002FF2G" H 1700 1350 50  0001 C CNN
	1    1700 1350
	0    1    1    0   
$EndComp
$Comp
L power:GND #PWR?
U 1 1 5FC4BC7C
P 2100 1800
AR Path="/5FAFE50B/5FC4BC7C" Ref="#PWR?"  Part="1" 
AR Path="/5FB05F69/5FC4BC7C" Ref="#PWR?"  Part="1" 
AR Path="/5FC4BC7C" Ref="#PWR?"  Part="1" 
AR Path="/5FC2CC0E/5FC4BC7C" Ref="#PWR047"  Part="1" 
F 0 "#PWR047" H 2100 1550 50  0001 C CNN
F 1 "GND" H 2105 1627 50  0000 C CNN
F 2 "" H 2100 1800 50  0001 C CNN
F 3 "" H 2100 1800 50  0001 C CNN
	1    2100 1800
	1    0    0    -1  
$EndComp
Wire Wire Line
	1850 1350 2100 1350
Connection ~ 2100 1350
Wire Wire Line
	2550 1350 3250 1350
Text HLabel 1550 1350 0    50   Input ~ 0
SDADC0P
Text HLabel 3250 1350 2    50   Input ~ 0
PB0
$Comp
L Device:R R?
U 1 1 5FC4BC87
P 2100 2500
AR Path="/5FAFE50B/5FC4BC87" Ref="R?"  Part="1" 
AR Path="/5FB05F69/5FC4BC87" Ref="R?"  Part="1" 
AR Path="/5FC4BC87" Ref="R?"  Part="1" 
AR Path="/5FC2CC0E/5FC4BC87" Ref="R18"  Part="1" 
F 0 "R18" H 2170 2546 50  0000 L CNN
F 1 "150" H 2170 2455 50  0000 L CNN
F 2 "Resistor_SMD:R_1206_3216Metric" V 2030 2500 50  0001 C CNN
F 3 "660-SG73P2BTTD151G" H 2100 2500 50  0001 C CNN
	1    2100 2500
	1    0    0    -1  
$EndComp
$Comp
L Device:R R?
U 1 1 5FC4BCA2
P 2400 2200
AR Path="/5FAFE50B/5FC4BCA2" Ref="R?"  Part="1" 
AR Path="/5FB05F69/5FC4BCA2" Ref="R?"  Part="1" 
AR Path="/5FC4BCA2" Ref="R?"  Part="1" 
AR Path="/5FC2CC0E/5FC4BCA2" Ref="R22"  Part="1" 
F 0 "R22" V 2607 2200 50  0000 C CNN
F 1 "2k" V 2516 2200 50  0000 C CNN
F 2 "Resistor_SMD:R_1206_3216Metric" V 2330 2200 50  0001 C CNN
F 3 "71-CRCW12062K00FKEAC" H 2400 2200 50  0001 C CNN
	1    2400 2200
	0    -1   -1   0   
$EndComp
Wire Wire Line
	2100 2350 2100 2200
Wire Wire Line
	2100 2200 2250 2200
$Comp
L Device:Polyfuse F?
U 1 1 5FC4BCAA
P 1700 2200
AR Path="/5FAFE50B/5FC4BCAA" Ref="F?"  Part="1" 
AR Path="/5FB05F69/5FC4BCAA" Ref="F?"  Part="1" 
AR Path="/5FC4BCAA" Ref="F?"  Part="1" 
AR Path="/5FC2CC0E/5FC4BCAA" Ref="F6"  Part="1" 
F 0 "F6" V 1475 2200 50  0000 C CNN
F 1 "Polyfuse" V 1566 2200 50  0000 C CNN
F 2 "Fuse:Fuse_0603_1608Metric" H 1750 2000 50  0001 L CNN
F 3 "530-0ZCM0002FF2G" H 1700 2200 50  0001 C CNN
	1    1700 2200
	0    1    1    0   
$EndComp
$Comp
L power:GND #PWR?
U 1 1 5FC4BCB0
P 2100 2650
AR Path="/5FAFE50B/5FC4BCB0" Ref="#PWR?"  Part="1" 
AR Path="/5FB05F69/5FC4BCB0" Ref="#PWR?"  Part="1" 
AR Path="/5FC4BCB0" Ref="#PWR?"  Part="1" 
AR Path="/5FC2CC0E/5FC4BCB0" Ref="#PWR048"  Part="1" 
F 0 "#PWR048" H 2100 2400 50  0001 C CNN
F 1 "GND" H 2105 2477 50  0000 C CNN
F 2 "" H 2100 2650 50  0001 C CNN
F 3 "" H 2100 2650 50  0001 C CNN
	1    2100 2650
	1    0    0    -1  
$EndComp
Wire Wire Line
	1850 2200 2100 2200
Connection ~ 2100 2200
Wire Wire Line
	2550 2200 3250 2200
Text HLabel 1550 2200 0    50   Input ~ 0
SDADC0M
Text HLabel 3250 2200 2    50   Input ~ 0
PB1
$Comp
L Device:R R?
U 1 1 5FC4BCBB
P 5000 1650
AR Path="/5FAFE50B/5FC4BCBB" Ref="R?"  Part="1" 
AR Path="/5FB05F69/5FC4BCBB" Ref="R?"  Part="1" 
AR Path="/5FC4BCBB" Ref="R?"  Part="1" 
AR Path="/5FC2CC0E/5FC4BCBB" Ref="R25"  Part="1" 
F 0 "R25" H 5070 1696 50  0000 L CNN
F 1 "150" H 5070 1605 50  0000 L CNN
F 2 "Resistor_SMD:R_1206_3216Metric" V 4930 1650 50  0001 C CNN
F 3 "660-SG73P2BTTD151G" H 5000 1650 50  0001 C CNN
	1    5000 1650
	1    0    0    -1  
$EndComp
$Comp
L Device:R R?
U 1 1 5FC4BCD6
P 5300 1350
AR Path="/5FAFE50B/5FC4BCD6" Ref="R?"  Part="1" 
AR Path="/5FB05F69/5FC4BCD6" Ref="R?"  Part="1" 
AR Path="/5FC4BCD6" Ref="R?"  Part="1" 
AR Path="/5FC2CC0E/5FC4BCD6" Ref="R27"  Part="1" 
F 0 "R27" V 5507 1350 50  0000 C CNN
F 1 "2k" V 5416 1350 50  0000 C CNN
F 2 "Resistor_SMD:R_1206_3216Metric" V 5230 1350 50  0001 C CNN
F 3 "71-CRCW12062K00FKEAC" H 5300 1350 50  0001 C CNN
	1    5300 1350
	0    -1   -1   0   
$EndComp
Wire Wire Line
	5000 1500 5000 1350
Wire Wire Line
	5000 1350 5150 1350
$Comp
L Device:Polyfuse F?
U 1 1 5FC4BCDE
P 4600 1350
AR Path="/5FAFE50B/5FC4BCDE" Ref="F?"  Part="1" 
AR Path="/5FB05F69/5FC4BCDE" Ref="F?"  Part="1" 
AR Path="/5FC4BCDE" Ref="F?"  Part="1" 
AR Path="/5FC2CC0E/5FC4BCDE" Ref="F9"  Part="1" 
F 0 "F9" V 4375 1350 50  0000 C CNN
F 1 "Polyfuse" V 4466 1350 50  0000 C CNN
F 2 "Fuse:Fuse_0603_1608Metric" H 4650 1150 50  0001 L CNN
F 3 "530-0ZCM0002FF2G" H 4600 1350 50  0001 C CNN
	1    4600 1350
	0    1    1    0   
$EndComp
$Comp
L power:GND #PWR?
U 1 1 5FC4BCE4
P 5000 1800
AR Path="/5FAFE50B/5FC4BCE4" Ref="#PWR?"  Part="1" 
AR Path="/5FB05F69/5FC4BCE4" Ref="#PWR?"  Part="1" 
AR Path="/5FC4BCE4" Ref="#PWR?"  Part="1" 
AR Path="/5FC2CC0E/5FC4BCE4" Ref="#PWR059"  Part="1" 
F 0 "#PWR059" H 5000 1550 50  0001 C CNN
F 1 "GND" H 5005 1627 50  0000 C CNN
F 2 "" H 5000 1800 50  0001 C CNN
F 3 "" H 5000 1800 50  0001 C CNN
	1    5000 1800
	1    0    0    -1  
$EndComp
Wire Wire Line
	4750 1350 5000 1350
Connection ~ 5000 1350
Wire Wire Line
	5450 1350 6150 1350
Text HLabel 4450 1350 0    50   Input ~ 0
SDADC2P
Text HLabel 6150 1350 2    50   Input ~ 0
PE8
$Comp
L Device:R R?
U 1 1 5FC4BCEF
P 5000 2500
AR Path="/5FAFE50B/5FC4BCEF" Ref="R?"  Part="1" 
AR Path="/5FB05F69/5FC4BCEF" Ref="R?"  Part="1" 
AR Path="/5FC4BCEF" Ref="R?"  Part="1" 
AR Path="/5FC2CC0E/5FC4BCEF" Ref="R26"  Part="1" 
F 0 "R26" H 5070 2546 50  0000 L CNN
F 1 "150" H 5070 2455 50  0000 L CNN
F 2 "Resistor_SMD:R_1206_3216Metric" V 4930 2500 50  0001 C CNN
F 3 "660-SG73P2BTTD151G" H 5000 2500 50  0001 C CNN
	1    5000 2500
	1    0    0    -1  
$EndComp
$Comp
L Device:R R?
U 1 1 5FC4BD0A
P 5300 2200
AR Path="/5FAFE50B/5FC4BD0A" Ref="R?"  Part="1" 
AR Path="/5FB05F69/5FC4BD0A" Ref="R?"  Part="1" 
AR Path="/5FC4BD0A" Ref="R?"  Part="1" 
AR Path="/5FC2CC0E/5FC4BD0A" Ref="R28"  Part="1" 
F 0 "R28" V 5507 2200 50  0000 C CNN
F 1 "2k" V 5416 2200 50  0000 C CNN
F 2 "Resistor_SMD:R_1206_3216Metric" V 5230 2200 50  0001 C CNN
F 3 "71-CRCW12062K00FKEAC" H 5300 2200 50  0001 C CNN
	1    5300 2200
	0    -1   -1   0   
$EndComp
Wire Wire Line
	5000 2350 5000 2200
Wire Wire Line
	5000 2200 5150 2200
$Comp
L Device:Polyfuse F?
U 1 1 5FC4BD12
P 4600 2200
AR Path="/5FAFE50B/5FC4BD12" Ref="F?"  Part="1" 
AR Path="/5FB05F69/5FC4BD12" Ref="F?"  Part="1" 
AR Path="/5FC4BD12" Ref="F?"  Part="1" 
AR Path="/5FC2CC0E/5FC4BD12" Ref="F10"  Part="1" 
F 0 "F10" V 4375 2200 50  0000 C CNN
F 1 "Polyfuse" V 4466 2200 50  0000 C CNN
F 2 "Fuse:Fuse_0603_1608Metric" H 4650 2000 50  0001 L CNN
F 3 "530-0ZCM0002FF2G" H 4600 2200 50  0001 C CNN
	1    4600 2200
	0    1    1    0   
$EndComp
$Comp
L power:GND #PWR?
U 1 1 5FC4BD18
P 5000 2650
AR Path="/5FAFE50B/5FC4BD18" Ref="#PWR?"  Part="1" 
AR Path="/5FB05F69/5FC4BD18" Ref="#PWR?"  Part="1" 
AR Path="/5FC4BD18" Ref="#PWR?"  Part="1" 
AR Path="/5FC2CC0E/5FC4BD18" Ref="#PWR060"  Part="1" 
F 0 "#PWR060" H 5000 2400 50  0001 C CNN
F 1 "GND" H 5005 2477 50  0000 C CNN
F 2 "" H 5000 2650 50  0001 C CNN
F 3 "" H 5000 2650 50  0001 C CNN
	1    5000 2650
	1    0    0    -1  
$EndComp
Wire Wire Line
	4750 2200 5000 2200
Connection ~ 5000 2200
Wire Wire Line
	5450 2200 6150 2200
Text HLabel 4450 2200 0    50   Input ~ 0
SDADC2M
Text HLabel 6150 2200 2    50   Input ~ 0
PE9
$Comp
L Device:R R?
U 1 1 5FC4BD23
P 8150 1650
AR Path="/5FAFE50B/5FC4BD23" Ref="R?"  Part="1" 
AR Path="/5FB05F69/5FC4BD23" Ref="R?"  Part="1" 
AR Path="/5FC4BD23" Ref="R?"  Part="1" 
AR Path="/5FC2CC0E/5FC4BD23" Ref="R29"  Part="1" 
F 0 "R29" H 8220 1696 50  0000 L CNN
F 1 "150" H 8220 1605 50  0000 L CNN
F 2 "Resistor_SMD:R_1206_3216Metric" V 8080 1650 50  0001 C CNN
F 3 "660-SG73P2BTTD151G" H 8150 1650 50  0001 C CNN
	1    8150 1650
	1    0    0    -1  
$EndComp
$Comp
L Device:R R?
U 1 1 5FC4BD3E
P 8450 1350
AR Path="/5FAFE50B/5FC4BD3E" Ref="R?"  Part="1" 
AR Path="/5FB05F69/5FC4BD3E" Ref="R?"  Part="1" 
AR Path="/5FC4BD3E" Ref="R?"  Part="1" 
AR Path="/5FC2CC0E/5FC4BD3E" Ref="R31"  Part="1" 
F 0 "R31" V 8657 1350 50  0000 C CNN
F 1 "2k" V 8566 1350 50  0000 C CNN
F 2 "Resistor_SMD:R_1206_3216Metric" V 8380 1350 50  0001 C CNN
F 3 "71-CRCW12062K00FKEAC" H 8450 1350 50  0001 C CNN
	1    8450 1350
	0    -1   -1   0   
$EndComp
Wire Wire Line
	8150 1500 8150 1350
Wire Wire Line
	8150 1350 8300 1350
$Comp
L Device:Polyfuse F?
U 1 1 5FC4BD46
P 7750 1350
AR Path="/5FAFE50B/5FC4BD46" Ref="F?"  Part="1" 
AR Path="/5FB05F69/5FC4BD46" Ref="F?"  Part="1" 
AR Path="/5FC4BD46" Ref="F?"  Part="1" 
AR Path="/5FC2CC0E/5FC4BD46" Ref="F11"  Part="1" 
F 0 "F11" V 7525 1350 50  0000 C CNN
F 1 "Polyfuse" V 7616 1350 50  0000 C CNN
F 2 "Fuse:Fuse_0603_1608Metric" H 7800 1150 50  0001 L CNN
F 3 "530-0ZCM0002FF2G" H 7750 1350 50  0001 C CNN
	1    7750 1350
	0    1    1    0   
$EndComp
$Comp
L power:GND #PWR?
U 1 1 5FC4BD4C
P 8150 1800
AR Path="/5FAFE50B/5FC4BD4C" Ref="#PWR?"  Part="1" 
AR Path="/5FB05F69/5FC4BD4C" Ref="#PWR?"  Part="1" 
AR Path="/5FC4BD4C" Ref="#PWR?"  Part="1" 
AR Path="/5FC2CC0E/5FC4BD4C" Ref="#PWR065"  Part="1" 
F 0 "#PWR065" H 8150 1550 50  0001 C CNN
F 1 "GND" H 8155 1627 50  0000 C CNN
F 2 "" H 8150 1800 50  0001 C CNN
F 3 "" H 8150 1800 50  0001 C CNN
	1    8150 1800
	1    0    0    -1  
$EndComp
Wire Wire Line
	7900 1350 8150 1350
Connection ~ 8150 1350
Wire Wire Line
	8600 1350 9300 1350
Text HLabel 7600 1350 0    50   Input ~ 0
SDADC3P
Text HLabel 9300 1350 2    50   Input ~ 0
PB14
$Comp
L Device:R R?
U 1 1 5FC4BD57
P 8150 2500
AR Path="/5FAFE50B/5FC4BD57" Ref="R?"  Part="1" 
AR Path="/5FB05F69/5FC4BD57" Ref="R?"  Part="1" 
AR Path="/5FC4BD57" Ref="R?"  Part="1" 
AR Path="/5FC2CC0E/5FC4BD57" Ref="R30"  Part="1" 
F 0 "R30" H 8220 2546 50  0000 L CNN
F 1 "150" H 8220 2455 50  0000 L CNN
F 2 "Resistor_SMD:R_1206_3216Metric" V 8080 2500 50  0001 C CNN
F 3 "660-SG73P2BTTD151G" H 8150 2500 50  0001 C CNN
	1    8150 2500
	1    0    0    -1  
$EndComp
$Comp
L Device:R R?
U 1 1 5FC4BD72
P 8450 2200
AR Path="/5FAFE50B/5FC4BD72" Ref="R?"  Part="1" 
AR Path="/5FB05F69/5FC4BD72" Ref="R?"  Part="1" 
AR Path="/5FC4BD72" Ref="R?"  Part="1" 
AR Path="/5FC2CC0E/5FC4BD72" Ref="R32"  Part="1" 
F 0 "R32" V 8657 2200 50  0000 C CNN
F 1 "2k" V 8566 2200 50  0000 C CNN
F 2 "Resistor_SMD:R_1206_3216Metric" V 8380 2200 50  0001 C CNN
F 3 "71-CRCW12062K00FKEAC" H 8450 2200 50  0001 C CNN
	1    8450 2200
	0    -1   -1   0   
$EndComp
Wire Wire Line
	8150 2350 8150 2200
Wire Wire Line
	8150 2200 8300 2200
$Comp
L Device:Polyfuse F?
U 1 1 5FC4BD7A
P 7750 2200
AR Path="/5FAFE50B/5FC4BD7A" Ref="F?"  Part="1" 
AR Path="/5FB05F69/5FC4BD7A" Ref="F?"  Part="1" 
AR Path="/5FC4BD7A" Ref="F?"  Part="1" 
AR Path="/5FC2CC0E/5FC4BD7A" Ref="F12"  Part="1" 
F 0 "F12" V 7525 2200 50  0000 C CNN
F 1 "Polyfuse" V 7616 2200 50  0000 C CNN
F 2 "Fuse:Fuse_0603_1608Metric" H 7800 2000 50  0001 L CNN
F 3 "530-0ZCM0002FF2G" H 7750 2200 50  0001 C CNN
	1    7750 2200
	0    1    1    0   
$EndComp
$Comp
L power:GND #PWR?
U 1 1 5FC4BD80
P 8150 2650
AR Path="/5FAFE50B/5FC4BD80" Ref="#PWR?"  Part="1" 
AR Path="/5FB05F69/5FC4BD80" Ref="#PWR?"  Part="1" 
AR Path="/5FC4BD80" Ref="#PWR?"  Part="1" 
AR Path="/5FC2CC0E/5FC4BD80" Ref="#PWR066"  Part="1" 
F 0 "#PWR066" H 8150 2400 50  0001 C CNN
F 1 "GND" H 8155 2477 50  0000 C CNN
F 2 "" H 8150 2650 50  0001 C CNN
F 3 "" H 8150 2650 50  0001 C CNN
	1    8150 2650
	1    0    0    -1  
$EndComp
Wire Wire Line
	7900 2200 8150 2200
Connection ~ 8150 2200
Wire Wire Line
	8600 2200 9300 2200
Text HLabel 7600 2200 0    50   Input ~ 0
SDADC3M
Text HLabel 9300 2200 2    50   Input ~ 0
PB15
$Comp
L Device:R R?
U 1 1 5FC64CC3
P 2100 3950
AR Path="/5FAFE50B/5FC64CC3" Ref="R?"  Part="1" 
AR Path="/5FB05F69/5FC64CC3" Ref="R?"  Part="1" 
AR Path="/5FC64CC3" Ref="R?"  Part="1" 
AR Path="/5FC2CC0E/5FC64CC3" Ref="R19"  Part="1" 
F 0 "R19" H 2170 3996 50  0000 L CNN
F 1 "150" H 2170 3905 50  0000 L CNN
F 2 "Resistor_SMD:R_1206_3216Metric" V 2030 3950 50  0001 C CNN
F 3 "660-SG73P2BTTD151G" H 2100 3950 50  0001 C CNN
	1    2100 3950
	1    0    0    -1  
$EndComp
$Comp
L Device:R R?
U 1 1 5FC64CDE
P 2400 3650
AR Path="/5FAFE50B/5FC64CDE" Ref="R?"  Part="1" 
AR Path="/5FB05F69/5FC64CDE" Ref="R?"  Part="1" 
AR Path="/5FC64CDE" Ref="R?"  Part="1" 
AR Path="/5FC2CC0E/5FC64CDE" Ref="R23"  Part="1" 
F 0 "R23" V 2607 3650 50  0000 C CNN
F 1 "2k" V 2516 3650 50  0000 C CNN
F 2 "Resistor_SMD:R_1206_3216Metric" V 2330 3650 50  0001 C CNN
F 3 "71-CRCW12062K00FKEAC" H 2400 3650 50  0001 C CNN
	1    2400 3650
	0    -1   -1   0   
$EndComp
Wire Wire Line
	2100 3800 2100 3650
Wire Wire Line
	2100 3650 2250 3650
$Comp
L Device:Polyfuse F?
U 1 1 5FC64CE6
P 1700 3650
AR Path="/5FAFE50B/5FC64CE6" Ref="F?"  Part="1" 
AR Path="/5FB05F69/5FC64CE6" Ref="F?"  Part="1" 
AR Path="/5FC64CE6" Ref="F?"  Part="1" 
AR Path="/5FC2CC0E/5FC64CE6" Ref="F7"  Part="1" 
F 0 "F7" V 1475 3650 50  0000 C CNN
F 1 "Polyfuse" V 1566 3650 50  0000 C CNN
F 2 "Fuse:Fuse_0603_1608Metric" H 1750 3450 50  0001 L CNN
F 3 "530-0ZCM0002FF2G" H 1700 3650 50  0001 C CNN
	1    1700 3650
	0    1    1    0   
$EndComp
$Comp
L power:GND #PWR?
U 1 1 5FC64CEC
P 2100 4100
AR Path="/5FAFE50B/5FC64CEC" Ref="#PWR?"  Part="1" 
AR Path="/5FB05F69/5FC64CEC" Ref="#PWR?"  Part="1" 
AR Path="/5FC64CEC" Ref="#PWR?"  Part="1" 
AR Path="/5FC2CC0E/5FC64CEC" Ref="#PWR049"  Part="1" 
F 0 "#PWR049" H 2100 3850 50  0001 C CNN
F 1 "GND" H 2105 3927 50  0000 C CNN
F 2 "" H 2100 4100 50  0001 C CNN
F 3 "" H 2100 4100 50  0001 C CNN
	1    2100 4100
	1    0    0    -1  
$EndComp
Wire Wire Line
	1850 3650 2100 3650
Connection ~ 2100 3650
Wire Wire Line
	2550 3650 3250 3650
Text HLabel 1550 3650 0    50   Input ~ 0
SDADC1
Text HLabel 3250 3650 2    50   Input ~ 0
PB2
$Comp
L Device:R R?
U 1 1 5FC64CF7
P 2100 4900
AR Path="/5FAFE50B/5FC64CF7" Ref="R?"  Part="1" 
AR Path="/5FB05F69/5FC64CF7" Ref="R?"  Part="1" 
AR Path="/5FC64CF7" Ref="R?"  Part="1" 
AR Path="/5FC2CC0E/5FC64CF7" Ref="R20"  Part="1" 
F 0 "R20" H 2170 4946 50  0000 L CNN
F 1 "150" H 2170 4855 50  0000 L CNN
F 2 "Resistor_SMD:R_1206_3216Metric" V 2030 4900 50  0001 C CNN
F 3 "660-SG73P2BTTD151G" H 2100 4900 50  0001 C CNN
	1    2100 4900
	1    0    0    -1  
$EndComp
$Comp
L Device:R R?
U 1 1 5FC64D12
P 2400 4600
AR Path="/5FAFE50B/5FC64D12" Ref="R?"  Part="1" 
AR Path="/5FB05F69/5FC64D12" Ref="R?"  Part="1" 
AR Path="/5FC64D12" Ref="R?"  Part="1" 
AR Path="/5FC2CC0E/5FC64D12" Ref="R24"  Part="1" 
F 0 "R24" V 2607 4600 50  0000 C CNN
F 1 "2k" V 2516 4600 50  0000 C CNN
F 2 "Resistor_SMD:R_1206_3216Metric" V 2330 4600 50  0001 C CNN
F 3 "71-CRCW12062K00FKEAC" H 2400 4600 50  0001 C CNN
	1    2400 4600
	0    -1   -1   0   
$EndComp
Wire Wire Line
	2100 4750 2100 4600
Wire Wire Line
	2100 4600 2250 4600
$Comp
L Device:Polyfuse F?
U 1 1 5FC64D1A
P 1700 4600
AR Path="/5FAFE50B/5FC64D1A" Ref="F?"  Part="1" 
AR Path="/5FB05F69/5FC64D1A" Ref="F?"  Part="1" 
AR Path="/5FC64D1A" Ref="F?"  Part="1" 
AR Path="/5FC2CC0E/5FC64D1A" Ref="F8"  Part="1" 
F 0 "F8" V 1475 4600 50  0000 C CNN
F 1 "Polyfuse" V 1566 4600 50  0000 C CNN
F 2 "Fuse:Fuse_0603_1608Metric" H 1750 4400 50  0001 L CNN
F 3 "530-0ZCM0002FF2G" H 1700 4600 50  0001 C CNN
	1    1700 4600
	0    1    1    0   
$EndComp
$Comp
L power:GND #PWR?
U 1 1 5FC64D20
P 2100 5050
AR Path="/5FAFE50B/5FC64D20" Ref="#PWR?"  Part="1" 
AR Path="/5FB05F69/5FC64D20" Ref="#PWR?"  Part="1" 
AR Path="/5FC64D20" Ref="#PWR?"  Part="1" 
AR Path="/5FC2CC0E/5FC64D20" Ref="#PWR050"  Part="1" 
F 0 "#PWR050" H 2100 4800 50  0001 C CNN
F 1 "GND" H 2105 4877 50  0000 C CNN
F 2 "" H 2100 5050 50  0001 C CNN
F 3 "" H 2100 5050 50  0001 C CNN
	1    2100 5050
	1    0    0    -1  
$EndComp
Wire Wire Line
	1850 4600 2100 4600
Connection ~ 2100 4600
Wire Wire Line
	2550 4600 3250 4600
Text HLabel 1550 4600 0    50   Input ~ 0
SDADC2
Text HLabel 3250 4600 2    50   Input ~ 0
PD8
Wire Notes Line
	950  700  10300 700 
Wire Notes Line
	10300 700  10300 2950
Wire Notes Line
	10300 2950 950  2950
Wire Notes Line
	950  2950 950  700 
Wire Notes Line
	950  3050 4000 3050
Wire Notes Line
	4000 3050 4000 5300
Wire Notes Line
	4000 5300 950  5300
Wire Notes Line
	950  5300 950  3050
Text Notes 9750 2900 0    50   ~ 0
Differential
Text Notes 3700 5250 0    50   ~ 0
Single
$Comp
L power:GND #PWR?
U 1 1 5FACE871
P 3700 1050
AR Path="/5FAFDC1C/5FACE871" Ref="#PWR?"  Part="1" 
AR Path="/5FC2CC0E/5FACE871" Ref="#PWR034"  Part="1" 
F 0 "#PWR034" H 3700 800 50  0001 C CNN
F 1 "GND" H 3705 877 50  0000 C CNN
F 2 "" H 3700 1050 50  0001 C CNN
F 3 "" H 3700 1050 50  0001 C CNN
	1    3700 1050
	1    0    0    -1  
$EndComp
$Comp
L Diode:BZX84Cxx D?
U 1 1 5FACE877
P 3400 1050
AR Path="/5FAFDC1C/5FACE877" Ref="D?"  Part="1" 
AR Path="/5FC2CC0E/5FACE877" Ref="D18"  Part="1" 
F 0 "D18" H 3400 1275 50  0000 C CNN
F 1 "BZX84C3V6LT1G" H 3400 1184 50  0000 C CNN
F 2 "Diode_SMD:D_SOT-23_ANK" H 3475 1175 50  0001 L CNN
F 3 "863-BZX84C3V6LT1G" H 3280 1050 50  0001 C CNN
	1    3400 1050
	1    0    0    -1  
$EndComp
Wire Wire Line
	3700 1050 3550 1050
Wire Wire Line
	3250 1050 3250 1350
$Comp
L power:GND #PWR?
U 1 1 5FAD3833
P 6600 1050
AR Path="/5FAFDC1C/5FAD3833" Ref="#PWR?"  Part="1" 
AR Path="/5FC2CC0E/5FAD3833" Ref="#PWR038"  Part="1" 
F 0 "#PWR038" H 6600 800 50  0001 C CNN
F 1 "GND" H 6605 877 50  0000 C CNN
F 2 "" H 6600 1050 50  0001 C CNN
F 3 "" H 6600 1050 50  0001 C CNN
	1    6600 1050
	1    0    0    -1  
$EndComp
$Comp
L Diode:BZX84Cxx D?
U 1 1 5FAD3839
P 6300 1050
AR Path="/5FAFDC1C/5FAD3839" Ref="D?"  Part="1" 
AR Path="/5FC2CC0E/5FAD3839" Ref="D22"  Part="1" 
F 0 "D22" H 6300 1275 50  0000 C CNN
F 1 "BZX84C3V6LT1G" H 6300 1184 50  0000 C CNN
F 2 "Diode_SMD:D_SOT-23_ANK" H 6375 1175 50  0001 L CNN
F 3 "863-BZX84C3V6LT1G" H 6180 1050 50  0001 C CNN
	1    6300 1050
	1    0    0    -1  
$EndComp
Wire Wire Line
	6600 1050 6450 1050
Wire Wire Line
	6150 1050 6150 1350
$Comp
L power:GND #PWR?
U 1 1 5FAD7E64
P 9750 1050
AR Path="/5FAFDC1C/5FAD7E64" Ref="#PWR?"  Part="1" 
AR Path="/5FC2CC0E/5FAD7E64" Ref="#PWR040"  Part="1" 
F 0 "#PWR040" H 9750 800 50  0001 C CNN
F 1 "GND" H 9755 877 50  0000 C CNN
F 2 "" H 9750 1050 50  0001 C CNN
F 3 "" H 9750 1050 50  0001 C CNN
	1    9750 1050
	1    0    0    -1  
$EndComp
$Comp
L Diode:BZX84Cxx D?
U 1 1 5FAD7E6A
P 9450 1050
AR Path="/5FAFDC1C/5FAD7E6A" Ref="D?"  Part="1" 
AR Path="/5FC2CC0E/5FAD7E6A" Ref="D24"  Part="1" 
F 0 "D24" H 9450 1275 50  0000 C CNN
F 1 "BZX84C3V6LT1G" H 9450 1184 50  0000 C CNN
F 2 "Diode_SMD:D_SOT-23_ANK" H 9525 1175 50  0001 L CNN
F 3 "863-BZX84C3V6LT1G" H 9330 1050 50  0001 C CNN
	1    9450 1050
	1    0    0    -1  
$EndComp
Wire Wire Line
	9750 1050 9600 1050
Wire Wire Line
	9300 1050 9300 1350
$Comp
L power:GND #PWR?
U 1 1 5FADC73D
P 9750 1900
AR Path="/5FAFDC1C/5FADC73D" Ref="#PWR?"  Part="1" 
AR Path="/5FC2CC0E/5FADC73D" Ref="#PWR041"  Part="1" 
F 0 "#PWR041" H 9750 1650 50  0001 C CNN
F 1 "GND" H 9755 1727 50  0000 C CNN
F 2 "" H 9750 1900 50  0001 C CNN
F 3 "" H 9750 1900 50  0001 C CNN
	1    9750 1900
	1    0    0    -1  
$EndComp
$Comp
L Diode:BZX84Cxx D?
U 1 1 5FADC743
P 9450 1900
AR Path="/5FAFDC1C/5FADC743" Ref="D?"  Part="1" 
AR Path="/5FC2CC0E/5FADC743" Ref="D25"  Part="1" 
F 0 "D25" H 9450 2125 50  0000 C CNN
F 1 "BZX84C3V6LT1G" H 9450 2034 50  0000 C CNN
F 2 "Diode_SMD:D_SOT-23_ANK" H 9525 2025 50  0001 L CNN
F 3 "863-BZX84C3V6LT1G" H 9330 1900 50  0001 C CNN
	1    9450 1900
	1    0    0    -1  
$EndComp
Wire Wire Line
	9750 1900 9600 1900
Wire Wire Line
	9300 1900 9300 2200
$Comp
L power:GND #PWR?
U 1 1 5FAE2B0F
P 6600 1900
AR Path="/5FAFDC1C/5FAE2B0F" Ref="#PWR?"  Part="1" 
AR Path="/5FC2CC0E/5FAE2B0F" Ref="#PWR039"  Part="1" 
F 0 "#PWR039" H 6600 1650 50  0001 C CNN
F 1 "GND" H 6605 1727 50  0000 C CNN
F 2 "" H 6600 1900 50  0001 C CNN
F 3 "" H 6600 1900 50  0001 C CNN
	1    6600 1900
	1    0    0    -1  
$EndComp
$Comp
L Diode:BZX84Cxx D?
U 1 1 5FAE2B15
P 6300 1900
AR Path="/5FAFDC1C/5FAE2B15" Ref="D?"  Part="1" 
AR Path="/5FC2CC0E/5FAE2B15" Ref="D23"  Part="1" 
F 0 "D23" H 6300 2125 50  0000 C CNN
F 1 "BZX84C3V6LT1G" H 6300 2034 50  0000 C CNN
F 2 "Diode_SMD:D_SOT-23_ANK" H 6375 2025 50  0001 L CNN
F 3 "863-BZX84C3V6LT1G" H 6180 1900 50  0001 C CNN
	1    6300 1900
	1    0    0    -1  
$EndComp
Wire Wire Line
	6600 1900 6450 1900
Wire Wire Line
	6150 1900 6150 2200
$Comp
L power:GND #PWR?
U 1 1 5FAE5E6C
P 3700 1900
AR Path="/5FAFDC1C/5FAE5E6C" Ref="#PWR?"  Part="1" 
AR Path="/5FC2CC0E/5FAE5E6C" Ref="#PWR035"  Part="1" 
F 0 "#PWR035" H 3700 1650 50  0001 C CNN
F 1 "GND" H 3705 1727 50  0000 C CNN
F 2 "" H 3700 1900 50  0001 C CNN
F 3 "" H 3700 1900 50  0001 C CNN
	1    3700 1900
	1    0    0    -1  
$EndComp
$Comp
L Diode:BZX84Cxx D?
U 1 1 5FAE5E72
P 3400 1900
AR Path="/5FAFDC1C/5FAE5E72" Ref="D?"  Part="1" 
AR Path="/5FC2CC0E/5FAE5E72" Ref="D19"  Part="1" 
F 0 "D19" H 3400 2125 50  0000 C CNN
F 1 "BZX84C3V6LT1G" H 3400 2034 50  0000 C CNN
F 2 "Diode_SMD:D_SOT-23_ANK" H 3475 2025 50  0001 L CNN
F 3 "863-BZX84C3V6LT1G" H 3280 1900 50  0001 C CNN
	1    3400 1900
	1    0    0    -1  
$EndComp
Wire Wire Line
	3700 1900 3550 1900
Wire Wire Line
	3250 1900 3250 2200
$Comp
L power:GND #PWR?
U 1 1 5FAE8C31
P 3700 3350
AR Path="/5FAFDC1C/5FAE8C31" Ref="#PWR?"  Part="1" 
AR Path="/5FC2CC0E/5FAE8C31" Ref="#PWR036"  Part="1" 
F 0 "#PWR036" H 3700 3100 50  0001 C CNN
F 1 "GND" H 3705 3177 50  0000 C CNN
F 2 "" H 3700 3350 50  0001 C CNN
F 3 "" H 3700 3350 50  0001 C CNN
	1    3700 3350
	1    0    0    -1  
$EndComp
$Comp
L Diode:BZX84Cxx D?
U 1 1 5FAE8C37
P 3400 3350
AR Path="/5FAFDC1C/5FAE8C37" Ref="D?"  Part="1" 
AR Path="/5FC2CC0E/5FAE8C37" Ref="D20"  Part="1" 
F 0 "D20" H 3400 3575 50  0000 C CNN
F 1 "BZX84C3V6LT1G" H 3400 3484 50  0000 C CNN
F 2 "Diode_SMD:D_SOT-23_ANK" H 3475 3475 50  0001 L CNN
F 3 "863-BZX84C3V6LT1G" H 3280 3350 50  0001 C CNN
	1    3400 3350
	1    0    0    -1  
$EndComp
Wire Wire Line
	3700 3350 3550 3350
Wire Wire Line
	3250 3350 3250 3650
$Comp
L power:GND #PWR?
U 1 1 5FAEC28A
P 3700 4300
AR Path="/5FAFDC1C/5FAEC28A" Ref="#PWR?"  Part="1" 
AR Path="/5FC2CC0E/5FAEC28A" Ref="#PWR037"  Part="1" 
F 0 "#PWR037" H 3700 4050 50  0001 C CNN
F 1 "GND" H 3705 4127 50  0000 C CNN
F 2 "" H 3700 4300 50  0001 C CNN
F 3 "" H 3700 4300 50  0001 C CNN
	1    3700 4300
	1    0    0    -1  
$EndComp
$Comp
L Diode:BZX84Cxx D?
U 1 1 5FAEC290
P 3400 4300
AR Path="/5FAFDC1C/5FAEC290" Ref="D?"  Part="1" 
AR Path="/5FC2CC0E/5FAEC290" Ref="D21"  Part="1" 
F 0 "D21" H 3400 4525 50  0000 C CNN
F 1 "BZX84C3V6LT1G" H 3400 4434 50  0000 C CNN
F 2 "Diode_SMD:D_SOT-23_ANK" H 3475 4425 50  0001 L CNN
F 3 "863-BZX84C3V6LT1G" H 3280 4300 50  0001 C CNN
	1    3400 4300
	1    0    0    -1  
$EndComp
Wire Wire Line
	3700 4300 3550 4300
Wire Wire Line
	3250 4300 3250 4600
$EndSCHEMATC
