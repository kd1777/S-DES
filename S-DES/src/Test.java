import java.util.Arrays;
import java.util.Random;

public class Test {
    public static void main(String[] args) {

//         test01();
//        test03();

        // test04();
        //test05();
    }

    //测试标准输入
    public static void test01() {
        int[] key = {0, 0, 1, 0, 0, 0, 1, 1, 0, 1};
        int[] p = {1, 1, 0, 0, 0, 0, 0, 1};
        S_DES MyDes = new S_DES(key);
        System.out.println("测试标准加密:");
        //加密测试
        System.out.print("输入明文：");
        MyDes.print(p, 8);
        int[] C = MyDes.S_Encrypt(p);
        System.out.print("输出密文：");
        MyDes.print(C, 8);

        //解密测试
        System.out.print("输入密文："); //将密文作为解密的输入
        MyDes.print(C, 8);
        int[] P = MyDes.S_Decrypt(C);
        System.out.print("输出明文：");
        MyDes.print(P, 8);
    }

    //测试拓展功能
    public static void test03() {
        int[] key = {1, 0, 1, 0, 0, 0, 0, 0, 1, 0};
        String p = "I like eating apple";

        S_DES MyDes = new S_DES(key);
        System.out.println("测试拓展功能:");
        System.out.print("输入明文:");
        System.out.println(p);
        String c = MyDes.Encrypt(p);
        System.out.print("输出密文:");
        System.out.println(c);

        System.out.print("输入密文:");
        System.out.println(c);
        String P = MyDes.Decrypt(c);
        System.out.print("输出明文:");
        System.out.println(P);
    }


    //尝试暴力破解
    //暴力破解的方法是从000000000到1111111111遍历密钥，通过尝试加密p得到c来确认遍历
    //到的密钥是我们想到破解出来的，然后用另一对明密文对p2 c2来确认密钥的正确性
    public static void test04() {
        S_DES MyDes = new S_DES();
        int[] key = generate_nbit(10);
        System.out.print("用以加密的密钥是:");
        MyDes.print(key, 10);
        MyDes.setKey(key);

        System.out.println("下面是暴力破解过程：");
        // 生成两组明文

        int[] p1 = generate_nbit(8);
        int[] p2 = generate_nbit(8);
        System.out.print("明文p1:");
        MyDes.print(p1, 8);
        System.out.print("明文p2:");
        MyDes.print(p2, 8);

        // 使用key加密明文
        int[] c1 = MyDes.S_Encrypt(p1);
        int[] c2 = MyDes.S_Encrypt(p2);
        System.out.print("密文c1:");
        MyDes.print(c1, 8);
        System.out.print("密文c2:");
        MyDes.print(c2, 8);

        S_DES testDes = new S_DES();
        int[] result_key = new int[10];

        // 遍历所有可能的10位密钥
        for (int i = 0; i < 1024; i++) {

            int[] testKeyArray = generate_test_key(i);
            testDes.setKey(testKeyArray);
            int[] temp = testDes.S_Encrypt(p1);

            // 检查是否找到了正确的密钥
            if (Arrays.equals(temp, c1)) {
                int[] confirmC = testDes.S_Encrypt(p2);
                if (Arrays.equals(confirmC, c2)) {
                    System.out.println("暴力破解得到的密钥是:");
                    testDes.copy(result_key, testKeyArray);
                    MyDes.print(result_key, 10);
                    break;
                } else {
                    System.out.println("未找到正确的密钥");
                }

            } else {
                MyDes.print(testKeyArray, 10);
                System.out.println("不是正确的密钥。");

            }
        }


    }

    //封闭测试
    //由于在test04调试中我发现了会出现不同的密钥ki！=kj 用他们加密同一个明文p会出现相同密文c，在此处予以验证
    public static void test05() {
        while (true) {
            // 8位明文
            int[] key = generate_nbit(10);
            int[] p1 = generate_nbit(8);

            S_DES MyDes = new S_DES(key);
            int[] c1 = MyDes.S_Encrypt(p1);
            int[] result_key1 = new int[10];
            MyDes.copy(result_key1, key);
            int[] result_key2 = new int[10];
            boolean find = false;

            // 尝试使用不同的密钥来加密明文
            for (int i = 0; i < 1024; i++) {
                int[] testKeyArray = generate_test_key(i);
                MyDes.setKey(testKeyArray);  // 设置新的测试密钥
                int[] temp = MyDes.S_Encrypt(p1); // 使用测试密钥加密

                // 如果找到了一个不同的密钥，并且加密结果与原始密文相同

                if (Arrays.equals(temp, c1) && !Arrays.equals(result_key1, testKeyArray)) {
                    MyDes.copy(result_key2, testKeyArray);
                    find = true;
                    break;
                }
            }

            // 输出明文和密文
            System.out.print("对于明密文对\nP:");
            MyDes.print(p1, 8);

            System.out.print("C:");
            MyDes.print(c1, 8);

            if (find) {
                System.out.print("找到了两个密钥\nk1:");
                MyDes.print(result_key1, 10);
                System.out.print("k2:");
                MyDes.print(result_key2, 10);
                System.out.print("使得加密出来密文相同\n");
                break;
            } else {
                System.out.print("没有找到\n");
            }

        }
    }


    // 假设的generate_nbit方法实现
    public static int[] generate_nbit(int n) {
        Random random = new Random();
        int[] output = new int[n];
        for (int i = 0; i < n; i++) {
            output[i] = random.nextInt(2);
        }
        return output;
    }

    // 假设的generate_test_key方法实现
    public static int[] generate_test_key(int num) {
        int[] key = new int[10];
        for (int i = 0; i < 10; i++) {
            key[i] = (num >> (9 - i)) & 1;
        }
        return key;
    }

}
