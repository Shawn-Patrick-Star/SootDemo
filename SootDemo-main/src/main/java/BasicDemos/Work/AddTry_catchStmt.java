package BasicDemos.Work;

import BasicDemos.Utility.Utility;
import BasicDemos.Utils;
import soot.*;

import java.util.ArrayList;
import java.util.List;


public class AddTry_catchStmt {
    private static final String dirpath = "sootOutput";

    public static void main(String[] args) {
        String className = "BasicDemos.examples.HelloSoot";
        Utils.initSootEnv();
        SootClass seedClass = Utils.loadClass(className);
        assert seedClass != null;
        List<SootMethod> sootMethods = seedClass.getMethods();

        for (SootMethod sootMethod : sootMethods) {

            Body sootMethodBody = sootMethod.retrieveActiveBody();
            if (sootMethod.getName().equals("main")){

                UnitPatchingChain units = sootMethodBody.getUnits();

                List<Unit> validUnits = new ArrayList<>();
                for (Unit unit : units) {
                    if (! unit.toString().contains(":= @")){
                        validUnits.add(unit);
                    }
                }

                List<Unit> tryContent = Utility.GetPrintUnits(sootMethodBody, "try block", "tryVar");
                List<Unit> catchContent = Utility.GetPrintUnits(sootMethodBody, "RuntimeException caught!", "catchVar");

                List<Unit> unitsList = Utility.GetTryUnits(sootMethodBody, tryContent, catchContent);
                units.insertBefore(unitsList, validUnits.get(0));
            }
        }

        Utils.saveClassToPath(seedClass, dirpath);
    }


    public static class AddMethod {
    }
}