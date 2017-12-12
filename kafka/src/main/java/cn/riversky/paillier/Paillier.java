package cn.riversky.paillier;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Random;

/**
 * @author Created by admin on 2017/12/9.
 */
public class Paillier {
    /**
     * p and q are two large primes. lambda = lcm(p-1, q-1) =(p-1)*(q-1)/gcd(p-1, q-1).
     */
    private BigInteger p, q, lambda;
    /**
     * n = p*q, where p and q are two large primes.
     */
    public BigInteger n;
    /**
     * nsquare = n*n
     */
    public BigInteger nsquare;
    /**
     * a random integer in Z*_{n^2} where gcd (L(g^lambda mod n^2), n) = 1.
     */
    private BigInteger g;
    /**
     * number of bits of modulus
     */
    private int bitLength;

    /**
     * Constructs an instance of the Paillier cryptosystem.
     *
     * @param bitLengthVal
     *            number of bits of modulus
     * @param certainty
     *            The probability that the new BigInteger represents a prime
     *            number will exceed (1 - 2^(-certainty)). The execution time of
     *            this constructor is proportional to the value of this
     *            parameter.
     */
    public Paillier(int bitLengthVal, int certainty) {
        keyGeneration(bitLengthVal, certainty);
    }

    /**
     * Constructs an instance of the Paillier cryptosystem with 512 bits of
     * modulus and at least 1-2^(-64) certainty of primes generation.
     */
    public Paillier() {
        keyGeneration(512, 64);
    }

    /**
     * Sets up the public key and private key.
     *设置公钥和私钥
     * @param bitLengthVal
     *            系数长度.
     * @param certainty
     *          新的BigInteger代表素数的概率将超过（1 - 2 ^（ - 确定性））。
     *          此构造函数的执行时间与此参数的值成比例。
     *
     */
    public void keyGeneration(int bitLengthVal, int certainty) {
        bitLength = bitLengthVal;
        /*
         * Constructs two randomly generated positive BigIntegers that are
         * probably prime, with the specified bitLength and certainty.
         */
        p = new BigInteger(bitLength / 2, certainty, new Random());
        q = new BigInteger(bitLength / 2, certainty, new Random());

        n = p.multiply(q);
        nsquare = n.multiply(n);

        g = new BigInteger("2");
        lambda = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE))
                .divide(p.subtract(BigInteger.ONE).gcd(q.subtract(BigInteger.ONE)));
        /* check whether g is good. */
        if (g.modPow(lambda, nsquare).subtract(BigInteger.ONE).divide(n).gcd(n).intValue() != 1) {
            System.out.println("g is not good. Choose g again.");
            System.exit(1);
        }
    }

    /**
     * Encrypts plaintext m. ciphertext c = g^m * r^n mod n^2. This function
     * explicitly requires random input r to help with encryption.
     *
     * @param m
     *            plaintext as a BigInteger
     * @param r
     *            random plaintext to help with encryption
     * @return ciphertext as a BigInteger
     */
    public BigInteger encryption(BigInteger m, BigInteger r) {
        return g.modPow(m, nsquare).multiply(r.modPow(n, nsquare)).mod(nsquare);
    }

    /**
     * Encrypts plaintext m. ciphertext c = g^m * r^n mod n^2. This function
     * automatically generates random input r (to help with encryption).
     *
     * @param m
     *            plaintext as a BigInteger
     * @return ciphertext as a BigInteger
     */
    public BigInteger encryption(BigInteger m) {
        BigInteger r = new BigInteger(bitLength, new Random());
        return g.modPow(m, nsquare).multiply(r.modPow(n, nsquare)).mod(nsquare);

    }

    /**
     * Decrypts ciphertext c. plaintext m = L(c^lambda mod n^2) * u mod n, where
     * u = (L(g^lambda mod n^2))^(-1) mod n.
     *
     * @param c
     *            ciphertext as a BigInteger
     * @return plaintext as a BigInteger
     */
    public BigInteger decryption(BigInteger c) {
        BigInteger u = g.modPow(lambda, nsquare).subtract(BigInteger.ONE).divide(n).modInverse(n);
        return c.modPow(lambda, nsquare).subtract(BigInteger.ONE).divide(n).multiply(u).mod(n);
    }

    /**
     * sum of (cipher) em1 and em2
     *
     * @param em1
     * @param em2
     * @return
     */
    public BigInteger cipherAdd(BigInteger em1, BigInteger em2) {
        return em1.multiply(em2).mod(nsquare);
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
         /* 实例化Paillier密码系统的对象 */
        Paillier paillier = new Paillier();
        /* 实例化两个明文消息 */
        BigInteger m1 = new BigInteger("hello word".getBytes("UTF-8"));
        BigInteger m2 = new BigInteger("世界".getBytes("UTF-8"));
        /* 加密 */
        BigInteger em1 = paillier.encryption(m1);
        BigInteger em2 = paillier.encryption(m2);
        /*打印加密的文本*/
        System.out.println(em1);
        System.out.println(em2);
        /* 打印输出解密文本 */
        System.out.println(new String(paillier.decryption(em1).toByteArray()));
        System.out.println(new String(paillier.decryption(em2).toByteArray(),"UTF-8"));
    }
    /**
     * main function
     *
     * @param str
     *            intput string
     */
    public static void main1(String[] str) {
        /* 实例化Paillier密码系统的对象 */
        Paillier paillier = new Paillier();
        /* 实例化两个明文消息 */
        BigInteger m1 = new BigInteger("20");
        BigInteger m2 = new BigInteger("60");
        /* 加密 */
        BigInteger em1 = paillier.encryption(m1);
        BigInteger em2 = paillier.encryption(m2);
        /*打印加密的文本*/
        System.out.println(em1);
        System.out.println(em2);
        /* 打印输出解密文本 */
        System.out.println(paillier.decryption(em1).toString());
        System.out.println(paillier.decryption(em2).toString());

        /*
         * 测试同态属性 -> D(E(m1)*E(m2) mod n^2) = (m1 + m2) mod  n
         */
        // m1+m2,求明文数值的和
        BigInteger sumM1m2 = m1.add(m2).mod(paillier.n);
        System.out.println("明文数值的和: " + sumM1m2.toString());
        // em1+em2，求密文数值的乘
        BigInteger productEm1em2 = em1.multiply(em2).mod(paillier.nsquare);
        System.out.println("encrypted sum: " + productEm1em2.toString());
        System.out.println("decrypted sum: " + paillier.decryption(productEm1em2).toString());

        /* test homomorphic properties -> D(E(m1)^m2 mod n^2) = (m1*m2) mod n */
        // m1*m2,求明文数值的乘
        BigInteger prodM1m2 = m1.multiply(m2).mod(paillier.n);
        System.out.println("original product: " + prodM1m2.toString());
        // em1的m2次方，再mod paillier.nsquare
        BigInteger expoEm1m2 = em1.modPow(m2, paillier.nsquare);
        System.out.println("encrypted product: " + expoEm1m2.toString());
        System.out.println("decrypted product: " + paillier.decryption(expoEm1m2).toString());

        //sum test
        System.out.println("--------------------------------");
        Paillier p = new Paillier();
        BigInteger t1 = new BigInteger("21");System.out.println(t1.toString());
        BigInteger t2 = new BigInteger("50");System.out.println(t2.toString());
        BigInteger t3 = new BigInteger("50");System.out.println(t3.toString());
        BigInteger et1 = p.encryption(t1);System.out.println(et1.toString());
        BigInteger et2 = p.encryption(t2);System.out.println(et2.toString());
        BigInteger et3 = p.encryption(t3);System.out.println(et3.toString());
        BigInteger sum = new BigInteger("1");
        sum = p.cipherAdd(sum, et1);
        sum = p.cipherAdd(sum, et2);
        sum = p.cipherAdd(sum, et3);
        System.out.println("sum: "+sum.toString());
        System.out.println("decrypted sum: "+p.decryption(sum).toString());
        System.out.println("--------------------------------");
    }
}
