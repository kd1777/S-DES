import java.util.Arrays;

public class S_DES {
    public int[] P; //明文
    public int[] C; //密文
    public int[] key;//密钥
    public int[][] KEY = new int[2][8]; //轮密钥
    public int[] P10 = {3, 5, 2, 7, 4, 10, 1, 9, 8, 6}; //密钥拓展置
    public int[] P8 = {6, 3, 7, 4, 8, 5, 10, 9};
    public int[] IP_start = {2, 6, 3, 1, 4, 8, 5, 7}; //初始置换盒
    public int[] IP_end = {4, 1, 3, 5, 7, 2, 8, 6}; //最终置换盒
    public int[] EPBox = {4, 1, 2, 3, 2, 3, 4, 1};
    public int[][] SBox1 = {{1, 0, 3, 2}, {3, 2, 1, 0}, {0, 2, 1, 3}, {3, 1, 0, 2}};
    public int[][] SBox2 = {{0, 1, 2, 3}, {2, 3, 1, 0}, {3, 0, 1, 2}, {2, 1, 0, 3}};
    public int[] SPBox = {2, 4, 3, 1};

    public S_DES()
    {
        P = new int[8];
        key = new int[10];
        C = new int[8];
    }
    public S_DES(int[] key) {
        P = new int[8];
        this.key = key;
        C = new int[8];
        generate_round_key();
    }

    //生成轮密钥
    public void generate_round_key() {
        int[] temp = new int[10];
        int[] temp1 = new int[10];
        for (int i = 0; i < 2; i++) {
            //P10
            permute(10, key, temp, P10);
            int shift = 0; //左移位数
            if (i == 0) {
                shift = 1;
            }
            if (i == 1) {
                shift = 2;
            }
            //五个一组循环左移
            for (int j = 0; j < 5; j++) {
                temp1[j] = temp[(j + shift) % 5];
            }
            for (int j = 5; j < 10; j++) {
                if (j + shift < 10) temp1[j] = temp[j + shift];
                else temp1[j] = temp[j + shift - 5];
            }
            //P8
            permute(8, temp1, KEY[i], P8);

        }
    }

    //标准加密 用来加密8位二进制明文
    public int[] S_Encrypt(int[] p) {
        int[] input = new int[8];
        int[] output = new int[8];
        //初始置换
        permute(8, p, input, IP_start);

        //多轮(2轮)变换
        int[] left = new int[4];
        int[] right = new int[4];
        split(input, left, right);

        for (int i = 0; i < 2; i++) {

            //mixer
            int[] temp1 = new int[4];
            copy(temp1, right);
            int[] temp2 = new int[4];
            function(right, KEY[i], temp2);
            int[] temp3 = new int[4];
            XOR(temp3, left, temp2);


            if (i != 1) {
                //swapper
                copy(right, temp3);
                copy(left, temp1);
            } else {
                copy(left, temp3);
            }
        }

        combine(output, left, right);
        int[] c = new int[8];
        //最终置换
        permute(8, output, c, IP_end);

        return c;
    }
    //标准解密，用来解密八位二进制密文
    public int[] S_Decrypt(int[] c) {

        int[] input = new int[8];
        int[] output = new int[8];
        //初始置换
        permute(8, c, input, IP_start);

        //多轮(2轮)变换
        int[] left = new int[4];
        int[] right = new int[4];
        split(input, left, right);

        for (int i = 0; i < 2; i++) {
            //mixer
            int[] temp1 = new int[4];
            copy(temp1, right);
            int[] temp2 = new int[4];
            function(temp1, KEY[1 - i], temp2);
            int[] temp3 = new int[4];
            XOR(temp3, left, temp2);


            if (i != 1) {
                //swapper
                int[] t = new int[4];
                copy(right, temp3);
                copy(left, temp1);
            } else {
                copy(left, temp3);
            }
        }

        combine(output, left, right);
        int[] p = new int[8];
        //最终置换
        permute(8, output, p, IP_end);

        return p;
    }

    //拓展加密，用来加密ASCII码
    public String Encrypt(String p) {
        StringBuilder c = new StringBuilder(); //密文
        int n = p.length();
        for (int i = 0; i < n; i++) {
            int p_number = p.charAt(i); //遍历得到每一个字符的ASCII码
            int[] p_array = IntTo8bits(p_number); //将得到的字符转换为8bit标准输入
            int[] c_array = S_Encrypt(p_array);//将每一位输入得到的密文保存
            int c_number = _8bitsToInt(c_array);
            c.append((char) c_number);
        }
        return c.toString();
    }
    //拓展解密
   public String Decrypt(String c)
   {
       StringBuilder p = new StringBuilder(); //密文
       int n = c.length();
       for (int i = 0; i < n; i++) {
           int c_number = c.charAt(i); //遍历得到每一个字符的ASCII码
           int[] c_array = IntTo8bits(c_number); //将得到的字符转换为8bit标准输入
           int[] p_array = S_Decrypt(c_array);//将每一位输入得到的密文保存
           int p_number = _8bitsToInt(p_array);
           p.append((char) p_number);
       }
       return p.toString();
   }

    //int类型转换为8bit标准输入
    public int[] IntTo8bits(int input) {
        int[] output = new int[8];
        for (int i = 0; i < 8; i++) {
            output[i] = (input >> 7 - i) & 1;
        }
        return output;
    }

    //8bit转换为int类型
    public int _8bitsToInt(int[] input) {
        int output = 0;
        for (int i = 0; i < 8; i++) {
            output += input[i] * (1 << (7 - i)); // 1 << (7 - i) is 2^(7-i)
        }
        return output;
    }





    public void split(int[] input, int[] left, int[] right) {
        int n = input.length;
        for (int i = 0; i < n / 2; i++) {
            left[i] = input[i]; //left
            right[i] = input[n / 2 + i];//right
        }
    }


    public void function(int[] input, int[] key, int[] output) {
        int[] temp1 = new int[8];
        permute(8, input, temp1, EPBox); //EPBOX
        int[] temp2 = new int[8];
        XOR(temp2, temp1, key); //XOR
        int[] temp3 = new int[4];
        //SBox
        SBOX(temp2, temp3);
        permute(4, temp3, output, SPBox);
    }

    public void permute(int n, int[] input, int[] output, int[] Box) {
        int[] temp = new int[input.length];
        copy(temp, input);
        for (int i = 0; i < n; i++) {
            output[i] = temp[Box[i] - 1];
        }
    }

    public void SBOX(int[] input, int[] output) {
        int x1 = input[0] * 2 + input[3];
        int y1 = input[1] * 2 + input[2];
        int x2 = input[4] * 2 + input[7];
        int y2 = input[5] * 2 + input[6];

        int z1 = SBox1[x1][y1];
        int z2 = SBox2[x2][y2];
        output[0] = z1 / 2;
        output[1] = z1 % 2;
        output[2] = z2 / 2;
        output[3] = z2 % 2;

    }

    public void combine(int[] output, int[] left, int[] right) {
        int n = left.length;
        for (int i = 0; i < n; i++) {
            output[i] = left[i];
            output[i + n] = right[i];
        }
    }


    public void print(int[] a, int n) {
        for (int i = 0; i < n; i++) {
            System.out.print(a[i]);
        }
        System.out.println();
    }

    public void XOR(int[] output, int[] a, int[] b) {
        int n = a.length;
        for (int i = 0; i < n; i++) {
            output[i] = a[i] ^ b[i];
        }
    }

    public void copy(int[] a, int[] b) {
        int n = b.length;
        System.arraycopy(b, 0, a, 0, n);
    }
    public void setKey(int[] key)
    {
        copy(this.key,key);
        generate_round_key();
    }
}
