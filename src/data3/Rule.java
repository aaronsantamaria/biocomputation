package data3;

/**
 *
 * @author aaronsantamaria
 */
public class Rule {

    public Rule() {
    }

    static int ConL = 12;

    public float[] cond = new float[ConL];/////////////////////////////////////////

    public float[] getCond() {
        return cond;
    }

    public void setCond(float[] cond) {
        this.cond = cond;
    }

    public float getCond(int index) {
        return this.cond[index];
    }

    public void setCond(int index, float cond) {
        this.cond[index] = cond;
    }

    public int out;///////////////////////////////////////////////////////////

    public int getOut() {
        return out;
    }

    public void setOut(int out) {
        this.out = out;
    }

}
