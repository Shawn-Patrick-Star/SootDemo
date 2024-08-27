package BasicDemos.Work;

import BasicDemos.Utility.Utility;
import BasicDemos.Utils;
import soot.*;
import soot.jimple.*;

import java.util.ArrayList;
import java.util.List;

public class AddForStmt {
    public static String dirpath = "sootOutput";

    public static void main(String[] args) {

        String className = "BasicDemos.examples.HelloSoot";
        Utils.initSootEnv();
        SootClass seedClass = Utils.loadClass(className);
        List<SootMethod> sootMethods = seedClass.getMethods();

        for (SootMethod sootMethod : sootMethods) {

            Body sootMethodBody = sootMethod.retrieveActiveBody();
            if (sootMethod.getName().equals("main")){

                UnitPatchingChain units = sootMethodBody.getUnits();

                List<Unit> validUnits = new ArrayList<>();
                for (Unit unit : sootMethodBody.getUnits()) {
                    if (! unit.toString().contains(":= @")){
                        validUnits.add(unit);
                    }
                }

                List<Unit> LoopContent = Utility.GetPrintUnits(sootMethodBody, "for loop", "out1");
                List<Unit> forUnits = Utility.GetForUnits(sootMethodBody, LoopContent);
                units.insertBefore(forUnits, validUnits.get(0));


            }
        }
        Utils.saveClassToPath(seedClass, dirpath);

    }
}
