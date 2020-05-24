package pgdp.color;

public class RgbColor {
    final private int red;
    final private int green;
    final private int blue;
    final private int bitDepth;

    //variables to help check validity of colors
    final private int MAX = 2147483647;
    final private int MIN = 0;

    public RgbColor(int bitDepth, int red, int green, int blue) {
        if (bitDepth > 31 || bitDepth <= 0)
            ExceptionUtil.unsupportedOperation("bitDepth with value: "+bitDepth+", is unvalid.");
        this.bitDepth = bitDepth;

        if (!isValid(red))
            ExceptionUtil.unsupportedOperation("color red with value: "+red+", is unvalid.");
        this.red = red;

        if (!isValid(green))
            ExceptionUtil.unsupportedOperation("color green with value: "+green+", is unvalid.");
        this.green = green;

        if (!isValid(blue))
            ExceptionUtil.unsupportedOperation("color blue with value: "+blue+" is unvalid.");
        this.blue = blue;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public int getBitDepth() {
        return bitDepth;
    }

    public RgbColor8Bit toRgbColor8Bit() {
        //we save the three colors in an array to save copy-paste of code later below
        int[] colors = new int[3];
        colors[0] = red;
        colors[1] = green;
        colors[2] = blue;

        if(this.bitDepth == 8) {
            return new RgbColor8Bit(this.red, this.green, this.blue);

        } else if (this.bitDepth > 8){

            for (int i = 0; i < 3; i++) {
                //calculate the difference and do the division with 2^difference - 1
                int difference = this.bitDepth - 8;
                int div = colors[i] / IntMath.powerOfTwo(difference - 1);

                //convert the result to a binary string
                String binaryNumber = Integer.toBinaryString(div);
                //get the last bit from the string and convert it to integer by subtracting ascii value 48
                int lastBit = binaryNumber.charAt(binaryNumber.length()-1) - 48;

                //divide once more by two and add the last bit if it was 1 and if the number is smaller then the max 255
                colors[i] = div / 2;
                if (lastBit == 1 && colors[i] <= 254)
                    colors[i] += 1;
            }

        } else {
            for (int i = 0; i < 3; i++) {
                String binaryNumber = Integer.toBinaryString(colors[i]); //convert each color to a binary number
                StringBuilder filledBitValue = new StringBuilder(); // the result, it saves the complete binary number made of the same bit sequence which repeats itself
                StringBuilder bit = new StringBuilder(); //save the bit sequence

                //add leading 0s to the bit sequence if the number has lesser bits then the required bitDepth
                if (binaryNumber.length() < bitDepth) {
                    for (int j = 0; j < bitDepth - binaryNumber.length(); j++) {
                        bit.append(0);
                    }
                }
                bit.append(binaryNumber); //add the number after the leading 0s
                filledBitValue.append(bit); //add the sequence to the result

                //add each bit in the bit sequence using a circular queue
                for (int j = bit.length(); j < 8; j++) {
                    filledBitValue.append(bit.charAt(j % bit.length()));
                }
                //convert the binary string to integer
                colors[i] = Integer.parseInt(filledBitValue.toString(),2);
            }
        }
        return new RgbColor8Bit(colors[0],colors[1],colors[2]);
    }

    //helper method
    //returns true if the color is valid, otherwise false
    private boolean isValid(int color) {
        if (this.bitDepth == 31)
            return color >= this.MIN && color <= this.MAX;
        else
            return color >= this.MIN && color <= (IntMath.powerOfTwo(this.bitDepth) - 1);
    }

}