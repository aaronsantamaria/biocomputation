package data1_data2;

/**
 *
 * @author aaronsantamaria
 */
public class Rule {

    public Rule() {
    }

    static int ConL = 5;

    public int[] cond = new int[ConL];/////////////////////////////////////////

    public int[] getCond() {
        return cond;
    }

    public void setCond(int[] cond) {
        this.cond = cond;
    }

    public int getCond(int index) {
        return this.cond[index];
    }

    public void setCond(int index, int cond) {
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
