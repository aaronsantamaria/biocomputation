package data1_data2;

/**
 *
 * @author aaronsantamaria
 */
public class Data {

    public Data() {
    }

    static int Vars = 5;

    private int[] variables = new int[Vars]; ///////////////////////////////////

    public int[] getVariables() {
        return variables;
    }

    public void setVariables(int[] variables) {
        this.variables = variables;
    }

    public int getVariables(int index) {
        return this.variables[index];
    }

    public void setVariables(int index, int variables) {
        this.variables[index] = variables;
    }

    private int class1; ////////////////////////////////////////////////////////

    public int getClass1() {
        return class1;
    }

    public void setClass1(int class1) {
        this.class1 = class1;
    }

    public String printData() {
        String data = "";
        for (int i = 0; i < Vars; i++) {
            data += (variables[i] + "");
        }
        data += (" " + class1);
        return data;
    }

}
