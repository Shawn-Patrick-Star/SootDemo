package BasicDemos;

import soot.Body;
import soot.SootClass;
import soot.SootMethod;

import java.util.List;

public class LoadJavaClass {

    public static String dirpath = "sootOutput";

    public static void main(String[] args) {

        String className = "BasicDemos.examples.HelloSoot";
        Utils.initSootEnv();
        SootClass seedClass = Utils.loadClass(className);
        List<SootMethod> sootMethods = seedClass.getMethods();
        //print all methods in the class
        for (SootMethod sootMethod : sootMethods) {
            Body sootMethodBody = sootMethod.retrieveActiveBody();
            System.out.println(sootMethodBody.getUnits());
            System.out.println("size of units: " + sootMethodBody.getUnits().size());
        }

        Utils.saveClassToPath(seedClass, dirpath);
    }
}
