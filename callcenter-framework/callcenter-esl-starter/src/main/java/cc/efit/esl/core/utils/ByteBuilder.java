package cc.efit.esl.core.utils;


public class ByteBuilder {

    private byte[] b = new byte[128];
    private int index = 0;

    private ByteBuilder() {
    }
    public static ByteBuilder newBuilder() {
        return new ByteBuilder();
    }

    public ByteBuilder append(byte byte0) {
        if (this.b.length <= (index + 1)) {
            byte[] newB = new byte[this.b.length * 2];
            System.arraycopy(this.b, 0, newB, 0, this.b.length);
            this.b = newB;
        }
        this.b[index] = byte0;
        index++;
        return this;
    }

    public int length() {
        return index;
    }

    public byte[] build() {
        return b;
    }

    @Override
    public String toString() {
        return string();
    }

    public String string() {
        return new String(b, 0, index);
    }
}
