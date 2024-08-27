package BasicDemos.Work;

import BasicDemos.Utility.Utility;
import BasicDemos.Utils;
import soot.*;
import soot.jimple.*;

import java.util.ArrayList;
import java.util.List;

public class AddDo_WhileStmt {
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

                List<Unit> doWhileContent = Utility.GetPrintUnits(sootMethodBody, "do while block", "doWhileVar");
                List<Unit> unitsList = Utility.GetDo_WhileUnits(sootMethodBody, doWhileContent);
                units.insertBefore(unitsList, validUnits.get(0));

            }
        }
        Utils.saveClassToPath(seedClass, dirpath);

    }
}
