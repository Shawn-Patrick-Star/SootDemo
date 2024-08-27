package BasicDemos;

import soot.*;
import soot.jimple.AssignStmt;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.StringConstant;

import java.util.ArrayList;
import java.util.List;

public class exeTest {

    private static String dirpath = "sootOutput";

    public static void main(String[] args) {
        String className = "BasicDemos.examples.test";
        Utils.initSootEnv();
        SootClass seedClass = Utils.loadClass(className);
        Utils.saveClassToPath(seedClass, dirpath);
    }
}
