一、用户指南
# S-DES 加密/解密工具

## 简介

S-DES (简化数据加密标准) 是一种简化的对称密钥加密算法，适用于教学和学习目的。本项目实现了 S-DES 加密和解密的功能，用户可以使用该工具对 8 位明文进行加密和解密，同时支持基于 ASCII 码的拓展加密和解密。

## 功能

- **标准模式加密/解密**：输入 8 位二进制明文，使用 10 位二进制密钥进行加密和解密。
- **扩展模式加密/解密**：输入任意 ASCII 字符串，使用 10 位二进制密钥进行加密和解密。
- **可视化界面**：使用 Java Swing 提供用户友好的图形界面。

## 系统要求

- Java Development Kit (JDK) 8 或更高版本
- 支持 Java Swing 的操作系统

## 使用说明

1. **克隆或下载项目**：
   ```bash
   git clone https://github.com/yourusername/S-DES.git
   cd S-DES
2.编译与运行： 使用你的 IDE（如 IntelliJ IDEA 或 Eclipse）打开项目，或者在终端中使用以下命令编译和运行：
javac GUI.java S_DES.java
java GUI
3.选择加密模式： 启动程序后，选择“标准加密”或“扩展加密”模式。
4.输入明文和密钥： 在相应的文本框中输入明文和密钥，然后点击“加密”或“解密”按钮查看结果

代码结构
GUI.java：程序的图形用户界面实现。
S_DES.java：S-DES 算法的核心实现，包含加密和解密的逻辑。

二、测试结果

第1关：基本测试   

根据S-DES算法编写和调试程序，提供GUI解密支持用户交互。输入可以是8bit的数据和10bit的密钥，输出是8bit的密文。

![d786d4218b3f824a17fcd47698d8cf48](https://github.com/user-attachments/assets/ea774984-a6c0-411d-825e-25d3eb4df88a)

![24f0752553417d4c3441b02cbd3dfd2c](https://github.com/user-attachments/assets/3e20cec0-f713-4b43-90fe-41298e332dd5)

第2关：交叉测试
神里凌华的狗组测试结果（与第一关测试的对比）
![df806a4a756a2fc01472d940af0ca805](https://github.com/user-attachments/assets/945b6cb4-7f70-4868-b461-e4ba3e03fcc6)





第3关：扩展功能

![image](https://github.com/user-attachments/assets/e304939b-3cdf-428e-89eb-4fde9f6817f9)

![image](https://github.com/user-attachments/assets/5a84701a-f57c-491f-99a0-a70d7d7290d1)

第4关：暴力破解

使用遍历进行暴力破解，均在1s内完成。

![image](https://github.com/user-attachments/assets/968554f9-3c0c-4ab3-9d30-b4c98b5e47a9)

![image](https://github.com/user-attachments/assets/4f7a0b17-8f05-4af7-b07c-1c2629dc0cf8)

第5关：封闭测试

对应明文空间任意给定的明文分组，会出现选择不同的密钥加密得到相同密文的情况。

![image](https://github.com/user-attachments/assets/a4304c9f-8144-41f9-a1d1-e6700b65699e)
























